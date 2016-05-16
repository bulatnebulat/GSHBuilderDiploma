/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.TGPoint2D;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;

public class RotateScroll
implements GraphListener {
    RotateLens rotateLens;
    double rotateAngle;
    RotateDragUI rotateDragUI;
    private DScrollBar rotateSB;
    boolean adjustmentIsInternal;
    private TGPanel tgPanel;

    public RotateScroll(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.rotateAngle = 0.0;
        this.rotateLens = new RotateLens();
        this.rotateDragUI = new RotateDragUI();
        this.rotateSB = new DScrollBar(0, 0, 80, -314, 318);
        this.rotateSB.addAdjustmentListener(new rotateAdjustmentListener());
        this.adjustmentIsInternal = false;
        this.tgPanel.addGraphListener(this);
    }

    public RotateLens getLens() {
        return this.rotateLens;
    }

    public Scrollbar getRotateSB() {
        return this.rotateSB;
    }

    public RotateDragUI getRotateDragUI() {
        return this.rotateDragUI;
    }

    public int getRotationAngle() {
        double d = this.rotateSB.getValue() - this.rotateSB.getMinimum();
        double d2 = this.rotateSB.getMaximum() - this.rotateSB.getMinimum() - this.rotateSB.getVisibleAmount();
        return (int)(d / d2 * 359.0);
    }

    public void setRotationAngle(int n) {
        double d = this.rotateSB.getMaximum() - this.rotateSB.getMinimum() - this.rotateSB.getVisibleAmount();
        this.rotateSB.setValue((int)((double)n / 359.0 * d + 0.5) + this.rotateSB.getMinimum());
    }

    public void graphMoved() {
    }

    public void graphReset() {
        this.rotateAngle = 0.0;
        this.rotateSB.setValue(0);
    }

    double computeAngle(double d, double d2) {
        double d3 = Math.atan(d2 / d);
        if (d == 0.0) {
            d3 = d2 > 0.0 ? 1.5707963267948966 : -1.5707963267948966;
        }
        if (d < 0.0) {
            d3 += 3.141592653589793;
        }
        return d3;
    }

    public void incrementRotateAngle(double d) {
        this.rotateAngle += d;
        if (this.rotateAngle > 3.141592653589793) {
            this.rotateAngle -= 6.283185307179586;
        }
        if (this.rotateAngle < -3.141592653589793) {
            this.rotateAngle += 6.283185307179586;
        }
        this.adjustmentIsInternal = true;
        this.rotateSB.setDValue(this.rotateAngle * 100.0);
        this.adjustmentIsInternal = false;
    }

    class RotateDragUI
    extends TGAbstractDragUI {
        double lastAngle;

        RotateDragUI() {
            super(RotateScroll.this.tgPanel);
        }

        double getMouseAngle(double d, double d2) {
            return RotateScroll.this.computeAngle(d - this.tgPanel.getDrawCenter().x, d2 - this.tgPanel.getDrawCenter().y);
        }

        public void preActivate() {
        }

        public void preDeactivate() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            this.lastAngle = this.getMouseAngle(mouseEvent.getX(), mouseEvent.getY());
        }

        public void mouseReleased(MouseEvent mouseEvent) {
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            double d = this.getMouseAngle(mouseEvent.getX(), mouseEvent.getY());
            RotateScroll.this.incrementRotateAngle(d - this.lastAngle);
            this.lastAngle = d;
            this.tgPanel.repaintAfterMove();
        }
    }

    class RotateLens
    extends TGAbstractLens {
        RotateLens() {
        }

        protected void applyLens(TGPoint2D tGPoint2D) {
            double d = RotateScroll.this.computeAngle(tGPoint2D.x, tGPoint2D.y);
            double d2 = Math.sqrt(tGPoint2D.x * tGPoint2D.x + tGPoint2D.y * tGPoint2D.y);
            tGPoint2D.x = d2 * Math.cos(d + RotateScroll.this.rotateAngle);
            tGPoint2D.y = d2 * Math.sin(d + RotateScroll.this.rotateAngle);
        }

        protected void undoLens(TGPoint2D tGPoint2D) {
            double d = RotateScroll.this.computeAngle(tGPoint2D.x, tGPoint2D.y);
            double d2 = Math.sqrt(tGPoint2D.x * tGPoint2D.x + tGPoint2D.y * tGPoint2D.y);
            tGPoint2D.x = d2 * Math.cos(d - RotateScroll.this.rotateAngle);
            tGPoint2D.y = d2 * Math.sin(d - RotateScroll.this.rotateAngle);
        }
    }

    class DScrollBar
    extends Scrollbar {
        private double doubleValue;

        DScrollBar(int n, int n2, int n3, int n4, int n5) {
            super(n, n2, n3, n4, n5);
            this.doubleValue = n2;
        }

        public void setValue(int n) {
            this.doubleValue = n;
            super.setValue(n);
        }

        public void setIValue(int n) {
            super.setValue(n);
        }

        public void setDValue(double d) {
            this.doubleValue = Math.max((double)this.getMinimum(), Math.min((double)this.getMaximum(), d));
            this.setIValue((int)d);
        }

        public double getDValue() {
            return this.doubleValue;
        }
    }

    private class rotateAdjustmentListener
    implements AdjustmentListener {
        private rotateAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            if (!RotateScroll.this.adjustmentIsInternal) {
                RotateScroll.this.rotateAngle = RotateScroll.this.rotateSB.getDValue() / 100.0;
                RotateScroll.this.tgPanel.repaintAfterMove();
            }
        }
    }

}

