/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.bpm.console.client.model;

/**
 *
 * @author katsu
 */
public class ActiveNodeInfo{

    private int width = -1;
    private int height = -1;
    private DiagramNodeInfo activeNode;

    public DiagramNodeInfo getActiveNode() {
        return activeNode;
    }

    public void setActiveNode(DiagramNodeInfo activeNode) {
        this.activeNode = activeNode;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }        
}
