/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.idtype;

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

import com.abada.cleia.dao.IdTypeDao;
import com.abada.cleia.entity.user.IdType;
import com.abada.cleia.entity.user.Views;
import com.abada.extjs.ComboBoxResponse;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import com.abada.springframework.web.servlet.view.JsonView;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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
@RequestMapping("/rs/idtype")
public class IdtypeController {

    private static final Log logger = LogFactory.getLog(IdtypeController.class);
    @Autowired
    private IdTypeDao idtypeDao;

    
    /**
     * Returns all IdType.
     *
     * @return Return all IdType.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void getAllIdType(Model model) {

        ExtjsStore aux = new ExtjsStore();
        List<IdType> lidtype = idtypeDao.getAll();
        aux.setTotal(lidtype.size());
        aux.setData(lidtype);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Returns one IdType by id
     *
     * @param ididtype IdType id.
     * @return Return one IdType.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{ididtype}", method = RequestMethod.GET)
    public void getIdTypeById(@PathVariable String ididtype, Model model) {

        IdType idtype = new IdType();
        try {
            idtype = idtypeDao.getIdTypeById(ididtype);
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, idtype);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Search a list of IdType by params
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
    public void getSearchIdType(String filter, String sort, Integer limit, Integer start, Model model) {

        List<IdType> lidtype = new ArrayList<IdType>();
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lidtype = this.idtypeDao.getAll(grequest);
            aux.setData(lidtype);
            aux.setTotal(this.idtypeDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Search a list of IdType by params
     *
     * @param filter Filter conditions of results. Set in JSON by an array of
     * FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search/combo", method = RequestMethod.GET)
    public ExtjsStore getSearchIdTypeCombo(String filter, String sort, Integer limit, Integer start) {

        List<ComboBoxResponse> data = new ArrayList<ComboBoxResponse>();
        List<IdType> lidtype = new ArrayList<IdType>();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lidtype = this.idtypeDao.getAll(grequest);
        } catch (Exception e) {
            logger.error(e);
        }

        for (IdType pid : lidtype) {
            ComboBoxResponse aux = new ComboBoxResponse();
            aux.setId(pid.getValue());
            aux.setValue(pid.getValue());
            data.add(aux);
        }
        ExtjsStore result = new ExtjsStore();
        result.setData(data);
        return result;
    }

    /**
     * Insert a IdType
     *
     * @param idtype IdType structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postIdType(@RequestBody IdType idtype) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.postIdType(idtype);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Modify a IdType by id
     *
     * @param ididtype IdType id.
     * @param idtype IdType structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{ididtype}", method = RequestMethod.PUT)
    public Success putIdType(@PathVariable String ididtype, @RequestBody IdType idtype) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.putIdType(ididtype, idtype);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Delete a IdType by id
     *
     * @param ididtype IdType id.
     * @return Return success structure.
     */
    @RolesAllowed(value={"ROLE_ADMIN","ROLE_USER","ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{value}", method = RequestMethod.DELETE)
    public Success deleteIdType(@PathVariable String value) {

        Success result = new Success(Boolean.FALSE);
        try {
            idtypeDao.deleteIdType(value);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }
}
