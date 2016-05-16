/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.DragAddUI;
import com.touchgraph.graphlayout.interaction.DragMultiselectUI;
import com.touchgraph.graphlayout.interaction.DragNodeUI;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.TGAbstractClickUI;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import com.touchgraph.graphlayout.interaction.TGUserInterface;
import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;

public class GLEditUI
extends TGUserInterface {
    TGPanel tgPanel;
    DragAddUI dragAddUI;
    DragNodeUI dragNodeUI;
    DragMultiselectUI dragMultiselectUI;
    TGAbstractClickUI switchSelectUI;
    TGAbstractDragUI hvDragUI;
    GLEditMouseListener ml;
    GLEditMouseMotionListener mml;
    PopupMenu nodePopup;
    PopupMenu edgePopup;
    PopupMenu backPopup;
    Node popupNode;
    Edge popupEdge;

    public GLEditUI(TGPanel tGPanel) {
        this.active = false;
        this.tgPanel = tGPanel;
        this.ml = new GLEditMouseListener();
        this.mml = new GLEditMouseMotionListener();
        this.dragAddUI = new DragAddUI(this.tgPanel);
        this.dragNodeUI = new DragNodeUI(this.tgPanel);
        this.dragMultiselectUI = new DragMultiselectUI(this.tgPanel);
        this.switchSelectUI = this.tgPanel.getSwitchSelectUI();
        this.setUpNodePopup(tGPanel);
        this.setUpEdgePopup(tGPanel);
        this.setUpBackPopup(tGPanel);
    }

    public GLEditUI(GLPanel gLPanel) {
        this(gLPanel.getTGPanel());
        this.hvDragUI = gLPanel.hvScroll.getHVDragUI();
    }

    public void activate() {
        this.tgPanel.addMouseListener(this.ml);
        this.tgPanel.addMouseMotionListener(this.mml);
        this.active = true;
    }

    public void deactivate() {
        if (!this.active) {
            this.dragMultiselectUI.deactivate();
        }
        this.tgPanel.removeMouseListener(this.ml);
        this.tgPanel.removeMouseMotionListener(this.mml);
        this.active = false;
    }

    private void setUpNodePopup(TGPanel tGPanel) {
        this.nodePopup = new PopupMenu();
        tGPanel.add(this.nodePopup);
        Menu menu = new Menu("Navigate");
        MenuItem menuItem = new MenuItem("Delete Node");
        ActionListener actionListener = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupNode != null) {
                    GLEditUI.this.tgPanel.deleteNode(GLEditUI.this.popupNode);
                }
            }
        };
        menuItem.addActionListener(actionListener);
        this.nodePopup.add(menuItem);
        menuItem = new MenuItem("Expand Node");
        ActionListener actionListener2 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupNode != null) {
                    GLEditUI.this.tgPanel.expandNode(GLEditUI.this.popupNode);
                }
            }
        };
        menuItem.addActionListener(actionListener2);
        menu.add(menuItem);
        menuItem = new MenuItem("Collapse Node");
        ActionListener actionListener3 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupNode != null) {
                    GLEditUI.this.tgPanel.collapseNode(GLEditUI.this.popupNode);
                }
            }
        };
        menuItem.addActionListener(actionListener3);
        menu.add(menuItem);
        menuItem = new MenuItem("Hide Node");
        ActionListener actionListener4 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                Node node = GLEditUI.this.tgPanel.getSelect();
                if (GLEditUI.this.popupNode != null) {
                    GLEditUI.this.tgPanel.hideNode(GLEditUI.this.popupNode);
                }
            }
        };
        menuItem.addActionListener(actionListener4);
        menu.add(menuItem);
        this.nodePopup.add(menu);
    }

    private void setUpEdgePopup(TGPanel tGPanel) {
        this.edgePopup = new PopupMenu();
        tGPanel.add(this.edgePopup);
        MenuItem menuItem = new MenuItem("Relax Edge");
        ActionListener actionListener = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupEdge != null) {
                    GLEditUI.this.popupEdge.setLength(GLEditUI.this.popupEdge.getLength() * 4);
                    GLEditUI.this.tgPanel.resetDamper();
                }
            }
        };
        menuItem.addActionListener(actionListener);
        this.edgePopup.add(menuItem);
        menuItem = new MenuItem("Tighten Edge");
        ActionListener actionListener2 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupEdge != null) {
                    GLEditUI.this.popupEdge.setLength(GLEditUI.this.popupEdge.getLength() / 4);
                    GLEditUI.this.tgPanel.resetDamper();
                }
            }
        };
        menuItem.addActionListener(actionListener2);
        this.edgePopup.add(menuItem);
        menuItem = new MenuItem("Delete Edge");
        ActionListener actionListener3 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (GLEditUI.this.popupEdge != null) {
                    GLEditUI.this.tgPanel.deleteEdge(GLEditUI.this.popupEdge);
                }
            }
        };
        menuItem.addActionListener(actionListener3);
        this.edgePopup.add(menuItem);
    }

    private void setUpBackPopup(TGPanel tGPanel) {
        this.backPopup = new PopupMenu();
        tGPanel.add(this.backPopup);
        MenuItem menuItem = new MenuItem("Multi-Select");
        ActionListener actionListener = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GLEditUI.this.dragMultiselectUI.activate(GLEditUI.this);
            }
        };
        menuItem.addActionListener(actionListener);
        this.backPopup.add(menuItem);
        menuItem = new MenuItem("Start Over");
        ActionListener actionListener2 = new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                GLEditUI.this.tgPanel.clearAll();
                GLEditUI.this.tgPanel.clearSelect();
                try {
                    GLEditUI.this.tgPanel.addNode();
                }
                catch (TGException var2_2) {
                    System.err.println(var2_2.getMessage());
                    var2_2.printStackTrace(System.err);
                }
                GLEditUI.this.tgPanel.fireResetEvent();
                GLEditUI.this.tgPanel.repaint();
            }
        };
        menuItem.addActionListener(actionListener2);
        this.backPopup.add(menuItem);
    }

    class GLEditMouseMotionListener
    extends MouseMotionAdapter {
        GLEditMouseMotionListener() {
        }

        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }

    class GLEditMouseListener
    extends MouseAdapter {
        GLEditMouseListener() {
        }

        public void mousePressed(MouseEvent mouseEvent) {
            Node node = GLEditUI.this.tgPanel.getMouseOverN();
            Node node2 = GLEditUI.this.tgPanel.getSelect();
            if (mouseEvent.getModifiers() == 16) {
                if (node != null) {
                    if (node != node2) {
                        GLEditUI.this.dragNodeUI.activate(mouseEvent);
                    } else {
                        GLEditUI.this.dragAddUI.activate(mouseEvent);
                    }
                } else if (GLEditUI.this.hvDragUI != null) {
                    GLEditUI.this.hvDragUI.activate(mouseEvent);
                }
            }
        }

        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getModifiers() == 16) {
                GLEditUI.this.switchSelectUI.activate(mouseEvent);
            }
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                GLEditUI.this.popupNode = GLEditUI.this.tgPanel.getMouseOverN();
                GLEditUI.this.popupEdge = GLEditUI.this.tgPanel.getMouseOverE();
                if (GLEditUI.this.popupNode != null) {
                    GLEditUI.this.tgPanel.setMaintainMouseOver(true);
                    GLEditUI.this.nodePopup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                } else if (GLEditUI.this.popupEdge != null) {
                    GLEditUI.this.tgPanel.setMaintainMouseOver(true);
                    GLEditUI.this.edgePopup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                } else {
                    GLEditUI.this.backPopup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                }
            }
        }
    }

}

