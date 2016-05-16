/*
 * Decompiled with CFR 0_114.
 */
package org.jgrapht;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AsUndirectedGraph;

public abstract class Graphs {
    public static <V, E> E addEdge(Graph<V, E> g, V sourceVertex, V targetVertex, double weight) {
        EdgeFactory<V, E> ef = g.getEdgeFactory();
        E e = ef.createEdge(sourceVertex, targetVertex);
        assert (g instanceof WeightedGraph);
        ((WeightedGraph)g).setEdgeWeight(e, weight);
        return g.addEdge(sourceVertex, targetVertex, e) ? e : null;
    }

    public static <V, E> E addEdgeWithVertices(Graph<V, E> g, V sourceVertex, V targetVertex) {
        g.addVertex(sourceVertex);
        g.addVertex(targetVertex);
        return g.addEdge(sourceVertex, targetVertex);
    }

    public static <V, E> boolean addEdgeWithVertices(Graph<V, E> targetGraph, Graph<V, E> sourceGraph, E edge) {
        V sourceVertex = sourceGraph.getEdgeSource(edge);
        V targetVertex = sourceGraph.getEdgeTarget(edge);
        targetGraph.addVertex(sourceVertex);
        targetGraph.addVertex(targetVertex);
        return targetGraph.addEdge(sourceVertex, targetVertex, edge);
    }

    public static <V, E> E addEdgeWithVertices(Graph<V, E> g, V sourceVertex, V targetVertex, double weight) {
        g.addVertex(sourceVertex);
        g.addVertex(targetVertex);
        return Graphs.addEdge(g, sourceVertex, targetVertex, weight);
    }

    public static <V, E> boolean addGraph(Graph<? super V, ? super E> destination, Graph<V, E> source) {
        boolean modified = Graphs.addAllVertices(destination, source.vertexSet());
        return modified |= Graphs.addAllEdges(destination, source, source.edgeSet());
    }

    public static <V, E> void addGraphReversed(DirectedGraph<? super V, ? super E> destination, DirectedGraph<V, E> source) {
        Graphs.addAllVertices(destination, source.vertexSet());
        for (E edge : source.edgeSet()) {
            destination.addEdge(source.getEdgeTarget(edge), source.getEdgeSource(edge));
        }
    }

    public static <V, E> boolean addAllEdges(Graph<? super V, ? super E> destination, Graph<V, E> source, Collection<? extends E> edges) {
        boolean modified = false;
        for (E e : edges) {
            V s = source.getEdgeSource(e);
            V t = source.getEdgeTarget(e);
            destination.addVertex(s);
            destination.addVertex(t);
            modified |= destination.addEdge(s, t, e);
        }
        return modified;
    }

    public static <V, E> boolean addAllVertices(Graph<? super V, ? super E> destination, Collection<? extends V> vertices) {
        boolean modified = false;
        for (V v : vertices) {
            modified |= destination.addVertex(v);
        }
        return modified;
    }

    public static <V, E> List<V> neighborListOf(Graph<V, E> g, V vertex) {
        ArrayList<V> neighbors = new ArrayList<V>();
        for (E e : g.edgesOf(vertex)) {
            neighbors.add(Graphs.getOppositeVertex(g, e, vertex));
        }
        return neighbors;
    }

    public static <V, E> List<V> predecessorListOf(DirectedGraph<V, E> g, V vertex) {
        ArrayList<V> predecessors = new ArrayList<V>();
        Set<E> edges = g.incomingEdgesOf(vertex);
        for (E e : edges) {
            predecessors.add(Graphs.getOppositeVertex(g, e, vertex));
        }
        return predecessors;
    }

    public static <V, E> List<V> successorListOf(DirectedGraph<V, E> g, V vertex) {
        ArrayList<V> successors = new ArrayList<V>();
        Set<E> edges = g.outgoingEdgesOf(vertex);
        for (E e : edges) {
            successors.add(Graphs.getOppositeVertex(g, e, vertex));
        }
        return successors;
    }

    public static <V, E> UndirectedGraph<V, E> undirectedGraph(Graph<V, E> g) {
        if (g instanceof DirectedGraph) {
            return new AsUndirectedGraph((DirectedGraph)g);
        }
        if (g instanceof UndirectedGraph) {
            return (UndirectedGraph)g;
        }
        throw new IllegalArgumentException("Graph must be either DirectedGraph or UndirectedGraph");
    }

    public static <V, E> boolean testIncidence(Graph<V, E> g, E e, V v) {
        if (!g.getEdgeSource(e).equals(v) && !g.getEdgeTarget(e).equals(v)) {
            return false;
        }
        return true;
    }

    public static <V, E> V getOppositeVertex(Graph<V, E> g, E e, V v) {
        V source = g.getEdgeSource(e);
        V target = g.getEdgeTarget(e);
        if (v.equals(source)) {
            return target;
        }
        if (v.equals(target)) {
            return source;
        }
        throw new IllegalArgumentException("no such vertex");
    }

    public static <V, E> List<V> getPathVertexList(GraphPath<V, E> path) {
        Graph<V, E> g = path.getGraph();
        ArrayList<V> list = new ArrayList<V>();
        V v = path.getStartVertex();
        list.add(v);
        for (E e : path.getEdgeList()) {
            v = Graphs.getOppositeVertex(g, e, v);
            list.add(v);
        }
        return list;
    }
}

