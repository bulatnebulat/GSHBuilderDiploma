/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class OverrideType
implements Type {
    private final Class override;
    private final Type type;

    public OverrideType(Type type, Class override) {
        this.override = override;
        this.type = type;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> label) {
        return this.type.getAnnotation(label);
    }

    @Override
    public Class getType() {
        return this.override;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}

