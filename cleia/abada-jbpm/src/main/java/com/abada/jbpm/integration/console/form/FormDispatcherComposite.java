/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import com.abada.jbpm.integration.guvnor.GuvnorUtils;
import com.abada.jbpm.task.service.TaskServiceFactory;
import java.net.URL;

import javax.activation.DataHandler;
import org.jboss.bpm.console.server.integration.ProcessManagement;

import org.jboss.bpm.console.server.plugin.FormAuthorityRef;
import org.jboss.bpm.console.server.plugin.FormDispatcherPlugin;

/**
 * Sustituye a {@link org.jbpm.integration.console.forms.FormDispatcherComposite}
 * jbpm 5.4.0.Final compliant
 * 
 * @author katsu
 */
public class FormDispatcherComposite implements FormDispatcherPlugin {
    
    private FormDispatcherPlugin taskDispatcher;
    private FormDispatcherPlugin processDispatcher;      

    public void setGuvnorUtils(GuvnorUtils guvnorUtils) {
        ((TaskFormDispatcher)this.taskDispatcher).setGuvnorUtils(guvnorUtils);
        ((ProcessFormDispatcher)this.processDispatcher).setGuvnorUtils(guvnorUtils);
    }

    /*public void setTaskService(TaskService taskService) {
        ((TaskFormDispatcher)this.taskDispatcher).setTaskService(taskService);
    }*/

    public void setTaskClientFactory(TaskServiceFactory taskClientFactory) {
        ((TaskFormDispatcher)this.taskDispatcher).setTaskClientFactory(taskClientFactory);
    }

    public void setProcessManagement(ProcessManagement processManagement){        
        ((TaskFormDispatcher)this.taskDispatcher).setProcessManagement(processManagement);
        ((ProcessFormDispatcher)this.processDispatcher).setProcessManagement(processManagement);
    }
    
    public FormDispatcherComposite(){
        this(null);
    }
    
    public FormDispatcherComposite(ProcessManagement processManagement) {
        this.taskDispatcher = new TaskFormDispatcher();        
        this.processDispatcher = new ProcessFormDispatcher();
        this.setProcessManagement(processManagement);
    }

    public URL getDispatchUrl(FormAuthorityRef ref) {
        switch (ref.getType()) {
            case TASK:
                return taskDispatcher.getDispatchUrl(ref);
            case PROCESS:
                return processDispatcher.getDispatchUrl(ref);
            default:
                throw new IllegalArgumentException("Unknown authority type:" + ref.getType());
        }
    }

    public DataHandler provideForm(FormAuthorityRef ref) {
        switch (ref.getType()) {
            case TASK:
                return taskDispatcher.provideForm(ref);
            case PROCESS:
                return processDispatcher.provideForm(ref);
            default:
                throw new IllegalArgumentException("Unknown authority type:" + ref.getType());
        }
    }
}
