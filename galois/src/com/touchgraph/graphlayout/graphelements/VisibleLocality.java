/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.graphelements.Locality;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;

public class VisibleLocality
extends Locality {
    public VisibleLocality(GraphEltSet graphEltSet) {
        super(graphEltSet);
    }

    public synchronized void addNode(Node node) throws TGException {
        super.addNode(node);
        node.setVisible(true);
    }

    public void addEdge(Edge edge) {
        if (!this.contains(edge)) {
            super.addEdge(edge);
            ++edge.from.visibleEdgeCnt;
            ++edge.to.visibleEdgeCnt;
        }
    }

    public boolean removeEdge(Edge edge) {
        boolean bl = super.removeEdge(edge);
        if (bl) {
            edge.setVisible(false);
            --edge.from.visibleEdgeCnt;
            --edge.to.visibleEdgeCnt;
        }
        return bl;
    }

    public boolean removeNode(Node node) {
        boolean bl = super.removeNode(node);
        if (bl) {
            node.setVisible(false);
        }
        return bl;
    }

    public synchronized void removeAll() {
        int n = 0;
        while (n < this.nodeCount()) {
            this.nodeAt(n).setVisible(false);
            ++n;
        }
        int n2 = 0;
        while (n2 < this.edgeCount()) {
            this.edgeAt(n2).setVisible(false);
            ++n2;
        }
        super.removeAll();
    }

    public void updateLocalityFromVisibility() throws TGException {
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                try {
                    if (node.isVisible() && !VisibleLocality.this.contains(node)) {
                        VisibleLocality.this.addNode(node);
                    } else if (!node.isVisible() && VisibleLocality.this.contains(node)) {
                        VisibleLocality.this.removeNode(node);
                    }
                }
                catch (TGException var2_2) {
                    var2_2.printStackTrace();
                }
            }
        };
        this.completeEltSet.forAllNodes(tGForEachNode);
        TGForEachEdge tGForEachEdge = new TGForEachEdge(){

            public void forEachEdge(Edge edge) {
                if (edge.isVisible() && !VisibleLocality.this.contains(edge)) {
                    VisibleLocality.this.addEdge(edge);
                } else if (!edge.isVisible() && VisibleLocality.this.contains(edge)) {
                    VisibleLocality.this.removeEdge(edge);
                }
            }
        };
        this.completeEltSet.forAllEdges(tGForEachEdge);
    }

}

