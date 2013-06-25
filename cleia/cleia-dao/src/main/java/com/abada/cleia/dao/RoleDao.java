/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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
     *Obtiene el tamaño de {@link Rolepriv}
     * @param filters 
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one role by id
     * @param idrole
     * @return 
     */
    public Role getRoleprivById(Integer idrole);

    /**
     * Insert a role 
     * @param rolename
     * @return 
     */
    public void postRolepriv(Role role) throws Exception;

    /**
     * Modify a role by id
     * @param idrolepriv
     * @param rolename
     * @return 
     */
    public void putRolepriv(Integer idrolepriv, Role newrole) throws Exception;

    /**
     * Delete a role by id
     * @param idrolepriv
     * @return 
     */
    public void deleteRolepriv(Integer idrolepriv) throws Exception;

    /**
     * Search a list of roles by params
     * @param filters
     * @return 
     */
    public List<Role> getAll(GridRequest filters);

    /**
     * Returns a list of all users from a role
     * @param idrolepriv
     * @return
     * @throws Exception 
     */
    public List<User> getUsersByIdRole(Integer idrolepriv) throws Exception;
}
