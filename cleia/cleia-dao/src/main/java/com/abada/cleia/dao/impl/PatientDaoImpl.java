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
            for (Id pid : result.getIds()) {
                pid.getType();
                pid.getUser();
            }
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
     * Gets the size {@link Patient}
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
            /*for (Id pid : patient.getIds()) {
                pid.getType();
                pid.getUser();
            }*/
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

    /**
     * find patient given id
     *
     * @param asList
     * @return
     * @throws Exception
     */
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

    /**
     * find patient given id
     *
     * @param ids
     * @throws Exception
     */
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
                        throw new Exception("Error. El identificador " + pid.getType().getValue() + " no se puede repetir");
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
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Id> getIdsForPatient(Long idpatient) {
        List<Id> ids = entityManager.createQuery("SELECT p FROM Id p WHERE p.user.id=?").setParameter(1, idpatient).getResultList();
        return ids;
    }

    /**
     * insert patient
     *
     * @param patient
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void postPatient(Patient patient) throws Exception {
        Patient p;
        if (patient.getIds() != null && !patient.getIds().isEmpty()) {
            List<Patient> lpatients = this.findPatients(patient.getIds());
            /*Si no hay ningun paciente con el mismo identificador lo insertamos*/
            if ((lpatients.isEmpty() && lpatients != null)) {
                p = new Patient();
                this.persistPatient(p, patient);
                /*Insertamos los patientid*/
                this.findRepeatIdtypes(patient.getIds());
                for (Id pi : patient.getIds()) {
                    List<IdType> lidtype = entityManager.createQuery("SELECT i FROM Idtype i WHERE i.name = ?").setParameter(1, pi.getType().getValue()).getResultList();
                    if (!lidtype.isEmpty() && lidtype != null) {
                        if (lidtype.size() == 1) {
                            try {
                                pi.getType().setValue(lidtype.get(0).getValue());

                            } catch (Exception e) {
                                throw new Exception("Error. Ha ocurrido un error al insertar uno de los identificadores");
                            }
                        } else {
                            throw new Exception("Error. El tipo de identificador esta repetido");
                        }
                    } else {
                        throw new Exception("Error. El tipo de identificador no existe");
                    }
                }
                p.setIds(patient.getIds());
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
        try {
            entityManager.persist(p);
        } catch (Exception e) {
            throw new Exception("Error. Ha ocurrido un error al insertar el paciente " + patient.getName() + " " + patient.getSurname() + " " + patient.getSurname1());
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
            List<Patient> lpatients = this.findPatients(patient.getIds());
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
                this.persistPatient(patient, patient);
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
                this.persistPatient(patient1, patient);
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

    /**
     * delete patient old
     *
     * @param patient
     * @param ids
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void deleteOldId(Patient patient, List<Id> ids) throws Exception {
        Id pidtodelete = new Id();
        List<Id> listtodelete = new ArrayList<Id>();
        for (Id pi : patient.getIds()) {
            if (!ids.contains(pi)) {
                listtodelete.add(pi);
            }
        }

        if (!listtodelete.isEmpty() && listtodelete != null) {
            for (Id pid : listtodelete) {
                pidtodelete = entityManager.find(Id.class, pid.getId());
                patient.getIds().remove(pidtodelete);
                entityManager.remove(pidtodelete);
            }
        }
        entityManager.flush();
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


                /*Comprobamos si vienen identificadores repetidos.En caso de que vengan
                 comprobamos si se pueden repetir. Tambien se comprueba si el tipo de identificador
                 existe.*/
                findRepeatIdtypes(ids);
                deleteOldId(patient, ids);
                for (Id pi : ids) {
                    List<IdType> lidtype = entityManager.createQuery("SELECT i FROM IdType i WHERE i.name = ?").setParameter(1, pi.getType().getValue()).getResultList();
                    if (!lidtype.isEmpty() && lidtype != null) {
                        if (lidtype.size() == 1) {
                            try {
                                pi.getType().setValue(lidtype.get(0).getValue());
                                entityManager.persist(pi);

                            } catch (Exception e) {
                                throw new Exception("Error. Ha ocurrido un error al insertar uno de los identificadores");
                            }
                        } else {
                            throw new Exception("Error. El tipo de identificador esta repetido");
                        }
                    }
                }

            } else {
                throw new Exception("Error. Ningún identificador enviado");
            }

        } else {
            throw new Exception("Error. El paciente no existe");
        }
    }
}
