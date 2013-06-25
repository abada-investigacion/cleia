/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console.task;

import java.util.List;

/**
 *
 * @author katsu
 */
public interface PatientTaskManagement {
    /**
     * Return the process instances of BPM for a patientId
     * @param patientId
     * @return 
     */
    public List<Long> getProcessInstancesIdsForPatient(Long patientId);
}
