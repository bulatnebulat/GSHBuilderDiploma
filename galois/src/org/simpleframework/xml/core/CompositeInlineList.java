/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import org.simpleframework.xml.core.CollectionFactory;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Repeater;
import org.simpleframework.xml.core.Traverser;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class CompositeInlineList
implements Repeater {
    private final CollectionFactory factory;
    private final Traverser root;
    private final String name;
    private final Type entry;
    private final Type type;

    public CompositeInlineList(Context context, Type type, Type entry, String name) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Traverser(context);
        this.entry = entry;
        this.type = type;
        this.name = name;
    }

    public Object read(InputNode node) throws Exception {
        Object value = this.factory.getInstance();
        Collection list = (Collection)value;
        if (list != null) {
            return this.read(node, list);
        }
        return null;
    }

    public Object read(InputNode node, Object value) throws Exception {
        Collection list = (Collection)value;
        if (list != null) {
            return this.read(node, list);
        }
        return this.read(node);
    }

    private Object read(InputNode node, Collection list) throws Exception {
        InputNode from = node.getParent();
        String name = node.getName();
        while (node != null) {
            Class type = this.entry.getType();
            Object item = this.read(node, type);
            if (item != null) {
                list.add(item);
            }
            node = from.getNext(name);
        }
        return list;
    }

    private Object read(InputNode node, Class expect) throws Exception {
        Object item = this.root.read(node, expect);
        Class result = item.getClass();
        Class actual = this.entry.getType();
        if (!actual.isAssignableFrom(result)) {
            throw new PersistenceException("Entry %s does not match %s for %s", result, this.entry, this.type);
        }
        return item;
    }

    public boolean validate(InputNode node) throws Exception {
        InputNode from = node.getParent();
        Class type = this.entry.getType();
        String name = node.getName();
        while (node != null) {
            boolean valid = this.root.validate(node, type);
            if (!valid) {
                return false;
            }
            node = from.getNext(name);
        }
        return true;
    }

    public void write(OutputNode node, Object source) throws Exception {
        Collection list = (Collection)source;
        OutputNode parent = node.getParent();
        if (!node.isCommitted()) {
            node.remove();
        }
        this.write(parent, list);
    }

    public void write(OutputNode node, Collection list) throws Exception {
        for (Object item : list) {
            Class actual;
            if (item == null) continue;
            Class expect = this.entry.getType();
            if (!expect.isAssignableFrom(actual = item.getClass())) {
                throw new PersistenceException("Entry %s does not match %s for %s", actual, expect, this.type);
            }
            this.root.write(node, item, expect, this.name);
        }
    }
}

