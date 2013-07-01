/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author katsu
 */
@Entity
public class IdType implements Serializable {
    @Id    
    @JsonView(Views.Public.class)
    @Column(name = "value1")
    private String value;
    @JsonView(Views.Public.class)
    @Column(length = 1024)
    private String description;
    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private boolean repeatable;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }
}
