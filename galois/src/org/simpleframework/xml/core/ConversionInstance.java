/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.strategy.Value;

class ConversionInstance
implements Instance {
    private final Context context;
    private final Class convert;
    private final Value value;

    public ConversionInstance(Context context, Value value, Class convert) throws Exception {
        this.context = context;
        this.convert = convert;
        this.value = value;
    }

    public Object getInstance() throws Exception {
        if (this.value.isReference()) {
            return this.value.getValue();
        }
        Object created = this.getInstance(this.convert);
        if (created != null) {
            this.setInstance(created);
        }
        return created;
    }

    public Object getInstance(Class type) throws Exception {
        Instance value = this.context.getInstance(type);
        Object object = value.getInstance();
        return object;
    }

    public Object setInstance(Object object) throws Exception {
        if (this.value != null) {
            this.value.setValue(object);
        }
        return object;
    }

    public Class getType() {
        return this.convert;
    }

    public boolean isReference() {
        return this.value.isReference();
    }
}

