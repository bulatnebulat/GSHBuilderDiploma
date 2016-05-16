/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.simpleframework.xml.core.MethodType;

interface MethodPart {
    public String getName();

    public Annotation getAnnotation();

    public <T extends Annotation> T getAnnotation(Class<T> var1);

    public Class getType();

    public Class getDependent();

    public Class[] getDependents();

    public Method getMethod();

    public MethodType getMethodType();

    public String toString();
}

