/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

public abstract class TGUserInterface {
    private TGUserInterface parentUI;
    boolean active;

    public abstract void activate();

    public boolean isActive() {
        return this.active;
    }

    public void activate(TGUserInterface tGUserInterface) {
        this.parentUI = tGUserInterface;
        this.parentUI.deactivate();
        this.activate();
    }

    public void deactivate() {
        if (this.parentUI != null) {
            this.parentUI.activate();
        }
        this.parentUI = null;
    }
}

