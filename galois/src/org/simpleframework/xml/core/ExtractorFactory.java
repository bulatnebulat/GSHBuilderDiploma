/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.ElementLabel;
import org.simpleframework.xml.core.ElementListLabel;
import org.simpleframework.xml.core.ElementMapLabel;
import org.simpleframework.xml.core.Extractor;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.PersistenceException;

class ExtractorFactory {
    private final Annotation label;
    private final Contact contact;

    public ExtractorFactory(Contact contact, Annotation label) {
        this.contact = contact;
        this.label = label;
    }

    public Extractor getInstance() throws Exception {
        return (Extractor)this.getInstance(this.label);
    }

    private Object getInstance(Annotation label) throws Exception {
        ExtractorBuilder builder = this.getBuilder(label);
        Constructor factory = builder.getConstructor();
        if (!factory.isAccessible()) {
            factory.setAccessible(true);
        }
        return factory.newInstance(this.contact, label);
    }

    private ExtractorBuilder getBuilder(Annotation label) throws Exception {
        if (label instanceof ElementUnion) {
            return new ExtractorBuilder(ElementUnion.class, ElementExtractor.class);
        }
        if (label instanceof ElementListUnion) {
            return new ExtractorBuilder(ElementListUnion.class, ElementListExtractor.class);
        }
        if (label instanceof ElementMapUnion) {
            return new ExtractorBuilder(ElementMapUnion.class, ElementMapExtractor.class);
        }
        throw new PersistenceException("Annotation %s is not a union", label);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class ElementMapExtractor
    implements Extractor<ElementMap> {
        private final Contact contact;
        private final ElementMapUnion union;

        public ElementMapExtractor(Contact contact, ElementMapUnion union) throws Exception {
            this.contact = contact;
            this.union = union;
        }

        @Override
        public List<ElementMap> getAnnotations() {
            ElementMap[] list = this.union.value();
            if (list.length > 0) {
                return Arrays.asList(list);
            }
            return Collections.emptyList();
        }

        @Override
        public Label getLabel(ElementMap element) {
            return new ElementMapLabel(this.contact, element);
        }

        @Override
        public Class getType(ElementMap element) {
            return element.valueType();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class ElementListExtractor
    implements Extractor<ElementList> {
        private final Contact contact;
        private final ElementListUnion union;

        public ElementListExtractor(Contact contact, ElementListUnion union) throws Exception {
            this.contact = contact;
            this.union = union;
        }

        @Override
        public List<ElementList> getAnnotations() {
            ElementList[] list = this.union.value();
            if (list.length > 0) {
                return Arrays.asList(list);
            }
            return Collections.emptyList();
        }

        @Override
        public Label getLabel(ElementList element) {
            return new ElementListLabel(this.contact, element);
        }

        @Override
        public Class getType(ElementList element) {
            return element.type();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class ElementExtractor
    implements Extractor<Element> {
        private final Contact contact;
        private final ElementUnion union;

        public ElementExtractor(Contact contact, ElementUnion union) throws Exception {
            this.contact = contact;
            this.union = union;
        }

        @Override
        public List<Element> getAnnotations() {
            Element[] list = this.union.value();
            if (list.length > 0) {
                return Arrays.asList(list);
            }
            return Collections.emptyList();
        }

        @Override
        public Label getLabel(Element element) {
            return new ElementLabel(this.contact, element);
        }

        @Override
        public Class getType(Element element) {
            Class type = element.type();
            if (type == Void.TYPE) {
                return this.contact.getType();
            }
            return type;
        }
    }

    private static class ExtractorBuilder {
        private final Class label;
        private final Class type;

        public ExtractorBuilder(Class label, Class type) {
            this.label = label;
            this.type = type;
        }

        private Constructor getConstructor() throws Exception {
            return this.type.getConstructor(Contact.class, this.label);
        }
    }

}

