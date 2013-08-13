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
package com.abada.jbpm.integration.console.form;

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

import com.abada.jbpm.task.service.TaskServiceFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jbpm.task.Content;
import org.jbpm.task.I18NText;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.SyncTaskServiceWrapper;
import org.jbpm.task.utils.ContentMarshallerHelper;

/**
 * Sustituye {@link org.jbpm.integration.console.forms.TaskFormDispatcher} jbpm
 * 5.4.0.Final compliant
 *
 * @author Kris Verlaenen
 * @author katsu
 */
public class TaskFormDispatcher extends AbstractFormDispatcher implements Observer {

    private static final Log logger = LogFactory.getLog(TaskFormDispatcher.class);
    private TaskService service;
    private TaskServiceFactory taskClientFactory;

    public void setTaskClientFactory(TaskServiceFactory taskClientFactory) {
        this.taskClientFactory = taskClientFactory;
    }

    public void connect() {
        if (service == null) {
            this.service = new SyncTaskServiceWrapper(taskClientFactory.getTaskClient());
            if (!service.connect()) {
                throw new IllegalArgumentException(
                        "Could not connect task client");
            }
        }
    }

    public DataHandler provideForm(FormAuthorityRef ref) {
        connect();
        Task task = service.getTask(new Long(ref.getReferenceId()));

        Object input = null;
        long contentId = task.getTaskData().getDocumentContentId();
        if (contentId != -1) {
            Content content = null;

            content = service.getContent(contentId);
            input = ContentMarshallerHelper.unmarshall(content.getContent(), null);
        }

        // check if a template exists
        String name = null;
        List<I18NText> names = task.getNames();
        for (I18NText text : names) {
            if ("en-UK".equals(text.getLanguage())) {
                name = text.getText();
            }
        }
        InputStream template = getTemplate(name);
        if (template == null) {
            template = TaskFormDispatcher.class.getResourceAsStream("/DefaultTask.ftl");
        }

        // merge template with process variables
        Map<String, Object> renderContext = new HashMap<String, Object>();
        renderContext.put("task", task);
        renderContext.put("content", input);
        if (this.getProcessManagement() != null) {
            Map<String, Object> add = this.getProcessManagement().getInstanceData(task.getTaskData().getProcessInstanceId() + "");
            renderContext.putAll(toRootMap(add));
        }
        if (input instanceof Map) {
            Map<?, ?> map = (Map) input;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() instanceof String) {
                    renderContext.put((String) entry.getKey(), entry.getValue());
                }
            }
        }

        if (ref instanceof com.abada.bpm.console.server.plugin.FormAuthorityRef){
            renderContext.put("WEB_BROWSER_DEVICE", ((com.abada.bpm.console.server.plugin.FormAuthorityRef)ref).getDevice());
        }
        return processTemplate(name, template, renderContext);
    }

    private Map<String, Object> toRootMap(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (map != null) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof Map) {
                    result.putAll(toRootMap((Map<String, Object>) value));
                } else {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    public void update(Observable o, Object o1) {
        if (o instanceof TaskService && o1 instanceof String) {
            if (com.abada.jbpm.task.spring.TaskService.BEFORE_STOP_EVENT.equals(o1)) {
                try {
                    service.disconnect();
                    logger.debug("Disconnect client.");
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }
}
