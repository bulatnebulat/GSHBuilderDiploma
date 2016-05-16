/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Node;
import java.util.Vector;

public class TGNodeQueue {
    Vector queue = new Vector();

    public void push(Node node) {
        this.queue.addElement(node);
    }

    public Node pop() {
        Node node = (Node)this.queue.elementAt(0);
        this.queue.removeElementAt(0);
        return node;
    }

    public boolean isEmpty() {
        return this.queue.size() == 0;
    }

    public boolean contains(Node node) {
        return this.queue.contains(node);
    }
}

