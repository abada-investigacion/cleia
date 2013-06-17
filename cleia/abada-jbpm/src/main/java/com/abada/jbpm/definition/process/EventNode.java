/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

/**
 * EventNode information
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class EventNode extends Node{
    private String eventType;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    
}
