/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import org.simpleframework.xml.core.CollectionFactory;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Traverser;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class CompositeList
implements Converter {
    private final CollectionFactory factory;
    private final Traverser root;
    private final String name;
    private final Type entry;
    private final Type type;

    public CompositeList(Context context, Type type, Type entry, String name) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Traverser(context);
        this.entry = entry;
        this.type = type;
        this.name = name;
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
        do {
            InputNode next = node.getNext();
            Class expect = this.entry.getType();
            if (next == null) {
                return list;
            }
            list.add(this.root.read(next, expect));
        } while (true);
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
        do {
            InputNode next = node.getNext();
            Class expect = this.entry.getType();
            if (next == null) {
                return true;
            }
            this.root.validate(next, expect);
        } while (true);
    }

    public void write(OutputNode node, Object source) throws Exception {
        Collection list = (Collection)source;
        for (Object item : list) {
            Class actual;
            if (item == null) continue;
            Class expect = this.entry.getType();
            if (!expect.isAssignableFrom(actual = item.getClass())) {
                throw new PersistenceException("Entry %s does not match %s for %s", actual, this.entry, this.type);
            }
            this.root.write(node, item, expect, this.name);
        }
    }
}

