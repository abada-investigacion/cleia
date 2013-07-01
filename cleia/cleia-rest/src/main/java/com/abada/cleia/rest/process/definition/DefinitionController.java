/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.definition;

import com.abada.cleia.entity.user.Views;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.SimpleGroupingComboBoxResponse;
import com.abada.jbpm.definition.process.FindEventNodes;
import com.abada.jbpm.definition.process.Node;
import com.abada.jbpm.definition.process.NodeListCommand;
import com.abada.jbpm.definition.process.ProcessTreeCommand;
import com.abada.jbpm.integration.console.ProcessManagement;
import com.abada.springframework.web.servlet.view.JsonView;
import com.abada.springframework.web.servlet.view.OutputStreamView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.impl.CommandBasedStatefulKnowledgeSession;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.bpm.console.client.model.DiagramInfo;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessDefinitionRefWrapper;
import org.jboss.bpm.console.client.model.ProcessInstanceRefWrapper;
import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;
import org.jboss.bpm.console.server.plugin.GraphViewerPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/process/definition")
public class DefinitionController {

    private static final Log logger = LogFactory.getLog(DefinitionController.class);
    @Autowired
    private ProcessManagement processManagement;
    @Autowired
    private GraphViewerPlugin graphViewerPlugin;
    @Autowired
    private FormDispatcherPlugin formDispatcherPlugin;
    @Autowired
    private StatefulKnowledgeSession ksession1;

    /**
     * Gets diagramInfo with information such as width, height, nodes.
     *
     * @param processName Proccess name.
     * @return Return DiagramInfo structure.
     * @throws IOException
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{processName}/diagram", method = RequestMethod.GET)
    public void getDiagramInfo(@PathVariable String processName, Model model) throws IOException {
        DiagramInfo result = graphViewerPlugin.getDiagramInfo(processName);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return node definitions of a process
     *
     * @param processName Process Name (id in Process definition)
     * @return Return a list of Node.
     */
    @RequestMapping(value = "/{processName}/nodes", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    public void getNodes(@PathVariable String processName, Model model) {
        NodeListCommand command = new NodeListCommand();
        command.setProcessId(processName);
        List<Node> result = ((CommandBasedStatefulKnowledgeSession) ksession1).getCommandService().execute(command);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return event nodes definitions of a process.
     *
     * @param processName Process Name (id in Process definition)
     * @return Return a list of Node.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{processName}/eventnodes", method = RequestMethod.GET)
    public void getEventNodes(@PathVariable String processName, Model model) {
        FindEventNodes command = new FindEventNodes();
        command.setProcessId(processName);
        List<Node> result = ((CommandBasedStatefulKnowledgeSession) ksession1).getCommandService().execute(command);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return a list of subprocess (processId) of a parent process. The list
     * returned contains processId parent.
     *
     * @param processId Process Id (id in Process definition)
     * @return Return a list of subprocess (processId) of a parent process. The
     * list returned contains processId parent.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{processId}/tree", method = RequestMethod.GET)
    public void getProcessIdTree(@PathVariable String processId, Model model) {
        ProcessTreeCommand command = new ProcessTreeCommand();
        command.setProcessId(processId);
        List<String> result = ((CommandBasedStatefulKnowledgeSession) ksession1).getCommandService().execute(command);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    //
    /**
     * Start a process instance by process name.
     *
     * @param processName Process Name (id in Process definition)
     * @param request Do nothing
     * @return Return ProcessInstanceRef structure.
     */
    @RequestMapping(value = "/{processName}/new_instance", method = RequestMethod.POST)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void startInstance(@PathVariable String processName, HttpServletRequest request, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, processManagement.newInstance(processName, com.abada.web.util.URL.parseRequest(request)));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Displays the diagram image for a given process by name.
     *
     * @param processName Process name.
     * @return Return an image by byte array.
     * @throws IOException
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{processName}/image", method = RequestMethod.GET)
    public OutputStreamView getProcessImage(@PathVariable String processName) throws IOException {
        byte[] result = graphViewerPlugin.getProcessImage(processName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result);
        return new OutputStreamView(out, processName + "-image.png", "image/png");
    }

    /*@RequestMapping(value = "/rs/process/definition/{processName}/image/{instance}", method = RequestMethod.GET)
     public OutputStreamView getProcessInstanceImage(@PathVariable String processName,@PathVariable String instance) throws IOException {
     byte [] result=
     ByteArrayOutputStream out=new ByteArrayOutputStream();
     out.write(result);
     return new OutputStreamView(out,processName+"-image.png","image/png");
     }*/
    /**
     * Return all knowledge process
     *
     * @return Return all knowledge process.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listResources(Model model) {
        List<ProcessDefinitionRef> processDefinitions = processManagement.getProcessDefinitions();
        model.addAttribute(JsonView.JSON_VIEW_RESULT, new ProcessDefinitionRefWrapper(decorateProcessDefintions(processDefinitions)));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return all knowledge process
     *
     * @return Return all knowledge process.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list/combo", method = RequestMethod.GET)
    public void listResourcesCombo(Model model) {
        List<ProcessDefinitionRef> processDefinitions = processManagement.getProcessDefinitions();
        ProcessDefinitionRefWrapper aux = new ProcessDefinitionRefWrapper(decorateProcessDefintions(processDefinitions));

        if (aux != null && aux.getDefinitions() != null && !aux.getDefinitions().isEmpty()) {
            List<SimpleGroupingComboBoxResponse> result = new ArrayList<SimpleGroupingComboBoxResponse>();
            for (ProcessDefinitionRef pdr : aux.getDefinitions()) {
                SimpleGroupingComboBoxResponse cbr = new SimpleGroupingComboBoxResponse();
                cbr.setId(pdr.getId());
                cbr.setValue(pdr.getName());
                cbr.setGroupingField(pdr.getPackageName());
                result.add(cbr);
            }
            ExtjsStore<SimpleGroupingComboBoxResponse> r = new ExtjsStore<SimpleGroupingComboBoxResponse>();
            r.setData(result);
            model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
            model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
        }
    }

    /**
     * Remove a process from knowledge base.
     *
     * @param processName Process name
     * @return Return a list of process after remove.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{processName}/remove", method = RequestMethod.POST)
    public void removeProcess(@PathVariable String processName, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, new ProcessDefinitionRefWrapper(processManagement.removeProcessDefinition(processName)));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return all process instances
     *
     * @param processName Process name
     * @return Return all process instances.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{processName}/instances", method = RequestMethod.GET)
    public void getInstances(@PathVariable String processName, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, new ProcessInstanceRefWrapper(processManagement.getProcessInstances(processName)));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Enriches {@link org.jboss.bpm.console.client.model.ProcessDefinitionRef}
     * with form and diagram URLs if applicable.
     */
    private List<ProcessDefinitionRef> decorateProcessDefintions(List<ProcessDefinitionRef> processDefinitions) {
        // decorate process form URL if plugin available
        if (this.formDispatcherPlugin != null) {
            for (ProcessDefinitionRef def : processDefinitions) {
                URL processFormURL = this.formDispatcherPlugin.getDispatchUrl(
                        new FormAuthorityRef(def.getId(), FormAuthorityRef.Type.PROCESS));
                if (processFormURL != null) {
                    def.setFormUrl(processFormURL.toExternalForm());
                }
            }
        }

        // decorate the diagram URL if available    
        if (graphViewerPlugin != null) {
            for (ProcessDefinitionRef def : processDefinitions) {
                URL diagramUrl = graphViewerPlugin.getDiagramURL(def.getId());
                if (diagramUrl != null) {
                    def.setDiagramUrl(diagramUrl.toExternalForm());
                }
            }
        }
        return processDefinitions;
    }
}
