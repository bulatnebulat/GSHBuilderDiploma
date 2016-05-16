/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.EmptyMatcher;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Instantiator;
import org.simpleframework.xml.core.Reflector;
import org.simpleframework.xml.core.Scanner;
import org.simpleframework.xml.core.ScannerFactory;
import org.simpleframework.xml.filter.Filter;
import org.simpleframework.xml.filter.PlatformFilter;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;
import org.simpleframework.xml.transform.Transformer;

class Support
implements Filter {
    private final ScannerFactory factory;
    private final Instantiator creator;
    private final Transformer transform;
    private final Matcher matcher;
    private final Filter filter;

    public Support() {
        this(new PlatformFilter());
    }

    public Support(Filter filter) {
        this(filter, new EmptyMatcher());
    }

    public Support(Filter filter, Matcher matcher) {
        this.transform = new Transformer(matcher);
        this.factory = new ScannerFactory();
        this.creator = new Instantiator();
        this.matcher = matcher;
        this.filter = filter;
    }

    public String replace(String text) {
        return this.filter.replace(text);
    }

    public Instance getInstance(Value value) {
        return this.creator.getInstance(value);
    }

    public Instance getInstance(Class type) {
        return this.creator.getInstance(type);
    }

    public Transform getTransform(Class type) throws Exception {
        return this.matcher.match(type);
    }

    public Scanner getScanner(Class type) throws Exception {
        return this.factory.getInstance(type);
    }

    public Object read(String value, Class type) throws Exception {
        return this.transform.read(value, type);
    }

    public String write(Object value, Class type) throws Exception {
        return this.transform.write(value, type);
    }

    public boolean valid(Class type) throws Exception {
        return this.transform.valid(type);
    }

    public String getName(Class type) throws Exception {
        Scanner schema = this.getScanner(type);
        String name = schema.getName();
        if (name != null) {
            return name;
        }
        return this.getClassName(type);
    }

    private String getClassName(Class type) throws Exception {
        if (type.isArray()) {
            type = type.getComponentType();
        }
        String name = type.getSimpleName();
        if (type.isPrimitive()) {
            return name;
        }
        return Reflector.getName(name);
    }

    public Class getPrimitive(Class type) throws Exception {
        if (type == Double.TYPE) {
            return Double.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        if (type == Character.TYPE) {
            return Character.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        return type;
    }

    public boolean isPrimitive(Class type) throws Exception {
        if (type == String.class) {
            return true;
        }
        if (type.isEnum()) {
            return true;
        }
        if (type.isPrimitive()) {
            return true;
        }
        return this.transform.valid(type);
    }

    public boolean isFloat(Class type) throws Exception {
        if (type == Double.class) {
            return true;
        }
        if (type == Float.class) {
            return true;
        }
        if (type == Float.TYPE) {
            return true;
        }
        if (type == Double.TYPE) {
            return true;
        }
        return false;
    }
}

