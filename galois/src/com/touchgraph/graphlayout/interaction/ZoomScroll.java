/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.TGPoint2D;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class ZoomScroll
implements GraphListener {
    protected ZoomLens zoomLens;
    private Scrollbar zoomSB;
    private TGPanel tgPanel;

    public ZoomScroll(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.zoomSB = new Scrollbar(0, -4, 7, -31, 19);
        this.zoomSB.addAdjustmentListener(new zoomAdjustmentListener());
        this.zoomLens = new ZoomLens();
        this.tgPanel.addGraphListener(this);
    }

    public Scrollbar getZoomSB() {
        return this.zoomSB;
    }

    public ZoomLens getLens() {
        return this.zoomLens;
    }

    public void graphMoved() {
    }

    public void graphReset() {
        this.zoomSB.setValue(-10);
    }

    public int getZoomValue() {
        double d = this.zoomSB.getValue() - this.zoomSB.getMinimum();
        double d2 = this.zoomSB.getMaximum() - this.zoomSB.getMinimum() - this.zoomSB.getVisibleAmount();
        return (int)(d / d2 * 200.0 - 100.0);
    }

    public void setZoomValue(int n) {
        double d = this.zoomSB.getMaximum() - this.zoomSB.getMinimum() - this.zoomSB.getVisibleAmount();
        this.zoomSB.setValue((int)((double)(n + 100) / 200.0 * d + 0.5) + this.zoomSB.getMinimum());
    }

    class ZoomLens
    extends TGAbstractLens {
        ZoomLens() {
        }

        protected void applyLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x *= Math.pow(2.0, (double)ZoomScroll.this.zoomSB.getValue() / 10.0);
            tGPoint2D.y *= Math.pow(2.0, (double)ZoomScroll.this.zoomSB.getValue() / 10.0);
        }

        protected void undoLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x /= Math.pow(2.0, (double)ZoomScroll.this.zoomSB.getValue() / 10.0);
            tGPoint2D.y /= Math.pow(2.0, (double)ZoomScroll.this.zoomSB.getValue() / 10.0);
        }
    }

    private class zoomAdjustmentListener
    implements AdjustmentListener {
        private zoomAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            ZoomScroll.this.tgPanel.repaintAfterMove();
        }
    }

}

