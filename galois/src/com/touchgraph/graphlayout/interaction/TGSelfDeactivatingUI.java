/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.interaction.TGUserInterface;

public abstract class TGSelfDeactivatingUI
extends TGUserInterface {
    boolean selfDeactivate = true;

    public void setSelfDeactivate(boolean bl) {
        this.selfDeactivate = bl;
    }
}

