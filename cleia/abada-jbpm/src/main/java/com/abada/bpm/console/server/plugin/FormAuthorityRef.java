/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.server.plugin;

/**
 *Sustituye {@link org.jboss.bpm.console.server.plugin.FormAuthorityRef}
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class FormAuthorityRef extends org.jboss.bpm.console.server.plugin.FormAuthorityRef{

    private String device;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    
    public FormAuthorityRef(String referenceId,String device) {
        this(referenceId,device,Type.TASK);
    }

    public FormAuthorityRef(String referenceId,String device, Type currentType) {
        super(referenceId, currentType);
        this.setDevice(device);
    }    
}
