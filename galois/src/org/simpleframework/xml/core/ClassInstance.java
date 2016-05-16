/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Instantiator;

class ClassInstance
implements Instance {
    private Instantiator creator;
    private Object value;
    private Class type;

    public ClassInstance(Instantiator creator, Class type) {
        this.creator = creator;
        this.type = type;
    }

    public Object getInstance() throws Exception {
        if (this.value == null) {
            this.value = this.creator.getObject(this.type);
        }
        return this.value;
    }

    public Object setInstance(Object value) throws Exception {
        this.value = value;
        return this.value;
    }

    public Class getType() {
        return this.type;
    }

    public boolean isReference() {
        return false;
    }
}

