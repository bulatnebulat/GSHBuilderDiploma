/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;

interface Section
extends Iterable<String> {
    public String getName();

    public String getPrefix();

    public Label getText() throws Exception;

    public LabelMap getElements() throws Exception;

    public LabelMap getAttributes() throws Exception;

    public Label getElement(String var1) throws Exception;

    public Section getSection(String var1) throws Exception;

    public String getPath(String var1) throws Exception;

    public String getAttribute(String var1) throws Exception;

    public boolean isSection(String var1) throws Exception;
}

