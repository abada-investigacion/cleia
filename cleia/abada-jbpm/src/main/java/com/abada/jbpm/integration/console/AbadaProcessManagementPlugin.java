/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

import java.util.List;
import java.util.Map;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;

/**
 *
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public interface AbadaProcessManagementPlugin {
    /**
     * Return the process instances with the inheratance of process instance passed
     * @param processInstanceId
     * @return 
     */
    public List<ProcessInstanceLog> getProcessInstanceTreeExecution(Long processInstanceId);
    
    /**
     * Return a history of nodes for a process instance
     * @param processInstanceId
     * @return 
     */
    public List<NodeInstanceLog> getNodeHistoryList(Long processInstanceId);
    /**
     * Signal to subprocess
     * @param parentProcessId
     * @param processId
     * @param signal
     * @param type 
     */
    public void signalExecution(Long parentProcessId,String processId, Map<String,Object> signal, String type);
}
