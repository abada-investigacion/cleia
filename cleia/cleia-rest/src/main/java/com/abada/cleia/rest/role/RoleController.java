/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.role;

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

import com.abada.cleia.dao.RoleDao;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.cleia.entity.user.Views;
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
@RequestMapping("/rs/role")
public class RoleController {

    private static final Log logger = LogFactory.getLog(RoleController.class);
    @Autowired(required = false)
    private RoleDao roleprivDao;

    /**
     * Returns all roles
     *
     * @return Return all roles.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void getAllRolepriv(Model model) {

        ExtjsStore aux = new ExtjsStore();
        List<Role> listroles = roleprivDao.getAllRoles();
        aux.setTotal(listroles.size());
        aux.setData(listroles);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Returns a list of all users that have a role
     *
     * @param idrole Role id
     * @return Return a list of all users form a role
     * @throws Exception
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrole}/users", method = RequestMethod.GET)
    public void getRoleUsers(@PathVariable String idrole, Model model) throws Exception {


        List<User> lusers = new ArrayList<User>();
        ExtjsStore aux = new ExtjsStore();
        try {
            lusers = this.roleprivDao.getUsersByIdRole(idrole);
        } catch (Exception e) {
            logger.error(e);
        }

        for (User u : lusers) {
            u.setPassword("");
        }

        aux.setData(lusers);
        aux.setTotal(lusers.size());

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Returns one role by id
     *
     * @param idrolepriv Role id.
     * @return Return rolepriv structure
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrolepriv}", method = RequestMethod.GET)
    public void getRoleprivById(@PathVariable Integer idrolepriv, Model model) {

        Role role = null;
        try {
            role = roleprivDao.getRoleById(idrolepriv);
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, role);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Insert a role
     *
     * @param role Role structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postRole(@RequestBody Role role) {

        Success result = new Success(Boolean.FALSE);
        try {
            roleprivDao.postRole(role);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);

        }
        return result;
    }

    /**
     * Modify a user by id
     *
     * @param idrole Role id.
     * @param role Role structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrole}", method = RequestMethod.PUT)
    public Success putRole(@PathVariable String idrole, @RequestBody Role role) throws Exception {

        Success result = new Success(Boolean.FALSE);
        try {
            roleprivDao.putRole(idrole, role);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);

        }
        return result;

    }

    /**
     * Delete a user by id
     *
     * @param idrole Role id.
     * @return Return success structure
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrole}", method = RequestMethod.DELETE)
    public Success deleteRole(@PathVariable String idrole) {

        Success result = new Success(Boolean.FALSE);
        try {
            roleprivDao.deleteRole(idrole);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }

        return result;
    }

    /**
     * Search a list of roles by params
     *
     * @param filter Filter conditions of results. Set in JSON by an array of
     * FilterRequestPriv
     * @param sort Order of results. Set in JSON by an array of OrderByRequest
     * @param limit Set the limit of the results. Use it for pagination
     * @param start Set the start of the results. Use it for pagination
     * @return Return a list of results.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public void getSearchRole(String filter, String sort, Integer limit, Integer start, Model model) {
        List<Role> lrole;
        ExtjsStore aux = new ExtjsStore();

        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            lrole = this.roleprivDao.getAll(grequest);
            aux.setData(lrole);
            aux.setTotal(this.roleprivDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }

        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
}
