/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.mina;

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
