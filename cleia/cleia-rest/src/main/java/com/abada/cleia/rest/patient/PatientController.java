/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.patient;

import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.dao.ProcessInstanceDao;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.PatientHasProcessInstance;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mmartin
 */
@Controller
@RequestMapping("/rs/patient")
public class PatientController {

    private static final Log logger = LogFactory.getLog(PatientController.class);
    @Autowired
    private PatientDao patientDao;
    @Autowired
    private ProcessInstanceDao pInstancePatientDao;

    /**
     * Returns all patients.
     *
     * @return Return a list of all patients.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ExtjsStore getAllPatients() {

        ExtjsStore aux = new ExtjsStore();
        List<Patient> lpatient = patientDao.getAllPatients();
        aux.setTotal(lpatient.size());
        aux.setData(lpatient);
        return aux;
    }

    /**
     * Returns one patient by id.
     *
     * @param idpatient Patient id.
     * @return Return patient structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idpatient}", method = RequestMethod.GET)
    public Patient getPatientById(@PathVariable Long idpatient) {

        Patient patient = new Patient();
        try {
            patient = patientDao.getPatientById(idpatient);
        } catch (Exception e) {
            logger.error(e);
        }

        return patient;
    }

    /**
     * Find patient with the passed identificators
     *
     * @param lpatientid Patient id list.
     * @return Return patient structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.PUT)
    public Patient getPatientByListId(@RequestBody Id[] lpatientid) {

        Patient patient = null;
        try {
            List<Patient> lpatient = patientDao.findPatients(Arrays.asList(lpatientid));
            if (!lpatient.isEmpty() && lpatient.size() == 1) {
                return lpatient.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return patient;
    }

    /**
     * Search a list of users by params
     *
     * @param filter Filter conditions of results. Set in JSON by an array of
     * FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ExtjsStore getSearchPatient(String filter, String sort, Integer limit, Integer start) {

        List<Patient> lpatient;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lpatient = this.patientDao.getAll(grequest);
            aux.setData(lpatient);
            aux.setTotal(this.patientDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        return aux;
    }
    
     /**
     * Search a list of users by params
     *
     * @param filter Filter conditions of results. Set in JSON by an array of
     * FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search/byUser/{username}", method = RequestMethod.GET)
    public ExtjsStore getSearchPatientuser(@PathVariable String username,String filter, String sort, Integer limit, Integer start) {

        List<Patient> lpatient;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lpatient = this.patientDao.getPatientUser(grequest,username);
            aux.setData(lpatient);
            aux.setTotal(this.patientDao.loadSizeuserpatient(grequest,username).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        return aux;
    }
    
    

    /**
     * Insert a patient
     *
     * @param patient Patient structure. Must set in JSON in the Http body.
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postPatient(@RequestBody Patient patient) {

        Success result = new Success(Boolean.FALSE);
        try {
            patientDao.postPatient(patient);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Modify a patient by id
     *
     * @param idpatient Patient id.
     * @param patient Patient structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idpatient}", method = RequestMethod.PUT)
    public Success putPatient(@PathVariable Long idpatient, @RequestBody Patient patient) {

        Success result = new Success(Boolean.FALSE);
        try {
            patientDao.putPatient(idpatient, patient);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Modify a patient by id
     *
     * @param idpatient Patient id.
     * @param patient Patient structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/rs/identity/patientDemographic/{idpatient}", method = RequestMethod.PUT)
    public Success putPatientData(@PathVariable Long idpatient, @RequestBody Patient patient) {

        Success result = new Success(Boolean.FALSE);
        try {
            patientDao.putPatientData(idpatient, patient);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Enable a patient by id
     *
     * @param idpatient Patient id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idpatient}/{enable}", method = RequestMethod.PUT)
    public Success enableDisablePatient(@PathVariable Long idpatient, @PathVariable boolean enable) {

        Success result = new Success(Boolean.FALSE);
        try {
            patientDao.enableDisablePatient(idpatient, enable);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Return a list of every process instance that have a patient.
     * @param patientId Patient id.
     * @return Return a list of every process instance that have a patient, oncoguide or not.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{patientId}/pinstances", method = RequestMethod.GET)
    public ExtjsStore getPInstancePatients(@PathVariable Long patientId) {
        ExtjsStore result = new ExtjsStore();
        List<PatientHasProcessInstance> aux = this.pInstancePatientDao.getProcessInstance(patientId);
        if (aux != null) {
            result.setData(aux);
        }
        return result;
    }
    
    /**
     * Return a list of every process instance that have a patient.
     * @param patientId Patient id.
     * @param pInstance Instance base to search all the subprocess from it.
     * @return Return a list of every process instance that have a patient, oncoguide or not.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{patientId}/pinstance/{pInstance}", method = RequestMethod.GET)
    public PatientHasProcessInstance getPInstancePatients(@PathVariable Long patientId,@PathVariable Long pInstance) {        
        PatientHasProcessInstance result = this.pInstancePatientDao.getProcessInstanceFromProcessIntance(patientId, pInstance);                
        return result;
    }
}
