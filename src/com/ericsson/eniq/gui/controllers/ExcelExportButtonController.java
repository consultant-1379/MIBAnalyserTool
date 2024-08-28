package com.ericsson.eniq.gui.controllers;

import java.awt.Desktop;
import java.io.File;

import javax.swing.filechooser.FileSystemView;

import com.ericsson.eniq.utils.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ExcelExportButtonController implements EventHandler<ActionEvent> {
    @SuppressWarnings("rawtypes")
    private TableView table = null;

    @SuppressWarnings("rawtypes")
    public ExcelExportButtonController(TableView table) {
        this.table = table;
    }

    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        try {
            ExtensionFilter xlsFilter = new ExtensionFilter("Excel Document", "*.xls");
            ExtensionFilter zipFilter = new ExtensionFilter("Zip Archives", "*.zip");
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(FileSystemView.getFileSystemView().getDefaultDirectory());
            chooser.getExtensionFilters().add(xlsFilter);
            chooser.getExtensionFilters().add(zipFilter);
            File file = chooser.showSaveDialog(StageManager.getInstance().getCurrentStage());
            if (file != null) {
                String path = file.getAbsolutePath();

                if (!path.contains(".xls")) {
                    path = path + ".xls";
                }
                file = new File(path);
                ExcelGenerator.getInstance().createDocument(file, table);

                File zipFile = new File(file.getParent() + "/" + file.getName().replace(".xls", "") + ".zip");
                ZipCreator.getInstance().createZip(zipFile);

            }
            //
            // Opening Excel
            //
            Desktop.getDesktop().open(file);
        }

        catch (Exception e) {
            Dialog.getInstance().createDiaog(e.getMessage());
        }
    }
}
