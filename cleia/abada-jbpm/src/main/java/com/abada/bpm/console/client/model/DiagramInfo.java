/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.client.model;

import java.util.List;

/**
 *
 * @author katsu
 */
public class DiagramInfo {

    private int width;
    private int height;
    private List<DiagramNodeInfo> nodeList;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<DiagramNodeInfo> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<DiagramNodeInfo> nodeList) {
        this.nodeList = nodeList;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }        
}
