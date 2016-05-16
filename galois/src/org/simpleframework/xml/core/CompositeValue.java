/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Entry;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Traverser;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

class CompositeValue
implements Converter {
    private final Context context;
    private final Traverser root;
    private final Style style;
    private final Entry entry;
    private final Type type;

    public CompositeValue(Context context, Entry entry, Type type) throws Exception {
        this.root = new Traverser(context);
        this.style = context.getStyle();
        this.context = context;
        this.entry = entry;
        this.type = type;
    }

    public Object read(InputNode node) throws Exception {
        InputNode next = node.getNext();
        Class expect = this.type.getType();
        if (next == null) {
            return null;
        }
        if (next.isEmpty()) {
            return null;
        }
        return this.root.read(next, expect);
    }

    public Object read(InputNode node, Object value) throws Exception {
        Class expect = this.type.getType();
        if (value != null) {
            throw new PersistenceException("Can not read value of %s for %s", expect, this.entry);
        }
        return this.read(node);
    }

    public boolean validate(InputNode node) throws Exception {
        Class expect = this.type.getType();
        String name = this.entry.getValue();
        if (name == null) {
            name = this.context.getName(expect);
        }
        return this.validate(node, name);
    }

    private boolean validate(InputNode node, String key) throws Exception {
        String name = this.style.getElement(key);
        InputNode next = node.getNext(name);
        Class expect = this.type.getType();
        if (next == null) {
            return true;
        }
        if (next.isEmpty()) {
            return true;
        }
        return this.root.validate(next, expect);
    }

    public void write(OutputNode node, Object item) throws Exception {
        Class expect = this.type.getType();
        String key = this.entry.getValue();
        if (key == null) {
            key = this.context.getName(expect);
        }
        String name = this.style.getElement(key);
        this.root.write(node, item, expect, name);
    }
}

