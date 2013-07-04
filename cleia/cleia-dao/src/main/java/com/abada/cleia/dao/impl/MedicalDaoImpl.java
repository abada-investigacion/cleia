/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.IdDao;
import com.abada.cleia.dao.MedicalDao;
import com.abada.cleia.dao.PatientDao;
import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Medical;
import com.abada.cleia.entity.user.Patient;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.ArrayList;
import java.util.List;
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
public class MedicalDaoImpl extends JpaDaoUtils implements MedicalDao {

    @Resource(name = "medicalDao")
    private static final Log logger = LogFactory.getLog(MedicalDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    @Autowired
    private ShaPasswordEncoder sha1PasswordEncoder;
    @Resource(name = "idDao")
    private IdDao idDao;
    @Resource(name = "patientDao")
    private PatientDao patientDao;
    @Resource(name = "userDao")
    private UserDao userDao;

    /**
     * obtained from medical id
     *
     * @param medicalId
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Medical getMedicalById(long id) {
        Medical result = entityManager.find(Medical.class, id);
        if (result != null) {
            result.getGroups().size();
            result.getRoles().size();
            result.getIds().size();
            result.getProcessInstances().size();
        }
        return result;
    }

    /**
     * obtained all medical
     *
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Medical> getAllMedicals() {
        List<Medical> lmedical = entityManager.createQuery("SELECT m FROM Medical m").getResultList();
        for (Medical medical : lmedical) {
            medical.getGroups().size();
            medical.getRoles().size();
            medical.getIds().size();
            medical.getProcessInstances().size();
        }
        return lmedical;

    }

    /**
     * Search a list of medicals by params
     *
     * @param params
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Medical> getMedicalUser(GridRequest filters, String username) {
        List<Medical> lm = new ArrayList<Medical>();
        Medical m = (Medical) entityManager.createQuery("select m from Medical m where m.username = :username").setParameter("username", username).getSingleResult();
        if (m instanceof Medical) {
            Medical medical = (Medical) m;
            medical.getGroups().size();
            medical.getRoles().size();
            medical.getIds().size();
            medical.getProcessInstances().size();
            lm.add(medical);

        }
        return lm;

    }

    /**
     * Gets the size of {@link Medical}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from Medical m" + filters.getQL("m", true), filters.getParamsValues());
        return result.get(0);
    }

    /**
     * Search a list of medicals by params
     *
     * @param params
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Medical> getAll(GridRequest filters) {
        List<Medical> lmedical = this.find(entityManager, "select m from Medical m" + filters.getQL("m", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        for (Medical medical : lmedical) {
            medical.getGroups().size();
            medical.getRoles().size();
            medical.getIds().size();
            medical.getProcessInstances().size();
        }
        return lmedical;

    }

    /**
     * setting medical
     *
     * @param medical
     * @param p
     */
    @Transactional("cleia-txm")
    public void persistMedical(Medical medical, Medical m) {
        medical.setPatients(m.getPatients());
        patientDao.updatePatient(medical, m);
    }

    /**
     * find medical given id
     *
     * @param asList
     * @return
     * @throws Exception
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Medical> findMedicalsrepeatable(List<Id> asList, Boolean repeatable) throws Exception {
        List<Medical> m = new ArrayList<Medical>();
        if (asList != null && !asList.isEmpty()) {
            int append = 0;
            StringBuilder query = new StringBuilder();
            query.append("SELECT m FROM Medical m join m.ids idss WHERE idss.id in (select distinct pid.id from Id pid where ");
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
                m = entityManager.createQuery(query.toString()).getResultList();

            }
        }
        return m;
    }

    /**
     * get List Id medical
     *
     * @param idmedical
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Id> getIdsForMedical(Long idmedical) {
        List<Medical> lmedical = entityManager.createQuery("SELECT m FROM Medical m WHERE m.id=?").setParameter(1, idmedical).getResultList();
        if (lmedical.size() > 0) {
            return lmedical.get(0).getIds();
        }
        return null;
    }

    /**
     * insert medical
     *
     * @param medical
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void postMedical(Medical medical) throws Exception {
        Medical m = null;
        if (medical.getIds() != null && !medical.getIds().isEmpty()) {
            List<Medical> lmedicals = this.findMedicalsrepeatable(medical.getIds(), false);
            /*Si no hay ningun medico con el mismo identificador lo insertamos*/
            if ((lmedicals.isEmpty() && lmedicals != null)) {
                medical.setPassword(sha1PasswordEncoder.encodePassword(medical.getPassword(), null));
                try {
                    for (Id id : medical.getIds()) {
                        idDao.postId(id);
                    }
                    for (int i = 0; i < medical.getPatients().size(); i++) {
                        List<Patient> lpatients = patientDao.findPatientsrepeatable(medical.getPatients().get(i).getIds(), false);
                        if ((lpatients.isEmpty() && lpatients != null)) {
                            patientDao.postPatientinsert(medical.getPatients().get(i));
                        }
                    }
                    medical.addPatients(medical.getPatients());
                    entityManager.persist(medical);
                } catch (Exception e) {
                    throw new Exception("Error. Ha ocurrido un error al insertar el medico " + medical.getName() + " " + medical.getSurname() + " " + medical.getSurname1(), e);
                }
            } else {
                String name = "";
                for (Medical me : lmedicals) {
                    name = me.getName() + " " + me.getSurname() + " " + me.getSurname() + ", ";
                }
                throw new Exception("Error. Ya existe el medico " + name + " con esos identificadores");
            }
        } else {
            throw new Exception("Error. Ningún identificador enviado");
        }

    }

    /**
     * update id medical
     *
     * @param idmedical
     * @param ids
     * @throws Exception
     */
    public void putMedicalid(Long idmedical, List<Id> ids) throws Exception {
        Medical medical = entityManager.find(Medical.class, idmedical);
        if (medical != null) {
            if (ids != null && !ids.isEmpty()) {
                /*Comprobamos si vienen identificadores repetidos y si ese asi insertamos sino modificamos*/
                for (Id id : medical.getIds()) {
                    id.setUser(medical);
                    Id idbd = idDao.getIdByusertype(id.getUser().getId(), id.getType().getValue());
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
            throw new Exception("Error. El medico no existe");
        }
    }

    /**
     * update medical
     *
     * @param idmedical
     * @param medical
     */
    @Transactional(value = "cleia-txm")
    public void putMedical(Long idmedical, Medical medical) throws Exception {
        Medical medical1 = entityManager.find(Medical.class, idmedical);
        if (medical1 != null) {
            List<Medical> lmedicals = this.findMedicalsrepeatable(medical.getIds(), false);
            if (!lmedicals.isEmpty() && lmedicals != null) {
                if (lmedicals.size() > 1 || (lmedicals.size() == 1 && lmedicals.get(0).getId() != medical.getId())) {
                    for (Medical aux : lmedicals) {
                        if (aux.getId() != medical1.getId()) {
                            throw new Exception("Error. El medico " + aux.getName() + " "
                                    + aux.getSurname() + " " + aux.getSurname1() + " ya tiene asignado uno de los identificadores enviados");
                        }
                    }
                }
            }
            putMedicalid(idmedical, medical.getIds());

            /*Modificamos el medico*/
            try {
                this.persistMedical(medical1, medical);
                userDao.updateUser(medical1, medical);
                patientDao.updatePatient(medical1, medical);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al modificar el medico "
                        + medical.getName() + " " + medical.getSurname() + " " + medical.getSurname());
            }
        } else {
            throw new Exception("Error. El medico no existe");
        }
    }

    /**
     * update medical data
     *
     * @param idmedical
     * @param medical
     */
    @Transactional(value = "cleia-txm")
    public void putMedicalData(Long idmedical, Medical medical) throws Exception {
        Medical medical1 = entityManager.find(Medical.class, idmedical);
        if (medical1 != null) {
            /*Modificamos el medico*/
            try {
                this.persistMedical(medical1, medical);
                userDao.updateUser(medical1, medical);
                patientDao.updatePatient(medical1, medical);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al modificar el medico "
                        + medical1.getName() + " " + medical1.getSurname() + " " + medical1.getSurname1());
            }
        } else {
            throw new Exception("Error. El medico no existe");
        }
    }

    /**
     * enable or disable medical
     *
     * @param idmedical
     * @param enable
     */
    @Transactional(value = "cleia-txm")
    public void enableDisableMedical(Long idmedical, boolean enable) throws Exception {
        Medical medical = entityManager.find(Medical.class, idmedical);
        String habilitar = "";
        if (medical != null) {
            if ((!medical.isEnabled() && enable) || (medical.isEnabled() && !enable)) {
                try {
                    medical.setEnabled(enable);
                } catch (Exception e) {
                    if (enable) {
                        habilitar = "habilitar";
                    } else {
                        habilitar = "deshabilitar";
                    }
                    throw new Exception("Error. Ha ocurrido un error al" + habilitar + " al medico " + medical.getName() + " " + medical.getSurname() + " " + medical.getSurname1());
                }
            } else {
                if (!enable) {
                    habilitar = "deshabilitado";
                } else {
                    habilitar = "habilitado";
                }
                throw new Exception("Error. El medico " + medical.getName() + " " + medical.getSurname() + " " + medical.getSurname1() + " ya esta " + habilitar);
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }
    }
}
