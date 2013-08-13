/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.manager.entity;

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
