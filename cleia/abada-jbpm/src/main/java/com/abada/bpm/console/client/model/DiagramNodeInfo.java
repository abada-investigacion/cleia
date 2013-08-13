/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.client.model;

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

/**
 *
 * @author katsu
 */
public class DiagramNodeInfo extends org.jboss.bpm.console.client.model.DiagramNodeInfo{
    private Long id;
    private Long nodeInstanceId;
    private String processId;
    private Long processInstanceId;

    public DiagramNodeInfo(String name, int x, int y, int width, int height, Long id, Long nodeInstanceId) {
        this(id, nodeInstanceId, null, null, name, x, y, width, height);                
    }                

    public DiagramNodeInfo(Long id, Long nodeInstanceId, String processId, Long processInstanceId, String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.id = id;
        this.nodeInstanceId = nodeInstanceId;
        this.processId = processId;
        this.processInstanceId = processInstanceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNodeInstanceId() {
        return nodeInstanceId;
    }

    public void setNodeInstanceId(Long nodeInstanceId) {
        this.nodeInstanceId = nodeInstanceId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }        
}
