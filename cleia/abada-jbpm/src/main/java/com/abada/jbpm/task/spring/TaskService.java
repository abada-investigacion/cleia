/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.spring;

import com.abada.jbpm.task.service.TaskServiceFactory;
import java.lang.Thread.State;
import java.util.Observable;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.SystemEventListenerFactory;
import org.jbpm.task.service.TaskServer;
import org.jbpm.task.service.TaskServiceSession;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 *
 * @author katsu
 */
public class TaskService extends Observable implements ApplicationListener {
    public static final String BEFORE_STOP_EVENT="BeforeStop";
    public static final String STOP_EVENT="Stop";
    public static final String STOP_ERROR_EVENT="StopError";
    public static final String START_EVENT="Start";
    

    private static final Log log = LogFactory.getLog(TaskService.class);
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    private org.jbpm.task.service.TaskService taskService;
    private TaskServiceFactory taskServiceFactory;
    private TaskServer server;
    private TaskServiceSession taskSession;
    private Thread thread;    

    public void setTaskServiceFactory(TaskServiceFactory taskServiceFactory) {
        this.taskServiceFactory = taskServiceFactory;
    }
    
    public void onApplicationEvent(ApplicationEvent event) {
        log.trace("Application Context Event Handled " + event);
        if (event instanceof ContextStoppedEvent) {
            //Stop
            stopTaskService();
        } else if (event instanceof ContextClosedEvent) {
            //Close
            stopTaskService();
        } else if (event instanceof ContextStartedEvent) {
            //Start
            startTaskService();
        } else if (event instanceof ContextRefreshedEvent) {
            //Refresh            
            startTaskService();
        }
    }

    private synchronized void startTaskService() {
        if (taskService == null) {
            try {
                taskService = new org.jbpm.task.service.TaskService(entityManagerFactory, SystemEventListenerFactory.getSystemEventListener());
                taskSession = taskService.createSession();

                // start server
                server=taskServiceFactory.getTaskServer(taskService);
                thread = new Thread(server);
                thread.start();
                int cont = 0;
                while (cont <= 20 && !server.isRunning()) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    cont++;
                    log.debug("Waiting for initialization of Task Server. Count: " + cont);
                };
                if (server.isRunning()) {
                    super.setChanged();
                    super.notifyObservers(START_EVENT);
                    //taskSession.dispose();
                    log.debug("Started Task Service");
                } else {                    
                    throw new Exception("Server not start");
                }
            } catch (Exception e) {
                taskService = null;
                log.error(e);
            }
        }
    }

    private synchronized void stopTaskService() {
        try {
            super.setChanged();
            super.notifyObservers(BEFORE_STOP_EVENT);
            //Wail for clients disconnect
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }
            try{
                taskSession.dispose();
            }catch (Exception e){
                log.error(e);
            }

            try{
                server.stop();
            }catch (Exception e){
                log.error(e);
            }

            log.debug("Stopped Task Service. Thread " + thread.getName());
            taskService = null;
            server = null;
            super.setChanged();
            super.notifyObservers(STOP_EVENT);

            thread.interrupt();
        } catch (Exception e) {
            log.error(e);
            super.setChanged();
            super.notifyObservers(STOP_ERROR_EVENT);
        }
    }

    public State getStatus() {
        if (thread != null) {
            return thread.getState();
        }
        return null;
    }

    public TaskServiceSession getTaskSession() {
        return taskSession;
    }

    /*public void addDebugUsers(){
    taskSession.addUser(new User("katsu"));
    taskSession.addUser(new User("Administrator"));
    }*/
}
