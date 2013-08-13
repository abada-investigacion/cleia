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

import com.abada.cleia.entity.user.IdType;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface IdTypeDao {

    /**
     * Returns all IdType
     *
     * @param
     * @return
     */
    public List<IdType> getAll();

    /**
     * Obtiene el tamaño de {@link IdType}
     *
     * @param filters
     * @return Long
     */
    public Long loadSizeAll(GridRequest filters);

    /**
     * Returns one IdType by id
     *
     * @param ididtype
     * @return
     */
    public IdType getIdTypeById(String value);

    /**
     * Search a list of IdType by params
     *
     * @param filter
     * @param dir
     * @param sort
     * @param limit
     * @param start
     * @return
     */
    public List<IdType> getAll(GridRequest filters);

    /**
     * Insert a IdType
     *
     * @param idtype
     * @return
     */
    public void postIdType(IdType idtype) throws Exception;

    /**
     * Modify a IdType by id
     *
     * @param ididtype
     * @param idtype
     * @return
     */
    public void putIdType(String ididtype, IdType idtype) throws Exception;

    /**
     * Delete a IdType by id
     *
     * @param value
     * @return
     */
    public void deleteIdType(String value) throws Exception;
}
