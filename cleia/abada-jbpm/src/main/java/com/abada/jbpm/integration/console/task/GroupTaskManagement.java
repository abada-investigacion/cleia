/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.console.task;

import java.util.List;

/**
 *
 * @author katsu
 */
public interface GroupTaskManagement {
    /**
     * Return all groups for a user
     * @param username
     * @return 
     */
    List<String> getGroupByUserName(String username);
}
