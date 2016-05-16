/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.simpleframework.xml.core.MethodName;
import org.simpleframework.xml.core.MethodPart;
import org.simpleframework.xml.core.MethodType;
import org.simpleframework.xml.core.Reflector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class GetPart
implements MethodPart {
    private final Annotation label;
    private final MethodType type;
    private final Method method;
    private final String name;

    public GetPart(MethodName method, Annotation label) {
        this.method = method.getMethod();
        this.name = method.getName();
        this.type = method.getType();
        this.label = label;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class getType() {
        return this.method.getReturnType();
    }

    @Override
    public Class getDependent() {
        return Reflector.getReturnDependent(this.method);
    }

    @Override
    public Class[] getDependents() {
        return Reflector.getReturnDependents(this.method);
    }

    @Override
    public Annotation getAnnotation() {
        return this.label;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return this.method.getAnnotation(type);
    }

    @Override
    public MethodType getMethodType() {
        return this.type;
    }

    @Override
    public Method getMethod() {
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
        return this.method;
    }

    @Override
    public String toString() {
        return this.method.toGenericString();
    }
}

