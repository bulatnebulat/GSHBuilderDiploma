/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.LocalityUtils;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGLayout;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPaintListener;
import com.touchgraph.graphlayout.TGPoint2D;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet;
import com.touchgraph.graphlayout.graphelements.Locality;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import com.touchgraph.graphlayout.interaction.GLEditUI;
import com.touchgraph.graphlayout.interaction.TGAbstractClickUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.Vector;

public class TGPanel
extends Panel {
    public static Color BACK_COLOR = Color.white;
    private GraphEltSet completeEltSet;
    private VisibleLocality visibleLocality;
    private LocalityUtils localityUtils;
    public TGLayout tgLayout;
    protected BasicMouseMotionListener basicMML;
    protected Edge mouseOverE;
    protected Node mouseOverN;
    protected boolean maintainMouseOver = false;
    protected Node select;
    Node dragNode;
    protected Point mousePos;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;
    private Vector graphListeners;
    private Vector paintListeners;
    TGLensSet tgLensSet;
    AdjustOriginLens adjustOriginLens;
    SwitchSelectUI switchSelectUI;
    TGPoint2D topLeftDraw = null;
    TGPoint2D bottomRightDraw = null;

    public TGPanel() {
        this.setLayout(null);
        this.setGraphEltSet(new GraphEltSet());
        this.addMouseListener(new BasicMouseListener());
        this.basicMML = new BasicMouseMotionListener();
        this.addMouseMotionListener(this.basicMML);
        this.graphListeners = new Vector();
        this.paintListeners = new Vector();
        this.adjustOriginLens = new AdjustOriginLens();
        this.switchSelectUI = new SwitchSelectUI();
        TGLayout tGLayout = new TGLayout(this);
        this.setTGLayout(tGLayout);
        tGLayout.start();
        this.setGraphEltSet(new GraphEltSet());
    }

    public void setLensSet(TGLensSet tGLensSet) {
        this.tgLensSet = tGLensSet;
    }

    public void setTGLayout(TGLayout tGLayout) {
        this.tgLayout = tGLayout;
    }

    public void setGraphEltSet(GraphEltSet graphEltSet) {
        this.completeEltSet = graphEltSet;
        this.visibleLocality = new VisibleLocality(this.completeEltSet);
        this.localityUtils = new LocalityUtils(this.visibleLocality, this);
    }

    public AdjustOriginLens getAdjustOriginLens() {
        return this.adjustOriginLens;
    }

    public SwitchSelectUI getSwitchSelectUI() {
        return this.switchSelectUI;
    }

    public void setBackColor(Color color) {
        BACK_COLOR = color;
    }

    public ImmutableGraphEltSet getGES() {
        return this.visibleLocality;
    }

    public int getNodeCount() {
        return this.completeEltSet.nodeCount();
    }

    public int nodeNum() {
        return this.visibleLocality.nodeCount();
    }

    public int visibleNodeCount() {
        return this.visibleLocality.nodeCount();
    }

    public Node findNode(String string) {
        if (string == null) {
            return null;
        }
        return this.completeEltSet.findNode(string);
    }

    public Node findNodeByURL(String string) {
        if (string == null) {
            return null;
        }
        return this.completeEltSet.findNodeByURL(string);
    }

    public Node findNodeLabelContaining(String string) {
        if (string == null) {
            return null;
        }
        return this.completeEltSet.findNodeLabelContaining(string);
    }

    public Node addNode() throws TGException {
        String string = String.valueOf(this.getNodeCount() + 1);
        return this.addNode(string, null);
    }

    public Node addNode(String string) throws TGException {
        return this.addNode(null, string);
    }

    public Node addNode(String string, String string2) throws TGException {
        Node node = string2 == null ? new Node(string) : new Node(string, string2);
        this.updateDrawPos(node);
        this.addNode(node);
        return node;
    }

    public void addNode(Node node) throws TGException {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            this.visibleLocality.addNode(node);
            this.resetDamper();
        }
    }

    public boolean deleteNodeById(String string) {
        if (string == null) {
            return false;
        }
        Node node = this.findNode(string);
        if (node == null) {
            return false;
        }
        return this.deleteNode(node);
    }

    public boolean deleteNode(Node node) {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            if (this.visibleLocality.deleteNode(node)) {
                if (node == this.select) {
                    this.clearSelect();
                }
                this.resetDamper();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    public void clearAll() {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            this.visibleLocality.clearAll();
        }
    }

    public Node getSelect() {
        return this.select;
    }

    public Node getMouseOverN() {
        return this.mouseOverN;
    }

    public synchronized void setMouseOverN(Node node) {
        if (this.dragNode != null || this.maintainMouseOver) {
            return;
        }
        if (this.mouseOverN != node) {
            Node node2 = this.mouseOverN;
            this.mouseOverN = node;
        }
        if (this.mouseOverN == null) {
            this.setCursor(new Cursor(13));
        } else {
            this.setCursor(new Cursor(12));
        }
    }

    public void deleteEdge(Edge edge) {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            this.visibleLocality.deleteEdge(edge);
            this.resetDamper();
        }
    }

    public void deleteEdge(Node node, Node node2) {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            this.visibleLocality.deleteEdge(node, node2);
        }
    }

    public int getEdgeCount() {
        return this.completeEltSet.edgeCount();
    }

    public int edgeNum() {
        return this.visibleLocality.edgeCount();
    }

    public int visibleEdgeCount() {
        return this.visibleLocality.edgeCount();
    }

    public Edge findEdge(Node node, Node node2) {
        return this.visibleLocality.findEdge(node, node2);
    }

    public void addEdge(Edge edge) {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            this.visibleLocality.addEdge(edge);
            this.resetDamper();
        }
    }

    public Edge addEdge(Node node, Node node2, int n) {
        LocalityUtils localityUtils = this.localityUtils;
        synchronized (localityUtils) {
            Edge edge = this.visibleLocality.addEdge(node, node2, n);
            return edge;
        }
    }

    public Edge getMouseOverE() {
        return this.mouseOverE;
    }

    public synchronized void setMouseOverE(Edge edge) {
        if (this.dragNode != null || this.maintainMouseOver) {
            return;
        }
        if (this.mouseOverE != edge) {
            Edge edge2 = this.mouseOverE;
            this.mouseOverE = edge;
        }
    }

    void fireMovedEvent() {
        Vector vector;
        TGPanel tGPanel = this;
        synchronized (tGPanel) {
            vector = (Vector)this.graphListeners.clone();
        }
        int n = 0;
        while (n < vector.size()) {
            GraphListener graphListener = (GraphListener)vector.elementAt(n);
            graphListener.graphMoved();
            ++n;
        }
    }

    public void fireResetEvent() {
        Vector vector;
        TGPanel tGPanel = this;
        synchronized (tGPanel) {
            vector = (Vector)this.graphListeners.clone();
        }
        int n = 0;
        while (n < vector.size()) {
            GraphListener graphListener = (GraphListener)vector.elementAt(n);
            graphListener.graphReset();
            ++n;
        }
    }

    public synchronized void addGraphListener(GraphListener graphListener) {
        this.graphListeners.addElement(graphListener);
    }

    public synchronized void removeGraphListener(GraphListener graphListener) {
        this.graphListeners.removeElement(graphListener);
    }

    public synchronized void addPaintListener(TGPaintListener tGPaintListener) {
        this.paintListeners.addElement(tGPaintListener);
    }

    public synchronized void removePaintListener(TGPaintListener tGPaintListener) {
        this.paintListeners.removeElement(tGPaintListener);
    }

    private void redraw() {
        this.resetDamper();
    }

    public void setMaintainMouseOver(boolean bl) {
        this.maintainMouseOver = bl;
    }

    public void clearSelect() {
        if (this.select != null) {
            this.select = null;
            this.repaint();
        }
    }

    public void selectFirstNode() {
        this.setSelect(this.getGES().getFirstNode());
    }

    public void setSelect(Node node) {
        if (node != null) {
            this.select = node;
            this.repaint();
        } else if (node == null) {
            this.clearSelect();
        }
    }

    public void multiSelect(TGPoint2D tGPoint2D, TGPoint2D tGPoint2D2) {
        double d;
        double d2;
        double d3;
        double d4;
        if (tGPoint2D.x > tGPoint2D2.x) {
            d2 = tGPoint2D.x;
            d4 = tGPoint2D2.x;
        } else {
            d4 = tGPoint2D.x;
            d2 = tGPoint2D2.x;
        }
        if (tGPoint2D.y > tGPoint2D2.y) {
            d3 = tGPoint2D.y;
            d = tGPoint2D2.y;
        } else {
            d = tGPoint2D.y;
            d3 = tGPoint2D2.y;
        }
        final Vector vector = new Vector();
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                double d5 = node.drawx;
                double d22 = node.drawy;
                if (d5 > d4 && d5 < d2 && d22 > d && d22 < d3) {
                    vector.addElement(node);
                }
            }
        };
        this.visibleLocality.forAllNodes(tGForEachNode);
        if (vector.size() > 0) {
            int n = (int)(Math.random() * (double)vector.size());
            this.setSelect((Node)vector.elementAt(n));
        } else {
            this.clearSelect();
        }
    }

    public void updateLocalityFromVisibility() throws TGException {
        this.visibleLocality.updateLocalityFromVisibility();
    }

    public void setLocale(Node node, int n, int n2, int n3, boolean bl) throws TGException {
        this.localityUtils.setLocale(node, n, n2, n3, bl);
    }

    public void fastFinishAnimation() {
        this.localityUtils.fastFinishAnimation();
    }

    public void setLocale(Node node, int n) throws TGException {
        this.localityUtils.setLocale(node, n);
    }

    public void expandNode(Node node) {
        this.localityUtils.expandNode(node);
    }

    public void hideNode(Node node) {
        this.localityUtils.hideNode(node);
    }

    public void collapseNode(Node node) {
        this.localityUtils.collapseNode(node);
    }

    public void hideEdge(Edge edge) {
        this.visibleLocality.removeEdge(edge);
        if (this.mouseOverE == edge) {
            this.setMouseOverE(null);
        }
        this.resetDamper();
    }

    public void setDragNode(Node node) {
        this.dragNode = node;
        this.tgLayout.setDragNode(node);
    }

    public Node getDragNode() {
        return this.dragNode;
    }

    void setMousePos(Point point) {
        this.mousePos = point;
    }

    public Point getMousePos() {
        return this.mousePos;
    }

    public void startDamper() {
        if (this.tgLayout != null) {
            this.tgLayout.startDamper();
        }
    }

    public void stopDamper() {
        if (this.tgLayout != null) {
            this.tgLayout.stopDamper();
        }
    }

    public void resetDamper() {
        if (this.tgLayout != null) {
            this.tgLayout.resetDamper();
        }
    }

    public void stopMotion() {
        if (this.tgLayout != null) {
            this.tgLayout.stopMotion();
        }
    }

    protected synchronized void findMouseOver() {
        if (this.mousePos == null) {
            this.setMouseOverN(null);
            this.setMouseOverE(null);
            return;
        }
        final int n = this.mousePos.x;
        final int n2 = this.mousePos.y;
        final Node[] arrnode = new Node[1];
        final Edge[] arredge = new Edge[1];
        TGForEachNode tGForEachNode = new TGForEachNode(){
            double minoverdist;

            public void forEachNode(Node node) {
                double d = node.drawx;
                double d2 = node.drawy;
                double d3 = Math.sqrt(((double)n - d) * ((double)n - d) + ((double)n2 - d2) * ((double)n2 - d2));
                if (d3 < this.minoverdist && node.containsPoint(n, n2)) {
                    this.minoverdist = d3;
                    arrnode[0] = node;
                }
            }
        };
        this.visibleLocality.forAllNodes(tGForEachNode);
        TGForEachEdge tGForEachEdge = new TGForEachEdge(){
            double minDist;
            double minFromDist;

            public void forEachEdge(Edge edge) {
                double d;
                double d2 = edge.from.drawx;
                double d3 = edge.from.drawy;
                double d4 = edge.distFromPoint(n, n2);
                if (d4 < this.minDist) {
                    this.minDist = d4;
                    this.minFromDist = Math.sqrt(((double)n - d2) * ((double)n - d2) + ((double)n2 - d3) * ((double)n2 - d3));
                    arredge[0] = edge;
                } else if (d4 == this.minDist && (d = Math.sqrt(((double)n - d2) * ((double)n - d2) + ((double)n2 - d3) * ((double)n2 - d3))) < this.minFromDist) {
                    this.minFromDist = d;
                    arredge[0] = edge;
                }
            }
        };
        this.visibleLocality.forAllEdges(tGForEachEdge);
        this.setMouseOverN(arrnode[0]);
        if (arrnode[0] == null) {
            this.setMouseOverE(arredge[0]);
        } else {
            this.setMouseOverE(null);
        }
    }

    public TGPoint2D getTopLeftDraw() {
        return new TGPoint2D(this.topLeftDraw);
    }

    public TGPoint2D getBottomRightDraw() {
        return new TGPoint2D(this.bottomRightDraw);
    }

    public TGPoint2D getCenter() {
        return this.tgLensSet.convDrawToReal(this.getSize().width / 2, this.getSize().height / 2);
    }

    public TGPoint2D getDrawCenter() {
        return new TGPoint2D(this.getSize().width / 2, this.getSize().height / 2);
    }

    public void updateGraphSize() {
        if (this.topLeftDraw == null) {
            this.topLeftDraw = new TGPoint2D(0.0, 0.0);
        }
        if (this.bottomRightDraw == null) {
            this.bottomRightDraw = new TGPoint2D(0.0, 0.0);
        }
        TGForEachNode tGForEachNode = new TGForEachNode(){
            boolean firstNode;

            public void forEachNode(Node node) {
                if (this.firstNode) {
                    TGPanel.this.topLeftDraw.setLocation(node.drawx, node.drawy);
                    TGPanel.this.bottomRightDraw.setLocation(node.drawx, node.drawy);
                    this.firstNode = false;
                } else {
                    TGPanel.this.topLeftDraw.setLocation(Math.min(node.drawx, TGPanel.this.topLeftDraw.x), Math.min(node.drawy, TGPanel.this.topLeftDraw.y));
                    TGPanel.this.bottomRightDraw.setLocation(Math.max(node.drawx, TGPanel.this.bottomRightDraw.x), Math.max(node.drawy, TGPanel.this.bottomRightDraw.y));
                }
            }
        };
        this.visibleLocality.forAllNodes(tGForEachNode);
    }

    public synchronized void processGraphMove() {
        this.updateDrawPositions();
        this.updateGraphSize();
    }

    public synchronized void repaintAfterMove() {
        this.processGraphMove();
        this.findMouseOver();
        this.fireMovedEvent();
        this.repaint();
    }

    public void updateDrawPos(Node node) {
        TGPoint2D tGPoint2D = this.tgLensSet.convRealToDraw(node.x, node.y);
        node.drawx = tGPoint2D.x;
        node.drawy = tGPoint2D.y;
    }

    public void updatePosFromDraw(Node node) {
        TGPoint2D tGPoint2D = this.tgLensSet.convDrawToReal(node.drawx, node.drawy);
        node.x = tGPoint2D.x;
        node.y = tGPoint2D.y;
    }

    public void updateDrawPositions() {
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                TGPanel.this.updateDrawPos(node);
            }
        };
        this.visibleLocality.forAllNodes(tGForEachNode);
    }

    Color myBrighter(Color color) {
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        n = Math.min(n + 96, 255);
        n2 = Math.min(n2 + 96, 255);
        n3 = Math.min(n3 + 96, 255);
        return new Color(n, n2, n3);
    }

    public synchronized void paint(Graphics graphics) {
        this.update(graphics);
    }

    public synchronized void update(Graphics graphics) {
        Object object;
        Object object2;
        Dimension dimension = this.getSize();
        if (this.offscreen == null || dimension.width != this.offscreensize.width || dimension.height != this.offscreensize.height) {
            this.offscreen = this.createImage(dimension.width, dimension.height);
            this.offscreensize = dimension;
            this.offgraphics = this.offscreen.getGraphics();
            this.processGraphMove();
            this.findMouseOver();
            this.fireMovedEvent();
        }
        this.offgraphics.setColor(BACK_COLOR);
        this.offgraphics.fillRect(0, 0, dimension.width, dimension.height);
        TGPanel tGPanel = this;
        synchronized (tGPanel) {
            this.paintListeners = (Vector)this.paintListeners.clone();
        }
        int n = 0;
        while (n < this.paintListeners.size()) {
            object2 = (TGPaintListener)this.paintListeners.elementAt(n);
            object2.paintFirst(this.offgraphics);
            ++n;
        }
        object2 = new TGForEachEdge(){

            public void forEachEdge(Edge edge) {
                edge.paint(TGPanel.this.offgraphics, TGPanel.this);
            }
        };
        this.visibleLocality.forAllEdges((TGForEachEdge)object2);
        int n2 = 0;
        while (n2 < this.paintListeners.size()) {
            object = (TGPaintListener)this.paintListeners.elementAt(n2);
            object.paintAfterEdges(this.offgraphics);
            ++n2;
        }
        object = new TGForEachNode(){

            public void forEachNode(Node node) {
                node.paint(TGPanel.this.offgraphics, TGPanel.this);
            }
        };
        this.visibleLocality.forAllNodes((TGForEachNode)object);
        if (this.mouseOverE != null) {
            this.mouseOverE.paint(this.offgraphics, this);
            this.mouseOverE.from.paint(this.offgraphics, this);
            this.mouseOverE.to.paint(this.offgraphics, this);
        }
        if (this.select != null) {
            this.select.paint(this.offgraphics, this);
        }
        if (this.mouseOverN != null) {
            this.mouseOverN.paint(this.offgraphics, this);
        }
        int n3 = 0;
        while (n3 < this.paintListeners.size()) {
            TGPaintListener tGPaintListener = (TGPaintListener)this.paintListeners.elementAt(n3);
            tGPaintListener.paintLast(this.offgraphics);
            ++n3;
        }
        this.paintComponents(this.offgraphics);
        graphics.drawImage(this.offscreen, 0, 0, null);
    }

    public static void main(String[] arrstring) {
        Frame frame = new Frame("TGPanel");
        TGPanel tGPanel = new TGPanel();
        frame.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        TGLensSet tGLensSet = new TGLensSet();
        tGLensSet.addLens(tGPanel.getAdjustOriginLens());
        tGPanel.setLensSet(tGLensSet);
        try {
            tGPanel.addNode();
        }
        catch (TGException var4_4) {
            System.err.println(var4_4.getMessage());
        }
        tGPanel.setVisible(true);
        new GLEditUI(tGPanel).activate();
        frame.add("Center", tGPanel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    class BasicMouseMotionListener
    implements MouseMotionListener {
        BasicMouseMotionListener() {
        }

        public void mouseDragged(MouseEvent mouseEvent) {
            TGPanel.this.mousePos = mouseEvent.getPoint();
            TGPanel.this.findMouseOver();
            try {
                Thread.currentThread();
                Thread.sleep(6);
            }
            catch (InterruptedException var2_2) {
                // empty catch block
            }
        }

        public void mouseMoved(MouseEvent mouseEvent) {
            TGPanel.this.mousePos = mouseEvent.getPoint();
            BasicMouseMotionListener basicMouseMotionListener = this;
            synchronized (basicMouseMotionListener) {
                Edge edge = TGPanel.this.mouseOverE;
                Node node = TGPanel.this.mouseOverN;
                TGPanel.this.findMouseOver();
                if (edge != TGPanel.this.mouseOverE || node != TGPanel.this.mouseOverN) {
                    TGPanel.this.repaint();
                }
            }
        }
    }

    class BasicMouseListener
    extends MouseAdapter {
        BasicMouseListener() {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
            TGPanel.this.addMouseMotionListener(TGPanel.this.basicMML);
        }

        public void mouseExited(MouseEvent mouseEvent) {
            TGPanel.this.removeMouseMotionListener(TGPanel.this.basicMML);
            TGPanel.this.mousePos = null;
            TGPanel.this.setMouseOverN(null);
            TGPanel.this.setMouseOverE(null);
            TGPanel.this.repaint();
        }
    }

    public class SwitchSelectUI
    extends TGAbstractClickUI {
        public void mouseClicked(MouseEvent mouseEvent) {
            if (TGPanel.this.mouseOverN != null) {
                if (TGPanel.this.mouseOverN != TGPanel.this.select) {
                    TGPanel.this.setSelect(TGPanel.this.mouseOverN);
                } else {
                    TGPanel.this.clearSelect();
                }
            }
        }
    }

    protected class AdjustOriginLens
    extends TGAbstractLens {
        protected AdjustOriginLens() {
        }

        protected void applyLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x += (double)(TGPanel.this.getSize().width / 2);
            tGPoint2D.y += (double)(TGPanel.this.getSize().height / 2);
        }

        protected void undoLens(TGPoint2D tGPoint2D) {
            tGPoint2D.x -= (double)(TGPanel.this.getSize().width / 2);
            tGPoint2D.y -= (double)(TGPanel.this.getSize().height / 2);
        }
    }

}

