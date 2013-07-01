/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

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
    public void deleteId(String value) throws Exception;

    public void putId(Id idt, Id newid) throws Exception;

    /**
     *
     * @param value
     * @param type
     * @return
     */
    public Id getIdByvaluetype(String value, String type);
}
