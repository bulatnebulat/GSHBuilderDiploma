/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.LinkedList;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Hierarchy
extends LinkedList<Class> {
    public Hierarchy(Class type) {
        this.scan(type);
    }

    private void scan(Class type) {
        while (type != null) {
            this.addFirst(type);
            type = type.getSuperclass();
        }
        this.remove(Object.class);
    }
}

