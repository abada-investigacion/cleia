/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.responsehandlers;

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
