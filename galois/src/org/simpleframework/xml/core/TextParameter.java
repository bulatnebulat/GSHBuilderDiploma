/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.ParameterContact;
import org.simpleframework.xml.core.TemplateParameter;
import org.simpleframework.xml.core.TextLabel;

class TextParameter
extends TemplateParameter {
    private final Expression expression;
    private final Constructor factory;
    private final Contact contact;
    private final Label label;
    private final String path;
    private final String name;
    private final Class type;
    private final int index;

    public TextParameter(Constructor factory, Text value, int index) throws Exception {
        this.contact = new Contact(value, factory, index);
        this.label = new TextLabel(this.contact, value);
        this.expression = this.label.getExpression();
        this.path = this.label.getPath();
        this.type = this.label.getType();
        this.name = this.label.getName();
        this.factory = factory;
        this.index = index;
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public String getPath(Context context) {
        return this.getPath();
    }

    public String getName(Context context) {
        return this.getName();
    }

    public Class getType() {
        return this.factory.getParameterTypes()[this.index];
    }

    public Annotation getAnnotation() {
        return this.contact.getAnnotation();
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isRequired() {
        return this.label.isRequired();
    }

    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }

    public boolean isText() {
        return true;
    }

    public String toString() {
        return this.contact.toString();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class Contact
    extends ParameterContact<Text> {
        public Contact(Text label, Constructor factory, int index) {
            super(label, factory, index);
        }

        @Override
        public String getName() {
            return "";
        }
    }

}

