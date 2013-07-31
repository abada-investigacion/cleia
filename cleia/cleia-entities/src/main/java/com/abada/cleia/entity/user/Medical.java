/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author katsu
 */
@Entity
public class Medical implements Serializable{
    @JsonView(Views.Public.class)
    @javax.persistence.Id    
    private Long id;
    @JsonView(Views.Level4.class)
    @JoinTable(name = "medical_has_patient",
            joinColumns = {
        @JoinColumn(name = "medical_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {
        @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Patient> patients;
    @JsonView(Views.Public.class)
    @OneToOne(optional = false)
    @JoinColumn(name = "id",nullable = false)
    private Patient patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    
}
