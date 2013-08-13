/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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

import com.abada.cleia.entity.user.Id;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author david
 */
public interface IdDao {

    /**
     * Obtiene el tamaño de {@link Id}
     *
     * @param filters
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one Id by id
     *
     * @param idId
     * @return
     */
    public Id getIdById(Long id);

    /**
     * Search a list of Id by params
     *
     * @param filter
     * @param dir
     * @param sort
     * @param limit
     * @param start
     * @return
     */
    public List<Id> getAll(GridRequest filters);

    /**
     * Insert a Id
     *
     * @param Id
     * @return
     */
    public void postId(Id Id) throws Exception;

    /**
     * Delete a Id by id
     *
     * @param value
     * @return
     */
    public void deleteId(long id) throws Exception;

    /**
     * update id
     *
     * @param idt
     * @param newid
     * @throws Exception
     */
    public void putId(Id idt, Id newid) throws Exception;

    /**
     *
     * @param value
     * @param type
     * @return
     */
    public Id getIdByvaluetype(String value, String type);

    /**
     * get id by user and type
     *
     * @param id
     * @param type
     * @return
     */
    public Id getIdByusertype(long id, String type);
}
