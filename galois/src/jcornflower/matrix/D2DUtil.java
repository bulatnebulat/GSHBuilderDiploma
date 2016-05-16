/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.matrix;

import Jama.Matrix;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import jcornflower.matrix.Double2D;
import jcornflower.matrix.xml.Relation;
import jcornflower.matrix.xml.XMLMatrix;
import jcornflower.random.JCRandom;
import org.simpleframework.xml.core.Persister;

public class D2DUtil {
    public static Double2D getContextFromTable(JTable table) {
        Double2D d2d = new Double2D();
        int rowCount = table.getModel().getRowCount();
        int colCount = table.getModel().getColumnCount();
        int i = 1;
        while (i < rowCount) {
            String objName = (String)table.getValueAt(i, 0);
            int j = 1;
            while (j < colCount) {
                String attrName = (String)table.getValueAt(0, j);
                String raw = (String)table.getValueAt(i, j);
                if ("X".equals(raw)) {
                    d2d.setValue(objName, attrName, 1.0);
                } else {
                    d2d.setValue(objName, attrName, 0.0);
                }
                ++j;
            }
            ++i;
        }
        return d2d;
    }

    public static XMLMatrix getDom(Double2D d2d) {
        XMLMatrix result = new XMLMatrix();
        List<String> objects = d2d.getObjnames();
        List<String> attributes = d2d.getAttrnames();
        ArrayList<Relation> relations = new ArrayList<Relation>();
        for (String p : objects) {
            for (String q : objects) {
                double value = d2d.getValue(p, q);
                if (value == 0.0) continue;
                relations.add(new Relation(p, q, value));
            }
        }
        result.setProperties(objects);
        result.setAttributes(attributes);
        result.setRelations(relations);
        return result;
    }

    public static Double2D getContextFromDom(XMLMatrix matrix) {
        ArrayList<String> objects = new ArrayList<String>(matrix.getProperties());
        ArrayList<String> attributes = new ArrayList<String>(matrix.getAttributes());
        double[][] arr = new double[objects.size()][attributes.size()];
        for (Relation p : matrix.getRelations()) {
            int y = objects.indexOf(p.getProperty());
            int x = attributes.indexOf(p.getAttribute());
            arr[y][x] = p.getValue();
        }
        Double2D result = new Double2D(objects, attributes, arr);
        return result;
    }

    public static Double2D getContextFromFile(File file) throws Exception {
        Persister serializer = new Persister();
        XMLMatrix dommatrix = serializer.read(XMLMatrix.class, file);
        return D2DUtil.getContextFromDom(dommatrix);
    }

    public static Double2D getEmptyContext(int objSize, int attrSize) {
        return D2DUtil.getRandomContext(objSize, attrSize, 0);
    }

    public static Double2D getRandomContext(int objSize, int attrSize) {
        int n = objSize * attrSize / 2;
        return D2DUtil.getRandomContext(objSize, attrSize, n);
    }

    public static Double2D getRandomContext(int objSize, int attrSize, int relations) {
        List<String> objects = D2DUtil.getSublist("obj_", objSize);
        List<String> attributes = D2DUtil.getSublist("attr_", attrSize);
        int n = relations;
        double[][] arr = JCRandom.getMatrix(objects.size(), attributes.size(), n, 1.0);
        Double2D result = new Double2D(objects, attributes, arr);
        return result;
    }

    private static List<String> getSublist(String prefix, int n) {
        ArrayList<String> result = new ArrayList<String>();
        int i = 0;
        while (i < n) {
            String s = String.valueOf(prefix) + i;
            result.add(s);
            ++i;
        }
        return result;
    }

    public static String[][] toString2D(Double2D d2d) {
        double[][] m = d2d.getMatrix().getArray();
        String[][] result = new String[m.length][m[0].length];
        int i = 0;
        while (i < m.length) {
            int j = 0;
            while (j < m[i].length) {
                result[i][j] = Double.toString(m[i][j]);
                ++j;
            }
            ++i;
        }
        return result;
    }
}

