/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.cleia.rest.entity;

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

import com.abada.jbpm.version.model.NodeMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author katsu
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JumpInfo {
    private String observation;
    private boolean fixProcess;
    private NodeMap [] nodes;

    public NodeMap[] getNodes() {
        return nodes;
    }

    public void setNodes(NodeMap[] nodes) {
        this.nodes = nodes;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public boolean isFixProcess() {
        return fixProcess;
    }

    public void setFixProcess(boolean fixProcess) {
        this.fixProcess = fixProcess;
    }        
}
