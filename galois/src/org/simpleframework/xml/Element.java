/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface Element {
    public String name() default "";

    public boolean data() default 0;

    public boolean required() default 1;

    public Class type() default void.class;
}

