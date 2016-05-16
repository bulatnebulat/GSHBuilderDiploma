/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.AnnotationFactory;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.ContactList;
import org.simpleframework.xml.core.ContactMap;
import org.simpleframework.xml.core.FieldContact;
import org.simpleframework.xml.core.Hierarchy;

class FieldScanner
extends ContactList {
    private final AnnotationFactory factory;
    private final Hierarchy hierarchy;
    private final DefaultType access;
    private final ContactMap done;

    public FieldScanner(Class type) throws Exception {
        this(type, null);
    }

    public FieldScanner(Class type, DefaultType access) throws Exception {
        this(type, access, true);
    }

    public FieldScanner(Class type, DefaultType access, boolean required) throws Exception {
        this.factory = new AnnotationFactory(required);
        this.hierarchy = new Hierarchy(type);
        this.done = new ContactMap();
        this.access = access;
        this.scan(type);
    }

    private void scan(Class type) throws Exception {
        for (Class next2 : this.hierarchy) {
            this.scan(next2, this.access);
        }
        for (Class next2 : this.hierarchy) {
            this.scan(next2, type);
        }
        this.build();
    }

    private void scan(Class type, Class real) {
        Field[] list;
        for (Field field : list = type.getDeclaredFields()) {
            this.scan(field);
        }
    }

    private void scan(Field field) {
        Annotation[] list;
        for (Annotation label : list = field.getDeclaredAnnotations()) {
            this.scan(field, label);
        }
    }

    private void scan(Class type, DefaultType access) throws Exception {
        Field[] list = type.getDeclaredFields();
        if (access == DefaultType.FIELD) {
            for (Field field : list) {
                Class real = field.getType();
                if (this.isStatic(field)) continue;
                this.process(field, real);
            }
        }
    }

    private void scan(Field field, Annotation label) {
        if (label instanceof Attribute) {
            this.process(field, label);
        }
        if (label instanceof ElementUnion) {
            this.process(field, label);
        }
        if (label instanceof ElementListUnion) {
            this.process(field, label);
        }
        if (label instanceof ElementMapUnion) {
            this.process(field, label);
        }
        if (label instanceof ElementList) {
            this.process(field, label);
        }
        if (label instanceof ElementArray) {
            this.process(field, label);
        }
        if (label instanceof ElementMap) {
            this.process(field, label);
        }
        if (label instanceof Element) {
            this.process(field, label);
        }
        if (label instanceof Transient) {
            this.remove(field, label);
        }
        if (label instanceof Version) {
            this.process(field, label);
        }
        if (label instanceof Text) {
            this.process(field, label);
        }
    }

    private void process(Field field, Class type) throws Exception {
        Annotation label = this.factory.getInstance(type);
        if (label != null) {
            this.process(field, label);
        }
    }

    private void process(Field field, Annotation label) {
        FieldContact contact = new FieldContact(field, label);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        this.done.put(field, contact);
    }

    private void remove(Field field, Annotation label) {
        this.done.remove(field);
    }

    private void build() {
        for (Contact contact : this.done) {
            this.add(contact);
        }
    }

    private boolean isStatic(Field field) {
        int modifier = field.getModifiers();
        if (Modifier.isStatic(modifier)) {
            return true;
        }
        return false;
    }
}

