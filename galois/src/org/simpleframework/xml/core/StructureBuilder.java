/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.AttributeException;
import org.simpleframework.xml.core.Comparer;
import org.simpleframework.xml.core.ConstructorException;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.ElementException;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.ExpressionBuilder;
import org.simpleframework.xml.core.Initializer;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelFactory;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.LabelResolver;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.ModelAssembler;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Policy;
import org.simpleframework.xml.core.Scanner;
import org.simpleframework.xml.core.Structure;
import org.simpleframework.xml.core.TextException;
import org.simpleframework.xml.core.TreeModel;
import org.simpleframework.xml.core.UnionException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class StructureBuilder {
    private ExpressionBuilder builder;
    private ModelAssembler assembler;
    private LabelResolver resolver;
    private LabelMap attributes;
    private LabelMap elements;
    private LabelMap texts;
    private Comparer comparer;
    private Scanner scanner;
    private Label version;
    private Label text;
    private Model root;
    private Class type;
    private boolean primitive;

    public StructureBuilder(Scanner scanner, Class type) throws Exception {
        this.builder = new ExpressionBuilder(type);
        this.assembler = new ModelAssembler(this.builder, type);
        this.root = new TreeModel(scanner, type);
        this.attributes = new LabelMap(scanner);
        this.elements = new LabelMap(scanner);
        this.texts = new LabelMap(scanner);
        this.resolver = new LabelResolver();
        this.comparer = new Comparer();
        this.scanner = scanner;
        this.type = type;
    }

    public void assemble(Class type) throws Exception {
        Order order = this.scanner.getOrder();
        if (order != null) {
            this.assembler.assemble(this.root, order);
        }
    }

    public void process(Contact field, Annotation label) throws Exception {
        if (label instanceof Attribute) {
            this.process(field, label, this.attributes);
        }
        if (label instanceof ElementUnion) {
            this.union(field, label, this.elements);
        }
        if (label instanceof ElementListUnion) {
            this.union(field, label, this.elements);
        }
        if (label instanceof ElementMapUnion) {
            this.union(field, label, this.elements);
        }
        if (label instanceof ElementList) {
            this.process(field, label, this.elements);
        }
        if (label instanceof ElementArray) {
            this.process(field, label, this.elements);
        }
        if (label instanceof ElementMap) {
            this.process(field, label, this.elements);
        }
        if (label instanceof Element) {
            this.process(field, label, this.elements);
        }
        if (label instanceof Version) {
            this.version(field, label);
        }
        if (label instanceof Text) {
            this.text(field, label);
        }
    }

    private void union(Contact field, Annotation type, LabelMap map) throws Exception {
        Annotation[] list;
        for (Annotation value : list = this.extract(type)) {
            Label label = LabelFactory.getInstance(field, type, value);
            String path = label.getPath();
            String name = label.getName();
            if (map.get(path) != null) {
                throw new PersistenceException("Duplicate annotation of name '%s' on %s", name, label);
            }
            this.process(field, label, map);
            this.validate(label, path);
        }
    }

    private void process(Contact field, Annotation type, LabelMap map) throws Exception {
        Label label = LabelFactory.getInstance(field, type);
        String path = label.getPath();
        String name = label.getName();
        if (map.get(path) != null) {
            throw new PersistenceException("Duplicate annotation of name '%s' on %s", name, field);
        }
        this.process(field, label, map);
        this.validate(label, path);
    }

    private void process(Contact field, Label label, LabelMap map) throws Exception {
        Expression expression = label.getExpression();
        String path = label.getPath();
        Model model = this.root;
        if (!expression.isEmpty()) {
            model = this.register(expression);
        }
        this.resolver.register(label);
        model.register(label);
        map.put(path, label);
    }

    private void text(Contact field, Annotation type) throws Exception {
        Label label = LabelFactory.getInstance(field, type);
        Expression expression = label.getExpression();
        String path = label.getPath();
        Model model = this.root;
        if (!expression.isEmpty()) {
            model = this.register(expression);
        }
        if (this.texts.get(path) != null) {
            throw new TextException("Multiple text annotations in %s", type);
        }
        this.resolver.register(label);
        model.register(label);
        this.texts.put(path, label);
    }

    private void version(Contact field, Annotation type) throws Exception {
        Label label = LabelFactory.getInstance(field, type);
        if (this.version != null) {
            throw new AttributeException("Multiple version annotations in %s", type);
        }
        this.version = label;
    }

    private Annotation[] extract(Annotation label) throws Exception {
        Class<? extends Annotation> union = label.annotationType();
        Method[] list = union.getDeclaredMethods();
        if (list.length != 1) {
            throw new UnionException("Annotation '%s' is not a valid union for %s", label, this.type);
        }
        Method method = list[0];
        Object value = method.invoke(label, new Object[0]);
        return (Annotation[])value;
    }

    public Structure build(Class type) {
        return new Structure(this.root, this.version, this.text, this.primitive);
    }

    private boolean isElement(String path) throws Exception {
        Expression target = this.builder.build(path);
        Model model = this.lookup(target);
        if (model != null) {
            String name = target.getLast();
            int index = target.getIndex();
            if (model.isElement(name)) {
                return true;
            }
            if (model.isModel(name)) {
                Model element = model.lookup(name, index);
                if (element.isEmpty()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean isAttribute(String path) throws Exception {
        Expression target = this.builder.build(path);
        Model model = this.lookup(target);
        if (model != null) {
            String name = target.getLast();
            return model.isAttribute(name);
        }
        return false;
    }

    private Model lookup(Expression path) throws Exception {
        Expression target = path.getPath(0, 1);
        if (path.isPath()) {
            return this.root.lookup(target);
        }
        return this.root;
    }

    private Model register(Expression path) throws Exception {
        Model model = this.root.lookup(path);
        if (model != null) {
            return model;
        }
        return this.create(path);
    }

    private Model create(Expression path) throws Exception {
        Model model = this.root;
        while (model != null) {
            String prefix = path.getPrefix();
            String name = path.getFirst();
            int index = path.getIndex();
            if (name != null) {
                model = model.register(name, prefix, index);
            }
            if (!path.isPath()) break;
            path = path.getPath(1);
        }
        return model;
    }

    public void validate(Class type) throws Exception {
        Creator creator = this.scanner.getCreator();
        Order order = this.scanner.getOrder();
        this.validateUnions(type);
        this.validateElements(type, order);
        this.validateAttributes(type, order);
        this.validateParameters(creator);
        this.validateConstructors(type);
        this.validateModel(type);
        this.validateText(type);
    }

    private void validateModel(Class type) throws Exception {
        if (!this.root.isEmpty()) {
            this.root.validate(type);
        }
    }

    private void validateText(Class type) throws Exception {
        if (this.root.getText() != null) {
            if (!this.elements.isEmpty()) {
                throw new TextException("Elements used with %s in %s", this.text, type);
            }
            if (this.root.isComposite()) {
                throw new TextException("Paths used with %s in %s", this.text, type);
            }
        } else if (this.scanner.isEmpty()) {
            this.primitive = this.isEmpty();
        }
    }

    private void validateUnions(Class type) throws Exception {
        for (Label label : this.elements) {
            Collection<String> options = label.getPaths();
            Contact contact = label.getContact();
            for (String option : options) {
                Annotation union = contact.getAnnotation();
                Label other = (Label)this.elements.get(option);
                if (label.isInline() != other.isInline()) {
                    throw new UnionException("Inline must be consistent in %s for %s", union, contact);
                }
                if (label.isRequired() == other.isRequired()) continue;
                throw new UnionException("Required must be consistent in %s for %s", union, contact);
            }
        }
    }

    private void validateElements(Class type, Order order) throws Exception {
        if (order != null) {
            for (String name : order.elements()) {
                if (this.isElement(name)) continue;
                throw new ElementException("Ordered element '%s' missing for %s", name, type);
            }
        }
    }

    private void validateAttributes(Class type, Order order) throws Exception {
        if (order != null) {
            for (String name : order.attributes()) {
                if (this.isAttribute(name)) continue;
                throw new AttributeException("Ordered attribute '%s' missing in %s", name, type);
            }
        }
    }

    private void validateConstructors(Class type) throws Exception {
        Creator creator = this.scanner.getCreator();
        List<Initializer> list = creator.getInitializers();
        if (creator.isDefault()) {
            this.validateConstructors(this.elements);
            this.validateConstructors(this.attributes);
        }
        if (!list.isEmpty()) {
            this.validateConstructors(this.elements, list);
            this.validateConstructors(this.attributes, list);
        }
    }

    private void validateConstructors(LabelMap map) throws Exception {
        for (Label label : map) {
            Contact contact;
            if (label == null || !(contact = label.getContact()).isReadOnly()) continue;
            throw new ConstructorException("Default constructor can not accept read only %s in %s", label, this.type);
        }
    }

    private void validateConstructors(LabelMap map, List<Initializer> list) throws Exception {
        for (Label label : map) {
            if (label == null) continue;
            this.validateConstructor(label, list);
        }
        if (list.isEmpty()) {
            throw new ConstructorException("No constructor accepts all read only values in %s", this.type);
        }
    }

    private void validateConstructor(Label label, List<Initializer> list) throws Exception {
        Iterator<Initializer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Initializer initializer = iterator.next();
            Contact contact = label.getContact();
            String path = label.getPath();
            if (!contact.isReadOnly()) continue;
            Parameter value = initializer.getParameter(path);
            Collection<String> options = label.getNames();
            if (value == null) {
                String option;
                Iterator<String> i$ = options.iterator();
                while (i$.hasNext() && (value = initializer.getParameter(option = i$.next())) == null) {
                }
            }
            if (value != null) continue;
            iterator.remove();
        }
    }

    private void validateParameters(Creator creator) throws Exception {
        List<Parameter> list = creator.getParameters();
        for (Parameter parameter : list) {
            Label label = this.resolver.resolve(parameter);
            String path = parameter.getPath();
            if (label != null) continue;
            throw new ConstructorException("Parameter '%s' does not have a match in %s", path, this.type);
        }
    }

    private void validate(Label label, String name) throws Exception {
        Creator factory = this.scanner.getCreator();
        Parameter parameter = factory.getParameter(name);
        if (parameter != null) {
            this.validate(label, parameter);
        }
    }

    private void validate(Label label, Parameter parameter) throws Exception {
        String require;
        Collection<String> options = label.getNames();
        Contact contact = label.getContact();
        String name = parameter.getName();
        Class expect = contact.getType();
        if (expect != parameter.getType()) {
            throw new ConstructorException("Type does not match %s for '%s' in %s", label, name, parameter);
        }
        if (!options.contains(name) && name != (require = label.getName())) {
            if (name == null || require == null) {
                throw new ConstructorException("Annotation does not match %s for '%s' in %s", label, name, parameter);
            }
            if (!name.equals(require)) {
                throw new ConstructorException("Annotation does not match %s for '%s' in %s", label, name, parameter);
            }
        }
        this.validateAnnotations(label, parameter);
    }

    private void validateAnnotations(Label label, Parameter parameter) throws Exception {
        Class<? extends Annotation> expect;
        Class<? extends Annotation> actual;
        Annotation field = label.getAnnotation();
        Annotation argument = parameter.getAnnotation();
        String name = parameter.getName();
        if (!this.comparer.equals(field, argument) && !(expect = field.annotationType()).equals(actual = argument.annotationType())) {
            throw new ConstructorException("Annotation %s does not match %s for '%s' in %s", actual, expect, name, parameter);
        }
    }

    private boolean isEmpty() {
        if (this.text != null) {
            return false;
        }
        return this.root.isEmpty();
    }
}

