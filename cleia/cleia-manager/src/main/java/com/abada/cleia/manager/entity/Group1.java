/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.manager.entity;


import com.abada.cleia.entity.user.User;

import java.util.List;


/**
 *
 * @author david
 */

public class Group1  {

    private static final long serialVersionUID = 1L;
   
    private Long idGroup;
   
    private String name;

    private List<User> userList;

    public Group1() {
    }

    public Group1(Long idGroup) {
        this.idGroup = idGroup;
    }

    public Group1(Long idGroup, String groupname) {
        this.idGroup = idGroup;
        this.name = groupname;
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String groupname) {
        this.name = groupname;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroup != null ? idGroup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Group1)) {
            return false;
        }
        Group1 other = (Group1) object;
        if ((this.idGroup == null && other.idGroup != null) || (this.idGroup != null && !this.idGroup.equals(other.idGroup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.abada.oggi.persistence.entity.Group1[ idGroup=" + idGroup + " ]";
    }
}
