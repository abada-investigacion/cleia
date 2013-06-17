/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.dao;

import org.jboss.bpm.console.client.model.TaskRef;

/**
 *
 * @author katsu
 */
public interface TaskDao {
    TaskRef fillPotencialsOwner(TaskRef taskRef);
}
