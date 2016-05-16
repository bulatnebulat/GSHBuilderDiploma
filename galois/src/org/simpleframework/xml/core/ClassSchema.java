/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.Caller;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Scanner;
import org.simpleframework.xml.core.Schema;
import org.simpleframework.xml.core.Section;

class ClassSchema
implements Schema {
    private final Decorator decorator;
    private final Section section;
    private final Version revision;
    private final Creator factory;
    private final Caller caller;
    private final Label version;
    private final Label text;
    private final Class type;
    private final boolean primitive;

    public ClassSchema(Scanner schema, Context context) throws Exception {
        this.section = schema.getSection(context);
        this.caller = schema.getCaller(context);
        this.factory = schema.getCreator();
        this.revision = schema.getRevision();
        this.decorator = schema.getDecorator();
        this.primitive = schema.isPrimitive();
        this.version = schema.getVersion();
        this.text = schema.getText();
        this.type = schema.getType();
    }

    public boolean isPrimitive() {
        return this.primitive;
    }

    public Creator getCreator() {
        return this.factory;
    }

    public Label getVersion() {
        return this.version;
    }

    public Version getRevision() {
        return this.revision;
    }

    public Decorator getDecorator() {
        return this.decorator;
    }

    public Caller getCaller() {
        return this.caller;
    }

    public Section getSection() {
        return this.section;
    }

    public Label getText() {
        return this.text;
    }

    public String toString() {
        return String.format("schema for %s", this.type);
    }
}

