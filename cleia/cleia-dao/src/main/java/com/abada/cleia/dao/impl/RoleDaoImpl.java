/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.RoleDao;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author katsu
 */
public class RoleDaoImpl extends JpaDaoUtils implements RoleDao{

    private static final Log logger = LogFactory.getLog(RoleDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    


    /**
     * Returns all roles
     * @return 
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Role> getAllRoles() {

        List<Role> listroles = entityManager.createQuery("SELECT r FROM Role r").getResultList();
        return listroles;
    }

    /**
     * Returns one role by id
     * @param idrole
     * @return 
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Role getRoleById(Integer idrole) {

        Role rolepriv = entityManager.find(Role.class, idrole);
        if (rolepriv != null) {
            return rolepriv;
        }

        return null;
    }

    /**
     * Insert a role 
     * @param rolename
     * @return 
     */
    @Transactional(value="cleia-txm",rollbackFor={Exception.class})
    public void postRole(Role role) throws Exception {
        List<Role> lrole = entityManager.createQuery("select r from Role r where r.authority=?").setParameter(1, role.getAuthority()).getResultList();
        if (lrole.isEmpty()) {
            try {
                this.addUsers(role, role.getUsers(), true);
                entityManager.persist(role);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al insertar el rol " + role.getAuthority());
            }
        } else {
            throw new Exception("Error. El rol " + role.getAuthority() + " ya existe.");
        }
    }

    /**
     * Modify a role by id
     * @param idrolepriv
     * @param rolename
     * @return 
     */
    @Transactional(value="cleia-txm",rollbackFor={Exception.class})
    public void putRole(String idrole, Role newrole) throws Exception {
        Role role = entityManager.find(Role.class, idrole);
        if (role != null) {
            List<Role> lrole = entityManager.createQuery("select r from Role r where r.authority=?").setParameter(1, newrole.getAuthority()).getResultList();
            if ((lrole.isEmpty() && lrole!=null) || newrole.getAuthority().equals(role.getAuthority())) {
                try {
                    this.addUsers(role, newrole.getUsers(), false);
                    role.setAuthority(newrole.getAuthority());
                } catch (Exception e) {
                    throw new Exception("Error. Ha ocurrido un error al modificar el rol " + newrole.getAuthority());
                }
            } else {
                throw new Exception("Error. El rol " + newrole.getAuthority() + " ya existe");
            }
        } else {
            throw new Exception("Error. El rol no existe");
        }
    }

    /**
     * Delete a role by id
     * @param idrolepriv
     * @return 
     */
    @Transactional(value="cleia-txm",rollbackFor={Exception.class})
    public void deleteRole(String idrole) throws Exception {

        Role role = (Role) entityManager.find(Role.class, idrole);
        if (role != null) {
            try {
                for (User user : role.getUsers()) {
                    user.getRoles().remove(role);
                }
                role.getUsers().clear();
                entityManager.remove(role);
            } catch (Exception e) {
                throw new Exception("Error. Ha ocurrido un error al borrar el rol: " + role.getAuthority());
            }
        } else {
            throw new Exception("Error. No se puede borrar el rol. Compruebe que exista.");
        }
    }

    /**
     * Search a list of roles by params
     * @param filters
     * @return 
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Role> getAll(GridRequest filters) {
        List<Role> lrole = this.find(entityManager,"select r from Role r" + filters.getQL("r", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        return lrole;
    }

    /**
     * Returns a list of all users from a role
     * @param idrolepriv
     * @return
     * @throws Exception 
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<User> getUsersByIdRole(String authority) throws Exception {

        Role role = new Role();

        role = entityManager.find(Role.class, authority);

        /*Si el role existe le fuerzo a que traiga su lista de User*/
        if (role == null) {
            throw new Exception("Error. El rol no existe");
        } else {
            for (User user : role.getUsers()) {
                user.getRoles().size();
                user.getGroups().size();
                
            }
        }

        return (List<User>) role.getUsers();
    }

    /**
     *Obtiene el tamaño de {@link Role}
     * @param filters 
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager,"select count(*) from Role r" + filters.getQL("r", true), filters.getParamsValues());
        return result.get(0);
    }

    @Transactional(value="cleia-txm",rollbackFor={Exception.class})
    public void addUsers(Role role, List<User> luser, boolean newrole) throws Exception {
        if (luser != null) {
            List<User> listuseraux=new ArrayList<User>(luser);
            if (!newrole) {
                for (User u : role.getUsers()) {
                    u.getRoles().remove(role);
                }
                role.getUsers().clear();

                entityManager.flush();
            }
            
            role.getUsers().clear();
            for (User u : listuseraux) {
                User user = entityManager.find(User.class, u.getId());
                if (user != null) {
                    role.addUser(user);
                } else {
                    throw new Exception("Error. Uno de los usuarios no existe");
                }
            }
        } else {
            throw new NullPointerException("Error. Lista de usuarios inexistente");
        }
    }
}
