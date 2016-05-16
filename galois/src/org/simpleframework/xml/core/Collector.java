/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Criteria;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Variable;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Collector
implements Criteria {
    private final Registry registry;
    private final Registry alias;
    private final Context context;

    public Collector(Context context) {
        this.registry = new Registry();
        this.alias = new Registry();
        this.context = context;
    }

    @Override
    public Variable get(String name) {
        return (Variable)this.registry.get(name);
    }

    @Override
    public Variable resolve(String name) {
        return (Variable)this.alias.get(name);
    }

    @Override
    public Variable remove(String label) throws Exception {
        Variable variable = (Variable)this.alias.remove(label);
        if (variable != null) {
            Collection<String> options = variable.getNames(this.context);
            String path = variable.getPath(this.context);
            for (String option : options) {
                this.alias.remove(option);
            }
            this.registry.remove(path);
        }
        return variable;
    }

    @Override
    public Iterator<String> iterator() {
        return this.registry.iterator();
    }

    @Override
    public void set(Label label, Object value) throws Exception {
        Variable variable = new Variable(label, value);
        if (label != null) {
            Collection<String> options = label.getNames(this.context);
            String path = label.getPath(this.context);
            if (!this.registry.containsKey(path)) {
                this.registry.put(path, variable);
            }
            for (String option : options) {
                this.alias.put(option, variable);
            }
        }
    }

    @Override
    public void commit(Object source) throws Exception {
        Collection set = this.registry.values();
        for (Variable entry : set) {
            Contact contact = entry.getContact();
            Object value = entry.getValue();
            contact.set(source, value);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class Registry
    extends LinkedHashMap<String, Variable> {
        private Registry() {
        }

        public Iterator<String> iterator() {
            return this.keySet().iterator();
        }
    }

}

