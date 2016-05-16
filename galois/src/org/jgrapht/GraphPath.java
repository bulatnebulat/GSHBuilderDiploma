/*
 * Decompiled with CFR 0_114.
 */
package org.jgrapht;

import java.util.List;
import org.jgrapht.Graph;

public interface GraphPath<V, E> {
    public Graph<V, E> getGraph();

    public V getStartVertex();

    public V getEndVertex();

    public List<E> getEdgeList();

    public double getWeight();
}

