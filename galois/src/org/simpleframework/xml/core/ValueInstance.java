/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Instantiator;
import org.simpleframework.xml.strategy.Value;

class ValueInstance
implements Instance {
    private final Instantiator creator;
    private final Value value;
    private final Class type;

    public ValueInstance(Instantiator creator, Value value) {
        this.type = value.getType();
        this.creator = creator;
        this.value = value;
    }

    public Object getInstance() throws Exception {
        if (this.value.isReference()) {
            return this.value.getValue();
        }
        Object object = this.creator.getObject(this.type);
        if (this.value != null) {
            this.value.setValue(object);
        }
        return object;
    }

    public Object setInstance(Object object) {
        if (this.value != null) {
            this.value.setValue(object);
        }
        return object;
    }

    public boolean isReference() {
        return this.value.isReference();
    }

    public Class getType() {
        return this.type;
    }
}

