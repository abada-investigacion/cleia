/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

import com.abada.cleia.entity.user.PatientHasProcessInstance;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface ProcessInstanceDao {
    public void addPInstancePatient(Long patientId, Long processInstanceId);

    public PatientHasProcessInstance getProcessInstanceFromProcessIntance(Long patientId, Long pInstance);

    public List<PatientHasProcessInstance> getProcessInstance(Long patientId);
}
