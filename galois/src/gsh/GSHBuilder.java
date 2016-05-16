/*
 * Decompiled with CFR 0_114.
 */
package gsh;

import gsh.algorithm.GSHAlgorithm;
import gsh.types.GSH;
import java.util.concurrent.ExecutionException;
import jcornflower.matrix.Double2D;

public class GSHBuilder {
    private GSHAlgorithm gshs;

    public GSHBuilder(GSHAlgorithm gshs) {
        this.gshs = gshs;
    }

    public GSHAlgorithm getGshs() {
        return this.gshs;
    }

    public void setGshs(GSHAlgorithm gshs) {
        this.gshs = gshs;
    }

    public GSH generate(Double2D matrix, boolean extendedNames) throws InterruptedException, ExecutionException {
        return this.gshs.getGSH(matrix, extendedNames);
    }
}

