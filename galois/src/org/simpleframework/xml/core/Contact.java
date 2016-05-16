/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

interface Contact
extends Type {
    public String getName();

    public Class getDependent();

    public Class[] getDependents();

    public Annotation getAnnotation();

    public void set(Object var1, Object var2) throws Exception;

    public Object get(Object var1) throws Exception;

    public boolean isReadOnly();

    public String toString();
}

