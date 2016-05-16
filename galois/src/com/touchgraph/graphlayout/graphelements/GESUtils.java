/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.graphelements.TGNodeQueue;
import java.util.Hashtable;
import java.util.Vector;

public class GESUtils {
    public static Hashtable calculateDistances(GraphEltSet graphEltSet, Node node, int n, int n2, int n3, boolean bl) {
        Hashtable<Node, Integer> hashtable = new Hashtable<Node, Integer>();
        hashtable.put(node, new Integer(0));
        TGNodeQueue tGNodeQueue = new TGNodeQueue();
        tGNodeQueue.push(node);
        while (!tGNodeQueue.isEmpty()) {
            Node node2 = tGNodeQueue.pop();
            int n4 = (Integer)hashtable.get(node2);
            if (n4 >= n) break;
            int n5 = 0;
            while (n5 < node2.edgeCount()) {
                Edge edge = node2.edgeAt(n5);
                if (node2 == node2.edgeAt(n5).getFrom() || !bl) {
                    Node node3 = edge.getOtherEndpt(node2);
                    if (graphEltSet.contains(edge) && !hashtable.containsKey(node3) && node3.edgeCount() <= n2) {
                        if (node3.edgeCount() <= n3) {
                            tGNodeQueue.push(node3);
                        }
                        hashtable.put(node3, new Integer(n4 + 1));
                    }
                }
                ++n5;
            }
        }
        return hashtable;
    }

    public static Hashtable calculateDistances(GraphEltSet graphEltSet, Node node, int n) {
        return GESUtils.calculateDistances(graphEltSet, node, n, 1000, 1000, false);
    }

    public static Hashtable getLargestConnectedSubgraph(GraphEltSet graphEltSet) {
        int n;
        int n2;
        int n3 = graphEltSet.nodeCount();
        if (n3 == 0) {
            return null;
        }
        Vector<Hashtable> vector = new Vector<Hashtable>();
        int n4 = 0;
        while (n4 < n3) {
            Node node = graphEltSet.nodeAt(n4);
            n = 0;
            n2 = 0;
            while (n2 < vector.size()) {
                if (((Hashtable)vector.elementAt(n2)).contains(node)) {
                    n = 1;
                }
                ++n2;
            }
            Hashtable hashtable = GESUtils.calculateDistances(graphEltSet, node, 1000);
            if (hashtable.size() > n3 / 2) {
                return hashtable;
            }
            if (n == 0) {
                vector.addElement(hashtable);
            }
            ++n4;
        }
        int n5 = 0;
        n = 0;
        n2 = 0;
        while (n2 < vector.size()) {
            if (((Hashtable)vector.elementAt(n2)).size() > n5) {
                n5 = ((Hashtable)vector.elementAt(n2)).size();
                n = n2;
            }
            ++n2;
        }
        return (Hashtable)vector.elementAt(n);
    }
}

