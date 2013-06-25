/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.client.model;

/**
 *
 * @author katsu
 */
public class DiagramNodeInfo extends org.jboss.bpm.console.client.model.DiagramNodeInfo{
    private Long id;
    private Long nodeInstanceId;
    private String processId;
    private Long processInstanceId;

    public DiagramNodeInfo(String name, int x, int y, int width, int height, Long id, Long nodeInstanceId) {
        this(id, nodeInstanceId, null, null, name, x, y, width, height);                
    }                

    public DiagramNodeInfo(Long id, Long nodeInstanceId, String processId, Long processInstanceId, String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.id = id;
        this.nodeInstanceId = nodeInstanceId;
        this.processId = processId;
        this.processInstanceId = processInstanceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNodeInstanceId() {
        return nodeInstanceId;
    }

    public void setNodeInstanceId(Long nodeInstanceId) {
        this.nodeInstanceId = nodeInstanceId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }        
}
