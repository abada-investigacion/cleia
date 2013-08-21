/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.user;

import com.abada.cleia.dao.GroupDao;
import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
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
    public void getAllUsers(Model model) {
        
        ExtjsStore aux = new ExtjsStore();
        List<User> listusers = userDao.getAllUsers();
        
        for (User u : listusers) {
            u.setPassword("");
        }
        
        aux.setTotal(listusers.size());
        aux.setData(listusers);
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }

    /**
     * Returns one user by id
     *
     * @param iduser User id
     * @return Returns a user
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}", method = RequestMethod.GET)
    public void getUserById(@PathVariable Long iduser,Model model) {
        
        User user = new User();
        try {
            user = userDao.getUserById(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        model.addAttribute(JsonView.JSON_VIEW_RESULT, user);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    public void getSearchUser(String filter, String sort, Integer limit, Integer start,Model model) {
        
        List<User> luser;
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            luser = this.userDao.getAll(grequest);           
            
            aux.setData(luser);
            aux.setTotal(this.userDao.loadSizeAll(grequest).intValue());
        } catch (Exception e) {
            logger.error(e);
        }
        
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Level1.class);
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
    public void getSearchUser(@PathVariable String username,String filter, String sort, Integer limit, Integer start,Model model) {
        
        List<User> luser = new ArrayList<User>();
        ExtjsStore aux = new ExtjsStore();
        try {
            GridRequest grequest = GridRequestFactory.parse(sort, start, limit, filter);
            User u= this.userDao.getUserByUsername(grequest,username);
            luser.add(u);
           
                u.setPassword("");
            
            
            aux.setData(luser);
            aux.setTotal(luser.size());
        } catch (Exception e) {
            logger.error(e);
        }
        
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    public void getUserGroups(@PathVariable Long iduser,Model model) throws Exception {
        
        
        List<Group> groups = new ArrayList<Group>();
        ExtjsStore aux = new ExtjsStore();
        try {
            groups = this.userDao.getGroupsByIdUser(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        for (Group g : groups) {
            g.getUsers().clear();
        }
        
        aux.setData(groups);
        aux.setTotal(groups.size());
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    public void getUserRoles(@PathVariable Long iduser,Model model) throws Exception {
        
        
        List<Role> lrole = new ArrayList<Role>();
        ExtjsStore aux = new ExtjsStore();
        try {
            lrole = this.userDao.getRolesByIdUser(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        aux.setData(lrole);
        aux.setTotal(lrole.size());
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
    }
    /**
     * Returns a list of all ids from a user
     *
     * @param iduser User id.
     * @return Return a list of all roles from a user.
     * @throws Exception
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{iduser}/ids", method = RequestMethod.GET)
    public void getUserIds(@PathVariable Long iduser,Model model) throws Exception {
        
        
        List<Id> lid = new ArrayList<Id>();
        ExtjsStore aux = new ExtjsStore();
        try {
            lid = this.userDao.getIdsByIdUser(iduser);
        } catch (Exception e) {
            logger.error(e);
        }
        
        aux.setData(lid);
        aux.setTotal(lid.size());
        model.addAttribute(JsonView.JSON_VIEW_RESULT, aux);
        model.addAttribute(JsonView.JSON_VIEW_CLASS, Views.Public.class);
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
    
    /**
     * Return all Patient Genre in a ExtjsStore structure
     *
     * @return
     */
    @RequestMapping(value = "/withoutAssignedPatient/combo", method = RequestMethod.GET)
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_ADMINISTRATIVE"})
    public ExtjsStore getUserWithoutAssignedPatientCombo() {
       
        List<ComboBoxResponse> data = new ArrayList<ComboBoxResponse>();
        List<User> luser = new ArrayList<User>();
        try {
            luser = this.userDao.getUserWithoutAssignedPatient();
        } catch (Exception e) {
            logger.error(e);
        }
         for (User u : luser) {
            ComboBoxResponse aux = new ComboBoxResponse();
            aux.setId(u.getId()+"");
            aux.setValue(u.getUsername());
            data.add(aux);
        }
        

        ExtjsStore result = new ExtjsStore();
        result.setData(data);
        return result;
    }
}
