/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.matrix;

import Jama.Matrix;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Double2D {
    private final int jamaMinXdim = 10;
    private final int jamaMinYdim = 10;
    private final List<String> objnames;
    private final List<String> attrnames;
    private Matrix matrix;

    public Double2D() {
        this.objnames = new ArrayList<String>();
        this.attrnames = new ArrayList<String>();
        this.matrix = new Matrix(10, 10);
    }

    public Double2D(List<String> objnames, List<String> attrnames, double[][] arr) {
        this.objnames = objnames;
        this.attrnames = attrnames;
        this.matrix = new Matrix(arr);
    }

    public List<String> getObjnames() {
        return this.objnames;
    }

    public List<String> getAttrnames() {
        return this.attrnames;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public static int countOtherThanZero(double[] arr) {
        int result = 0;
        int i = 0;
        while (i < arr.length) {
            if (arr[i] != 0.0) {
                ++result;
            }
            ++i;
        }
        return result;
    }

    public Double getValue(String objname, String attrname) {
        Double result = null;
        if (this.getObjnames().contains(objname) && this.getAttrnames().contains(attrname)) {
            int y = this.getObjnames().indexOf(objname);
            int x = this.getAttrnames().indexOf(attrname);
            result = this.getMatrix().get(y, x);
        }
        return result;
    }

    public List<String> getCorrespondingNames(List<String> names) {
        ArrayList<String> source = null;
        int size = 0;
        if (this.getObjnames().containsAll(names)) {
            size = this.getAttrnames().size();
            source = new ArrayList<String>(this.getAttrnames());
        } else if (this.getAttrnames().containsAll(names)) {
            size = this.getObjnames().size();
            source = new ArrayList<String>(this.getObjnames());
        }
        int[] sum = new int[size];
        ArrayList<String> result = new ArrayList<String>();
        for (String item : names) {
            double[] arr = this.getVector(item);
            int i = 0;
            while (i < arr.length) {
                if (arr[i] != 0.0) {
                    int[] arrn = sum;
                    int n = i;
                    arrn[n] = arrn[n] + 1;
                }
                ++i;
            }
        }
        int i = 0;
        while (i < sum.length) {
            if (sum[i] == names.size()) {
                result.add(source.get(i));
            }
            ++i;
        }
        return result;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public double[] getVector(String name) {
        double[] result = null;
        if (this.getObjnames().contains(name)) {
            result = new double[this.getAttrnames().size()];
            int y = this.getObjnames().indexOf(name);
            int i = 0;
            while (i < this.getAttrnames().size()) {
                result[i] = this.getMatrix().get(y, i);
                ++i;
            }
            return result;
        } else {
            if (!this.getAttrnames().contains(name)) return result;
            result = new double[this.getObjnames().size()];
            int x = this.getAttrnames().indexOf(name);
            int i = 0;
            while (i < this.getObjnames().size()) {
                result[i] = this.getMatrix().get(i, x);
                ++i;
            }
        }
        return result;
    }

    public List<String> getEquivalents(String name) {
        ArrayList<String> result = new ArrayList<String>();
        result.add(name);
        ArrayList<String> names = null;
        if (this.getObjnames().contains(name)) {
            names = new ArrayList<String>(this.getObjnames());
        } else if (this.getAttrnames().contains(name)) {
            names = new ArrayList<String>(this.getAttrnames());
        }
        int i = 0;
        while (i < names.size()) {
            if (!names.get(i).equals(name) && Double2D.areEquivalent(this.getVector(name), this.getVector(names.get(i)))) {
                result.add(names.get(i));
            }
            ++i;
        }
        return result;
    }

    public boolean hasEquivalent(String name) {
        boolean result = false;
        Iterator<String> i = null;
        if (this.getObjnames().contains(name)) {
            i = this.getObjnames().iterator();
        } else if (this.getAttrnames().contains(name)) {
            i = this.getAttrnames().iterator();
        }
        while (i.hasNext()) {
            String t = i.next();
            if (t.equals(name) || !Double2D.areEquivalent(this.getVector(t), this.getVector(name))) continue;
            result = true;
            break;
        }
        return result;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean hasParent(String name) {
        Iterator<String> i;
        if (this.getObjnames().contains(name)) {
            i = this.getObjnames().iterator();
        } else {
            if (!this.getAttrnames().contains(name)) {
                return false;
            }
            i = this.getAttrnames().iterator();
        }
        while (i.hasNext()) {
            String t = i.next();
            if (t.equals(name) || Double2D.areEquivalent(this.getVector(name), this.getVector(t)) || !this.isMergeable(name, t)) continue;
            return true;
        }
        return false;
    }

    public static boolean areEquivalent(double[] a, double[] b) {
        if (a.length > 0 && a.length == b.length) {
            int i = 0;
            while (i < a.length) {
                if (a[i] != 0.0 ^ b[i] != 0.0) {
                    return false;
                }
                ++i;
            }
        }
        return true;
    }

    public boolean areEquivalent(List<String> list) {
        boolean result = false;
        if (this.getObjnames().containsAll(list) || this.getAttrnames().containsAll(list)) {
            result = true;
            Iterator<String> p = list.iterator();
            String item = p.next();
            double[] a = this.getVector(item);
            while (p.hasNext()) {
                double[] b = this.getVector(p.next());
                if (Double2D.areEquivalent(a, b)) continue;
                return false;
            }
        }
        return result;
    }

    public boolean commonConnectionExists(List<String> a, List<String> b) {
        boolean result = false;
        if (a.size() > 0 && b.size() > 0) {
            result = true;
            for (String p : a) {
                for (String q : b) {
                    if (this.areRelated(p, q)) continue;
                    result = false;
                    break;
                }
                if (!result) break;
            }
        }
        return result;
    }

    public boolean isMergeable(String waterdrop, String ocean) {
        boolean result = false;
        if (this.getObjnames().contains(waterdrop) && this.getObjnames().contains(ocean) || this.getAttrnames().contains(waterdrop) && this.getAttrnames().contains(ocean)) {
            double[] w = this.getVector(waterdrop);
            double[] o = this.getVector(ocean);
            if (w.length > 0 && w.length == o.length) {
                result = true;
                int i = 0;
                while (i < w.length) {
                    if (w[i] != 0.0 && o[i] == 0.0) {
                        result = false;
                        break;
                    }
                    ++i;
                }
            }
        }
        return result;
    }

    public boolean isParent(String parent, String child) {
        boolean result = false;
        if ((this.getObjnames().contains(parent) && this.getObjnames().contains(child) || this.getAttrnames().contains(parent) && this.getAttrnames().contains(child)) && !Double2D.areEquivalent(this.getVector(child), this.getVector(parent)) && this.isMergeable(child, parent)) {
            result = true;
        }
        return result;
    }

    public void print(boolean seeValues) {
        String row = "";
        int i = 0;
        while (i < this.getObjnames().size()) {
            int j = 0;
            while (j < this.getAttrnames().size()) {
                double value = this.getMatrix().get(i, j);
                row = value != 0.0 ? (seeValues ? String.valueOf(row) + value : String.valueOf(row) + "x  ") : String.valueOf(row) + "   ";
                row = String.valueOf(row) + "\t";
                ++j;
            }
            System.out.println(row);
            row = "";
            ++i;
        }
    }

    public void setValue(String objname, String attrname, double value) {
        if (this.getAttrnames().contains(objname) || this.getObjnames().contains(attrname)) {
            throw new IllegalArgumentException();
        }
        this.synchNamesAndMatrixIfNecessary(objname, attrname);
        int y = this.getObjnames().indexOf(objname);
        int x = this.getAttrnames().indexOf(attrname);
        this.getMatrix().set(y, x, value);
    }

    private void synchNamesAndMatrixIfNecessary(String objname, String attrname) {
        if (!this.getObjnames().contains(objname)) {
            this.getObjnames().add(objname);
        }
        if (!this.getAttrnames().contains(attrname)) {
            this.getAttrnames().add(attrname);
        }
        this.synchMatrixIfNecessary();
    }

    private void synchMatrixIfNecessary() {
        int newobjdim = this.getObjnames().size();
        int newattrdim = this.getAttrnames().size();
        if (newobjdim != this.getMatrix().getRowDimension() || newattrdim != this.getMatrix().getColumnDimension()) {
            int ydim = Math.max(newobjdim, 10);
            int xdim = Math.max(newattrdim, 10);
            Matrix result = new Matrix(ydim, xdim);
            double[][] arr = this.getMatrix().getArray();
            int a = Math.min(newobjdim, arr.length);
            int b = Math.min(newattrdim, arr[0].length);
            int i = 0;
            while (i < a) {
                int j = 0;
                while (j < b) {
                    result.set(i, j, arr[i][j]);
                    ++j;
                }
                ++i;
            }
            this.matrix = result;
        }
    }

    public boolean setObjVector(String objname, double[] vector) {
        boolean result = false;
        if (vector.length <= this.getAttrnames().size()) {
            result = true;
            int i = 0;
            while (i < vector.length) {
                String attrname = this.getAttrnames().get(i);
                this.setValue(objname, attrname, vector[i]);
                ++i;
            }
        }
        return result;
    }

    public boolean setAttrVector(String attrname, double[] vector) {
        boolean result = false;
        if (vector.length <= this.getObjnames().size()) {
            result = true;
            int i = 0;
            while (i < vector.length) {
                String objname = this.getObjnames().get(i);
                this.setValue(objname, attrname, vector[i]);
                ++i;
            }
        }
        return result;
    }

    public List<String> getParents(String child) {
        ArrayList<String> result = new ArrayList<String>();
        Iterator<String> p = null;
        if (this.getObjnames().contains(child)) {
            p = this.getObjnames().iterator();
        } else if (this.getAttrnames().contains(child)) {
            p = this.getAttrnames().iterator();
        }
        while (p.hasNext()) {
            String parent = p.next();
            if (parent.equals(child) || !this.isParent(parent, child)) continue;
            result.add(parent);
        }
        return result;
    }

    public boolean areRelated(String a, String b) {
        boolean result = false;
        if (this.getObjnames().contains(a) && this.getAttrnames().contains(b) && this.getValue(a, b) != 0.0) {
            result = true;
        } else if (this.getAttrnames().contains(a) && this.getObjnames().contains(b) && this.getValue(b, a) != 0.0) {
            result = true;
        }
        return result;
    }

    public boolean fromSameBreed(List<String> names) {
        if (this.getObjnames().containsAll(names) || this.getAttrnames().containsAll(names)) {
            return true;
        }
        return false;
    }

    public boolean fromSameBreed(String a, String b) {
        ArrayList<String> names = new ArrayList<String>();
        names.add(a);
        names.add(b);
        return this.fromSameBreed(names);
    }

    public boolean mergesIntoAllConnectableVectors(String a, String b) {
        boolean result = false;
        if (this.areRelated(a, b)) {
            result = true;
            double[] bv = this.getVector(b);
            int i = 0;
            while (i < bv.length) {
                String key;
                if (bv[i] != 0.0 && !this.isMergeable(a, key = this.getObjnames().contains(a) ? this.getObjnames().get(i) : this.getAttrnames().get(i))) {
                    result = false;
                    break;
                }
                ++i;
            }
        }
        return result;
    }

    public boolean areEquivalent(String item, String item2) {
        List<String> test = Arrays.asList(item, item2);
        if (this.getAttrnames().containsAll(test) || this.getObjnames().containsAll(test)) {
            return this.areEquivalent(test);
        }
        return false;
    }
}

