/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.matrix.xml;

import java.util.List;
import jcornflower.matrix.xml.Relation;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class XMLMatrix {
    @ElementList(entry="property")
    private List<String> properties;
    @ElementList(entry="attribute")
    private List<String> attributes;
    @ElementList
    private List<Relation> relations;

    public List<String> getAttributes() {
        return this.attributes;
    }

    public List<Relation> getRelations() {
        return this.relations;
    }

    public List<String> getProperties() {
        return this.properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}

