/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Expression;

interface Parameter {
    public Class getType();

    public int getIndex();

    public Annotation getAnnotation();

    public Expression getExpression();

    public String getName();

    public String getPath();

    public String getPath(Context var1);

    public String getName(Context var1);

    public boolean isRequired();

    public boolean isPrimitive();

    public boolean isAttribute();

    public boolean isText();

    public String toString();
}

