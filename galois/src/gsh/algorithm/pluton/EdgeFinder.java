/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm.pluton;

import gsh.algorithm.pluton.SetType;
import gsh.algorithm.pluton.ToGSH;
import gsh.types.Concept;
import java.util.List;
import java.util.Map;
import jcornflower.matrix.Double2D;

public class EdgeFinder
implements Runnable {
    private volatile boolean stop = false;
    private final ToGSH togsh;

    public EdgeFinder(ToGSH togsh) {
        this.togsh = togsh;
        this.run();
    }

    private ToGSH getToGSH() {
        return this.togsh;
    }

    public Double2D getContext() {
        return this.getToGSH().getContext();
    }

    public List<Concept> getCsets() {
        return this.togsh.getCsets();
    }

    public boolean isExtendedNames() {
        return this.getToGSH().isExtendedNames();
    }

    public void requestStop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!this.stop) {
            Concept cset = this.getToGSH().nextCset();
            if (cset == null) break;
            int i = this.getCsets().indexOf(cset);
            Concept item = ToGSH.getNormalized(this.getContext(), cset, this.isExtendedNames());
            this.getToGSH().makeConnectable(item);
            if (i + 1 >= this.getCsets().size()) continue;
            int j = i + 1;
            while (j < this.getCsets().size()) {
                Concept item2 = ToGSH.getNormalized(this.getContext(), this.getCsets().get(j), this.isExtendedNames());
                SetType pt = ToGSH.getParentType(this.getContext(), item, item2);
                if (pt == SetType.extent && this.getToGSH().getConnections().containsKey(item)) {
                    boolean perm = false;
                    List<Concept> snapshot = this.getToGSH().getConnections().get(item);
                    for (Concept p : snapshot) {
                        SetType pt2 = ToGSH.getParentType(this.getContext(), p, item2);
                        if (pt2 != SetType.extent) continue;
                        perm = true;
                        break;
                    }
                    if (!perm) {
                        this.getToGSH().addEdge(item, item2);
                    }
                }
                ++j;
            }
        }
    }
}

