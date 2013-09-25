/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.medical;

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

import com.abada.cleia.dao.MedicalDao;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Medical;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.Views;

import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import com.abada.springframework.web.servlet.view.JsonView;
import java.security.Principal;
import java.util.ArrayList;
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
@RequestMapping("/rs/medical")
public class MedicalController {

    private static final Log logger = LogFactory.getLog(MedicalController.class);
    @Autowired
    private MedicalDao medicalDao;

    /**
     * Returns all medicals.
     *
     * @return Return a list of all medicals.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ExtjsStore getAllMedicals() {

        ExtjsStore aux = new ExtjsStore();
        List<Medical> lmedical = medicalDao.getAllMedicals();
        aux.setTotal(lmedical.size());
        aux.setData(lmedical);
        return aux;
    }

    /**
     * Returns one medical by id.
     *
     * @param idmedical Medical id.
     * @return Return medical structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Medical getMedicalById(@PathVariable Long id) {

        Medical medical = new Medical();
        try {
            medical = medicalDao.getMedicalById(id);
        } catch (Exception e) {
            logger.error(e);
        }

        return medical;
    }

    /**
     * Find medical with the passed identificators
     *
     * @param lmedicalid Medical id list.
     * @return Return medical structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.PUT)
    public Medical getMedicalByListId(@RequestBody Id[] lid) {

        Medical medical = null;
        try {
            List<Medical> lmedical = medicalDao.findMedicalsrepeatable(Arrays.asList(lid), null);
            if (!lmedical.isEmpty() && lmedical.size() == 1) {
                return lmedical.get(0);
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return medical;
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
    public void getSearchMedical(String filter, String sort, Integer limit, Integer start, Model model) {

        List<Medical> lmedical;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lmedical = this.medicalDao.getAll(grequest);
            aux.setData(lmedical);
            aux.setTotal(this.medicalDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Level1.class);
    }

   

    /**
     * Get patients by Medical id
     * 
     * @param idmedical
     * @return 
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}/patients", method = RequestMethod.GET)
    public void getPatientsByMedicalId(@PathVariable Long idmedical, Model model) {

        List<Patient> lpatient = new ArrayList<Patient>();
        ExtjsStore aux = new ExtjsStore();
        try {
            lpatient = medicalDao.findPatientsByMedicalId(idmedical);

        } catch (Exception e) {
            logger.error(e);
        }
                     
       
        aux.setTotal(lpatient.size());
        aux.setData(lpatient);
        
        
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Case1.class);

    }

    /**
     * Insert a medical
     *
     * @param medical Medical structure. Must set in JSON in the Http body.
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postMedical(@RequestBody Medical medical) {

        Success result = new Success(Boolean.FALSE);
        try {
            medicalDao.postMedical(medical);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * add patient a medical
     *
     * @param medical Medical structure. Must set in JSON in the Http body.
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/assignpatient", method = RequestMethod.POST)
    public Success postaddPatient(@RequestBody Medical medical) {

        Success result = new Success(Boolean.FALSE);
        try {
            medicalDao.addpatientMedical(medical);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Modify a medical by id
     *
     * @param idmedical Medical id.
     * @param medical Medical structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}", method = RequestMethod.PUT)
    public Success putMedical(@PathVariable Long idmedical, @RequestBody Medical medical) {

        Success result = new Success(Boolean.FALSE);
        try {
            medicalDao.putMedical(idmedical, medical);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Enable a medical by id
     *
     * @param idmedical Medical id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}/{enable}", method = RequestMethod.PUT)
    public Success enableDisableMedical(@PathVariable Long idmedical, @PathVariable boolean enable) {

        Success result = new Success(Boolean.FALSE);
        try {
            medicalDao.enableDisableMedical(idmedical, enable);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Return all identifiers of a medical.
     *
     * @param idmedical Medical id.
     * @return Return all identifiers of a medical.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}/id", method = RequestMethod.GET)
    public ExtjsStore getMedicalidByMedical(@PathVariable Long idmedical) {

        List<Id> lmedicalid;
        ExtjsStore aux = new ExtjsStore();
        try {
            lmedicalid = this.medicalDao.getIdsForMedical(idmedical);
            aux.setData(lmedicalid);
            aux.setTotal(lmedicalid.size());
        } catch (Exception e) {
            logger.error(e);
        }

        return aux;
    }

    /**
     * Modify a medical by id.
     *
     * @param idmedical Medical id.
     * @param medical Medical structure. Must set in JSON in the Http body.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}/id", method = RequestMethod.PUT)
    public Success putMedicalid(@PathVariable Long idmedical, @RequestBody Id[] lmedicalid) {

        Success result = new Success(Boolean.FALSE);
        try {
            medicalDao.putMedicalid(idmedical, Arrays.asList(lmedicalid));
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }
    
     @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idmedical}/{operation}/{idpatient}", method = RequestMethod.PUT)
    public Success assignPatients(@PathVariable Long idmedical, @PathVariable String operation,@PathVariable Long idpatient) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            if (operation.equals("add")) {
                this.medicalDao.putPatientMedical(idpatient, idmedical);
            } else {
                this.medicalDao.deletePatientMedical(idpatient, idmedical);
            }            
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        return result;
    }
     
     
       @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/idmedical", method = RequestMethod.GET)
    public Long idmedical(HttpServletRequest request) {
        
        Long result =null;
        try {
             Principal principal=request.getUserPrincipal();
             result=this.medicalDao.getMedicalUser(principal.getName());
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }
}
