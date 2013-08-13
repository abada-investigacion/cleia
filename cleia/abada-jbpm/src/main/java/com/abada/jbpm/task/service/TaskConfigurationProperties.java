/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service;

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

/**
 *
 * @author katsu
 */
public class TaskConfigurationProperties {
    public static final String TASK_HOST="jbpm.task.mina.host";
    public static final String TASK_PORT="jbpm.task.mina.port";
    
    public static final String JMS_BROKER_URL="jbpm.task.jms.broker.url";//"vm://localhost?broker.persistent=false"
    
    public static final String HORNET_HOST="jbpm.task.hornetq.host";
    public static final String HORNET_PORT="jbpm.task.hornetq.port";
}
