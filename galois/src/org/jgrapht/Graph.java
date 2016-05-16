/*
 * Decompiled with CFR 0_114.
 */
package org.jgrapht;

import java.util.Collection;
import java.util.Set;
import org.jgrapht.EdgeFactory;

public interface Graph<V, E> {
    public Set<E> getAllEdges(V var1, V var2);

    public E getEdge(V var1, V var2);

    public EdgeFactory<V, E> getEdgeFactory();

    public E addEdge(V var1, V var2);

    public boolean addEdge(V var1, V var2, E var3);

    public boolean addVertex(V var1);

    public boolean containsEdge(V var1, V var2);

    public boolean containsEdge(E var1);

    public boolean containsVertex(V var1);

    public Set<E> edgeSet();

    public Set<E> edgesOf(V var1);

    public boolean removeAllEdges(Collection<? extends E> var1);

    public Set<E> removeAllEdges(V var1, V var2);

    public boolean removeAllVertices(Collection<? extends V> var1);

    public E removeEdge(V var1, V var2);

    public boolean removeEdge(E var1);

    public boolean removeVertex(V var1);

    public Set<V> vertexSet();

    public V getEdgeSource(E var1);

    public V getEdgeTarget(E var1);

    public double getEdgeWeight(E var1);
}

