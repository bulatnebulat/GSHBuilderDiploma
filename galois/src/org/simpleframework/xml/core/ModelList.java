/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import org.simpleframework.xml.core.Model;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ModelList
extends ArrayList<Model> {
    public ModelList build() {
        ModelList list = new ModelList();
        for (Model model : this) {
            list.register(model);
        }
        return list;
    }

    @Override
    public boolean isEmpty() {
        for (Model model : this) {
            if (model == null || model.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public Model lookup(int index) {
        int size = this.size();
        if (index <= size) {
            return (Model)this.get(index - 1);
        }
        return null;
    }

    public void register(Model model) {
        int index = model.getIndex();
        int size = this.size();
        for (int i = 0; i < index; ++i) {
            if (i >= size) {
                this.add(null);
            }
            if (i != index - 1) continue;
            this.set(index - 1, model);
        }
    }

    public Model take() {
        while (!this.isEmpty()) {
            Model model = (Model)this.remove(0);
            if (model.isEmpty()) continue;
            return model;
        }
        return null;
    }
}

