/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.core.TextException;
import org.simpleframework.xml.strategy.Type;

class TextLabel
extends TemplateLabel {
    private Introspector detail;
    private Contact contact;
    private Text label;
    private Class type;
    private String empty;

    public TextLabel(Contact contact, Text label) {
        this.detail = new Introspector(contact, this);
        this.type = contact.getType();
        this.empty = label.empty();
        this.contact = contact;
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return null;
    }

    public Converter getConverter(Context context) throws Exception {
        String ignore = this.getEmpty(context);
        Contact type = this.getContact();
        if (!context.isPrimitive(type)) {
            throw new TextException("Cannot use %s to represent %s", type, this.label);
        }
        return new Primitive(context, type, ignore);
    }

    public String getName(Context context) {
        return this.getName();
    }

    public String getPath(Context context) throws Exception {
        return this.getPath();
    }

    public String getEmpty(Context context) {
        if (this.detail.isEmpty(this.empty)) {
            return null;
        }
        return this.empty;
    }

    public String getPath() throws Exception {
        return this.getExpression().getPath();
    }

    public Expression getExpression() throws Exception {
        return this.detail.getExpression();
    }

    public Annotation getAnnotation() {
        return this.label;
    }

    public Contact getContact() {
        return this.contact;
    }

    public String getName() {
        return "";
    }

    public String getOverride() {
        return this.contact.toString();
    }

    public Class getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.label.required();
    }

    public boolean isData() {
        return this.label.data();
    }

    public boolean isText() {
        return true;
    }

    public boolean isInline() {
        return true;
    }

    public String toString() {
        return this.detail.toString();
    }
}

