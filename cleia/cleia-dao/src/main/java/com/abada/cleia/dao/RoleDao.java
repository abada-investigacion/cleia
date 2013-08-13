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

import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface RoleDao {
    /**
     * Returns all roles
     * @return 
     */
    public List<Role> getAllRoles();
    /**
     *Obtiene el tamaño de {@link Role}
     * @param filters 
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one role by id
     * @param idrole
     * @return 
     */
    public Role getRoleById(Integer idrole);

    /**
     * Insert a role 
     * @param rolename
     * @return 
     */
    public void postRole(Role role) throws Exception;

    /**
     * Modify a role by id
     * @param idrole
     * @param rolename
     * @return 
     */
    public void putRole(String idrole, Role newrole) throws Exception;

    /**
     * Delete a role by id
     * @param idrole
     * @return 
     */
    public void deleteRole(String idrole) throws Exception;

    /**
     * Search a list of roles by params
     * @param filters
     * @return 
     */
    public List<Role> getAll(GridRequest filters);

    /**
     * Returns a list of all users from a role
     * @param idrole
     * @return
     * @throws Exception 
     */
    public List<User> getUsersByIdRole(String authority) throws Exception;
}
