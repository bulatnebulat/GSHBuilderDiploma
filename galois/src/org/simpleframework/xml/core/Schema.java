/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;
import org.simpleframework.xml.core.Caller;
import org.simpleframework.xml.core.Creator;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.Section;

interface Schema {
    public boolean isPrimitive();

    public Creator getCreator();

    public Label getVersion();

    public Version getRevision();

    public Decorator getDecorator();

    public Caller getCaller();

    public Section getSection();

    public Label getText();
}

