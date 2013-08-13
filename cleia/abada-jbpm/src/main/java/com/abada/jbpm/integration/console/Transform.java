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

/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.drools.definition.process.Process;
import org.jboss.bpm.console.client.model.ProcessDefinitionRef;
import org.jboss.bpm.console.client.model.ProcessInstanceRef;
import org.jboss.bpm.console.client.model.TaskRef;
import org.jboss.bpm.console.client.model.TokenReference;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.task.I18NText;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transform {

    private static final Logger logger = LoggerFactory.getLogger(Transform.class);

    public static ProcessDefinitionRef processDefinition(Process process) {
        long version = 0;
        try {
            version = new Long(process.getVersion());
        } catch (NumberFormatException e) {
            // Do nothing, keep version 0
        }
        ProcessDefinitionRef result = new ProcessDefinitionRef(
                process.getId(), process.getName(), version);
        result.setPackageName(process.getPackageName());
        result.setDeploymentId("N/A");
        return result;
    }

    public static ProcessInstanceRef processInstance(ProcessInstanceLog processInstance) {
        ProcessInstanceRef result = new ProcessInstanceRef(
                processInstance.getProcessInstanceId() + "",
                processInstance.getProcessId(),
                processInstance.getStart(),
                processInstance.getEnd(),
                false);
        TokenReference token = new TokenReference(
                processInstance.getProcessInstanceId() + "", null, "");
        result.setRootToken(token);

        /*if (activeNodes != null && !activeNodes.isEmpty()) {
            try {
                Iterator<NodeInstance> it = activeNodes.iterator();
                List<TokenReference> children = new ArrayList<TokenReference>();
                TokenReference childToken = null;
                StringBuffer nodeNames = new StringBuffer();
                while (it.hasNext()) {
                    NodeInstance nodeInstance = (NodeInstance) it.next();
                    childToken = new TokenReference(processInstance.getProcessInstanceId() + "", null, nodeInstance.getNodeName());
                    if (nodeNames.length() > 0) {
                        nodeNames.append(", ");
                    }
                    nodeNames.append(nodeInstance.getNodeName());
                    if (nodeInstance instanceof EventNodeInstance) {
                        String type = ((EventNodeInstance) nodeInstance).getEventNode().getType();
                        if (type != null && !type.startsWith("Message-")) {
                            childToken.setName(type);
                            childToken.setCanBeSignaled(true);
                        }

                    }

                    children.add(childToken);
                }
                token.setChildren(children);
                token.setCurrentNodeName(nodeNames.toString());
            } catch (Exception e) {
                logger.error("Error when collecting node information", e);
            }
        }*/
        return result;
    }

    public static TaskRef task(TaskSummary task) {
        return new TaskRef(
                task.getId(),
                Long.toString(task.getProcessInstanceId()),
                task.getProcessId() == null ? "" : task.getProcessId(),
                task.getName(),
                task.getActualOwner() == null ? null : task.getActualOwner().getId(),
                !task.isSkipable(),
                false);
    }

    public static TaskRef task(Task task) {
        String name = "";
        for (I18NText text : task.getNames()) {
            if ("en-UK".equals(text.getLanguage())) {
                name = text.getText();
            }
        }
        return new TaskRef(
                task.getId(),
                Long.toString(task.getTaskData().getProcessInstanceId()),
                task.getTaskData().getProcessId() == null ? "" : task.getTaskData().getProcessId(),
                name,
                task.getTaskData().getActualOwner() == null ? null : task.getTaskData().getActualOwner().getId(),
                !task.getTaskData().isSkipable(),
                false);
    }
}
