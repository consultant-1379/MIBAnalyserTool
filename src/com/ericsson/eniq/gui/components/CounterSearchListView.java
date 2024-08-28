package com.ericsson.eniq.gui.components;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.models.MatListCell;
import com.ericsson.eniq.utils.Search;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

public class CounterSearchListView extends ListView<String> {
    public CounterSearchListView(final MibsTree mibTree, final MibCountersTree countersTree) {
        getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    String[] split = newValue.split(":");
                    String counterName = "";
                    String mibName = "";

                    if (split.length > 0) {
                        counterName = split[0];
                        mibName = split[1];
                    }

                    List<MibNodeDto> ret = Search.getInstance().findCounterByName(counterName, false);

                    if (!ret.isEmpty()) {
                        for (MibNodeDto dto : ret) {
                            if (dto.getMibName().equals(mibName)) {
                                CheckBoxTreeItem<String> mibItem = (CheckBoxTreeItem<String>) mibTree.getTreeItem(dto.getMibName());
                                mibTree.getSelectionModel().select(mibItem);
                                mibTree.scrollTo(mibTree.getRow(mibItem));
                            }
                        }

                        TreeItem<String> counterItem = countersTree.getTreeItem(counterName);
                        countersTree.getSelectionModel().select(counterItem);
                        countersTree.scrollTo(countersTree.getRow(counterItem));
                    }
                }

            }
        });

        setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> arg0) {
                // TODO Auto-generated method stub
                return new MatListCell();
            }
        });

    }

    public void update(List<MibNodeDto> searchResultsList) {

        ArrayList<String> arrayList = new ArrayList<String>();
        for (MibNodeDto node : searchResultsList) {
            // filter to counters only
            if (node.isCounter() || node.isIndexCounter()) {
                arrayList.add(node.getName() + ":" + node.getMibName());
            }
        }
        ObservableList<String> dataList = FXCollections.observableList(arrayList);

        setItems(dataList);

    }
}
