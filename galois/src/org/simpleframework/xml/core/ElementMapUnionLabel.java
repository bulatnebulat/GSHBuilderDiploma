/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.core.CompositeMapUnion;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.ElementMapLabel;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Group;
import org.simpleframework.xml.core.GroupExtractor;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.UnionException;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ElementMapUnionLabel
implements Label {
    private GroupExtractor extractor;
    private Expression expression;
    private Contact contact;
    private Label label;

    public ElementMapUnionLabel(Contact contact, ElementMapUnion union, ElementMap element) throws Exception {
        this.extractor = new GroupExtractor(contact, union);
        this.label = new ElementMapLabel(contact, element);
        this.contact = contact;
    }

    @Override
    public boolean isUnion() {
        return true;
    }

    @Override
    public Contact getContact() {
        return this.contact;
    }

    @Override
    public Annotation getAnnotation() {
        return this.label.getAnnotation();
    }

    @Override
    public Type getType(Class type) {
        return this.getContact();
    }

    @Override
    public Label getLabel(Class type) {
        return this;
    }

    @Override
    public Converter getConverter(Context context) throws Exception {
        Expression path = this.getExpression();
        Contact type = this.getContact();
        if (type == null) {
            throw new UnionException("Union %s was not declared on a field or method", this.label);
        }
        return new CompositeMapUnion(context, this.extractor, path, type);
    }

    @Override
    public Collection<String> getNames() throws Exception {
        return this.extractor.getNames();
    }

    @Override
    public Collection<String> getPaths() throws Exception {
        return this.extractor.getPaths();
    }

    @Override
    public Collection<String> getNames(Context context) throws Exception {
        return this.extractor.getNames(context);
    }

    @Override
    public Collection<String> getPaths(Context context) throws Exception {
        return this.extractor.getPaths(context);
    }

    @Override
    public Object getEmpty(Context context) throws Exception {
        return this.label.getEmpty(context);
    }

    @Override
    public String getName(Context context) throws Exception {
        return this.label.getName(context);
    }

    @Override
    public String getPath(Context context) throws Exception {
        return this.label.getPath(context);
    }

    @Override
    public Decorator getDecorator() throws Exception {
        return this.label.getDecorator();
    }

    @Override
    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }

    @Override
    public String getEntry() throws Exception {
        return this.label.getEntry();
    }

    @Override
    public String getName() throws Exception {
        return this.label.getName();
    }

    @Override
    public String getPath() throws Exception {
        return this.label.getPath();
    }

    @Override
    public Expression getExpression() throws Exception {
        if (this.expression == null) {
            this.expression = this.label.getExpression();
        }
        return this.expression;
    }

    @Override
    public String getOverride() {
        return this.label.getOverride();
    }

    @Override
    public Class getType() {
        return this.label.getType();
    }

    @Override
    public boolean isAttribute() {
        return this.label.isAttribute();
    }

    @Override
    public boolean isCollection() {
        return this.label.isCollection();
    }

    @Override
    public boolean isData() {
        return this.label.isData();
    }

    @Override
    public boolean isInline() {
        return this.label.isInline();
    }

    @Override
    public boolean isRequired() {
        return this.label.isRequired();
    }

    @Override
    public boolean isText() {
        return this.label.isText();
    }

    @Override
    public String toString() {
        return this.label.toString();
    }
}

