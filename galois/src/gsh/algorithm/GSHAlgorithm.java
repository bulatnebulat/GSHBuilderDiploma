/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm;

import gsh.types.GSH;
import java.util.concurrent.ExecutionException;
import jcornflower.matrix.Double2D;

public interface GSHAlgorithm {
    public GSH getGSH(Double2D var1, boolean var2) throws InterruptedException, ExecutionException;
}

