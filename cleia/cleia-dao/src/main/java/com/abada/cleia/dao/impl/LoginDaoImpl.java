package com.abada.cleia.dao.impl;

import com.abada.cleia.entity.user.Group;
import com.abada.cleia.entity.user.User;
import javax.persistence.EntityManager;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;
import com.abada.springframework.security.authentication.dni.DniAuthenticationDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.task.identity.UserGroupCallback;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author katsu
 */
public class LoginDaoImpl extends JpaDaoUtils implements UserGroupCallback, UserDetailsService, DniAuthenticationDao {

    private static final Log logger = LogFactory.getLog(LoginDaoImpl.class);
    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactoryBean(EntityManagerFactoryInfo entityManagerFactoryBean) throws Exception {
        entityManagerFactory = entityManagerFactoryBean.getNativeEntityManagerFactory();
    }

    public User getUserByDNI(String dni) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            User result = (User) em.createQuery("select id.user from Id id where id.value = :dni and id.type.value =  'DNI'").setParameter("dni", dni).getSingleResult();
            if (result != null) {
                result.getGroups().size();
                result.getRoles().size();            
            }
            et.commit();
            return result;
        } catch (RuntimeException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return null;
    }

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            User result = (User) em.createQuery("select u from User u where u.username=:username").setParameter("username", username).getSingleResult();
            if (result == null) {
                throw new UsernameNotFoundException(username);
            }
            result.getGroups().size();
            result.getRoles().size();
            et.commit();
            return result;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public List<String> getGroupByUserName(String username) {
        User user = loadUserByUsername(username);
        if (user != null) {
            List<String> result = new ArrayList<String>();
            for (Group g : user.getGroups()) {
                result.add(g.getValue());
            }
            return result;
        }
        return null;
    }

    public boolean existsUser(String userId) {
        try {
            if ("Administrator".equals(userId)) {
                return true;//FIXIT
            }
            loadUserByUsername(userId);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    public boolean existsGroup(String groupId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            Group result = em.find(Group.class, groupId);
            et.commit();
            if (result != null) {
                return true;
            }
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
        return false;
    }

    public List<String> getGroupsForUser(String userId, List<String> groupIds, List<String> allExistingGroupIds) {
        return getGroupByUserName(userId);
    }
}
