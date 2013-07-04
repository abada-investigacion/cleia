/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author katsu
 */
@Entity
public class Medical extends Patient {

    @JsonView(Views.Level4.class)
    @JoinTable(name = "medical_has_patient",
            joinColumns = {
        @JoinColumn(name = "medical_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {
        @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatients(List<Patient> ĺpatient) {
        for (Patient p : ĺpatient) {
            if (this.getPatients() == null) {
                this.setPatients(new ArrayList<Patient>());
            }
            if (!this.patients.contains(p)) {
                this.patients.add(p);
            }

            if (p.getMedicals() == null) {
                p.setMedicals(new ArrayList<Medical>());
            }
            if (!p.getMedicals().contains(this)) {
                p.getMedicals().add(this);
            }
        }
    }
}
