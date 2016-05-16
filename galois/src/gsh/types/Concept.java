/*
 * Decompiled with CFR 0_114.
 */
package gsh.types;

import gsh.types.NamingConvention;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jcornflower.TypeOperations.JCLists;

public class Concept {
    private final List<String> intents;
    private final List<String> extents;
    private boolean isExtended;
    private NamingConvention namingConvention;
    private int level = 0;
    private static /* synthetic */ int[] $SWITCH_TABLE$gsh$types$NamingConvention;

    public Concept(List<String> extents, List<String> intents, boolean isExtended, NamingConvention namingConvention) {
        this.extents = extents;
        this.intents = intents;
        this.isExtended = isExtended;
        this.namingConvention = namingConvention;
    }

    public Concept(boolean isExtended, NamingConvention namingConvention) {
        this(new ArrayList<String>(), new ArrayList<String>(), isExtended, namingConvention);
    }

    public Concept(boolean isExtended) {
        this(new ArrayList<String>(), new ArrayList<String>(), isExtended, NamingConvention.html);
    }

    public NamingConvention getNamingConvention() {
        return this.namingConvention;
    }

    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public boolean isExtended() {
        return this.isExtended;
    }

    public void setExtended(boolean isExtended) {
        this.isExtended = isExtended;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString() {
        switch (Concept.$SWITCH_TABLE$gsh$types$NamingConvention()[this.namingConvention.ordinal()]) {
            case 3: {
                return this.einame();
            }
            case 2: {
                return this.eiSimpleName();
            }
        }
        return this.htmlName();
    }

    private String einame() {
        String result = "E={" + JCLists.join(this.extents, ",") + "} I={" + JCLists.join(this.intents, ",") + "}";
        return result;
    }

    private String eiSimpleName() {
        String result = "(";
        if (this.extents.size() > 0) {
            result = String.valueOf(result) + JCLists.join(this.extents, ",");
        }
        if (this.intents.size() > 0) {
            result = String.valueOf(result) + JCLists.join(this.intents, ",");
        }
        result = String.valueOf(result) + ")";
        return result;
    }

    private String htmlName() {
        ArrayList<String> labels = new ArrayList<String>();
        if (this.intents.size() > 0) {
            labels.add(JCLists.join(this.intents, "<br>"));
        }
        if (this.extents.size() > 0) {
            labels.add("<i>" + JCLists.join(this.extents, "<br>") + "</i>");
        }
        String result = "<html>" + JCLists.join(labels, "<br><br>") + "</html>";
        return result;
    }

    public List<String> getIntents() {
        return this.intents;
    }

    public List<String> getExtents() {
        return this.extents;
    }

    public int getWidth() {
        int length;
        int result = 0;
        Iterator<String> p = this.getExtents().iterator();
        while (p.hasNext()) {
            length = p.next().length();
            if (length <= result) continue;
            result = length;
        }
        p = this.getIntents().iterator();
        while (p.hasNext()) {
            length = p.next().length();
            if (length <= result) continue;
            result = length;
        }
        return result;
    }

    public int getHeight() {
        return this.getIntents().size() + this.getExtents().size();
    }

    static /* synthetic */ int[] $SWITCH_TABLE$gsh$types$NamingConvention() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$gsh$types$NamingConvention;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[NamingConvention.values().length];
        try {
            arrn[NamingConvention.ei.ordinal()] = 3;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[NamingConvention.eiSimple.ordinal()] = 2;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[NamingConvention.html.ordinal()] = 1;
        }
        catch (NoSuchFieldError v3) {}
        $SWITCH_TABLE$gsh$types$NamingConvention = arrn;
        return $SWITCH_TABLE$gsh$types$NamingConvention;
    }
}

