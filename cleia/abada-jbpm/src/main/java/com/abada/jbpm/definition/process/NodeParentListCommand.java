/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

import java.util.ArrayList;
import java.util.List;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Command thar return a list with the path of nodes from startNode to nodeTo
 * in a given process
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class NodeParentListCommand implements GenericCommand<List<Node>> {

    /**
     * ProcessId of parent process
     */
    private String processId;
    /**
     * to node
     */
    private Node nodeTo;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Node getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(Node nodeTo) {
        this.nodeTo = nodeTo;
    }

    public List<Node> execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        if (getProcessId() == null || getProcessId().isEmpty()) {
            return null;
        }

        //List with all nodes
        NodeListCommand nlc=new NodeListCommand();
        nlc.setProcessId(this.getProcessId());
        List<Node> list = ksession.execute(nlc);

        if (list != null) {
            //Search path from the bottom
            Node aux = getNodeWithParent(this.getNodeTo(), list);
            List<Node> result = new ArrayList<Node>();
            while (aux != null) {
                result.add(0, aux);
                aux = aux.getParent();
            }
            return result;
        }
        return null;
    }

    /**
     * Return the subnode with the path to node or node if node is contain in list
     * @param node
     * @param list
     * @return
     */
    private Node getNodeWithParent(Node node, List<Node> list) {
        for (Node n : list) {
            if (n.getId().longValue()==node.getId().longValue() && n.getProcessId().equals(node.getProcessId())) {
                return n;
            }
            if (n instanceof SubNode) {
                Node n2 = getNodeWithParent(node, ((SubNode) n).getNodes());
                if (n2 != null) {
                    return n2;
                }
            }
        }
        return null;
    }
}
