/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

import com.abada.utils.Constants;
import javax.persistence.Entity;
import javax.persistence.Lob;
import org.jbpm.process.audit.NodeInstanceLog;

/**
 *
 * @author katsu
 */
@Entity
public class NodeInstanceLogExt extends NodeInstanceLog{

    /**
     * Observations
     */
    @Lob    
    private String observation;
    
    NodeInstanceLogExt() {
        super(-1, -1, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING);
    }

    public NodeInstanceLogExt(int type, long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName,String observation) {
        super(type, processInstanceId, processId, nodeInstanceId, nodeId, nodeName);
        this.setObservation(observation);
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }        
}
