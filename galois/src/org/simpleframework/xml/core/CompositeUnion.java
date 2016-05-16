/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Group;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.Repeater;
import org.simpleframework.xml.core.UnionException;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

class CompositeUnion
implements Repeater {
    private final LabelMap elements;
    private final Expression path;
    private final Context context;
    private final Group group;
    private final Type type;

    public CompositeUnion(Context context, Group group, Expression path, Type type) throws Exception {
        this.elements = group.getElements(context);
        this.context = context;
        this.group = group;
        this.type = type;
        this.path = path;
    }

    public Object read(InputNode node) throws Exception {
        String name = node.getName();
        String element = this.path.getElement(name);
        Label label = (Label)this.elements.get(element);
        Converter converter = label.getConverter(this.context);
        return converter.read(node);
    }

    public Object read(InputNode node, Object value) throws Exception {
        String name = node.getName();
        String element = this.path.getElement(name);
        Label label = (Label)this.elements.get(element);
        Converter converter = label.getConverter(this.context);
        return converter.read(node, value);
    }

    public boolean validate(InputNode node) throws Exception {
        String name = node.getName();
        String element = this.path.getElement(name);
        Label label = (Label)this.elements.get(element);
        Converter converter = label.getConverter(this.context);
        return converter.validate(node);
    }

    public void write(OutputNode node, Object object) throws Exception {
        Class real = object.getClass();
        Label label = this.group.getLabel(real);
        if (label == null) {
            throw new UnionException("Value of %s not declared in %s with annotation %s", real, this.type, this.group);
        }
        this.write(node, object, label);
    }

    private void write(OutputNode node, Object object, Label label) throws Exception {
        label.getConverter(this.context).write(node, object);
    }
}

