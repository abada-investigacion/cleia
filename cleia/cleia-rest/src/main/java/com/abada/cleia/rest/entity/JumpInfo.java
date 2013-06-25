/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.entity;

import com.abada.jbpm.version.model.NodeMap;

/**
 *
 * @author katsu
 */
public class JumpInfo {
    private String observation;
    private boolean fixProcess;
    private NodeMap [] nodes;

    public NodeMap[] getNodes() {
        return nodes;
    }

    public void setNodes(NodeMap[] nodes) {
        this.nodes = nodes;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public boolean isFixProcess() {
        return fixProcess;
    }

    public void setFixProcess(boolean fixProcess) {
        this.fixProcess = fixProcess;
    }        
}
