/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm.pluton;

import com.google.common.collect.Lists;
import gsh.algorithm.GSHAlgorithm;
import gsh.algorithm.pluton.ToGSH;
import gsh.algorithm.pluton.ToLinext;
import gsh.algorithm.pluton.TomThumb;
import gsh.types.Concept;
import gsh.types.GSH;
import java.util.List;
import java.util.concurrent.ExecutionException;
import jcornflower.matrix.Double2D;

public class Pluton
implements GSHAlgorithm {
    @Override
    public GSH getGSH(Double2D matrix, boolean extendedNames) throws InterruptedException, ExecutionException {
        List<String> maxmods = Lists.reverse(TomThumb.getMaxmods(matrix));
        List<Concept> csets = ToLinext.getLinearExtensions(matrix, maxmods);
        ToGSH togsh = new ToGSH(matrix, csets, extendedNames);
        GSH result = togsh.getGSH();
        return result;
    }
}

