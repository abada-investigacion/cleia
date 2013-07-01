/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.role;

import com.abada.cleia.dao.RoleDao;
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
    public ExtjsStore getAllRolepriv() {
        
        ExtjsStore aux = new ExtjsStore();
        List<Role> listroles = roleprivDao.getAllRoles();
        aux.setTotal(listroles.size());
        aux.setData(listroles);
        return aux;
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
    public ExtjsStore getRoleUsers(@PathVariable Integer idrole) throws Exception {
        
        
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
        
        return aux;
    }

    /**
     * Returns one role by id
     *
     * @param idrolepriv Role id.
     * @return Return rolepriv structure
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrolepriv}", method = RequestMethod.GET)
    public Role getRoleprivById(@PathVariable Integer idrolepriv) {
        
        Role role=null;
        try {
            role = roleprivDao.getRoleById(idrolepriv);
        } catch (Exception e) {
            logger.error(e);
        }
        
        return role;
    }

    /**
     * Insert a role
     *
     * @param role Rolepriv structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(method = RequestMethod.POST)
    public Success postRolepriv(@RequestBody Role role) {
        
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
     * @param idrolepriv Role id.
     * @param rolepriv Rolepriv structure. Must set in JSON in the Http body
     * request.
     * @return Return success structure.
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrolepriv}", method = RequestMethod.PUT)
    public Success putRolepriv(@PathVariable Integer idrolepriv, @RequestBody Role rolepriv) throws Exception {
        
        Success result = new Success(Boolean.FALSE);
        try {
            roleprivDao.putRole(idrolepriv, rolepriv);
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
     * @param idrolepriv Role id.
     * @return Return success structure
     */
    @RolesAllowed(value = {"ROLE_ADMIN", "ROLE_ADMINISTRATIVE"})
    @RequestMapping(value = "/{idrolepriv}", method = RequestMethod.DELETE)
    public Success deleteRolepriv(@PathVariable Integer idrolepriv) {
        
        Success result = new Success(Boolean.FALSE);
        try {
            roleprivDao.deleteRole(idrolepriv);
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
    public ExtjsStore getSearchRolepriv(String filter, String sort, Integer limit, Integer start) {
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
        
        return aux;
    }
}
