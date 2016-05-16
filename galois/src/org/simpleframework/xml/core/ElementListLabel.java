/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.ClassType;
import org.simpleframework.xml.core.CollectionFactory;
import org.simpleframework.xml.core.CompositeInlineList;
import org.simpleframework.xml.core.CompositeList;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PrimitiveInlineList;
import org.simpleframework.xml.core.PrimitiveList;
import org.simpleframework.xml.core.Qualifier;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class ElementListLabel
extends TemplateLabel {
    private Decorator decorator;
    private ElementList label;
    private Introspector detail;
    private Class type;
    private Class item;
    private String entry;
    private String name;

    public ElementListLabel(Contact contact, ElementList label) {
        this.detail = new Introspector(contact, this);
        this.decorator = new Qualifier(contact);
        this.type = contact.getType();
        this.entry = label.entry();
        this.item = label.type();
        this.name = label.name();
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        String entry = this.getEntry(context);
        if (!this.label.inline()) {
            return this.getConverter(context, entry);
        }
        return this.getInlineConverter(context, entry);
    }

    private Converter getConverter(Context context, String name) throws Exception {
        Type item = this.getDependent();
        Contact type = this.getContact();
        if (!context.isPrimitive(item)) {
            return new CompositeList(context, type, item, name);
        }
        return new PrimitiveList(context, type, item, name);
    }

    private Converter getInlineConverter(Context context, String name) throws Exception {
        Type item = this.getDependent();
        Contact type = this.getContact();
        if (!context.isPrimitive(item)) {
            return new CompositeInlineList(context, type, item, name);
        }
        return new PrimitiveInlineList(context, type, item, name);
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

    private String getEntry(Context context) throws Exception {
        Style style = context.getStyle();
        String entry = this.getEntry();
        return style.getElement(entry);
    }

    public Object getEmpty(Context context) throws Exception {
        ClassType list = new ClassType(this.type);
        CollectionFactory factory = new CollectionFactory(context, list);
        if (!this.label.empty()) {
            return factory.getInstance();
        }
        return null;
    }

    public Type getDependent() throws Exception {
        Contact contact = this.getContact();
        if (this.item == Void.TYPE) {
            this.item = contact.getDependent();
        }
        if (this.item == null) {
            throw new ElementException("Unable to determine generic type for %s", contact);
        }
        return new ClassType(this.item);
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

    public Class getType() {
        return this.type;
    }

    public Contact getContact() {
        return this.detail.getContact();
    }

    public String getOverride() {
        return this.name;
    }

    public boolean isData() {
        return this.label.data();
    }

    public boolean isCollection() {
        return true;
    }

    public boolean isRequired() {
        return this.label.required();
    }

    public boolean isInline() {
        return this.label.inline();
    }

    public String toString() {
        return this.detail.toString();
    }
}

