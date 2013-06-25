/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.runtime.process;

import com.abada.jbpm.definition.process.Node;
import com.abada.jbpm.definition.process.NodeParentListCommand;
import com.abada.jbpm.exception.JbpmException;
import com.abada.jbpm.process.audit.ProcessEventListener;
import com.abada.jbpm.version.model.NodeMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.NodeInstance;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.workflow.core.WorkflowProcess;
import org.jbpm.workflow.core.node.CompositeContextNode;
import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeEnd;
import org.jbpm.workflow.core.node.CompositeNode.CompositeNodeStart;
import org.jbpm.workflow.core.node.DynamicNode;
import org.jbpm.workflow.core.node.EventNode;
import org.jbpm.workflow.core.node.FaultNode;
import org.jbpm.workflow.core.node.ForEachNode;
import org.jbpm.workflow.core.node.ForEachNode.ForEachJoinNode;
import org.jbpm.workflow.core.node.ForEachNode.ForEachSplitNode;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.jbpm.workflow.core.node.StateNode;
import org.jbpm.workflow.core.node.TimerNode;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.jbpm.workflow.instance.node.SubProcessNodeInstance;

/**
 * Comman to jump between nodes
 * 
 * jbpm 5.4.0.Final compliant
 *
 * @author katsu
 */
public class JumpCommand implements GenericCommand<Boolean> {

    private static final Log logger = LogFactory.getLog(JumpCommand.class);
    /**
     * ProcessId of parent process
     */
    private Long processInstanceId;
    /**
     * Node to
     */
    private List<NodeMap> nodeMaps;
    /**
     * Observation to jump wrote by medic
     */
    private String observation;

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<NodeMap> getNodeMaps() {
        return nodeMaps;
    }

    public void setNodeMaps(List<NodeMap> nodeMaps) {
        this.nodeMaps = nodeMaps;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    private ProcessEventListener processEventListener;

    public ProcessEventListener getProcessEventListener() {
        return processEventListener;
    }

    public void setProcessEventListener(ProcessEventListener processEventListener) {
        this.processEventListener = processEventListener;
    }
    private Boolean skipSourceCheck;

    public JumpCommand() {
        this(false);
    }

    public JumpCommand(Boolean skipSourceCheck) {
        this.skipSourceCheck = skipSourceCheck;
    }

    public Boolean execute(Context context) {
        try {
            StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();

            ProcessInstance processInstance = ksession.getProcessInstance(this.getProcessInstanceId());
            synchronized (processInstance) {
                //Check Mappings
                if (!skipSourceCheck) {
                    checkSourceNodes(processInstance, this.nodeMaps, ksession);
                }

                checkTargetNode(this.nodeMaps, ksession);
                //((WorkflowProcessInstanceImpl) processInstance).disconnect();
                //Abort unnecessary                
                List<ProcessInstanceNode> pinms = findUnnecessaryProcessInstances(ksession, processInstance, nodeMaps);
                pinms = findLeafProcessInstances(ksession, pinms);
                logger.debug("Aborting unnecessary process instances to bottom.");
                //check future travel before abort
                checkFutureTravel(pinms);


                //Abort every SubProcessInstance that are unnecessary
                abortProcessInstanceFrom(pinms, ksession);
                //Create SubProcessInstances to the ProcessInstance that have the nodeTo
                //logger.debug("Creating new sub process instances to the new bottom.");
                //pinms = createSubProcessInstances(pinms, ksession);
                //((WorkflowProcessInstanceImpl) processInstance).reconnect();
                //jump to nodeTo
                logger.debug("Last jump to nodeTo");
                jumpTo(pinms);
            }
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    /**
     * Search the common running ProcessInstances with the path of the future nodeTo, abort every unnecessary ProcessInstances to a bottom
     * @param ksession
     * @param processInstance
     * @param nodeTo
     * @return the last common ProcessInstance and the path still ahead to nodeTo
     */
    private ProcessInstanceNode findUnnecessaryProcessInstances(StatefulKnowledgeSession ksession, ProcessInstance processInstance, NodeMap nodeTo) throws Exception {
        ProcessInstanceNode result = new ProcessInstanceNode();

        //Search ProcessInstance from must abort SubProcessInstance and
        ProcessInstance pi = processInstance;
        //
        NodeParentListCommand nplc = new NodeParentListCommand();
        nplc.setNodeTo(nodeTo.getToNode());
        nplc.setProcessId(processInstance.getProcessId());
        List<Node> parents = ksession.execute(nplc);

        if (parents == null || parents.isEmpty()) {
            throw new Exception("NodeTo not found");
        }

        for (int i = 0; i < parents.size() && result.getProcessInstance() == null; i++) {
            NodeInstance ni = getSubNodeInstance(pi, parents.get(i));
            //if pi (ProcessInstance) have ni(NodeInstance) then go down to a SubProcess
            if (ni != null) {
                pi = ksession.getProcessInstance(((SubProcessNodeInstance) ni).getProcessInstanceId());
            } else {
                //Else we find the last common ProessInstance with a path
                result.setProcessInstance(pi);
                result.setNodes(parents.subList(i, parents.size()));
                result.setNodeMap(nodeTo);
            }
        }

        //if we don't find any common path then the root parent ProcessInstance
        if (result.getProcessInstance() == null) {
            result.setProcessInstance(pi);
            result.setNodes(parents);
            result.setNodeMap(nodeTo);
        }

        return result;
    }

    /**
     * Generate a new NodeInstance in a ProcessInstance
     * @param processInstance
     * @param node
     * @return
     */
    private NodeInstance getNodeInstance(ProcessInstance processInstance, Node node) {
        WorkflowProcessInstanceImpl wpi = (WorkflowProcessInstanceImpl) processInstance;
        org.drools.definition.process.Node n = wpi.getNodeContainer().getNode(node.getId());
        if (n != null) {
            return wpi.getNodeInstance(n);
        }
        return null;
    }

    /**
     * Get SubNodeInstance from node information in a ProcessInstance
     * @param processInstance
     * @param node
     * @return
     */
    private NodeInstance getSubNodeInstance(ProcessInstance processInstance, Node node) {
        WorkflowProcessInstance wpi = (WorkflowProcessInstance) processInstance;
        Collection<NodeInstance> nodeInstances = wpi.getNodeInstances();
        if (nodeInstances != null) {
            for (NodeInstance ni : nodeInstances) {
                if (ni.getNodeId() == node.getId()) {
                    if (ni instanceof SubProcessNodeInstance) {
                        return ni;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Abort all SubProcessInstances from parent processInstance to the bottom
     * @param processInstance
     * @param ksession
     */
    private void abortProcessInstanceFrom(ProcessInstance processInstance, StatefulKnowledgeSession ksession) {
        if (processInstance != null) {
            WorkflowProcessInstance wpi = (WorkflowProcessInstance) processInstance;
            Collection<NodeInstance> nodeInstances = wpi.getNodeInstances();
            if (nodeInstances != null) {
                for (NodeInstance ni : nodeInstances) {
                    if (ni instanceof SubProcessNodeInstance) {
                        ProcessInstance pi = ksession.getProcessInstance(((SubProcessNodeInstance) ni).getProcessInstanceId());
                        abortProcessInstanceFrom(pi, ksession);
                        ((NodeInstanceImpl) ni).cancel();
                        //if (this.processEventListener!=null) this.processEventListener.addNodeCancelLog(ni.getProcessInstance().getId(),ni.getProcessInstance().getProcessId(),ni.getId()+"",ni.getNodeId()+"",ni.getNodeName(),this.getObservation());
                        //ksession.abortProcessInstance(pi.getId()); Not needed in Jbpm 5.2
                    } else {
                        ((NodeInstanceImpl) ni).cancel();
                        //if (this.processEventListener!=null) this.processEventListener.addNodeCancelLog(ni.getProcessInstance().getId(),ni.getProcessInstance().getProcessId(),ni.getId()+"",ni.getNodeId()+"",ni.getNodeName(),this.getObservation());
                    }
                    if (this.processEventListener != null) {
                        this.processEventListener.addNodeCancelLog(ni.getProcessInstance().getId(), ni.getProcessInstance().getProcessId(), ni.getId() + "", ni.getNodeId() + "", ni.getNodeName(), this.getObservation());
                    }
                }
            }
        }
    }

    /**
     *
     * @param piwithnodeTo
     * @param nodeTo
     * @return
     */
    private NodeInstance jumpTo(ProcessInstance processInstance, Node nodeTo) {
        NodeInstance nodeInstance = this.getNodeInstance(processInstance, nodeTo);
        if (nodeInstance != null) {
            ((NodeInstanceImpl) nodeInstance).trigger(null, org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE);
        } else {
            //TODO start node
        }
        return nodeInstance;
    }

    private List<ProcessInstanceNode> findUnnecessaryProcessInstances(StatefulKnowledgeSession ksession, ProcessInstance processInstance, List<NodeMap> nodeMaps) throws Exception {
        List<ProcessInstanceNode> result = new ArrayList<ProcessInstanceNode>();
        for (NodeMap n : nodeMaps) {
            result.add(findUnnecessaryProcessInstances(ksession, processInstance, n));
        }
        return result;
    }

    /**
     * Return a list of leaf ProcessInstances 
     * @param ksession
     * @param pinms
     * @return 
     */
    private List<ProcessInstanceNode> findLeafProcessInstances(StatefulKnowledgeSession ksession, List<ProcessInstanceNode> pins) {
        List<ProcessInstanceNode> result = new ArrayList<ProcessInstanceNode>();

        for (ProcessInstanceNode pin : pins) {
            if (isLeafProcessNodeInstance(pin, pins, ksession)) {
                result.add(pin);
            }
        }

        return result;
    }

    private boolean isLeafProcessNodeInstance(ProcessInstanceNode pin, List<ProcessInstanceNode> pins, StatefulKnowledgeSession ksession) {
        boolean result = true;
        //look for subprocess of this process
        Collection<NodeInstance> nodeInstances = ((WorkflowProcessInstance) pin.getProcessInstance()).getNodeInstances();
        if (nodeInstances != null) {
            for (NodeInstance ni : nodeInstances) {
                if (ni instanceof SubProcessNodeInstance) {
                    result = false;
                }
            }
        }
        //if there is no subprocess then is leaf
        if (result) {
            return true;
        }
        return isLeafProcessInstance(pin.getProcessInstance(), pin, pins, ksession);
    }

    private boolean isLeafProcessInstance(ProcessInstance processInstance, ProcessInstanceNode pin, List<ProcessInstanceNode> pins, StatefulKnowledgeSession ksession) {
        for (ProcessInstanceNode pinm1 : pins) {
            if (!pinm1.equals(pin)) {
                if (pinm1.getProcessInstance().equals(processInstance)) {
                    return false;
                }
            }
        }

        ProcessInstance aux;
        Collection<NodeInstance> nodeInstances = ((WorkflowProcessInstance) processInstance).getNodeInstances();
        if (nodeInstances != null) {
            for (NodeInstance ni : nodeInstances) {
                if (ni instanceof SubProcessNodeInstance) {
                    aux = ksession.getProcessInstance(((SubProcessNodeInstance) ni).getProcessInstanceId());
                    if (aux != null && !isLeafProcessInstance(aux, pin, pins, ksession)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void abortProcessInstanceFrom(List<ProcessInstanceNode> pins, StatefulKnowledgeSession ksession) {
        for (ProcessInstanceNode pin : pins) {
            abortProcessInstanceFrom(pin.getProcessInstance(), ksession);
        }
    }

    /**
     * Check for process instance node that has more than 1 node. This means that
     * we has to create sub process instances to go the last node
     * @param pins
     * @throws Exception 
     */
    private void checkFutureTravel(List<ProcessInstanceNode> pins) throws Exception {
        for (ProcessInstanceNode pin : pins) {
            ProcessInstance pi = pin.getProcessInstance();
            if (pin.getNodes() != null && pin.getNodes().size() > 1) {
                //TODO Crear los viajes en el tiempo, Lllamar Marty McFly, y que nos pase el movil del Doc
                throw new JbpmException("Future travel forbidden. Will be included in next revisions. xD");
            }
            //result.add(pinm);
        }
    }

    /**
     * Create SubProcessInstances to the ProcessInstance that have the nodeTo
     * @param pin
     * @return the ProcessInstance where must start nodeTo
     */
    /*private ProcessInstance createSubProcessInstances(ProcessInstanceNode pin, StatefulKnowledgeSession ksession) throws Exception {
    ProcessInstance pi = pin.getProcessInstance();
    if (pin.getNodes() != null && pin.getNodes().size() > 1) {
    //TODO Crear los viajes en el tiempo, Lllamar Marty McFly, y que nos pase el movil del Doc
    throw new JbpmException("Future travel forbidden. Will be included in next revisions. xD");            
    }
    return pin.getProcessInstance();
    }
    
    private List<ProcessInstanceNode> createSubProcessInstances(List<ProcessInstanceNode> pins, StatefulKnowledgeSession ksession) throws Exception {
    //List<ProcessInstanceNodeMap> result=new ArrayList<ProcessInstanceNodeMap>();
    for (ProcessInstanceNode pin : pins) {
    pin.setProcessInstance(createSubProcessInstances(pin, ksession));
    //result.add(pinm);
    }
    return pins;
    }*/
    private void jumpTo(List<ProcessInstanceNode> pins) {
        for (ProcessInstanceNode pin : pins) {
            jumpTo(pin.getProcessInstance(), pin.getNodeMap().getToNode());
        }
    }

    private void checkSourceNodes(ProcessInstance processInstance, List<NodeMap> nodeMaps, StatefulKnowledgeSession ksession) throws Exception {
        for (NodeMap nm : nodeMaps) {
            if (findNodeInstance(processInstance, nm.getFromNode(), ksession) == null) {
                throw new Exception("Source node not found.");
            }
        }
    }

    private NodeInstance findNodeInstance(ProcessInstance processInstance, Node node, StatefulKnowledgeSession ksession) {
        Collection<NodeInstance> nodeInstances = ((WorkflowProcessInstance) processInstance).getNodeInstances();
        if (nodeInstances != null) {
            for (NodeInstance ni : nodeInstances) {
                if (ni.getNode().getId() == node.getId() && processInstance.getProcessId().equals(node.getProcessId())) {
                    return ni;
                } else if (ni instanceof SubProcessNodeInstance) {
                    return findNodeInstance(ksession.getProcessInstance(((SubProcessNodeInstance) ni).getProcessInstanceId()), node, ksession);
                }
            }
        }
        return null;
    }

    /**
     * Check that target node is not a:<br/>
     * Gateway
     * MessageEvent
     * ErrorEvent
     * Timer
     * @param nodeMaps
     * @param ksession 
     */
    private void checkTargetNode(List<NodeMap> nodeMaps, StatefulKnowledgeSession ksession) throws JbpmException {
        org.drools.definition.process.Process process;
        WorkflowProcess process2;
        org.drools.definition.process.Node node;
        for (NodeMap nm : nodeMaps) {
            process = ksession.getKnowledgeBase().getProcess(nm.getToNode().getProcessId());
            if (process == null) {
                throw new JbpmException("Process Target not found.");
            }
            if (process instanceof WorkflowProcess) {
                process2 = (WorkflowProcess) process;

                node = process2.getNode(nm.getToNode().getId());
                if (node == null) {
                    throw new JbpmException("Node Target not found.");
                }
                if (node instanceof Split
                        || node instanceof CompositeNodeStart
                        || node instanceof CompositeNodeEnd
                        || node instanceof ForEachJoinNode
                        || node instanceof TimerNode
                        || node instanceof EventNode
                        || node instanceof FaultNode
                        || node instanceof ForEachSplitNode
                        || node instanceof ForEachNode
                        || node instanceof DynamicNode
                        || node instanceof StateNode
                        || node instanceof CompositeContextNode
                        || node instanceof Join) {
                    throw new JbpmException("Node Target Type error.");
                }
            }
        }
    }
}
