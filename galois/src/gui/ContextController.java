/*
 * Decompiled with CFR 0_114.
 */
package gui;

import dot.CustomDotExporter;
import gsh.GSHBuilder;
import gsh.algorithm.GSHAlgorithm;
import gsh.algorithm.pluton.Pluton;
import gsh.types.GSH;
import gui.ContextTable;
import gui.GSHBStart;
import gui.JGraphApplet;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import jcornflower.matrix.D2DUtil;
import jcornflower.matrix.Double2D;
import jcornflower.matrix.xml.XMLMatrix;
import jcornflower.ui.JCSwing;
import org.simpleframework.xml.core.Persister;

public class ContextController {
    private final GSHBStart gui;

    public ContextController(GSHBStart gui) {
        this.gui = gui;
    }

    public void showGSH() {
        JTable contextJTable = this.gui.getContextJTable();
        if (contextJTable instanceof ContextTable) {
            try {
                GSH gsh = this.getGshFromContextTable(contextJTable);
                JGraphApplet app = new JGraphApplet();
                app.setJGraph(gsh);
                app.run();
            }
            catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(GSHBStart.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void exportGSHToFile() {
        JTable contextJTable = this.gui.getContextJTable();
        try {
            if (contextJTable instanceof ContextTable) {
                GSH gsh = this.getGshFromContextTable(contextJTable);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("dot files", "dot");
                File saveas = JCSwing.getSaveAsFile(this.gui, filter);
                ResourceBundle bundle = ResourceBundle.getBundle("dot.dot");
                CustomDotExporter.export(saveas, bundle, gsh);
            }
        }
        catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(GSHBStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private GSH getGshFromContextTable(JTable contextJTable) throws InterruptedException, ExecutionException {
        Double2D d2d = D2DUtil.getContextFromTable(contextJTable);
        GSHBuilder gshf = new GSHBuilder(new Pluton());
        return gshf.generate(d2d, false);
    }

    public void importFromXML() {
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            this.gui.getImportxmlJFileChooser().addChoosableFileFilter(filter);
            int ret = this.gui.getImportxmlJFileChooser().showDialog(null, "Open xml context file");
            if (ret == 0) {
                File file = this.gui.getImportxmlJFileChooser().getSelectedFile();
                Double2D d2d = D2DUtil.getContextFromFile(file);
                this.gui.initContextJTable(d2d);
            }
        }
        catch (Exception ex) {
            Logger.getLogger(GSHBStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportContextToXML() {
        try {
            JTable contextJTable = this.gui.getContextJTable();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            File saveas = JCSwing.getSaveAsFile(this.gui, filter);
            XMLMatrix dom = D2DUtil.getDom(D2DUtil.getContextFromTable(contextJTable));
            Persister serializer = new Persister();
            serializer.write((Object)dom, saveas);
        }
        catch (Exception ex) {
            Logger.getLogger(GSHBStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

