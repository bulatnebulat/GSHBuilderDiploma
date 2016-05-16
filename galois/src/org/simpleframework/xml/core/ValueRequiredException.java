/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class ValueRequiredException
extends PersistenceException {
    public /* varargs */ ValueRequiredException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ ValueRequiredException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

