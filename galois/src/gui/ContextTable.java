/*
 * Decompiled with CFR 0_114.
 */
package gui;

import Jama.Matrix;
import gui.ContextTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import jcornflower.matrix.Double2D;

public class ContextTable
extends JTable {
    private static final long serialVersionUID = -1131433913526680881L;

    private ContextTable getTable() {
        return this;
    }

    public ContextTable(Double2D d2d) {
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ContextTable.this.rowAtPoint(e.getPoint());
                    int col = ContextTable.this.columnAtPoint(e.getPoint());
                    if (row > 0 && col > 0) {
                        String value = (String)ContextTable.this.getTable().getValueAt(row, col);
                        if ("X".equals(value)) {
                            ContextTable.this.getTable().setValueAt("", row, col);
                        } else {
                            ContextTable.this.getTable().setValueAt("X", row, col);
                        }
                    }
                }
            }
        });
        this.setModel(new ContextTableModel(ContextTable.toString2D(d2d)));
        this.setDefaultRenderer(String.class, new CustomRenderer());
        this.setRowSelectionAllowed(false);
        this.setShowGrid(true);
        int totalLength = 0;
        int totalWidth = 0;
        int length = 0;
        totalWidth += d2d.getObjnames().size() * 10;
        for (String p2 : d2d.getObjnames()) {
            if (p2.length() <= length) continue;
            length = p2.length();
        }
        this.getColumnModel().getColumn(0).setPreferredWidth(length * 6);
        totalLength += length * 6;
        Iterator<String> p = d2d.getAttrnames().iterator();
        int i = 1;
        while (i < this.getColumnModel().getColumnCount()) {
            int lenght = p.next().length();
            this.getColumnModel().getColumn(i).setPreferredWidth(lenght * 6);
            totalLength += length * 6;
            ++i;
        }
        this.getTableHeader().setReorderingAllowed(true);
        this.setSize(totalLength, totalWidth);
    }

    private static String[][] toString2D(Double2D d2d) {
        double[][] m = d2d.getMatrix().getArray();
        String[][] result = new String[d2d.getObjnames().size() + 1][d2d.getAttrnames().size() + 1];
        int y = 1;
        Iterator<String> iterator = d2d.getObjnames().iterator();
        while (iterator.hasNext()) {
            String p;
            result[y][0] = p = iterator.next();
            ++y;
        }
        int x = 1;
        Iterator<String> iterator2 = d2d.getAttrnames().iterator();
        while (iterator2.hasNext()) {
            String p;
            result[0][x] = p = iterator2.next();
            ++x;
        }
        int i = 0;
        while (i < m.length) {
            int j = 0;
            while (j < m[i].length) {
                if (m[i][j] != 0.0) {
                    result[i + 1][j + 1] = "X";
                }
                ++j;
            }
            ++i;
        }
        return result;
    }

    class CustomRenderer
    extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -6119127567576059949L;

        CustomRenderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                this.setHorizontalAlignment(2);
            } else {
                this.setHorizontalAlignment(0);
            }
            Color bg = Color.WHITE;
            if (column != 0 && row != 0 && this.getText().equals("X")) {
                bg = Color.LIGHT_GRAY;
            }
            c.setBackground(bg);
            return c;
        }
    }

}

