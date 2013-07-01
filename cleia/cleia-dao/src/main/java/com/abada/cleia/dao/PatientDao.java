/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Patient;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface PatientDao {

    /**
     * Return patient by Id
     *
     * @param patientId
     * @return
     */
    public Patient getPatientById(long patientId);

    public List<Patient> getAllPatients();

    public List<Patient> findPatientsrepeatable(List<Id> asList, Boolean repeatable) throws Exception;

    public List<Patient> getAll(GridRequest grequest);

    public Long loadSizeAll(GridRequest grequest);

    public List<Patient> getPatientUser(GridRequest grequest, String username);

    public void updatePatient(Patient patient, Patient p);

    public void postPatient(Patient patient) throws Exception;

    public void putPatient(Long idpatient, Patient patient) throws Exception;

    public void putPatientData(Long idpatient, Patient patient) throws Exception;

    public void enableDisablePatient(Long idpatient, boolean enable) throws Exception;

    public List<Id> getIdsForPatient(Long idpatient);

    public void putPatientid(Long idpatient, List<Id> ids) throws Exception;
}
