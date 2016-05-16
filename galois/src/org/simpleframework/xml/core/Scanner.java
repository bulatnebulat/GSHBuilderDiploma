/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.Caller;
import org.simpleframework.xml.core.ClassScanner;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.FieldScanner;
import org.simpleframework.xml.core.Function;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.MethodScanner;
import org.simpleframework.xml.core.Policy;
import org.simpleframework.xml.core.Reflector;
import org.simpleframework.xml.core.Section;
import org.simpleframework.xml.core.Structure;
import org.simpleframework.xml.core.StructureBuilder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Scanner
implements Policy {
    private StructureBuilder builder;
    private ClassScanner scanner;
    private Structure structure;
    private DefaultType access;
    private String name;
    private Class type;
    private boolean required;

    public Scanner(Class type) throws Exception {
        this.scanner = new ClassScanner(type);
        this.builder = new StructureBuilder(this, type);
        this.type = type;
        this.scan(type);
    }

    public Class getType() {
        return this.type;
    }

    public Creator getCreator() {
        return this.scanner.getCreator();
    }

    public Decorator getDecorator() {
        return this.scanner.getDecorator();
    }

    public Caller getCaller(Context context) {
        return new Caller(this, context);
    }

    public Section getSection(Context context) {
        return this.structure.getSection(context);
    }

    public Version getRevision() {
        return this.structure.getRevision();
    }

    public Order getOrder() {
        return this.scanner.getOrder();
    }

    public Label getVersion() {
        return this.structure.getVersion();
    }

    public Label getText() {
        return this.structure.getText();
    }

    public String getName() {
        return this.name;
    }

    public Function getCommit() {
        return this.scanner.getCommit();
    }

    public Function getValidate() {
        return this.scanner.getValidate();
    }

    public Function getPersist() {
        return this.scanner.getPersist();
    }

    public Function getComplete() {
        return this.scanner.getComplete();
    }

    public Function getReplace() {
        return this.scanner.getReplace();
    }

    public Function getResolve() {
        return this.scanner.getResolve();
    }

    public boolean isPrimitive() {
        return this.structure.isPrimitive();
    }

    public boolean isEmpty() {
        return this.scanner.getRoot() == null;
    }

    @Override
    public boolean isStrict() {
        return this.scanner.isStrict();
    }

    private void scan(Class type) throws Exception {
        this.root(type);
        this.order(type);
        this.access(type);
        this.field(type);
        this.method(type);
        this.validate(type);
        this.commit(type);
    }

    private void commit(Class type) throws Exception {
        if (this.structure == null) {
            this.structure = this.builder.build(type);
        }
        this.builder = null;
    }

    private void order(Class<?> type) throws Exception {
        this.builder.assemble(type);
    }

    private void validate(Class type) throws Exception {
        this.builder.validate(type);
    }

    private void root(Class<?> type) {
        String real = type.getSimpleName();
        Root root = this.scanner.getRoot();
        String text = real;
        if (root != null) {
            text = root.name();
            if (this.isEmpty(text)) {
                text = Reflector.getName(real);
            }
            this.name = text.intern();
        }
    }

    private void access(Class<?> type) {
        Default holder = this.scanner.getDefault();
        if (holder != null) {
            this.required = holder.required();
            this.access = holder.value();
        }
    }

    private boolean isEmpty(String value) {
        return value.length() == 0;
    }

    private void field(Class type) throws Exception {
        FieldScanner list = new FieldScanner(type, this.access, this.required);
        for (Contact contact : list) {
            Annotation label = contact.getAnnotation();
            if (label == null) continue;
            this.builder.process(contact, label);
        }
    }

    public void method(Class type) throws Exception {
        MethodScanner list = new MethodScanner(type, this.access, this.required);
        for (Contact contact : list) {
            Annotation label = contact.getAnnotation();
            if (label == null) continue;
            this.builder.process(contact, label);
        }
    }
}

