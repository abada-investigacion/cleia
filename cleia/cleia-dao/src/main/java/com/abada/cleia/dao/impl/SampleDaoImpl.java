
package com.abada.cleia.dao.impl;

import com.abada.cleia.dao.SampleDao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.abada.springframework.orm.jpa.support.JpaDaoUtils;


/**
 *
 * @author katsu
 */
public class SampleDaoImpl extends JpaDaoUtils implements SampleDao{
    @PersistenceContext(unitName = "cleiaPU")
    private EntityManager entityManager;
    
//    @Transactional(value = "cleia-txm", rollbackFor = {Exception.class})
//    public void persist(SampleEntity entity){
//        entityManager.persist(entity);
//    }
}
