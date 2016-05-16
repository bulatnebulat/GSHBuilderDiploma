/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.TGSelfDeactivatingUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class TGAbstractClickUI
extends TGSelfDeactivatingUI {
    private ACUIMouseListener ml;
    private TGPanel tgPanel;

    public TGAbstractClickUI() {
        this.tgPanel = null;
        this.ml = null;
    }

    public TGAbstractClickUI(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.ml = new ACUIMouseListener();
    }

    public final void activate() {
        if (this.tgPanel != null && this.ml != null) {
            this.tgPanel.addMouseListener(this.ml);
        }
    }

    public final void activate(MouseEvent mouseEvent) {
        this.mouseClicked(mouseEvent);
    }

    public final void deactivate() {
        if (this.tgPanel != null && this.ml != null) {
            this.tgPanel.removeMouseListener(this.ml);
        }
        super.deactivate();
    }

    public abstract void mouseClicked(MouseEvent var1);

    private class ACUIMouseListener
    extends MouseAdapter {
        private ACUIMouseListener() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            TGAbstractClickUI.this.mouseClicked(mouseEvent);
            if (TGAbstractClickUI.this.selfDeactivate) {
                TGAbstractClickUI.this.deactivate();
            }
        }
    }

}

