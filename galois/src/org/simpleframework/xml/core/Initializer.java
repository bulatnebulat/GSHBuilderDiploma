/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.simpleframework.xml.core.Contact;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Criteria;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.core.Signature;
import org.simpleframework.xml.core.Variable;

class Initializer {
    private final List<Parameter> list;
    private final Constructor factory;
    private final Signature signature;

    public Initializer(Constructor factory, Signature signature) {
        this.list = signature.getParameters();
        this.signature = signature;
        this.factory = factory;
    }

    public boolean isDefault() {
        return this.signature.size() == 0;
    }

    public Parameter getParameter(String name) {
        return (Parameter)this.signature.get(name);
    }

    public Object getInstance(Context context) throws Exception {
        if (!this.factory.isAccessible()) {
            this.factory.setAccessible(true);
        }
        return this.factory.newInstance(new Object[0]);
    }

    public Object getInstance(Context context, Criteria criteria) throws Exception {
        Object[] values = this.list.toArray();
        for (int i = 0; i < this.list.size(); ++i) {
            values[i] = this.getVariable(context, criteria, i);
        }
        return this.getInstance(values);
    }

    private Object getVariable(Context context, Criteria criteria, int index) throws Exception {
        Parameter parameter = this.list.get(index);
        String path = parameter.getPath(context);
        Variable variable = criteria.remove(path);
        if (variable != null) {
            return variable.getValue();
        }
        return null;
    }

    public double getScore(Context context, Criteria criteria) throws Exception {
        Signature match = this.signature.getSignature(context);
        for (String name : criteria) {
            Variable label = criteria.resolve(name);
            if (label == null) continue;
            Parameter value = match.getParameter(name);
            Contact contact = label.getContact();
            if (value == null) {
                String option;
                Collection<String> options = label.getNames(context);
                Iterator<String> i$ = options.iterator();
                while (i$.hasNext() && (value = match.getParameter(option = i$.next())) == null) {
                }
            }
            if (!contact.isReadOnly() || value != null) continue;
            return -1.0;
        }
        return this.getPercentage(context, criteria);
    }

    private double getPercentage(Context context, Criteria criteria) throws Exception {
        double score = 0.0;
        for (Parameter value : this.list) {
            String name = value.getPath(context);
            Variable label = criteria.resolve(name);
            if (label == null) {
                if (value.isRequired()) {
                    return -1.0;
                }
                if (!value.isPrimitive()) continue;
                return -1.0;
            }
            score += 1.0;
        }
        return this.getAdjustment(context, score);
    }

    private double getAdjustment(Context context, double score) {
        double adjustment = (double)this.list.size() / 1000.0;
        if (score > 0.0) {
            return adjustment + score / (double)this.list.size();
        }
        return score / (double)this.list.size();
    }

    private Object getInstance(Object[] list) throws Exception {
        if (!this.factory.isAccessible()) {
            this.factory.setAccessible(true);
        }
        return this.factory.newInstance(list);
    }

    public String toString() {
        return this.factory.toString();
    }
}

