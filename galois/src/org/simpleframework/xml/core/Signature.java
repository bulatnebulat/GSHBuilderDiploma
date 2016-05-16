/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Parameter;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Signature
extends LinkedHashMap<String, Parameter> {
    private final Class type;

    public Signature(Class type) {
        this.type = type;
    }

    public Parameter getParameter(int ordinal) {
        return this.getParameters().get(ordinal);
    }

    public Parameter getParameter(String name) {
        return (Parameter)this.get(name);
    }

    public List<Parameter> getParameters() {
        return new ArrayList<Parameter>(this.values());
    }

    public Signature getSignature(Context context) throws Exception {
        Signature signature = new Signature(this.type);
        for (Parameter value : this.values()) {
            String name = value.getPath(context);
            signature.put(name, value);
        }
        return signature;
    }

    public Class getType() {
        return this.type;
    }
}

