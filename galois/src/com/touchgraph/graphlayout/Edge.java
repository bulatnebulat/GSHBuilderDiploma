/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Edge {
    public static Color DEFAULT_COLOR = Color.decode("#006090");
    public static Color MOUSE_OVER_COLOR = Color.decode("#ccddff");
    public static int DEFAULT_LENGTH = 40;
    public Node from;
    public Node to;
    protected Color col;
    protected int length;
    protected boolean visible;
    protected String id = null;

    public Edge(Node node, Node node2, int n) {
        this.from = node;
        this.to = node2;
        this.length = n;
        this.col = DEFAULT_COLOR;
        this.visible = false;
    }

    public Edge(Node node, Node node2) {
        this(node, node2, DEFAULT_LENGTH);
    }

    public static void setEdgeDefaultColor(Color color) {
        DEFAULT_COLOR = color;
    }

    public static void setEdgeMouseOverColor(Color color) {
        MOUSE_OVER_COLOR = color;
    }

    public static void setEdgeDefaultLength(int n) {
        DEFAULT_LENGTH = n;
    }

    public Node getFrom() {
        return this.from;
    }

    public Node getTo() {
        return this.to;
    }

    public Color getColor() {
        return this.col;
    }

    public void setColor(Color color) {
        this.col = color;
    }

    public String getID() {
        return this.id;
    }

    public void setID(String string) {
        this.id = string;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int n) {
        this.length = n;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Node getOtherEndpt(Node node) {
        if (this.to != node) {
            return this.to;
        }
        return this.from;
    }

    public void reverse() {
        Node node = this.to;
        this.to = this.from;
        this.from = node;
    }

    public boolean intersects(Dimension dimension) {
        int n = (int)this.from.drawx;
        int n2 = (int)this.from.drawy;
        int n3 = (int)this.to.drawx;
        int n4 = (int)this.to.drawy;
        return !(n <= 0 && n3 <= 0 || n >= dimension.width && n3 >= dimension.width || n2 <= 0 && n4 <= 0 || n2 >= dimension.height && n4 >= dimension.height);
    }

    public double distFromPoint(double d, double d2) {
        double d3 = this.from.drawx;
        double d4 = this.from.drawy;
        double d5 = this.to.drawx;
        double d6 = this.to.drawy;
        if (d < Math.min(d3, d5) - 8.0 || d > Math.max(d3, d5) + 8.0 || d2 < Math.min(d4, d6) - 8.0 || d2 > Math.max(d4, d6) + 8.0) {
            return 1000.0;
        }
        double d7 = 1000.0;
        if (d3 - d5 != 0.0) {
            d7 = Math.abs((d6 - d4) / (d5 - d3) * (d - d3) + (d4 - d2));
        }
        if (d4 - d6 != 0.0) {
            d7 = Math.min(d7, Math.abs((d5 - d3) / (d6 - d4) * (d2 - d4) + (d3 - d)));
        }
        return d7;
    }

    public boolean containsPoint(double d, double d2) {
        return this.distFromPoint(d, d2) < 10.0;
    }

    public static void paintArrow(Graphics graphics, int n, int n2, int n3, int n4, Color color) {
        graphics.setColor(color);
        int n5 = n;
        int n6 = n2;
        double d = Math.sqrt((n3 - n) * (n3 - n) + (n4 - n2) * (n4 - n2));
        if (d > 10.0) {
            double d2 = (d - 10.0) / d;
            n5 = (int)((double)n + (double)(n3 - n) * d2);
            n6 = (int)((double)n2 + (double)(n4 - n2) * d2);
        }
        n5 = (int)((double)(n5 * 4 + n) / 5.0);
        n6 = (int)((double)(n6 * 4 + n2) / 5.0);
        graphics.drawLine(n5, n6, n3, n4);
        graphics.drawLine(n, n2, n5, n6);
        graphics.drawLine(n + 1, n2, n5, n6);
        graphics.drawLine(n + 2, n2, n5, n6);
        graphics.drawLine(n + 3, n2, n5, n6);
        graphics.drawLine(n + 4, n2, n5, n6);
        graphics.drawLine(n - 1, n2, n5, n6);
        graphics.drawLine(n - 2, n2, n5, n6);
        graphics.drawLine(n - 3, n2, n5, n6);
        graphics.drawLine(n - 4, n2, n5, n6);
        graphics.drawLine(n, n2 + 1, n5, n6);
        graphics.drawLine(n, n2 + 2, n5, n6);
        graphics.drawLine(n, n2 + 3, n5, n6);
        graphics.drawLine(n, n2 + 4, n5, n6);
        graphics.drawLine(n, n2 - 1, n5, n6);
        graphics.drawLine(n, n2 - 2, n5, n6);
        graphics.drawLine(n, n2 - 3, n5, n6);
        graphics.drawLine(n, n2 - 4, n5, n6);
    }

    public void paint(Graphics graphics, TGPanel tGPanel) {
        Color color = tGPanel.getMouseOverE() == this ? MOUSE_OVER_COLOR : this.col;
        int n = (int)this.from.drawx;
        int n2 = (int)this.from.drawy;
        int n3 = (int)this.to.drawx;
        int n4 = (int)this.to.drawy;
        if (this.intersects(tGPanel.getSize())) {
            Edge.paintArrow(graphics, n, n2, n3, n4, color);
        }
    }
}

