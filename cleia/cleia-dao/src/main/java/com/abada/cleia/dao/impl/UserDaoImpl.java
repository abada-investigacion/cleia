/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.UserDao;
import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.Patient;
import com.abada.cleia.entity.user.Role;
import com.abada.cleia.entity.user.User;
import com.abada.jbpm.task.spring.TaskService;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.web.servlet.command.extjs.gridpanel.GridRequest;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author david
 */
public class UserDaoImpl extends JpaDaoUtils implements UserDao {

    private static final Log logger = LogFactory.getLog(UserDaoImpl.class);
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ShaPasswordEncoder sha1PasswordEncoder;

    /**
     * Returns all actors that an actor has in their groups
     *
     * @param username
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<String> getUserGroup(String username) {
        List<String> listuser = new ArrayList<String>();
        List<User> users = entityManager.createQuery("select u from User u where u.username=?").setParameter(1, username).getResultList();
        if (users != null && !users.isEmpty()) {
            for (Group group : users.get(0).getGroups()) {
                if (group != null) {
                    for (User u : group.getUsers()) {
                        if (!listuser.contains(u.getUsername())) {
                            listuser.add(u.getUsername());
                        }
                    }
                }
            }
        }
        return listuser;
    }

    /**
     * Returns all users
     *
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<User> getAllUsers() {

        List<User> lusers = entityManager.createQuery("SELECT u FROM User u").getResultList();

        /*
         * Fuerzo a que cada usuario traiga sus lista de Role y Group
         */
        if (!lusers.isEmpty()) {
            for (User u : lusers) {
                u.getGroups().size();
                u.getRoles().size();
                u.getIds().size();
                if (u instanceof Patient) {
                    Patient p = (Patient) u;
                    p.getMedicals().size();
                    p.getProcessInstances().size();
                }

            }
        }
        return lusers;
    }

    /**
     * Obtiene el tamaño de {@link User}
     *
     * @param filters
     * @return Long
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public Long loadSizeAll(GridRequest filters) {
        List<Long> result = this.find(entityManager, "select count(*) from User u" + filters.getQL("u", true), filters.getParamsValues());
        return result.get(0);
    }

    /**
     * Returns one user by id
     *
     * @param iduser
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public User getUserById(Long iduser) {

        User user = entityManager.find(User.class, iduser);

        /*
         * Si el usuario no es null le fuerzo a que traiga su lista de Role y
         * Group
         */
        if (user != null) {
            user.getGroups().size();
            user.getRoles().size();
            user.getIds().size();
            if (user instanceof Patient) {
                Patient p = (Patient) user;
                p.getMedicals().size();
                p.getProcessInstances();
            }
            return user;
        }

        return null;

    }

    /**
     * Insert a user
     *
     * @param enabled
     * @param nonexpired
     * @param nonexpiredCredentials
     * @param nonlocked
     * @param password
     * @param username
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void postUser(User user) throws Exception {

        if (user.getGroups().isEmpty() || user.getGroups() == null) {
            throw new Exception("Error. El usuario debe pertenecer a un servicio");
        } else if (user.getRoles().isEmpty() || user.getRoles() == null) {
            throw new Exception("Error. El usuario debe tener asignado un rol");
        }
        List<User> luser = entityManager.createQuery("select u from User u where u.username=?").setParameter(1, user.getUsername()).getResultList();
        if (luser.isEmpty() && luser != null) {

            try {
                org.jbpm.task.User usertask = new org.jbpm.task.User();
                usertask.setId(user.getUsername());
                taskService.getTaskSession().addUser(usertask);
                this.addGroupsAndRoles(user, user.getGroups(), user.getRoles(), true);
                user.setPassword(sha1PasswordEncoder.encodePassword(user.getPassword(), null));

                entityManager.persist(user);
            } catch (Exception e) {

                throw new Exception("Error. Ha ocurrido un error al insertar el usuario " + user.getUsername(), e);
            }

        } else {
            throw new Exception("Error. El usuario " + user.getUsername() + " ya existe.");
        }
    }

    /**
     * Modify a user by id
     *
     * @param iduser
     * @param enabled
     * @param nonexpired
     * @param nonexpiredCredentials
     * @param nonlocked
     * @param password
     * @param username
     * @throws Exception
     */
    @Transactional(value = "cleia-txm")
    public void putUser(Long iduser, User newuser) throws Exception {
        if (newuser.getGroups().isEmpty() || newuser.getGroups() == null) {
            throw new Exception("Error. El usuario debe pertenecer a un servicio");
        } else if (newuser.getRoles().isEmpty() || newuser.getRoles() == null) {
            throw new Exception("Error. El usuario debe tener asignado un rol");
        }
        User user = entityManager.find(User.class, iduser);

        if (user != null) {
            List<User> luser = entityManager.createQuery("select u from User u where u.username=?").setParameter(1, newuser.getUsername()).getResultList();
            if (luser.isEmpty() || newuser.getUsername().equals(user.getUsername())) {

                try {
                    if (!newuser.getUsername().equals(user.getUsername())) {
                        org.jbpm.task.User usertask = new org.jbpm.task.User();
                        usertask.setId(newuser.getUsername());
                        taskService.getTaskSession().addUser(usertask);

                    }
                    this.addGroupsAndRoles(user, newuser.getGroups(), newuser.getRoles(), true);
                    this.updateUser(user, newuser);
                } catch (Exception e) {

                    throw new Exception("Error. Ha ocurrido un error al modificar el usuario " + newuser.getUsername(), e);
                }

            } else {
                throw new Exception("Error. El usuario " + newuser.getUsername() + " ya existe");
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }
    }

    /**
     * Search a list of users by params
     *
     * @param filters
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<User> getAll(GridRequest filters) {

        List<User> luser = this.find(entityManager, "select u from User u" + filters.getQL("u", true), filters.getParamsValues(), filters.getStart(), filters.getLimit());
        /*
         * Fuerzo a que cada usuario traiga sus lista de Role y Group
         */
        if (!luser.isEmpty()) {
            for (User u : luser) {
                u.getGroups().size();
                u.getRoles().size();
                u.getIds().size();
                if (u instanceof Patient) {
                    Patient p = (Patient) u;
                    p.getProcessInstances().size();
                    p.getMedicals().size();
                }

            }
        }
        return luser;
    }

    /**
     * Search a list of users by params
     *
     * @param filters
     * @return
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public User getUserByUsername(GridRequest filters, String username) {
        User u = (User) entityManager.createQuery("select u from User u where u.username = :username").setParameter("username", username).getSingleResult();
        u.getGroups().size();
        u.getRoles().size();
        u.getIds().size();
        if (u instanceof Patient) {
            Patient p = (Patient) u;
            p.getMedicals().size();
            p.getProcessInstances().size();
        }

        return u;
    }

    /**
     * Returns a list of all groups from a user
     *
     * @param iduser
     * @return
     * @throws Exception
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Group> getGroupsByIdUser(Long iduser) throws Exception {
        User user = new User();
        user = entityManager.find(User.class, iduser);

        /*
         * Si el usuario existe le fuerzo a que traiga la lista de Group
         */
        if (user == null) {
            throw new Exception("Error. El usuario no existe");
        } else {
            for (Group g : user.getGroups()) {
                g.getUsers().size();
            }
        }

        return (List<Group>) user.getGroups();
    }

    /**
     * Returns a list of all roles from a user
     *
     * @param iduser
     * @return
     * @throws Exception
     */
    @Transactional(value = "cleia-txm", readOnly = true)
    public List<Role> getRolesByIdUser(Long iduser) throws Exception {

        User user = new User();
        user = entityManager.find(User.class, iduser);
        /*
         * Si el usuario existe le fuerzo a que traiga su lista de Role
         */
        if (user == null) {
            throw new Exception("Error. El usuario no existe");
        } else {
            user.getRoles().size();
        }

        return (List<Role>) user.getRoles();
    }

    /**
     * Modifies the relationship between a user and a group
     *
     * @param iduser
     * @param idgroup
     * @return
     */
    @Transactional(value = "cleia-txm")
    public void putUserGroup(Long iduser, Long idgroup) throws Exception {
        User user = (User) entityManager.find(User.class, iduser);

        if (user != null) {
            Group group = (Group) entityManager.find(Group.class, idgroup);
            if (group != null) {
                List<Group> lgroup = user.getGroups();
                List<User> luser = group.getUsers();
                if (lgroup.contains(group) || luser.contains(user)) {
                    throw new Exception("Error. El usuario " + user.getUsername() + " ya esta en el grupo " + group.getValue());
                } else {
                    lgroup.add(group);
                    luser.add(user);
                }
            } else {
                throw new Exception("Error. El grupo no existe");
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }
    }

    /**
     * Removes the relationship between a user and a group
     *
     * @param iduser
     * @param idgroup
     * @return
     */
    @Transactional(value = "cleia-txm")
    public void deleteUserGroup(Long iduser, Long idgroup) throws Exception {
        User user = (User) entityManager.find(User.class, iduser);

        if (user != null) {
            Group group = (Group) entityManager.find(Group.class, idgroup);
            if (group != null) {
                List<Group> lgroup = user.getGroups();
                List<User> luser = group.getUsers();
                if (!lgroup.contains(group) || !luser.contains(user)) {
                    throw new Exception("Error. El usuario " + user.getUsername() + " no esta en el grupo " + group.getValue());
                } else {
                    lgroup.remove(group);
                    luser.remove(user);
                }
            } else {
                throw new Exception("Error. El grupo no existe");
            }
        } else {
            throw new Exception("Error. El usuario no existe.");
        }
    }

    /**
     * Modifies the relationship between a user and a role
     *
     * @param iduser
     * @param idrole
     * @return
     */
    @Transactional(value = "cleia-txm")
    public void putUserRole(Long iduser, Integer idrole) throws Exception {
        User user = (User) entityManager.find(User.class, iduser);

        if (user != null) {
            Role role = (Role) entityManager.find(Role.class, idrole);
            if (role != null) {
                List<Role> lrole = user.getRoles();
                List<User> luser = role.getUsers();
                if (lrole.contains(role) || luser.contains(user)) {
                    throw new Exception("Error. El usuario " + user.getUsername() + " ya tiene asignado el rol " + role.getAuthority());
                } else {
                    lrole.add(role);
                    luser.add(user);
                }
            } else {
                throw new Exception("Error. El rol no existe");
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }
    }

    /**
     * Removes the relationship between a user and a role
     *
     * @param iduser
     * @param idrole
     * @return
     */
    @Transactional(value = "cleia-txm")
    public void deleteUserRole(Long iduser, Integer idrole) throws Exception {
        User user = (User) entityManager.find(User.class, iduser);

        if (user != null) {
            Role role = (Role) entityManager.find(Role.class, idrole);
            if (role != null) {
                List<Role> lrole = user.getRoles();
                List<User> luser = role.getUsers();
                if (!lrole.contains(role) || !luser.contains(user)) {
                    throw new Exception("Error. El usuario " + user.getUsername() + " no tiene asignado el rol " + role.getAuthority());
                } else {
                    lrole.remove(role);
                    luser.remove(user);
                }
            } else {
                throw new Exception("Error. El rol no existe");
            }
        } else {
            throw new Exception("Error. El usuario no existe.");
        }
    }

    /**
     * setting user
     *
     * @param user
     * @param newuser
     */
    @Transactional(value = "cleia-txm")
    public void updateUser(User user, User newuser) {
        user.setEnabled(newuser.isEnabled());
        user.setAccountNonExpired(newuser.isAccountNonExpired());
        user.setCredentialsNonExpired(newuser.isCredentialsNonExpired());
        user.setAccountNonLocked(newuser.isAccountNonLocked());
        user.setPassword(sha1PasswordEncoder.encodePassword(newuser.getPassword(), null));
        user.setUsername(newuser.getUsername());
    }

    /**
     * Enable a user by id
     *
     * @param iduser
     * @return
     */
    @Transactional(value = "cleia-txm")
    public void enableDisableUser(Long iduser, boolean enable) throws Exception {

        User user = entityManager.find(User.class, iduser);
        String habilitar = "";
        if (user != null) {
            if ((!user.isEnabled() && enable) || (user.isEnabled() && !enable)) {
                try {
                    user.setEnabled(enable);
                } catch (Exception e) {
                    if (enable) {
                        habilitar = "habilitar";
                    } else {
                        habilitar = "deshabilitar";
                    }
                    throw new Exception("Error. Ha ocurrido un error al " + habilitar + " al usuario " + user.getUsername());
                }
            } else {
                if (!enable) {
                    throw new Exception("Error. El usuario " + user.getUsername() + " ya esta deshabilitado");
                } else {
                    throw new Exception("Error. El usuario " + user.getUsername() + " ya esta habilitado");
                }
            }
        } else {
            throw new Exception("Error. El usuario no existe");
        }

    }

    @Transactional(value = "cleia-txm")
    public void addPatient2User(String username, Patient p) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.


    }

    @Transactional(value = "cleia-txm")
    public void addGroupsAndRoles(User user, List<Group> lgroup, List<Role> lrole, boolean newUser) throws Exception {

        if (lgroup != null && lrole != null) {
            List<Group> lgroupaux = new ArrayList<Group>(lgroup);
            List<Role> lroleaux = new ArrayList<Role>(lrole);

            //Eliminamos todos los grupos y roles de un usuario
            if (!newUser) {
                for (Group g : user.getGroups()) {
                    g.getUsers().remove(user);
                }
                for (Role r : user.getRoles()) {
                    r.getUsers().remove(user);
                }

                user.getGroups().clear();
                user.getRoles().clear();


                entityManager.flush();
            }
            if (lgroup != null) {
                user.getGroups().clear();
                for (Group g : lgroupaux) {
                    Group group = entityManager.find(Group.class, g.getValue());
                    if (group != null) {
                        user.addGroup(group);
                    } else {
                        throw new Exception("Error. Uno de los servicios no existe");
                    }
                }
            }
            if (lrole != null) {
                user.getRoles().clear();
                for (Role r : lroleaux) {
                    Role role = entityManager.find(Role.class, r.getAuthority());
                    if (role != null) {
                        user.addRole(role);
                    } else {
                        throw new Exception("Error. Uno de los roles no existe");
                    }
                }
            }


        } else {
            throw new NullPointerException("Error. Lista de servicios y roles inexistente");
        }
    }
}
