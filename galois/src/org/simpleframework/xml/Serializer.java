/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public interface Serializer {
    public <T> T read(Class<? extends T> var1, String var2) throws Exception;

    public <T> T read(Class<? extends T> var1, File var2) throws Exception;

    public <T> T read(Class<? extends T> var1, InputStream var2) throws Exception;

    public <T> T read(Class<? extends T> var1, Reader var2) throws Exception;

    public <T> T read(Class<? extends T> var1, InputNode var2) throws Exception;

    public <T> T read(Class<? extends T> var1, String var2, boolean var3) throws Exception;

    public <T> T read(Class<? extends T> var1, File var2, boolean var3) throws Exception;

    public <T> T read(Class<? extends T> var1, InputStream var2, boolean var3) throws Exception;

    public <T> T read(Class<? extends T> var1, Reader var2, boolean var3) throws Exception;

    public <T> T read(Class<? extends T> var1, InputNode var2, boolean var3) throws Exception;

    public <T> T read(T var1, String var2) throws Exception;

    public <T> T read(T var1, File var2) throws Exception;

    public <T> T read(T var1, InputStream var2) throws Exception;

    public <T> T read(T var1, Reader var2) throws Exception;

    public <T> T read(T var1, InputNode var2) throws Exception;

    public <T> T read(T var1, String var2, boolean var3) throws Exception;

    public <T> T read(T var1, File var2, boolean var3) throws Exception;

    public <T> T read(T var1, InputStream var2, boolean var3) throws Exception;

    public <T> T read(T var1, Reader var2, boolean var3) throws Exception;

    public <T> T read(T var1, InputNode var2, boolean var3) throws Exception;

    public boolean validate(Class var1, String var2) throws Exception;

    public boolean validate(Class var1, File var2) throws Exception;

    public boolean validate(Class var1, InputStream var2) throws Exception;

    public boolean validate(Class var1, Reader var2) throws Exception;

    public boolean validate(Class var1, InputNode var2) throws Exception;

    public boolean validate(Class var1, String var2, boolean var3) throws Exception;

    public boolean validate(Class var1, File var2, boolean var3) throws Exception;

    public boolean validate(Class var1, InputStream var2, boolean var3) throws Exception;

    public boolean validate(Class var1, Reader var2, boolean var3) throws Exception;

    public boolean validate(Class var1, InputNode var2, boolean var3) throws Exception;

    public void write(Object var1, File var2) throws Exception;

    public void write(Object var1, OutputStream var2) throws Exception;

    public void write(Object var1, Writer var2) throws Exception;

    public void write(Object var1, OutputNode var2) throws Exception;
}

