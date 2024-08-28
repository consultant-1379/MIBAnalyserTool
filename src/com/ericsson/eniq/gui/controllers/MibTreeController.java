package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.gui.components.MibCountersTree;
import com.ericsson.eniq.gui.components.MibInfoTable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

public class MibTreeController implements ChangeListener<TreeItem<String>> {
    private MibCountersTree mibCountersTree;
    private MibInfoTable mibInfoTable;

    public MibTreeController(MibCountersTree tree) {
        this.mibCountersTree = tree;
    }

    public MibTreeController(MibCountersTree countersTreeView, MibInfoTable mibInfoTable) {
        this.mibCountersTree = countersTreeView;
        this.mibInfoTable = mibInfoTable;
    }

    @Override
    public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
        if (newValue == null) {
            return; // exit
        }
        String value = newValue.getValue();

        if (!mibCountersTree.isFocused()) {
            mibInfoTable.updateTable(null);
        }

        mibCountersTree.updateTree(value);
    }

}
