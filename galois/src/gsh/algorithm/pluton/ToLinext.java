/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm.pluton;

import gsh.types.Concept;
import gsh.types.NamingConvention;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import jcornflower.matrix.Double2D;

public class ToLinext {
    public static List<Concept> getLinearExtensions(Double2D m, List<String> maxmods) {
        ArrayList<Concept> result = new ArrayList<Concept>();
        ListIterator p = (ListIterator)maxmods.iterator();
        while (p.hasNext()) {
            ArrayList<String> foo = new ArrayList<String>();
            ArrayList<String> bar = new ArrayList<String>();
            String item = (String)p.next();
            foo.add(item);
            block1 : while (p.hasNext()) {
                String item2 = (String)p.next();
                if (m.areEquivalent(item, item2)) {
                    foo.add(item2);
                    continue;
                }
                if (m.mergesIntoAllConnectableVectors(item, item2)) {
                    bar.add(item2);
                    while (p.hasNext()) {
                        String item3 = (String)p.next();
                        if (m.areEquivalent(item2, item3)) {
                            bar.add(item3);
                            continue;
                        }
                        p.previous();
                        break block1;
                    }
                    break;
                }
                p.previous();
                break;
            }
            Concept concept = m.getObjnames().contains(item) ? new Concept(foo, bar, false, NamingConvention.html) : new Concept(bar, foo, false, NamingConvention.html);
            result.add(concept);
        }
        return result;
    }
}

