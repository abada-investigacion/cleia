/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.user;

import com.abada.cleia.dao.GroupDao;
import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.extjs.ExtjsStore;
import com.abada.extjs.Success;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.factory.GridRequestFactory;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/rs/user")
public class UserController {

    private static final Log logger = LogFactory.getLog(UserController.class);
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    /**
     * Returns all members of a group
     *
     * @param groupid Group id.
     * @param request Do nothing.
     * @return Return all members of a group.
     */
    @RequestMapping(value = "/{groupid}/members", method = RequestMethod.GET)
    public List<String> getMembersGroups(@PathVariable String groupid) {
        List<String> listmembers = groupDao.getMemberGroup(groupid);
        return listmembers;
    }
    
    /**
     * Returns all users that belongs to the same group of logged user.
     * @param userid User id.
     * @param request Do nothing.
     * @return Return a user list.
     */
    @RequestMapping(value = "/{userid}/actors", method = RequestMethod.GET)
    public List<String> getActorsForUser(@PathVariable String userid) {
        List<String> listuser = userDao.getUserGroup(userid);
        return listuser;
    }
    
    //
    /**
     * Returns all users
     *
     * @return Return all users
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ExtjsStore getAllUsers() {
        
        ExtjsStore aux = new ExtjsStore();
        List<User> listusers = userDao.getAllUsers();
        
        for (User u : listusers) {
            u.setPassword("");
        }
        
        aux.setTotal(listusers.size());
        aux.setData(listusers);
        return aux;
    }

    /**
     * Returns one user by id
     *
     * @param iduser User id
     * @return Returns a user
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long iduser) {
        
        User user = new User();
        try {
            user = userDao.getUserById(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        user.setPassword("");
        
        return user;
    }
    
    

    /**
     * Insert a user
     *
     * @param user User structure. Must set in JSON in the Http body request.
     * @return Success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postUser(@RequestBody User user) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            userDao.postUser(user);
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
     * @param iduser User id.
     * @param user User structure. Must set in JSON in the Http body request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}", method = RequestMethod.PUT)
    public Success putUser(@PathVariable Long iduser, @RequestBody User user) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            userDao.putUser(iduser, user);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        
        return result;
    }

    
    
    /**
     * Enable a user by id
     *
     * @param iduser User id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/{enable}", method = RequestMethod.PUT)
    public Success enableDisableUser(@PathVariable Long iduser, @PathVariable boolean enable) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            userDao.enableDisableUser(iduser, enable);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        
        return result;
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
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ExtjsStore getSearchUser(String filter, String sort, Integer limit, Integer start) {
        
        List<User> luser;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            luser = this.userDao.getAll(grequest);
            
            for (User u : luser) {
                u.setPassword("");
            }
            
            aux.setData(luser);
            aux.setTotal(this.userDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }
        
        return aux;
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
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/search/{username}", method = RequestMethod.GET)
    public ExtjsStore getSearchUser(@PathVariable String username,String filter, String sort, Integer limit, Integer start) {
        
        List<User> luser = new ArrayList<User>();
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            User u= this.userDao.getAll(grequest,username);
            luser.add(u);
           
                u.setPassword("");
            
            
            aux.setData(luser);
            aux.setTotal(luser.size());
        } catch (Exception e) {
            logger.error(e);
        }
        
        return aux;
    }

    /**
     * Returns a list of all groups from a user
     *
     * @param iduser User id.
     * @return Return a list of all groups from a user.
     * @throws Exception
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/groups", method = RequestMethod.GET)
    public ExtjsStore getUserGroups(@PathVariable Long iduser) throws Exception {
        
        
        List<Group> groups = new ArrayList<Group>();
        ExtjsStore aux = new ExtjsStore();
        try {
            groups = this.userDao.getGroupsByIdUser(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        for (Group g : groups) {
            for (User u : g.getUsers()) {
                u.setPassword("");
            }
        }
        
        aux.setData(groups);
        aux.setTotal(groups.size());
        return aux;
    }

    /**
     * Returns a list of all roles from a user
     *
     * @param iduser User id.
     * @return Return a list of all roles from a user.
     * @throws Exception
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/roles", method = RequestMethod.GET)
    public ExtjsStore getUserRoles(@PathVariable Long iduser) throws Exception {
        
        
        List<Role> lrole = new ArrayList<Role>();
        ExtjsStore aux = new ExtjsStore();
        try {
            lrole = this.userDao.getRolesByIdUser(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        aux.setData(lrole);
        aux.setTotal(lrole.size());
        return aux;
    }

    /**
     * Modifies the relationship between a user and a role
     *
     * @param iduser User id.
     * @param idrole Role id
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/putrole/{idrole}", method = RequestMethod.PUT)
    public Success putUserRole(@PathVariable Long iduser, @PathVariable Integer idrole) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            this.userDao.putUserRole(iduser, idrole);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        return result;
    }

    /**
     * Removes the relationship between a user and a role
     *
     * @param iduser User id
     * @param idrole Role id.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/removerole/{idrole}", method = RequestMethod.DELETE)
    public Success removeUserRole(@PathVariable Long iduser, @PathVariable Integer idrole) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            this.userDao.deleteUserRole(iduser, idrole);
            result.setSuccess(Boolean.TRUE);
        } catch (Exception e) {
            result.setErrors(new com.abada.extjs.Error(e.getMessage()));
            logger.error(e);
        }
        return result;
    }      
}
