/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.form;

/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.abada.bpm.console.server.plugin.FormAuthorityRef;
import com.abada.springframework.web.servlet.view.InputStreamView;
import com.abada.web.util.URL;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.jboss.bpm.console.server.integration.TaskManagement;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
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
     * Displays a form of a human task. Used to display an html form of the
     * passed human task id.
     *
     * @param taskid Task id
     * @param request Do nothing.
     * @return Return the html to show in a iframe in the client interface.
     * @throws IOException
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{taskid}/render", method = RequestMethod.GET)
    public View renderTask(@PathVariable String taskid, HttpServletRequest request, Device device) throws IOException {
        com.abada.springframework.web.servlet.menu.Device deviceAux;
        if (device.isMobile()) {
            deviceAux = com.abada.springframework.web.servlet.menu.Device.MOBILE;
        } else if (device.isNormal()) {
            deviceAux = com.abada.springframework.web.servlet.menu.Device.DESKTOP;
        } else {
            deviceAux = com.abada.springframework.web.servlet.menu.Device.TABLET;
        }
        return new InputStreamView(formDispatcherPlugin.provideForm(new FormAuthorityRef(taskid,deviceAux.name())).getInputStream(), "text/html", null);
    }

    /**
     * Submit and complete the human task of the form showed in the
     * renderProcess method.
     *
     * @param taskid Task id
     * @param request Do nothing.
     * @param model Do nothing.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
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
