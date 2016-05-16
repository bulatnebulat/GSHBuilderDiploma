/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class DragNodeUI
extends TGAbstractDragUI {
    public Point dragOffs;

    public DragNodeUI(TGPanel tGPanel) {
        super(tGPanel);
    }

    public void preActivate() {
        if (this.dragOffs == null) {
            this.dragOffs = new Point(0, 0);
        }
    }

    public void preDeactivate() {
    }

    public void mousePressed(MouseEvent mouseEvent) {
        Node node = this.tgPanel.getMouseOverN();
        Point point = mouseEvent != null ? mouseEvent.getPoint() : new Point((int)node.drawx, (int)node.drawy);
        if (node != null) {
            this.tgPanel.setDragNode(node);
            this.dragOffs.setLocation((int)(node.drawx - (double)point.x), (int)(node.drawy - (double)point.y));
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        this.tgPanel.setDragNode(null);
        this.tgPanel.repaintAfterMove();
        this.tgPanel.startDamper();
    }

    public synchronized void mouseDragged(MouseEvent mouseEvent) {
        Node node = this.tgPanel.getDragNode();
        node.drawx = mouseEvent.getX() + this.dragOffs.x;
        node.drawy = mouseEvent.getY() + this.dragOffs.y;
        this.tgPanel.updatePosFromDraw(node);
        this.tgPanel.repaintAfterMove();
        this.tgPanel.stopDamper();
        mouseEvent.consume();
    }
}

