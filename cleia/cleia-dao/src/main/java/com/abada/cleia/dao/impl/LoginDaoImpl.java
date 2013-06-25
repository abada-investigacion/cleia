package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.jbpm.integration.console.task.GroupTaskManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.security.authentication.dni.DniAuthenticationDao;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class LoginDaoImpl extends JpaDaoUtils implements GroupTaskManagement, UserDetailsService,DniAuthenticationDao,UserDao {

    private static final Log logger = LogFactory.getLog(LoginDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;

    @Transactional(value = "cleia-login-txm", readOnly = true)
    public User getUserByDNI(String dni) {
        User result = (User) entityManager.createQuery("select id.user from Id id where id.value = :dni and id.type.value =  'DNI'").setParameter("dni", dni).getSingleResult();
        if (result != null) {
            result.getGroups().size();
            result.getRoles().size();
            result.setPassword("");
        }
        return result;
    }

    @Transactional(value = "cleia-login-txm", readOnly = true)
    public List<String> getGroupByUserName(String username) {
        try {
            User user = this.loadUserByUsernameWithoutPass(username);
            
            List<String> result = new ArrayList<String>();
            if (user.getGroups() != null) {
                for (Group g : user.getGroups()) {
                    result.add(g.getValue());
                }
            }
            return result;
        } catch (UsernameNotFoundException u) {
            if (logger.isErrorEnabled()) {
                logger.error(u);
            }
        }
        return null;
    }

    @Transactional(value = "cleia-login-txm", readOnly = true)
    public User loadUserByUsernameWithoutPass(String username) throws UsernameNotFoundException{
        User result=loadUserByUsername(username);
        result.setPassword("");
        return result;
    }
    
    @Transactional(value = "cleia-login-txm", readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User result = (User)entityManager.createQuery("select u from User u where u.username=:username").setParameter("username", username).getSingleResult();
        if (result == null) {
            throw new UsernameNotFoundException(username);
        }
        result.getGroups().size();
        result.getRoles().size();
        return result;
    }

    public User getAll(GridRequest filters, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addPatient2User(String username, Patient p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long loadSizeAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public User getUserById(Long iduser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void postUser(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putUser(Long iduser, User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void enableDisableUser(Long iduser, boolean enable) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<User> getAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Group> getGroupsByIdUser(Long iduser) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Role> getRolesByIdUser(Long iduser) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putUserGroup(Long iduser, Long idgroup) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteUserGroup(Long iduser, Long idgroup) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putUserRole(Long iduser, Integer idrole) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteUserRole(Long iduser, Integer idrole) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getUserGroup(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
