/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.simpleframework.xml.core.AttributeException;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.ModelList;
import org.simpleframework.xml.core.ModelMap;
import org.simpleframework.xml.core.PathException;
import org.simpleframework.xml.core.Policy;
import org.simpleframework.xml.core.TextException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class TreeModel
implements Model {
    private Expression expression;
    private LabelMap attributes;
    private LabelMap elements;
    private ModelMap models;
    private OrderList order;
    private Policy policy;
    private String name;
    private String prefix;
    private Class type;
    private Label text;
    private int index;

    public TreeModel(Policy policy, Class type) {
        this(policy, type, null, null, 1);
    }

    public TreeModel(Policy policy, Class type, String name, String prefix, int index) {
        this.attributes = new LabelMap(policy);
        this.elements = new LabelMap(policy);
        this.models = new ModelMap(type);
        this.order = new OrderList();
        this.policy = policy;
        this.prefix = prefix;
        this.index = index;
        this.name = name;
        this.type = type;
    }

    @Override
    public Model lookup(Expression path) {
        String name = path.getFirst();
        int index = path.getIndex();
        Model model = this.lookup(name, index);
        if (path.isPath()) {
            path = path.getPath(1, 0);
            if (model != null) {
                return model.lookup(path);
            }
        }
        return model;
    }

    @Override
    public void registerElement(String name) throws Exception {
        if (!this.order.contains(name)) {
            this.order.add(name);
        }
        this.elements.put(name, null);
    }

    @Override
    public void registerAttribute(String name) throws Exception {
        this.attributes.put(name, null);
    }

    @Override
    public void registerText(Label label) throws Exception {
        if (this.text != null) {
            throw new TextException("Duplicate text annotation on %s", label);
        }
        this.text = label;
    }

    @Override
    public void registerAttribute(Label label) throws Exception {
        String name = label.getName();
        if (this.attributes.get(name) != null) {
            throw new AttributeException("Duplicate annotation of name '%s' on %s", name, label);
        }
        this.attributes.put(name, label);
    }

    @Override
    public void registerElement(Label label) throws Exception {
        String name = label.getName();
        if (this.elements.get(name) != null) {
            throw new ElementException("Duplicate annotation of name '%s' on %s", name, label);
        }
        if (!this.order.contains(name)) {
            this.order.add(name);
        }
        this.elements.put(name, label);
    }

    @Override
    public ModelMap buildModels(Context context) throws Exception {
        return this.models.getModels(context);
    }

    @Override
    public LabelMap buildAttributes(Context context) throws Exception {
        return this.attributes.getLabels(context);
    }

    @Override
    public LabelMap buildElements(Context context) throws Exception {
        return this.elements.getLabels(context);
    }

    @Override
    public boolean isModel(String name) {
        return this.models.containsKey(name);
    }

    @Override
    public boolean isElement(String name) {
        return this.elements.containsKey(name);
    }

    @Override
    public boolean isAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    @Override
    public Iterator<String> iterator() {
        ArrayList<String> list = new ArrayList<String>();
        for (String name : this.order) {
            list.add(name);
        }
        return list.iterator();
    }

    @Override
    public void validate(Class type) throws Exception {
        this.validateExpressions(type);
        this.validateAttributes(type);
        this.validateElements(type);
        this.validateModels(type);
        this.validateText(type);
    }

    private void validateText(Class type) throws Exception {
        if (this.text != null) {
            if (!this.elements.isEmpty()) {
                throw new TextException("Text annotation %s used with elements in %s", this.text, type);
            }
            if (this.isComposite()) {
                throw new TextException("Text annotation %s can not be used with paths in %s", this.text, type);
            }
        }
    }

    private void validateExpressions(Class type) throws Exception {
        for (Label label2 : this.elements) {
            if (label2 == null) continue;
            this.validateExpression(label2);
        }
        for (Label label2 : this.attributes) {
            if (label2 == null) continue;
            this.validateExpression(label2);
        }
        if (this.text != null) {
            this.validateExpression(this.text);
        }
    }

    private void validateExpression(Label label) throws Exception {
        Expression location = label.getExpression();
        if (this.expression != null) {
            String expect;
            String path = this.expression.getPath();
            if (!path.equals(expect = location.getPath())) {
                throw new PathException("Path '%s' does not match '%s' in %s", path, expect, this.type);
            }
        } else {
            this.expression = location;
        }
    }

    private void validateModels(Class type) throws Exception {
        for (ModelList list : this.models) {
            int count = 1;
            for (Model model : list) {
                if (model == null) continue;
                String name = model.getName();
                int index = model.getIndex();
                if (index != count++) {
                    throw new ElementException("Path section '%s[%s]' is out of sequence in %s", name, index, type);
                }
                model.validate(type);
            }
        }
    }

    private void validateAttributes(Class type) throws Exception {
        Set keys = this.attributes.keySet();
        for (String name : keys) {
            Label label = (Label)this.attributes.get(name);
            if (label != null) continue;
            throw new AttributeException("Ordered attribute '%s' does not exist in %s", name, type);
        }
    }

    private void validateElements(Class type) throws Exception {
        Set keys = this.elements.keySet();
        for (String name : keys) {
            ModelList list = (ModelList)this.models.get(name);
            Label label = (Label)this.elements.get(name);
            if (list == null && label == null) {
                throw new ElementException("Ordered element '%s' does not exist in %s", name, type);
            }
            if (list == null || label == null || list.isEmpty()) continue;
            throw new ElementException("Element '%s' is also a path name in %s", name, type);
        }
    }

    @Override
    public void register(Label label) throws Exception {
        if (label.isAttribute()) {
            this.registerAttribute(label);
        } else if (label.isText()) {
            this.registerText(label);
        } else {
            this.registerElement(label);
        }
    }

    @Override
    public Model lookup(String name, int index) {
        return this.models.lookup(name, index);
    }

    @Override
    public Model register(String name, String prefix, int index) throws Exception {
        Model model = this.models.lookup(name, index);
        if (model == null) {
            return this.create(name, prefix, index);
        }
        return model;
    }

    private Model create(String name, String prefix, int index) throws Exception {
        TreeModel model = new TreeModel(this.policy, this.type, name, prefix, index);
        if (name != null) {
            this.models.register(name, model);
            this.order.add(name);
        }
        return model;
    }

    @Override
    public boolean isComposite() {
        for (ModelList list : this.models) {
            for (Model model : list) {
                if (model == null || model.isEmpty()) continue;
                return true;
            }
        }
        return !this.models.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        if (this.text != null) {
            return false;
        }
        if (!this.elements.isEmpty()) {
            return false;
        }
        if (!this.attributes.isEmpty()) {
            return false;
        }
        return !this.isComposite();
    }

    @Override
    public Label getText() {
        return this.text;
    }

    @Override
    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    public String toString() {
        return String.format("model '%s[%s]'", this.name, this.index);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class OrderList
    extends ArrayList<String> {
    }

}

