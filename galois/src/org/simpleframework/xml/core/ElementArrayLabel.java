/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.core.ArrayFactory;
import org.simpleframework.xml.core.ClassType;
import org.simpleframework.xml.core.CompositeArray;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.InstantiationException;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PrimitiveArray;
import org.simpleframework.xml.core.Qualifier;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class ElementArrayLabel
extends TemplateLabel {
    private Decorator decorator;
    private ElementArray label;
    private Introspector detail;
    private Class type;
    private String entry;
    private String name;

    public ElementArrayLabel(Contact contact, ElementArray label) {
        this.detail = new Introspector(contact, this);
        this.decorator = new Qualifier(contact);
        this.type = contact.getType();
        this.entry = label.entry();
        this.name = label.name();
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        String entry = this.getEntry(context);
        Contact contact = this.getContact();
        if (!this.type.isArray()) {
            throw new InstantiationException("Type is not an array %s for %s", this.type, contact);
        }
        return this.getConverter(context, entry);
    }

    private Converter getConverter(Context context, String name) throws Exception {
        Type entry = this.getDependent();
        Contact type = this.getContact();
        if (!context.isPrimitive(entry)) {
            return new CompositeArray(context, type, entry, name);
        }
        return new PrimitiveArray(context, type, entry, name);
    }

    private String getEntry(Context context) throws Exception {
        Style style = context.getStyle();
        String entry = this.getEntry();
        return style.getElement(entry);
    }

    public String getName(Context context) throws Exception {
        Style style = context.getStyle();
        String name = this.detail.getName();
        return style.getElement(name);
    }

    public String getPath(Context context) throws Exception {
        Expression path = this.getExpression();
        String name = this.getName(context);
        return path.getElement(name);
    }

    public Object getEmpty(Context context) throws Exception {
        ClassType array = new ClassType(this.type);
        ArrayFactory factory = new ArrayFactory(context, array);
        if (!this.label.empty()) {
            return factory.getInstance();
        }
        return null;
    }

    public String getEntry() throws Exception {
        if (this.detail.isEmpty(this.entry)) {
            this.entry = this.detail.getEntry();
        }
        return this.entry;
    }

    public String getName() throws Exception {
        return this.detail.getName();
    }

    public String getPath() throws Exception {
        Expression path = this.getExpression();
        String name = this.getName();
        return path.getElement(name);
    }

    public Expression getExpression() throws Exception {
        return this.detail.getExpression();
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public Type getDependent() {
        Class entry = this.type.getComponentType();
        if (entry == null) {
            return new ClassType(this.type);
        }
        return new ClassType(entry);
    }

    public Class getType() {
        return this.type;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.name;
    }

    public boolean isRequired() {
        return this.label.required();
    }

    public boolean isData() {
        return this.label.data();
    }

    public String toString() {
        return this.detail.toString();
    }
}

