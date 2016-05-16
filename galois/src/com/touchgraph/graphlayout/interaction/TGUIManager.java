/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.interaction.TGUserInterface;
import java.util.Vector;

public class TGUIManager {
    Vector userInterfaces = new Vector();

    public void addUI(TGUserInterface tGUserInterface, String string) {
        this.userInterfaces.addElement(new NamedUI(tGUserInterface, string));
    }

    public void addUI(TGUserInterface tGUserInterface) {
        this.addUI(tGUserInterface, null);
    }

    public void removeUI(String string) {
        int n = 0;
        while (n < this.userInterfaces.size()) {
            if (((NamedUI)this.userInterfaces.elementAt((int)n)).name.equals(string)) {
                this.userInterfaces.removeElementAt(n);
            }
            ++n;
        }
    }

    public void removeUI(TGUserInterface tGUserInterface) {
        int n = 0;
        while (n < this.userInterfaces.size()) {
            if (((NamedUI)this.userInterfaces.elementAt((int)n)).ui == tGUserInterface) {
                this.userInterfaces.removeElementAt(n);
            }
            ++n;
        }
    }

    public void activate(String string) {
        int n = 0;
        while (n < this.userInterfaces.size()) {
            NamedUI namedUI = (NamedUI)this.userInterfaces.elementAt(n);
            TGUserInterface tGUserInterface = namedUI.ui;
            if (((NamedUI)this.userInterfaces.elementAt((int)n)).name.equals(string)) {
                tGUserInterface.activate();
            } else {
                tGUserInterface.deactivate();
            }
            ++n;
        }
    }

    public void activate(TGUserInterface tGUserInterface) {
        int n = 0;
        while (n < this.userInterfaces.size()) {
            if (((NamedUI)this.userInterfaces.elementAt((int)n)).ui == tGUserInterface) {
                tGUserInterface.activate();
            } else {
                tGUserInterface.deactivate();
            }
            ++n;
        }
    }

    class NamedUI {
        TGUserInterface ui;
        String name;

        NamedUI(TGUserInterface tGUserInterface, String string) {
            this.ui = tGUserInterface;
            this.name = string;
        }
    }

}

