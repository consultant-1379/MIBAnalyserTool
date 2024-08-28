package com.ericsson.eniq.gui.components;

import java.util.HashMap;
import java.util.List;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.threads.SharedMatMibOperations;
import com.ericsson.eniq.utils.*;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

/**
 * Class for creating a treeview of counters
 *
 * @author etarvol
 *
 */
public class MibCountersTree extends TreeView<String> {
    private String mibFileName = null;
    private ImageView rootImage = null;
    private HashMap<String, TreeItem<String>> structure = new HashMap<String, TreeItem<String>>();

    public MibCountersTree() {
        super();
        rootImage = Loader.getImageView("./images/folder_open.png");

        createTree();
    }

    public String getMibName() {
        return mibFileName;
    }

    private void createTree() {
        String searchMibName = mibFileName;
        if (mibFileName != null) {
            searchMibName = MibUtils.removeMibExtension(mibFileName);
        }

        List<MibNodeDto> countersList = Search.getInstance().findCountersByMIB(searchMibName, false);

        populteTree(searchMibName, countersList);
    }

    private void populteTree(String rootName, List<MibNodeDto> countersList) {
        TreeItem<String> rootItem = new TreeItem<String>(rootName, rootImage);
        rootItem.setExpanded(true);
        setRoot(rootItem);

        if (SharedMatMibOperations.getInstance().getParsedWithErrorMibList().containsKey(mibFileName)) {
            TreeItem<String> errorItem = new TreeItem<String>(SharedMatMibOperations.getInstance().getParsedErrors().get(mibFileName));
            rootItem.getChildren().add(errorItem);
            return; // exit
        }
        //
        // Creating tables
        //
        for (MibNodeDto mibNode : countersList) {
            if (mibNode.isTable()) {
                String childKey = mibNode.getName();
                TreeItem<String> item = new TreeItem<String>(mibNode.getName(), mibNode.getImage());
                if (!structure.containsKey(childKey)) {

                    structure.put(childKey, item);
                    rootItem.getChildren().add(item);
                } else {
                    item = structure.get(childKey);
                    if (!rootItem.getChildren().contains(item)) {
                        rootItem.getChildren().add(item);
                    }

                }

            }
        }
        //
        // Creating table entries
        //
        for (MibNodeDto mibNode : countersList) {
            if (mibNode.isTableEntry()) {

                String parentKey = mibNode.getParent().getName();
                String childKey = mibNode.getName();
                if (!structure.containsKey(childKey)) {
                    TreeItem<String> item = new TreeItem<String>(mibNode.getName(), mibNode.getImage());
                    if (structure.containsKey(parentKey)) {
                        structure.get(parentKey).getChildren().add(item);
                    }
                    structure.put(mibNode.getName(), item);
                }
            }
        }
        //
        // Creating Index counters
        //
        for (MibNodeDto mibNode : countersList) {
            if (mibNode.isIndexCounter()) {
                String parentKey = mibNode.getParent().getName();
                String childKey = mibNode.getName();
                if (!structure.containsKey(mibNode.getName())) {
                    TreeItem<String> item = new TreeItem<String>(childKey, mibNode.getImage());
                    if (structure.containsKey(parentKey)) {
                        structure.get(parentKey).getChildren().add(item);
                    }
                    structure.put(childKey, item);
                }
            }
        }
        //
        // Creating Other counters
        //
        int counters = 0;
        for (MibNodeDto mibNode : countersList) {

            if (mibNode.isCounter()) {
                String parentKey = mibNode.getParent().getName();
                String childKey = mibNode.getName();
                if (!structure.containsKey(childKey)) {
                    TreeItem<String> item = new TreeItem<String>(childKey, mibNode.getImage());
                    if (structure.containsKey(parentKey)) {
                        structure.get(parentKey).getChildren().add(item);
                    } else {
                        rootItem.getChildren().add(item);
                    }
                    structure.put(childKey, item);

                } else {
                    if (!structure.containsKey(parentKey)) {
                        rootItem.getChildren().add(structure.get(childKey));
                    }
                }

                counters++;
            }
        }

        if (counters == 0) {
            rootItem.setValue("No counters");
        }
    }

    public void setMibName(String mibName) {
        this.mibFileName = mibName;
    }

    public void updateTree(String mibName) {
        //
        // Updating mib name
        //
        setMibName(mibName);
        //
        // Recreating tree
        //
        createTree();
    }

    public TreeItem<String> getTreeItem(String value) {
        // TODO Auto-generated method stub
        return structure.get(value);
    }
}
