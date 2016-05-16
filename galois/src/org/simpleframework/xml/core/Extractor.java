/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.List;
import org.simpleframework.xml.core.Label;

interface Extractor<T extends Annotation> {
    public List<T> getAnnotations() throws Exception;

    public Class getType(T var1) throws Exception;

    public Label getLabel(T var1) throws Exception;
}

