/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console.graph;

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
