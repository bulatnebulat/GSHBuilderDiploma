/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.HashMap;
import org.simpleframework.xml.core.ClassType;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.PathParser;
import org.simpleframework.xml.strategy.Type;

class ExpressionBuilder {
    private final Cache cache;
    private final Type type;

    public ExpressionBuilder(Class type) {
        this(new ClassType(type));
    }

    public ExpressionBuilder(Type type) {
        this.cache = new Cache();
        this.type = type;
    }

    public Expression build(String path) throws Exception {
        Expression expression = (Expression)this.cache.get(path);
        if (expression == null) {
            return this.create(path);
        }
        return expression;
    }

    private Expression create(String path) throws Exception {
        PathParser expression = new PathParser(this.type, path);
        if (this.cache != null) {
            this.cache.put(path, expression);
        }
        return expression;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class Cache
    extends HashMap<String, Expression> {
    }

}

