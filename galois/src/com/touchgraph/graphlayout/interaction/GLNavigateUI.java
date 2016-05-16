/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.DragNodeUI;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.LocalityScroll;
import com.touchgraph.graphlayout.interaction.RotateScroll;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import com.touchgraph.graphlayout.interaction.TGUserInterface;
import java.awt.Component;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintStream;

public class GLNavigateUI
extends TGUserInterface {
    GLPanel glPanel;
    TGPanel tgPanel;
    GLNavigateMouseListener ml;
    TGAbstractDragUI hvDragUI;
    TGAbstractDragUI rotateDragUI;
    DragNodeUI dragNodeUI;
    LocalityScroll localityScroll;
    PopupMenu nodePopup;
    PopupMenu edgePopup;
    Node popupNode;
    Edge popupEdge;

    public GLNavigateUI(GLPanel gLPanel) {
        this.glPanel = gLPanel;
        this.tgPanel = this.glPanel.getTGPanel();
        this.localityScroll = this.glPanel.getLocalityScroll();
        this.hvDragUI = this.glPanel.getHVScroll().getHVDragUI();
        this.rotateDragUI = this.glPanel.getRotateScroll().getRotateDragUI();
        this.dragNodeUI = new DragNodeUI(this.tgPanel);
        this.ml = new GLNavigateMouseListener();
        this.setUpNodePopup(gLPanel);
        this.setUpEdgePopup(gLPanel);
    }

    public void activate() {
        this.tgPanel.addMouseListener(this.ml);
    }

    public void deactivate() {
        this.tgPanel.removeMouseListener(this.ml);
    }

    private void setUpNodePopup(GLPanel gLPanel) {
        this.nodePopup = new PopupMenu();
        gLPanel.add(this.nodePopup);
        MenuItem menuItem = new MenuItem("Expand Node");
        ActionListener actionListener = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLNavigateUI.this.popupNode != null) {
                    GLNavigateUI.this.tgPanel.expandNode(GLNavigateUI.this.popupNode);
                }
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
                GLNavigateUI.this.tgPanel.setMouseOverN(null);
                GLNavigateUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener);
        this.nodePopup.add(menuItem);
        menuItem = new MenuItem("Collapse Node");
        ActionListener actionListener2 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLNavigateUI.this.popupNode != null) {
                    GLNavigateUI.this.tgPanel.collapseNode(GLNavigateUI.this.popupNode);
                }
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
                GLNavigateUI.this.tgPanel.setMouseOverN(null);
                GLNavigateUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener2);
        this.nodePopup.add(menuItem);
        menuItem = new MenuItem("Hide Node");
        ActionListener actionListener3 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLNavigateUI.this.popupNode != null) {
                    GLNavigateUI.this.tgPanel.hideNode(GLNavigateUI.this.popupNode);
                }
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
                GLNavigateUI.this.tgPanel.setMouseOverN(null);
                GLNavigateUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener3);
        this.nodePopup.add(menuItem);
        menuItem = new MenuItem("Center Node");
        ActionListener actionListener4 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLNavigateUI.this.popupNode != null) {
                    GLNavigateUI.this.glPanel.getHVScroll().slowScrollToCenter(GLNavigateUI.this.popupNode);
                }
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
                GLNavigateUI.this.tgPanel.setMouseOverN(null);
                GLNavigateUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener4);
        this.nodePopup.add(menuItem);
    }

    private void setUpEdgePopup(GLPanel gLPanel) {
        this.edgePopup = new PopupMenu();
        gLPanel.add(this.edgePopup);
        MenuItem menuItem = new MenuItem("Hide Edge");
        ActionListener actionListener = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLNavigateUI.this.popupEdge != null) {
                    GLNavigateUI.this.tgPanel.hideEdge(GLNavigateUI.this.popupEdge);
                }
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
                GLNavigateUI.this.tgPanel.setMouseOverN(null);
                GLNavigateUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener);
        this.edgePopup.add(menuItem);
    }

    class GLNavigateMouseListener
    extends MouseAdapter {
        GLNavigateMouseListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            Node node = GLNavigateUI.this.tgPanel.getMouseOverN();
            if (mouseEvent.getModifiers() == 16) {
                if (node == null) {
                    GLNavigateUI.this.hvDragUI.activate(mouseEvent);
                } else {
                    GLNavigateUI.this.dragNodeUI.activate(mouseEvent);
                }
            }
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            Node node = GLNavigateUI.this.tgPanel.getMouseOverN();
            if (mouseEvent.getModifiers() == 16 && node != null) {
                GLNavigateUI.this.tgPanel.setSelect(node);
                GLNavigateUI.this.glPanel.getHVScroll().slowScrollToCenter(node);
                try {
                    GLNavigateUI.this.tgPanel.setLocale(node, GLNavigateUI.this.localityScroll.getLocalityRadius());
                }
                catch (TGException var3_3) {
                    System.out.println("Error setting locale");
                    var3_3.printStackTrace();
                }
            }
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                GLNavigateUI.this.popupNode = GLNavigateUI.this.tgPanel.getMouseOverN();
                GLNavigateUI.this.popupEdge = GLNavigateUI.this.tgPanel.getMouseOverE();
                if (GLNavigateUI.this.popupNode != null) {
                    GLNavigateUI.this.tgPanel.setMaintainMouseOver(true);
                    GLNavigateUI.this.nodePopup.show(GLNavigateUI.this.tgPanel, mouseEvent.getX(), mouseEvent.getY());
                } else if (GLNavigateUI.this.popupEdge != null) {
                    GLNavigateUI.this.tgPanel.setMaintainMouseOver(true);
                    GLNavigateUI.this.edgePopup.show(GLNavigateUI.this.tgPanel, mouseEvent.getX(), mouseEvent.getY());
                } else {
                    GLNavigateUI.this.glPanel.glPopup.show(GLNavigateUI.this.tgPanel, mouseEvent.getX(), mouseEvent.getY());
                }
            } else {
                GLNavigateUI.this.tgPanel.setMaintainMouseOver(false);
            }
        }
    }

}

