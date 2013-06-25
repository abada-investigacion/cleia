/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

import com.abada.bpm.console.server.integration.AbadaTaskManagement;
import com.abada.jbpm.integration.console.task.GroupTaskManagement;
import com.abada.jbpm.integration.console.task.PatientTaskManagement;
import com.abada.jbpm.task.dao.TaskDao;
import com.abada.jbpm.task.service.TaskClient;
import com.abada.jbpm.task.service.TaskServiceFactory;
import com.abada.jbpm.task.spring.TaskService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jbpm.integration.console.Transform;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.workitem.wsht.BlockingGetTaskResponseHandler;
import org.jbpm.task.AccessType;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.ContentData;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

/**
 * Sustituye {@link org.jbpm.integration.console.TaskManagement} jbpm
 * 
 * jbpm 5.4.0.Final compliant
 *
 *
 * @author katsu
 */
public class TaskManagement implements AbadaTaskManagement, org.jboss.bpm.console.server.integration.TaskManagement, Observer {

    private static final Log logger = LogFactory.getLog(TaskManagement.class);
    private TaskClient client;
    private TaskDao taskDao;
    private TaskServiceFactory taskClientFactory;
    private GroupTaskManagement groupTaskManagement;
    private AbadaProcessManagementPlugin processManagement;
    private PatientTaskManagement patientTaskManagement;

    //<editor-fold defaultstate="collapsed" desc="Setters">
    public void setTaskClientFactory(TaskServiceFactory taskClientFactory) {
        this.taskClientFactory = taskClientFactory;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public void setGroupTaskManagement(GroupTaskManagement groupTaskManagement) {
        this.groupTaskManagement = groupTaskManagement;
    }

    public void setProcessManagement(AbadaProcessManagementPlugin processManagement) {
        this.processManagement = processManagement;
    }

    public void setPatientTaskManagement(PatientTaskManagement patientTaskManagement) {
        this.patientTaskManagement = patientTaskManagement;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TaskManagement implementation">
    public void connect() {
        if (client == null) {
            try {
                client = taskClientFactory.getTaskClient();
                boolean connected = client.connect();
                if (!connected) {
                    throw new IllegalArgumentException(
                            "Could not connect task client");
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    public TaskRef getTaskById(long taskId) {
        connect();
        BlockingGetTaskResponseHandler responseHandler = new BlockingGetTaskResponseHandler();
        client.getTask(taskId, responseHandler);
        Task task = responseHandler.getTask();
        return Transform.task(task);
    }

    public void assignTask(long taskId, String idRef, String userId) {
        connect();
        BlockingTaskOperationResponseHandler responseHandler = new BlockingTaskOperationResponseHandler();
        if (idRef == null) {
            client.release(taskId, userId, responseHandler);
        } else if (idRef.equals(userId)) {
            List<String> groups = groupTaskManagement.getGroupByUserName(userId);
            if (groups == null) {
                client.claim(taskId, idRef, responseHandler);
            } else {
                client.claim(taskId, idRef, groups, responseHandler);
            }
        } else {
            client.delegate(taskId, userId, idRef, responseHandler);
        }
        responseHandler.waitTillDone(5000);
    }

    @SuppressWarnings("unchecked")
    public void completeTask(long taskId, Map data, String userId) {
        connect();
        BlockingTaskOperationResponseHandler responseHandler = new BlockingTaskOperationResponseHandler();
        client.start(taskId, userId, responseHandler);
        responseHandler.waitTillDone(5000);
        responseHandler = new BlockingTaskOperationResponseHandler();
        ContentData contentData = null;
        if (data != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(data);
                out.close();
                contentData = new ContentData();
                contentData.setContent(bos.toByteArray());
                contentData.setAccessType(AccessType.Inline);
            } catch (IOException e) {
                logger.error(e);
            }
        }
        client.complete(taskId, userId, contentData, responseHandler);
        responseHandler.waitTillDone(5000);
    }

    @SuppressWarnings("unchecked")
    public void completeTask(long taskId, String outcome, Map data, String userId) {
        if ("jbpm_skip_task".equalsIgnoreCase(outcome)) {
            skipTask(taskId, userId);
        } else {
            data.put("outcome", outcome);
            completeTask(taskId, data, userId);
        }
    }

    public void skipTask(long taskId, String userId) {
        connect();
        BlockingTaskOperationResponseHandler response = new BlockingTaskOperationResponseHandler();
        client.skip(taskId, userId, response);
    }

    public void releaseTask(long taskId, String userId) {
        // TODO: this method is not being invoked, it's using
        // assignTask with null parameter instead
        connect();
        BlockingTaskOperationResponseHandler responseHandler = new BlockingTaskOperationResponseHandler();
        client.release(taskId, userId, responseHandler);
        responseHandler.waitTillDone(5000);
    }

    public List<TaskRef> getAssignedTasks(String idRef) {
        connect();
        List<TaskRef> result = new ArrayList<TaskRef>();
        try {
            BlockingTaskSummaryResponseHandler responseHandler = new BlockingTaskSummaryResponseHandler();
            client.getTasksOwned(idRef, "en-UK", responseHandler);
            List<TaskSummary> tasks = responseHandler.getResults();
            for (TaskSummary task : tasks) {
                if (task.getStatus() == Status.Reserved) {
                    result.add(taskDao.fillPotencialsOwner(Transform.task(task)));
                }
            }
        } catch (Throwable t) {
            logger.error(t);
        }
        return result;
    }

    public List<TaskRef> getUnassignedTasks(String idRef, String participationType) {
        // TODO participationType ?
        connect();
        List<TaskRef> result = new ArrayList<TaskRef>();
        try {
            BlockingTaskSummaryResponseHandler responseHandler = new BlockingTaskSummaryResponseHandler();
            List<String> groups = groupTaskManagement.getGroupByUserName(idRef);
            if (groups == null) {
                client.getTasksAssignedAsPotentialOwner(idRef, "en-UK", responseHandler);
            } else {
                client.getTasksAssignedAsPotentialOwner(idRef, groups, "en-UK", responseHandler);
            }
            List<TaskSummary> tasks = responseHandler.getResults();
            for (TaskSummary task : tasks) {
                result.add(taskDao.fillPotencialsOwner(Transform.task(task)));
            }
        } catch (Throwable t) {
            logger.error(t);
        }
        return result;
    }

    public void update(Observable o, Object o1) {
        if (o instanceof TaskService && o1 instanceof String) {
            if (TaskService.BEFORE_STOP_EVENT.equals(o1)) {
                try {
                    client.disconnect();
                    logger.debug("Disconnect client.");
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AbadaTaskManagement implementation">
    public List<TaskRef> getTaskByUser(Long patientId, String userId) {
        List<Long> pids = patientTaskManagement.getProcessInstancesIdsForPatient(patientId);
        List<TaskRef> result = new ArrayList<TaskRef>();
        List<TaskRef> tasks = this.getUnassignedTasks(userId, null);
        List<ProcessInstanceLog> instanceLogs;
        if (pids != null && !pids.isEmpty()) {
            for (Long id : pids) {
                instanceLogs = processManagement.getProcessInstanceTreeExecution(id);
                for (TaskRef tr : tasks) {
                    if (contains(instanceLogs, tr.getProcessInstanceId())) {
                        result.add(tr);
                    }
                }
            }
        }
        return result;
    }

    public List<TaskRef> getTaskForProcessInstanceAndSubProcess(Long processInstance, String userId) {
        List<ProcessInstanceLog> instanceLogs = processManagement.getProcessInstanceTreeExecution(processInstance);
        List<TaskRef> result = new ArrayList<TaskRef>();
        if (instanceLogs != null && !instanceLogs.isEmpty()) {
            List<TaskRef> tasks = this.getUnassignedTasks(userId, null);
            if (tasks != null && !tasks.isEmpty()) {
                for (TaskRef tr : tasks) {
                    if (contains(instanceLogs, tr.getProcessInstanceId())) {
                        result.add(tr);
                    }
                }
            }
        }
        return result;
    }

    private boolean contains(List<ProcessInstanceLog> processInstancesLog, String processInstanceId) {
        for (ProcessInstanceLog pil : processInstancesLog) {
            if (processInstanceId.equals("" + pil.getProcessInstanceId())) {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>
}
