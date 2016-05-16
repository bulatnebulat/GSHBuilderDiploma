/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Repeater;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Variable
implements Label {
    private final Object value;
    private final Label label;

    public Variable(Label label, Object value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public Label getLabel(Class type) {
        return this;
    }

    @Override
    public Type getType(Class type) throws Exception {
        return this.label.getType(type);
    }

    @Override
    public Collection<String> getNames() throws Exception {
        return this.label.getNames();
    }

    @Override
    public Collection<String> getPaths() throws Exception {
        return this.label.getPaths();
    }

    @Override
    public Collection<String> getNames(Context context) throws Exception {
        return this.label.getNames(context);
    }

    @Override
    public Collection<String> getPaths(Context context) throws Exception {
        return this.label.getPaths(context);
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public Decorator getDecorator() throws Exception {
        return this.label.getDecorator();
    }

    @Override
    public Converter getConverter(Context context) throws Exception {
        Converter reader = this.label.getConverter(context);
        if (reader instanceof Adapter) {
            return reader;
        }
        return new Adapter(reader, this.value);
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
    public Object getEmpty(Context context) throws Exception {
        return this.label.getEmpty(context);
    }

    @Override
    public Contact getContact() {
        return this.label.getContact();
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
    public Annotation getAnnotation() {
        return this.label.getAnnotation();
    }

    @Override
    public String getPath() throws Exception {
        return this.label.getPath();
    }

    @Override
    public Expression getExpression() throws Exception {
        return this.label.getExpression();
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
    public boolean isData() {
        return this.label.isData();
    }

    @Override
    public boolean isInline() {
        return this.label.isInline();
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
    public boolean isRequired() {
        return this.label.isRequired();
    }

    @Override
    public boolean isText() {
        return this.label.isText();
    }

    @Override
    public boolean isUnion() {
        return this.label.isUnion();
    }

    @Override
    public String toString() {
        return this.label.toString();
    }

    private class Adapter
    implements Repeater {
        private final Converter reader;
        private final Object value;

        public Adapter(Converter reader, Object value) {
            this.reader = reader;
            this.value = value;
        }

        public Object read(InputNode node) throws Exception {
            return this.read(node, this.value);
        }

        public Object read(InputNode node, Object value) throws Exception {
            Position line = node.getPosition();
            String name = node.getName();
            if (this.reader instanceof Repeater) {
                Repeater repeat = (Repeater)this.reader;
                return repeat.read(node, value);
            }
            throw new PersistenceException("Element '%s' declared twice at %s", name, line);
        }

        public boolean validate(InputNode node) throws Exception {
            Position line = node.getPosition();
            String name = node.getName();
            if (this.reader instanceof Repeater) {
                Repeater repeat = (Repeater)this.reader;
                return repeat.validate(node);
            }
            throw new PersistenceException("Element '%s' declared twice at %s", name, line);
        }

        public void write(OutputNode node, Object value) throws Exception {
            this.write(node, value);
        }
    }

}

