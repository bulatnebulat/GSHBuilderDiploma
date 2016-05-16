/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

class Revision {
    private boolean equal = true;

    public double getDefault() {
        return 1.0;
    }

    public boolean compare(Object expected, Object current) {
        if (current != null) {
            this.equal = current.equals(expected);
        } else if (expected != null) {
            this.equal = expected.equals(1.0);
        }
        return this.equal;
    }

    public boolean isEqual() {
        return this.equal;
    }
}

