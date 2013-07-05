/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

import com.abada.json.Json;
import com.abada.json.JsonFactory;
import com.abada.json.JsonType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.bpm.console.client.model.DiagramNodeInfo;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.audit.VariableInstanceLog;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Sustituye {@link org.jbpm.process.audit.ProcessInstanceDbLog}
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class ProcessInstanceDbLog {

    private static final Json gson= JsonFactory.getInstance().getInstance(JsonType.DEFAULT);
    @PersistenceContext(unitName = "com.abada.drools.persistence.jpa")
    private EntityManager em;

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<ProcessInstanceLog> findProcessInstances() {
        List<ProcessInstanceLog> result = em.createQuery("from ProcessInstanceLog").getResultList();
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<ProcessInstanceLog> findProcessInstances(String processId) {
        List<ProcessInstanceLog> result = em.createQuery("from ProcessInstanceLog as log where log.processId = ?").setParameter(1, processId).getResultList();
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<ProcessInstanceLog> findActiveProcessInstances(String processId) {
        List<ProcessInstanceLog> result = em.createQuery(
                "from ProcessInstanceLog as log where log.processId = ? and log.end is null").setParameter(1, processId).getResultList();
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public ProcessInstanceLog findProcessInstance(long processInstanceId) {
        List<ProcessInstanceLog> result = em.createQuery(
                "from ProcessInstanceLog as log where log.processInstanceId = ?").setParameter(1, processInstanceId).getResultList();
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<NodeInstanceLog> findNodeInstances(long processInstanceId) {
        List<NodeInstanceLog> result = em.createQuery(
                "from NodeInstanceLog as log where log.processInstanceId = ? order by log.date, log.nodeInstanceId ").setParameter(1, processInstanceId).getResultList();
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<NodeInstanceLog> findNodeInstances(long processInstanceId, String nodeId) {
        List<NodeInstanceLog> result = em.createQuery(
                "from NodeInstanceLog as log where log.processInstanceId = ? and log.nodeId = ?").setParameter(1, processInstanceId).setParameter(2, nodeId).getResultList();
        return result;
    }

    @Transactional(value="jbpm-txm",rollbackFor={Exception.class})
    public void clear() {
        List<ProcessInstanceLog> processInstances =
                em.createQuery("from ProcessInstanceLog").getResultList();
        for (ProcessInstanceLog processInstance : processInstances) {
            em.remove(processInstance);
        }
        List<NodeInstanceLog> nodeInstances =
                em.createQuery("from NodeInstanceLog").getResultList();
        for (NodeInstanceLog nodeInstance : nodeInstances) {
            em.remove(nodeInstance);
        }
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<ProcessInstanceLog> findProcessInstancesExecuting(String processId) {
        List<ProcessInstanceLog> result = new ArrayList<ProcessInstanceLog>();
        List<ProcessInstanceLog> processInstanceLogs = em.createQuery("from ProcessInstanceLog as log where log.processId = ?").setParameter(1, processId).getResultList();
        for (ProcessInstanceLog pil : processInstanceLogs) {
            if (isExecuting(pil)) {
                result.add(pil);
            }
        }
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    private boolean isExecuting(ProcessInstanceLog processInstanceLog) {
        Object result = em.createQuery("select count(*) from processinstanceinfo where InstanceId = ?").setParameter(1, processInstanceLog.getId()).getSingleResult();
        return result != null && ((Number)result).longValue()>0;
    }

    /**
     * return the inheritance tree of process instances from a passed processInstanceId
     * @param processInstanceId
     * @return 
     */
    @Transactional(value = "jbpm-txm", readOnly = true)
    public List<ProcessInstanceLog> findProcessInstancesFrom(Long processInstanceId) {
        List<ProcessInstanceLog> result = new ArrayList<ProcessInstanceLog>();
        //primero yo mismo xD
        ProcessInstanceLog me = this.findProcessInstance(processInstanceId);
        if (me != null) {
            result.add(me);
            //mis hijitos
            List<ProcessInstanceLog> childs = em.createQuery("from ProcessInstanceLog as log where log.parentProcessInstanceId = ?").setParameter(1, processInstanceId).getResultList();
            if (childs != null && !childs.isEmpty()) {
                for (ProcessInstanceLog log : childs) {
                    result.addAll(this.findProcessInstancesFrom(log.getProcessInstanceId()));
                }
            }
        }
        return result;
    }

    @Transactional(value = "jbpm-txm", readOnly = true)
    public Long findNumberProcessInNode(DiagramNodeInfo node, String processId, Date start, Date end) {
        if (node instanceof com.abada.bpm.console.client.model.DiagramNodeInfo) {
            com.abada.bpm.console.client.model.DiagramNodeInfo nodeAux = (com.abada.bpm.console.client.model.DiagramNodeInfo) node;
            String sql = "select count(*) from NodeInstanceLog as nil where nil.processId=? and nil.nodeId=? and nil.type=0 and ";
            if (start != null && end != null) {
                sql += " nil.date between ? and ? and ";
            }
            sql += "((select count(*) from NodeInstanceLog as nil2 where nil.processInstanceId=nil2.processInstanceId "
                    + "and nil.nodeInstanceId=nil2.nodeInstanceId and nil2.type>=1)=0)";
            Long result = (Long) em.createQuery(sql).setParameter(1, processId).setParameter(2, nodeAux.getId() + "").setParameter(3, start).setParameter(4, end).getSingleResult();
            return result;
        }
        return Long.MIN_VALUE;
    }
    
    @Transactional(value = "jbpm-txm", readOnly = true)
    public Map<String, Object> findLastVariablesValues(Long processInstanceId) throws ClassNotFoundException {
        Map<String, Object> result=new HashMap<String, Object>();
        List<VariableInstanceLog> list=em.createQuery("select v from VariableInstanceLog v where v.processInstanceId=? order by v.date DESC").setParameter(1, processInstanceId).getResultList();
        for (VariableInstanceLog vil:list){
            if (vil instanceof VariableInstanceLogExt && !result.keySet().contains(vil.getVariableId())){                
                result.put(vil.getVariableId(), gson.deserialize(((VariableInstanceLogExt)vil).getValueJson(),this.getClass().getClassLoader().loadClass(((VariableInstanceLogExt)vil).getValueType())));
            }else{
                result.put(vil.getVariableId(), vil.getValue());
            }
        }
        return result;        
    }
}
