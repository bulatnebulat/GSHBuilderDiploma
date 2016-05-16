/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.GESUtils;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.graphelements.Locality;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class LocalityUtils {
    TGPanel tgPanel;
    Locality locality;
    public static final int INFINITE_LOCALITY_RADIUS = Integer.MAX_VALUE;
    ShiftLocaleThread shiftLocaleThread;
    boolean fastFinishShift = false;

    public LocalityUtils(Locality locality, TGPanel tGPanel) {
        this.locality = locality;
        this.tgPanel = tGPanel;
    }

    public void fastFinishAnimation() {
        this.fastFinishShift = true;
    }

    private synchronized boolean markDistantNodes(final Hashtable hashtable) {
        final boolean[] arrbl = new boolean[]{false};
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                if (!hashtable.containsKey(node)) {
                    node.markedForRemoval = true;
                    arrbl[0] = true;
                }
            }
        };
        this.locality.forAllNodes(tGForEachNode);
        return arrbl[0];
    }

    private synchronized void removeMarkedNodes() {
        final Vector vector = new Vector();
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                if (node.markedForRemoval) {
                    vector.addElement(node);
                    node.markedForRemoval = false;
                    node.massfade = 1.0;
                }
            }
        };
        Locality locality = this.locality;
        synchronized (locality) {
            this.locality.forAllNodes(tGForEachNode);
            this.locality.removeNodes(vector);
        }
    }

    private synchronized void addNearNodes(Hashtable hashtable, int n) throws TGException {
        int n2 = 0;
        while (n2 < n + 1) {
            Enumeration enumeration = hashtable.keys();
            while (enumeration.hasMoreElements()) {
                Node node = (Node)enumeration.nextElement();
                if (this.locality.contains(node) || (Integer)hashtable.get(node) > n2) continue;
                node.massfade = 1.0;
                node.justMadeLocal = true;
                this.locality.addNodeWithEdges(node);
                if (this.fastFinishShift) continue;
                try {
                    if (n == 1) {
                        Thread.currentThread();
                        Thread.sleep(50);
                        continue;
                    }
                    Thread.currentThread();
                    Thread.sleep(50);
                    continue;
                }
                catch (InterruptedException var6_6) {
                    // empty catch block
                }
            }
            ++n2;
        }
    }

    private synchronized void unmarkNewAdditions() {
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                node.justMadeLocal = false;
            }
        };
        this.locality.forAllNodes(tGForEachNode);
    }

    public void setLocale(Node node, int n, int n2, int n3, boolean bl) throws TGException {
        if (node == null || n < 0) {
            return;
        }
        if (this.shiftLocaleThread != null && this.shiftLocaleThread.isAlive()) {
            this.fastFinishShift = true;
            while (this.shiftLocaleThread.isAlive()) {
                try {
                    Thread.currentThread();
                    Thread.sleep(100);
                    continue;
                }
                catch (InterruptedException var6_6) {
                    // empty catch block
                }
            }
        }
        if (n == Integer.MAX_VALUE || node == null) {
            this.addAllGraphElts();
            this.tgPanel.resetDamper();
            return;
        }
        this.fastFinishShift = false;
        this.shiftLocaleThread = new ShiftLocaleThread(node, n, n2, n3, bl);
    }

    public void setLocale(Node node, int n) throws TGException {
        this.setLocale(node, n, 1000, 1000, false);
    }

    public synchronized void addAllGraphElts() throws TGException {
        this.locality.addAll();
    }

    public void expandNode(final Node node) {
        new Thread(){

            public void run() {
                LocalityUtils localityUtils = LocalityUtils.this;
                synchronized (localityUtils) {
                    if (!LocalityUtils.this.locality.getCompleteEltSet().contains(node)) {
                        return;
                    }
                    LocalityUtils.this.tgPanel.stopDamper();
                    int n = 0;
                    while (n < node.edgeCount()) {
                        Node node2 = node.edgeAt(n).getOtherEndpt(node);
                        if (!LocalityUtils.this.locality.contains(node2)) {
                            node2.justMadeLocal = true;
                            try {
                                LocalityUtils.this.locality.addNodeWithEdges(node2);
                                Thread.currentThread();
                                Thread.sleep(50);
                            }
                            catch (TGException var4_5) {
                                System.err.println("TGException: " + var4_5.getMessage());
                            }
                            catch (InterruptedException var5_6) {}
                        } else if (!LocalityUtils.this.locality.contains(node.edgeAt(n))) {
                            LocalityUtils.this.locality.addEdge(node.edgeAt(n));
                        }
                        ++n;
                    }
                    try {
                        Thread.currentThread();
                        Thread.sleep(200);
                    }
                    catch (InterruptedException var3_4) {
                        // empty catch block
                    }
                    LocalityUtils.this.unmarkNewAdditions();
                    LocalityUtils.this.tgPanel.resetDamper();
                }
            }
        }.start();
    }

    public synchronized void hideNode(final Node node) {
        if (node == null) {
            return;
        }
        new Thread(){

            public void run() {
                LocalityUtils localityUtils = LocalityUtils.this;
                synchronized (localityUtils) {
                    if (!LocalityUtils.this.locality.getCompleteEltSet().contains(node)) {
                        return;
                    }
                    LocalityUtils.this.locality.removeNode(node);
                    if (node == LocalityUtils.this.tgPanel.getSelect()) {
                        LocalityUtils.this.tgPanel.clearSelect();
                    }
                    Hashtable hashtable = GESUtils.getLargestConnectedSubgraph(LocalityUtils.this.locality);
                    LocalityUtils.this.markDistantNodes(hashtable);
                    LocalityUtils.this.tgPanel.repaint();
                    try {
                        Thread.currentThread();
                        Thread.sleep(200);
                    }
                    catch (InterruptedException var3_3) {
                        // empty catch block
                    }
                    LocalityUtils.this.removeMarkedNodes();
                    LocalityUtils.this.tgPanel.resetDamper();
                }
            }
        }.start();
    }

    public synchronized void collapseNode(final Node node) {
        if (node == null) {
            return;
        }
        new Thread(){

            public void run() {
                LocalityUtils localityUtils = LocalityUtils.this;
                synchronized (localityUtils) {
                    if (!LocalityUtils.this.locality.getCompleteEltSet().contains(node)) {
                        return;
                    }
                    LocalityUtils.this.locality.removeNode(node);
                    Hashtable hashtable = GESUtils.getLargestConnectedSubgraph(LocalityUtils.this.locality);
                    LocalityUtils.this.markDistantNodes(hashtable);
                    try {
                        LocalityUtils.this.locality.addNodeWithEdges(node);
                    }
                    catch (TGException var3_3) {
                        var3_3.printStackTrace();
                    }
                    LocalityUtils.this.tgPanel.repaint();
                    LocalityUtils.this.tgPanel.resetDamper();
                    try {
                        Thread.currentThread();
                        Thread.sleep(600);
                    }
                    catch (InterruptedException var3_4) {
                        // empty catch block
                    }
                    LocalityUtils.this.removeMarkedNodes();
                    LocalityUtils.this.tgPanel.resetDamper();
                }
            }
        }.start();
    }

    class ShiftLocaleThread
    extends Thread {
        Hashtable distHash;
        Node focusNode;
        int radius;
        int maxAddEdgeCount;
        int maxExpandEdgeCount;
        boolean unidirectional;

        ShiftLocaleThread(Node node, int n, int n2, int n3, boolean bl) {
            this.focusNode = node;
            this.radius = n;
            this.maxAddEdgeCount = n2;
            this.maxExpandEdgeCount = n3;
            this.unidirectional = bl;
            this.start();
        }

        public void run() {
            LocalityUtils localityUtils = LocalityUtils.this;
            synchronized (localityUtils) {
                if (!LocalityUtils.this.locality.getCompleteEltSet().contains(this.focusNode)) {
                    return;
                }
                LocalityUtils.this.tgPanel.stopDamper();
                this.distHash = GESUtils.calculateDistances(LocalityUtils.this.locality.getCompleteEltSet(), this.focusNode, this.radius, this.maxAddEdgeCount, this.maxExpandEdgeCount, this.unidirectional);
                try {
                    int n;
                    int n2;
                    if (this.radius == 1) {
                        LocalityUtils.this.addNearNodes(this.distHash, this.radius);
                        n = 0;
                        while (n < 4 && !LocalityUtils.this.fastFinishShift) {
                            Thread.currentThread();
                            Thread.sleep(100);
                            ++n;
                        }
                        LocalityUtils.this.unmarkNewAdditions();
                        n2 = 0;
                        while (n2 < 4 && !LocalityUtils.this.fastFinishShift) {
                            Thread.currentThread();
                            Thread.sleep(100);
                            ++n2;
                        }
                    }
                    if (LocalityUtils.this.markDistantNodes(this.distHash)) {
                        n = 0;
                        while (n < 8 && !LocalityUtils.this.fastFinishShift) {
                            Thread.currentThread();
                            Thread.sleep(100);
                            ++n;
                        }
                    }
                    LocalityUtils.this.removeMarkedNodes();
                    n = 0;
                    while (n < 1 && !LocalityUtils.this.fastFinishShift) {
                        if (this.radius > 1) {
                            Thread.currentThread();
                            Thread.sleep(100);
                        }
                        ++n;
                    }
                    if (this.radius != 1) {
                        LocalityUtils.this.addNearNodes(this.distHash, this.radius);
                        n2 = 0;
                        while (n2 < 4 && !LocalityUtils.this.fastFinishShift) {
                            Thread.currentThread();
                            Thread.sleep(100);
                            ++n2;
                        }
                        LocalityUtils.this.unmarkNewAdditions();
                    }
                }
                catch (TGException var2_3) {
                    System.err.println("TGException: " + var2_3.getMessage());
                }
                catch (InterruptedException var3_5) {
                    // empty catch block
                }
                LocalityUtils.this.tgPanel.resetDamper();
            }
        }
    }

}

