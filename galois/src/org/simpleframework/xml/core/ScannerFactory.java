/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.Scanner;
import org.simpleframework.xml.core.ScannerCache;

class ScannerFactory {
    private final ScannerCache cache = new ScannerCache();

    public Scanner getInstance(Class type) throws Exception {
        Scanner schema = (Scanner)this.cache.get(type);
        if (schema == null) {
            schema = new Scanner(type);
            this.cache.put(type, schema);
        }
        return schema;
    }
}

