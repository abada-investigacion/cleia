/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.IdDao;
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
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class PatientDaoImpl extends JpaDaoUtils implements PatientDao {

    private static final Log logger = LogFactory.getLog(PatientDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    @Autowired
    private ShaPasswordEncoder sha1PasswordEncoder;
    @Resource(name = "idDao")
    private IdDao idDao;

    /**
     * obtained from patient id
     *
     * @param patientId
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Patient getPatientById(long patientId) {
        Patient result = entityManager.find(Patient.class, patientId);
        if (result != null) {
            result.getGroups().size();
            result.getRoles().size();
            result.getIds().size();
            result.getProcessInstances().size();
        }
        return result;
    }

    /**
     * obtained all patient
     *
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> getAllPatients() {
        List<Patient> lpatient = entityManager.createQuery("SELECT p FROM Patient p").getResultList();
        for (Patient patient : lpatient) {
            patient.getGroups().size();
            patient.getRoles().size();
            patient.getIds().size();
            patient.getProcessInstances().size();
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
        List<Patient> lp = new ArrayList<Patient>();
        User user = (User) entityManager.createQuery("select u from User u where u.username = :username").setParameter("username", username).getSingleResult();
        if (user instanceof Patient) {
            Patient patient = (Patient) user;
            patient.getGroups().size();
            patient.getRoles().size();
            patient.getIds().size();
            patient.getProcessInstances().size();
            lp.add(patient);

        }
        return lp;

    }

    /**
     * Gets the size of {@link Patient}
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
        }
        return lpatient;

    }

    /**
     * setting patient
     *
     * @param patient
     * @param p
     */
    @Transactional("cleia-txm")
    public void updatePatient(Patient patient, Patient p) {
        patient.setGenre(p.getGenre());
        patient.setName(p.getName());
        patient.setAddress(p.getAddress());
        patient.setSurname(p.getSurname());
        patient.setSurname1(p.getSurname1());
        patient.setBirthDay(p.getBirthDay());
        patient.setProcessInstances(p.getProcessInstances());
    }

    /**
     * find patient given id
     *
     * @param asList
     * @return
     * @throws Exception
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Patient> findPatientsrepeatable(List<Id> asList, Boolean repeatable) throws Exception {
        List<Patient> p = new ArrayList<Patient>();
        if (asList != null && !asList.isEmpty()) {
            int append = 0;
            StringBuilder query = new StringBuilder();
            query.append("SELECT p FROM Patient p join p.ids idss WHERE idss.id in (select distinct pid.id from Id pid where ");
            for (Id pid : asList) {
                if (pid.getValue() != null && !pid.getValue().equals("") && pid.getType() != null && pid.getType().getValue() != null) {
                    append++;
                    if (append != 1) {
                        query.append(" or ");
                    }
                    query.append("pid.value='").append(pid.getValue()).append("'");
                    if (repeatable != null) {
                        query.append(" and pid.type.repeatable=").append(repeatable);
                    }

                    query.append(" and pid.type.value='").append(pid.getType().getValue()).append("'");
                } else {
                    throw new Exception("Error. Ha ocurrido un error en uno de los identificadores");
                }
            }
            if (append != 0) {
                query.append(")");
                p = entityManager.createQuery(query.toString()).getResultList();
                for (Patient patient : p) {
                    patient.getGroups().size();
                    patient.getRoles().size();
                    patient.getIds().size();
                    patient.getProcessInstances().size();
                }

            }
        }
        return p;
    }

  

    /**
     * get List Id patient
     *
     * @param idpatient
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Id> getIdsForPatient(Long idpatient) {
        List<Patient> lp = entityManager.createQuery("SELECT u FROM Patient p WHERE p.id=?").setParameter(1, idpatient).getResultList();
        if (lp.size() > 0) {
            return lp.get(0).getIds();
        }
        return null;
    }

    /**
     * insert patient
     *
     * @param patient
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void postPatient(Patient patient) throws Exception {
        Patient p = null;
        if (patient.getIds() != null && !patient.getIds().isEmpty()) {
            List<Patient> lpatients = this.findPatientsrepeatable(patient.getIds(), false);
            /*Si no hay ningun paciente con el mismo identificador lo insertamos*/
            if ((lpatients.isEmpty() && lpatients != null)) {
                patient.setPassword(sha1PasswordEncoder.encodePassword(patient.getPassword(), null));
                try {
                    for (Id id : patient.getIds()) {
                        idDao.postId(id);
                    }
                    entityManager.persist(patient);
                } catch (Exception e) {
                    throw new Exception("Error. Ha ocurrido un error al insertar el paciente " + patient.getName() + " " + patient.getSurname() + " " + patient.getSurname1() + " " + e.toString());
                }
            } else {
                String name = "";
                for (Patient pat : lpatients) {
                    name = pat.getName() + " " + pat.getSurname() + " " + pat.getSurname() + ", ";
                }
                throw new Exception("Error. Ya existe el paciente " + name + " con esos identificadores");
            }
        } else {
            throw new Exception("Error. Ningún identificador enviado");
        }

    }

    
    
      /**
     * update id patient
     *
     * @param idpatient
     * @param ids
     * @throws Exception
     */
    public void putPatientid(Long idpatient, List<Id> ids) throws Exception {
        Patient patient = entityManager.find(Patient.class, idpatient);
        if (patient != null) {
            if (ids != null && !ids.isEmpty()) {
                /*Comprobamos si vienen identificadores repetidos y si ese asi insertamos sino modificamos*/
                for (Id id : patient.getIds()) {
                    Id idbd = idDao.getIdByvaluetype(id.getValue(), id.getType().getValue());
                    if (idbd != null) {//modificadomos id actual
                        idDao.putId(idbd, id);
                    } else {
                        idDao.postId(id);
                    }
                }


            } else {
                throw new Exception("Error. Ningún identificador enviado");
            }

        } else {
            throw new Exception("Error. El paciente no existe");
        }
    }
    /**
     * update patient
     *
     * @param idpatient
     * @param patient
     */
    @Transactional(value = "cleia-txm")
    public void putPatient(Long idpatient, Patient patient) throws Exception {
        Patient patient1 = entityManager.find(Patient.class, idpatient);
        if (patient1 != null) {
            List<Patient> lpatients = this.findPatientsrepeatable(patient.getIds(), false);
            if (!lpatients.isEmpty() && lpatients != null) {
                if (lpatients.size() > 1 || (lpatients.size() == 1 && lpatients.get(0).getId() != patient.getId())) {
                    for (Patient aux : lpatients) {
                        if (aux.getId() != patient1.getId()) {
                            throw new Exception("Error. El paciente " + aux.getName() + " "
                                    + aux.getSurname() + " " + aux.getSurname1() + " ya tiene asignado uno de los identificadores enviados");
                        }
                    }
                }
            }
            putPatientid(idpatient, patient.getIds());
            /*Modificamos el paciente*/
            try {
                this.updatePatient(patient1, patient);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al modificar el paciente "
                        + patient.getName() + " " + patient.getSurname() + " " + patient.getSurname());
            }
        } else {
            throw new Exception("Error. El paciente no existe");
        }
    }

    /**
     * update patient data
     *
     * @param idpatient
     * @param patient
     */
    @Transactional(value = "cleia-txm")
    public void putPatientData(Long idpatient, Patient patient) throws Exception {
        Patient patient1 = entityManager.find(Patient.class, idpatient);
        if (patient1 != null) {
            /*Modificamos el paciente*/
            try {
                this.updatePatient(patient1, patient);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al modificar el paciente "
                        + patient1.getName() + " " + patient1.getSurname() + " " + patient1.getSurname1());
            }
        } else {
            throw new Exception("Error. El paciente no existe");
        }
    }

    /**
     *
     * @param idpatient
     * @param enable
     */
    @Transactional(value = "cleia-txm")
    public void enableDisablePatient(Long idpatient, boolean enable) throws Exception {
        Patient patient = entityManager.find(Patient.class, idpatient);
        String habilitar = "";
        if (patient != null) {
            if ((!patient.isEnabled() && enable) || (patient.isEnabled() && !enable)) {
                try {
                    patient.setEnabled(enable);
                } catch (Exception e) {
                    if (enable) {
                        habilitar = "habilitar";
                    } else {
                        habilitar = "deshabilitar";
                    }
                    throw new Exception("Error. Ha ocurrido un error al" + habilitar + " al paciente " + patient.getName() + " " + patient.getSurname() + " " + patient.getSurname1());
                }
            } else {
                if (!enable) {
                    habilitar = "deshabilitado";
                } else {
                    habilitar = "habilitado";
                }
                throw new Exception("Error. El paciente " + patient.getName() + " " + patient.getSurname() + " " + patient.getSurname1() + " ya esta " + habilitar);
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }
    }

 

  
}
