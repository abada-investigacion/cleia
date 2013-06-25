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
package com.abada.jbpm.integration.console.graph;

import com.abada.jbpm.integration.guvnor.GuvnorUtils;
import com.abada.jbpm.process.audit.ProcessInstanceDbLog;
import com.abada.utils.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.drools.KnowledgeBase;
import org.drools.definition.process.Node;
import org.drools.definition.process.NodeContainer;
import org.drools.definition.process.Process;
import org.drools.definition.process.WorkflowProcess;
import org.jboss.bpm.console.client.model.ActiveNodeInfo;
import org.jboss.bpm.console.client.model.DiagramInfo;
import org.jboss.bpm.console.client.model.DiagramNodeInfo;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;

/**
 * 
 * Sustituye a {@link org.jbpm.integration.console.graph.GraphViewerPluginImpl}
 * jbpm 5.4.0.Final compliant
 * 
 * @author Kris Verlaenen
 * @author katsu
 */
public class GraphViewerPluginImpl implements AbadaGraphViewerPlugin {

    //<editor-fold defaultstate="collapsed" desc="Declarations">
    private static final Log logger = LogFactory.getLog(GraphViewerPluginImpl.class);
    private static final String playImage = "com/abada/jbpm/integration/console/graph/image/play.png";
    private static final String okImage = "com/abada/jbpm/integration/console/graph/image/ok.png";
    private static final String xImage = "com/abada/jbpm/integration/console/graph/image/x.png";
    private KnowledgeBase kbase;
    private ProcessInstanceDbLog processInstanceDbLog;

    public void setProcessInstanceDbLog(ProcessInstanceDbLog processInstanceDbLog) {
        this.processInstanceDbLog = processInstanceDbLog;
    }

    public void setKbase(KnowledgeBase kbase) {
        this.kbase = kbase;
    }

    public GraphViewerPluginImpl() {
    }

    public GraphViewerPluginImpl(ProcessInstanceDbLog processInstanceDbLog) {
        this.processInstanceDbLog = processInstanceDbLog;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GraphViewerPlugin">
    public List<ActiveNodeInfo> getActiveNodeInfo(String instanceId) {
        ProcessInstanceLog processInstance = processInstanceDbLog.findProcessInstance(new Long(instanceId));
        if (processInstance == null) {
            throw new IllegalArgumentException("Could not find process instance " + instanceId);
        }
        Map<String, NodeInstanceLog> nodeInstances = new HashMap<String, NodeInstanceLog>();
        for (NodeInstanceLog nodeInstance : processInstanceDbLog.findNodeInstances(new Long(instanceId))) {
            if (nodeInstance.getType() == NodeInstanceLog.TYPE_ENTER) {
                nodeInstances.put(nodeInstance.getNodeInstanceId(), nodeInstance);
            } else {
                nodeInstances.remove(nodeInstance.getNodeInstanceId());
            }
        }
        if (!nodeInstances.isEmpty()) {
            List<ActiveNodeInfo> result = new ArrayList<ActiveNodeInfo>();
            for (NodeInstanceLog nodeInstance : nodeInstances.values()) {
                boolean found = false;
                DiagramInfo diagramInfo = getDiagramInfo(processInstance.getProcessId());
                for (DiagramNodeInfo nodeInfo : diagramInfo.getNodeList()) {
                    if (nodeInfo instanceof com.abada.bpm.console.client.model.DiagramNodeInfo
                            && ((com.abada.bpm.console.client.model.DiagramNodeInfo) nodeInfo).getId().toString().equals(nodeInstance.getNodeId())) {

                        ((com.abada.bpm.console.client.model.DiagramNodeInfo) nodeInfo).setNodeInstanceId(Long.parseLong(nodeInstance.getNodeInstanceId()));
                        ((com.abada.bpm.console.client.model.DiagramNodeInfo) nodeInfo).setProcessInstanceId(Long.parseLong(instanceId));

                        result.add(new ActiveNodeInfo(diagramInfo.getWidth(), diagramInfo.getHeight(), nodeInfo));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalArgumentException("Could not find info for node "
                            + nodeInstance.getNodeId() + " of process " + processInstance.getProcessId());
                }
            }
            return result;
        }
        return null;
    }

    public DiagramInfo getDiagramInfo(String processId) {
        Process process = kbase.getProcess(processId);
        if (process == null) {
            return null;
        }

        DiagramInfo result = new DiagramInfo();
        byte[] imageOriginal = this.getProcessImage(processId);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageOriginal));
            result.setWidth(bufferedImage.getWidth());
            result.setHeight(bufferedImage.getHeight());
        } catch (IOException e) {
            logger.error(e);
            result.setHeight(-1);
            result.setWidth(-1);
        }
        List<DiagramNodeInfo> nodeList = new ArrayList<DiagramNodeInfo>();
        if (process instanceof WorkflowProcess) {
            addNodesInfo(nodeList, ((WorkflowProcess) process).getNodes(), processId);
        }
        result.setNodeList(nodeList);
        return result;
    }

    private void addNodesInfo(List<DiagramNodeInfo> nodeInfos, Node[] nodes, String processId) {
        for (Node node : nodes) {
            nodeInfos.add(new com.abada.bpm.console.client.model.DiagramNodeInfo(
                    node.getId(), -1L,
                    processId, -1L,
                    node.getName(),
                    (Integer) node.getMetaData().get("x"),
                    (Integer) node.getMetaData().get("y"),
                    (Integer) node.getMetaData().get("width"),
                    (Integer) node.getMetaData().get("height")));
            if (node instanceof NodeContainer) {
                addNodesInfo(nodeInfos, ((NodeContainer) node).getNodes(), processId);
            }
        }
    }

    public byte[] getProcessImage(String processId) {
        InputStream is = GraphViewerPluginImpl.class.getResourceAsStream("/" + processId + ".png");
        if (is != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                transfer(is, os);
            } catch (IOException e) {
                throw new RuntimeException("Could not read process image: " + e.getMessage());
            }
            return os.toByteArray();
        }
        StringBuilder sb = new StringBuilder(processId);
        sb.append("-image");
        return GuvnorUtils.getFileAsByte(sb.toString(), ".png");
    }
    private static final int BUFFER_SIZE = 512;

    public static int transfer(InputStream in, OutputStream out) throws IOException {
        int total = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = in.read(buffer);
        while (bytesRead != -1) {
            out.write(buffer, 0, bytesRead);
            total += bytesRead;
            bytesRead = in.read(buffer);
        }
        return total;
    }

    public URL getDiagramURL(String id) {
        URL result = GraphViewerPluginImpl.class.getResource("/" + id + ".png");
        if (result != null) {
            return result;
        }
        StringBuilder sb = new StringBuilder(id);
        sb.append("-image");
        return GuvnorUtils.getURL(sb.toString(), ".png");
    }

    public List<ActiveNodeInfo> getNodeInfoForActivities(
            String processDefinitionId, List<String> activities) {
        // TODO Auto-generated method stub
        return new ArrayList<ActiveNodeInfo>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AbadaGraphViewerPlugin">
    public List<ActiveNodeInfo> getAllActivateNodes(Long processInstanceId) {
        List<ActiveNodeInfo> result = new ArrayList<ActiveNodeInfo>();
        for (ProcessInstanceLog pil : processInstanceDbLog.findProcessInstancesFrom(processInstanceId)) {
            result.addAll(this.getActiveNodeInfo(pil.getProcessInstanceId() + Constants.EMPTY_STRING));
        }
        return result;
    }

    public byte[] getProcessStatusImage(String processId, Date start, Date end) {
        //Cargar info del processoId
        DiagramInfo diagramInfo = this.getDiagramInfo(processId);
        byte[] imageOriginal = this.getProcessImage(processId);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageOriginal));
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setFont(new Font("Serif", Font.BOLD, 16));
            graphics2D.setColor(Color.blue);
            //recorrer los nodos viendo cuantos procesos estan en cada nodo
            Long nin;
            for (DiagramNodeInfo diagramNodeInfo : diagramInfo.getNodeList()) {
                nin = processInstanceDbLog.findNumberProcessInNode(diagramNodeInfo, processId, start, end);
                graphics2D.drawString(nin + "", diagramNodeInfo.getX() + (diagramNodeInfo.getWidth() / 3), diagramNodeInfo.getY() + (diagramNodeInfo.getHeight() / 3));

            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
        }
        return null;
    }

    public byte[] getProcessInstanceStatusImage(Long processInstanceId) {
        ProcessInstanceLog processInstanceLog = this.processInstanceDbLog.findProcessInstance(processInstanceId);
        byte[] image = this.getProcessImage(processInstanceLog.getProcessId());
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setColor(Color.blue);
            graphics2D.setFont(new Font("Serif", Font.BOLD, 16));
            List<NodeInstanceLog> activeNodes = this.processInstanceDbLog.findNodeInstances(processInstanceId);
            activeNodes = removeInactiveNodes(activeNodes);
            DiagramInfo diagramInfo = this.getDiagramInfo(processInstanceLog.getProcessId());
            if (activeNodes != null && !activeNodes.isEmpty()) {
                BufferedImage play = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(playImage));
                BufferedImage ok = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(okImage));
                BufferedImage x = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(xImage));
                int cont;
                DiagramNodeInfo dni;
                for (NodeInstanceLog nodeInstanceLog : activeNodes) {
                    cont = countrepetNode(nodeInstanceLog, activeNodes);
                    if (cont > 0) {
                        dni = this.getDiagramNodeInfo(diagramInfo, nodeInstanceLog);
                        if (dni != null) {
                            if (nodeInstanceLog.getType() == NodeInstanceLog.TYPE_EXIT) {
                                graphics2D.drawImage(ok, dni.getX(), dni.getY(), dni.getWidth() / 3, dni.getHeight() / 3, null);
                            } else if (nodeInstanceLog.getType() == NodeInstanceLog.TYPE_ENTER) {
                                graphics2D.drawImage(play, dni.getX(), dni.getY(), dni.getWidth() / 3, dni.getHeight() / 3, null);
                            } else {
                                graphics2D.drawImage(x, dni.getX(), dni.getY(), dni.getWidth() / 3, dni.getHeight() / 3, null);
                            }
                            if (cont > 1) {
                                graphics2D.drawString(cont + "", dni.getX() + (dni.getWidth() / 3), dni.getY() + (dni.getHeight() / 3));
                            }

                        }
                    }
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
        }
        return null;
    }

    protected DiagramNodeInfo getDiagramNodeInfo(DiagramInfo diagramInfo, NodeInstanceLog nodeInstanceLog) {
        for (DiagramNodeInfo dni : diagramInfo.getNodeList()) {
            if (dni instanceof com.abada.bpm.console.client.model.DiagramNodeInfo
                    && ((com.abada.bpm.console.client.model.DiagramNodeInfo) dni).getId().toString().equals(nodeInstanceLog.getNodeId())) {
                return dni;
            }
        }
        return null;
    }

    protected List<NodeInstanceLog> removeInactiveNodes(List<NodeInstanceLog> activeNodes) {
        List<NodeInstanceLog> result = new ArrayList<NodeInstanceLog>();
        boolean add;
        for (NodeInstanceLog nil : activeNodes) {
            if (nil.getType() == NodeInstanceLog.TYPE_ENTER) {
                add = true;
                for (NodeInstanceLog nil2 : activeNodes) {
                    if (nil2.getNodeInstanceId().equals(nil.getNodeInstanceId()) && nil2.getType() >= NodeInstanceLog.TYPE_EXIT) {
                        add = false;
                        result.add(nil2);
                    }
                }
                if (add) {
                    result.add(nil);
                }
            }
        }
        return result;
    }

    private int countrepetNode(NodeInstanceLog nodeInstanceLog, List<NodeInstanceLog> activeNodes) {
        int cont = 0;
        boolean salir = false;
        for (int i = 0; i < activeNodes.size() && !salir; i++) {
            if (activeNodes.get(i).getNodeId().equals(nodeInstanceLog.getNodeId())) {
                if ((nodeInstanceLog.getDate().getTime()) < activeNodes.get(i).getDate().getTime()) {
                    cont = 0;
                    salir = true;
                } else {
                    cont++;
                }
            }
        }
        return cont;
    }
    //</editor-fold>
}
