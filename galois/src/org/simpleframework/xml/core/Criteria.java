/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Variable;

interface Criteria
extends Iterable<String> {
    public Variable get(String var1);

    public Variable resolve(String var1);

    public Variable remove(String var1) throws Exception;

    public void set(Label var1, Object var2) throws Exception;

    public void commit(Object var1) throws Exception;
}

