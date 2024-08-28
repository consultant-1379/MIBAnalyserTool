package com.ericsson.eniq.utils;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageManager {

    private static StageManager thisInstance = null;
    private Stage stage = new Stage();
    private Pane currentMainPane = null;
    private BorderPane currentBorderPane = null;

    private StageManager() {

    }

    public static StageManager getInstance() {
        if (thisInstance == null) {
            thisInstance = new StageManager();
        }

        return thisInstance;
    }

    public Stage getCurrentStage() {
        return stage;
    }

    public void setCurrentStage(Stage stageParam) {
        stage = stageParam;
    }

    public Stage createAndClose() {
        stage.close();
        stage = new Stage();

        return stage;
    }

    public Pane getCurrentMainPane() {
        return currentMainPane;
    }

    public void setCurrentMainPane(Pane currentMainPane) {
        this.currentMainPane = currentMainPane;
    }

    public BorderPane getCurrentBorderPane() {
        return currentBorderPane;
    }

    public void setCurrentBorderPane(BorderPane currentBorderPane) {
        this.currentBorderPane = currentBorderPane;
    }

}
