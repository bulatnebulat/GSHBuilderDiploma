/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.Order;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.ExpressionBuilder;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.PathException;

class ModelAssembler {
    private final ExpressionBuilder builder;
    private final Class type;

    public ModelAssembler(ExpressionBuilder builder, Class type) throws Exception {
        this.builder = builder;
        this.type = type;
    }

    public void assemble(Model model, Order order) throws Exception {
        this.assembleElements(model, order);
        this.assembleAttributes(model, order);
    }

    private void assembleElements(Model model, Order order) throws Exception {
        for (String value : order.elements()) {
            Expression path = this.builder.build(value);
            if (path.isAttribute()) {
                throw new PathException("Ordered element '%s' references an attribute in %s", path, this.type);
            }
            this.registerElements(model, path);
        }
    }

    private void assembleAttributes(Model model, Order order) throws Exception {
        for (String value : order.attributes()) {
            Expression path = this.builder.build(value);
            if (!path.isAttribute() && path.isPath()) {
                throw new PathException("Ordered attribute '%s' references an element in %s", path, this.type);
            }
            this.registerAttributes(model, path);
        }
    }

    private void registerAttributes(Model model, Expression path) throws Exception {
        String prefix = path.getPrefix();
        String name = path.getFirst();
        int index = path.getIndex();
        if (path.isPath()) {
            Model next = model.register(name, prefix, index);
            Expression child = path.getPath(1);
            if (next == null) {
                throw new PathException("Element '%s' does not exist in %s", name, this.type);
            }
            this.registerAttributes(next, child);
        } else {
            this.registerAttribute(model, path);
        }
    }

    private void registerAttribute(Model model, Expression path) throws Exception {
        String name = path.getFirst();
        if (name != null) {
            model.registerAttribute(name);
        }
    }

    private void registerElements(Model model, Expression path) throws Exception {
        String prefix = path.getPrefix();
        String name = path.getFirst();
        int index = path.getIndex();
        if (name != null) {
            Model next = model.register(name, prefix, index);
            Expression child = path.getPath(1);
            if (path.isPath()) {
                this.registerElements(next, child);
            }
        }
        this.registerElement(model, path);
    }

    private void registerElement(Model model, Expression path) throws Exception {
        Model previous;
        String prefix = path.getPrefix();
        String name = path.getFirst();
        int index = path.getIndex();
        if (index > 1 && (previous = model.lookup(name, index - 1)) == null) {
            throw new PathException("Ordered element '%s' in path '%s' is out of sequence for %s", name, path, this.type);
        }
        model.register(name, prefix, index);
    }
}

