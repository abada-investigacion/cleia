/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.definition.process;

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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Command that result is a list of event nodes from a process
 * 
 * jbpm 5.4.0.Final compliant
 * @author katsu
 */
public class FindEventNodes implements GenericCommand<List<Node>> {

    private static final Log logger = LogFactory.getLog(FindEventNodes.class);
    /**
     * Process Id
     */
    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<Node> execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        if (getProcessId() == null || getProcessId().isEmpty()) {
            return null;
        }

        //List with all nodes
        NodeListCommand nlc = new NodeListCommand();
        nlc.setProcessId(this.getProcessId());
        List<Node> list = ksession.execute(nlc);
        return findEventNodes(list);
    }

    private List<Node> findEventNodes(List<Node> list) {
        List<Node> result = new ArrayList<Node>();
        if (list != null) {
            for (Node n : list) {
                if (n instanceof SubNode) {
                    result.addAll(findEventNodes(((SubNode)n).getNodes()));
                } else if (n instanceof EventNode && !result.contains(n)) {
                    result.add(n);
                }
            }
        }
        return result;
    }
}
