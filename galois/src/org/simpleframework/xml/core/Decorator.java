/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;

interface Decorator {
    public void decorate(OutputNode var1);

    public void decorate(OutputNode var1, Decorator var2);
}

