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
    public IdType getIdTypeById(String value) {
        return entityManager.find(IdType.class, value);
    }

    @Transactional(value = "cleia-txm", readOnly = true)
    public List<IdType> getAll(GridRequest filters) {
        List<IdType> lidtype = this.find(entityManager, "select i from IdType i" + filters.getQL("i", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        return lidtype;

    }

    @Transactional(value = "cleia-txm")
    public void postIdType(IdType idtype) throws Exception {
        entityManager.persist(idtype);

    }

    @Transactional(value = "cleia-txm")
    public void putIdType(String ididtype, IdType idtype) throws Exception {

        IdType idt = entityManager.find(IdType.class, ididtype);
        if (idtype != null) {
            idt.setDescription(idtype.getDescription());
            idt.setRepeatable(idtype.isRepeatable());

        }
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void deleteIdType(String value) throws Exception {
        
        IdType idType=entityManager.find(IdType.class, value);
        entityManager.remove(idType);
        
    }

    /**
     *
     * @return
     */
    @Transactional(value = "cleia-txm")
    public List<IdType> getAll() {
        List<IdType> lidtype = entityManager.createQuery("SELECT u FROM IdType u").getResultList();
        return lidtype;
    }
}
