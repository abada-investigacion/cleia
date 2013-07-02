/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.IdDao;
import com.abada.cleia.entity.user.Id;
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
public class IdDaoImpl extends JpaDaoUtils implements IdDao {

    private static final Log logger = LogFactory.getLog(IdDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;

    /**
     * Obtiene el tamaño de {@link Id}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from Id i" + filters.getQL("i", true), filters.getParamsValues());
        return result.get(0);
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public Id getIdById(Long id) {
        return entityManager.find(Id.class, id);

    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public Id getIdByvaluetype(String value, String type) {
        return (Id) entityManager.createQuery("select u from Id i where i.value = :value and i.type.value=:type ").setParameter("value", value).setParameter("type", type).getSingleResult();
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Id> getAll(GridRequest filters) {

        List<Id> lidtype = this.find(entityManager, "select i from Id i" + filters.getQL("i", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        return lidtype;

    }

    @Transactional(value = "cleia-txm")
    public void postId(Id id) throws Exception {
        id.addUser(id.getUser());
        entityManager.persist(id);
    }

    /**
     * add id repeatable or update not repeatable
     *
     * @param id
     * @param newid
     * @param repeatable
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void putId(Id idt, Id newid) throws Exception {

        if (idt.getType().isRepeatable()) {
            postId(newid);
        } else {
            if (idt != null) {
                idt.setType(newid.getType());
                idt.setUser(newid.getUser());
                idt.setValue(newid.getValue());
            }
        }
    }

    /**
     * delete id
     *
     * @param value
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void deleteId(String value) throws Exception {
        Id id = (Id) entityManager.find(Id.class, value);
        entityManager.remove(id);
    }

    /**
     *
     * @return
     */
    @Transactional(value = "cleia-txm")
    public List<Id> getAll() {
        List<Id> id = entityManager.createQuery("SELECT i FROM Id i").getResultList();
        return id;
    }
}
