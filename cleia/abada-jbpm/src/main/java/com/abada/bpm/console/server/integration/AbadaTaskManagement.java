/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.server.integration;

import java.util.List;
import org.jboss.bpm.console.client.model.TaskRef;

/**
 *
 * @author katsu
 */
public interface AbadaTaskManagement {
    List<TaskRef> getTaskByUser(Long patientId, String userId);
    List<TaskRef> getTaskForProcessInstanceAndSubProcess(Long processInstance, String userId);
}
