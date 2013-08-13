/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.user;

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

import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Patient;
import com.abada.extjs.Success;
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
 * @author katsu
 */
@Controller
public class UserController1 {

    private static final Log logger = LogFactory.getLog(UserController1.class);
    @Autowired
    private UserDao userDao;    
    
      /**
     * Modify a user by id
     *
     * @param iduser User id.
     * @param user User structure. Must set in JSON in the Http body request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/rs/identity/userpatient/{username}", method = RequestMethod.PUT)
    public Success putUserPatient(@PathVariable String username, @RequestBody Patient p) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            userDao.addPatient2User(username, p);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        
        return result;
    }

        /**
     * Removes the relationship between a user and a group
     *
     * @param iduser User id.
     * @param idgroup User id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = {"/rs/identity/user/{iduser}/removegroup/{idgroup}",
        "/rs/identity/group/{idgroup}/removeuser/{iduser}"}, method = RequestMethod.DELETE)
    public Success removeUserGroup(@PathVariable Long iduser, @PathVariable Long idgroup) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            this.userDao.deleteUserGroup(iduser, idgroup);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        return result;
    }        
    
    /**
     * Modifies the relationship between a user and a group
     *
     * @param iduser User id.
     * @param idgroup Group id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = {"/rs/identity/user/{iduser}/putgroup/{idgroup}",
        "/rs/identity/group/{idgroup}/putuser/{iduser}"}, method = RequestMethod.PUT)
    public Success putUserGroups(@PathVariable Long iduser, @PathVariable Long idgroup) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            this.userDao.putUserGroup(iduser, idgroup);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        return result;
    }
}
