/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.definition.history;

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

import com.abada.cleia.entity.user.Views;
import com.abada.springframework.web.servlet.view.JsonView;
import java.util.Arrays;
import javax.annotation.security.RolesAllowed;
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
@RequestMapping("/rs/process/definition/history")
public class HistoryController {

    @Autowired
    private GraphViewerPlugin graphViewerPlugin;

    /**
     * Collects node information (coordinates) for certain selected activities
     * and processes
     *
     * @param id Process definition id which nodes information should be
     * retrieved for
     * @param activities list of activity names treated as a filter
     * @return Return a list of found node information. Can be empty list if no
     * definition was found
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/nodeInfo", method = RequestMethod.GET)
    public void getHistoryActiveNodeInfo(@PathVariable Long id, String[] activities, Model model) {
        model.addAttribute(JsonView.JSON_VIEW_RESULT, graphViewerPlugin.getNodeInfoForActivities(id.toString(), Arrays.asList(activities)));
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
}
