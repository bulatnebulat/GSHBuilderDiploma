/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Array;
import org.simpleframework.xml.core.ArrayFactory;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Traverser;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;

class CompositeArray
implements Converter {
    private final ArrayFactory factory;
    private final Traverser root;
    private final String parent;
    private final Type entry;
    private final Type type;

    public CompositeArray(Context context, Type type, Type entry, String parent) {
        this.factory = new ArrayFactory(context, type);
        this.root = new Traverser(context);
        this.parent = parent;
        this.entry = entry;
        this.type = type;
    }

    public Object read(InputNode node) throws Exception {
        Instance type = this.factory.getInstance(node);
        Object list = type.getInstance();
        if (!type.isReference()) {
            return this.read(node, list);
        }
        return list;
    }

    public Object read(InputNode node, Object list) throws Exception {
        int length = Array.getLength(list);
        int pos = 0;
        do {
            Position line = node.getPosition();
            InputNode next = node.getNext();
            if (next == null) {
                return list;
            }
            if (pos >= length) {
                throw new ElementException("Array length missing or incorrect for %s at %s", this.type, line);
            }
            this.read(next, list, pos);
            ++pos;
        } while (true);
    }

    private void read(InputNode node, Object list, int index) throws Exception {
        Class type = this.entry.getType();
        Object value = null;
        if (!node.isEmpty()) {
            value = this.root.read(node, type);
        }
        Array.set(list, index, value);
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
        int i = 0;
        InputNode next;
        while ((next = node.getNext()) != null) {
            if (!next.isEmpty()) {
                this.root.validate(next, type);
            }
            ++i;
        }
        return true;
    }

    public void write(OutputNode node, Object source) throws Exception {
        int size = Array.getLength(source);
        for (int i = 0; i < size; ++i) {
            Object item = Array.get(source, i);
            Class type = this.entry.getType();
            this.root.write(node, item, type, this.parent);
        }
        node.commit();
    }
}

