/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.ModelSection;
import org.simpleframework.xml.core.Section;

class Structure {
    private final Label version;
    private final Label text;
    private final Model model;
    private final boolean primitive;

    public Structure(Model model, Label version, Label text, boolean primitive) {
        this.primitive = primitive;
        this.version = version;
        this.model = model;
        this.text = text;
    }

    public Section getSection(Context context) {
        return new ModelSection(context, this.model);
    }

    public boolean isPrimitive() {
        return this.primitive;
    }

    public Version getRevision() {
        if (this.version != null) {
            Contact contact = this.version.getContact();
            return (Version)contact.getAnnotation(Version.class);
        }
        return null;
    }

    public Label getVersion() {
        return this.version;
    }

    public Label getText() {
        return this.text;
    }
}

