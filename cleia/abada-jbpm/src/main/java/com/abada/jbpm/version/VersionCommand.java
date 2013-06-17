/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.version;

import com.abada.jbpm.definition.process.Node;
import com.abada.jbpm.process.audit.ProcessEventListener;
import com.abada.jbpm.runtime.process.JumpCommand;
import com.abada.jbpm.version.dao.ProcessInstanceInfoDao;
import com.abada.jbpm.version.model.NodeMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.common.InternalKnowledgeRuntime;
import org.drools.definition.process.WorkflowProcess;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.jbpm.workflow.instance.node.SubProcessNodeInstance;

/**
 *
 * @author david
 * @author katsu
 */
public class VersionCommand implements GenericCommand<Boolean> {

    private static final Log logger = LogFactory.getLog(VersionCommand.class);
    /**
     * Process Id
     */
    private Long processInstanceId;
    /**
     * nodemapping
     */
    private List<NodeMap> nodeMapping;
    /**
     * id bpmn
     */
    private String processid;
    private ProcessInstanceInfoDao processInstanceInfoDao;

    public ProcessInstanceInfoDao getProcessInstanceInfoDao() {
        return processInstanceInfoDao;
    }

    public void setProcessInstanceInfoDao(ProcessInstanceInfoDao processInstanceInfoDao) {
        this.processInstanceInfoDao = processInstanceInfoDao;
    }

    public List<NodeMap> getNodeMapping() {
        return nodeMapping;
    }

    public void setNodeMapping(List<NodeMap> nodemapping) {
        this.nodeMapping = nodemapping;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }
    public ProcessEventListener processEventListener;

    public ProcessEventListener getProcessEventListener() {
        return processEventListener;
    }

    public void setProcessEventListener(ProcessEventListener processEventListener) {
        this.processEventListener = processEventListener;
    }
    /**
     * Observation to jump wrote by medic
     */
    private String observation;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    
    private boolean fixProcess;    

    public void setFixProcess(boolean fixProcess) {
        this.fixProcess = fixProcess;
    }

    public VersionCommand() {        
        this.fixProcess=false;        
    }

    /**
     * modification in a process instance to another version of BPMN
     * @param context
     * @return 
     */
    public Boolean execute(Context context) {
        try {
            StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();

            WorkflowProcessInstanceImpl processInstance = (WorkflowProcessInstanceImpl) ksession.getProcessInstance(processInstanceId);
            if (processInstance == null) {
                throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
            }
            if (processid == null) {
                throw new IllegalArgumentException("Null process id");
            }

            WorkflowProcess process = (WorkflowProcess) ksession.getKnowledgeBase().getProcess(processid);
            if (process == null) {
                throw new IllegalArgumentException("Could not find process " + processid);
            }
            if (!fixProcess && processInstance.getProcessId().equals(processid)) {
                return false;
            }

            WorkflowProcess oldProcess = (WorkflowProcess) processInstance.getProcess();

            if (!fixProcess)
                checkSourceNodes(processInstance, nodeMapping,ksession);
            
            changeProcess(processInstance, process, ksession);

            JumpCommand jumpCommand = new JumpCommand(true);
            jumpCommand.setNodeMaps(nodeMapping);
            jumpCommand.setObservation(observation);
            jumpCommand.setProcessEventListener(processEventListener);
            jumpCommand.setProcessInstanceId(processInstanceId);

            boolean status = ksession.execute(jumpCommand);

            if (status) {
                processInstanceInfoDao.updateProcessId(processid, processInstanceId);
            } else {
                changeProcess(processInstance, oldProcess, ksession);
            }

            return status;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    private void changeProcess(WorkflowProcessInstanceImpl processInstance, WorkflowProcess process, StatefulKnowledgeSession ksession) {
        synchronized (processInstance) {
            processInstance.disconnect();
            processInstance.setKnowledgeRuntime((InternalKnowledgeRuntime) ksession);
            processInstance.setProcess(process);
            processInstance.reconnect();
        }
    }

    private void checkSourceNodes(ProcessInstance processInstance, List<NodeMap> nodeMaps,StatefulKnowledgeSession ksession) throws Exception {
        for (NodeMap nm : nodeMaps) {
            if (findNodeInstance(processInstance, nm.getFromNode(),ksession) == null) {
                throw new Exception("Source node not found.");
            }
        }
    }

    private NodeInstance findNodeInstance(ProcessInstance processInstance, Node node,StatefulKnowledgeSession ksession) {
        for (NodeInstance ni : ((WorkflowProcessInstance) processInstance).getNodeInstances()) {
            if (ni.getNode().getId() == node.getId() && processInstance.getProcessId().equals(node.getProcessId())) {
                return ni;
            } else if (ni instanceof SubProcessNodeInstance) {
                return findNodeInstance(ksession.getProcessInstance(((SubProcessNodeInstance) ni).getProcessInstanceId()), node,ksession);
            }
        }
        return null;
    }

}
