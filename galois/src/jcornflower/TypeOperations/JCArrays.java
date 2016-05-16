/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.TypeOperations;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCArrays {
    public static Object resizeArray(Object oldArray, int newSize) {
        int oldSize = Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    public static void print(int[] arr) {
        String print = "";
        int i = 0;
        while (i < arr.length) {
            print = String.valueOf(print) + arr[i] + " ";
            ++i;
        }
        System.out.println("------");
        System.out.println(print);
        System.out.println("------");
    }

    public static void print(double[][] arr) {
        int i = 0;
        while (i < arr.length) {
            int j = 0;
            while (j < arr[0].length) {
                System.out.print(String.valueOf(arr[i][j]) + "\t");
                ++j;
            }
            System.out.println();
            ++i;
        }
    }

    public static void print(double[] arr) {
        int i = 0;
        while (i < arr.length) {
            System.out.print(String.valueOf(arr[i]) + " ");
            ++i;
        }
    }

    public static List<Map> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<Map> results = new ArrayList<Map>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            results.add(row);
            int i = 1;
            while (i <= columns) {
                row.put(md.getColumnName(i), rs.getObject(i));
                ++i;
            }
        }
        return results;
    }
}

