/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.interaction.GLEditUI;
import com.touchgraph.graphlayout.interaction.GLNavigateUI;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.HyperScroll;
import com.touchgraph.graphlayout.interaction.LocalityScroll;
import com.touchgraph.graphlayout.interaction.RotateScroll;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import com.touchgraph.graphlayout.interaction.TGUserInterface;
import com.touchgraph.graphlayout.interaction.ZoomScroll;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.util.Hashtable;

public class GLPanel
extends Panel {
    public String zoomLabel = "Zoom";
    public String rotateLabel = "Rotate";
    public String localityLabel = "Radius";
    public String hyperLabel = "Hyperbolic";
    public HVScroll hvScroll;
    public ZoomScroll zoomScroll;
    public HyperScroll hyperScroll;
    public RotateScroll rotateScroll;
    public LocalityScroll localityScroll;
    public PopupMenu glPopup;
    public Hashtable scrollBarHash;
    protected TGPanel tgPanel;
    protected TGLensSet tgLensSet;
    protected TGUIManager tgUIManager;
    private Scrollbar currentSB = null;
    private Color defaultBackColor = new Color(1, 17, 68);
    private Color defaultBorderBackColor = new Color(2, 53, 129);
    private Color defaultForeColor = new Color(0.95f, 0.85f, 0.55f);

    public GLPanel() {
        this.setBackground(this.defaultBorderBackColor);
        this.setForeground(this.defaultForeColor);
        this.scrollBarHash = new Hashtable();
        this.tgLensSet = new TGLensSet();
        this.tgPanel = new TGPanel();
        this.tgPanel.setBackColor(this.defaultBackColor);
        this.hvScroll = new HVScroll(this.tgPanel, this.tgLensSet);
        this.zoomScroll = new ZoomScroll(this.tgPanel);
        this.hyperScroll = new HyperScroll(this.tgPanel);
        this.rotateScroll = new RotateScroll(this.tgPanel);
        this.localityScroll = new LocalityScroll(this.tgPanel);
        this.initialize();
    }

    public void initialize() {
        this.buildPanel();
        this.buildLens();
        this.tgPanel.setLensSet(this.tgLensSet);
        this.addUIs();
        try {
            this.randomGraph();
        }
        catch (TGException var1_1) {
            System.err.println(var1_1.getMessage());
            var1_1.printStackTrace(System.err);
        }
        this.setVisible(true);
    }

    public TGPanel getTGPanel() {
        return this.tgPanel;
    }

    public HVScroll getHVScroll() {
        return this.hvScroll;
    }

    public HyperScroll getHyperScroll() {
        return this.hyperScroll;
    }

    public void setOffset(Point point) {
        this.hvScroll.setOffset(point);
    }

    public Point getOffset() {
        return this.hvScroll.getOffset();
    }

    public RotateScroll getRotateScroll() {
        return this.rotateScroll;
    }

    public void setRotationAngle(int n) {
        this.rotateScroll.setRotationAngle(n);
    }

    public int getRotationAngle() {
        return this.rotateScroll.getRotationAngle();
    }

    public LocalityScroll getLocalityScroll() {
        return this.localityScroll;
    }

    public void setLocalityRadius(int n) {
        this.localityScroll.setLocalityRadius(n);
    }

    public int getLocalityRadius() {
        return this.localityScroll.getLocalityRadius();
    }

    public ZoomScroll getZoomScroll() {
        return this.zoomScroll;
    }

    public void setZoomValue(int n) {
        this.zoomScroll.setZoomValue(n);
    }

    public int getZoomValue() {
        return this.zoomScroll.getZoomValue();
    }

    public PopupMenu getGLPopup() {
        return this.glPopup;
    }

    public void buildLens() {
        this.tgLensSet.addLens(this.hvScroll.getLens());
        this.tgLensSet.addLens(this.zoomScroll.getLens());
        this.tgLensSet.addLens(this.hyperScroll.getLens());
        this.tgLensSet.addLens(this.rotateScroll.getLens());
        this.tgLensSet.addLens(this.tgPanel.getAdjustOriginLens());
    }

    public void buildPanel() {
        final Scrollbar scrollbar = this.hvScroll.getHorizontalSB();
        final Scrollbar scrollbar2 = this.hvScroll.getVerticalSB();
        Scrollbar scrollbar3 = this.zoomScroll.getZoomSB();
        Scrollbar scrollbar4 = this.rotateScroll.getRotateSB();
        Scrollbar scrollbar5 = this.localityScroll.getLocalitySB();
        Scrollbar scrollbar6 = this.hyperScroll.getHyperSB();
        this.setLayout(new BorderLayout());
        Panel panel = new Panel();
        panel.setBackground(this.defaultBackColor);
        panel.setForeground(this.defaultForeColor);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        Panel panel2 = new Panel();
        panel2.setBackground(this.defaultBackColor);
        panel2.setForeground(this.defaultForeColor);
        panel2.setLayout(new FlowLayout(1, 0, 0));
        final Panel panel3 = new Panel();
        panel3.setBackground(this.defaultBorderBackColor);
        panel3.setForeground(this.defaultForeColor);
        panel3.setLayout(new GridBagLayout());
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        this.scrollBarHash.put(this.zoomLabel, scrollbar3);
        this.scrollBarHash.put(this.rotateLabel, scrollbar4);
        this.scrollBarHash.put(this.localityLabel, scrollbar5);
        this.scrollBarHash.put(this.hyperLabel, scrollbar6);
        Panel panel4 = this.scrollSelectPanel(new String[]{this.zoomLabel, this.rotateLabel, this.hyperLabel});
        panel4.setBackground(this.defaultBorderBackColor);
        panel4.setForeground(this.defaultForeColor);
        panel3.add((Component)panel4, gridBagConstraints);
        this.add((Component)panel3, "South");
        gridBagConstraints.fill = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panel.add((Component)this.tgPanel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        this.add((Component)panel, "Center");
        this.glPopup = new PopupMenu();
        this.add(this.glPopup);
        MenuItem menuItem = new MenuItem("Toggle Controls");
        ActionListener actionListener = new ActionListener(){
            boolean controlsVisible;

            public void actionPerformed(ActionEvent actionEvent) {
                this.controlsVisible = !this.controlsVisible;
                scrollbar.setVisible(this.controlsVisible);
                scrollbar2.setVisible(this.controlsVisible);
                panel3.setVisible(this.controlsVisible);
                GLPanel.this.doLayout();
            }
        };
        menuItem.addActionListener(actionListener);
        this.glPopup.add(menuItem);
    }

    protected Panel scrollSelectPanel(String[] arrstring) {
        Panel panel = new Panel(new GridBagLayout());
        panel.setBackground(this.defaultBorderBackColor);
        panel.setForeground(this.defaultForeColor);
        Panel panel2 = new Panel(new GridBagLayout());
        CheckboxGroup checkboxGroup = new CheckboxGroup();
        int n = arrstring.length;
        Checkbox[] arrcheckbox = new Checkbox[n];
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = 17;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.fill = 2;
        int n2 = 0;
        while (n2 < n) {
            arrcheckbox[n2] = new Checkbox(arrstring[n2], true, checkboxGroup);
            gridBagConstraints.gridx = n2;
            panel2.add((Component)arrcheckbox[n2], gridBagConstraints);
            ++n2;
        }
        arrcheckbox[0].setState(true);
        gridBagConstraints.gridx = n;
        gridBagConstraints.weightx = 1.0;
        Label label = new Label("     Right-click nodes and background for more options");
        panel2.add((Component)label, gridBagConstraints);
        int n3 = 0;
        while (n3 < n) {
            class RadioItemListener
            implements ItemListener {
                private String scrollBarName;
                private final /* synthetic */ CheckboxGroup val$bg;
                private final /* synthetic */ String[] val$scrollBarNames;
                private final /* synthetic */ Panel val$sbp;
                private final /* synthetic */ GLPanel this$0;

                public RadioItemListener(GLPanel gLPanel, CheckboxGroup checkboxGroup, String[] arrstring, Panel panel, String string) {
                    this.this$0 = gLPanel;
                    this.val$bg = checkboxGroup;
                    this.val$scrollBarNames = arrstring;
                    this.val$sbp = panel;
                    this.scrollBarName = string;
                }

                public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
                    Scrollbar scrollbar = (Scrollbar)this.this$0.scrollBarHash.get(this.val$bg.getSelectedCheckbox().getLabel());
                    if (itemEvent.getStateChange() == 1) {
                        int n = 0;
                        while (n < this.val$scrollBarNames.length) {
                            Scrollbar scrollbar2 = (Scrollbar)this.this$0.scrollBarHash.get(this.val$scrollBarNames[n]);
                            scrollbar2.setVisible(false);
                            ++n;
                        }
                        scrollbar.setBounds(GLPanel.access$000(this.this$0).getBounds());
                        if (scrollbar != null) {
                            scrollbar.setVisible(true);
                        }
                        GLPanel.access$002(this.this$0, scrollbar);
                        this.val$sbp.invalidate();
                    }
                }
            }
            arrcheckbox[n3].addItemListener(new RadioItemListener(this, checkboxGroup, arrstring, panel, arrstring[0]));
            ++n3;
        }
        gridBagConstraints.anchor = 18;
        gridBagConstraints.insets = new Insets(1, 5, 1, 5);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.fill = 0;
        gridBagConstraints.anchor = 17;
        panel.add((Component)panel2, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = 2;
        int n4 = 0;
        while (n4 < arrstring.length) {
            Scrollbar scrollbar = (Scrollbar)this.scrollBarHash.get(arrstring[n4]);
            if (scrollbar != null) {
                if (this.currentSB == null) {
                    this.currentSB = scrollbar;
                }
                panel.add((Component)scrollbar, gridBagConstraints);
            }
            ++n4;
        }
        return panel;
    }

    public void addUIs() {
        this.tgUIManager = new TGUIManager();
        GLEditUI gLEditUI = new GLEditUI(this);
        GLNavigateUI gLNavigateUI = new GLNavigateUI(this);
        this.tgUIManager.addUI(gLEditUI, "Edit");
        this.tgUIManager.addUI(gLNavigateUI, "Navigate");
        this.tgUIManager.activate("Navigate");
    }

    public void randomGraph() throws TGException {
        Node node = this.tgPanel.addNode();
        node.setType(0);
        int n = 0;
        while (n < 249) {
            this.tgPanel.addNode();
            ++n;
        }
        TGForEachNode tGForEachNode = new TGForEachNode(){

            public void forEachNode(Node node) {
                int n = 0;
                while (n < 5) {
                    Node node2 = GLPanel.this.tgPanel.getGES().getRandomNode();
                    if (node2 != node && GLPanel.this.tgPanel.findEdge(node2, node) == null) {
                        GLPanel.this.tgPanel.addEdge(node2, node, Edge.DEFAULT_LENGTH);
                    }
                    ++n;
                }
            }
        };
        this.tgPanel.getGES().forAllNodes(tGForEachNode);
        this.tgPanel.setLocale(node, 1);
        this.tgPanel.setSelect(node);
        try {
            Thread.currentThread();
            Thread.sleep(2000);
        }
        catch (InterruptedException var4_4) {
            // empty catch block
        }
        this.getHVScroll().slowScrollToCenter(node);
    }

    public static void main(String[] arrstring) {
        final GLPanel gLPanel = new GLPanel();
        final Frame frame = new Frame("TouchGraph GraphLayout");
        frame.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                frame.remove(gLPanel);
                frame.dispose();
            }
        });
        frame.add("Center", gLPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    static /* synthetic */ Scrollbar access$000(GLPanel gLPanel) {
        return gLPanel.currentSB;
    }

    static /* synthetic */ Scrollbar access$002(GLPanel gLPanel, Scrollbar scrollbar) {
        gLPanel.currentSB = scrollbar;
        return gLPanel.currentSB;
    }

}

