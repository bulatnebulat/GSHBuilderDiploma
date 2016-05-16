/*
 * Decompiled with CFR 0_114.
 */
package gsh.types;

import gsh.types.Concept;
import gsh.types.Edge;
import gsh.types.GSH;
import java.util.List;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;



public class ToGraph {
    public static ListenableGraph<String, DefaultEdge> fromGSH(GSH gsh) {
        ListenableUndirectedGraph<String, DefaultEdge> result = new ListenableUndirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        for (Concept p2 : gsh.getConcepts()) {
            result.addVertex(p2.toString());
        }
        for (Edge p : gsh.getEdges()) {
            result.addEdge(p.getChild().toString(), p.getParent().toString());
        }
        return result;
    }
}

