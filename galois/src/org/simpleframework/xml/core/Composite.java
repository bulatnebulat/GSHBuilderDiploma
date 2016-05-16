/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.AttributeException;
import org.simpleframework.xml.core.Caller;
import org.simpleframework.xml.core.Collector;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Criteria;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.ObjectFactory;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Primitive;
import org.simpleframework.xml.core.Revision;
import org.simpleframework.xml.core.Schema;
import org.simpleframework.xml.core.Section;
import org.simpleframework.xml.core.TextException;
import org.simpleframework.xml.core.ValueRequiredException;
import org.simpleframework.xml.core.Variable;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;

class Composite
implements Converter {
    private final ObjectFactory factory;
    private final Primitive primitive;
    private final Criteria criteria;
    private final Revision revision;
    private final Context context;
    private final Type type;

    public Composite(Context context, Type type) {
        this(context, type, null);
    }

    public Composite(Context context, Type type, Class override) {
        this.factory = new ObjectFactory(context, type, override);
        this.primitive = new Primitive(context, type);
        this.criteria = new Collector(context);
        this.revision = new Revision();
        this.context = context;
        this.type = type;
    }

    public Object read(InputNode node) throws Exception {
        Instance value = this.factory.getInstance(node);
        Class type = value.getType();
        if (value.isReference()) {
            return value.getInstance();
        }
        if (this.context.isPrimitive(type)) {
            return this.readPrimitive(node, value);
        }
        return this.read(node, value, type);
    }

    public Object read(InputNode node, Object source) throws Exception {
        Class type = source.getClass();
        Schema schema = this.context.getSchema(type);
        Caller caller = schema.getCaller();
        this.read(node, source, schema);
        this.criteria.commit(source);
        caller.validate(source);
        caller.commit(source);
        return this.readResolve(node, source, caller);
    }

    private Object read(InputNode node, Instance value, Class real) throws Exception {
        Schema schema = this.context.getSchema(real);
        Caller caller = schema.getCaller();
        Object source = this.read(node, schema, value);
        caller.validate(source);
        caller.commit(source);
        value.setInstance(source);
        return this.readResolve(node, source, caller);
    }

    private Object read(InputNode node, Schema schema, Instance value) throws Exception {
        Creator creator = schema.getCreator();
        if (creator.isDefault()) {
            return this.readDefault(node, schema, value);
        }
        this.read(node, (Object)null, schema);
        return this.readConstructor(node, schema, value);
    }

    private Object readDefault(InputNode node, Schema schema, Instance value) throws Exception {
        Object source = value.getInstance();
        if (value != null) {
            value.setInstance(source);
            this.read(node, source, schema);
            this.criteria.commit(source);
        }
        return source;
    }

    private Object readConstructor(InputNode node, Schema schema, Instance value) throws Exception {
        Creator creator = schema.getCreator();
        Object source = creator.getInstance(this.context, this.criteria);
        if (value != null) {
            value.setInstance(source);
            this.criteria.commit(source);
        }
        return source;
    }

    private Object readPrimitive(InputNode node, Instance value) throws Exception {
        Class type = value.getType();
        Object result = this.primitive.read(node, type);
        if (type != null) {
            value.setInstance(result);
        }
        return result;
    }

    private Object readResolve(InputNode node, Object source, Caller caller) throws Exception {
        if (source != null) {
            Class real;
            Position line = node.getPosition();
            Object value = caller.resolve(source);
            Class expect = this.type.getType();
            if (!expect.isAssignableFrom(real = value.getClass())) {
                throw new ElementException("Type %s does not match %s at %s", real, expect, line);
            }
            return value;
        }
        return source;
    }

    private void read(InputNode node, Object source, Schema schema) throws Exception {
        Section section = schema.getSection();
        this.readVersion(node, source, schema);
        this.readSection(node, source, section);
    }

    private void readSection(InputNode node, Object source, Section section) throws Exception {
        this.readText(node, source, section);
        this.readAttributes(node, source, section);
        this.readElements(node, source, section);
    }

    private void readVersion(InputNode node, Object source, Schema schema) throws Exception {
        Label label = schema.getVersion();
        Class expect = this.type.getType();
        if (label != null) {
            String name = label.getName();
            NodeMap<InputNode> map = node.getAttributes();
            InputNode value = map.remove(name);
            if (value != null) {
                this.readVersion(value, source, label);
            } else {
                Version version = this.context.getVersion(expect);
                Double start = this.revision.getDefault();
                Double expected = version.revision();
                this.criteria.set(label, start);
                this.revision.compare(expected, start);
            }
        }
    }

    private void readVersion(InputNode node, Object source, Label label) throws Exception {
        Object value = this.readInstance(node, source, label);
        Class expect = this.type.getType();
        if (value != null) {
            Version version = this.context.getVersion(expect);
            Double actual = version.revision();
            if (!value.equals(this.revision)) {
                this.revision.compare(actual, value);
            }
        }
    }

    private void readAttributes(InputNode node, Object source, Section section) throws Exception {
        NodeMap<InputNode> list = node.getAttributes();
        LabelMap map = section.getAttributes();
        for (String name : list) {
            InputNode value = node.getAttribute(name);
            if (value == null) continue;
            this.readAttribute(value, source, section, map);
        }
        this.validate(node, map, source);
    }

    private void readElements(InputNode node, Object source, Section section) throws Exception {
        LabelMap map = section.getElements();
        InputNode child = node.getNext();
        while (child != null) {
            String name = child.getName();
            Section block = section.getSection(name);
            if (block != null) {
                this.readSection(child, source, block);
            } else {
                this.readElement(child, source, section, map);
            }
            child = node.getNext();
        }
        this.validate(node, map, source);
    }

    private void readText(InputNode node, Object source, Section section) throws Exception {
        Label label = section.getText();
        if (label != null) {
            this.readInstance(node, source, label);
        }
    }

    private void readAttribute(InputNode node, Object source, Section section, LabelMap map) throws Exception {
        String name = node.getName();
        String path = section.getAttribute(name);
        Label label = map.getLabel(path);
        if (label == null) {
            Position line = node.getPosition();
            Class expect = this.context.getType(this.type, source);
            if (map.isStrict(this.context) && this.revision.isEqual()) {
                throw new AttributeException("Attribute '%s' does not have a match in %s at %s", path, expect, line);
            }
        } else {
            this.readInstance(node, source, label);
        }
    }

    private void readElement(InputNode node, Object source, Section section, LabelMap map) throws Exception {
        String name = node.getName();
        String path = section.getPath(name);
        Label label = map.getLabel(path);
        if (label == null) {
            label = this.criteria.get(path);
        }
        if (label == null) {
            Position line = node.getPosition();
            Class expect = this.context.getType(this.type, source);
            if (map.isStrict(this.context) && this.revision.isEqual()) {
                throw new ElementException("Element '%s' does not have a match in %s at %s", path, expect, line);
            }
            node.skip();
        } else {
            this.readUnion(node, source, map, label);
        }
    }

    private void readUnion(InputNode node, Object source, LabelMap map, Label label) throws Exception {
        Object value = this.readInstance(node, source, label);
        Collection<String> list = label.getPaths(this.context);
        for (String key : list) {
            Label union = map.getLabel(key);
            if (!label.isInline()) continue;
            this.criteria.set(union, value);
        }
    }

    private Object readInstance(InputNode node, Object source, Label label) throws Exception {
        Object object = this.readVariable(node, source, label);
        if (object == null) {
            Position line = node.getPosition();
            Class expect = this.context.getType(this.type, source);
            if (label.isRequired() && this.revision.isEqual()) {
                throw new ValueRequiredException("Empty value for %s in %s at %s", label, expect, line);
            }
        } else if (object != label.getEmpty(this.context)) {
            this.criteria.set(label, object);
        }
        return object;
    }

    private Object readVariable(InputNode node, Object source, Label label) throws Exception {
        Converter reader = label.getConverter(this.context);
        String name = label.getName(this.context);
        if (label.isCollection()) {
            Object value;
            Variable variable = this.criteria.get(name);
            Contact contact = label.getContact();
            if (variable != null) {
                Object value2 = variable.getValue();
                return reader.read(node, value2);
            }
            if (source != null && (value = contact.get(source)) != null) {
                return reader.read(node, value);
            }
        }
        return reader.read(node);
    }

    private void validate(InputNode node, LabelMap map, Object source) throws Exception {
        Class expect = this.context.getType(this.type, source);
        Position line = node.getPosition();
        for (Label label : map) {
            if (label.isRequired() && this.revision.isEqual()) {
                throw new ValueRequiredException("Unable to satisfy %s for %s at %s", label, expect, line);
            }
            Object value = label.getEmpty(this.context);
            if (value == null) continue;
            this.criteria.set(label, value);
        }
    }

    public boolean validate(InputNode node) throws Exception {
        Instance value = this.factory.getInstance(node);
        if (!value.isReference()) {
            Object result = value.setInstance(null);
            Class type = value.getType();
            return this.validate(node, type);
        }
        return true;
    }

    private boolean validate(InputNode node, Class type) throws Exception {
        Schema schema = this.context.getSchema(type);
        Section section = schema.getSection();
        this.validateText(node, schema);
        this.validateSection(node, section);
        return node.isElement();
    }

    private void validateSection(InputNode node, Section section) throws Exception {
        this.validateAttributes(node, section);
        this.validateElements(node, section);
    }

    private void validateAttributes(InputNode node, Section section) throws Exception {
        NodeMap<InputNode> list = node.getAttributes();
        LabelMap map = section.getAttributes();
        for (String name : list) {
            InputNode value = node.getAttribute(name);
            if (value == null) continue;
            this.validateAttribute(value, section, map);
        }
        this.validate(node, map);
    }

    private void validateElements(InputNode node, Section section) throws Exception {
        LabelMap map = section.getElements();
        InputNode next = node.getNext();
        while (next != null) {
            String name = next.getName();
            Section child = section.getSection(name);
            if (child != null) {
                this.validateSection(next, child);
            } else {
                this.validateElement(next, section, map);
            }
            next = node.getNext();
        }
        this.validate(node, map);
    }

    private void validateText(InputNode node, Schema schema) throws Exception {
        Label label = schema.getText();
        if (label != null) {
            this.validate(node, label);
        }
    }

    private void validateAttribute(InputNode node, Section section, LabelMap map) throws Exception {
        Position line = node.getPosition();
        String name = node.getName();
        String path = section.getAttribute(name);
        Label label = map.getLabel(path);
        if (label == null) {
            Class expect = this.type.getType();
            if (map.isStrict(this.context) && this.revision.isEqual()) {
                throw new AttributeException("Attribute '%s' does not exist for %s at %s", path, expect, line);
            }
        } else {
            this.validate(node, label);
        }
    }

    private void validateElement(InputNode node, Section section, LabelMap map) throws Exception {
        String name = node.getName();
        String path = section.getPath(name);
        Label label = map.getLabel(path);
        if (label == null) {
            label = this.criteria.get(path);
        }
        if (label == null) {
            Position line = node.getPosition();
            Class expect = this.type.getType();
            if (map.isStrict(this.context) && this.revision.isEqual()) {
                throw new ElementException("Element '%s' does not exist for %s at %s", path, expect, line);
            }
            node.skip();
        } else {
            this.validateUnion(node, map, label);
        }
    }

    private void validateUnion(InputNode node, LabelMap map, Label label) throws Exception {
        Collection<String> list = label.getPaths(this.context);
        for (String key : list) {
            Label union = map.getLabel(key);
            if (union == null || !label.isInline()) continue;
            this.criteria.set(union, null);
        }
        this.validate(node, label);
    }

    private void validate(InputNode node, Label label) throws Exception {
        Converter reader = label.getConverter(this.context);
        Position line = node.getPosition();
        Class expect = this.type.getType();
        boolean valid = reader.validate(node);
        if (!valid) {
            throw new PersistenceException("Invalid value for %s in %s at %s", label, expect, line);
        }
        this.criteria.set(label, null);
    }

    private void validate(InputNode node, LabelMap map) throws Exception {
        Position line = node.getPosition();
        for (Label label : map) {
            Class expect = this.type.getType();
            if (!label.isRequired() || !this.revision.isEqual()) continue;
            throw new ValueRequiredException("Unable to satisfy %s for %s at %s", label, expect, line);
        }
    }

    public void write(OutputNode node, Object source) throws Exception {
        block4 : {
            Class type = source.getClass();
            Schema schema = this.context.getSchema(type);
            Caller caller = schema.getCaller();
            try {
                if (schema.isPrimitive()) {
                    this.primitive.write(node, source);
                    break block4;
                }
                caller.persist(source);
                this.write(node, source, schema);
            }
            finally {
                caller.complete(source);
            }
        }
    }

    private void write(OutputNode node, Object source, Schema schema) throws Exception {
        Section section = schema.getSection();
        this.writeVersion(node, source, schema);
        this.writeSection(node, source, section);
    }

    private void writeSection(OutputNode node, Object source, Section section) throws Exception {
        NamespaceMap scope = node.getNamespaces();
        String prefix = section.getPrefix();
        if (prefix != null) {
            String reference = scope.getReference(prefix);
            if (reference == null) {
                throw new ElementException("Namespace prefix '%s' in %s is not in scope", prefix, this.type);
            }
            node.setReference(reference);
        }
        this.writeAttributes(node, source, section);
        this.writeElements(node, source, section);
        this.writeText(node, source, section);
    }

    private void writeVersion(OutputNode node, Object source, Schema schema) throws Exception {
        Version version = schema.getRevision();
        Label label = schema.getVersion();
        if (version != null) {
            Double start = this.revision.getDefault();
            Double value = version.revision();
            if (this.revision.compare(value, start)) {
                if (label.isRequired()) {
                    this.writeAttribute(node, value, label);
                }
            } else {
                this.writeAttribute(node, value, label);
            }
        }
    }

    private void writeAttributes(OutputNode node, Object source, Section section) throws Exception {
        LabelMap attributes = section.getAttributes();
        for (Label label : attributes) {
            Contact contact = label.getContact();
            Object value = contact.get(source);
            Class expect = this.context.getType(this.type, source);
            if (value == null) {
                value = label.getEmpty(this.context);
            }
            if (value == null && label.isRequired()) {
                throw new AttributeException("Value for %s is null in %s", label, expect);
            }
            this.writeAttribute(node, value, label);
        }
    }

    private void writeElements(OutputNode node, Object source, Section section) throws Exception {
        for (String name : section) {
            Section child = section.getSection(name);
            if (child != null) {
                OutputNode next = node.getChild(name);
                this.writeSection(next, source, child);
                continue;
            }
            String path = section.getPath(name);
            Label label = section.getElement(path);
            Class expect = this.context.getType(this.type, source);
            Variable value = this.criteria.get(path);
            if (value != null) continue;
            if (label == null) {
                throw new ElementException("Element '%s' not defined in %s", name, expect);
            }
            this.writeUnion(node, source, section, label);
        }
    }

    private void writeUnion(OutputNode node, Object source, Section section, Label label) throws Exception {
        Contact contact = label.getContact();
        Object value = contact.get(source);
        Class expect = this.context.getType(this.type, source);
        if (value == null && label.isRequired()) {
            throw new ElementException("Value for %s is null in %s", label, expect);
        }
        Object replace = this.writeReplace(value);
        if (replace != null) {
            this.writeElement(node, replace, label);
        }
        Collection<String> list = label.getPaths(this.context);
        for (String name : list) {
            Label union = section.getElement(name);
            if (union == null) continue;
            this.criteria.set(union, replace);
        }
    }

    private Object writeReplace(Object source) throws Exception {
        if (source != null) {
            Class type = source.getClass();
            Caller caller = this.context.getCaller(type);
            return caller.replace(source);
        }
        return source;
    }

    private void writeText(OutputNode node, Object source, Section section) throws Exception {
        Label label = section.getText();
        if (label != null) {
            Contact contact = label.getContact();
            Object value = contact.get(source);
            Class expect = this.context.getType(this.type, source);
            if (value == null) {
                value = label.getEmpty(this.context);
            }
            if (value == null && label.isRequired()) {
                throw new TextException("Value for %s is null in %s", label, expect);
            }
            this.writeText(node, value, label);
        }
    }

    private void writeAttribute(OutputNode node, Object value, Label label) throws Exception {
        if (value != null) {
            Decorator decorator = label.getDecorator();
            String name = label.getName(this.context);
            String text = this.factory.getText(value);
            OutputNode done = node.setAttribute(name, text);
            decorator.decorate(done);
        }
    }

    private void writeElement(OutputNode node, Object value, Label label) throws Exception {
        if (value != null) {
            Class real = value.getClass();
            Label match = label.getLabel(real);
            String name = match.getName(this.context);
            Type type = label.getType(real);
            OutputNode next = node.getChild(name);
            if (!match.isInline()) {
                this.writeNamespaces(next, type, match);
            }
            if (match.isInline() || !this.isOverridden(next, value, type)) {
                Converter convert = match.getConverter(this.context);
                boolean data = match.isData();
                next.setData(data);
                this.writeElement(next, value, convert);
            }
        }
    }

    private void writeElement(OutputNode node, Object value, Converter convert) throws Exception {
        convert.write(node, value);
    }

    private void writeNamespaces(OutputNode node, Type type, Label label) throws Exception {
        Class expect = type.getType();
        Decorator primary = this.context.getDecorator(expect);
        Decorator decorator = label.getDecorator();
        decorator.decorate(node, primary);
    }

    private void writeText(OutputNode node, Object value, Label label) throws Exception {
        if (value != null) {
            String text = this.factory.getText(value);
            boolean data = label.isData();
            node.setData(data);
            node.setValue(text);
        }
    }

    private boolean isOverridden(OutputNode node, Object value, Type type) throws Exception {
        return this.factory.setOverride(type, value, node);
    }
}

