/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Context;
import org.simpleframework.xml.core.EmptyMatcher;
import org.simpleframework.xml.core.Session;
import org.simpleframework.xml.core.SessionManager;
import org.simpleframework.xml.core.Source;
import org.simpleframework.xml.core.Support;
import org.simpleframework.xml.core.Traverser;
import org.simpleframework.xml.filter.Filter;
import org.simpleframework.xml.filter.PlatformFilter;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Style;
import org.simpleframework.xml.transform.Matcher;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Persister
implements Serializer {
    private final SessionManager manager;
    private final Strategy strategy;
    private final Support support;
    private final Format format;
    private final Style style;

    public Persister() {
        this(new HashMap());
    }

    public Persister(Format format) {
        this((Strategy)new TreeStrategy(), format);
    }

    public Persister(Map filter) {
        this(new PlatformFilter(filter));
    }

    public Persister(Map filter, Format format) {
        this(new PlatformFilter(filter));
    }

    public Persister(Filter filter) {
        this((Strategy)new TreeStrategy(), filter);
    }

    public Persister(Filter filter, Format format) {
        this((Strategy)new TreeStrategy(), filter, format);
    }

    public Persister(Matcher matcher) {
        this((Strategy)new TreeStrategy(), matcher);
    }

    public Persister(Matcher matcher, Format format) {
        this((Strategy)new TreeStrategy(), matcher, format);
    }

    public Persister(Strategy strategy) {
        this(strategy, new HashMap());
    }

    public Persister(Strategy strategy, Format format) {
        this(strategy, new HashMap(), format);
    }

    public Persister(Filter filter, Matcher matcher) {
        this((Strategy)new TreeStrategy(), filter, matcher);
    }

    public Persister(Filter filter, Matcher matcher, Format format) {
        this(new TreeStrategy(), filter, matcher, format);
    }

    public Persister(Strategy strategy, Map data) {
        this(strategy, new PlatformFilter(data));
    }

    public Persister(Strategy strategy, Map data, Format format) {
        this(strategy, (Filter)new PlatformFilter(data), format);
    }

    public Persister(Strategy strategy, Filter filter) {
        this(strategy, filter, new Format());
    }

    public Persister(Strategy strategy, Filter filter, Format format) {
        this(strategy, filter, new EmptyMatcher(), format);
    }

    public Persister(Strategy strategy, Matcher matcher) {
        this(strategy, (Filter)new PlatformFilter(), matcher);
    }

    public Persister(Strategy strategy, Matcher matcher, Format format) {
        this(strategy, new PlatformFilter(), matcher, format);
    }

    public Persister(Strategy strategy, Filter filter, Matcher matcher) {
        this(strategy, filter, matcher, new Format());
    }

    public Persister(Strategy strategy, Filter filter, Matcher matcher, Format format) {
        this.support = new Support(filter, matcher);
        this.manager = new SessionManager();
        this.style = format.getStyle();
        this.strategy = strategy;
        this.format = format;
    }

    @Override
    public <T> T read(Class<? extends T> type, String source) throws Exception {
        return (T)this.read((T)type, source, true);
    }

    @Override
    public <T> T read(Class<? extends T> type, File source) throws Exception {
        return (T)this.read((T)type, source, true);
    }

    @Override
    public <T> T read(Class<? extends T> type, InputStream source) throws Exception {
        return (T)this.read((T)type, source, true);
    }

    @Override
    public <T> T read(Class<? extends T> type, Reader source) throws Exception {
        return (T)this.read((T)type, source, true);
    }

    @Override
    public <T> T read(Class<? extends T> type, InputNode source) throws Exception {
        return (T)this.read((T)type, source, true);
    }

    @Override
    public <T> T read(Class<? extends T> type, String source, boolean strict) throws Exception {
        return (T)this.read((T)type, (Reader)new StringReader(source), strict);
    }

    @Override
    public <T> T read(Class<? extends T> type, File source, boolean strict) throws Exception {
        FileInputStream file = new FileInputStream(source);
        try {
            Class<? extends T> class_ = this.read((T)type, (InputStream)file, strict);
            return (T)class_;
        }
        finally {
            file.close();
        }
    }

    @Override
    public <T> T read(Class<? extends T> type, InputStream source, boolean strict) throws Exception {
        return (T)this.read((T)type, NodeBuilder.read(source), strict);
    }

    @Override
    public <T> T read(Class<? extends T> type, Reader source, boolean strict) throws Exception {
        return (T)this.read((T)type, NodeBuilder.read(source), strict);
    }

    @Override
    public <T> T read(Class<? extends T> type, InputNode node, boolean strict) throws Exception {
        Session session = this.manager.open(strict);
        try {
            Class<? extends T> class_ = this.read((T)type, node, session);
            return (T)class_;
        }
        finally {
            this.manager.close();
        }
    }

    private <T> T read(Class<? extends T> type, InputNode node, Session session) throws Exception {
        return (T)this.read((T)type, node, (Context)new Source(this.strategy, this.support, this.style, session));
    }

    private <T> T read(Class<? extends T> type, InputNode node, Context context) throws Exception {
        return (T)new Traverser(context).read(node, type);
    }

    @Override
    public <T> T read(T value, String source) throws Exception {
        return this.read(value, source, true);
    }

    @Override
    public <T> T read(T value, File source) throws Exception {
        return this.read(value, source, true);
    }

    @Override
    public <T> T read(T value, InputStream source) throws Exception {
        return this.read(value, source, true);
    }

    @Override
    public <T> T read(T value, Reader source) throws Exception {
        return this.read(value, source, true);
    }

    @Override
    public <T> T read(T value, InputNode source) throws Exception {
        return this.read(value, source, true);
    }

    @Override
    public <T> T read(T value, String source, boolean strict) throws Exception {
        return this.read(value, (Reader)new StringReader(source), strict);
    }

    @Override
    public <T> T read(T value, File source, boolean strict) throws Exception {
        FileInputStream file = new FileInputStream(source);
        try {
            T t = this.read(value, (InputStream)file, strict);
            return t;
        }
        finally {
            file.close();
        }
    }

    @Override
    public <T> T read(T value, InputStream source, boolean strict) throws Exception {
        return this.read(value, NodeBuilder.read(source), strict);
    }

    @Override
    public <T> T read(T value, Reader source, boolean strict) throws Exception {
        return this.read(value, NodeBuilder.read(source), strict);
    }

    @Override
    public <T> T read(T value, InputNode node, boolean strict) throws Exception {
        Session session = this.manager.open(strict);
        try {
            T t = this.read(value, node, session);
            return t;
        }
        finally {
            this.manager.close();
        }
    }

    private <T> T read(T value, InputNode node, Session session) throws Exception {
        return this.read(value, node, (Context)new Source(this.strategy, this.support, this.style, session));
    }

    private <T> T read(T value, InputNode node, Context context) throws Exception {
        return (T)new Traverser(context).read(node, value);
    }

    @Override
    public boolean validate(Class type, String source) throws Exception {
        return this.validate(type, source, true);
    }

    @Override
    public boolean validate(Class type, File source) throws Exception {
        return this.validate(type, source, true);
    }

    @Override
    public boolean validate(Class type, InputStream source) throws Exception {
        return this.validate(type, source, true);
    }

    @Override
    public boolean validate(Class type, Reader source) throws Exception {
        return this.validate(type, source, true);
    }

    @Override
    public boolean validate(Class type, InputNode source) throws Exception {
        return this.validate(type, source, true);
    }

    @Override
    public boolean validate(Class type, String source, boolean strict) throws Exception {
        return this.validate(type, new StringReader(source), strict);
    }

    @Override
    public boolean validate(Class type, File source, boolean strict) throws Exception {
        FileInputStream file = new FileInputStream(source);
        try {
            boolean bl = this.validate(type, file, strict);
            return bl;
        }
        finally {
            file.close();
        }
    }

    @Override
    public boolean validate(Class type, InputStream source, boolean strict) throws Exception {
        return this.validate(type, NodeBuilder.read(source), strict);
    }

    @Override
    public boolean validate(Class type, Reader source, boolean strict) throws Exception {
        return this.validate(type, NodeBuilder.read(source), strict);
    }

    @Override
    public boolean validate(Class type, InputNode node, boolean strict) throws Exception {
        Session session = this.manager.open(strict);
        try {
            boolean bl = this.validate(type, node, session);
            return bl;
        }
        finally {
            this.manager.close();
        }
    }

    private boolean validate(Class type, InputNode node, Session session) throws Exception {
        return this.validate(type, node, new Source(this.strategy, this.support, this.style, session));
    }

    private boolean validate(Class type, InputNode node, Context context) throws Exception {
        return new Traverser(context).validate(node, type);
    }

    @Override
    public void write(Object source, OutputNode root) throws Exception {
        Session session = this.manager.open();
        try {
            this.write(source, root, session);
        }
        finally {
            this.manager.close();
        }
    }

    private void write(Object source, OutputNode root, Session session) throws Exception {
        this.write(source, root, new Source(this.strategy, this.support, this.style, session));
    }

    private void write(Object source, OutputNode node, Context context) throws Exception {
        new Traverser(context).write(node, source);
    }

    @Override
    public void write(Object source, File out) throws Exception {
        FileOutputStream file = new FileOutputStream(out);
        try {
            this.write(source, file);
        }
        finally {
            file.close();
        }
    }

    @Override
    public void write(Object source, OutputStream out) throws Exception {
        this.write(source, out, "utf-8");
    }

    public void write(Object source, OutputStream out, String charset) throws Exception {
        this.write(source, new OutputStreamWriter(out, charset));
    }

    @Override
    public void write(Object source, Writer out) throws Exception {
        this.write(source, NodeBuilder.write(out, this.format));
    }
}

