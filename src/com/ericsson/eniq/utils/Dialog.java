package com.ericsson.eniq.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class Dialog extends Stage {
    private static Dialog thisInstance = null;

    private VBox vbox = new VBox(5);
    private Label label = new Label("");
    private Button okButton = new Button("OK");

    private Dialog() {
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UTILITY);
        initGUI();
    }

    public static Dialog getInstance() {
        if (thisInstance == null) {
            thisInstance = new Dialog();
        }

        return thisInstance;
    }

    private void initGUI() {
        vbox.getChildren().add(label);
        vbox.getChildren().add(okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        StackPane pane = new StackPane();
        pane.getChildren().add(vbox);

        setScene(new Scene(pane, 500, 120));
        //
        //	When OK is pressed call our close functions
        //
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                closeDiaog();
            }
        });
        //
        // In case of close call our close function
        //
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                // TODO Auto-generated method stub
                closeDiaog();
            }
        });
    }

    public void createDiaog(String msg) {
        createDiaog(msg, "Information Dialog");
    }

    public void createDiaog(String msg, String title) {
        //
        // Creating blur effect
        //
        StageManager.getInstance().getCurrentMainPane().setEffect(new MotionBlur(0, 2.5));
        //
        // Creating label
        //
        label.setText(msg);
        label.setWrapText(true);
        //
        // Showing stage
        //
        setTitle(title);
        show();
    }

    public void closeDiaog() {
        StageManager.getInstance().getCurrentMainPane().setEffect(null);
        close();
    }
}
