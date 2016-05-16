/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.ConversionInstance;
import org.simpleframework.xml.core.Factory;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;

class CollectionFactory
extends Factory {
    public CollectionFactory(Context context, Type type) {
        super(context, type);
    }

    public Object getInstance() throws Exception {
        Class expect = this.getType();
        Class real = expect;
        if (!CollectionFactory.isInstantiable(real)) {
            real = this.getConversion(expect);
        }
        if (!this.isCollection(real)) {
            throw new InstantiationException("Invalid collection %s for %s", expect, this.type);
        }
        return real.newInstance();
    }

    public Instance getInstance(InputNode node) throws Exception {
        Value value = this.getOverride(node);
        Class expect = this.getType();
        if (value != null) {
            return this.getInstance(value);
        }
        if (!CollectionFactory.isInstantiable(expect)) {
            expect = this.getConversion(expect);
        }
        if (!this.isCollection(expect)) {
            throw new InstantiationException("Invalid collection %s for %s", expect, this.type);
        }
        return this.context.getInstance(expect);
    }

    public Instance getInstance(Value value) throws Exception {
        Class expect = value.getType();
        if (!CollectionFactory.isInstantiable(expect)) {
            expect = this.getConversion(expect);
        }
        if (!this.isCollection(expect)) {
            throw new InstantiationException("Invalid collection %s for %s", expect, this.type);
        }
        return new ConversionInstance(this.context, value, expect);
    }

    public Class getConversion(Class require) throws Exception {
        if (require.isAssignableFrom(ArrayList.class)) {
            return ArrayList.class;
        }
        if (require.isAssignableFrom(HashSet.class)) {
            return HashSet.class;
        }
        if (require.isAssignableFrom(TreeSet.class)) {
            return TreeSet.class;
        }
        throw new InstantiationException("Cannot instantiate %s for %s", require, this.type);
    }

    private boolean isCollection(Class type) {
        return Collection.class.isAssignableFrom(type);
    }
}

