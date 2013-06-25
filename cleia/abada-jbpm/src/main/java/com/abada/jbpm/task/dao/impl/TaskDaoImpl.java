/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.dao.impl;

import com.abada.jbpm.task.dao.TaskDao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.bpm.console.client.model.ParticipantRef;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jbpm.task.Group;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class TaskDaoImpl implements TaskDao{

    private static final String USER_TYPE="user";
    private static final String GROUP_TYPE="group";
    
    @PersistenceContext(unitName = "com.abada.jbpm.task")
    private EntityManager entityManager;
    
    @Transactional(value = "jbpm-task-txm", readOnly = true)
    public TaskRef fillPotencialsOwner(TaskRef taskRef) {
        Task task=entityManager.find(Task.class, taskRef.getId());
        if (task!=null){
            for (OrganizationalEntity oe:task.getPeopleAssignments().getPotentialOwners()){
                if (oe instanceof User){
                    taskRef.getParticipantUsers().add(new ParticipantRef(USER_TYPE, oe.getId()));
                }else if (oe instanceof Group){            
                    ParticipantRef aux=new ParticipantRef(GROUP_TYPE,oe.getId());
                    aux.setGroup(true);
                    taskRef.getParticipantGroups().add(aux);
                }
            }
            return taskRef;
        }
        return null;
    }    
}
