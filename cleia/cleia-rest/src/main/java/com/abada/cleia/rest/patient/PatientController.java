/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.patient;

/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.dao.ProcessInstanceDao;
import com.abada.cleia.entity.temporal.PatientHasProcessInstanceInfo;
import com.abada.cleia.entity.user.Genre;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.Views;
import com.abada.extjs.ComboBoxResponse;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import java.util.ArrayList;
import com.abada.springframework.web.servlet.view.JsonView;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public void getAllPatients(Model model) {

        ExtjsStore aux = new ExtjsStore();
        List<Patient> lpatient = patientDao.getAllPatients();
        aux.setTotal(lpatient.size());
        aux.setData(lpatient);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Returns one patient by id.
     *
     * @param idpatient Patient id.
     * @return Return patient structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idpatient}", method = RequestMethod.GET)
    public void getPatientById(@PathVariable Long idpatient, Model model) {

        Patient patient = new Patient();
        try {
            patient = patientDao.getPatientById(idpatient);
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, patient);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    public void getSearchPatient(String filter, String sort, Integer limit, Integer start, Model model) {

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
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Level1.class);
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
    @RequestMapping(value = "/searchforassignment", method = RequestMethod.GET)
    public void getPatientsForAssignment(String filter, String sort, Integer limit, Integer start, Model model) {

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
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.MedicalData.class);
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
    @RequestMapping(value = "/assigned/search", method = RequestMethod.GET)
    public void getSearchPatient(String filter, String sort, Integer limit, Integer start, Model model,HttpServletRequest request) {

        List<Patient> lpatient;
        ExtjsStore aux = new ExtjsStore();
        try {
            Principal principal=request.getUserPrincipal();
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lpatient = this.patientDao.getAllbyMedical(grequest,principal.getName());
            aux.setData(lpatient);
            aux.setTotal(this.patientDao.loadSizeAllbyMedical(grequest,principal.getName()).intValue());
        } catch (Exception e) {
            logger.error(e);
        }
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    public void getSearchPatientuser(@PathVariable String username, String filter, String sort, Integer limit, Integer start, Model model) {

        List<Patient> lpatient;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lpatient = this.patientDao.getPatientUser(grequest, username);
            aux.setData(lpatient);
            aux.setTotal(lpatient.size());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
    
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search/sessionPatient", method = RequestMethod.GET)
    public void getSearchPatientSessionUser(HttpServletRequest request, Model model) {

        
        Patient aux = null;
        try {
            String username = request.getUserPrincipal().getName();
            aux = this.patientDao.getPatientUser(null, username).get(0);
            
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/patientData", method = RequestMethod.PUT)
    public Success putPatientSessionUser( @RequestBody Patient patient, HttpServletRequest request) {

        Success result = new Success(Boolean.FALSE);
        try {
            if(!patient.getUser().getUsername().equals(request.getUserPrincipal().getName())){
                throw new Exception("El usuario no coincide con la sessión");
            }
            patientDao.putPatient(patient.getId(), patient);
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
     *
     * @param patientId Patient id.
     * @return Return a list of every process instance that have a patient,
     * oncoguide or not.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/{patientId}/pinstance/list", method = RequestMethod.GET)
    public void getPInstancePatients(@PathVariable Long patientId, Model model) {
        ExtjsStore result = new ExtjsStore();
        List<PatientHasProcessInstanceInfo> aux = this.pInstancePatientDao.getProcessInstance(patientId);
        if (aux != null) {
            result.setData(aux);
        }
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
    
    /**
     * Return a list of every process instance that have a patient.
     *
     * @param patientId Patient id.
     * @return Return a list of every process instance that have a patient,
     * oncoguide or not.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/pinstance/list", method = RequestMethod.GET)
    public void getPInstancePatient(Model model, HttpServletRequest request) {
        ExtjsStore result = new ExtjsStore();
        List<PatientHasProcessInstanceInfo> aux = this.pInstancePatientDao.getProcessInstance(request.getUserPrincipal().getName());
        if (aux != null) {
            result.setData(aux);
        }
        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

//    /**
//     * Return a list of every process instance that have a patient.
//     *
//     * @param patientId Patient id.
//     * @param pInstance Instance base to search all the subprocess from it.
//     * @return Return a list of every process instance that have a patient,
//     * oncoguide or not.
//     */
//    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER"})
//    @RequestMapping(value = "/{patientId}/pinstance/{pInstance}", method = RequestMethod.GET)
//    public void getPInstancePatients(@PathVariable Long patientId, @PathVariable Long pInstance, Model model) {
//        PatientHasProcessInstanceInfo result = this.pInstancePatientDao.getProcessInstanceFromProcessIntance(patientId, pInstance);
//        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
//        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
//    }

    /**
     * Return all identifiers of a patient.
     *
     * @param idpatient Patient id.
     * @return Return all identifiers of a patient.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idpatient}/id", method = RequestMethod.GET)
    public void getPatientidByPatient(@PathVariable Long idpatient, Model model) {

        List<Id> lpatientid;
        ExtjsStore aux = new ExtjsStore();
        try {
            lpatientid = this.patientDao.getIdsForPatient(idpatient);
            aux.setData(lpatientid);
            aux.setTotal(lpatientid.size());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Return all Patient Genre in a ExtjsStore structure
     *
     * @return
     */
    @RequestMapping(value = "/genre/combo", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    public void getGenreCombo(Model model) {
        List<ComboBoxResponse> data = new ArrayList<ComboBoxResponse>();
        for (Genre tr : Genre.values()) {
            ComboBoxResponse aux = new ComboBoxResponse();
            aux.setId(tr.toString());
            aux.setValue(tr.toString());
            data.add(aux);
        }
        ExtjsStore result = new ExtjsStore();
        result.setData(data);

        model.addAttribute(JsonView.JSON_VIEW_RESULT, result);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Search a list of patient
     *
     * @param filter Filter conditions of results. Set in JSON by an array of
     * FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search/patientnotmedical", method = RequestMethod.GET)
    public void getSearchPatientnotmedical(String filter, String sort, Integer limit, Integer start, Model model) {

        List<Patient> lpatient;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lpatient = this.patientDao.getPatientnotmedical(grequest);
            aux.setData(lpatient);
            aux.setTotal(this.patientDao.getPatientnotmedicalsize(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
}
