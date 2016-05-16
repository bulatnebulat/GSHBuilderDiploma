/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.TGForEachNodePair;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class GraphEltSet
implements ImmutableGraphEltSet {
    protected Vector nodes = new Vector();
    protected Vector edges = new Vector();
    protected Hashtable nodeIDRegistry = new Hashtable();

    protected Node nodeAt(int n) {
        if (this.nodes.size() == 0) {
            return null;
        }
        return (Node)this.nodes.elementAt(n);
    }

    public int nodeNum() {
        return this.nodes.size();
    }

    public int nodeCount() {
        return this.nodes.size();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public synchronized void addNode(Node node) throws TGException {
        String string = node.getID();
        if (string != null) {
            if (this.findNode(string) != null) throw new TGException(1, "node ID '" + string + "' already exists.");
            this.nodeIDRegistry.put(string, node);
            this.nodes.addElement(node);
            return;
        } else {
            String string2 = node.getLabel().trim();
            if (string2 == null) {
                string2 = "";
            }
            if (!string2.equals("") && this.findNode(node.getLabel()) == null) {
                string = string2;
            } else {
                int n = 1;
                while (this.findNode(string2 + "-" + n) != null) {
                    ++n;
                }
                string = string2 + "-" + n;
            }
            node.setID(string);
            this.nodeIDRegistry.put(string, node);
            this.nodes.addElement(node);
        }
    }

    public boolean contains(Node node) {
        return this.nodes.contains(node);
    }

    protected Edge edgeAt(int n) {
        if (this.edges.size() == 0) {
            return null;
        }
        return (Edge)this.edges.elementAt(n);
    }

    public int edgeNum() {
        return this.edges.size();
    }

    public int edgeCount() {
        return this.edges.size();
    }

    public void addEdge(Edge edge) {
        if (edge == null) {
            return;
        }
        if (!this.contains(edge)) {
            this.edges.addElement(edge);
            edge.from.addEdge(edge);
            edge.to.addEdge(edge);
        }
    }

    public Edge addEdge(Node node, Node node2, int n) {
        Edge edge = null;
        if (node != null && node2 != null) {
            edge = new Edge(node, node2, n);
            this.addEdge(edge);
        }
        return edge;
    }

    public boolean contains(Edge edge) {
        return this.edges.contains(edge);
    }

    public Node findNode(String string) {
        if (string == null) {
            return null;
        }
        return (Node)this.nodeIDRegistry.get(string);
    }

    public Node findNodeByURL(String string) {
        Node node = null;
        if (string == null) {
            return null;
        }
        Enumeration enumeration = this.nodeIDRegistry.elements();
        while (enumeration.hasMoreElements()) {
            Node node2 = (Node)enumeration.nextElement();
            if (!node2.getURL().equalsIgnoreCase(string)) continue;
            node = node2;
            break;
        }
        return node;
    }

    public Node findNodeLabelContaining(String string) {
        int n = 0;
        while (n < this.nodeCount()) {
            if (this.nodeAt(n) != null && this.nodeAt(n).getLabel().toLowerCase().equals(string.toLowerCase())) {
                return this.nodeAt(n);
            }
            ++n;
        }
        int n2 = 0;
        while (n2 < this.nodeCount()) {
            if (this.nodeAt(n2) != null && this.nodeAt(n2).getLabel().toLowerCase().indexOf(string.toLowerCase()) > -1) {
                return this.nodeAt(n2);
            }
            ++n2;
        }
        return null;
    }

    public Edge findEdge(Node node, Node node2) {
        int n = 0;
        while (n < node.edgeCount()) {
            Edge edge = node.edgeAt(n);
            if (edge.to == node2) {
                return edge;
            }
            ++n;
        }
        return null;
    }

    public boolean deleteEdge(Edge edge) {
        Vector vector = this.edges;
        synchronized (vector) {
            if (edge == null) {
                boolean bl = false;
                return bl;
            }
            if (!this.edges.removeElement(edge)) {
                boolean bl = false;
                return bl;
            }
            edge.from.removeEdge(edge);
            edge.to.removeEdge(edge);
            boolean bl = true;
            return bl;
        }
    }

    public void deleteEdges(Vector vector) {
        Vector vector2 = this.edges;
        synchronized (vector2) {
            int n = 0;
            while (n < vector.size()) {
                this.deleteEdge((Edge)vector.elementAt(n));
                ++n;
            }
        }
    }

    public boolean deleteEdge(Node node, Node node2) {
        Vector vector = this.edges;
        synchronized (vector) {
            Edge edge = this.findEdge(node, node2);
            if (edge != null) {
                boolean bl = this.deleteEdge(edge);
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    public boolean deleteNode(Node node) {
        Vector vector = this.nodes;
        synchronized (vector) {
            if (node == null) {
                boolean bl = false;
                return bl;
            }
            if (!this.nodes.removeElement(node)) {
                boolean bl = false;
                return bl;
            }
            String string = node.getID();
            if (string != null) {
                this.nodeIDRegistry.remove(string);
            }
            int n = 0;
            while (n < node.edgeCount()) {
                Edge edge = node.edgeAt(n);
                if (edge.from == node) {
                    this.edges.removeElement(edge);
                    edge.to.removeEdge(edge);
                } else if (edge.to == node) {
                    this.edges.removeElement(edge);
                    edge.from.removeEdge(edge);
                }
                ++n;
            }
        }
        return true;
    }

    public void deleteNodes(Vector vector) {
        Vector vector2 = this.nodes;
        synchronized (vector2) {
            int n = 0;
            while (n < vector.size()) {
                this.deleteNode((Node)vector.elementAt(n));
                ++n;
            }
        }
    }

    public Node getRandomNode() {
        if (this.nodes.size() == 0) {
            return null;
        }
        int n = (int)(Math.random() * (double)this.nodeCount());
        return this.nodeAt(n);
    }

    public Node getFirstNode() {
        if (this.nodes.size() == 0) {
            return null;
        }
        return this.nodeAt(0);
    }

    public void clearAll() {
        Vector vector = this.nodes;
        synchronized (vector) {
            Vector vector2 = this.edges;
            synchronized (vector2) {
                this.nodes.removeAllElements();
                this.edges.removeAllElements();
                this.nodeIDRegistry.clear();
            }
        }
    }

    public void forAllNodes(TGForEachNode tGForEachNode) {
        Vector vector = this.nodes;
        synchronized (vector) {
            int n = 0;
            while (n < this.nodeCount()) {
                Node node = this.nodeAt(n);
                tGForEachNode.forEachNode(node);
                ++n;
            }
        }
    }

    public void forAllNodePairs(TGForEachNodePair tGForEachNodePair) {
        Vector vector = this.nodes;
        synchronized (vector) {
            int n = 0;
            while (n < this.nodeCount()) {
                Node node = this.nodeAt(n);
                tGForEachNodePair.beforeInnerLoop(node);
                int n2 = n + 1;
                while (n2 < this.nodeCount()) {
                    tGForEachNodePair.forEachNodePair(node, this.nodeAt(n2));
                    ++n2;
                }
                tGForEachNodePair.afterInnerLoop(node);
                ++n;
            }
        }
    }

    public void forAllEdges(TGForEachEdge tGForEachEdge) {
        Vector vector = this.edges;
        synchronized (vector) {
            int n = 0;
            while (n < this.edgeCount()) {
                Edge edge = this.edgeAt(n);
                tGForEachEdge.forEachEdge(edge);
                ++n;
            }
        }
    }
}

