/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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
}
