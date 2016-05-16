/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class ElementException
extends PersistenceException {
    public /* varargs */ ElementException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ ElementException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

