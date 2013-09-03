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
import com.abada.cleia.entity.user.User;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface GroupDao {
/**
     * returns all users in a group
     * @param groupname
     * @return 
     */
    public List<String> getMemberGroup(String groupname);

    /**
     * Returns all groups
     * @return 
     */
    public List<Group> getAllGroups();
    
    /**
     *Obtiene el tamaño de {@link Group1}
     * @param filters 
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one group by id
     * @param idgroup
     * @param request
     * @return 
     */
    public Group getGroupById(Long idgroup);

    /**
     * Insert a group in database
     * @param name 
     */
    public void postGroup(Group group) throws Exception;

    /**
     * Modify a group by id
     * @param idgroup
     * @param name
     * @return 
     */
    public void putGroup(String idgroup, Group newgroup) throws Exception;
    
    /**
     * Search a list of groups by params
     * @param filters
     * @return 
     */
    public List<Group> getAll(GridRequest filters);
    
    /**
     * Returns a list of all users from a group
     * @param idgroup
     * @return
     * @throws Exception 
     */
    public List<User> getUsersByIdGroup(String idgroup,GridRequest filters) throws Exception;

    /**
     * Enable a group by id
     *
     * @param idgroup
     * @return
     */
    public void enableDisableGroup(String idgroup, boolean enable) throws Exception;
    
    /**
     * Modifies the relationship between a user and a group
     *
     * @param iduser
     * @param idgroup
     * @return
     */
    public void putUserGroup(Long iduser, String idgroup) throws Exception;

    /**
     * Removes the relationship between a user and a group
     *
     * @param iduser
     * @param idgroup
     * @return
     */
    public void deleteUserGroup(Long iduser, String idgroup) throws Exception;
}
