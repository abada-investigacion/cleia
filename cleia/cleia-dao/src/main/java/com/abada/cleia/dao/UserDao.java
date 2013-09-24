/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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

import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface UserDao {

    /**
     *
     * @param filters
     * @param username
     * @return
     */
    public User getUserByUsername(GridRequest filters, String username);

    /**
     * Add patient to a Medical user.
     *
     * @param username
     * @param p
     * @throws Exception
     */
    public void addPatient2User(String username, Patient p) throws Exception;

    /**
     * Returns all users
     *
     * @return
     */
    public List<User> getAllUsers();

    /**
     * Obtiene el tamaño de {@link User}
     *
     * @param filters
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one user by id
     *
     * @param iduser
     * @param request
     * @return
     */
    public User getUserById(Long iduser);

    /**
     * Insert a user
     *
     * @param enabled
     * @param nonExpired
     * @param nonExpiredCredentials
     * @param nonLocked
     * @param password
     * @param username
     * @throws Exception
     */
    public void postUser(User user) throws Exception;

    /**
     * Modify a user by id
     *
     * @param iduser
     * @param enabled
     * @param nonExpired
     * @param nonExpiredCredentials
     * @param nonLocked
     * @param password
     * @param username
     * @throws Exception
     */
    public void putUser(Long iduser, User user) throws Exception;

    /**
     * Enable a user by id
     *
     * @param iduser
     * @return
     */
    public void enableDisableUser(Long iduser, boolean enable) throws Exception;

    /**
     * Search a list of users by params
     *
     * @param filters
     * @return
     */
    public List<User> getAll(GridRequest filters);

    /**
     * Returns a list of all groups from a user
     *
     * @param iduser
     * @return
     * @throws Exception
     */
    public List<Group> getGroupsByIdUser(Long iduser) throws Exception;

    /**
     * Returns a list of all roles from a user
     *
     * @param iduser
     * @return
     * @throws Exception
     */
    public List<Role> getRolesByIdUser(Long iduser) throws Exception;
    
    /**
     * Returns a list of all ids from a user
     *
     * @param iduser
     * @return
     * @throws Exception
     */
    public List<Id> getIdsByIdUser(Long iduser) throws Exception;


    /**
     * Modifies the relationship between a user and a role
     *
     * @param iduser
     * @param idrole
     * @return
     */
    public void putUserRole(Long iduser, Integer idrole) throws Exception;

    /**
     * Removes the relationship between a user and a role
     *
     * @param iduser
     * @param idrole
     * @return
     */
    public void deleteUserRole(Long iduser, Integer idrole) throws Exception;

    /**
     * Returns all actors that an actor has in their groups
     *
     * @param username
     * @return
     */
    public List<String> getUserGroup(String username);

    /**
     * setting user
     *
     * @param user
     * @param newuser
     */
    public void updateUser(User user, User newuser);
    
    /**
     * Returns a list of users who are not assigned a patient 
     * 
     * @return 
     */
    public List<User> getUserWithoutAssignedPatient();
  

    public List<User> getUsernotPatient(GridRequest grequest);
    public Long getUsernotPatientsize(GridRequest filters);
}
