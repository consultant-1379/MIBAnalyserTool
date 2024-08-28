package com.ericsson.eniq.gui.models;

import javafx.scene.control.ListCell;

public class MatListCell extends ListCell<String> {

    public MatListCell() {

    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            String[] split = item.split(":");
            if (split.length > 0) {
                item = split[0];
            }
        }

        setText(item);
        setGraphic(getGraphic());

    }

}
