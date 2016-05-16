/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Reflector;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class FieldContact
implements Contact {
    private Annotation label;
    private Field field;
    private String name;
    private int modifier;

    public FieldContact(Field field, Annotation label) {
        this.modifier = field.getModifiers();
        this.label = label;
        this.field = field;
    }

    @Override
    public boolean isReadOnly() {
        return !this.isStatic() && this.isFinal();
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.modifier);
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.modifier);
    }

    @Override
    public Class getType() {
        return this.field.getType();
    }

    @Override
    public Class getDependent() {
        return Reflector.getDependent(this.field);
    }

    @Override
    public Class[] getDependents() {
        return Reflector.getDependents(this.field);
    }

    @Override
    public String getName() {
        if (this.name == null) {
            this.name = this.getName(this.field);
        }
        return this.name;
    }

    private String getName(Field field) {
        String name = field.getName();
        if (name != null) {
            name = name.intern();
        }
        return name;
    }

    @Override
    public Annotation getAnnotation() {
        return this.label;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        if (type == this.label.annotationType()) {
            return (T)this.label;
        }
        return this.field.getAnnotation(type);
    }

    @Override
    public void set(Object source, Object value) throws Exception {
        if (!this.isFinal()) {
            this.field.set(source, value);
        }
    }

    @Override
    public Object get(Object source) throws Exception {
        return this.field.get(source);
    }

    @Override
    public String toString() {
        return String.format("field '%s' %s", this.getName(), this.field.toString());
    }
}

