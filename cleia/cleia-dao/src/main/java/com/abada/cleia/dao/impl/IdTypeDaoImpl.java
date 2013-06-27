/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.IdTypeDao;
import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.IdType;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class IdTypeDaoImpl extends JpaDaoUtils implements IdTypeDao {

    private static final Log logger = LogFactory.getLog(IdTypeDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;

//    @Transactional(value = "cleia-txm", readOnly = true)
//    public Patient getPatientById(long patientId) {
//        Patient result=entityManager.find(Patient.class, patientId);
//        return result;
//    }
    /**
     * Obtiene el tamaño de {@link IdType}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from IdType i" + filters.getQL("i", true), filters.getParamsValues());
        return result.get(0);
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public IdType getIdTypeById(Integer ididtype) {

        IdType idtype = entityManager.find(IdType.class, ididtype);
        if (idtype != null) {
            return idtype;
        }

        return null;

    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<IdType> getAll(GridRequest filters) {

        List<IdType> lidtype = this.find(entityManager, "select i from IdType i" + filters.getQL("i", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        return lidtype;

    }

    @Transactional(value = "cleia-txm")
    public void postIdType(IdType idtype) throws Exception {
        List<IdType> lidtype = entityManager.createQuery("select i from IdType i where i.name=?").setParameter(1, idtype.getValue()).getResultList();
        if (lidtype.isEmpty()) {
            IdType idt = new IdType();
            try {
                idt.setValue(idtype.getValue());
                idt.setRepeatable(idtype.isRepeatable());
                entityManager.persist(idt);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al insertar el tipo de identificador " + idtype.getValue(), e);
            }
        } else {
            throw new Exception("Error. El tipo de identificador " + idtype.getValue() + " ya existe.");
        }
    }

    @Transactional(value = "cleia-txm")
    public void putIdType(Integer ididtype, IdType idtype) throws Exception {

        IdType idt = entityManager.find(IdType.class, ididtype);
        if (idtype != null) {
            List<IdType> lidtype = entityManager.createQuery("select i from IdType i where i.name=?").setParameter(1, idtype.getValue()).getResultList();
            if (lidtype.isEmpty() || idt.getValue().equals(idtype.getValue())) {
                try {
                    idt.setValue(idtype.getValue());
                    idt.setRepeatable(idtype.isRepeatable());
                } catch (Exception e) {
                    throw new Exception("Error. Ha ocurrido un error al modificar el tipo de identificador " + idt.getValue(), e);
                }
            } else {
                throw new Exception("Error. El tipo de identificador " + idtype.getValue() + " ya existe.");
            }
        } else {
            throw new Exception("Error. El tipo de identifiacdor no existe.");
        }
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void deleteIdType(String value) throws Exception {
        IdType idtype = (IdType) entityManager.find(IdType.class, value);
        List<Id> ids = entityManager.createQuery("select id from Id id where id.type.value=?").setParameter(1, value).getResultList();


        if (ids.size() > 0) {
            throw new Exception("Error. El tipo de identificador " + idtype.getValue() + " no puede ser borrado."
                    + " Compruebe que no este asignado a ningun identificador de paciente.");
        }
        if (idtype != null) {
            try {
                entityManager.remove(idtype);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al borrar el tipo de identificador " + idtype.getValue(), e);
            }
        } else {
            throw new Exception("Error. No se puede borrar el tipo de identificador. Compruebe que exista.");
        }
    }

    /**
     *
     * @return
     */
    @Transactional(value = "cleia-txm")
    public List<IdType> getAllIdType() {
        List<IdType> lidtype = entityManager.createQuery("SELECT u FROM IdType u").getResultList();
        return lidtype;
    }
}
