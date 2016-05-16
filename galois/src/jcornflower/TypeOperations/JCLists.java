/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.TypeOperations;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JCLists {
    public static void print(List out) {
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

    public static String join(List list, String delimiter) {
        String result = "";
        int i = 0;
        while (i < list.size()) {
            if (i != 0) {
                result = String.valueOf(result) + delimiter;
            }
            result = String.valueOf(result) + list.get(i).toString();
            ++i;
        }
        return result;
    }

    public static List<String> merge(List<String> a, List<String> b) {
        ArrayList<String> result = new ArrayList<String>(a);
        result.removeAll(b);
        result.addAll(b);
        return result;
    }
}

