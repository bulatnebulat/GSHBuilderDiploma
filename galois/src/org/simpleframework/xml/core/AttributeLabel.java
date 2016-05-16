/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.core.AttributeException;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.core.Qualifier;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class AttributeLabel
extends TemplateLabel {
    private Decorator decorator;
    private Introspector detail;
    private Attribute label;
    private Class type;
    private String name;
    private String empty;

    public AttributeLabel(Contact contact, Attribute label) {
        this.detail = new Introspector(contact, this);
        this.decorator = new Qualifier(contact);
        this.type = contact.getType();
        this.empty = label.empty();
        this.name = label.name();
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        String ignore = this.getEmpty(context);
        Contact type = this.getContact();
        if (!context.isPrimitive(type)) {
            throw new AttributeException("Cannot use %s to represent %s", this.label, type);
        }
        return new Primitive(context, type, ignore);
    }

    public String getEmpty(Context context) {
        if (this.detail.isEmpty(this.empty)) {
            return null;
        }
        return this.empty;
    }

    public String getName(Context context) throws Exception {
        Style style = context.getStyle();
        String name = this.detail.getName();
        return style.getAttribute(name);
    }

    public String getPath(Context context) throws Exception {
        Expression path = this.getExpression();
        String name = this.getName(context);
        return path.getAttribute(name);
    }

    public String getName() throws Exception {
        return this.detail.getName();
    }

    public String getPath() throws Exception {
        Expression path = this.getExpression();
        String name = this.getName();
        return path.getAttribute(name);
    }

    public Expression getExpression() throws Exception {
        return this.detail.getExpression();
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public String getOverride() {
        return this.name;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public Class getType() {
        return this.type;
    }

    public boolean isAttribute() {
        return true;
    }

    public boolean isRequired() {
        return this.label.required();
    }

    public boolean isData() {
        return false;
    }

    public String toString() {
        return this.detail.toString();
    }
}

