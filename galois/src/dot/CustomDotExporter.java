/*
 * Decompiled with CFR 0_114.
 */
package dot;

import gsh.types.Concept;
import gsh.types.Edge;
import gsh.types.GSH;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.ResourceBundle;
import jcornflower.TypeOperations.JCLists;

public class CustomDotExporter {
    public static void export(File file, ResourceBundle bundle, GSH gsh) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(file));
        String nl = System.getProperty("line.separator");
        String nodeshape = bundle.getString("nodeshape");
        String graphtype = bundle.getString("graphtype");
        out.println(String.valueOf(graphtype) + " GSH {");
        out.println("\tnode [shape=" + nodeshape + "]" + nl);
        for (Concept p2 : gsh.getConcepts()) {
            out.println(CustomDotExporter.getVertex(p2, bundle));
        }
        out.println();
        for (Edge p : gsh.getEdges()) {
            out.println(CustomDotExporter.getEdge(p, bundle));
        }
        out.println("}");
        out.flush();
    }

    private static String getEdge(Edge concept, ResourceBundle bundle) {
        String graphtype = bundle.getString("graphtype");
        String connector = "digraph".equals(graphtype) ? " -> " : " -- ";
        String result = "\"" + concept.getChild().toString() + "\" " + connector + " \"" + concept.getParent().toString() + "\";";
        return result;
    }

    private static String getVertex(Concept concept, ResourceBundle bundle) {
        String cellspacing = bundle.getString("cellspacing");
        String attributes_bgcolor = bundle.getString("attributes_bgcolor");
        String objects_bgcolor = bundle.getString("objects_bgcolor");
        String result = " \"" + concept.toString() + "\" [label=<<table cellspacing=\"" + cellspacing + "\">";
        if (concept.getExtents().size() > 0) {
            result = String.valueOf(result) + "<tr><td bgcolor=\"" + objects_bgcolor + "\" id=\"objects\">" + JCLists.join(concept.getExtents(), "<br/>") + "</td>" + "</tr>";
        }
        if (concept.getIntents().size() > 0) {
            result = String.valueOf(result) + "<tr><td bgcolor=\"" + attributes_bgcolor + "\" id=\"attributes\">" + JCLists.join(concept.getIntents(), "<br/>") + "</td>" + "</tr>";
        }
        result = String.valueOf(result) + "</table>>];";
        return result;
    }
}

