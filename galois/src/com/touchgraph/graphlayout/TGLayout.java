/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.TGForEachNodePair;

public class TGLayout
implements Runnable {
    private TGPanel tgPanel;
    private Thread relaxer;
    private double damper = 0.0;
    private double maxMotion = 0.0;
    private double lastMaxMotion = 0.0;
    private double motionRatio = 0.0;
    private boolean damping = true;
    private double rigidity = 1.0;
    private double newRigidity = 1.0;
    Node dragNode = null;

    public TGLayout(TGPanel tGPanel) {
        this.tgPanel = tGPanel;
        this.relaxer = null;
    }

    void setRigidity(double d) {
        this.newRigidity = d;
    }

    void setDragNode(Node node) {
        this.dragNode = node;
    }

    private synchronized void relaxEdges() {
        TGForEachEdge tGForEachEdge = new TGForEachEdge(){

            public void forEachEdge(Edge edge) {
                double d;
                double d2 = edge.to.x - edge.from.x;
                double d3 = edge.to.y - edge.from.y;
                double d4 = Math.sqrt(d2 * d2 + d3 * d3);
                double d5 = d2 * TGLayout.this.rigidity;
                double d6 = d3 * TGLayout.this.rigidity;
                d5 /= (double)(edge.getLength() * 100);
                d6 /= (double)(edge.getLength() * 100);
                if (edge.to.justMadeLocal || edge.to.markedForRemoval || !edge.from.justMadeLocal && !edge.from.markedForRemoval) {
                    edge.to.dx -= d5 * d4;
                    edge.to.dy -= d6 * d4;
                } else {
                    d = edge.from.markedForRemoval ? edge.from.massfade : 1.0 - edge.from.massfade;
                    d *= d;
                    edge.to.dx -= d5 * d4 * d;
                    edge.to.dy -= d6 * d4 * d;
                }
                if (edge.from.justMadeLocal || edge.from.markedForRemoval || !edge.to.justMadeLocal && !edge.to.markedForRemoval) {
                    edge.from.dx += d5 * d4;
                    edge.from.dy += d6 * d4;
                } else {
                    d = edge.to.markedForRemoval ? edge.to.massfade : 1.0 - edge.to.massfade;
                    d *= d;
                    edge.from.dx += d5 * d4 * d;
                    edge.from.dy += d6 * d4 * d;
                }
            }
        };
        this.tgPanel.getGES().forAllEdges(tGForEachEdge);
    }

    private synchronized void avoidLabels() {
        TGForEachNodePair tGForEachNodePair = new TGForEachNodePair(){

            public void forEachNodePair(Node node, Node node2) {
                double d;
                double d2 = 0.0;
                double d3 = 0.0;
                double d4 = node.x - node2.x;
                double d5 = node.y - node2.y;
                double d6 = d4 * d4 + d5 * d5;
                if (d6 == 0.0) {
                    d2 = Math.random();
                    d3 = Math.random();
                } else if (d6 < 360000.0) {
                    d2 = d4 / d6;
                    d3 = d5 / d6;
                }
                int n = node.repulsion * node2.repulsion / 100;
                if (node.justMadeLocal || node.markedForRemoval || !node2.justMadeLocal && !node2.markedForRemoval) {
                    node.dx += d2 * (double)n * TGLayout.this.rigidity;
                    node.dy += d3 * (double)n * TGLayout.this.rigidity;
                } else {
                    d = node2.markedForRemoval ? node2.massfade : 1.0 - node2.massfade;
                    d *= d;
                    node.dx += d2 * (double)n * TGLayout.this.rigidity * d;
                    node.dy += d3 * (double)n * TGLayout.this.rigidity * d;
                }
                if (node2.justMadeLocal || node2.markedForRemoval || !node.justMadeLocal && !node.markedForRemoval) {
                    node2.dx -= d2 * (double)n * TGLayout.this.rigidity;
                    node2.dy -= d3 * (double)n * TGLayout.this.rigidity;
                } else {
                    d = node.markedForRemoval ? node.massfade : 1.0 - node.massfade;
                    d *= d;
                    node2.dx -= d2 * (double)n * TGLayout.this.rigidity * d;
                    node2.dy -= d3 * (double)n * TGLayout.this.rigidity * d;
                }
            }
        };
        this.tgPanel.getGES().forAllNodePairs(tGForEachNodePair);
    }

    public void startDamper() {
        this.damping = true;
    }

    public void stopDamper() {
        this.damping = false;
        this.damper = 1.0;
    }

    public void resetDamper() {
        this.damping = true;
        this.damper = 1.0;
    }

    public void stopMotion() {
        this.damping = true;
        this.damper = this.damper > 0.3 ? 0.3 : 0.0;
    }

    public void damp() {
        if (this.damping && this.motionRatio <= 0.001) {
            if ((this.maxMotion < 0.2 || this.maxMotion > 1.0 && this.damper < 0.9) && this.damper > 0.01) {
                this.damper -= 0.01;
            } else if (this.maxMotion < 0.4 && this.damper > 0.003) {
                this.damper -= 0.003;
            } else if (this.damper > 1.0E-4) {
                this.damper -= 1.0E-4;
            }
        }
        if (this.maxMotion < 0.001 && this.damping) {
            this.damper = 0.0;
        }
    }

    private synchronized void moveNodes() {
        this.lastMaxMotion = this.maxMotion;
        final double[] arrd = new double[]{0.0};
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                double d = node.dx;
                double d2 = node.dy;
                node.dx = (d *= TGLayout.this.damper) / 2.0;
                node.dy = (d2 *= TGLayout.this.damper) / 2.0;
                double d3 = Math.sqrt(d * d + d2 * d2);
                if (!node.fixed && node != TGLayout.this.dragNode) {
                    node.x += Math.max(-30.0, Math.min(30.0, d));
                    node.y += Math.max(-30.0, Math.min(30.0, d2));
                }
                arrd[0] = Math.max(d3, arrd[0]);
                if (!node.justMadeLocal && !node.markedForRemoval) {
                    node.massfade = 1.0;
                } else if (node.massfade >= 0.004) {
                    node.massfade -= 0.004;
                }
            }
        };
        this.tgPanel.getGES().forAllNodes(tGForEachNode);
        this.maxMotion = arrd[0];
        this.motionRatio = this.maxMotion > 0.0 ? this.lastMaxMotion / this.maxMotion - 1.0 : 0.0;
        this.damp();
    }

    private synchronized void relax() {
        int n = 0;
        while (n < 10) {
            this.relaxEdges();
            this.avoidLabels();
            this.moveNodes();
            ++n;
        }
        if (this.rigidity != this.newRigidity) {
            this.rigidity = this.newRigidity;
        }
        this.tgPanel.repaintAfterMove();
    }

    private void myWait() {
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException var1_1) {
            // empty catch block
        }
    }

    public void run() {
        Thread thread = Thread.currentThread();
        while (this.relaxer == thread) {
            this.relax();
            try {
                Thread.sleep(20);
                while (this.damper < 0.1 && this.damping && this.maxMotion < 0.001) {
                    this.myWait();
                }
                continue;
            }
            catch (InterruptedException var2_2) {
                break;
            }
        }
    }

    public void start() {
        this.relaxer = new Thread(this);
        this.relaxer.start();
    }

    public void stop() {
        this.relaxer = null;
    }

}

