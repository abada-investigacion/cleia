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

import com.abada.cleia.dao.IdDao;
import com.abada.cleia.entity.user.Id;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
     * get total load
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from Id i" + filters.getQL("i", true), filters.getParamsValues());
        return result.get(0);
    }

    /**
     * get id
     *
     * @param id
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Id getIdById(Long id) {
        return entityManager.find(Id.class, id);

    }

    /**
     * get id by value and type
     *
     * @param value
     * @param type
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true, noRollbackFor = {NoResultException.class})
    public Id getIdByvaluetype(String value, String type) {
        List<Id> ids = entityManager.createQuery("select i from Id i where i.value = :value and i.type.value=:type ").setParameter("value", value).setParameter("type", type).getResultList();
        if (ids != null && !ids.isEmpty()) {
            return ids.get(0);
        } else {
            return null;
        }
    }

    /**
     * get id by user and type
     *
     * @param id
     * @param type
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true, noRollbackFor = {NoResultException.class})
    public Id getIdByusertype(long id, String type) {
        List<Id> ids = entityManager.createQuery("select i from Id i where i.user.id = :id and i.type.value=:type ").setParameter("id", id).setParameter("type", type).getResultList();
        if (ids != null && !ids.isEmpty()) {
            return ids.get(0);
        } else {
            return null;
        }
    }

    /**
     * get all id
     *
     * @param filters
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Id> getAll(GridRequest filters) {
        List<Id> lidtype = this.find(entityManager, "select i from Id i" + filters.getQL("i", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        return lidtype;

    }

    /**
     * insert id
     *
     * @param id
     * @throws Exception
     */
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
    public void deleteId(long pk) throws Exception {
        Id id = (Id) entityManager.find(Id.class, pk);
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
