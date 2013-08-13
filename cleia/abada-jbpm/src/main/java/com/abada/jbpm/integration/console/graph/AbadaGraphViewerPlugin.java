/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console.graph;

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

import java.util.Date;
import java.util.List;
import org.jboss.bpm.console.client.model.ActiveNodeInfo;
import org.jboss.bpm.console.server.plugin.GraphViewerPlugin;

/**
 *
 * 
 * @author katsu
 */
public interface AbadaGraphViewerPlugin extends GraphViewerPlugin{
    /**
     * Return an image with the status of a processInstanceId
     * 
     * @param processInstanceId
     * @return 
     */
    public byte [] getProcessInstanceStatusImage(Long processInstanceId);
    
    /**
     * Return an image with the number of people that are in each status from a
     * custom range of time.
     * @param processId
     * @param start
     * @param end
     * @return 
     */
    public byte [] getProcessStatusImage(String processId,Date start,Date end);
    
    /**
     * Return all activate nodes in process and subprocess
     * @param processInstanceId
     * @return 
     */
    public List<ActiveNodeInfo> getAllActivateNodes(Long processInstanceId);
}
