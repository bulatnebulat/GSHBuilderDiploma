/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ClassType
implements Type {
    private final Class type;

    public ClassType(Class type) {
        this.type = type;
    }

    @Override
    public Class getType() {
        return this.type;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return null;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}

