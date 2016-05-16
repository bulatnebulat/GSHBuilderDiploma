/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.ContactList;
import org.simpleframework.xml.core.Hierarchy;
import org.simpleframework.xml.core.MethodContact;
import org.simpleframework.xml.core.MethodException;
import org.simpleframework.xml.core.MethodPart;
import org.simpleframework.xml.core.MethodPartFactory;
import org.simpleframework.xml.core.MethodType;

class MethodScanner
extends ContactList {
    private final MethodPartFactory factory;
    private final Hierarchy hierarchy;
    private final DefaultType access;
    private final PartMap write;
    private final PartMap read;
    private final Class type;

    public MethodScanner(Class type) throws Exception {
        this(type, null);
    }

    public MethodScanner(Class type, DefaultType access) throws Exception {
        this(type, access, true);
    }

    public MethodScanner(Class type, DefaultType access, boolean required) throws Exception {
        this.factory = new MethodPartFactory(required);
        this.hierarchy = new Hierarchy(type);
        this.write = new PartMap();
        this.read = new PartMap();
        this.access = access;
        this.type = type;
        this.scan(type);
    }

    private void scan(Class type) throws Exception {
        for (Class next2 : this.hierarchy) {
            this.scan(next2, this.access);
        }
        for (Class next2 : this.hierarchy) {
            this.scan(next2, type);
        }
        this.build();
        this.validate();
    }

    private void scan(Class type, Class real) throws Exception {
        Method[] list;
        for (Method method : list = type.getDeclaredMethods()) {
            this.scan(method);
        }
    }

    private void scan(Method method) throws Exception {
        Annotation[] list;
        for (Annotation label : list = method.getDeclaredAnnotations()) {
            this.scan(method, label);
        }
    }

    private void scan(Class type, DefaultType access) throws Exception {
        Method[] list = type.getDeclaredMethods();
        if (access == DefaultType.PROPERTY) {
            for (Method method : list) {
                Class value = this.factory.getType(method);
                if (value == null) continue;
                this.process(method);
            }
        }
    }

    private void scan(Method method, Annotation label) throws Exception {
        if (label instanceof Attribute) {
            this.process(method, label);
        }
        if (label instanceof ElementUnion) {
            this.process(method, label);
        }
        if (label instanceof ElementListUnion) {
            this.process(method, label);
        }
        if (label instanceof ElementMapUnion) {
            this.process(method, label);
        }
        if (label instanceof ElementList) {
            this.process(method, label);
        }
        if (label instanceof ElementArray) {
            this.process(method, label);
        }
        if (label instanceof ElementMap) {
            this.process(method, label);
        }
        if (label instanceof Element) {
            this.process(method, label);
        }
        if (label instanceof Transient) {
            this.remove(method, label);
        }
        if (label instanceof Version) {
            this.process(method, label);
        }
        if (label instanceof Text) {
            this.process(method, label);
        }
    }

    private void process(Method method, Annotation label) throws Exception {
        MethodPart part = this.factory.getInstance(method, label);
        MethodType type = part.getMethodType();
        if (type == MethodType.GET) {
            this.process(part, this.read);
        }
        if (type == MethodType.IS) {
            this.process(part, this.read);
        }
        if (type == MethodType.SET) {
            this.process(part, this.write);
        }
    }

    private void process(Method method) throws Exception {
        MethodPart part = this.factory.getInstance(method);
        MethodType type = part.getMethodType();
        if (type == MethodType.GET) {
            this.process(part, this.read);
        }
        if (type == MethodType.IS) {
            this.process(part, this.read);
        }
        if (type == MethodType.SET) {
            this.process(part, this.write);
        }
    }

    private void process(MethodPart method, PartMap map) {
        String name = method.getName();
        if (name != null) {
            map.put(name, method);
        }
    }

    private void remove(Method method, Annotation label) throws Exception {
        MethodPart part = this.factory.getInstance(method, label);
        MethodType type = part.getMethodType();
        if (type == MethodType.GET) {
            this.remove(part, this.read);
        }
        if (type == MethodType.IS) {
            this.remove(part, this.read);
        }
        if (type == MethodType.SET) {
            this.remove(part, this.write);
        }
    }

    private void remove(MethodPart part, PartMap map) throws Exception {
        String name = part.getName();
        if (name != null) {
            map.remove(name);
        }
    }

    private void build() throws Exception {
        for (String name : this.read) {
            MethodPart part = (MethodPart)this.read.get(name);
            if (part == null) continue;
            this.build(part, name);
        }
    }

    private void build(MethodPart read, String name) throws Exception {
        MethodPart match = this.write.take(name);
        if (match != null) {
            this.build(read, match);
        } else {
            this.build(read);
        }
    }

    private void build(MethodPart read) throws Exception {
        this.add(new MethodContact(read));
    }

    private void build(MethodPart read, MethodPart write) throws Exception {
        Annotation label = read.getAnnotation();
        String name = read.getName();
        if (!write.getAnnotation().equals(label)) {
            throw new MethodException("Annotations do not match for '%s' in %s", name, this.type);
        }
        Class type = read.getType();
        if (type != write.getType()) {
            throw new MethodException("Method types do not match for %s in %s", name, type);
        }
        this.add(new MethodContact(read, write));
    }

    private void validate() throws Exception {
        for (String name : this.write) {
            MethodPart part = (MethodPart)this.write.get(name);
            if (part == null) continue;
            this.validate(part, name);
        }
    }

    private void validate(MethodPart write, String name) throws Exception {
        MethodPart match = this.read.take(name);
        Method method = write.getMethod();
        if (match == null) {
            throw new MethodException("No matching get method for %s in %s", method, this.type);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class PartMap
    extends LinkedHashMap<String, MethodPart>
    implements Iterable<String> {
        private PartMap() {
        }

        @Override
        public Iterator<String> iterator() {
            return this.keySet().iterator();
        }

        public MethodPart take(String name) {
            return (MethodPart)this.remove(name);
        }
    }

}

