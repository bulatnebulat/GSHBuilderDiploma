/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.ClassType;
import org.simpleframework.xml.core.CompositeKey;
import org.simpleframework.xml.core.CompositeValue;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.PrimitiveKey;
import org.simpleframework.xml.core.PrimitiveValue;
import org.simpleframework.xml.strategy.Type;

class Entry {
    private static final String DEFAULT_NAME = "entry";
    private ElementMap label;
    private Contact contact;
    private Class valueType;
    private Class keyType;
    private String entry;
    private String value;
    private String key;
    private boolean attribute;

    public Entry(Contact contact, ElementMap label) {
        this.attribute = label.attribute();
        this.entry = label.entry();
        this.value = label.value();
        this.key = label.key();
        this.contact = contact;
        this.label = label;
    }

    public Contact getContact() {
        return this.contact;
    }

    public boolean isAttribute() {
        return this.attribute;
    }

    public boolean isInline() throws Exception {
        return this.isAttribute();
    }

    public Converter getKey(Context context) throws Exception {
        Type type = this.getKeyType();
        if (context.isPrimitive(type)) {
            return new PrimitiveKey(context, this, type);
        }
        return new CompositeKey(context, this, type);
    }

    public Converter getValue(Context context) throws Exception {
        Type type = this.getValueType();
        if (context.isPrimitive(type)) {
            return new PrimitiveValue(context, this, type);
        }
        return new CompositeValue(context, this, type);
    }

    protected Type getKeyType() throws Exception {
        if (this.keyType == null) {
            this.keyType = this.label.keyType();
            if (this.keyType == Void.TYPE) {
                this.keyType = this.getDependent(0);
            }
        }
        return new ClassType(this.keyType);
    }

    protected Type getValueType() throws Exception {
        if (this.valueType == null) {
            this.valueType = this.label.valueType();
            if (this.valueType == Void.TYPE) {
                this.valueType = this.getDependent(1);
            }
        }
        return new ClassType(this.valueType);
    }

    private Class getDependent(int index) throws Exception {
        Class[] list = this.contact.getDependents();
        if (list.length < index) {
            throw new PersistenceException("Could not find type for %s at index %s", this.contact, index);
        }
        return list[index];
    }

    public String getKey() throws Exception {
        if (this.key == null) {
            return this.key;
        }
        if (this.isEmpty(this.key)) {
            this.key = null;
        }
        return this.key;
    }

    public String getValue() throws Exception {
        if (this.value == null) {
            return this.value;
        }
        if (this.isEmpty(this.value)) {
            this.value = null;
        }
        return this.value;
    }

    public String getEntry() throws Exception {
        if (this.entry == null) {
            return this.entry;
        }
        if (this.isEmpty(this.entry)) {
            this.entry = "entry";
        }
        return this.entry;
    }

    private boolean isEmpty(String value) {
        return value.length() == 0;
    }

    public String toString() {
        return String.format("%s on %s", this.label, this.contact);
    }
}

