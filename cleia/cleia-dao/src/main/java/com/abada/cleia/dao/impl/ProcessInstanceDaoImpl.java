/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.ProcessInstanceDao;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.PatientHasProcessInstance;
import com.abada.jbpm.integration.console.task.PatientTaskManagement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class ProcessInstanceDaoImpl implements PatientTaskManagement, ProcessInstanceDao{

    private static final Log logger = LogFactory.getLog(ProcessInstanceDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Long> getProcessInstancesIdsForPatient(Long patientId) {
        Patient patient=entityManager.find(Patient.class, patientId);
        if (patient!=null && patient.getProcessInstances()!=null){
            List<Long> result=new ArrayList<Long>();            
            for (PatientHasProcessInstance pi:patient.getProcessInstances()){
                result.add(pi.getProcessInstanceId());
            }
            return result;
        }
        return null;
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public void addPInstancePatient(Long patientId, Long processInstanceId) {
        PatientHasProcessInstance add=new PatientHasProcessInstance();        
        add.setProcessInstanceId(processInstanceId);
        Patient patient=entityManager.find(Patient.class, patientId);
        patient.addPatientHasProcessInstance(add);
        entityManager.persist(add);
    }
    
}
