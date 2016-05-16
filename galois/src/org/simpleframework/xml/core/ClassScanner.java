/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Complete;
import org.simpleframework.xml.core.ConstructorScanner;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Function;
import org.simpleframework.xml.core.NamespaceDecorator;
import org.simpleframework.xml.core.Persist;
import org.simpleframework.xml.core.Replace;
import org.simpleframework.xml.core.Resolve;
import org.simpleframework.xml.core.Validate;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ClassScanner {
    private NamespaceDecorator decorator;
    private ConstructorScanner scanner;
    private Namespace namespace;
    private Function commit;
    private Function validate;
    private Function persist;
    private Function complete;
    private Function replace;
    private Function resolve;
    private Default access;
    private Order order;
    private Root root;

    public ClassScanner(Class type) throws Exception {
        this.scanner = new ConstructorScanner(type);
        this.decorator = new NamespaceDecorator();
        this.scan(type);
    }

    public Creator getCreator() {
        return this.scanner.getCreator();
    }

    public Decorator getDecorator() {
        return this.decorator;
    }

    public Default getDefault() {
        return this.access;
    }

    public Order getOrder() {
        return this.order;
    }

    public Root getRoot() {
        return this.root;
    }

    public Function getCommit() {
        return this.commit;
    }

    public Function getValidate() {
        return this.validate;
    }

    public Function getPersist() {
        return this.persist;
    }

    public Function getComplete() {
        return this.complete;
    }

    public Function getReplace() {
        return this.replace;
    }

    public Function getResolve() {
        return this.resolve;
    }

    public boolean isStrict() {
        if (this.root != null) {
            return this.root.strict();
        }
        return true;
    }

    private void scan(Class type) throws Exception {
        Class real = type;
        while (type != null) {
            this.global(type);
            this.scope(type);
            this.scan(real, type);
            type = type.getSuperclass();
        }
        this.process(real);
    }

    private void global(Class type) throws Exception {
        if (this.namespace == null) {
            this.namespace(type);
        }
        if (this.root == null) {
            this.root(type);
        }
        if (this.order == null) {
            this.order(type);
        }
        if (this.access == null) {
            this.access(type);
        }
    }

    private void scan(Class real, Class type) throws Exception {
        Method[] method = type.getDeclaredMethods();
        for (int i = 0; i < method.length; ++i) {
            this.scan(method[i]);
        }
    }

    private void root(Class<?> type) {
        if (type.isAnnotationPresent(Root.class)) {
            this.root = (Root)type.getAnnotation(Root.class);
        }
    }

    private void order(Class<?> type) {
        if (type.isAnnotationPresent(Order.class)) {
            this.order = (Order)type.getAnnotation(Order.class);
        }
    }

    private void access(Class<?> type) {
        if (type.isAnnotationPresent(Default.class)) {
            this.access = (Default)type.getAnnotation(Default.class);
        }
    }

    private void namespace(Class<?> type) {
        if (type.isAnnotationPresent(Namespace.class)) {
            this.namespace = (Namespace)type.getAnnotation(Namespace.class);
            if (this.namespace != null) {
                this.decorator.add(this.namespace);
            }
        }
    }

    private void scope(Class<?> type) {
        if (type.isAnnotationPresent(NamespaceList.class)) {
            Namespace[] list;
            NamespaceList scope = (NamespaceList)type.getAnnotation(NamespaceList.class);
            for (Namespace name : list = scope.value()) {
                this.decorator.add(name);
            }
        }
    }

    private void process(Class type) throws Exception {
        if (this.namespace != null) {
            this.decorator.set(this.namespace);
        }
    }

    private void scan(Method method) {
        if (this.commit == null) {
            this.commit(method);
        }
        if (this.validate == null) {
            this.validate(method);
        }
        if (this.persist == null) {
            this.persist(method);
        }
        if (this.complete == null) {
            this.complete(method);
        }
        if (this.replace == null) {
            this.replace(method);
        }
        if (this.resolve == null) {
            this.resolve(method);
        }
    }

    private void replace(Method method) {
        Object mark = method.getAnnotation(Replace.class);
        if (mark != null) {
            this.replace = this.getFunction(method);
        }
    }

    private void resolve(Method method) {
        Object mark = method.getAnnotation(Resolve.class);
        if (mark != null) {
            this.resolve = this.getFunction(method);
        }
    }

    private void commit(Method method) {
        Object mark = method.getAnnotation(Commit.class);
        if (mark != null) {
            this.commit = this.getFunction(method);
        }
    }

    private void validate(Method method) {
        Object mark = method.getAnnotation(Validate.class);
        if (mark != null) {
            this.validate = this.getFunction(method);
        }
    }

    private void persist(Method method) {
        Object mark = method.getAnnotation(Persist.class);
        if (mark != null) {
            this.persist = this.getFunction(method);
        }
    }

    private void complete(Method method) {
        Object mark = method.getAnnotation(Complete.class);
        if (mark != null) {
            this.complete = this.getFunction(method);
        }
    }

    private Function getFunction(Method method) {
        boolean contextual = this.isContextual(method);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return new Function(method, contextual);
    }

    private boolean isContextual(Method method) {
        Class<?>[] list = method.getParameterTypes();
        if (list.length == 1) {
            return Map.class.equals(list[0]);
        }
        return false;
    }
}

