/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Iterator;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.Label;
import org.simpleframework.xml.core.LabelMap;
import org.simpleframework.xml.core.Model;
import org.simpleframework.xml.core.ModelList;
import org.simpleframework.xml.core.ModelMap;
import org.simpleframework.xml.core.Section;
import org.simpleframework.xml.stream.Style;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ModelSection
implements Section {
    private LabelMap attributes;
    private LabelMap elements;
    private ModelMap models;
    private Context context;
    private Style style;
    private Model model;

    public ModelSection(Context context, Model model) {
        this.style = context.getStyle();
        this.context = context;
        this.model = model;
    }

    @Override
    public String getName() {
        return this.model.getName();
    }

    @Override
    public String getPrefix() {
        return this.model.getPrefix();
    }

    @Override
    public String getPath(String name) throws Exception {
        Expression path = this.model.getExpression();
        if (path == null) {
            return name;
        }
        return path.getElement(name);
    }

    @Override
    public String getAttribute(String name) throws Exception {
        Expression path = this.model.getExpression();
        if (path == null) {
            return name;
        }
        return path.getAttribute(name);
    }

    @Override
    public Iterator<String> iterator() {
        ArrayList<String> list = new ArrayList<String>();
        for (String element : this.model) {
            String name = this.style.getElement(element);
            if (name == null) continue;
            list.add(name);
        }
        return list.iterator();
    }

    @Override
    public boolean isSection(String name) throws Exception {
        return this.getModels().get(name) != null;
    }

    public ModelMap getModels() throws Exception {
        if (this.models == null) {
            this.models = this.model.buildModels(this.context);
        }
        return this.models;
    }

    @Override
    public Label getText() throws Exception {
        return this.model.getText();
    }

    @Override
    public LabelMap getAttributes() throws Exception {
        if (this.attributes == null) {
            this.attributes = this.model.buildAttributes(this.context);
        }
        return this.attributes;
    }

    @Override
    public LabelMap getElements() throws Exception {
        if (this.elements == null) {
            this.elements = this.model.buildElements(this.context);
        }
        return this.elements;
    }

    @Override
    public Label getElement(String name) throws Exception {
        return this.getElements().getLabel(name);
    }

    @Override
    public Section getSection(String name) throws Exception {
        Model model;
        ModelMap map = this.getModels();
        ModelList list = (ModelList)map.get(name);
        if (list != null && (model = list.take()) != null) {
            return new ModelSection(this.context, model);
        }
        return null;
    }
}

