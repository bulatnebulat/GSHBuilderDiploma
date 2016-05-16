/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.stream.Style;

abstract class TemplateParameter
implements Parameter {
    protected TemplateParameter() {
    }

    public String getPath(Context context) {
        Expression expression = this.getExpression();
        String name = this.getName(context);
        return expression.getElement(name);
    }

    public String getName(Context context) {
        Style style = context.getStyle();
        String name = this.getName();
        return style.getElement(name);
    }

    public boolean isAttribute() {
        return false;
    }

    public boolean isText() {
        return false;
    }
}

