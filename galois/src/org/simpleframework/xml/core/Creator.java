/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.List;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Criteria;
import org.simpleframework.xml.core.Initializer;
import org.simpleframework.xml.core.Parameter;

interface Creator {
    public boolean isDefault();

    public Object getInstance(Context var1) throws Exception;

    public Object getInstance(Context var1, Criteria var2) throws Exception;

    public Parameter getParameter(String var1);

    public List<Parameter> getParameters();

    public List<Initializer> getInitializers();
}

