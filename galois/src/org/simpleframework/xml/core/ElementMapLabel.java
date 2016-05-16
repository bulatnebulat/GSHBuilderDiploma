/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.ClassType;
import org.simpleframework.xml.core.CompositeInlineMap;
import org.simpleframework.xml.core.CompositeMap;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Entry;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Introspector;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.MapFactory;
import org.simpleframework.xml.core.Qualifier;
import org.simpleframework.xml.core.TemplateLabel;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class ElementMapLabel
extends TemplateLabel {
    private Decorator decorator;
    private Introspector detail;
    private ElementMap label;
    private Entry entry;
    private Class type;
    private Class[] items;
    private String parent;
    private String name;

    public ElementMapLabel(Contact contact, ElementMap label) {
        this.detail = new Introspector(contact, this);
        this.decorator = new Qualifier(contact);
        this.entry = new Entry(contact, label);
        this.type = contact.getType();
        this.name = label.name();
        this.label = label;
    }

    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }

    public Converter getConverter(Context context) throws Exception {
        Type type = this.getMap();
        if (!this.label.inline()) {
            return new CompositeMap(context, this.entry, type);
        }
        return new CompositeInlineMap(context, this.entry, type);
    }

    public String getName(Context context) throws Exception {
        Style style = context.getStyle();
        String name = this.entry.getEntry();
        if (!this.label.inline()) {
            name = this.detail.getName();
        }
        return style.getElement(name);
    }

    public String getPath(Context context) throws Exception {
        Expression path = this.getExpression();
        String name = this.getName(context);
        return path.getElement(name);
    }

    public Object getEmpty(Context context) throws Exception {
        ClassType map = new ClassType(this.type);
        MapFactory factory = new MapFactory(context, map);
        if (!this.label.empty()) {
            return factory.getInstance();
        }
        return null;
    }

    public Type getDependent() throws Exception {
        Contact contact = this.getContact();
        if (this.items == null) {
            this.items = contact.getDependents();
        }
        if (this.items == null) {
            throw new ElementException("Unable to determine type for %s", contact);
        }
        if (this.items.length == 0) {
            return new ClassType(Object.class);
        }
        return new ClassType(this.items[0]);
    }

    public String getEntry() throws Exception {
        if (this.detail.isEmpty(this.parent)) {
            this.parent = this.detail.getEntry();
        }
        return this.parent;
    }

    public String getName() throws Exception {
        if (this.label.inline()) {
            return this.entry.getEntry();
        }
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

    private Type getMap() {
        return new ClassType(this.type);
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

