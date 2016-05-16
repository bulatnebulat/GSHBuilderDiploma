/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.AttributeParameter;
import org.simpleframework.xml.core.ElementArrayParameter;
import org.simpleframework.xml.core.ElementListParameter;
import org.simpleframework.xml.core.ElementMapParameter;
import org.simpleframework.xml.core.ElementParameter;
import org.simpleframework.xml.core.Parameter;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.TextParameter;

final class ParameterFactory {
    ParameterFactory() {
    }

    public static Parameter getInstance(Constructor method, Annotation label, int index) throws Exception {
        Constructor factory = ParameterFactory.getConstructor(label);
        if (!factory.isAccessible()) {
            factory.setAccessible(true);
        }
        return (Parameter)factory.newInstance(method, label, index);
    }

    private static Constructor getConstructor(Annotation label) throws Exception {
        return ParameterFactory.getBuilder(label).getConstructor();
    }

    private static PameterBuilder getBuilder(Annotation label) throws Exception {
        if (label instanceof Element) {
            return new PameterBuilder(ElementParameter.class, Element.class);
        }
        if (label instanceof ElementList) {
            return new PameterBuilder(ElementListParameter.class, ElementList.class);
        }
        if (label instanceof ElementArray) {
            return new PameterBuilder(ElementArrayParameter.class, ElementArray.class);
        }
        if (label instanceof ElementMap) {
            return new PameterBuilder(ElementMapParameter.class, ElementMap.class);
        }
        if (label instanceof Attribute) {
            return new PameterBuilder(AttributeParameter.class, Attribute.class);
        }
        if (label instanceof Text) {
            return new PameterBuilder(TextParameter.class, Text.class);
        }
        throw new PersistenceException("Annotation %s not supported", label);
    }

    private static class PameterBuilder {
        public Class create;
        public Class type;

        public PameterBuilder(Class create, Class type) {
            this.create = create;
            this.type = type;
        }

        public Constructor getConstructor() throws Exception {
            return this.getConstructor(Constructor.class, this.type, Integer.TYPE);
        }

        private /* varargs */ Constructor getConstructor(Class ... types) throws Exception {
            return this.create.getConstructor(types);
        }
    }

}

