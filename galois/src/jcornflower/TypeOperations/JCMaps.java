/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.TypeOperations;

import java.io.PrintStream;
import java.util.Map;

public class JCMaps {
    public static int keyOfFirstOccurence(Map map, Object value) {
        int result = -1;
        if (map != null) {
            int i = 0;
            while (i < map.size()) {
                if (map.get(i) == value) {
                    result = i;
                }
                ++i;
            }
        }
        return result;
    }

    public static void print(Map out) {
        System.out.println("----------");
        if (out != null && out.size() > 0) {
            int i = 0;
            while (i < out.size()) {
                System.out.println(out.get(i));
                ++i;
            }
        }
        System.out.println("----------");
    }
}

