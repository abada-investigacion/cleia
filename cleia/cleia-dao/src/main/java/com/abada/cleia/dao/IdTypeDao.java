/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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
