/*
 * Decompiled with CFR 0_114.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Session;

class SessionManager {
    private ThreadLocal<Reference> local = new ThreadLocal();

    public Session open() throws Exception {
        return this.open(true);
    }

    public Session open(boolean strict) throws Exception {
        Reference session = this.local.get();
        if (session != null) {
            return session.get();
        }
        return this.create(strict);
    }

    private Session create(boolean strict) throws Exception {
        Reference session = new Reference(strict);
        if (session != null) {
            this.local.set(session);
        }
        return session.get();
    }

    public void close() throws Exception {
        Reference session = this.local.get();
        if (session == null) {
            throw new PersistenceException("Session does not exist", new Object[0]);
        }
        int reference = session.clear();
        if (reference == 0) {
            this.local.remove();
        }
    }

    private static class Reference {
        private Session session;
        private int count;

        public Reference(boolean strict) {
            this.session = new Session(strict);
        }

        public Session get() {
            if (this.count >= 0) {
                ++this.count;
            }
            return this.session;
        }

        public int clear() {
            return --this.count;
        }
    }

}

