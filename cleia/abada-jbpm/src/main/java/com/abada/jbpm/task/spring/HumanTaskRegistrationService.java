/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.spring;

import com.abada.jbpm.task.service.TaskClient;
import com.abada.jbpm.task.service.TaskConfigurationProperties;
import com.abada.jbpm.task.service.TaskServiceFactory;
import java.util.Observable;
import java.util.Observer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.runtime.KnowledgeRuntime;
import org.jbpm.process.workitem.wsht.AsyncGenericHTWorkItemHandler;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 *
 * @author katsu
 */
public class HumanTaskRegistrationService extends ApplicationObjectSupport implements Observer {

    private static final Log logger = LogFactory.getLog(HumanTaskRegistrationService.class);
    private boolean loaded = false;
    private AsyncGenericHTWorkItemHandler handler;
    private TaskService taskService;
    private TaskServiceFactory taskClientFactory;        

    public void setTaskClientFactory(TaskServiceFactory taskClientFactory) {
        this.taskClientFactory = taskClientFactory;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
        this.taskService.addObserver(this);
    }

    private synchronized void registerWSHumanTask() {
        if (!loaded) {
            try {
                KnowledgeRuntime session = this.getApplicationContext().getBean("ksession1", KnowledgeRuntime.class);
                if (session != null) {
                    handler = new AsyncGenericHTWorkItemHandler(session);
                    TaskClient client=taskClientFactory.getTaskClient();
                    handler.setClient(client);                    
                    handler.setIpAddress(taskClientFactory.getProperties().getProperty(TaskConfigurationProperties.HORNET_HOST));
                    handler.setPort(Integer.parseInt(taskClientFactory.getProperties().getProperty(TaskConfigurationProperties.HORNET_PORT)));

//                    if (client.connect()) {
                        session.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
                        handler.connect();
                        loaded = true;
                        logger.debug("Registered Human Task Handler");
//                    }else{
//                        throw new Exception("Couldn't connect to Human Server");
//                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public synchronized void update(Observable o, Object arg) {
        if (o instanceof TaskService && arg instanceof String) {
            if (TaskService.START_EVENT.equals(arg)) {
                registerWSHumanTask();
            } else if (TaskService.BEFORE_STOP_EVENT.equals(arg)) {
                try {
                    handler.dispose();
                    loaded = false;
                    logger.debug("Stopped Human Task Handler");
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }
}
