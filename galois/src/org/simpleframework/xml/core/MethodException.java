/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class MethodException
extends PersistenceException {
    public /* varargs */ MethodException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ MethodException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

