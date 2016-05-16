/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
enum MethodType {
    GET(3),
    IS(2),
    SET(3),
    NONE(0);
    
    private int prefix;

    private MethodType(int prefix) {
        this.prefix = prefix;
    }

    public int getPrefix() {
        return this.prefix;
    }
}

