/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

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
import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.dao.ProcessInstanceDao;
import com.abada.cleia.entity.temporal.PatientHasProcessInstanceInfo;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.PatientHasProcessInstance;
import com.abada.jbpm.integration.console.ProcessManagement;
import com.abada.jbpm.integration.console.task.PatientTaskManagement;
import com.abada.jbpm.process.audit.ProcessInstanceDbLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class ProcessInstanceDaoImpl implements PatientTaskManagement, ProcessInstanceDao {

    private static final Log logger = LogFactory.getLog(ProcessInstanceDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    private PatientDao patientDao;
    private ProcessManagement processManagement;
    private ProcessInstanceDbLog processInstanceDbLog;

    public PatientDao getPatientDao() {
        return patientDao;
    }

    public void setPatientDao(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    public ProcessManagement getProcessManagement() {
        return processManagement;
    }

    public void setProcessManagement(ProcessManagement processManagement) {
        this.processManagement = processManagement;
    }

    public ProcessInstanceDbLog getProcessInstanceDbLog() {
        return processInstanceDbLog;
    }

    public void setProcessInstanceDbLog(ProcessInstanceDbLog processInstanceDbLog) {
        this.processInstanceDbLog = processInstanceDbLog;
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Long> getProcessInstancesIdsForPatient(Long patientId) {
        Patient patient = entityManager.find(Patient.class, patientId);
        if (patient != null && patient.getProcessInstances() != null) {
            List<Long> result = new ArrayList<Long>();
            for (PatientHasProcessInstance pi : patient.getProcessInstances()) {
                result.add(pi.getProcessInstanceId());
            }
            return result;
        }
        return null;
    }

    @Transactional(value = "cleia-txm")
    public void addPInstancePatient(Long patientId, Long processInstanceId) {
        PatientHasProcessInstance add = new PatientHasProcessInstance();
        add.setProcessInstanceId(processInstanceId);
        Patient patient = entityManager.find(Patient.class, patientId);
        patient.addPatientHasProcessInstance(add);
        entityManager.persist(add);
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public PatientHasProcessInstanceInfo getProcessInstanceFromProcessIntance(Long patientId, Long pInstance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<PatientHasProcessInstanceInfo> getProcessInstance(Long patientId) {
        Patient patient = patientDao.getPatientById(patientId);
        return getProcessInstancePriv(patient);
    }
    
    public List<PatientHasProcessInstanceInfo> getProcessInstance(String username) {
        Patient patient=patientDao.getPatientByUsername(username);
        return getProcessInstancePriv(patient);
    }

    private List<PatientHasProcessInstanceInfo> getProcessInstancePriv(Patient patient) {
        if (patient != null && patient.getProcessInstances() != null && !patient.getProcessInstances().isEmpty()) {
            List<PatientHasProcessInstanceInfo> result = new ArrayList<PatientHasProcessInstanceInfo>();
            for (PatientHasProcessInstance og : patient.getProcessInstances()) {
                //if (og instanceof Oncoguide) {
                ProcessInstanceLog pil = processInstanceDbLog.findProcessInstance(og.getProcessInstanceId());
                if (pil != null) {
                    PatientHasProcessInstanceInfo po = new PatientHasProcessInstanceInfo();
                    po.setPatientId(patient.getId());
                    /*try {
                     po.setProcessName(processManagement.getProcessDefinition(pil.getProcessId()).getName());
                     } catch (Exception e) {
                     logger.warn("Process without name. " + pil.getProcessId());
                     }*/
                    po.setProcessName(pil.getProcessId());
                    po.setProcessId(pil.getProcessId());
                    po.setProcessInstanceId(((Long) pil.getProcessInstanceId()).toString());
                    po.setStart(pil.getStart());
                    po.setEnd(pil.getEnd());
                    result.add(po);
                }
                //}
            }
            Collections.sort(result, new PIComparator());
            return result;
        }
        return null;
    }

    private class PIComparator implements Comparator<PatientHasProcessInstanceInfo> {

        public int compare(PatientHasProcessInstanceInfo o1, PatientHasProcessInstanceInfo o2) {
            long id1 = Long.parseLong(o1.getProcessInstanceId());
            long id2 = Long.parseLong(o2.getProcessInstanceId());
            if (id1 == id2) {
                return 0;
            } else if (id1 > id2) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
