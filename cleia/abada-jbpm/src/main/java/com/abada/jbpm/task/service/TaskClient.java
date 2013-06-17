/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service;

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
import com.abada.jbpm.integration.console.task.GroupTaskManagement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.drools.runtime.Environment;

import org.jbpm.task.service.BaseHandler;
import org.jbpm.task.service.Command;
import org.jbpm.task.service.CommandName;
import org.jbpm.task.service.Operation;
import org.jbpm.task.service.TaskClientConnector;
import org.jbpm.task.service.TaskClientHandler.TaskOperationResponseHandler;

/**
 * Sustituye {@link org.jbpm.task.service.TaskClient} jbpm 5.4.0.Final compliant
 *
 * @author katsu
 */
public class TaskClient extends org.jbpm.task.service.TaskClient {

    private final BaseHandler handler;
    private final AtomicInteger counter;
    private final TaskClientConnector connector;
    private final GroupTaskManagement groupTaskManagement;

    public TaskClient(TaskClientConnector connector, Environment environment, GroupTaskManagement groupTaskManagement) {
        super(connector, environment);
        this.connector = connector;
        this.counter = connector.getCounter();
        this.handler = connector.getHandler();
        this.groupTaskManagement = groupTaskManagement;
    }

    public TaskClient(TaskClientConnector connector, GroupTaskManagement groupTaskManagement) {
        super(connector);
        this.connector = connector;
        this.counter = connector.getCounter();
        this.handler = connector.getHandler();
        this.groupTaskManagement = groupTaskManagement;
    }

    @Override
    public void start(long taskId,
            String userId,
            TaskOperationResponseHandler responseHandler) {
        List<Object> args = new ArrayList<Object>(6);
        args.add(Operation.Start);
        args.add(taskId);
        args.add(userId);
        args.add(null);
        args.add(null);
        args.add(groupTaskManagement.getGroupByUserName(userId));
        Command cmd = new Command(counter.getAndIncrement(),
                CommandName.OperationRequest,
                args);

        handler.addResponseHandler(cmd.getId(),
                responseHandler);

        connector.write(cmd);
    }
}