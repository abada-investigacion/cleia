/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.form;

import com.abada.springframework.web.servlet.view.InputStreamView;
import com.abada.web.util.URL;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.server.integration.TaskManagement;
import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/form/task")
public class FormTaskController {

    @Autowired
    private FormDispatcherPlugin formDispatcherPlugin;
    @Autowired
    private TaskManagement taskManagement;


    /**
     * Displays a form of a human task. Used to display an html form of the passed human task id.
     * @param taskid Task id
     * @param request Do nothing.
     * @return Return the html to show in a iframe in the client interface.
     * @throws IOException
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER"})
    @RequestMapping(value = "/{taskid}/render", method = RequestMethod.GET)
    public View renderTask(@PathVariable String taskid, HttpServletRequest request) throws IOException {
        return new InputStreamView(formDispatcherPlugin.provideForm(new FormAuthorityRef(taskid)).getInputStream(), "text/html", null);
    }

    /**
     * Submit and complete the human task of the form showed in the renderProcess method.
     * @param taskid Task id
     * @param request Do nothing.
     * @param model Do nothing.
     * @return Return success structure.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER"})
    @RequestMapping(value = "/{taskid}/complete", method = RequestMethod.POST)
    public String completeTask(@PathVariable Long taskid, HttpServletRequest request, Model model) {
        try {
            taskManagement.completeTask(taskid, URL.parseRequest(request), request.getUserPrincipal().getName());
            model.addAttribute("success", Boolean.TRUE.toString());
        } catch (Exception e) {
            model.addAttribute("success", Boolean.FALSE.toString());
            model.addAttribute("error", e.getMessage());
        }
        return "success";
    }
}
