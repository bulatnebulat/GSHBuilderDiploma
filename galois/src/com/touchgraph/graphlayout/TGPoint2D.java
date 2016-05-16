/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

public class TGPoint2D {
    public double x;
    public double y;

    public TGPoint2D(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public TGPoint2D(TGPoint2D tGPoint2D) {
        this.x = tGPoint2D.x;
        this.y = tGPoint2D.y;
    }

    public void setLocation(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public void setX(double d) {
        this.x = d;
    }

    public void setY(double d) {
        this.y = d;
    }
}

