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
 * Command that result is a list of nodes from a process
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class NodeListCommand implements GenericCommand<List<Node>> {

    private static final Log logger = LogFactory.getLog(NodeListCommand.class);
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

    public List<Node> execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        if (getProcessId() == null || getProcessId().isEmpty()) {
            return null;
        }
        try {
            return getNodes(ksession.getKnowledgeBase(), getProcessId(), null);
        } catch (JbpmException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private List<Node> getNodes(KnowledgeBase kbase, String processId, Node parent) throws JbpmException {
        List<Node> result = new ArrayList<Node>();
        NodeContainer process = (NodeContainer) kbase.getProcess(processId);
        if (process == null) {
            throw new JbpmException("Process don't exist " + processId + ". Parent " + parent.getProcessId());
        }

        for (org.drools.definition.process.Node n : process.getNodes()) {
            Node aux;
            if (n instanceof SubProcessNode) {
                aux = new SubNode();
            } else if (n instanceof org.jbpm.workflow.core.node.EventNode) {
                aux = new EventNode();
            } else {
                aux = new Node();
            }
            aux.setId(n.getId());
            aux.setProcessId(processId);
            aux.setName(n.getName());
            aux.setType(n.getClass().getName());
            aux.setParent(parent);
            if (n instanceof SubProcessNode) {
                List<Node> subProcess = getNodes(kbase, ((SubProcessNode) n).getProcessId(), aux);
                ((SubNode) aux).setNodes(subProcess);
            } else if (n instanceof org.jbpm.workflow.core.node.EventNode) {                
                ((EventNode)aux).setEventType(((org.jbpm.workflow.core.node.EventNode)n).getType());
            }
            result.add(aux);
        }
        return result;
    }
}
