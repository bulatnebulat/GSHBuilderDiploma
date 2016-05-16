/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Composite;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.OverrideType;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.core.Qualifier;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class ElementLabel
extends TemplateLabel {
    private Decorator decorator;
    private Introspector detail;
    private Element label;
    private Class override;
    private Class type;
    private String name;

    public ElementLabel(Contact contact, Element label) {
        this.detail = new Introspector(contact, this);
        this.decorator = new Qualifier(contact);
        this.type = contact.getType();
        this.override = label.type();
        this.name = label.name();
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Type getType(Class type) {
        Contact contact = this.getContact();
        if (this.override == Void.TYPE) {
            return contact;
        }
        return new OverrideType(contact, this.override);
    }

    public Converter getConverter(Context context) throws Exception {
        Contact type = this.getContact();
        if (context.isPrimitive(type)) {
            return new Primitive(context, type);
        }
        if (this.override == Void.TYPE) {
            return new Composite(context, type);
        }
        return new Composite(context, type, this.override);
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

    public Object getEmpty(Context context) {
        return null;
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

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.name;
    }

    public Class getType() {
        if (this.override == Void.TYPE) {
            return this.type;
        }
        return this.override;
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

