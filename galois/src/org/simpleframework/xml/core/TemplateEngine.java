/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Template;
import org.simpleframework.xml.filter.Filter;

class TemplateEngine {
    private Template source = new Template();
    private Template name = new Template();
    private Template text = new Template();
    private Filter filter;
    private int off;

    public TemplateEngine(Filter filter) {
        this.filter = filter;
    }

    public String process(String value) {
        if (value.indexOf(36) < 0) {
            return value;
        }
        try {
            this.source.append(value);
            this.parse();
            String string = this.text.toString();
            return string;
        }
        finally {
            this.clear();
        }
    }

    private void parse() {
        while (this.off < this.source.count) {
            char next;
            if ((next = this.source.buf[this.off++]) == '$' && this.off < this.source.count) {
                if (this.source.buf[this.off++] == '{') {
                    this.name();
                    continue;
                }
                --this.off;
            }
            this.text.append(next);
        }
    }

    private void name() {
        while (this.off < this.source.count) {
            char next;
            if ((next = this.source.buf[this.off++]) == '}') {
                this.replace();
                break;
            }
            this.name.append(next);
        }
        if (this.name.length() > 0) {
            this.text.append("${");
            this.text.append(this.name);
        }
    }

    private void replace() {
        if (this.name.length() > 0) {
            this.replace(this.name);
        }
        this.name.clear();
    }

    private void replace(Template name) {
        this.replace(name.toString());
    }

    private void replace(String name) {
        String value = this.filter.replace(name);
        if (value == null) {
            this.text.append("${");
            this.text.append(name);
            this.text.append("}");
        } else {
            this.text.append(value);
        }
    }

    public void clear() {
        this.name.clear();
        this.text.clear();
        this.source.clear();
        this.off = 0;
    }
}

