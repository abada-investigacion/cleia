/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.task;

import com.abada.bpm.console.server.integration.AbadaTaskManagement;
import java.net.URL;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jboss.bpm.console.client.model.TaskRefWrapper;
import org.jboss.bpm.console.server.integration.TaskManagement;
import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/tasks")
public class AbadaTaskController {

    @Autowired
    private AbadaTaskManagement taskManagementAbada;
    @Autowired
    private TaskManagement taskManagement;
    @Autowired
    private FormDispatcherPlugin formDispatcherPlugin;

    /**
     * Return the tasks for a given patient that have to be completed by user.
     *
     * @param userId User id.
     * @param patientId Patient id.
     * @return Return the tasks for a given patient that have to be completed by
     * user.
     */
    @RequestMapping(value = "/patient/{patientId}/user/{userId}", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public TaskRefWrapper getTaskForPatient(@PathVariable Long patientId, @PathVariable String userId) {
        return new TaskRefWrapper(processTaskListResponse(taskManagementAbada.getTaskByUser(patientId, userId)));
    }

    /**
     * Return the tasks for a given processInstance that have to completed by
     * user.
     *
     * @param userId User id.
     * @param processInstanceId ProcessInstance id.
     * @return Return the tasks for a given processInstance that have to
     * completed by user.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/process/{processInstanceId}/user/{userId}", method = RequestMethod.GET)
    public TaskRefWrapper getTaskForProcessInstanceAndSubProcess(@PathVariable Long processInstanceId, @PathVariable String userId) {
        return new TaskRefWrapper(processTaskListResponse(taskManagementAbada.getTaskForProcessInstanceAndSubProcess(processInstanceId, userId)));
    }

    /**
     * Return the tasks that have to be completed by user.
     *
     * @param userId User id.
     * @return Return the tasks that have to be completed by user.
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public TaskRefWrapper getAssignedTasks(@PathVariable String userId) {
        return new TaskRefWrapper(processTaskListResponse(taskManagement.getAssignedTasks(userId)));
    }

    /**
     * Return the tasks that have to be completed by user and the groups you
     * belong to.
     *
     * @param userId User id.
     * @return Return the tasks that have to be completed by user and the groups
     * you belong to.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{userId}/participation", method = RequestMethod.GET)
    public TaskRefWrapper getTasksForIdRefPaticipation(@PathVariable String userId) {
        return new TaskRefWrapper(processTaskListResponse(taskManagement.getUnassignedTasks(userId, null)));
    }

    /**
     * Assign user to task
     *
     * @param taskId Task id.
     * @param idRef User id.
     * @param request Do nothing.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{taskId}/assign/{idRef}", method = RequestMethod.POST)
    public void assignTask(@PathVariable Long taskId, @PathVariable String idRef, HttpServletRequest request) {
        taskManagement.assignTask(taskId, idRef, request.getUserPrincipal().getName());
    }

    /**
     * Release Task
     *
     * @param taskId Task id.
     * @param request Do nothing.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{taskId}/release", method = RequestMethod.POST)
    public void releaseTask(@PathVariable Long taskId, HttpServletRequest request) {
        taskManagement.assignTask(taskId, null, request.getUserPrincipal().getName());
    }

    /**
     * Close a task with the passed params in the http request.
     *
     * @param taskid Task id.
     * @param request
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{taskid}/close", method = RequestMethod.POST)
    public void close(@PathVariable Long taskid, HttpServletRequest request) {
        taskManagement.completeTask(taskid, com.abada.web.util.URL.parseRequest(request), request.getUserPrincipal().getName());
    }

    /**
     * Completing a task by passing data.
     *
     * @param taskid Task id.
     * @param outcome Reason comment.
     * @param request Do nothing.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{taskid}/close/{outcome}", method = RequestMethod.POST)
    public void closeTaskWithSignal(@PathVariable Long taskid, @PathVariable String outcome, HttpServletRequest request) {
        taskManagement.completeTask(taskid, outcome, null, request.getUserPrincipal().getName());
    }

    private List<TaskRef> processTaskListResponse(List<TaskRef> taskList) {
        // decorate task form URL if plugin available
        if (formDispatcherPlugin != null) {
            for (TaskRef task : taskList) {
                URL taskFormURL = formDispatcherPlugin.getDispatchUrl(
                        new FormAuthorityRef(String.valueOf(task.getId())));
                if (taskFormURL != null) {
                    task.setUrl(taskFormURL.toExternalForm());
                }
            }
        }
        return taskList;
    }
}
