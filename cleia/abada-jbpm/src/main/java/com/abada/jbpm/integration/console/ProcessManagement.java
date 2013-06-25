/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

import com.abada.jbpm.process.audit.ProcessInstanceDbLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.definition.process.Process;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef.RESULT;
import org.jboss.bpm.console.client.model.ProcessInstanceRef.STATE;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;

/**
 * Sustituye a {@link org.jbpm.integration.console.ProcessManagement}
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class ProcessManagement implements org.jboss.bpm.console.server.integration.ProcessManagement, AbadaProcessManagementPlugin {

    private CommandDelegate delegate;

    public ProcessManagement(StatefulKnowledgeSession session, ProcessInstanceDbLog processInstanceDbLog) {
        delegate = new CommandDelegate(session, processInstanceDbLog);
    }

    public List<ProcessDefinitionRef> getProcessDefinitions() {
        List<Process> processes = delegate.getProcesses();
        List<ProcessDefinitionRef> result = new ArrayList<ProcessDefinitionRef>();
        for (Process process : processes) {
            result.add(Transform.processDefinition(process));
        }
        return result;
    }

    public ProcessDefinitionRef getProcessDefinition(String definitionId) {
        Process process = delegate.getProcess(definitionId);
        return Transform.processDefinition(process);
    }

    /**
     * method unsupported
     */
    public List<ProcessDefinitionRef> removeProcessDefinition(String definitionId) {
        delegate.removeProcess(definitionId);
        return getProcessDefinitions();
    }

    public ProcessInstanceRef getProcessInstance(String instanceId) {
        ProcessInstanceLog processInstance = delegate.getProcessInstanceLog(instanceId);
        return Transform.processInstance(processInstance);
    }

    public List<ProcessInstanceRef> getProcessInstances(String definitionId) {
        List<ProcessInstanceLog> processInstances = delegate.getActiveProcessInstanceLogsByProcessId(definitionId);
        List<ProcessInstanceRef> result = new ArrayList<ProcessInstanceRef>();
        for (ProcessInstanceLog processInstance : processInstances) {
            result.add(Transform.processInstance(processInstance));
        }
        return result;
    }

    public ProcessInstanceRef newInstance(String definitionId) {
        return newInstance(definitionId, null);
    }

    public ProcessInstanceRef newInstance(String definitionId, Map<String, Object> processVars) {
        ProcessInstanceLog processInstance = delegate.startProcess(definitionId, processVars);
        return Transform.processInstance(processInstance);
    }

    public void setProcessState(String instanceId, STATE nextState) {
        if (nextState == STATE.ENDED) {
            delegate.abortProcessInstance(instanceId);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Map<String, Object> getInstanceData(String instanceId) {
        return delegate.getProcessInstanceVariables(instanceId);
    }

    public void setInstanceData(String instanceId, Map<String, Object> data) {
        delegate.setProcessInstanceVariables(instanceId, data);
    }

    public void signalExecution(String executionId, Map<String, Object> signal) {
        delegate.signalExecution(executionId, signal);
    }

    public void signalExecution(String executionId, Map<String, Object> signal, String type) {
        delegate.signalExecution(executionId, type, signal);
    }

    public void signalExecution(Long parentProcessId, String processId, Map<String, Object> signal, String type) {
        List<ProcessInstanceLog> list=this.getProcessInstanceTreeExecution(parentProcessId);
        if (list!=null){
            for (ProcessInstanceLog pil:list){
                if (pil.getProcessId().equals(processId)){
                    this.signalExecution(pil.getProcessInstanceId()+"", signal, type);
                }
            }
        }
    }

    public void deleteInstance(String instanceId) {
        delegate.abortProcessInstance(instanceId);
    }

    //result means nothing
    public void endInstance(String instanceId, RESULT result) {
        delegate.abortProcessInstance(instanceId);
    }

    public List<ProcessInstanceLog> getProcessInstanceTreeExecution(Long processInstanceId) {
        return delegate.getProcessInstanceTreeExecution(processInstanceId);
    }

    public List<NodeInstanceLog> getNodeHistoryList(Long processInstanceId) {
        return delegate.getNodeHistoryList(processInstanceId);
    }

    public void signalExecution(String executionId, String signal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
