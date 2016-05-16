/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintStream;

public class LocalityScroll
implements GraphListener {
    private Scrollbar localitySB;
    private TGPanel tgPanel;

    public LocalityScroll(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.localitySB = new Scrollbar(0, 1, 1, 0, 7);
        this.localitySB.setBlockIncrement(1);
        this.localitySB.setUnitIncrement(1);
        this.localitySB.addAdjustmentListener(new localityAdjustmentListener());
        this.tgPanel.addGraphListener(this);
    }

    public Scrollbar getLocalitySB() {
        return this.localitySB;
    }

    public int getLocalityRadius() {
        int n = this.localitySB.getValue();
        if (n >= 6) {
            return Integer.MAX_VALUE;
        }
        return n;
    }

    public void setLocalityRadius(int n) {
        if (n <= 0) {
            this.localitySB.setValue(0);
        } else if (n <= 5) {
            this.localitySB.setValue(n);
        } else {
            this.localitySB.setValue(6);
        }
    }

    public void graphMoved() {
    }

    public void graphReset() {
        this.localitySB.setValue(1);
    }

    private class localityAdjustmentListener
    implements AdjustmentListener {
        private localityAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            Node node = LocalityScroll.this.tgPanel.getSelect();
            if (node != null || LocalityScroll.this.getLocalityRadius() == Integer.MAX_VALUE) {
                try {
                    LocalityScroll.this.tgPanel.setLocale(node, LocalityScroll.this.getLocalityRadius());
                }
                catch (TGException var3_3) {
                    System.out.println("Error setting locale");
                    var3_3.printStackTrace();
                }
            }
        }
    }

}

