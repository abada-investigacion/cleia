/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.WorkingMemory;
import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.runtime.EnvironmentName;

/**
 * Enables history log via JPA.
 * Sustituye a {@link org.jbpm.process.audit.JPAWorkingMemoryDbLogger} 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class JPAWorkingMemoryDbLogger extends org.jbpm.process.audit.JPAWorkingMemoryDbLogger implements ProcessEventListener{
    private static final Log logger=LogFactory.getLog(JPAWorkingMemoryDbLogger.class);
    
    private boolean isJTA=true;
    
    public JPAWorkingMemoryDbLogger(WorkingMemory workingMemory) {
        super(workingMemory);
        Boolean bool = (Boolean) env.get("IS_JTA_TRANSACTION");
        if (bool != null) {
        	isJTA = bool.booleanValue();
        }        
    }

    public JPAWorkingMemoryDbLogger(KnowledgeRuntimeEventManager session) {
        super(session);
        Boolean bool = (Boolean) env.get("IS_JTA_TRANSACTION");
        if (bool != null) {
        	isJTA = bool.booleanValue();
        }  
    }
    
    public void addNodeCancelLog(long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName,String observation) {
        NodeInstanceLogExt log = new NodeInstanceLogExt(2, processInstanceId, processId, nodeInstanceId, nodeId, nodeName,observation);
        persist(log);
    }
    
    public void addNodeChangeVersionLog(long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName,String observation) {
        NodeInstanceLogExt log = new NodeInstanceLogExt(3, processInstanceId, processId, nodeInstanceId, nodeId, nodeName,observation);
        persist(log);
    }
    
    //<editor-fold defaultstate="collapsed" desc="From father">
    private boolean sharedEM = false;
    
    /**
     * This method creates a entity manager.
     */
    private EntityManager getEntityManager() {
        EntityManager em = (EntityManager) env.get(EnvironmentName.CMD_SCOPED_ENTITY_MANAGER);
        if (em != null) {
            sharedEM = true;
            return em;
        }
        EntityManagerFactory emf = (EntityManagerFactory) env.get(EnvironmentName.ENTITY_MANAGER_FACTORY);
        if (emf != null) {
            return emf.createEntityManager();
        }
        throw new RuntimeException("Could not find EntityManager, both command-scoped EM and EMF in environment are null");
    }
    
    /**
     * This method persists the entity given to it.
     * </p>
     * This method also makes sure that the entity manager used for persisting the entity, joins the existing JTA transaction.
     * @param entity An entity to be persisted.
     */
    private void persist(Object entity) {
        EntityManager em = getEntityManager();
        UserTransaction ut = joinTransaction(em);
        em.persist(entity);
        if (!sharedEM) {
            flush(em, ut);
        }
    }
    
    /**
     * This method opens a new transaction, if none is currently running, and joins the entity manager/persistence context
     * to that transaction.
     * @param em The entity manager we're using.
     * @return {@link UserTransaction} If we've started a new transaction, then we return it so that it can be closed.
     * @throws NotSupportedException
     * @throws SystemException
     * @throws Exception if something goes wrong.
     */
    private UserTransaction joinTransaction(EntityManager em) {
        boolean newTx = false;
        UserTransaction ut = null;
        
        if (isJTA) {
            try {
                em.joinTransaction();
                
            } catch (TransactionRequiredException e) {
                ut = findUserTransaction();
                try {
                    if( ut != null && ut.getStatus() == Status.STATUS_NO_TRANSACTION ) {
                        ut.begin();
                        newTx = true;
                        // since new transaction was started em must join it
                        em.joinTransaction();
                    }
                } catch(Exception ex) {
                    throw new IllegalStateException("Unable to find or open a transaction: " + ex.getMessage(), ex);
                }
                
                if (!newTx) {
                    // rethrow TransactionRequiredException if UserTransaction was not found or started
                    throw e;
                }
                
            }
            
            if( newTx ) {
                return ut;
            }
        }
        return null;
    }
    
    /**
     * This method closes the entity manager and transaction. It also makes sure that any objects associated
     * with the entity manager/persistence context are detached.
     * </p>
     * Obviously, if the transaction returned by the {@link #joinTransaction(EntityManager)} method is null,
     * nothing is done with the transaction parameter.
     * @param em The entity manager.
     * @param ut The (user) transaction.
     */
    private static void flush(EntityManager em, UserTransaction ut) {
        em.flush(); // This saves any changes made
        em.clear(); // This makes sure that any returned entities are no longer attached to this entity manager/persistence context
        em.close(); // and this closes the entity manager
        try {
            if( ut != null ) {
                // There's a tx running, close it.
                ut.commit();
            }
        } catch(Exception e) {
            logger.error("Unable to commit transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //</editor-fold>
}
