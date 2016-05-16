/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.TGForEachNodePair;

public interface ImmutableGraphEltSet {
    public int nodeCount();

    public int nodeNum();

    public int edgeCount();

    public int edgeNum();

    public Node findNode(String var1);

    public Node findNodeLabelContaining(String var1);

    public Edge findEdge(Node var1, Node var2);

    public Node getRandomNode();

    public Node getFirstNode();

    public void forAllNodes(TGForEachNode var1);

    public void forAllNodePairs(TGForEachNodePair var1);

    public void forAllEdges(TGForEachEdge var1);
}

