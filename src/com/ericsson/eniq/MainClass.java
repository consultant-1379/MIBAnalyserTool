package com.ericsson.eniq;

import com.ericsson.eniq.gui.windows.MainWindow;
import com.ericsson.eniq.utils.StageManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) {
        //
        // Setting current stage
        //
        StageManager.getInstance().setCurrentStage(primaryStage);
        //
        // Creating new Main Window
        //
        new MainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
