/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Criteria;
import org.simpleframework.xml.core.Initializer;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Signature;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ClassCreator
implements Creator {
    private final List<Initializer> list;
    private final Initializer primary;
    private final Signature registry;
    private final Class type;

    public ClassCreator(List<Initializer> list, Signature registry, Initializer primary) {
        this.type = registry.getType();
        this.registry = registry;
        this.primary = primary;
        this.list = list;
    }

    @Override
    public boolean isDefault() {
        return this.primary != null;
    }

    @Override
    public Object getInstance(Context context) throws Exception {
        return this.primary.getInstance(context);
    }

    @Override
    public Object getInstance(Context context, Criteria criteria) throws Exception {
        Initializer initializer = this.getInitializer(context, criteria);
        if (initializer == null) {
            throw new PersistenceException("Constructor not matched for %s", this.type);
        }
        return initializer.getInstance(context, criteria);
    }

    private Initializer getInitializer(Context context, Criteria criteria) throws Exception {
        Initializer result = this.primary;
        double max = 0.0;
        for (Initializer initializer : this.list) {
            double score = initializer.getScore(context, criteria);
            if (score <= max) continue;
            result = initializer;
            max = score;
        }
        return result;
    }

    @Override
    public Parameter getParameter(String name) {
        return (Parameter)this.registry.get(name);
    }

    @Override
    public List<Parameter> getParameters() {
        return this.registry.getParameters();
    }

    @Override
    public List<Initializer> getInitializers() {
        return new ArrayList<Initializer>(this.list);
    }

    public String toString() {
        return String.format("creator for %s", this.type);
    }
}

