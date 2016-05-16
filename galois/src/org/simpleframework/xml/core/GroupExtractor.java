/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.simpleframework.xml.core.CacheLabel;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Extractor;
import org.simpleframework.xml.core.ExtractorFactory;
import org.simpleframework.xml.core.Group;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class GroupExtractor
implements Group {
    private final ExtractorFactory factory;
    private final Annotation label;
    private final Registry registry;
    private final LabelMap elements;

    public GroupExtractor(Contact contact, Annotation label) throws Exception {
        this.factory = new ExtractorFactory(contact, label);
        this.elements = new LabelMap();
        this.registry = new Registry(this.elements);
        this.label = label;
        this.extract();
    }

    public Set<String> getNames() throws Exception {
        return this.elements.getKeys();
    }

    public Set<String> getPaths() throws Exception {
        return this.elements.getPaths();
    }

    public Set<String> getNames(Context context) throws Exception {
        return this.elements.getKeys(context);
    }

    public Set<String> getPaths(Context context) throws Exception {
        return this.elements.getPaths(context);
    }

    @Override
    public LabelMap getElements(Context context) throws Exception {
        return this.elements.getLabels(context);
    }

    @Override
    public Label getLabel(Class type) {
        return (Label)this.registry.get(type);
    }

    public boolean isValid(Class type) {
        return this.registry.containsKey(type);
    }

    @Override
    public boolean isInline() {
        for (Label label : this.registry) {
            if (label.isInline()) continue;
            return false;
        }
        return !this.registry.isEmpty();
    }

    private void extract() throws Exception {
        Extractor extractor = this.factory.getInstance();
        if (extractor != null) {
            this.extract(extractor);
        }
    }

    private void extract(Extractor extractor) throws Exception {
        List list = extractor.getAnnotations();
        for (Annotation label : list) {
            this.extract(extractor, label);
        }
    }

    private void extract(Extractor extractor, Annotation value) throws Exception {
        Label label = extractor.getLabel(value);
        Class type = extractor.getType(value);
        String name = label.getName();
        if (this.registry != null) {
            this.registry.register(name, label);
            this.registry.register(type, label);
        }
    }

    @Override
    public String toString() {
        return this.label.toString();
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class Registry
    extends LinkedHashMap<Class, Label>
    implements Iterable<Label> {
        private final LabelMap elements;

        public Registry(LabelMap elements) {
            this.elements = elements;
        }

        @Override
        public Iterator<Label> iterator() {
            return this.values().iterator();
        }

        public void register(String name, Label label) throws Exception {
            CacheLabel cache = new CacheLabel(label);
            if (!this.elements.containsKey(name)) {
                this.elements.put(name, cache);
            }
        }

        public void register(Class type, Label label) throws Exception {
            CacheLabel cache = new CacheLabel(label);
            if (!this.containsKey(type)) {
                this.put(type, cache);
            }
        }
    }

}

