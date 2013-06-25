/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.process.instance.event;

import com.abada.jbpm.integration.console.ProcessManagement;
import com.abada.web.util.URL;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/process/instance/event")
public class EventsController {

    @Autowired
    private ProcessManagement processManagement;

    /**
     * Raising an event in a given process instance setting some values. Values
     * must are contains in a http request.
     *
     * @param id ProcessInstance id.
     * @param request The values passed to the event must be contained in the
     * http request. By POST.
     * @param type Type of signal.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/transition/special", method = RequestMethod.POST)
    public void signal(@PathVariable Long id, HttpServletRequest request, String type, String processId) {
        Map<String, Object> params = URL.parseRequest(request);
        params.remove("type");
        params.remove("processId");
        processManagement.signalExecution(id, processId, params, type);
    }
    
    /**
     * Raising an event in a given process instance setting some values. Values
     * must are contains in a http request.
     *
     * @param id ProcessInstance id.
     * @param request The values passed to the event must be contained in the
     * http request. By POST.
     * @param type Type of signal.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/transition", method = RequestMethod.POST)
    public void signal(@PathVariable Long id, HttpServletRequest request, @RequestParam(required = false) String type) {
        Map<String, Object> params = URL.parseRequest(request);
        params.remove("type");
        if (type != null) {
            processManagement.signalExecution(id.toString(), params, type);
        } else {
            processManagement.signalExecution(id.toString(), params);
        }
    }

    /**
     * Raising an event of an instance of a process.
     *
     * @param id ProcessInstance id
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{id}/transition/default", method = RequestMethod.POST)
    public void signal(@PathVariable Long id) {
        processManagement.signalExecution(id.toString(), null, "signal");
    }
}
