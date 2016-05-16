/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import java.awt.Graphics;
import java.util.EventListener;

public interface TGPaintListener
extends EventListener {
    public void paintFirst(Graphics var1);

    public void paintAfterEdges(Graphics var1);

    public void paintLast(Graphics var1);
}

