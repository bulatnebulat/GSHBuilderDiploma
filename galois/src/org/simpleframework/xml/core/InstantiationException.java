/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class InstantiationException
extends PersistenceException {
    public /* varargs */ InstantiationException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ InstantiationException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

