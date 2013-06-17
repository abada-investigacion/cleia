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
 * Command that result is a list of processId from a processId
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class ProcessTreeCommand implements GenericCommand<List<String>> {

    private static final Log logger = LogFactory.getLog(ProcessTreeCommand.class);
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

        NodeListCommand command = new NodeListCommand();
        command.setProcessId(processId);
        List<Node> nodes = ksession.execute(command);
        
        if (nodes!=null && !nodes.isEmpty()){
            return getProcessTree(nodes);
        }
        return null;
    }
    
    private List<String> getProcessTree(List<Node> nodes){
        List<String> result=new ArrayList<String>();
        if (nodes!=null){
            for (Node n:nodes){
                if (n instanceof SubNode){
                    List<String> aux=getProcessTree(((SubNode)n).getNodes());
                    for (String s:aux){
                        if (!result.contains(s))
                            result.add(s);
                    }
                }
                if (!result.contains(n.getProcessId()))
                    result.add(n.getProcessId());
            }
        }
        return result;
    }
}
