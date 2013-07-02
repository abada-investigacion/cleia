/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.instance;

import com.abada.cleia.entity.user.Views;
import com.abada.cleia.rest.entity.JumpInfo;
import com.abada.extjs.ComboBoxResponse;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.jbpm.integration.console.AbadaProcessManagementPlugin;
import com.abada.jbpm.integration.console.ProcessManagement;
import com.abada.jbpm.integration.console.graph.AbadaGraphViewerPlugin;
import com.abada.jbpm.process.audit.ProcessEventListener;
import com.abada.jbpm.runtime.process.JumpCommand;
import com.abada.jbpm.version.VersionCommand;
import com.abada.jbpm.version.dao.ProcessInstanceInfoDao;
import com.abada.springframework.web.servlet.view.JsonView;
import com.abada.springframework.web.servlet.view.OutputStreamView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.bpm.console.client.model.ActiveNodeInfo;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/process/instance")
public class InstanceController {

    private static final Log logger = LogFactory.getLog(InstanceController.class);
    @Autowired
    private AbadaGraphViewerPlugin graphViewerPlugin;
    @Autowired
    private AbadaProcessManagementPlugin processManagementAbada;
    @Autowired
    private StatefulKnowledgeSession ksession1;
    @Autowired
    private ProcessManagement processManagement;
    @Autowired(required = false)
    private ProcessEventListener processEventListener;
    @Autowired
    private ProcessInstanceInfoDao processInstanceInfoDao;

    /**
     * Displays the diagram image for a given process instance by id. The image
     * have draw the execution secuence of the process instance.
     *
     * @param id ProcessInstance id
     * @return Return an image.
     * @throws IOException
     */
    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public OutputStreamView getProcessImage(@PathVariable Long id) throws IOException {
        byte[] result = graphViewerPlugin.getProcessInstanceStatusImage(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, id + "-image.png", "image/png");
    }

    /**
     * Gets the instance tree with all its processes and threads.
     *
     * @param id ProcessInstance id
     * @return Return a list of subprocess instances from parent process
     * instance. Result contain parent process instance information.
     * @throws IOException
     */
    @RequestMapping(value = "/{id}/tree", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void getProcessInstances(@PathVariable Long id, Model model) throws IOException {
        ExtjsStore result = new ExtjsStore();
        result.setData(processManagementAbada.getProcessInstanceTreeExecution(id));
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Gets the execution history of the nodes in a process instance.
     *
     * @param id ProcessInstance id
     * @return Return a NodeInstanceLog structure.
     * @throws IOException
     */
    @RequestMapping(value = "/{id}/history", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void getNodeHistory(@PathVariable Long id, Model model) throws IOException {
        ExtjsStore result = new ExtjsStore();
        result.setData(processManagementAbada.getNodeHistoryList(id));
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Jump inside the running process
     *
     * @param processName Process Name (id in Process definition) where nodeId
     * belong
     * @param nodeid Node id of nodet to jump
     * @param id Process Id of running instance
     * @return Return success structure.
     */
    @RequestMapping(value = "/{id}/jump", method = RequestMethod.POST)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public Success doJump(@PathVariable Long id, @RequestBody JumpInfo jumpInfo) {
        Success result = new Success(Boolean.FALSE);
        try {
            JumpCommand command = new JumpCommand();
            command.setProcessInstanceId(id);
            command.setNodeMaps(Arrays.asList(jumpInfo.getNodes()));
            command.setProcessEventListener(processEventListener);
            command.setObservation(jumpInfo.getObservation());
            result.setSuccess(((CommandBasedStatefulKnowledgeSession) ksession1).getCommandService().execute(command));
        } catch (Exception e) {
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * update in a process instance to another version of BPMN, Note:
     * Content-Type header attribute must be text/plain but request body must
     * have json
     *
     * @param processid Id of process instance
     * @param id Process Id of running instance
     * @return Return success structure.
     */
    @RequestMapping(value = "/{processInstanceid}/{processid}/version", method = RequestMethod.POST)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public Success version(@PathVariable Long processInstanceid, @PathVariable String processid, @RequestBody JumpInfo jumpInfo) {
        Success result = new Success(Boolean.FALSE);
        try {
            VersionCommand command = new VersionCommand();
            command.setNodeMapping(Arrays.asList(jumpInfo.getNodes()));
            command.setProcessid(processid);
            command.setProcessInstanceId(processInstanceid);
            command.setProcessInstanceInfoDao(processInstanceInfoDao);
            command.setProcessEventListener(processEventListener);
            command.setObservation(jumpInfo.getObservation());
            command.setFixProcess(jumpInfo.isFixProcess());
            result.setSuccess(((CommandBasedStatefulKnowledgeSession) ksession1).getCommandService().execute(command));
        } catch (Exception e) {
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * Return variables of an process instance in Extjs
     *
     * @param id ProcessInstance id
     * @return Return a list of ComboBoxResponse structure.
     */
    @RequestMapping(value = "/{id}/variables/extjs", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void getVariablesExtjs(@PathVariable Long id, Model model) {
        Map<String, Object> aux = processManagement.getInstanceData(id.toString());
        ExtjsStore<ComboBoxResponse<Object>> result = new ExtjsStore<ComboBoxResponse<Object>>();
        result.setData(new ArrayList<ComboBoxResponse<Object>>());

        for (String k : aux.keySet()) {
            ComboBoxResponse<Object> a = new ComboBoxResponse<Object>();
            a.setId(k);
            a.setValue(aux.get(k));
            result.getData().add(a);
        }
        result.setTotal(result.getData().size());
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return a list of active nodes for a process instance and all subprocess
     * instance.
     *
     * @param id ProcessInstance id
     * @return Return a list of ActiveNodeInfo structure.
     */
    @RequestMapping(value = "/{id}/allActiveNodeInfo", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void getAllActiveNodeInfo(@PathVariable Long id, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, graphViewerPlugin.getAllActivateNodes(id));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    //
    /**
     * Change state of a process to the next
     *
     * @param id ProcessInstance id
     * @param next Next state
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/state/{next}", method = RequestMethod.POST)
    public void changeStatus(@PathVariable Long id, @PathVariable ProcessInstanceRef.STATE next) {
        processManagement.setProcessState(id.toString(), next);
    }

    /**
     * Return a list of active nodes for a process instance.
     *
     * @param id ProcessInstance id
     * @return Return a list of ActiveNodeInfo structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/activeNodeInfo", method = RequestMethod.GET)
    public void getActiveNodeInfo(@PathVariable Long id, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, graphViewerPlugin.getActiveNodeInfo(id.toString()));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Finish a process instance with a set result state.
     *
     * @param id ProcessInstance id.
     * @param result Result state for finished process instance.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/end/{result}", method = RequestMethod.POST)
    public void endInstance(@PathVariable Long id, @PathVariable ProcessInstanceRef.RESULT result) {
        processManagement.endInstance(id.toString(), result);
    }

    /**
     * Finish the instance of a process
     *
     * @param id ProcessInstance id.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public void endInstance(@PathVariable Long id) {
        processManagement.deleteInstance(id.toString());
    }

    /**
     * Return variables of a instance process
     *
     * @param id Processinstance id.
     * @return Return variables of a instance process.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/variables", method = RequestMethod.GET)
    public void getVariables(@PathVariable Long id,Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, processManagement.getInstanceData(id.toString()));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);        
    }
}
