/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.core.Decorator;
import org.simpleframework.xml.stream.NamespaceMap;
import org.simpleframework.xml.stream.OutputNode;

class NamespaceDecorator
implements Decorator {
    private List<Namespace> scope = new ArrayList<Namespace>();
    private Namespace primary;

    public void set(Namespace namespace) {
        if (namespace != null) {
            this.add(namespace);
        }
        this.primary = namespace;
    }

    public void add(Namespace namespace) {
        this.scope.add(namespace);
    }

    public void decorate(OutputNode node) {
        this.decorate(node, null);
    }

    public void decorate(OutputNode node, Decorator decorator) {
        if (decorator != null) {
            decorator.decorate(node);
        }
        this.scope(node);
        this.namespace(node);
    }

    private void scope(OutputNode node) {
        NamespaceMap map = node.getNamespaces();
        for (Namespace next : this.scope) {
            String reference = next.reference();
            String prefix = next.prefix();
            map.setReference(reference, prefix);
        }
    }

    private void namespace(OutputNode node) {
        if (this.primary != null) {
            String reference = this.primary.reference();
            node.setReference(reference);
        }
    }
}

