package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.gui.windows.ParserOptionsWindow;
import com.ericsson.eniq.utils.Dialog;
import com.ericsson.eniq.utils.MibsSelector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ParseOptionsButtonController implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        // TODO Auto-generated method stub

        if (MibsSelector.getInstance().getSelected().isEmpty()) {
            Dialog.getInstance().createDiaog("No MIB's selected ! Please select mibs to parse.");
        } else {
            new ParserOptionsWindow();
        }

    }

}
