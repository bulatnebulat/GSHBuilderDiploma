/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.AnnotationHandler;

class AnnotationFactory {
    private final boolean required;

    public AnnotationFactory() {
        this(true);
    }

    public AnnotationFactory(boolean required) {
        this.required = required;
    }

    public Annotation getInstance(Class type) throws Exception {
        ClassLoader loader = this.getClassLoader();
        if (Map.class.isAssignableFrom(type)) {
            return this.getInstance(loader, ElementMap.class);
        }
        if (Collection.class.isAssignableFrom(type)) {
            return this.getInstance(loader, ElementList.class);
        }
        if (type.isArray()) {
            return this.getInstance(loader, ElementArray.class);
        }
        return this.getInstance(loader, Element.class);
    }

    private Annotation getInstance(ClassLoader loader, Class label) throws Exception {
        AnnotationHandler handler = new AnnotationHandler(label, this.required);
        Class[] list = new Class[]{label};
        return (Annotation)Proxy.newProxyInstance(loader, list, handler);
    }

    private ClassLoader getClassLoader() throws Exception {
        return AnnotationFactory.class.getClassLoader();
    }
}

