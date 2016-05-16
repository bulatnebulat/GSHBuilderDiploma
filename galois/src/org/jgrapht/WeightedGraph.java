/*
 * Decompiled with CFR 0_114.
 */
package org.jgrapht;

import org.jgrapht.Graph;

public interface WeightedGraph<V, E>
extends Graph<V, E> {
    public static final double DEFAULT_EDGE_WEIGHT = 1.0;

    public void setEdgeWeight(E var1, double var2);
}

