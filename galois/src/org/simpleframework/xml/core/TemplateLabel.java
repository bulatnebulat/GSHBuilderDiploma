/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
abstract class TemplateLabel
implements Label {
    protected TemplateLabel() {
    }

    @Override
    public Type getType(Class type) {
        return this.getContact();
    }

    @Override
    public Label getLabel(Class type) {
        return this;
    }

    @Override
    public Collection<String> getNames() throws Exception {
        String path = this.getPath();
        String name = this.getName();
        return Arrays.asList(path, name);
    }

    @Override
    public Collection<String> getPaths() throws Exception {
        String path = this.getPath();
        return Collections.singleton(path);
    }

    @Override
    public Collection<String> getNames(Context context) throws Exception {
        String path = this.getPath(context);
        String name = this.getName(context);
        return Arrays.asList(path, name);
    }

    @Override
    public Collection<String> getPaths(Context context) throws Exception {
        String path = this.getPath(context);
        return Collections.singleton(path);
    }

    @Override
    public String getEntry() throws Exception {
        return null;
    }

    @Override
    public Type getDependent() throws Exception {
        return null;
    }

    @Override
    public boolean isAttribute() {
        return false;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isInline() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public boolean isUnion() {
        return false;
    }
}

