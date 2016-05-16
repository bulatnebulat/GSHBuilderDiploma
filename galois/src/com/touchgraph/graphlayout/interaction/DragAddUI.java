/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPaintListener;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

public class DragAddUI
extends TGAbstractDragUI
implements TGPaintListener {
    Point mousePos = null;
    Node dragAddNode = null;

    public DragAddUI(TGPanel tGPanel) {
        super(tGPanel);
    }

    public void preActivate() {
        this.mousePos = null;
        this.tgPanel.addPaintListener(this);
    }

    public void preDeactivate() {
        this.tgPanel.removePaintListener(this);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        this.dragAddNode = this.tgPanel.getMouseOverN();
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        Node node = this.tgPanel.getMouseOverN();
        if (node != null && this.dragAddNode != null && node != this.dragAddNode) {
            Edge edge = this.tgPanel.findEdge(this.dragAddNode, node);
            if (edge == null) {
                this.tgPanel.addEdge(this.dragAddNode, node, Edge.DEFAULT_LENGTH);
            } else {
                this.tgPanel.deleteEdge(edge);
            }
        } else if (node == null && this.dragAddNode != null) {
            try {
                Node node2 = this.tgPanel.addNode();
                this.tgPanel.addEdge(this.dragAddNode, node2, Edge.DEFAULT_LENGTH);
                node2.drawx = this.tgPanel.getMousePos().x;
                node2.drawy = this.tgPanel.getMousePos().y;
                this.tgPanel.updatePosFromDraw(node2);
            }
            catch (TGException var3_5) {
                System.err.println(var3_5.getMessage());
                var3_5.printStackTrace(System.err);
            }
        }
        if (this.mouseWasDragged) {
            this.tgPanel.resetDamper();
            this.tgPanel.startDamper();
        }
        this.dragAddNode = null;
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        this.mousePos = mouseEvent.getPoint();
        this.tgPanel.repaint();
    }

    public void paintFirst(Graphics graphics) {
    }

    public void paintLast(Graphics graphics) {
    }

    public void paintAfterEdges(Graphics graphics) {
        if (this.mousePos == null) {
            return;
        }
        Node node = this.tgPanel.getMouseOverN();
        if (node == null) {
            graphics.drawRect(this.mousePos.x - 7, this.mousePos.y - 7, 14, 14);
        }
        Color color = node == this.dragAddNode ? Color.darkGray : Color.blue;
        Edge.paintArrow(graphics, (int)this.dragAddNode.drawx, (int)this.dragAddNode.drawy, this.mousePos.x, this.mousePos.y, color);
    }
}

