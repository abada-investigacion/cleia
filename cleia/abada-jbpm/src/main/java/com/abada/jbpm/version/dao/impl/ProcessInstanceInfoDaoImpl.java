/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.version.dao.impl;

import com.abada.jbpm.version.dao.ProcessInstanceInfoDao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class ProcessInstanceInfoDaoImpl implements ProcessInstanceInfoDao{

    @PersistenceContext(unitName = "com.abada.drools.persistence.jpa")
    private EntityManager entityManager;
    
    @Transactional(value="jbpm-txm",rollbackFor={Exception.class})
    public boolean updateProcessId(String processId, Long id) {        
        if (entityManager.createNativeQuery("update processinstanceinfo set processid=? where instanceid=? ").setParameter(1, processId).setParameter(2, id).executeUpdate()>0){
            return entityManager.createNativeQuery("update processinstancelog set processid=? where processinstanceid=? ").setParameter(1, processId).setParameter(2, id).executeUpdate()>0;
        }
        return false;
    }
    
}
