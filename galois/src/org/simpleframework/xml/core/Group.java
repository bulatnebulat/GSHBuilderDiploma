/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;

interface Group {
    public boolean isInline();

    public Label getLabel(Class var1);

    public LabelMap getElements(Context var1) throws Exception;

    public String toString();
}

