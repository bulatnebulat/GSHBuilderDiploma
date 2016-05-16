/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.AttributeLabel;
import org.simpleframework.xml.core.CacheLabel;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.ElementArrayLabel;
import org.simpleframework.xml.core.ElementLabel;
import org.simpleframework.xml.core.ElementListLabel;
import org.simpleframework.xml.core.ElementListUnionLabel;
import org.simpleframework.xml.core.ElementMapLabel;
import org.simpleframework.xml.core.ElementMapUnionLabel;
import org.simpleframework.xml.core.ElementUnionLabel;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.TextLabel;
import org.simpleframework.xml.core.VersionLabel;

final class LabelFactory {
    LabelFactory() {
    }

    public static Label getInstance(Contact contact, Annotation label) throws Exception {
        return LabelFactory.getInstance(contact, label, null);
    }

    public static Label getInstance(Contact contact, Annotation label, Annotation entry) throws Exception {
        Label value = LabelFactory.getLabel(contact, label, entry);
        if (value == null) {
            return value;
        }
        return new CacheLabel(value);
    }

    private static Label getLabel(Contact contact, Annotation label, Annotation entry) throws Exception {
        Constructor factory = LabelFactory.getConstructor(label);
        if (entry != null) {
            return (Label)factory.newInstance(contact, label, entry);
        }
        return (Label)factory.newInstance(contact, label);
    }

    private static Constructor getConstructor(Annotation label) throws Exception {
        LabelBuilder builder = LabelFactory.getBuilder(label);
        Constructor factory = builder.getConstructor();
        if (!factory.isAccessible()) {
            factory.setAccessible(true);
        }
        return factory;
    }

    private static LabelBuilder getBuilder(Annotation label) throws Exception {
        if (label instanceof Element) {
            return new LabelBuilder(ElementLabel.class, Element.class);
        }
        if (label instanceof ElementList) {
            return new LabelBuilder(ElementListLabel.class, ElementList.class);
        }
        if (label instanceof ElementArray) {
            return new LabelBuilder(ElementArrayLabel.class, ElementArray.class);
        }
        if (label instanceof ElementMap) {
            return new LabelBuilder(ElementMapLabel.class, ElementMap.class);
        }
        if (label instanceof ElementUnion) {
            return new LabelBuilder(ElementUnionLabel.class, ElementUnion.class, Element.class);
        }
        if (label instanceof ElementListUnion) {
            return new LabelBuilder(ElementListUnionLabel.class, ElementListUnion.class, ElementList.class);
        }
        if (label instanceof ElementMapUnion) {
            return new LabelBuilder(ElementMapUnionLabel.class, ElementMapUnion.class, ElementMap.class);
        }
        if (label instanceof Attribute) {
            return new LabelBuilder(AttributeLabel.class, Attribute.class);
        }
        if (label instanceof Version) {
            return new LabelBuilder(VersionLabel.class, Version.class);
        }
        if (label instanceof Text) {
            return new LabelBuilder(TextLabel.class, Text.class);
        }
        throw new PersistenceException("Annotation %s not supported", label);
    }

    private static class LabelBuilder {
        public Class label;
        public Class entry;
        public Class type;

        public LabelBuilder(Class type, Class label) {
            this(type, label, null);
        }

        public LabelBuilder(Class type, Class label, Class entry) {
            this.entry = entry;
            this.label = label;
            this.type = type;
        }

        public Constructor getConstructor() throws Exception {
            if (this.entry != null) {
                return this.getConstructor(this.label, this.entry);
            }
            return this.getConstructor(this.label);
        }

        private Constructor getConstructor(Class label) throws Exception {
            return this.type.getConstructor(Contact.class, label);
        }

        private Constructor getConstructor(Class label, Class entry) throws Exception {
            return this.type.getConstructor(Contact.class, label, entry);
        }
    }

}

