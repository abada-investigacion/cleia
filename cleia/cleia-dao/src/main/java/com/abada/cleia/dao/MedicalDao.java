/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao;

import com.abada.cleia.entity.user.Id;
import com.abada.cleia.entity.user.Medical;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author david
 */
public interface MedicalDao {

    /**
     * Return by Id
     *
     * @param Id
     * @return
     */
    public Medical getMedicalById(long Id);

    public List<Medical> getAllMedicals();

    public List<Medical> findMedicalsrepeatable(List<Id> asList, Boolean repeatable) throws Exception;

    public List<Medical> getAll(GridRequest grequest);

    public Long loadSizeAll(GridRequest grequest);

    public List<Medical> getMedicalUser(GridRequest grequest, String username);

    public void postMedical(Medical medical) throws Exception;

    public void putMedical(Long id, Medical medical) throws Exception;

    public void putMedicalData(Long id, Medical medical) throws Exception;

    public void enableDisableMedical(Long id, boolean enable) throws Exception;

    public List<Id> getIdsForMedical(Long id);

    public void putMedicalid(Long id, List<Id> ids) throws Exception;

    public void addpatientMedical(Medical m) throws Exception;
}
