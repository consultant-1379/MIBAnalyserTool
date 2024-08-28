package com.ericsson.eniq.gui.windows;

import com.ericsson.eniq.gui.controllers.ParseButtonController;
import com.ericsson.eniq.utils.ParserOptions;
import com.ericsson.eniq.utils.StageManager;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class ParserOptionsWindow {
    private Stage stage = new Stage();
    private StackPane stackPane = new StackPane();
    private Button parseButton = new Button("Parse");

    public ParserOptionsWindow() {
        initGUI();
    }

    private void initGUI() {
        VBox mainVBox = new VBox(5);
        Accordion accordion = new Accordion();

        ParserOptions options = ParserOptions.getInstance();

        TitledPane accessPane = new TitledPane();
        accessPane.setText("Access Options");
        accessPane.setContent(options.getAccessListView());

        TitledPane statusPane = new TitledPane();
        statusPane.setText("Status Options");
        statusPane.setContent(options.getStatusListView());

        TitledPane syntaxPane = new TitledPane();
        syntaxPane.setText("Syntax Options");
        syntaxPane.setContent(options.getSyntaxListView());

        parseButton.setOnAction(new ParseButtonController(stage));
        parseButton.setPrefWidth(120);
        parseButton.setPrefHeight(50);
        parseButton.setAlignment(Pos.CENTER);

        accordion.getPanes().addAll(accessPane, statusPane, syntaxPane);
        accordion.setExpandedPane(accessPane);

        mainVBox.setPadding(new Insets(5, 5, 5, 5));
        mainVBox.getChildren().add(accordion);
        mainVBox.getChildren().add(parseButton);
        mainVBox.setAlignment(Pos.CENTER_RIGHT);

        accessPane.prefWidthProperty().bind(stackPane.widthProperty());
        accessPane.prefHeightProperty().bind(stackPane.heightProperty());

        statusPane.prefWidthProperty().bind(stackPane.widthProperty());
        statusPane.prefHeightProperty().bind(stackPane.heightProperty());

        syntaxPane.prefWidthProperty().bind(stackPane.widthProperty());
        syntaxPane.prefHeightProperty().bind(stackPane.heightProperty());

        stackPane.getChildren().add(mainVBox);

        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.setResizable(false);
        stage.setScene(new Scene(stackPane, 500, 500));
        stage.setTitle("Parsing Options");
        stage.show();
        //
        //	Creating blur effect
        //
        StageManager.getInstance().getCurrentMainPane().setEffect(new MotionBlur(0, 2.5));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                // TODO Auto-generated method stub
                //
                //	Removing blur effect
                //
                StageManager.getInstance().getCurrentMainPane().setEffect(null);
            }
        });
    }

}
