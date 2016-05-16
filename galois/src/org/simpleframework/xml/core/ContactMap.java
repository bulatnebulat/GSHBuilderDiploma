/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.simpleframework.xml.core.Contact;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ContactMap
extends LinkedHashMap<Object, Contact>
implements Iterable<Contact> {
    ContactMap() {
    }

    @Override
    public Iterator<Contact> iterator() {
        return this.values().iterator();
    }
}

