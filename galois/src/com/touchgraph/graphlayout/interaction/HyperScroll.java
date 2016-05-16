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

public class HyperScroll
implements GraphListener {
    private Scrollbar hyperSB;
    private TGPanel tgPanel;
    HyperLens hyperLens;
    double[] inverseArray = new double[200];
    double width;

    public HyperScroll(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.hyperSB = new Scrollbar(0, 100, 8, 0, 108);
        this.hyperSB.addAdjustmentListener(new hyperAdjustmentListener());
        this.hyperLens = new HyperLens();
        this.width = 2000.0;
        this.updateInverseArray();
        this.tgPanel.addGraphListener(this);
    }

    public Scrollbar getHyperSB() {
        return this.hyperSB;
    }

    public HyperLens getLens() {
        return this.hyperLens;
    }

    public void graphMoved() {
    }

    public void graphReset() {
        this.hyperSB.setValue(0);
    }

    double rawHyperDist(double d) {
        if (this.hyperSB.getValue() == 0) {
            return d;
        }
        double d2 = this.hyperSB.getValue();
        return Math.log(d / (Math.pow(1.5, (70.0 - d2) / 40.0) * 80.0) + 1.0);
    }

    double hyperDist(double d) {
        double d2 = this.hyperSB.getValue();
        double d3 = this.rawHyperDist(d) / this.rawHyperDist(250.0) * 250.0;
        double d4 = d2;
        double d5 = 100.0;
        d3 = d3 * d4 / d5 + d * (d5 - d4) / d5;
        return d3;
    }

    void updateInverseArray() {
        int n = 0;
        while (n < 200) {
            double d = this.width * (double)n / 200.0;
            this.inverseArray[n] = this.hyperDist(d);
            ++n;
        }
    }

    int findInd(int n, int n2, double d) {
        int n3 = (n + n2) / 2;
        if (this.inverseArray[n3] < d) {
            if (n2 - n3 == 1) {
                return n2;
            }
            return this.findInd(n3, n2, d);
        }
        if (n3 - n == 1) {
            return n3;
        }
        return this.findInd(n, n3, d);
    }

    double invHyperDist(double d) {
        if (d == 0.0) {
            return 0.0;
        }
        int n = this.inverseArray[199] < d ? 199 : this.findInd(0, 199, d);
        double d2 = this.inverseArray[n];
        double d3 = this.inverseArray[n - 1];
        double d4 = (d - d3) / (d2 - d3);
        return ((double)n + d4 - 1.0) / 200.0 * this.width;
    }

    class HyperLens
    extends TGAbstractLens {
        HyperLens() {
        }

        protected void applyLens(TGPoint2D tGPoint2D) {
            double d = Math.sqrt(tGPoint2D.x * tGPoint2D.x + tGPoint2D.y * tGPoint2D.y);
            if (d > 0.0) {
                tGPoint2D.x = tGPoint2D.x / d * HyperScroll.this.hyperDist(d);
                tGPoint2D.y = tGPoint2D.y / d * HyperScroll.this.hyperDist(d);
            } else {
                tGPoint2D.x = 0.0;
                tGPoint2D.y = 0.0;
            }
        }

        protected void undoLens(TGPoint2D tGPoint2D) {
            double d = Math.sqrt(tGPoint2D.x * tGPoint2D.x + tGPoint2D.y * tGPoint2D.y);
            if (d > 0.0) {
                tGPoint2D.x = tGPoint2D.x / d * HyperScroll.this.invHyperDist(d);
                tGPoint2D.y = tGPoint2D.y / d * HyperScroll.this.invHyperDist(d);
            } else {
                tGPoint2D.x = 0.0;
                tGPoint2D.y = 0.0;
            }
        }
    }

    private class hyperAdjustmentListener
    implements AdjustmentListener {
        private hyperAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            HyperScroll.this.updateInverseArray();
            HyperScroll.this.tgPanel.repaintAfterMove();
        }
    }

}

