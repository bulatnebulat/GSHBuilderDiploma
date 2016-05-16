/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

public class PersistenceException
extends Exception {
    public /* varargs */ PersistenceException(String text, Object ... list) {
        super(String.format(text, list));
    }

    public /* varargs */ PersistenceException(Throwable cause, String text, Object ... list) {
        super(String.format(text, list), cause);
    }
}

