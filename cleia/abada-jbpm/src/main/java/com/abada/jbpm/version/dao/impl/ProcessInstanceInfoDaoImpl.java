/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.version.dao.impl;

/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
