/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class RootException
extends PersistenceException {
    public /* varargs */ RootException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ RootException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

