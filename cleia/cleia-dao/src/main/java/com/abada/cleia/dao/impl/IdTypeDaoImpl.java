/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.IdTypeDao;
import com.abada.cleia.entity.user.IdType;
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
public class IdTypeDaoImpl implements IdTypeDao{

    private static final Log logger = LogFactory.getLog(IdTypeDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    
//    @Transactional(value = "cleia-txm", readOnly = true)
//    public Patient getPatientById(long patientId) {
//        Patient result=entityManager.find(Patient.class, patientId);
//        return result;
//    }

    public List<IdType> getAllIdType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long loadSizeAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public IdType getIdTypeById(Integer ididtype) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<IdType> getAll(GridRequest filters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void postIdType(IdType idtype) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putIdType(Integer ididtype, IdType idtype) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteIdType(Integer ididtype) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
