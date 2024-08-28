package com.ericsson.eniq.tabs;

import com.ericsson.eniq.gui.components.ParsedCountersTable;
import com.ericsson.eniq.gui.controllers.ExcelExportButtonController;
import com.ericsson.eniq.utils.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Tab with results produced by parser
 * 
 * @author etarvol
 *
 */
public class ParserResultTab extends Tab {
    private VBox vbox = new VBox(5);
    private static int instanceCounter = 1;

    public ParserResultTab() {
        super("Parsed Results " + instanceCounter);
        instanceCounter++;

        initGUI();
    }

    public ParserResultTab(String tabName) {
        super(tabName + " " + instanceCounter);
        instanceCounter++;

        initGUI();
    }

    private void initGUI() {
        SplitPane splitPane = new SplitPane();

        ParsedCountersTable table = new ParsedCountersTable();
        Button excelExportButton = new Button("Export to Excel");

        excelExportButton.setOnAction(new ExcelExportButtonController(table));
        excelExportButton.setPrefWidth(120);
        excelExportButton.setPrefHeight(60);

        table.updateTable(Search.getInstance().getFilteredCounters());
        table.prefHeightProperty().bind(StageManager.getInstance().getCurrentStage().heightProperty());

        TitledPane selectedOptionsPane = new TitledPane();
        selectedOptionsPane.setText("Selected Options");
        HBox hBox = new HBox();

        VBox accessVBox = new VBox();
        VBox statusVBox = new VBox();
        VBox syntaxVBox = new VBox();
        VBox mibsVBox = new VBox();

        accessVBox.setAlignment(Pos.CENTER);
        statusVBox.setAlignment(Pos.CENTER);
        syntaxVBox.setAlignment(Pos.CENTER);
        mibsVBox.setAlignment(Pos.CENTER);

        ListView<String> accessList = new ListView<String>();
        ListView<String> statusList = new ListView<String>();
        ListView<String> syntaxList = new ListView<String>();
        ListView<String> mibList = new ListView<String>();

        final ObservableList<String> selectedAccess = FXCollections.observableList(SelectedParserOptions.getInstance().getSelectedAccess());
        final ObservableList<String> selectedStatus = FXCollections.observableList(SelectedParserOptions.getInstance().getSelectedStatus());
        final ObservableList<String> selectedSyntax = FXCollections.observableList(SelectedParserOptions.getInstance().getSelectedSyntax());
        if (selectedAccess.isEmpty()) {
            selectedAccess.add("All");
        }
        if (selectedStatus.isEmpty()) {
            selectedStatus.add("All");
        }
        if (selectedSyntax.isEmpty()) {
            selectedSyntax.add("All");
        }
        accessList.setItems(selectedAccess);
        statusList.setItems(selectedStatus);
        syntaxList.setItems(selectedSyntax);
        mibList.setItems(FXCollections.observableList(MibsSelector.getInstance().getSelected()));

        accessVBox.getChildren().addAll(new Label("Access"), accessList);
        statusVBox.getChildren().addAll(new Label("Status"), statusList);
        syntaxVBox.getChildren().addAll(new Label("Syntax"), syntaxList);
        mibsVBox.getChildren().addAll(new Label("MIBS"), mibList);

        hBox.getChildren().addAll(accessVBox, statusVBox, syntaxVBox);
        splitPane.getItems().add(mibsVBox);
        splitPane.getItems().add(hBox);

        selectedOptionsPane.setContent(splitPane);

        vbox.setPadding(new Insets(5, 5, 5, 5));
        vbox.getChildren().addAll(selectedOptionsPane, table, excelExportButton);
        vbox.setAlignment(Pos.CENTER_RIGHT);

        setContent(vbox);
    }
}
