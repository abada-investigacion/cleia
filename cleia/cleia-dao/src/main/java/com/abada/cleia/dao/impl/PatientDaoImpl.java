/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.IdType;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class PatientDaoImpl extends JpaDaoUtils implements PatientDao {

    private static final Log logger = LogFactory.getLog(PatientDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;

    @Transactional(value = "cleia-txm", readOnly = true)
    public Patient getPatientById(long patientId) {
        Patient result = entityManager.find(Patient.class, patientId);
        if (result != null) {
            for (Id pid : result.getIds()) {
                pid.getType();
                pid.getUser();
            }
        }
        return result;
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> getAllPatients() {

        List<Patient> lpatient = entityManager.createQuery("SELECT p FROM Patient p").getResultList();
        for (Patient patient : lpatient) {
            patient.getIds().size();
        }
        return lpatient;

    }

    /**
     * Search a list of patients by params
     *
     * @param params
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> getPatientUser(GridRequest filters, String username) {
        User user = (User) entityManager.createQuery("select u from User u where u.username = :username").setParameter("username", username).getSingleResult();
        List<Patient> lpatient;

        Map<String, Object> aux = filters.getParamsValues();
        if (user.getIds().size() >= 1) {
            aux.put("Ids", user.getIds());
            lpatient = this.find(entityManager, "select p from Patient p where p.ids in (:Ids) " + filters.getQL("p", false), aux, filters.getStart(), filters.getLimit());
        } else {
            lpatient = new ArrayList();

        }
        for (Patient patient : lpatient) {
            for (Id pid : patient.getIds()) {
                pid.getType();
                pid.getUser();
            }

        }

        return lpatient;

    }

    /**
     * Obtiene el tamaño de {@link Patient}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeuserpatient(GridRequest filters, String username) {
        User user = (User) entityManager.createQuery("select u from User u where u.username = :username").setParameter("username", username).getSingleResult();
        Map<String, Object> aux = filters.getParamsValues();
        List<Long> result = new ArrayList();
        if (user.getIds().size() >= 1) {
            aux.put("ids", user.getIds());
            result = this.find(entityManager, "select count(*) from Patient p where p.ids in (:ids) " + filters.getQL("p", false), aux, filters.getStart(), filters.getLimit());
        } else {
            result.add(new Long(0));

        }
        return result.get(0);
    }

    /**
     * Obtiene el tamaño de {@link Patient}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from Patient p" + filters.getQL("p", true), filters.getParamsValues());
        return result.get(0);
    }

    /**
     * Search a list of patients by params
     *
     * @param params
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> getAll(GridRequest filters) {
        List<Patient> lpatient = this.find(entityManager, "select p from Patient p" + filters.getQL("p", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        for (Patient patient : lpatient) {
            patient.getGroups().size();
            patient.getRoles().size();
            patient.getIds().size();
            patient.getProcessInstances().size();
            /*for (Id pid : patient.getIds()) {
                pid.getType();
                pid.getUser();
            }*/
        }
        return lpatient;

    }

    @Transactional("cleia-txm")
    public void persistPatient(Patient patient, Patient p) {
        patient.setGenre(p.getGenre());
        patient.setName(p.getName());
        patient.setAddress(p.getAddress());
        patient.setSurname(p.getSurname());
        patient.setSurname1(p.getSurname1());
        patient.setBirthDay(p.getBirthDay());
        patient.setMedicals(p.getMedicals());
        patient.setProcessInstances(p.getProcessInstances());
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> findPatients(List<Id> asList) throws Exception {
        List<Patient> p = new ArrayList<Patient>();
        if (asList != null && !asList.isEmpty()) {
            int append = 0;
            StringBuilder query = new StringBuilder();
            query.append("from Patient p where p.ids in (select distinct pid.id from Id pid where ");
            for (Id pid : asList) {
                if (pid.getValue() != null && !pid.getValue().equals("") && pid.getType() != null && pid.getType().getValue() != null) {
                    append++;
                    if (append != 1) {
                        query.append(" or ");
                    }
                    query.append("pid.value='").append(pid.getValue()).append("' and pid.type.value='").append(pid.getType().getValue()).append("'");
                } else {
                    throw new Exception("Error. Ha ocurrido un error en uno de los identificadores");
                }
            }
            if (append != 0) {

                p = entityManager.createQuery(query.toString()).getResultList();
                for (Patient patient : p) {
                    patient.getIds().size();
                }
            }
        }
        return p;
    }
    
     @Transactional(value = "cleia-txm", readOnly = true)
    public void findRepeatIdtypes(List<Id> ids) throws Exception {
        List<IdType> lidtyperepeat = entityManager.createQuery("from IdType i where i.repeatable=1").getResultList();
        List<String> keys = new ArrayList<String>(), values = new ArrayList<String>();
        List<String> lidtype = entityManager.createQuery("SELECT i.value FROM IdType i").getResultList();

        for (Id pid : ids) {

            if (lidtype.contains(pid.getType().getValue()) && lidtype != null) {
                //Comprobamos si el tipo de identificador esta repetido
                if (keys.size() > 0 && values.size() > 0) {
                    if (keys.contains(pid.getType().getValue()) && !lidtyperepeat.contains(pid.getType())) {
                        throw new Exception("Error. El identificador " + pid.getType().getValue()+ " no se puede repetir");
                    }

                    if (values.contains(pid.getType().getValue() + " " + pid.getValue())) {
                        throw new Exception("Error. El identificador " + pid.getType().getValue() + " " + pid.getValue() + " esta repetido");
                    }
                }

                keys.add(pid.getType().getValue());
                values.add(pid.getType().getValue() + " " + pid.getValue());

            } else {
                throw new Exception("Error. El tipo de identificador no existe");
            }
        }
    }

    /**
     * get List Id patient
     *
     * @param idpatient
     * @return
     */
    public List<Id> getIdsForPatient(Long idpatient) {
        List<Id> lpatientid = entityManager.createQuery("SELECT p FROM Id p WHERE p.user.id=?").setParameter(1, idpatient).getResultList();
        return lpatientid;
    }

    public void postPatient(Patient patient) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putPatient(Long idpatient, Patient patient) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putPatientData(Long idpatient, Patient patient) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void enableDisablePatient(Long idpatient, boolean enable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void putPatientid(Long idpatient, List<Id> ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
