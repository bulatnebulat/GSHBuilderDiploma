/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Converter;
import org.simpleframework.xml.stream.InputNode;

interface Repeater
extends Converter {
    public Object read(InputNode var1, Object var2) throws Exception;
}

