/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

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

import com.abada.jbpm.process.audit.ProcessInstanceDbLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.drools.command.runtime.process.SignalEventCommand;

import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;

/**
 *
 * 
 * Sustituye {@link org.jbpm.integration.console.CommandDelegate} 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class CommandDelegate {

    private StatefulKnowledgeSession ksession;
    private ProcessInstanceDbLog processInstanceDbLog;

    public CommandDelegate(StatefulKnowledgeSession session, ProcessInstanceDbLog processInstanceDbLog) {
        ksession = session;
        this.processInstanceDbLog = processInstanceDbLog;
    }

    private StatefulKnowledgeSession getSession() {
        return ksession;
    }

    public void setSession(StatefulKnowledgeSession session) {
        ksession = session;
    }

    public List<Process> getProcesses() {
        List<Process> result = new ArrayList<Process>();
        for (KnowledgePackage kpackage : getSession().getKnowledgeBase().getKnowledgePackages()) {
            result.addAll(kpackage.getProcesses());
        }
        return result;
    }

    public Process getProcess(String processId) {
        for (KnowledgePackage kpackage : getSession().getKnowledgeBase().getKnowledgePackages()) {
            for (Process process : kpackage.getProcesses()) {
                if (processId.equals(process.getId())) {
                    return process;
                }
            }
        }
        return null;
    }

    public Process getProcessByName(String name) {
        for (KnowledgePackage kpackage : getSession().getKnowledgeBase().getKnowledgePackages()) {
            for (Process process : kpackage.getProcesses()) {
                if (name.equals(process.getName())) {
                    return process;
                }
            }
        }
        return null;
    }

    public void removeProcess(String processId) {
        throw new UnsupportedOperationException();
    }

    public ProcessInstanceLog getProcessInstanceLog(String processInstanceId) {
        return processInstanceDbLog.findProcessInstance(new Long(processInstanceId));
    }

    public List<ProcessInstanceLog> getProcessInstanceLogsByProcessId(String processId) {
        return processInstanceDbLog.findProcessInstances(processId);
    }

    public List<ProcessInstanceLog> getActiveProcessInstanceLogsByProcessId(String processId) {
        return processInstanceDbLog.findActiveProcessInstances(processId);
    }

    public ProcessInstanceLog startProcess(String processId, Map<String, Object> parameters) {
        long processInstanceId = ksession.startProcess(processId, parameters).getId();
        return processInstanceDbLog.findProcessInstance(processInstanceId);
    }

    public void abortProcessInstance(String processInstanceId) {
        ProcessInstance processInstance = ksession.getProcessInstance(new Long(processInstanceId));
        if (processInstance != null) {
            ksession.abortProcessInstance(new Long(processInstanceId));
        } else {
            throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
        }
    }

    public Map<String, Object> getProcessInstanceVariables(String processInstanceId) {
        ProcessInstance processInstance = ksession.getProcessInstance(new Long(processInstanceId));
        //TODO mirar en tabla de log si el proceso a finalizado
        if (processInstance != null) {
            Map<String, Object> variables =
                    ((WorkflowProcessInstanceImpl) processInstance).getVariables();
            if (variables == null) {
                return new HashMap<String, Object>();
            }
            // filter out null values
            Map<String, Object> result = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                if (entry.getValue() != null) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            return result;
        } else {
            try {
                return processInstanceDbLog.findLastVariablesValues(Long.parseLong(processInstanceId));
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
            }
        }
    }

    public void setProcessInstanceVariables(String processInstanceId, Map<String, Object> variables) {
        ProcessInstance processInstance = ksession.getProcessInstance(new Long(processInstanceId));
        if (processInstance != null) {
            VariableScopeInstance variableScope = (VariableScopeInstance) ((org.jbpm.process.instance.ProcessInstance) processInstance).getContextInstance(VariableScope.VARIABLE_SCOPE);
            if (variableScope == null) {
                throw new IllegalArgumentException(
                        "Could not find variable scope for process instance " + processInstanceId);
            }
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                variableScope.setVariable(entry.getKey(), entry.getValue());
            }
        } else {
            throw new IllegalArgumentException("Could not find process instance " + processInstanceId);
        }
    }

    public void signalExecution(String executionId, Map<String,Object> signal) {
        this.signalExecution(executionId, "signal", signal);
    }

    public void signalExecution(String executionId, String type, Map<String,Object> signal) {
        SignalEventCommand sigCmd = new SignalEventCommand(new Long(executionId), type, signal);
        ksession.execute(sigCmd);
    }

    public List<ProcessInstanceLog> getProcessInstanceTreeExecution(Long processInstanceId) {
        return processInstanceDbLog.findProcessInstancesFrom(processInstanceId);
    }

    List<NodeInstanceLog> getNodeHistoryList(Long processInstanceId) {
        return processInstanceDbLog.findNodeInstances(processInstanceId);
    }
}
