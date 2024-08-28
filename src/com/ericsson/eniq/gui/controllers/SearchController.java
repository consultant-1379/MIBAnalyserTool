package com.ericsson.eniq.gui.controllers;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.components.CounterSearchListView;
import com.ericsson.eniq.utils.Search;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SearchController implements EventHandler<KeyEvent> {
    private ComboBox<String> comboBox;
    private CounterSearchListView searchList;
    private TextField mibsFilterField;
    private List<MibNodeDto> searchResultsList = new ArrayList<MibNodeDto>();

    public SearchController(TextField mibsFilterField, CounterSearchListView searchList, ComboBox<String> comboBox) {
        this.mibsFilterField = mibsFilterField;
        this.comboBox = comboBox;
        this.searchList = searchList;
    }

    @Override
    public void handle(final KeyEvent event) {
        TextField field = (TextField) event.getSource();
        if (field != null) {
            final String lookUp = field.getText().trim();
            String comboSelection = comboBox.getSelectionModel().selectedItemProperty().getValue();
            searchResultsList.clear();

            if (comboSelection != null) {
                if (comboSelection.equals(Search.COMBO_COUNTER_NAME)) {
                    searchResultsList = Search.getInstance().findCounterByName(lookUp, true);
                } else if (comboSelection.equals(Search.COMBO_COUNTER_OID)) {
                    searchResultsList = Search.getInstance().findCounterByOID(lookUp, true);
                } else if (comboSelection.equals(Search.COMBO_COUNTER_SYNTAX)) {
                    searchResultsList = Search.getInstance().findCounterBySyntax(lookUp, true);
                } else if (comboSelection.equals(Search.COMBO_COUNTER_STATUS)) {
                    searchResultsList = Search.getInstance().findCounterByStatus(lookUp, true);
                }

            }
            /**
             * Updating GUI
             */
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    if (lookUp.length() == 0) {
                        searchList.setPrefHeight(0);
                    } else {
                        searchList.setPrefHeight(searchList.getMaxHeight());
                        mibsFilterField.setText("");
                        mibsFilterField.fireEvent(event);
                    }

                    searchList.update(searchResultsList);
                }
            });

        }

    }
}
