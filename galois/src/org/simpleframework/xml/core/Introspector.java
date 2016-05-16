/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PathParser;
import org.simpleframework.xml.core.Reflector;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Introspector {
    private Annotation marker;
    private Contact contact;
    private Label label;

    public Introspector(Contact contact, Label label) {
        this.marker = contact.getAnnotation();
        this.contact = contact;
        this.label = label;
    }

    public Contact getContact() {
        return this.contact;
    }

    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }

    public String getEntry() throws Exception {
        String name;
        Type depend = this.getDependent();
        Class type = depend.getType();
        if (type.isArray()) {
            type = type.getComponentType();
        }
        if ((name = this.getName(type)) == null) {
            return null;
        }
        return name.intern();
    }

    private String getName(Class type) throws Exception {
        String name = this.getRoot(type);
        if (name != null) {
            return name;
        }
        name = type.getSimpleName();
        return Reflector.getName(name);
    }

    private String getRoot(Class type) {
        Class real = type;
        while (type != null) {
            String name = this.getRoot(real, type);
            if (name != null) {
                return name;
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private String getRoot(Class<?> real, Class<?> type) {
        String name = type.getSimpleName();
        if (type.isAnnotationPresent(Root.class)) {
            Root root = (Root)type.getAnnotation(Root.class);
            String text = root.name();
            if (!this.isEmpty(text)) {
                return text;
            }
            return Reflector.getName(name);
        }
        return null;
    }

    public String getName() throws Exception {
        String entry = this.label.getEntry();
        if (!this.label.isInline()) {
            entry = this.getDefault();
        }
        return entry.intern();
    }

    private String getDefault() throws Exception {
        String name = this.label.getOverride();
        if (!this.isEmpty(name)) {
            return name;
        }
        return this.contact.getName();
    }

    public Expression getExpression() throws Exception {
        String path = this.getPath();
        if (path != null) {
            return new PathParser(this.contact, path);
        }
        return new PathParser(this.contact, ".");
    }

    public String getPath() throws Exception {
        Path path = (Path)this.contact.getAnnotation(Path.class);
        if (path == null) {
            return null;
        }
        return path.value();
    }

    public boolean isEmpty(String value) {
        if (value != null) {
            return value.length() == 0;
        }
        return true;
    }

    public String toString() {
        return String.format("%s on %s", this.marker, this.contact);
    }
}

