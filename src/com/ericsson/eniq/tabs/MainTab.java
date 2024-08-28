package com.ericsson.eniq.tabs;

import com.ericsson.eniq.gui.components.*;
import com.ericsson.eniq.gui.controllers.*;
import com.ericsson.eniq.utils.Search;
import com.ericsson.eniq.utils.StageManager;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Main Tab of an application
 * 
 * @author etarvol
 *
 */
public class MainTab extends Tab {
    private SplitPane splitPane = new SplitPane();
    private BorderPane counterBorderPane = new BorderPane();
    private VBox vBoxCenter = new VBox(5);
    private VBox vBoxLeft = new VBox();

    private Stage stage = null;

    public MainTab() {
        this.stage = StageManager.getInstance().getCurrentStage();

        initGUI();
    }

    private void initGUI() {
        MibCountersTree countersTreeView = new MibCountersTree();
        MibInfoTable mibInfoTable = new MibInfoTable();
        MibsTree mibTreeView = new MibsTree();

        ComboBox<String> searchComboBox = new ComboBox<String>();
        searchComboBox.setItems(Search.getInstance().getSearchOptionsNames());
        searchComboBox.getSelectionModel().select(0);

        Button parseButton = new Button("Parse");
        parseButton.setPrefWidth(120);
        parseButton.setPrefHeight(60);

        TextField mibsFilterField = new TextField();
        mibsFilterField.setPromptText("Type to filter MIBs...");
        mibsFilterField.setMinHeight(30);

        TextField counterSearchField = new TextField();
        counterSearchField.setPromptText("Type to search for counters...");
        counterSearchField.setPrefWidth(300);
        //
        // Setting up search list
        //
        CounterSearchListView searchList = new CounterSearchListView(mibTreeView, countersTreeView);
        searchList.setMaxHeight(100);
        searchList.setPrefHeight(0);
        //
        // Setting up controllers
        //
        mibTreeView.getSelectionModel().selectedItemProperty().addListener(new MibTreeController(countersTreeView, mibInfoTable));
        countersTreeView.getSelectionModel().selectedItemProperty().addListener(new MibCountersTreeController(mibInfoTable));
        mibsFilterField.setOnKeyReleased(new MibSearchController(mibTreeView));
        counterSearchField.setOnKeyReleased(new SearchController(mibsFilterField, searchList, searchComboBox));
        parseButton.setOnAction(new ParseOptionsButtonController());
        //
        // Organizing Layout
        //
        //
        // Setting padding between elements on the left
        //
        vBoxCenter.setAlignment(Pos.CENTER_RIGHT);
        //
        // Adding filter at bottom of the tree
        //
        vBoxLeft.getChildren().add(mibsFilterField);
        //
        // Adding tree with list of mibs
        //
        vBoxLeft.getChildren().add(mibTreeView);
        //
        // Adding tree with counters
        //
        vBoxCenter.getChildren().add(countersTreeView);
        //
        // Adding table where info about
        //
        vBoxCenter.getChildren().add(mibInfoTable);
        //
        // Adding parser button
        //
        vBoxCenter.getChildren().add(parseButton);
        //
        // Search functionality layout
        //
        VBox searchVbox = new VBox(2);
        HBox searchHbox = new HBox(5);
        searchHbox.setAlignment(Pos.BASELINE_LEFT);
        searchHbox.getChildren().add(counterSearchField);
        searchHbox.getChildren().add(new Label("Search By"));
        searchHbox.getChildren().add(searchComboBox);
        searchVbox.getChildren().add(searchHbox);
        searchVbox.getChildren().add(searchList);
        TitledPane searchPane = new TitledPane("Search Counters", searchVbox);
        searchPane.setAlignment(Pos.BASELINE_LEFT);

        searchVbox.autosize();
        searchHbox.autosize();
        searchPane.autosize();

        //
        // Setting main content
        //
        counterBorderPane.setTop(searchPane);
        counterBorderPane.setCenter(vBoxCenter);

        //
        // Binding with to stage so it resizes correctly
        //
        mibTreeView.prefHeightProperty().bind(stage.heightProperty());
        countersTreeView.prefWidthProperty().bind(stage.widthProperty());
        countersTreeView.prefHeightProperty().bind(stage.heightProperty());
        mibInfoTable.prefWidthProperty().bind(stage.widthProperty());
        mibInfoTable.prefHeightProperty().bind(stage.heightProperty());

        splitPane.getItems().add(vBoxLeft);
        splitPane.getItems().add(counterBorderPane);

        setContent(splitPane);
    }
}
