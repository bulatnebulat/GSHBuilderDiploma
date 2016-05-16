/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ElementList {
    public String name() default "";

    public String entry() default "";

    public Class type() default void.class;

    public boolean data() default 0;

    public boolean required() default 1;

    public boolean inline() default 0;

    public boolean empty() default 1;
}

