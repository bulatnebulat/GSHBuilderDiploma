/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import org.simpleframework.xml.core.MethodType;

class MethodName {
    private MethodType type;
    private Method method;
    private String name;

    public MethodName(Method method, MethodType type, String name) {
        this.name = name.intern();
        this.method = method;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public MethodType getType() {
        return this.type;
    }

    public Method getMethod() {
        return this.method;
    }
}

