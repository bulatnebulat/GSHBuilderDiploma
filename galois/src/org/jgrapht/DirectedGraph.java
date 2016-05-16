/*
 * Decompiled with CFR 0_114.
 */
package org.jgrapht;

import java.util.Set;
import org.jgrapht.Graph;

public interface DirectedGraph<V, E>
extends Graph<V, E> {
    public int inDegreeOf(V var1);

    public Set<E> incomingEdgesOf(V var1);

    public int outDegreeOf(V var1);

    public Set<E> outgoingEdgesOf(V var1);
}

