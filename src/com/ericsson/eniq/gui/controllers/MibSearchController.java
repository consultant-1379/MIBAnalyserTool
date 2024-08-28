package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.gui.components.MibsTree;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MibSearchController implements EventHandler<KeyEvent> {
    private MibsTree treeView;

    public MibSearchController(MibsTree treeViewParam) {
        treeView = treeViewParam;
    }

    @Override
    public void handle(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        if (field != null) {
            final String lookUp = field.getText().trim();
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    if (lookUp.length() == 0) {
                        treeView.createTree();
                    } else {
                        treeView.searchUpdate(lookUp);
                    }
                }
            });

        }

    }
}
