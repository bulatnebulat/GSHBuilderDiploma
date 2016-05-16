/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.GLPanel;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.PrintStream;

public class GraphLayoutApplet
extends Applet {
    public String getAppletInfo() {
        String string = "";
        string = string + "***************************************************************************\n";
        string = string + "*   TouchGraph GraphLayout                                                *\n";
        string = string + "*   (c) 2001-2002 TouchGraph LLC                                          *\n";
        string = string + "*   Author: Alexander Shapiro     Email: alex@touchgraph.com              *\n";
        string = string + "***************************************************************************\n";
        return string;
    }

    public void init() {
        System.out.println(this.getAppletInfo());
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.add((Component)new GLPanel(), "Center");
    }
}

