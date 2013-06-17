/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.process.audit;

/**
 *
 * @author katsu
 */
public interface ProcessEventListener {
    public void addNodeCancelLog(long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName,String observation);
    public void addNodeChangeVersionLog(long processInstanceId, String processId, String nodeInstanceId, String nodeId, String nodeName,String observation);
}
