/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console;

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

import java.util.List;
import java.util.Map;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;

/**
 *
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public interface AbadaProcessManagementPlugin {
    /**
     * Return the process instances with the inheratance of process instance passed
     * @param processInstanceId
     * @return 
     */
    public List<ProcessInstanceLog> getProcessInstanceTreeExecution(Long processInstanceId);
    
    /**
     * Return a history of nodes for a process instance
     * @param processInstanceId
     * @return 
     */
    public List<NodeInstanceLog> getNodeHistoryList(Long processInstanceId);
    /**
     * Signal to subprocess
     * @param parentProcessId
     * @param processId
     * @param signal
     * @param type 
     */
    public void signalExecution(Long parentProcessId,String processId, Map<String,Object> signal, String type);
}
