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
import com.abada.cleia.entity.user.Medical;
import com.abada.cleia.entity.user.Patient;
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

    public List<Patient> findPatientsByMedicalId(Long id) throws Exception;
}
