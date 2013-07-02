/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Node Information
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class Node {

    /**
     * Node name
     */
    private String name;
    /**
     * Node type
     */
    private String type;
    /**
     * Node id, maybe componsite node
     */
    private Long id;
    /**
     * ProcessId who belongs the node
     */
    private String processId;
    /**
     * In case the node was a SubNode then this is the parent
     */
    @JsonIgnore
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processName) {
        this.processId = processName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }        

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        /*if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }*/
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.processId == null) ? (other.processId != null) : !this.processId.equals(other.processId)) {
            return false;
        }
        /*if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }*/

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
