/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

import com.abada.jbpm.exception.JbpmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.definition.process.NodeContainer;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.workflow.core.node.SubProcessNode;

/**
 * Command that result is a list of String withe the name of subprocess of a process
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class ProcessIdsInheritanceListCommand implements GenericCommand<List<String>> {
    private static final Log logger=LogFactory.getLog(ProcessIdsInheritanceListCommand.class);
    /**
     * Process Id
     */
    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<String> execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        if (getProcessId() == null || getProcessId().isEmpty()) {
            return null;
        }
        try{
            return getProcess(ksession.getKnowledgeBase(),getProcessId());
        }catch (JbpmException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    private List<String> getProcess(KnowledgeBase kbase, String processId) throws JbpmException {
        List<String> result = new ArrayList<String>();
        NodeContainer process = (NodeContainer) kbase.getProcess(processId);
        if (process==null) throw new JbpmException("Process don't exist "+processId+".");
        
        result.add(processId);
        
        for (org.drools.definition.process.Node n : process.getNodes()) {            
            if (n instanceof SubProcessNode) {
                List<String> subProcess = getProcess(kbase, ((SubProcessNode) n).getProcessId());
                result.addAll(subProcess);
            }            
        }
        return result;
    }
}
