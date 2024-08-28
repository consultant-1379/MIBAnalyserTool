package com.ericsson.eniq.tabs;

import com.ericsson.eniq.filebrowser.FileTreeView;
import com.ericsson.eniq.gui.controllers.LoadingButtonController;
import com.ericsson.eniq.utils.LoadingLabelQueue;
import com.ericsson.eniq.utils.PostLoadingLabels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Loading Tab of an application
 * 
 * @author etarvol
 *
 */
public class LoadingTab extends Tab {
    public LoadingTab() {
        initGUI();
    }

    public LoadingTab(String name) {
        super(name);
        initGUI();
    }

    private void initGUI() {
        SplitPane splitPane = new SplitPane();

        TextField pathTextField = new TextField();
        pathTextField.setMinWidth(500);
        ProgressBar progressBar = new ProgressBar();
        Button loadingButton = new Button("Load");
        loadingButton.setAlignment(Pos.CENTER_RIGHT);
        loadingButton.setOnAction(new LoadingButtonController(progressBar, pathTextField));

        FileTreeView fileTreeView = new FileTreeView(pathTextField);
        fileTreeView.setPrefWidth(250);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(5, 5, 5, 5));
        hbox.getChildren().addAll(new Label("Selected Path :"));
        hbox.getChildren().addAll(pathTextField);
        hbox.getChildren().addAll(loadingButton);
        hbox.setAlignment(Pos.BASELINE_CENTER);

        VBox loadingBox = new VBox(5);
        loadingBox.getChildren().add(hbox);
        loadingBox.getChildren().addAll(LoadingLabelQueue.getInstance().getLabels());
        loadingBox.getChildren().add(progressBar);
        progressBar.setVisible(false);
        loadingBox.setAlignment(Pos.CENTER);

        VBox postLoadingBox = new VBox(5);
        postLoadingBox.getChildren().add(PostLoadingLabels.getInstance().getLoadedMibsLabel());
        postLoadingBox.getChildren().add(PostLoadingLabels.getInstance().getErrorMibsLabel());
        postLoadingBox.getChildren().add(PostLoadingLabels.getInstance().getCountersLabel());
        postLoadingBox.getChildren().add(PostLoadingLabels.getInstance().getElapsedLabel());

        VBox titlePaneVbox = new VBox(50);
        titlePaneVbox.getChildren().add(hbox);
        titlePaneVbox.getChildren().add(postLoadingBox);
        titlePaneVbox.getChildren().add(loadingBox);

        TitledPane allPane = new TitledPane("Loading", titlePaneVbox);
        allPane.setAlignment(Pos.TOP_CENTER);
        allPane.prefHeightProperty().bind(splitPane.heightProperty());

        splitPane.getItems().add(fileTreeView);
        splitPane.getItems().add(allPane);

        setContent(splitPane);
    }
}
