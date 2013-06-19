/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.entity.user;

import com.abada.gson.exclusionstrategy.JsonExclude;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author katsu
 */
@Entity(name = "patient_has_pi")
public class PatientHasProcessInstance implements Serializable {
    @javax.persistence.Id
    @Basic(optional = false)
    @Column(name = "process", nullable = false)
    private Long processInstanceId;
    @JsonExclude
    @JoinColumn(name = "patient", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Patient patientId;        

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Patient getPatientId() {
        return patientId;
    }

    public void setPatientId(Patient patientId) {
        this.patientId = patientId;
    }
}
