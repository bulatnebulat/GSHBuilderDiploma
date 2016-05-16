/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm.pluton;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import jcornflower.matrix.Double2D;

public class TomThumb {
    private static ArrayList<String> order(Double2D m, ArrayList<String> names, boolean increasing) {
        Integer key;
        ArrayList<String> result = new ArrayList<String>();
        HashMap map = new HashMap();
        int vectorsize = m.getObjnames().containsAll(names) ? m.getAttrnames().size() : m.getObjnames().size();
        for (String item : names) {
            key = new Integer(Double2D.countOtherThanZero(m.getVector(item)));
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList());
            }
            ((ArrayList)map.get(key)).add(item);
        }
        if (increasing) {
            int i = 0;
            while (i <= vectorsize) {
                key = new Integer(i);
                if (map.containsKey(key)) {
                    result.addAll((Collection)map.get(key));
                }
                ++i;
            }
        } else {
            int i = vectorsize;
            while (i >= 0) {
                key = new Integer(i);
                if (map.containsKey(key)) {
                    result.addAll((Collection)map.get(key));
                }
                --i;
            }
        }
        return result;
    }

    public static List<String> maxmodPartition(Double2D m, List<String> ask) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> answer = null;
        answer = m.getObjnames().containsAll(ask) ? new ArrayList<String>(m.getAttrnames()) : new ArrayList<String>(m.getObjnames());
        for (String item : ask) {
            ArrayList<String> temp = new ArrayList<String>();
            double[] vector = m.getVector(item);
            int j = 0;
            while (j < vector.length) {
                if (vector[j] != 0.0 && answer.get(j) != null) {
                    temp.add(answer.get(j));
                    answer.set(j, null);
                }
                ++j;
            }
            if (temp.size() > 1) {
                temp = TomThumb.order(m, temp, false);
            }
            result.addAll(temp);
        }
        return result;
    }

    public static List<String> getMaxmods(Double2D m) {
        List<String> extents = Lists.reverse(TomThumb.maxmodPartition(m, m.getAttrnames()));
        List<String> intents = TomThumb.maxmodPartition(m, extents);
        ArrayList<String> result = new ArrayList<String>();
        int i = 0;
        int sets = extents.size() + intents.size();
        while (i < sets) {
            String extent;
            block3 : {
                if (extents.size() <= 0) continue;
                extent = extents.get(0);
                while (intents.size() > 0) {
                    String intent = intents.get(0);
                    if (m.getValue(extent, intent) != 0.0) {
                        result.add(intent);
                        intents.remove(intent);
                        continue;
                    }
                    break block3;
                }
                result.addAll(extents);
                break;
            }
            result.add(extent);
            extents.remove(extent);
            i = result.size();
        }
        return result;
    }
}

