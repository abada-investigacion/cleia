/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.hornetq;

import org.jbpm.task.service.BaseClientHandler;
/**
 *
 * @author katsu
 */
public class HornetQTaskClientConnector extends org.jbpm.task.service.hornetq.HornetQTaskClientConnector{

    private String host;
    private int port;
    
    public HornetQTaskClientConnector(String name,String host,int port, BaseClientHandler handler) {
        super(name, handler);
        this.host=host;
        this.port=port;
    }            

    @Override
    public boolean connect() {
        return super.connect(host, port);
    }        
}
