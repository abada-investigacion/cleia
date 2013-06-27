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

public class Rolepriv{

   
    private Integer idRolePriv;
   
    private String authority;
    private List<User> userList;

    public Rolepriv() {
    }

    public Rolepriv(Integer idRolePriv) {
        this.idRolePriv = idRolePriv;
    }

    public Rolepriv(Integer idRolePriv, String authority) {
        this.idRolePriv = idRolePriv;
        this.authority = authority;
    }

    public Integer getIdRolePriv() {
        return idRolePriv;
    }

    public void setIdRolePriv(Integer idRolePriv) {
        this.idRolePriv = idRolePriv;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
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
        hash += (idRolePriv != null ? idRolePriv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rolepriv)) {
            return false;
        }
        Rolepriv other = (Rolepriv) object;
        if ((this.idRolePriv == null && other.idRolePriv != null) || (this.idRolePriv != null && !this.idRolePriv.equals(other.idRolePriv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.abada.oggi.persistence.entity.Rolepriv[ idRolePriv=" + idRolePriv + " ]";
    }
}
