/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;

class OverrideValue
implements Value {
    private final Value value;
    private final Class type;

    public OverrideValue(Value value, Class type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return this.value.getValue();
    }

    public void setValue(Object instance) {
        this.value.setValue(instance);
    }

    public Class getType() {
        return this.type;
    }

    public int getLength() {
        return this.value.getLength();
    }

    public boolean isReference() {
        return this.value.isReference();
    }
}

