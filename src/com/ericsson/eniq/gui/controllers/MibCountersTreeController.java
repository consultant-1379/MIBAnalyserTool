package com.ericsson.eniq.gui.controllers;

import java.util.List;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.components.MibInfoTable;
import com.ericsson.eniq.utils.Search;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

public class MibCountersTreeController implements ChangeListener<TreeItem<String>> {
    MibInfoTable table = null;

    public MibCountersTreeController(MibInfoTable table) {
        this.table = table;
    }

    @Override
    public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
        if (newValue == null) {
            return; // exit
        }

        List<MibNodeDto> dtolist = Search.getInstance().findCounterByName(newValue.getValue(), false);
        if (dtolist.size() > 0) {
            MibNodeDto dto = dtolist.get(0);
            table.updateTable(dto);
        }

    }
}
