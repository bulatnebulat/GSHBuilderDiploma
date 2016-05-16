/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.MethodException;
import org.simpleframework.xml.core.MethodPart;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class MethodContact
implements Contact {
    private Annotation label;
    private MethodPart set;
    private Class[] items;
    private Class item;
    private Class type;
    private Method get;
    private String name;

    public MethodContact(MethodPart get) {
        this(get, null);
    }

    public MethodContact(MethodPart get, MethodPart set) {
        this.label = get.getAnnotation();
        this.items = get.getDependents();
        this.item = get.getDependent();
        this.get = get.getMethod();
        this.type = get.getType();
        this.name = get.getName();
        this.set = set;
    }

    @Override
    public boolean isReadOnly() {
        return this.set == null;
    }

    @Override
    public Annotation getAnnotation() {
        return this.label;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        T result = this.get.getAnnotation(type);
        if (type == this.label.annotationType()) {
            return (T)this.label;
        }
        if (result == null && this.set != null) {
            return this.set.getAnnotation(type);
        }
        return result;
    }

    @Override
    public Class getType() {
        return this.type;
    }

    @Override
    public Class getDependent() {
        return this.item;
    }

    @Override
    public Class[] getDependents() {
        return this.items;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void set(Object source, Object value) throws Exception {
        Class type = this.get.getDeclaringClass();
        if (this.set == null) {
            throw new MethodException("Property '%s' is read only in %s", this.name, type);
        }
        this.set.getMethod().invoke(source, value);
    }

    @Override
    public Object get(Object source) throws Exception {
        return this.get.invoke(source, new Object[0]);
    }

    @Override
    public String toString() {
        return String.format("method '%s'", this.name);
    }
}

