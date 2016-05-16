/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.ModelList;
import org.simpleframework.xml.core.PathException;
import org.simpleframework.xml.stream.Style;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ModelMap
extends LinkedHashMap<String, ModelList>
implements Iterable<ModelList> {
    private final Class type;

    public ModelMap(Class type) {
        this.type = type;
    }

    public Model lookup(String name, int index) {
        ModelList list = (ModelList)this.get(name);
        if (list != null) {
            return list.lookup(index);
        }
        return null;
    }

    public void register(String name, Model model) {
        ModelList list = (ModelList)this.get(name);
        if (list == null) {
            list = new ModelList();
            this.put(name, list);
        }
        list.register(model);
    }

    @Override
    public Iterator<ModelList> iterator() {
        return this.values().iterator();
    }

    public ModelMap getModels(Context context) throws Exception {
        Style style = context.getStyle();
        if (style != null) {
            return this.getModels(style);
        }
        return this;
    }

    private ModelMap getModels(Style style) throws Exception {
        ModelMap map = new ModelMap(this.type);
        for (String element : this.keySet()) {
            ModelList list = (ModelList)this.get(element);
            String name = style.getElement(element);
            if (list != null) {
                list = list.build();
            }
            if (map.containsKey(name)) {
                throw new PathException("Path with name '%s' is a duplicate of '%s' in %s ", element, name, this.type);
            }
            map.put(name, list);
        }
        return map;
    }
}

