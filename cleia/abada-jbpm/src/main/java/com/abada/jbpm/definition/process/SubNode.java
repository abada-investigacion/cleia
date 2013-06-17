/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

import java.util.List;

/**
 * SubNode information
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class SubNode extends Node {

    /**
     * Nodes that belong the subproces
     */
    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubNode other = (SubNode) obj;
        if (other.getNodes() !=null && this.getNodes()!=null && this.getNodes().size()== other.getNodes().size()){
            for (Node n:this.getNodes()){
                if (!other.getNodes().contains(n)){
                    return false;
                }
            }
        }
        return true;
    }
}
