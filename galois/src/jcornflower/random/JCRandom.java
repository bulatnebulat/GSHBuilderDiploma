/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JCRandom {
    public static int between(int start, int end) {
        int result = 0;
        if (++end > start) {
            Random rand = new Random();
            int interval = Math.abs(end - start);
            result = start + rand.nextInt(interval);
        } else if (end == start) {
            result = start;
        } else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public static int integer(int seed) {
        Random rand = new Random();
        return rand.nextInt(seed);
    }

    public static double[][] getMatrix(int ydim, int xdim, int count, double value) {
        double[][] result = new double[ydim][xdim];
        ArrayList<Integer> keys = new ArrayList<Integer>();
        int n = ydim * xdim;
        int i = 0;
        while (i < n) {
            keys.add(i);
            ++i;
        }
        Collections.shuffle(keys);
        int i2 = 0;
        while (i2 < count) {
            int pos = (Integer)keys.get(i2);
            int x = pos % xdim;
            int y = (pos - x) / xdim;
            result[y][x] = value;
            ++i2;
        }
        return result;
    }
}

