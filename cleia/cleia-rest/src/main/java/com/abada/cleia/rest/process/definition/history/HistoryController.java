/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.definition.history;

import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.jboss.bpm.console.client.model.ActiveNodeInfo;
import org.jboss.bpm.console.server.plugin.GraphViewerPlugin;
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
@RequestMapping("/rs/process/definition/history")
public class HistoryController {

    @Autowired
    private GraphViewerPlugin graphViewerPlugin;

    /**
     * Collects node information (coordinates) for certain selected activities and processes
     * @param id Process definition id which nodes information should be retrieved for
     * @param activities list of activity names treated as a filter
     * @return Return a list of found node information. Can be empty list if no definition was found
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER"})    
    @RequestMapping(value = "/{id}/nodeInfo", method = RequestMethod.GET)
    public List<ActiveNodeInfo> getHistoryActiveNodeInfo(@PathVariable Long id, String[] activities) {
        return graphViewerPlugin.getNodeInfoForActivities(id.toString(), Arrays.asList(activities));
    }
}
