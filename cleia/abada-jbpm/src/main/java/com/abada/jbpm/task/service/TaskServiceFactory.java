/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service;

import static com.abada.jbpm.task.service.TaskClientType.HORNETQ;
import static com.abada.jbpm.task.service.TaskClientType.JMS;
import static com.abada.jbpm.task.service.TaskClientType.MINA;
import com.abada.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import com.abada.jbpm.task.service.mina.MinaTaskClientConnector;
import java.util.Properties;
import javax.naming.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.SystemEventListenerFactory;
import org.jbpm.task.identity.UserGroupCallback;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.jbpm.task.service.hornetq.HornetQTaskServer;
import org.jbpm.task.service.mina.MinaTaskClientHandler;
import org.jbpm.task.service.mina.MinaTaskServer;

/**
 *
 * @author katsu
 */
public class TaskServiceFactory {

    private static final Log logger = LogFactory.getLog(TaskServiceFactory.class);
    private UserGroupCallback groupTaskManagement;
    private Properties properties;
    private TaskClientType type;
    private Context context;

    public void setType(TaskClientType type) {
        this.type = type;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setGroupTaskManagement(UserGroupCallback groupTaskManagement) {
        this.groupTaskManagement = groupTaskManagement;
    }

    public Properties getProperties() {
        return properties;
    }

    public TaskClient getTaskClient() {
        switch (type) {
            case JMS:
                return getJMSClient();
            case MINA:
                return getMinaClient();
            case HORNETQ:
                return getHornetQClient();
        }
        return null;
    }

    public TaskServer getTaskServer(org.jbpm.task.service.TaskService taskService) {
        switch (type) {
            case JMS:
                return getJMSServer(taskService);
            case MINA:
                return getMinaServer(taskService);
            case HORNETQ:
                return getHornetQServer(taskService);
        }
        return null;
    }

    private TaskClient getJMSClient() {
        //FIXME 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        try {
//            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(properties.getProperty(TaskConfigurationProperties.JMS_BROKER_URL));
//
//            context = EasyMock.createMock(Context.class);
//            EasyMock.expect(context.lookup("ConnectionFactory")).andReturn(factory).anyTimes();
//            EasyMock.replay(context);
//            TaskClient client = new TaskClient(new JMSTaskClientConnector("JMS Task Client",
//                    new JMSTaskClientHandler(SystemEventListenerFactory.getSystemEventListener()), properties, context), groupTaskManagement);
//            logger.debug("Return JMS client. ");
//            return client;
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return null;
    }

    private TaskServer getJMSServer(org.jbpm.task.service.TaskService taskService) {
        //FIXME 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        try {
//            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(properties.getProperty(TaskConfigurationProperties.JMS_BROKER_URL));
//
//            context = EasyMock.createMock(Context.class);
//            EasyMock.expect(context.lookup("ConnectionFactory")).andReturn(factory).anyTimes();
//            EasyMock.replay(context);
//
//            TaskServer server = new JMSTaskServer(taskService, properties, this.context);
//            logger.debug("Return JMS server. ");
//            return server;
//        } catch (Exception e) {
//            logger.error(e);
//        }
//        return null;
    }

    private TaskClient getHornetQClient() {
        try {
            TaskClient client = new TaskClient(new HornetQTaskClientConnector("HornetQ Task Client", properties.getProperty(TaskConfigurationProperties.HORNET_HOST), Integer.parseInt(properties.getProperty(TaskConfigurationProperties.HORNET_PORT)), new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener())));
            logger.debug("Return HornetQ client. ");
            return client;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private TaskServer getHornetQServer(TaskService taskService) {
        try {
            TaskServer server = new HornetQTaskServer(taskService, properties.getProperty(TaskConfigurationProperties.HORNET_HOST), Integer.parseInt(properties.getProperty(TaskConfigurationProperties.HORNET_PORT)));
            logger.debug("Return HornetQ server. ");
            return server;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private TaskClient getMinaClient() {
        try {
            TaskClient client = new TaskClient(new MinaTaskClientConnector("Mina Task Client",
                    new MinaTaskClientHandler(SystemEventListenerFactory.getSystemEventListener()), properties.getProperty(TaskConfigurationProperties.TASK_HOST),
                    Integer.parseInt(properties.getProperty(TaskConfigurationProperties.TASK_PORT))));
            logger.debug("Return Mina client. ");
            return client;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private TaskServer getMinaServer(TaskService taskService) {
        try {
            TaskServer server = new MinaTaskServer(taskService, Integer.parseInt(properties.getProperty(TaskConfigurationProperties.TASK_PORT)),
                    properties.getProperty(TaskConfigurationProperties.TASK_HOST));
            logger.debug("Return Socket server. ");
            return server;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
