/*
 * Decompiled with CFR 0_114.
 */
package gui;

import gsh.types.Concept;
import gsh.types.GSH;
import gsh.types.ToGraph;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.undo.UndoableEdit;
import jcornflower.ui.JCSwing;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;

public class JGraphApplet
extends JApplet {
    private static final long serialVersionUID = 3256444702936019250L;
    private static final Dimension DEFAULT_SIZE = JCSwing.getScreenSize();
    private JGraphModelAdapter<String, DefaultEdge> jgAdapter;

    public void setJGraph(GSH gsh) {
        this.jgAdapter = new JGraphModelAdapter<String, DefaultEdge>(ToGraph.fromGSH(gsh));
        JGraph jgraph = new JGraph(this.jgAdapter);
        this.adjustDisplaySettings(jgraph);
        this.getContentPane().add(jgraph);
        this.resize(DEFAULT_SIZE);
        HashMap<Concept, Integer> levels = new HashMap<Concept, Integer>();
        for (Concept p : gsh.getConcepts()) {
            levels.put(p, p.getLevel());
        }
        int ymax = 0;
        HashMap<Integer, Integer> col = new HashMap<Integer, Integer>();
        for (Concept p2 : levels.keySet()) {
            int level = (Integer)levels.get(p2);
            int value = !col.containsKey(level) ? 1 : (Integer)col.get(level) + 1;
            col.put(level, value);
            if (level <= ymax) continue;
            ymax = level;
        }
        HashMap<Integer, Integer> counters = new HashMap<Integer, Integer>(col);
        for (Concept p3 : levels.keySet()) {
            int level = (Integer)levels.get(p3);
            int onlevel = (Integer)col.get(level);
            int x = JGraphApplet.DEFAULT_SIZE.width / (onlevel + 1) * (Integer)counters.get(level) - 70;
            int y = JGraphApplet.DEFAULT_SIZE.height / (ymax + 1) * (ymax - level + 1);
            counters.put(level, (Integer)counters.get(level) - 1);
            this.positionAndResize(p3.toString(), x, y, p3.getWidth() * 8 + 15, p3.getHeight() * 30 + 10);
        }
    }

    public void run() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setTitle("Galois' Sub-Hierarchy graph visualization");
        frame.setDefaultCloseOperation(2);
        frame.pack();
        frame.setVisible(true);
    }

    private void adjustDisplaySettings(JGraph jg) {
    }

    public void positionAndResize(Object vertex, int x, int y, int width, int height) {
        DefaultGraphCell cell = this.jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D.Double newBounds = new Rectangle2D.Double(x, y, width, height);
        GraphConstants.setBounds(attr, newBounds);
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        this.jgAdapter.edit((Map)cellAttr, null, null, null);
    }
}

