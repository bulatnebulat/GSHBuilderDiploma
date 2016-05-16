/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.TGSelfDeactivatingUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public abstract class TGAbstractDragUI
extends TGSelfDeactivatingUI {
    public TGPanel tgPanel;
    private ADUIMouseListener ml;
    private ADUIMouseMotionListener mml;
    public boolean mouseWasDragged;

    public TGAbstractDragUI(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.ml = new ADUIMouseListener();
        this.mml = new ADUIMouseMotionListener();
    }

    public final void activate() {
        this.preActivate();
        this.tgPanel.addMouseListener(this.ml);
        this.tgPanel.addMouseMotionListener(this.mml);
        this.mouseWasDragged = false;
    }

    public final void activate(MouseEvent mouseEvent) {
        this.activate();
        this.mousePressed(mouseEvent);
    }

    public final void deactivate() {
        this.preDeactivate();
        this.tgPanel.removeMouseListener(this.ml);
        this.tgPanel.removeMouseMotionListener(this.mml);
        super.deactivate();
    }

    public abstract void preActivate();

    public abstract void preDeactivate();

    public abstract void mousePressed(MouseEvent var1);

    public abstract void mouseDragged(MouseEvent var1);

    public abstract void mouseReleased(MouseEvent var1);

    private class ADUIMouseMotionListener
    extends MouseMotionAdapter {
        private ADUIMouseMotionListener() {
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            TGAbstractDragUI.this.mouseWasDragged = true;
            TGAbstractDragUI.this.mouseDragged(mouseEvent);
        }
    }

    private class ADUIMouseListener
    extends MouseAdapter {
        private ADUIMouseListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            TGAbstractDragUI.this.mousePressed(mouseEvent);
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            TGAbstractDragUI.this.mouseReleased(mouseEvent);
            if (TGAbstractDragUI.this.selfDeactivate) {
                TGAbstractDragUI.this.deactivate();
            }
        }
    }

}

