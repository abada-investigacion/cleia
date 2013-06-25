/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.version.dao;

/**
 *
 * @author katsu
 */
public interface ProcessInstanceInfoDao {
    boolean updateProcessId(String processId, Long id);
}
