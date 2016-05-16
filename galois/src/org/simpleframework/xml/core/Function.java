/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import java.util.Map;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Session;

class Function {
    private final Method method;
    private final boolean contextual;

    public Function(Method method) {
        this(method, false);
    }

    public Function(Method method, boolean contextual) {
        this.contextual = contextual;
        this.method = method;
    }

    public Object call(Context context, Object source) throws Exception {
        if (source != null) {
            Session session = context.getSession();
            Map table = session.getMap();
            if (this.contextual) {
                return this.method.invoke(source, table);
            }
            return this.method.invoke(source, new Object[0]);
        }
        return null;
    }
}

