package com.ericsson.eniq.gui.windows;

import com.ericsson.eniq.tabs.LoadingTab;
import com.ericsson.eniq.tabs.TabsManager;
import com.ericsson.eniq.utils.StageManager;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author etarvol
 *
 */
public class MainWindow {
    private StackPane stackPane = new StackPane();
    private Stage stage = StageManager.getInstance().getCurrentStage();
    private TabsManager tabManager = TabsManager.getInstance();

    public MainWindow() {
        stage.initStyle(StageStyle.DECORATED);
        stackPane.getChildren().add(tabManager.getPane());
        //
        // Creating our tabs
        //
        LoadingTab tab = new LoadingTab();
        tab.setText("Loading");
        tab.setClosable(false);
        tabManager.addTab(tab);

        stage.setTitle("MIB Analyzer Tool");
        stage.setScene(new Scene(stackPane, 1200, 800));

        StageManager.getInstance().setCurrentMainPane(stackPane);

        stage.show();
    }

}
