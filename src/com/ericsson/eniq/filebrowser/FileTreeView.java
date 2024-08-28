package com.ericsson.eniq.filebrowser;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;

public class FileTreeView extends TreeView<String> {

    private TextField pathTextField = null;
    private String selectedFolderPath = new String();

    public FileTreeView(TextField pathTextField) {
        initTree();
        this.pathTextField = pathTextField;
    }

    public String getSelectedFolderPath() {
        return selectedFolderPath;
    }

    private void initTree() {
        String hostName = "My PC";

        TreeItem<String> rootFolder = new TreeItem<String>(hostName);
        setRoot(rootFolder);

        for (File rootFile : File.listRoots()) {
            FileTreeItem item = new FileTreeItem(this, rootFile.getAbsolutePath());
            item.setExpanded(true);
            rootFolder.getChildren().add(item);
            item.setExpanded(false);
        }

        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                if (newValue != null && newValue instanceof FileTreeItem) {
                    FileTreeItem source = (FileTreeItem) newValue;
                    pathTextField.setText(source.getFilePath());
                }

            }
        });

        rootFolder.setExpanded(true);
    }

    public void setSelectedFolderPath(String selectedFolderPath) {
        this.selectedFolderPath = selectedFolderPath;
    }

}
