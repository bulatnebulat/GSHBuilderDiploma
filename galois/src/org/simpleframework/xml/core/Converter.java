/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

interface Converter {
    public Object read(InputNode var1) throws Exception;

    public Object read(InputNode var1, Object var2) throws Exception;

    public boolean validate(InputNode var1) throws Exception;

    public void write(OutputNode var1, Object var2) throws Exception;
}

