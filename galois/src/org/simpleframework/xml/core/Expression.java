/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

interface Expression
extends Iterable<String> {
    public int getIndex();

    public String getPrefix();

    public String getFirst();

    public String getLast();

    public String getPath();

    public String getElement(String var1);

    public String getAttribute(String var1);

    public Expression getPath(int var1);

    public Expression getPath(int var1, int var2);

    public boolean isAttribute();

    public boolean isPath();

    public boolean isEmpty();

    public String toString();
}

