/*
 * Decompiled with CFR 0_114.
 */
package com.touchgraph.graphlayout;

public class TGException
extends Exception {
    public static final int NODE_EXISTS = 1;
    public static final int NODE_DOESNT_EXIST = 2;
    public static final int NODE_NO_ID = 3;
    protected int id = -1;
    public Exception exception = null;

    public TGException(int n) {
        this.id = n;
    }

    public TGException(int n, String string) {
        super(string);
        this.id = n;
    }

    public TGException(String string) {
        super(string);
    }

    public TGException(Exception exception) {
        this.exception = exception;
    }

    public int getId() {
        return this.id;
    }
}

