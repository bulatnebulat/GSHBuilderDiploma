/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;

public class UnionException
extends PersistenceException {
    public /* varargs */ UnionException(String text, Object ... list) {
        super(String.format(text, list), new Object[0]);
    }
}

