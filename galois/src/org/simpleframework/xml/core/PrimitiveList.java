/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import org.simpleframework.xml.core.CollectionFactory;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class PrimitiveList
implements Converter {
    private final CollectionFactory factory;
    private final Primitive root;
    private final String parent;
    private final Type entry;

    public PrimitiveList(Context context, Type type, Type entry, String parent) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Primitive(context, entry);
        this.parent = parent;
        this.entry = entry;
    }

    public Object read(InputNode node) throws Exception {
        Instance type = this.factory.getInstance(node);
        Object list = type.getInstance();
        if (!type.isReference()) {
            return this.populate(node, list);
        }
        return list;
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
        Collection list = (Collection)result;
        InputNode next;
        while ((next = node.getNext()) != null) {
            list.add(this.root.read(next));
        }
        return list;
    }

    public boolean validate(InputNode node) throws Exception {
        Instance value = this.factory.getInstance(node);
        if (!value.isReference()) {
            Object result = value.setInstance(null);
            Class expect = value.getType();
            return this.validate(node, expect);
        }
        return true;
    }

    private boolean validate(InputNode node, Class type) throws Exception {
        InputNode next;
        while ((next = node.getNext()) != null) {
            this.root.validate(next);
        }
        return true;
    }

    public void write(OutputNode node, Object source) throws Exception {
        Collection list = (Collection)source;
        for (Object item : list) {
            OutputNode child;
            if (item == null || this.isOverridden(child = node.getChild(this.parent), item)) continue;
            this.root.write(child, item);
        }
    }

    private boolean isOverridden(OutputNode node, Object value) throws Exception {
        return this.factory.setOverride(this.entry, value, node);
    }
}

