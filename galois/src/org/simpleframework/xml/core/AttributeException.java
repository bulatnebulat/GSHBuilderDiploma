/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class AttributeException
extends PersistenceException {
    public /* varargs */ AttributeException(String text, Object ... list) {
        super(text, list);
    }

    public /* varargs */ AttributeException(Throwable cause, String text, Object ... list) {
        super(cause, text, list);
    }
}

