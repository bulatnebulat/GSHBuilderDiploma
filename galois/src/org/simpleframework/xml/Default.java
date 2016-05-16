/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.simpleframework.xml.DefaultType;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Default {
    public DefaultType value() default DefaultType.FIELD;

    public boolean required() default 1;
}

