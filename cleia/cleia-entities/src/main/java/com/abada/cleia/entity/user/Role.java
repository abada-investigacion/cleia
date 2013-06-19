/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.entity.user;

import com.abada.gson.exclusionstrategy.JsonExclude;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author katsu
 */
@Entity(name = "role1")
public class Role implements GrantedAuthority{
    @Id
    @Column(length = 1024,name = "value1")
    private String value;    
    @JsonExclude
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getAuthority() {
        return value;
    }
}
