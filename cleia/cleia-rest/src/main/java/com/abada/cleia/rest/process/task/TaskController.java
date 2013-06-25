/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.task;

import java.net.URL;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jboss.bpm.console.client.model.TaskRefWrapper;
import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;
import org.jboss.bpm.console.server.integration.TaskManagement;
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
public class TaskController {

    @Autowired
    private TaskManagement taskManagement;
    @Autowired
    private FormDispatcherPlugin formDispatcherPlugin;

    
}
