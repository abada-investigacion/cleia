/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.mina;

import java.net.InetSocketAddress;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.jbpm.task.service.mina.BaseMinaHandler;

/**
 *
 * @author katsu
 */
public class MinaTaskClientConnector extends org.jbpm.task.service.mina.MinaTaskClientConnector{

    public MinaTaskClientConnector(String name, BaseMinaHandler handler,String address,int port) {
        super(name, handler);
        this.connector = new NioSocketConnector();
        this.address = new InetSocketAddress( address, port );
        this.connector.setHandler( this.handler );
    }       
}
