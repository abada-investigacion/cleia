/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.version.model;

import com.abada.jbpm.definition.process.Node;

/**
 *
 * @author katsu
 */
public class NodeMap {
    private Node toNode;
    private Node fromNode;

    public Node getFromNode() {
        return fromNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }       
}
