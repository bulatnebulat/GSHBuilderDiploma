/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.core.ObjectInstance;
import org.simpleframework.xml.core.PrimitiveFactory;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;

class ObjectFactory
extends PrimitiveFactory {
    public ObjectFactory(Context context, Type type, Class override) {
        super(context, type, override);
    }

    public Instance getInstance(InputNode node) throws Exception {
        Value value = this.getOverride(node);
        Class expect = this.getType();
        if (value == null) {
            if (!ObjectFactory.isInstantiable(expect)) {
                throw new InstantiationException("Cannot instantiate %s for %s", expect, this.type);
            }
            return this.context.getInstance(expect);
        }
        return new ObjectInstance(this.context, value);
    }
}

