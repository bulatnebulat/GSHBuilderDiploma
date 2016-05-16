/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

class Template {
    protected String cache;
    protected char[] buf;
    protected int count;

    public Template() {
        this(16);
    }

    public Template(int size) {
        this.buf = new char[size];
    }

    public void append(char c) {
        this.ensureCapacity(this.count + 1);
        this.buf[this.count++] = c;
    }

    public void append(String str) {
        this.ensureCapacity(this.count + str.length());
        str.getChars(0, str.length(), this.buf, this.count);
        this.count += str.length();
    }

    public void append(Template text) {
        this.append(text.buf, 0, text.count);
    }

    public void append(char[] c, int off, int len) {
        this.ensureCapacity(this.count + len);
        System.arraycopy(c, off, this.buf, this.count, len);
        this.count += len;
    }

    public void append(String str, int off, int len) {
        this.ensureCapacity(this.count + len);
        str.getChars(off, len, this.buf, this.count);
        this.count += len;
    }

    public void append(Template text, int off, int len) {
        this.append(text.buf, off, len);
    }

    protected void ensureCapacity(int min) {
        if (this.buf.length < min) {
            int size = this.buf.length * 2;
            int max = Math.max(min, size);
            char[] temp = new char[max];
            System.arraycopy(this.buf, 0, temp, 0, this.count);
            this.buf = temp;
        }
    }

    public void clear() {
        this.cache = null;
        this.count = 0;
    }

    public int length() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }
}

