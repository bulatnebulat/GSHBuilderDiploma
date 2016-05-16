/*
 * Decompiled with CFR 0_114.
 */
package gui;

import javax.swing.table.DefaultTableModel;

public class ContextTableModel
extends DefaultTableModel {
    private static final long serialVersionUID = 975088719461909711L;

    public ContextTableModel(String[][] data) {
        this.setColumnCount(data[0].length);
        this.setRowCount(data.length);
        int i = 0;
        while (i < data.length) {
            int j = 0;
            while (j < data[i].length) {
                this.setValueAt(data[i][j], i, j);
                ++j;
            }
            ++i;
        }
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean result = false;
        if (rowIndex == 0 ^ columnIndex == 0) {
            result = true;
        }
        return result;
    }
}

