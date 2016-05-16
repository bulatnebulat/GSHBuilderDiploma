/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.ClassCreator;
import org.simpleframework.xml.core.ConstructorException;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Initializer;
import org.simpleframework.xml.core.MethodException;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.core.ParameterFactory;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Signature;

class ConstructorScanner {
    private List<Initializer> list = new ArrayList<Initializer>();
    private Initializer primary;
    private Signature registry;
    private Class type;

    public ConstructorScanner(Class type) throws Exception {
        this.registry = new Signature(type);
        this.type = type;
        this.scan(type);
    }

    public Creator getCreator() {
        return new ClassCreator(this.list, this.registry, this.primary);
    }

    private void scan(Class type) throws Exception {
        Constructor<?>[] array = type.getDeclaredConstructors();
        if (!this.isInstantiable(type)) {
            throw new ConstructorException("Can not construct inner %s", type);
        }
        for (Constructor factory : array) {
            Signature index = new Signature(type);
            if (type.isPrimitive()) continue;
            this.scan(factory, index);
        }
    }

    private void scan(Constructor factory, Signature map) throws Exception {
        Annotation[][] labels = factory.getParameterAnnotations();
        Class<?>[] types = factory.getParameterTypes();
        for (int i = 0; i < types.length; ++i) {
            for (int j = 0; j < labels[i].length; ++j) {
                Parameter value = this.process(factory, labels[i][j], i);
                if (value == null) continue;
                String path = value.getPath();
                if (map.containsKey(path)) {
                    throw new PersistenceException("Parameter '%s' is a duplicate in %s", path, factory);
                }
                this.registry.put(path, value);
                map.put(path, value);
            }
        }
        if (types.length == map.size()) {
            this.build(factory, map);
        }
    }

    private void build(Constructor factory, Signature signature) throws Exception {
        Initializer initializer = new Initializer(factory, signature);
        if (initializer.isDefault()) {
            this.primary = initializer;
        }
        this.list.add(initializer);
    }

    private Parameter process(Constructor factory, Annotation label, int ordinal) throws Exception {
        if (label instanceof Attribute) {
            return this.create(factory, label, ordinal);
        }
        if (label instanceof ElementList) {
            return this.create(factory, label, ordinal);
        }
        if (label instanceof ElementArray) {
            return this.create(factory, label, ordinal);
        }
        if (label instanceof ElementMap) {
            return this.create(factory, label, ordinal);
        }
        if (label instanceof Element) {
            return this.create(factory, label, ordinal);
        }
        if (label instanceof Text) {
            return this.create(factory, label, ordinal);
        }
        return null;
    }

    private Parameter create(Constructor factory, Annotation label, int ordinal) throws Exception {
        Parameter value = ParameterFactory.getInstance(factory, label, ordinal);
        String path = value.getPath();
        if (this.registry.containsKey(path)) {
            this.validate(value, path);
        }
        return value;
    }

    private void validate(Parameter parameter, String name) throws Exception {
        Parameter other = (Parameter)this.registry.get(name);
        Annotation label = other.getAnnotation();
        if (!parameter.getAnnotation().equals(label)) {
            throw new MethodException("Annotations do not match for '%s' in %s", name, this.type);
        }
        Class expect = other.getType();
        if (expect != parameter.getType()) {
            throw new MethodException("Method types do not match for '%s' in %s", name, this.type);
        }
    }

    private boolean isInstantiable(Class type) {
        int modifiers = type.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            return true;
        }
        return !type.isMemberClass();
    }
}

