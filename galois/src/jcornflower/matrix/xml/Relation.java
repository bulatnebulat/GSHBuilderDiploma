/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.matrix.xml;

import org.simpleframework.xml.Element;

public class Relation {
    @Element
    private String property;
    @Element
    private String attribute;
    @Element
    private double value;

    public Relation() {
    }

    public Relation(String property, String attribute, double value) {
        this.property = property;
        this.attribute = attribute;
        this.value = value;
    }

    public String getProperty() {
        return this.property;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public double getValue() {
        return this.value;
    }
}

