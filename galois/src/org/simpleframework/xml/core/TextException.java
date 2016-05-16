/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class TextException
extends PersistenceException {
    public /* varargs */ TextException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ TextException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

