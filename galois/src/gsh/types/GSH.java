/*
 * Decompiled with CFR 0_114.
 */
package gsh.types;

import gsh.types.Concept;
import gsh.types.Edge;
import java.util.ArrayList;
import java.util.List;

public class GSH {
    private final List<Concept> concepts;
    private final List<Edge> edges = new ArrayList<Edge>();

    public GSH(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public GSH() {
        this.concepts = new ArrayList<Concept>();
    }

    public List<Concept> getConcepts() {
        return this.concepts;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public boolean containsEdge(Edge edge) {
        for (Edge p : this.edges) {
            if (!p.getChild().toString().equals(edge.getChild().toString()) || !p.getParent().toString().equals(edge.getParent().toString())) continue;
            return true;
        }
        return false;
    }
}

