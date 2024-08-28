package com.ericsson.eniq.gui.controllers;

import com.ericsson.eniq.tabs.ParserResultTab;
import com.ericsson.eniq.tabs.TabsManager;
import com.ericsson.eniq.utils.StageManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ParseButtonController implements EventHandler<ActionEvent> {
    private Stage stage = null;

    public ParseButtonController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        TabsManager.getInstance().addTab(new ParserResultTab());
        //
        //	Removing blur effect
        //
        StageManager.getInstance().getCurrentMainPane().setEffect(null);

        stage.close();
    }

}
