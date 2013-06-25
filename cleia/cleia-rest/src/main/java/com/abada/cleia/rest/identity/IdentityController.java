/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.identity;

import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.security.authentication.dni.DniAuthenticationDao;
import java.security.Principal;
import java.util.Collection;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author katsu
 */
@Controller
@RequestMapping("/rs/identity/user")
public class IdentityController {

    @Resource
    private ApplicationContext context;
    /**
     * Returns all roles of logged users.
     *
     * @param request Do nothing.
     * @return Return all roles of logged users.
     */
    @RequestMapping(value = "/roles/list", method = RequestMethod.GET)
    public Collection<? extends GrantedAuthority> getRoles(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        if (p instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken u = (UsernamePasswordAuthenticationToken) p;
            if (u.getPrincipal() instanceof UserDetails) {
                UserDetails user = (UserDetails) u.getPrincipal();                
                return user.getAuthorities();
            }
        }
        return null;
    }
    
    /**
     * Returns all roles for a user by dni
     *
     * @param dni Dni
     * @return Returns all roles for a user by dni
     */
    @RolesAllowed(value = {"ROLE_ADMIN","ROLE_DNI_CONSULT"})
    @RequestMapping(value = "/dni/roles/list", method = RequestMethod.GET)
    public Collection<? extends GrantedAuthority> getRolesByDni(String dni) throws Exception {   
        DniAuthenticationDao dniAuthenticationDao=context.getBean(DniAuthenticationDao.class);
        if (dniAuthenticationDao!=null){
            UserDetails userDetails=dniAuthenticationDao.getUserByDNI(dni);
            if (userDetails!=null){
                return userDetails.getAuthorities();
            }
            throw new Exception("User with dni "+dni+" not found.");
        }
        throw new Exception("No DniAuthenticationDao bean found.");
    }
    
    /**
     * Returns all roles of logged users.
     *
     * @param request Do nothing.
     * @return Return all roles of logged users.
     */
    @RequestMapping(value = "/groups/list", method = RequestMethod.GET)
    public Collection<Group> getGroups(HttpServletRequest request) {
        Principal p = request.getUserPrincipal();
        if (p instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken u = (UsernamePasswordAuthenticationToken) p;
            if (u.getPrincipal() instanceof User) {
                User user = (User) u.getPrincipal();                
                return user.getGroups();
            }
        }
        return null;
    }       
}
