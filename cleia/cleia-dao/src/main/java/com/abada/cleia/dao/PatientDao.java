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
import com.abada.cleia.entity.user.Patient;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.List;

/**
 *
 * @author katsu
 */
public interface PatientDao {

    /**
     * Return patient by Id
     *
     * @param patientId
     * @return
     */
    public Patient getPatientById(long patientId);

    public List<Patient> getAllPatients();

    public List<Patient> getAll(GridRequest grequest);

    public Long loadSizeAll(GridRequest grequest);

    public List<Patient> getAllbyMedical(GridRequest grequest,String usernameMedical);

    public Long loadSizeAllbyMedical(GridRequest grequest,String usernameMedical);

    public List<Patient> getPatientUser(GridRequest grequest, String username);

    public void updatePatient(Patient patient, Patient p);

    public void postPatient(Patient patient) throws Exception;

    public void putPatient(Long idpatient, Patient patient) throws Exception;

    public void putPatientData(Long idpatient, Patient patient) throws Exception;

    public void enableDisablePatient(Long idpatient, boolean enable) throws Exception;

    public List<Id> getIdsForPatient(Long idpatient);

    public List<Patient> findPatientsbylisId(List<Id> asList, Boolean repeatable) throws Exception;

    public List<Patient> getPatientnotmedical(GridRequest filters);

    public Long getPatientnotmedicalsize(GridRequest filters);
}
