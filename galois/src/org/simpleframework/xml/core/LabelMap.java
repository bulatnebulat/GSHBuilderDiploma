/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Policy;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class LabelMap
extends LinkedHashMap<String, Label>
implements Iterable<Label> {
    private final Policy policy;

    public LabelMap() {
        this(null);
    }

    public LabelMap(Policy policy) {
        this.policy = policy;
    }

    @Override
    public Iterator<Label> iterator() {
        return this.values().iterator();
    }

    public Label getLabel(String name) {
        return (Label)this.remove(name);
    }

    public Set<String> getKeys() throws Exception {
        HashSet<String> list = new HashSet<String>();
        for (Label label : this) {
            if (label == null) continue;
            String path = label.getPath();
            String name = label.getName();
            list.add(path);
            list.add(name);
        }
        return list;
    }

    public Set<String> getKeys(Context context) throws Exception {
        HashSet<String> list = new HashSet<String>();
        for (Label label : this) {
            if (label == null) continue;
            String path = label.getPath(context);
            String name = label.getName(context);
            list.add(path);
            list.add(name);
        }
        return list;
    }

    public Set<String> getPaths() throws Exception {
        HashSet<String> list = new HashSet<String>();
        for (Label label : this) {
            if (label == null) continue;
            String path = label.getPath();
            list.add(path);
        }
        return list;
    }

    public Set<String> getPaths(Context context) throws Exception {
        HashSet<String> list = new HashSet<String>();
        for (Label label : this) {
            if (label == null) continue;
            String path = label.getPath(context);
            list.add(path);
        }
        return list;
    }

    public LabelMap getLabels(Context context) throws Exception {
        LabelMap map = new LabelMap(this.policy);
        for (Label label : this) {
            if (label == null) continue;
            String name = label.getPath(context);
            map.put(name, label);
        }
        return map;
    }

    public boolean isStrict(Context context) {
        if (this.policy == null) {
            return context.isStrict();
        }
        return context.isStrict() && this.policy.isStrict();
    }
}

