/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.entity.user.Patient;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class PatientDaoImpl implements PatientDao{

    private static final Log logger = LogFactory.getLog(PatientDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    
    @Transactional(value = "cleia-txm", readOnly = true)
    public Patient getPatientById(long patientId) {
        Patient result=entityManager.find(Patient.class, patientId);
        return result;
    }
    
}
