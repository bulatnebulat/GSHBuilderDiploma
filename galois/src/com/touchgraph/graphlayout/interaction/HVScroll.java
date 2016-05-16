/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.TGPoint2D;
import com.touchgraph.graphlayout.interaction.TGAbstractClickUI;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;

public class HVScroll
implements GraphListener {
    private DScrollbar horizontalSB;
    private DScrollbar verticalSB;
    HVLens hvLens;
    HVDragUI hvDragUI;
    HVScrollToCenterUI hvScrollToCenterUI;
    public boolean scrolling;
    private boolean adjustmentIsInternal;
    private TGPanel tgPanel;
    private TGLensSet tgLensSet;
    TGPoint2D offset;
    Thread noRepaintThread;
    Thread scrollThread;

    public HVScroll(TGPanel tGPanel, TGLensSet tGLensSet) {
        this.tgPanel = tGPanel;
        this.tgLensSet = tGLensSet;
        this.offset = new TGPoint2D(0.0, 0.0);
        this.scrolling = false;
        this.adjustmentIsInternal = false;
        this.horizontalSB = new DScrollbar(0, 0, 100, -1000, 1100);
        this.horizontalSB.setBlockIncrement(100);
        this.horizontalSB.setUnitIncrement(20);
        this.horizontalSB.addAdjustmentListener(new horizAdjustmentListener());
        this.verticalSB = new DScrollbar(1, 0, 100, -1000, 1100);
        this.verticalSB.setBlockIncrement(100);
        this.verticalSB.setUnitIncrement(20);
        this.verticalSB.addAdjustmentListener(new vertAdjustmentListener());
        this.hvLens = new HVLens();
        this.hvDragUI = new HVDragUI();
        this.hvScrollToCenterUI = new HVScrollToCenterUI();
        this.tgPanel.addGraphListener(this);
    }

    public Scrollbar getHorizontalSB() {
        return this.horizontalSB;
    }

    public Scrollbar getVerticalSB() {
        return this.verticalSB;
    }

    public HVDragUI getHVDragUI() {
        return this.hvDragUI;
    }

    public HVLens getLens() {
        return this.hvLens;
    }

    public TGAbstractClickUI getHVScrollToCenterUI() {
        return this.hvScrollToCenterUI;
    }

    public TGPoint2D getTopLeftDraw() {
        TGPoint2D tGPoint2D = this.tgPanel.getTopLeftDraw();
        tGPoint2D.setLocation(tGPoint2D.x - (double)(this.tgPanel.getSize().width / 4), tGPoint2D.y - (double)(this.tgPanel.getSize().height / 4));
        return tGPoint2D;
    }

    public TGPoint2D getBottomRightDraw() {
        TGPoint2D tGPoint2D = this.tgPanel.getBottomRightDraw();
        tGPoint2D.setLocation(tGPoint2D.x + (double)(this.tgPanel.getSize().width / 4), tGPoint2D.y + (double)(this.tgPanel.getSize().height / 4));
        return tGPoint2D;
    }

    public TGPoint2D getDrawCenter() {
        return new TGPoint2D(this.tgPanel.getSize().width / 2, this.tgPanel.getSize().height / 2);
    }

    public void graphMoved() {
        if (this.tgPanel.getDragNode() == null && this.tgPanel.getSize().height > 0) {
            TGPoint2D tGPoint2D = this.getDrawCenter();
            TGPoint2D tGPoint2D2 = this.getTopLeftDraw();
            TGPoint2D tGPoint2D3 = this.getBottomRightDraw();
            double d = (- tGPoint2D2.x - tGPoint2D.x) / (tGPoint2D3.x - tGPoint2D2.x) * 2000.0 - 1000.0;
            double d2 = (- tGPoint2D2.y - tGPoint2D.y) / (tGPoint2D3.y - tGPoint2D2.y) * 2000.0 - 1000.0;
            boolean bl = true;
            if (d < (double)this.horizontalSB.getMaximum() && d > (double)this.horizontalSB.getMinimum() && d2 < (double)this.verticalSB.getMaximum() && d2 > (double)this.verticalSB.getMinimum()) {
                bl = false;
            }
            this.adjustmentIsInternal = true;
            this.horizontalSB.setDValue(d);
            this.verticalSB.setDValue(d2);
            this.adjustmentIsInternal = false;
            if (bl) {
                this.adjustHOffset();
                this.adjustVOffset();
                this.tgPanel.repaint();
            }
        }
        if (this.noRepaintThread != null && this.noRepaintThread.isAlive()) {
            this.noRepaintThread.interrupt();
        }
        this.noRepaintThread = new Thread(){

            public void run() {
                try {
                    Thread.currentThread();
                    Thread.sleep(40);
                }
                catch (InterruptedException var1_1) {
                    // empty catch block
                }
            }
        };
        this.noRepaintThread.start();
    }

    public void graphReset() {
        this.horizontalSB.setDValue(0.0);
        this.verticalSB.setDValue(0.0);
        this.adjustHOffset();
        this.adjustVOffset();
    }

    private void adjustHOffset() {
        int n = 0;
        while (n < 3) {
            TGPoint2D tGPoint2D = this.tgPanel.getCenter();
            TGPoint2D tGPoint2D2 = this.getTopLeftDraw();
            TGPoint2D tGPoint2D3 = this.getBottomRightDraw();
            double d = (this.horizontalSB.getDValue() + 1000.0) / 2000.0 * (tGPoint2D3.x - tGPoint2D2.x) + tGPoint2D2.x;
            double d2 = this.tgPanel.getSize().height / 2;
            TGPoint2D tGPoint2D4 = this.tgLensSet.convDrawToReal(d, d2);
            this.offset.setX(this.offset.x + (tGPoint2D4.x - tGPoint2D.x));
            this.offset.setY(this.offset.y + (tGPoint2D4.y - tGPoint2D.y));
            this.tgPanel.processGraphMove();
            ++n;
        }
    }

    private void adjustVOffset() {
        int n = 0;
        while (n < 10) {
            TGPoint2D tGPoint2D = this.tgPanel.getCenter();
            TGPoint2D tGPoint2D2 = this.getTopLeftDraw();
            TGPoint2D tGPoint2D3 = this.getBottomRightDraw();
            double d = this.tgPanel.getSize().width / 2;
            double d2 = (this.verticalSB.getDValue() + 1000.0) / 2000.0 * (tGPoint2D3.y - tGPoint2D2.y) + tGPoint2D2.y;
            TGPoint2D tGPoint2D4 = this.tgLensSet.convDrawToReal(d, d2);
            this.offset.setX(this.offset.x + (tGPoint2D4.x - tGPoint2D.x));
            this.offset.setY(this.offset.y + (tGPoint2D4.y - tGPoint2D.y));
            this.tgPanel.processGraphMove();
            ++n;
        }
    }

    public void setOffset(Point point) {
        this.offset.setLocation(point.x, point.y);
        this.tgPanel.processGraphMove();
        this.graphMoved();
    }

    public Point getOffset() {
        return new Point((int)this.offset.x, (int)this.offset.y);
    }

    public void scrollAtoB(TGPoint2D tGPoint2D, TGPoint2D tGPoint2D2) {
        TGPoint2D tGPoint2D3 = this.tgLensSet.convDrawToReal(tGPoint2D);
        TGPoint2D tGPoint2D4 = this.tgLensSet.convDrawToReal(tGPoint2D2);
        this.offset.setX(this.offset.x + (tGPoint2D3.x - tGPoint2D4.x));
        this.offset.setY(this.offset.y + (tGPoint2D3.y - tGPoint2D4.y));
    }

    public void slowScrollToCenter(final Node node) {
        TGPoint2D tGPoint2D = new TGPoint2D(node.drawx, node.drawy);
        TGPoint2D tGPoint2D2 = this.getDrawCenter();
        this.scrolling = true;
        if (this.scrollThread != null && this.scrollThread.isAlive()) {
            this.scrollThread.interrupt();
        }
        this.scrollThread = new Thread(){

            public void run() {
                double d = -999.0;
                double d2 = -999.0;
                boolean bl = true;
                boolean bl2 = false;
                int n = 0;
                while (bl && n++ < 250) {
                    double d3;
                    double d4;
                    d = node.drawx;
                    double d5 = HVScroll.this.getDrawCenter().x;
                    d2 = node.drawy;
                    double d6 = HVScroll.this.getDrawCenter().y;
                    double d7 = Math.sqrt((d - d5) * (d - d5) + (d2 - d6) * (d2 - d6));
                    if (d7 > 5.0) {
                        d3 = d5 + (d - d5) * ((d7 - 5.0) / d7);
                        d4 = d6 + (d2 - d6) * ((d7 - 5.0) / d7);
                    } else {
                        d3 = d5;
                        d4 = d6;
                    }
                    HVScroll.this.scrollAtoB(new TGPoint2D(d, d2), new TGPoint2D(d3, d4));
                    if (HVScroll.this.noRepaintThread == null || !HVScroll.this.noRepaintThread.isAlive()) {
                        HVScroll.this.tgPanel.repaintAfterMove();
                    } else {
                        HVScroll.this.tgPanel.processGraphMove();
                    }
                    try {
                        Thread.currentThread();
                        Thread.sleep(20);
                    }
                    catch (InterruptedException var18_8) {
                        bl = false;
                    }
                    if (d7 >= 3.0) continue;
                    try {
                        Thread.currentThread();
                        Thread.sleep(200);
                    }
                    catch (InterruptedException var18_9) {
                        bl = false;
                    }
                    d = node.drawx;
                    d2 = node.drawy;
                    d5 = HVScroll.this.getDrawCenter().x;
                    d6 = HVScroll.this.getDrawCenter().y;
                    d7 = Math.sqrt((d - d5) * (d - d5) + (d2 - d6) * (d2 - d6));
                    if (d7 >= 3.0) continue;
                    bl = false;
                }
                HVScroll.this.scrollAtoB(new TGPoint2D(node.drawx, node.drawy), HVScroll.this.getDrawCenter());
                HVScroll.this.tgPanel.repaintAfterMove();
                HVScroll.this.scrolling = false;
            }
        };
        this.scrollThread.start();
    }

    class HVDragUI
    extends TGAbstractDragUI {
        TGPoint2D lastMousePos;

        HVDragUI() {
            super(HVScroll.this.tgPanel);
        }

        public void preActivate() {
        }

        public void preDeactivate() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            this.lastMousePos = new TGPoint2D(mouseEvent.getX(), mouseEvent.getY());
        }

        public void mouseReleased(MouseEvent mouseEvent) {
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            if (!HVScroll.this.scrolling) {
                HVScroll.this.scrollAtoB(this.lastMousePos, new TGPoint2D(mouseEvent.getX(), mouseEvent.getY()));
            }
            this.lastMousePos.setLocation(mouseEvent.getX(), mouseEvent.getY());
            this.tgPanel.repaintAfterMove();
        }
    }

    class HVScrollToCenterUI
    extends TGAbstractClickUI {
        HVScrollToCenterUI() {
        }

        public void mouseClicked(MouseEvent mouseEvent) {
        }
    }

    class HVLens
    extends TGAbstractLens {
        HVLens() {
        }

        protected void applyLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x -= HVScroll.this.offset.x;
            tGPoint2D.y -= HVScroll.this.offset.y;
        }

        protected void undoLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x += HVScroll.this.offset.x;
            tGPoint2D.y += HVScroll.this.offset.y;
        }
    }

    private class vertAdjustmentListener
    implements AdjustmentListener {
        private vertAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            if (!HVScroll.this.adjustmentIsInternal) {
                HVScroll.this.adjustVOffset();
                HVScroll.this.tgPanel.repaintAfterMove();
            }
        }
    }

    private class horizAdjustmentListener
    implements AdjustmentListener {
        private horizAdjustmentListener() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            if (!HVScroll.this.adjustmentIsInternal) {
                HVScroll.this.adjustHOffset();
                HVScroll.this.tgPanel.repaintAfterMove();
            }
        }
    }

    class DScrollbar
    extends Scrollbar {
        private double doubleValue;

        DScrollbar(int n, int n2, int n3, int n4, int n5) {
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

}

