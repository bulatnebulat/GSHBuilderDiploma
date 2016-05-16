/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.ConversionInstance;
import org.simpleframework.xml.core.Factory;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;

class MapFactory
extends Factory {
    public MapFactory(Context context, Type type) {
        super(context, type);
    }

    public Object getInstance() throws Exception {
        Class expect = this.getType();
        Class real = expect;
        if (!MapFactory.isInstantiable(real)) {
            real = this.getConversion(expect);
        }
        if (!this.isMap(real)) {
            throw new InstantiationException("Invalid map %s for %s", expect, this.type);
        }
        return real.newInstance();
    }

    public Instance getInstance(InputNode node) throws Exception {
        Value value = this.getOverride(node);
        Class expect = this.getType();
        if (value != null) {
            return this.getInstance(value);
        }
        if (!MapFactory.isInstantiable(expect)) {
            expect = this.getConversion(expect);
        }
        if (!this.isMap(expect)) {
            throw new InstantiationException("Invalid map %s for %s", expect, this.type);
        }
        return this.context.getInstance(expect);
    }

    public Instance getInstance(Value value) throws Exception {
        Class expect = value.getType();
        if (!MapFactory.isInstantiable(expect)) {
            expect = this.getConversion(expect);
        }
        if (!this.isMap(expect)) {
            throw new InstantiationException("Invalid map %s for %s", expect, this.type);
        }
        return new ConversionInstance(this.context, value, expect);
    }

    public Class getConversion(Class require) throws Exception {
        if (require.isAssignableFrom(HashMap.class)) {
            return HashMap.class;
        }
        if (require.isAssignableFrom(TreeMap.class)) {
            return TreeMap.class;
        }
        throw new InstantiationException("Cannot instantiate %s for %s", require, this.type);
    }

    private boolean isMap(Class type) {
        return Map.class.isAssignableFrom(type);
    }
}

