/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.runtime.process;

import com.abada.jbpm.definition.process.Node;
import com.abada.jbpm.version.model.NodeMap;
import java.util.List;
import org.drools.runtime.process.ProcessInstance;

class ProcessInstanceNode {

    /**
     * ProcessInstance from have to start the jump
     */
    private ProcessInstance processInstance;
    /**
     * Nodes that have to trigger from processInstance, the last node is the the nodeTo
     */
    private List<Node> nodes;
    /**
     * Node To
     */
    private NodeMap nodeMap;

    public ProcessInstanceNode() {
        this.setNodes(null);
        this.setProcessInstance(null);
        this.setNodeMap(null);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public NodeMap getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(NodeMap nodeMap) {
        this.nodeMap = nodeMap;
    }        
}