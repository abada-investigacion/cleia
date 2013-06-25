/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.RoleDao;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author katsu
 */
public class RoleDaoImpl implements RoleDao{

    private static final Log logger = LogFactory.getLog(RoleDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    
//    @Transactional(value = "cleia-txm", readOnly = true)
//    public Patient getPatientById(long patientId) {
//        Patient result=entityManager.find(Patient.class, patientId);
//        return result;
//    }

    public List<Role> getAllRoles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long loadSizeAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Role getRoleprivById(Integer idrole) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void postRolepriv(Role role) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putRolepriv(Integer idrolepriv, Role newrole) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteRolepriv(Integer idrolepriv) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Role> getAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<User> getUsersByIdRole(Integer idrolepriv) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
