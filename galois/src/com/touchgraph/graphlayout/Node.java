/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

public class Node {
    public static final int TYPE_RECTANGLE = 1;
    public static final int TYPE_ROUNDRECT = 2;
    public static final int TYPE_ELLIPSE = 3;
    public static final int TYPE_CIRCLE = 4;
    public static final Font SMALL_TAG_FONT = new Font("Courier", 0, 9);
    public Color BACK_FIXED_COLOR = new Color(255, 32, 20);
    public Color BACK_SELECT_COLOR = new Color(225, 164, 0);
    public Color BACK_DEFAULT_COLOR = Color.decode("#4080A0");
    public Color BACK_HILIGHT_COLOR = new Color(205, 192, 166);
    public Color BACK_MRF_COLOR = new Color(2, 35, 81);
    public Color BACK_JML_COLOR = new Color(58, 176, 255);
    public Color BORDER_DRAG_COLOR = new Color(130, 130, 180);
    public Color BORDER_MOUSE_OVER_COLOR = new Color(160, 160, 180);
    public Color BORDER_INACTIVE_COLOR = new Color(30, 50, 160);
    public Color TEXT_COLOR = Color.white;
    public static Font TEXT_FONT = new Font("Verdana", 0, 10);
    public static int DEFAULT_TYPE = 1;
    protected int typ = 1;
    private String id;
    public double drawx;
    public double drawy;
    protected FontMetrics fontMetrics;
    protected Font font;
    protected String lbl;
    protected Color backColor = this.BACK_DEFAULT_COLOR;
    protected Color textColor = this.TEXT_COLOR;
    public double x;
    public double y;
    public double massfade = 1.0;
    protected double dx;
    protected double dy;
    protected boolean fixed;
    protected int repulsion;
    public boolean justMadeLocal = false;
    public boolean markedForRemoval = false;
    public int visibleEdgeCnt;
    protected boolean visible;
    private Vector edges;
    private String strUrl;

    public Node() {
        this.initialize(null);
        this.lbl = this.id;
    }

    public Node(String string) {
        this.initialize(string);
        this.lbl = string;
    }

    public Node(String string, String string2) {
        this.initialize(string);
        this.lbl = string2 == null ? string : string2;
    }

    public Node(String string, int n, Color color, String string2) {
        this.initialize(string);
        this.typ = n;
        this.backColor = color;
        this.lbl = string2 == null ? string : string2;
    }

    private void initialize(String string) {
        this.id = string;
        this.edges = new Vector();
        this.x = Math.random() * 2.0 - 1.0;
        this.y = Math.random() * 2.0 - 1.0;
        this.repulsion = 100;
        this.font = TEXT_FONT;
        this.fixed = false;
        this.typ = DEFAULT_TYPE;
        this.visibleEdgeCnt = 0;
        this.visible = false;
    }

    public void setNodeBackFixedColor(Color color) {
        this.BACK_FIXED_COLOR = color;
    }

    public void setNodeBackSelectColor(Color color) {
        this.BACK_SELECT_COLOR = color;
    }

    public void setNodeBackDefaultColor(Color color) {
        this.BACK_DEFAULT_COLOR = color;
    }

    public void setNodeBackHilightColor(Color color) {
        this.BACK_HILIGHT_COLOR = color;
    }

    public void setNodeBorderDragColor(Color color) {
        this.BORDER_DRAG_COLOR = color;
    }

    public void setNodeBorderMouseOverColor(Color color) {
        this.BORDER_MOUSE_OVER_COLOR = color;
    }

    public void setNodeBorderInactiveColor(Color color) {
        this.BORDER_INACTIVE_COLOR = color;
    }

    public void setNodeTextColor(Color color) {
        this.TEXT_COLOR = color;
    }

    public void setNodeTextFont(Font font) {
        TEXT_FONT = font;
    }

    public void setNodeType(int n) {
        DEFAULT_TYPE = n;
    }

    public void setID(String string) {
        this.id = string;
    }

    public String getID() {
        return this.id;
    }

    public void setLocation(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Point getLocation() {
        return new Point((int)this.x, (int)this.y);
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setType(int n) {
        this.typ = n;
    }

    public int getType() {
        return this.typ;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

    public void setBackColor(Color color) {
        this.backColor = color;
    }

    public Color getBackColor() {
        return this.backColor;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public void setLabel(String string) {
        this.lbl = string;
    }

    public String getLabel() {
        return this.lbl;
    }

    public void setFixed(boolean bl) {
        this.fixed = bl;
    }

    public boolean getFixed() {
        return this.fixed;
    }

    public int edgeNum() {
        return this.edges.size();
    }

    public int edgeCount() {
        return this.edges.size();
    }

    public int visibleEdgeCount() {
        return this.visibleEdgeCnt;
    }

    public Edge edgeAt(int n) {
        return (Edge)this.edges.elementAt(n);
    }

    public void addEdge(Edge edge) {
        if (edge == null) {
            return;
        }
        this.edges.addElement(edge);
    }

    public void removeEdge(Edge edge) {
        this.edges.removeElement(edge);
    }

    public int getWidth() {
        if (this.fontMetrics != null && this.lbl != null) {
            return this.fontMetrics.stringWidth(this.lbl) + 12;
        }
        return 10;
    }

    public int getHeight() {
        if (this.fontMetrics != null) {
            return this.fontMetrics.getHeight() + 6;
        }
        return 6;
    }

    public boolean intersects(Dimension dimension) {
        return this.drawx > 0.0 && this.drawx < (double)dimension.width && this.drawy > 0.0 && this.drawy < (double)dimension.height;
    }

    public boolean containsPoint(double d, double d2) {
        return d > this.drawx - (double)(this.getWidth() / 2) && d < this.drawx + (double)(this.getWidth() / 2) && d2 > this.drawy - (double)(this.getHeight() / 2) && d2 < this.drawy + (double)(this.getHeight() / 2);
    }

    public boolean containsPoint(Point point) {
        return (double)point.x > this.drawx - (double)(this.getWidth() / 2) && (double)point.x < this.drawx + (double)(this.getWidth() / 2) && (double)point.y > this.drawy - (double)(this.getHeight() / 2) && (double)point.y < this.drawy + (double)(this.getHeight() / 2);
    }

    public void paint(Graphics graphics, TGPanel tGPanel) {
        if (!this.intersects(tGPanel.getSize())) {
            return;
        }
        this.paintNodeBody(graphics, tGPanel);
        if (this.visibleEdgeCount() < this.edgeCount()) {
            int n = (int)this.drawx;
            int n2 = (int)this.drawy;
            int n3 = this.getHeight();
            int n4 = this.getWidth();
            int n5 = n + (n4 - 7) / 2 - 2 + n4 % 2;
            int n6 = n2 - n3 / 2 - 2;
            int n7 = this.edgeCount() - this.visibleEdgeCount();
            char c = n7 < 9 ? (char)(48 + n7) : '*';
            this.paintSmallTag(graphics, tGPanel, n5, n6, Color.red, Color.white, c);
        }
    }

    public Color getPaintBorderColor(TGPanel tGPanel) {
        if (this == tGPanel.getDragNode()) {
            return this.BORDER_DRAG_COLOR;
        }
        if (this == tGPanel.getMouseOverN()) {
            return this.BORDER_MOUSE_OVER_COLOR;
        }
        return this.BORDER_INACTIVE_COLOR;
    }

    public Color getPaintBackColor(TGPanel tGPanel) {
        if (this == tGPanel.getSelect()) {
            return this.BACK_SELECT_COLOR;
        }
        if (this.fixed) {
            return this.BACK_FIXED_COLOR;
        }
        if (this.markedForRemoval) {
            return this.BACK_MRF_COLOR;
        }
        if (this.justMadeLocal) {
            return this.BACK_JML_COLOR;
        }
        return this.backColor;
    }

    public Color getPaintTextColor(TGPanel tGPanel) {
        return this.textColor;
    }

    public void paintNodeBody(Graphics graphics, TGPanel tGPanel) {
        graphics.setFont(this.font);
        this.fontMetrics = graphics.getFontMetrics();
        int n = (int)this.drawx;
        int n2 = (int)this.drawy;
        int n3 = this.getHeight();
        int n4 = this.getWidth();
        int n5 = n3 / 2 + 1;
        Color color = this.getPaintBorderColor(tGPanel);
        graphics.setColor(color);
        if (this.typ == 2) {
            graphics.fillRoundRect(n - n4 / 2, n2 - n3 / 2, n4, n3, n5, n5);
        } else if (this.typ == 3) {
            graphics.fillOval(n - n4 / 2, n2 - n3 / 2, n4, n3);
        } else if (this.typ == 4) {
            graphics.fillOval(n - n4 / 2, n2 - n4 / 2, n4, n4);
        } else {
            graphics.fillRect(n - n4 / 2, n2 - n3 / 2, n4, n3);
        }
        Color color2 = this.getPaintBackColor(tGPanel);
        graphics.setColor(color2);
        if (this.typ == 2) {
            graphics.fillRoundRect(n - n4 / 2 + 2, n2 - n3 / 2 + 2, n4 - 4, n3 - 4, n5, n5);
        } else if (this.typ == 3) {
            graphics.fillOval(n - n4 / 2 + 2, n2 - n3 / 2 + 2, n4 - 4, n3 - 4);
        } else if (this.typ == 4) {
            graphics.fillOval(n - n4 / 2 + 2, n2 - n4 / 2 + 2, n4 - 4, n4 - 4);
        } else {
            graphics.fillRect(n - n4 / 2 + 2, n2 - n3 / 2 + 2, n4 - 4, n3 - 4);
        }
        Color color3 = this.getPaintTextColor(tGPanel);
        graphics.setColor(color3);
        graphics.drawString(this.lbl, n - this.fontMetrics.stringWidth(this.lbl) / 2, n2 + this.fontMetrics.getDescent() + 1);
    }

    public void paintSmallTag(Graphics graphics, TGPanel tGPanel, int n, int n2, Color color, Color color2, char c) {
        graphics.setColor(color);
        graphics.fillRect(n, n2, 8, 8);
        graphics.setColor(color2);
        graphics.setFont(SMALL_TAG_FONT);
        graphics.drawString("" + c, n + 2, n2 + 7);
    }

    public String getURL() {
        return this.strUrl;
    }

    public void setURL(String string) {
        this.strUrl = string;
    }
}

