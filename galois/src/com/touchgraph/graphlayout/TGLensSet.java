/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGPoint2D;
import java.util.Vector;

public class TGLensSet {
    Vector lenses = new Vector();

    public void addLens(TGAbstractLens tGAbstractLens) {
        this.lenses.addElement(tGAbstractLens);
    }

    public void applyLens(TGPoint2D tGPoint2D) {
        if (this.lenses.isEmpty()) {
            return;
        }
        int n = 0;
        while (n < this.lenses.size()) {
            ((TGAbstractLens)this.lenses.elementAt(n)).applyLens(tGPoint2D);
            ++n;
        }
    }

    public void undoLens(TGPoint2D tGPoint2D) {
        if (this.lenses.isEmpty()) {
            return;
        }
        int n = this.lenses.size() - 1;
        while (n >= 0) {
            ((TGAbstractLens)this.lenses.elementAt(n)).undoLens(tGPoint2D);
            --n;
        }
    }

    public TGPoint2D convRealToDraw(TGPoint2D tGPoint2D) {
        TGPoint2D tGPoint2D2 = new TGPoint2D(tGPoint2D);
        this.applyLens(tGPoint2D2);
        return tGPoint2D2;
    }

    public TGPoint2D convRealToDraw(double d, double d2) {
        TGPoint2D tGPoint2D = new TGPoint2D(d, d2);
        this.applyLens(tGPoint2D);
        return tGPoint2D;
    }

    public TGPoint2D convDrawToReal(TGPoint2D tGPoint2D) {
        TGPoint2D tGPoint2D2 = new TGPoint2D(tGPoint2D);
        this.undoLens(tGPoint2D2);
        return tGPoint2D2;
    }

    public TGPoint2D convDrawToReal(double d, double d2) {
        TGPoint2D tGPoint2D = new TGPoint2D(d, d2);
        this.undoLens(tGPoint2D);
        return tGPoint2D;
    }
}

