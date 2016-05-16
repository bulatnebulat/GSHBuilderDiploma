/*
 * Decompiled with CFR 0_114.
 */
package gsh.algorithm.pluton;

import gsh.algorithm.pluton.EdgeFinder;
import gsh.algorithm.pluton.SetType;
import gsh.types.Concept;
import gsh.types.Edge;
import gsh.types.GSH;
import gsh.types.NamingConvention;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import jcornflower.matrix.Double2D;

public final class ToGSH {
    private final Double2D context;
    private final List<Concept> csets;
    private final Iterator<Concept> csetsIterator;
    private final boolean extendedNames;
    private final Map<Concept, List<Concept>> connections = new HashMap<Concept, List<Concept>>();
    private final GSH gsh = new GSH();

    public ToGSH(Double2D context, List<Concept> csets, boolean extendedNames) throws InterruptedException, ExecutionException {
        this.context = context;
        this.csets = csets;
        this.csetsIterator = csets.iterator();
        this.extendedNames = extendedNames;
        this.solve();
    }

    public synchronized Concept nextCset() {
        if (this.csetsIterator.hasNext()) {
            return this.csetsIterator.next();
        }
        return null;
    }

    public Double2D getContext() {
        return this.context;
    }

    public List<Concept> getCsets() {
        return this.csets;
    }

    public boolean isExtendedNames() {
        return this.extendedNames;
    }

    public Map<Concept, List<Concept>> getConnections() {
        return this.connections;
    }

    public GSH getGSH() {
        return this.gsh;
    }

    private void solve() throws InterruptedException, ExecutionException {
        int poolSize = (int)Math.ceil(this.csets.size() / 5);
        ExecutorService service = Executors.newFixedThreadPool(++poolSize);
        ArrayList<Future> futures = new ArrayList<Future>();
        int n = 0;
        while (n < poolSize) {
            Future f = service.submit(new EdgeFinder(this));
            futures.add(f);
            ++n;
        }
        for (Future f : futures) {
            f.get();
        }
        futures.clear();
        service.shutdownNow();
        for (Concept p : this.connections.keySet()) {
            this.gsh.getConcepts().add(p);
            for (Concept q : this.connections.get(p)) {
                this.gsh.getEdges().add(new Edge(p, q));
            }
        }
    }

    public static Concept getNormalized(Double2D m, Concept concept, boolean extendedNames) {
        Concept extended = ToGSH.getExtendedConcept(m, concept);
        Concept result = extendedNames ? extended : concept;
        result.setLevel(extended.getExtents().size());
        return result;
    }

    public static SetType getParentType(Double2D m, Concept child, Concept parent) {
        if (!child.isExtended()) {
            child = ToGSH.getExtendedConcept(m, child);
        }
        if (!parent.isExtended()) {
            parent = ToGSH.getExtendedConcept(m, parent);
        }
        if (parent.getExtents().containsAll(child.getExtents())) {
            return SetType.extent;
        }
        if (parent.getIntents().containsAll(child.getIntents())) {
            return SetType.intent;
        }
        return null;
    }

    private static Concept getExtendedConcept(Double2D m, Concept concept) {
        List<String> i;
        List<String> e;
        if (concept.getExtents().size() == 0) {
            e = m.getCorrespondingNames(concept.getIntents());
            i = m.getCorrespondingNames(e);
        } else {
            i = m.getCorrespondingNames(concept.getExtents());
            e = m.getCorrespondingNames(i);
        }
        return new Concept(m.getCorrespondingNames(i), m.getCorrespondingNames(e), true, NamingConvention.eiSimple);
    }

    public synchronized void makeConnectable(Concept item) {
        this.connections.put(item, new ArrayList());
    }

    public synchronized void addEdge(Concept concept, Concept concept2) {
        this.connections.get(concept).add(concept2);
    }
}

