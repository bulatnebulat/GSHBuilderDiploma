/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.Caller;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Instance;
import org.simpleframework.xml.core.Schema;
import org.simpleframework.xml.core.Session;
import org.simpleframework.xml.core.Support;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;

interface Context {
    public boolean isStrict();

    public Style getStyle();

    public Session getSession();

    public Support getSupport();

    public boolean isFloat(Class var1) throws Exception;

    public boolean isFloat(Type var1) throws Exception;

    public boolean isPrimitive(Class var1) throws Exception;

    public boolean isPrimitive(Type var1) throws Exception;

    public Instance getInstance(Value var1);

    public Instance getInstance(Class var1);

    public String getName(Class var1) throws Exception;

    public Caller getCaller(Class var1) throws Exception;

    public Version getVersion(Class var1) throws Exception;

    public Decorator getDecorator(Class var1) throws Exception;

    public Schema getSchema(Class var1) throws Exception;

    public Value getOverride(Type var1, InputNode var2) throws Exception;

    public boolean setOverride(Type var1, Object var2, OutputNode var3) throws Exception;

    public Class getType(Type var1, Object var2);

    public Object getAttribute(Object var1);

    public String getProperty(String var1);
}

