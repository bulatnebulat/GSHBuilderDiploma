/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.TGPaintListener;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.TGPoint2D;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class DragMultiselectUI
extends TGAbstractDragUI
implements TGPaintListener {
    TGPoint2D mousePos = null;
    TGPoint2D startPos = null;

    DragMultiselectUI(TGPanel tGPanel) {
        super(tGPanel);
    }

    public void preActivate() {
        this.startPos = null;
        this.mousePos = null;
        this.tgPanel.addPaintListener(this);
    }

    public void preDeactivate() {
        this.tgPanel.removePaintListener(this);
        this.tgPanel.repaint();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        this.startPos = new TGPoint2D(mouseEvent.getX(), mouseEvent.getY());
        this.mousePos = new TGPoint2D(this.startPos);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        this.mousePos.setLocation(mouseEvent.getX(), mouseEvent.getY());
        this.tgPanel.multiSelect(this.startPos, this.mousePos);
        this.tgPanel.repaint();
    }

    public void paintFirst(Graphics graphics) {
    }

    public void paintAfterEdges(Graphics graphics) {
    }

    public void paintLast(Graphics graphics) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.mousePos == null) {
            return;
        }
        graphics.setColor(Color.black);
        if (this.startPos.x < this.mousePos.x) {
            n2 = (int)this.startPos.x;
            n4 = (int)(this.mousePos.x - this.startPos.x);
        } else {
            n2 = (int)this.mousePos.x;
            n4 = (int)(this.startPos.x - this.mousePos.x);
        }
        if (this.startPos.y < this.mousePos.y) {
            n3 = (int)this.startPos.y;
            n = (int)(this.mousePos.y - this.startPos.y);
        } else {
            n3 = (int)this.mousePos.y;
            n = (int)(this.startPos.y - this.mousePos.y);
        }
        int n5 = n2;
        while (n5 < n2 + n4) {
            graphics.drawLine(n5, n3, n5, n3);
            graphics.drawLine(n5, n3 + n, n5, n3 + n);
            n5 += 2;
        }
        int n6 = n3;
        while (n6 < n3 + n) {
            graphics.drawLine(n2, n6, n2, n6);
            graphics.drawLine(n2 + n4, n6, n2 + n4, n6);
            n6 += 2;
        }
    }
}

