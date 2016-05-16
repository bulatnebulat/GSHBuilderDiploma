/*
 * Decompiled with CFR 0_114.
 */
package jcornflower.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class JCSwing {
    public static void display(JFrame window) {
        JCSwing.positionCenter(window);
        window.pack();
        window.setVisible(true);
    }

    public static void display(JDialog window) {
        JCSwing.positionCenter(window);
        window.pack();
        window.setVisible(true);
    }

    private static void positionCenter(JFrame window) {
        Dimension screen = JCSwing.getScreenSize();
        int y = (screen.height - window.getPreferredSize().height) / 2;
        int x = (screen.width - window.getPreferredSize().width) / 2;
        window.setLocation(x, y);
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private static void positionCenter(JDialog window) {
        Dimension screen = JCSwing.getScreenSize();
        int y = (screen.height - window.getPreferredSize().height) / 2;
        int x = (screen.width - window.getPreferredSize().width) / 2;
        window.setLocation(x, y);
    }

    public static File getSaveAsFile(Component parent, FileFilter filter) throws IOException {
        File fileName;
        File result = null;
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int actionDialog = chooser.showSaveDialog(parent);
        if (actionDialog == 0 && (fileName = new File(chooser.getSelectedFile().toString())) != null) {
            if (fileName.exists()) {
                actionDialog = JOptionPane.showConfirmDialog(parent, "Replace existing file?");
                while (actionDialog == 1) {
                    actionDialog = chooser.showSaveDialog(parent);
                    if (actionDialog != 0 || !(fileName = new File(chooser.getSelectedFile().toString())).exists()) continue;
                    actionDialog = JOptionPane.showConfirmDialog(parent, "Replace existing file?");
                }
                if (actionDialog == 0) {
                    result = fileName;
                }
                return result;
            }
            result = fileName;
        }
        return result;
    }
}

