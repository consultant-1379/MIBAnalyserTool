package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.utils.ParserOptions;
import com.ericsson.eniq.utils.SelectedParserOptions;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;

public class OptionsAccessCheckBoxController implements EventHandler<ActionEvent> {

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub
        CheckBox checkBox = (CheckBox) event.getSource();
        String text = checkBox.getText();
        boolean isSelected = checkBox.isSelected();

        if (text.equals(ParserOptions.CB_SELECT_ALL)) {
            SelectedParserOptions.getInstance().selectAllAccess(isSelected);
            //
            //	Selecting everything visually
            //
            for (CheckBox cb : (ObservableList<CheckBox>) checkBox.getUserData()) {
                cb.setSelected(isSelected);
            }
        } else {
            SelectedParserOptions.getInstance().addAccessOption(text, isSelected);
        }
    }

}
