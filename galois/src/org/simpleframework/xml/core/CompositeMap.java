/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Map;
import java.util.Set;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Entry;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.MapFactory;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

class CompositeMap
implements Converter {
    private final MapFactory factory;
    private final Converter value;
    private final Converter key;
    private final Style style;
    private final Entry entry;

    public CompositeMap(Context context, Entry entry, Type type) throws Exception {
        this.factory = new MapFactory(context, type);
        this.value = entry.getValue(context);
        this.key = entry.getKey(context);
        this.style = context.getStyle();
        this.entry = entry;
    }

    public Object read(InputNode node) throws Exception {
        Instance type = this.factory.getInstance(node);
        Object map = type.getInstance();
        if (!type.isReference()) {
            return this.populate(node, map);
        }
        return map;
    }

    public Object read(InputNode node, Object result) throws Exception {
        Instance type = this.factory.getInstance(node);
        if (type.isReference()) {
            return type.getInstance();
        }
        type.setInstance(result);
        if (result != null) {
            return this.populate(node, result);
        }
        return result;
    }

    private Object populate(InputNode node, Object result) throws Exception {
        Map map = (Map)result;
        InputNode next;
        while ((next = node.getNext()) != null) {
            Object index = this.key.read(next);
            Object item = this.value.read(next);
            map.put(index, item);
        }
        return map;
    }

    public boolean validate(InputNode node) throws Exception {
        Instance value = this.factory.getInstance(node);
        if (!value.isReference()) {
            Object result = value.setInstance(null);
            Class type = value.getType();
            return this.validate(node, type);
        }
        return true;
    }

    private boolean validate(InputNode node, Class type) throws Exception {
        InputNode next;
        do {
            if ((next = node.getNext()) == null) {
                return true;
            }
            if (this.key.validate(next)) continue;
            return false;
        } while (this.value.validate(next));
        return false;
    }

    public void write(OutputNode node, Object source) throws Exception {
        Map map = (Map)source;
        for (Object index : map.keySet()) {
            String root = this.entry.getEntry();
            String name = this.style.getElement(root);
            OutputNode next = node.getChild(name);
            Object item = map.get(index);
            this.key.write(next, index);
            this.value.write(next, item);
        }
    }
}

