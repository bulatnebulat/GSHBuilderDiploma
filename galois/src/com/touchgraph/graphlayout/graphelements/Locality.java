/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import java.util.Hashtable;
import java.util.Vector;

public class Locality
extends GraphEltSet {
    protected GraphEltSet completeEltSet;

    public Locality(GraphEltSet graphEltSet) {
        this.completeEltSet = graphEltSet;
    }

    public GraphEltSet getCompleteEltSet() {
        return this.completeEltSet;
    }

    public synchronized void addNode(Node node) throws TGException {
        if (!this.contains(node)) {
            super.addNode(node);
            if (!this.completeEltSet.contains(node)) {
                this.completeEltSet.addNode(node);
            }
        }
    }

    public void addEdge(Edge edge) {
        if (!this.contains(edge)) {
            this.edges.addElement(edge);
            if (!this.completeEltSet.contains(edge)) {
                this.completeEltSet.addEdge(edge);
            }
        }
    }

    public synchronized void addNodeWithEdges(Node node) throws TGException {
        this.addNode(node);
        int n = 0;
        while (n < node.edgeCount()) {
            Edge edge = node.edgeAt(n);
            if (this.contains(edge.getOtherEndpt(node))) {
                this.addEdge(edge);
            }
            ++n;
        }
    }

    public synchronized void addAll() throws TGException {
        GraphEltSet graphEltSet = this.completeEltSet;
        synchronized (graphEltSet) {
            int n = 0;
            while (n < this.completeEltSet.nodeCount()) {
                this.addNode(this.completeEltSet.nodeAt(n));
                ++n;
            }
            int n2 = 0;
            while (n2 < this.completeEltSet.edgeCount()) {
                this.addEdge(this.completeEltSet.edgeAt(n2));
                ++n2;
            }
        }
    }

    public Edge findEdge(Node node, Node node2) {
        Edge edge = super.findEdge(node, node2);
        if (edge != null && this.edges.contains(edge)) {
            return edge;
        }
        return null;
    }

    public boolean deleteEdge(Edge edge) {
        if (edge == null) {
            return false;
        }
        this.removeEdge(edge);
        return this.completeEltSet.deleteEdge(edge);
    }

    public synchronized void deleteEdges(Vector vector) {
        this.removeEdges(vector);
        this.completeEltSet.deleteEdges(vector);
    }

    public boolean removeEdge(Edge edge) {
        if (edge == null) {
            return false;
        }
        if (this.edges.removeElement(edge)) {
            return true;
        }
        return false;
    }

    public synchronized void removeEdges(Vector vector) {
        int n = 0;
        while (n < vector.size()) {
            this.removeEdge((Edge)vector.elementAt(n));
            ++n;
        }
    }

    public boolean deleteNode(Node node) {
        if (node == null) {
            return false;
        }
        this.removeNode(node);
        return this.completeEltSet.deleteNode(node);
    }

    public synchronized void deleteNodes(Vector vector) {
        this.removeNodes(vector);
        this.completeEltSet.deleteNodes(vector);
    }

    public boolean removeNode(Node node) {
        if (node == null) {
            return false;
        }
        if (!this.nodes.removeElement(node)) {
            return false;
        }
        String string = node.getID();
        if (string != null) {
            this.nodeIDRegistry.remove(string);
        }
        int n = 0;
        while (n < node.edgeCount()) {
            this.removeEdge(node.edgeAt(n));
            ++n;
        }
        return true;
    }

    public synchronized void removeNodes(Vector vector) {
        int n = 0;
        while (n < vector.size()) {
            this.removeNode((Node)vector.elementAt(n));
            ++n;
        }
    }

    public synchronized void removeAll() {
        super.clearAll();
    }

    public synchronized void clearAll() {
        this.removeAll();
        this.completeEltSet.clearAll();
    }
}

