/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Entry;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.core.PrimitiveFactory;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

class PrimitiveKey
implements Converter {
    private final PrimitiveFactory factory;
    private final Context context;
    private final Primitive root;
    private final Style style;
    private final Entry entry;
    private final Type type;

    public PrimitiveKey(Context context, Entry entry, Type type) {
        this.factory = new PrimitiveFactory(context, type);
        this.root = new Primitive(context, type);
        this.style = context.getStyle();
        this.context = context;
        this.entry = entry;
        this.type = type;
    }

    public Object read(InputNode node) throws Exception {
        Class expect = this.type.getType();
        String name = this.entry.getKey();
        if (name == null) {
            name = this.context.getName(expect);
        }
        if (!this.entry.isAttribute()) {
            return this.readElement(node, name);
        }
        return this.readAttribute(node, name);
    }

    public Object read(InputNode node, Object value) throws Exception {
        Class expect = this.type.getType();
        if (value != null) {
            throw new PersistenceException("Can not read key of %s for %s", expect, this.entry);
        }
        return this.read(node);
    }

    private Object readAttribute(InputNode node, String key) throws Exception {
        String name = this.style.getAttribute(key);
        InputNode child = node.getAttribute(name);
        if (child == null) {
            return null;
        }
        return this.root.read(child);
    }

    private Object readElement(InputNode node, String key) throws Exception {
        String name = this.style.getElement(key);
        InputNode child = node.getNext(name);
        if (child == null) {
            return null;
        }
        return this.root.read(child);
    }

    public boolean validate(InputNode node) throws Exception {
        Class expect = this.type.getType();
        String name = this.entry.getKey();
        if (name == null) {
            name = this.context.getName(expect);
        }
        if (!this.entry.isAttribute()) {
            return this.validateElement(node, name);
        }
        return this.validateAttribute(node, name);
    }

    private boolean validateAttribute(InputNode node, String key) throws Exception {
        String name = this.style.getElement(key);
        InputNode child = node.getAttribute(name);
        if (child == null) {
            return true;
        }
        return this.root.validate(child);
    }

    private boolean validateElement(InputNode node, String key) throws Exception {
        String name = this.style.getElement(key);
        InputNode child = node.getNext(name);
        if (child == null) {
            return true;
        }
        return this.root.validate(child);
    }

    public void write(OutputNode node, Object item) throws Exception {
        if (!this.entry.isAttribute()) {
            this.writeElement(node, item);
        } else if (item != null) {
            this.writeAttribute(node, item);
        }
    }

    private void writeElement(OutputNode node, Object item) throws Exception {
        Class expect = this.type.getType();
        String key = this.entry.getKey();
        if (key == null) {
            key = this.context.getName(expect);
        }
        String name = this.style.getElement(key);
        OutputNode child = node.getChild(name);
        if (item != null && !this.isOverridden(child, item)) {
            this.root.write(child, item);
        }
    }

    private void writeAttribute(OutputNode node, Object item) throws Exception {
        Class expect = this.type.getType();
        String text = this.factory.getText(item);
        String key = this.entry.getKey();
        if (key == null) {
            key = this.context.getName(expect);
        }
        String name = this.style.getAttribute(key);
        if (text != null) {
            node.setAttribute(name, text);
        }
    }

    private boolean isOverridden(OutputNode node, Object value) throws Exception {
        return this.factory.setOverride(this.type, value, node);
    }
}

