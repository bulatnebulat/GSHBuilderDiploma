/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class ConstructorException
extends PersistenceException {
    public /* varargs */ ConstructorException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ ConstructorException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

