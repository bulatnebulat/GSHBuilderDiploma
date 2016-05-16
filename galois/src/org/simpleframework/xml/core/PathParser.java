/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.core.Expression;
import org.simpleframework.xml.core.PathException;
import org.simpleframework.xml.strategy.Type;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class PathParser
implements Expression {
    private LinkedList<Integer> indexes = new LinkedList();
    private LinkedList<String> prefixes = new LinkedList();
    private LinkedList<String> names = new LinkedList();
    private StringBuilder builder = new StringBuilder();
    private Cache attributes;
    private Cache elements;
    private String location;
    private String cache;
    private String path;
    private Type type;
    private boolean attribute;
    private char[] data;
    private int count;
    private int start;
    private int off;

    public PathParser(Type type, String path) throws Exception {
        this.attributes = new Cache();
        this.elements = new Cache();
        this.type = type;
        this.path = path;
        this.parse(path);
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty(this.location);
    }

    @Override
    public boolean isPath() {
        return this.names.size() > 1;
    }

    @Override
    public boolean isAttribute() {
        return this.attribute;
    }

    @Override
    public int getIndex() {
        return this.indexes.getFirst();
    }

    @Override
    public String getPrefix() {
        return this.prefixes.getFirst();
    }

    @Override
    public String getFirst() {
        return this.names.getFirst();
    }

    @Override
    public String getLast() {
        return this.names.getLast();
    }

    @Override
    public String getPath() {
        return this.location;
    }

    @Override
    public String getElement(String name) {
        String path = (String)this.elements.get(name);
        if (path == null && (path = this.getElementPath(this.location, name)) != null) {
            this.elements.put(name, path);
        }
        return path;
    }

    private String getElementPath(String path, String name) {
        if (this.isEmpty(name)) {
            return path;
        }
        if (this.isEmpty(path)) {
            return name;
        }
        return path + "/" + name + "[1]";
    }

    @Override
    public String getAttribute(String name) {
        String path = (String)this.attributes.get(name);
        if (path == null && (path = this.getAttributePath(this.location, name)) != null) {
            this.attributes.put(name, path);
        }
        return path;
    }

    private String getAttributePath(String path, String name) {
        if (this.isEmpty(path)) {
            return name;
        }
        return path + "/@" + name;
    }

    @Override
    public Iterator<String> iterator() {
        return this.names.iterator();
    }

    @Override
    public Expression getPath(int from) {
        return this.getPath(from, 0);
    }

    @Override
    public Expression getPath(int from, int trim) {
        int last = this.names.size() - 1;
        if (last - trim >= from) {
            return new PathSection(from, last - trim);
        }
        return new PathSection(from, from);
    }

    private void parse(String path) throws Exception {
        if (path != null) {
            this.count = path.length();
            this.data = new char[this.count];
            path.getChars(0, this.count, this.data, 0);
        }
        this.path();
    }

    private void path() throws Exception {
        if (this.data[this.off] == '/') {
            throw new PathException("Path '%s' in %s references document root", this.path, this.type);
        }
        if (this.data[this.off] == '.') {
            this.skip();
        }
        while (this.off < this.count) {
            if (this.attribute) {
                throw new PathException("Path '%s' in %s references an invalid attribute", this.path, this.type);
            }
            this.segment();
        }
        this.truncate();
        this.build();
    }

    private void build() {
        int count = this.names.size();
        int last = count - 1;
        for (int i = 0; i < count; ++i) {
            String segment = this.names.get(i);
            int index = this.indexes.get(i);
            if (i > 0) {
                this.builder.append('/');
            }
            if (this.attribute && i == last) {
                this.builder.append('@');
                this.builder.append(segment);
                continue;
            }
            this.builder.append(segment);
            this.builder.append('[');
            this.builder.append(index);
            this.builder.append(']');
        }
        this.location = this.builder.toString();
    }

    private void skip() throws Exception {
        if (this.data.length > 1) {
            if (this.data[this.off + 1] != '/') {
                throw new PathException("Path '%s' in %s has an illegal syntax", this.path, this.type);
            }
            ++this.off;
        }
        this.start = ++this.off;
    }

    private void segment() throws Exception {
        char first = this.data[this.off];
        if (first == '/') {
            throw new PathException("Invalid path expression '%s' in %s", this.path, this.type);
        }
        if (first == '@') {
            this.attribute();
        } else {
            this.element();
        }
        this.align();
    }

    private void element() throws Exception {
        int mark = this.off;
        int size = 0;
        while (this.off < this.count) {
            char value;
            if (!this.isValid(value = this.data[this.off++])) {
                if (value == '@') {
                    --this.off;
                    break;
                }
                if (value == '[') {
                    this.index();
                    break;
                }
                if (value == '/') break;
                throw new PathException("Illegal character '%s' in element for '%s' in %s", Character.valueOf(value), this.path, this.type);
            }
            ++size;
        }
        this.insert(mark, size);
    }

    private void attribute() throws Exception {
        int mark = ++this.off;
        while (this.off < this.count) {
            char value;
            if (this.isValid(value = this.data[this.off++])) continue;
            throw new PathException("Illegal character '%s' in attribute for '%s' in %s", Character.valueOf(value), this.path, this.type);
        }
        if (this.off <= mark) {
            throw new PathException("Attribute reference in '%s' for %s is empty", this.path, this.type);
        }
        this.attribute = true;
        this.insert(mark, this.off - mark);
    }

    private void index() throws Exception {
        int value = 0;
        if (this.data[this.off - 1] == '[') {
            char digit;
            while (this.off < this.count && this.isDigit(digit = this.data[this.off++])) {
                value *= 10;
                value += digit;
                value -= 48;
            }
        }
        if (this.data[this.off++ - 1] != ']') {
            throw new PathException("Invalid index for path '%s' in %s", this.path, this.type);
        }
        this.indexes.add(value);
    }

    private void truncate() throws Exception {
        if (this.off - 1 >= this.data.length) {
            --this.off;
        } else if (this.data[this.off - 1] == '/') {
            --this.off;
        }
    }

    private void align() throws Exception {
        int size;
        int require = this.names.size();
        if (require > (size = this.indexes.size())) {
            this.indexes.add(1);
        }
    }

    private boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    private boolean isDigit(char value) {
        return Character.isDigit(value);
    }

    private boolean isValid(char value) {
        return this.isLetter(value) || this.isSpecial(value);
    }

    private boolean isSpecial(char value) {
        return value == '_' || value == '-' || value == ':';
    }

    private boolean isLetter(char value) {
        return Character.isLetterOrDigit(value);
    }

    private void insert(int start, int count) {
        String segment = new String(this.data, start, count);
        if (count > 0) {
            this.insert(segment);
        }
    }

    private void insert(String segment) {
        int index = segment.indexOf(58);
        String prefix = null;
        if (index > 0) {
            prefix = segment.substring(0, index);
            segment = segment.substring(index + 1);
        }
        this.prefixes.add(prefix);
        this.names.add(segment);
    }

    @Override
    public String toString() {
        int size = this.off - this.start;
        if (this.cache == null) {
            this.cache = new String(this.data, this.start, size);
        }
        return this.cache;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class Cache
    extends LinkedHashMap<String, String> {
        public Cache() {
            super(16, 0.75f, false);
        }

        @Override
        public boolean removeEldestEntry(Map.Entry entry) {
            return this.size() > 16;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class PathSection
    implements Expression {
        private List<String> cache;
        private String section;
        private String path;
        private int begin;
        private int end;

        public PathSection(int index, int end) {
            this.cache = new ArrayList<String>();
            this.begin = index;
            this.end = end;
        }

        @Override
        public boolean isEmpty() {
            return this.begin == this.end;
        }

        @Override
        public boolean isPath() {
            return this.end - this.begin >= 1;
        }

        @Override
        public boolean isAttribute() {
            if (PathParser.this.attribute) {
                return this.end >= PathParser.this.names.size() - 1;
            }
            return false;
        }

        @Override
        public String getPath() {
            if (this.section == null) {
                this.section = this.getCanonicalPath();
            }
            return this.section;
        }

        @Override
        public String getElement(String name) {
            String path = this.getPath();
            if (path != null) {
                return PathParser.this.getElementPath(path, name);
            }
            return name;
        }

        @Override
        public String getAttribute(String name) {
            String path = this.getPath();
            if (path != null) {
                return PathParser.this.getAttributePath(path, name);
            }
            return name;
        }

        @Override
        public int getIndex() {
            return (Integer)PathParser.this.indexes.get(this.begin);
        }

        @Override
        public String getPrefix() {
            return (String)PathParser.this.prefixes.get(this.begin);
        }

        @Override
        public String getFirst() {
            return (String)PathParser.this.names.get(this.begin);
        }

        @Override
        public String getLast() {
            return (String)PathParser.this.names.get(this.end);
        }

        @Override
        public Expression getPath(int from) {
            return this.getPath(from, 0);
        }

        @Override
        public Expression getPath(int from, int trim) {
            return new PathSection(this.begin + from, this.end - trim);
        }

        @Override
        public Iterator<String> iterator() {
            if (this.cache.isEmpty()) {
                for (int i = this.begin; i <= this.end; ++i) {
                    String segment = (String)PathParser.this.names.get(i);
                    if (segment == null) continue;
                    this.cache.add(segment);
                }
            }
            return this.cache.iterator();
        }

        private String getCanonicalPath() {
            int start = 0;
            int last = 0;
            int pos = 0;
            for (pos = 0; pos < this.begin; ++pos) {
                start = PathParser.this.location.indexOf(47, start + 1);
            }
            last = start;
            while (pos <= this.end) {
                last = PathParser.this.location.indexOf(47, last + 1);
                if (last == -1) {
                    last = PathParser.this.location.length();
                }
                ++pos;
            }
            return PathParser.this.location.substring(start + 1, last);
        }

        private String getFragment() {
            int last = PathParser.this.start;
            int pos = 0;
            int i = 0;
            while (i <= this.end) {
                if (last >= PathParser.this.count) {
                    ++last;
                    break;
                }
                if (PathParser.this.data[last++] != '/' || ++i != this.begin) continue;
                pos = last;
            }
            return new String(PathParser.this.data, pos, --last - pos);
        }

        @Override
        public String toString() {
            if (this.path == null) {
                this.path = this.getFragment();
            }
            return this.path;
        }
    }

}

