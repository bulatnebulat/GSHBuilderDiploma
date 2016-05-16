/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Array;
import org.simpleframework.xml.core.ArrayInstance;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Factory;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.Position;

class ArrayFactory
extends Factory {
    public ArrayFactory(Context context, Type type) {
        super(context, type);
    }

    public Object getInstance() throws Exception {
        Class type = this.getComponentType();
        if (type != null) {
            return Array.newInstance(type, 0);
        }
        return null;
    }

    public Instance getInstance(InputNode node) throws Exception {
        Position line = node.getPosition();
        Value value = this.getOverride(node);
        if (value == null) {
            throw new ElementException("Array length required for %s at %s", this.type, line);
        }
        Class type = value.getType();
        return this.getInstance(value, type);
    }

    private Instance getInstance(Value value, Class entry) throws Exception {
        Class expect = this.getComponentType();
        if (!expect.isAssignableFrom(entry)) {
            throw new InstantiationException("Array of type %s cannot hold %s for %s", expect, entry, this.type);
        }
        return new ArrayInstance(value);
    }

    private Class getComponentType() throws Exception {
        Class expect = this.getType();
        if (!expect.isArray()) {
            throw new InstantiationException("The %s not an array for %s", expect, this.type);
        }
        return expect.getComponentType();
    }
}

