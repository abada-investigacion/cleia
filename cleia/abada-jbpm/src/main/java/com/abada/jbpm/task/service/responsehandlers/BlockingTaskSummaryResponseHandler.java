/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.responsehandlers;

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
import org.jbpm.task.query.TaskSummary;

/**
 *
 * @author katsu
 */
public class BlockingTaskSummaryResponseHandler extends org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler{

    private static final int RESULTS_WAIT_TIME = 60000;

    private volatile List<TaskSummary> results;

    @Override
    public synchronized void execute(List<TaskSummary> results) {
        this.results = results;
        setDone(true);
    }

    @Override
    public List<TaskSummary> getResults() {
        // note that this method doesn't need to be synced because if waitTillDone returns true,
        // it means results is available 
        boolean done = waitTillDone(RESULTS_WAIT_TIME);

        if (!done) {
            throw new RuntimeException("Timeout : unable to retrieve results");
        }

        return results;
    } 
}
