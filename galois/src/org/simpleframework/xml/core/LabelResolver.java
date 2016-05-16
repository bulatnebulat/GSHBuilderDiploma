/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.Parameter;

class LabelResolver {
    private final LabelMap attributes = new LabelMap();
    private final LabelMap elements = new LabelMap();
    private final LabelMap texts = new LabelMap();

    public void register(Label label) throws Exception {
        if (label.isAttribute()) {
            this.register(label, this.attributes);
        } else if (label.isText()) {
            this.register(label, this.texts);
        } else {
            this.register(label, this.elements);
        }
    }

    private void register(Label label, LabelMap map) throws Exception {
        String name = label.getName();
        String path = label.getPath();
        if (map.containsKey(name)) {
            map.put(name, null);
        } else {
            map.put(name, label);
        }
        map.put(path, label);
    }

    public Label resolve(Parameter parameter) throws Exception {
        if (parameter.isAttribute()) {
            return this.resolve(parameter, this.attributes);
        }
        if (parameter.isText()) {
            return this.resolve(parameter, this.texts);
        }
        return this.resolve(parameter, this.elements);
    }

    private Label resolve(Parameter parameter, LabelMap map) throws Exception {
        String name = parameter.getName();
        String path = parameter.getPath();
        Label label = (Label)map.get(path);
        if (label == null) {
            return (Label)map.get(name);
        }
        return label;
    }
}

