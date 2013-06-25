/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service;

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
