/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Command that result is a list of event nodes from a process
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class FindEventNodes implements GenericCommand<List<Node>> {

    private static final Log logger = LogFactory.getLog(FindEventNodes.class);
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

        //List with all nodes
        NodeListCommand nlc = new NodeListCommand();
        nlc.setProcessId(this.getProcessId());
        List<Node> list = ksession.execute(nlc);
        return findEventNodes(list);
    }

    private List<Node> findEventNodes(List<Node> list) {
        List<Node> result = new ArrayList<Node>();
        if (list != null) {
            for (Node n : list) {
                if (n instanceof SubNode) {
                    result.addAll(findEventNodes(((SubNode)n).getNodes()));
                } else if (n instanceof EventNode && !result.contains(n)) {
                    result.add(n);
                }
            }
        }
        return result;
    }
}
