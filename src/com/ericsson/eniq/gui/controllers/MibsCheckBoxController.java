package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.utils.MibsSelector;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBoxTreeItem;

@SuppressWarnings("rawtypes")
public class MibsCheckBoxController implements EventHandler {

    @Override
    public void handle(Event event) {
        // TODO Auto-generated method stub
        @SuppressWarnings("unchecked")
        CheckBoxTreeItem<String> checkBoxTreeItem = (CheckBoxTreeItem<String>) event.getSource();

        MibsSelector.getInstance().addMib(checkBoxTreeItem.getValue(), checkBoxTreeItem.isSelected());
    }

}
