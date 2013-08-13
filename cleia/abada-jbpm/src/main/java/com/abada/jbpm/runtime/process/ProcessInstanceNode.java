/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.runtime.process;

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