package com.ericsson.eniq.gui.components;

import java.util.ArrayList;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.models.MibInfoTableModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MibInfoTable extends TableView<MibInfoTableModel> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MibInfoTable() {
        super();

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<MibInfoTableModel, String>("name"));
        name.setSortable(false);
        name.setMinWidth(200);

        TableColumn value = new TableColumn("Value");
        value.setCellValueFactory(new PropertyValueFactory<MibInfoTableModel, String>("value"));
        value.setSortable(false);
        value.prefWidthProperty().bind(widthProperty());

        getColumns().add(name);
        getColumns().add(value);

        setEditable(true);

    }

    private ObservableList<MibInfoTableModel> createModelList(MibNodeDto dto) {
        ArrayList<MibInfoTableModel> models = new ArrayList<MibInfoTableModel>();

        if (dto != null) {
            models.add(new MibInfoTableModel("OID", dto.getOid()));
            models.add(new MibInfoTableModel("Name", dto.getName()));
            /*
             * if (dto.getParent() != null) { models.add(new MibInfoTableModel("Parent Name", dto.getParent().getName())); }
             */
            if (dto.getImportedMibName() != null) {
                models.add(new MibInfoTableModel("Imported From", dto.getImportedMibName()));
            }

            models.add(new MibInfoTableModel("MIB Name", dto.getMibName()));
            models.add(new MibInfoTableModel("MIB File Name", dto.getMibFileName()));
            models.add(new MibInfoTableModel("Counter Type", dto.getType()));
            models.add(new MibInfoTableModel("Syntax", dto.getSyntax()));
            models.add(new MibInfoTableModel("Syntax Description (Units)", dto.getSyntaxDescription()));
            models.add(new MibInfoTableModel("Access", dto.getAccess()));
            models.add(new MibInfoTableModel("Status", dto.getStatus()));
            models.add(new MibInfoTableModel("Default Value", dto.getDefVal()));
            models.add(new MibInfoTableModel("Indexes", dto.getIndexes()));
            models.add(new MibInfoTableModel("Objects", dto.getObjects()));
            models.add(new MibInfoTableModel("Description", dto.getDescription()));
        }

        return FXCollections.observableArrayList(models);
    }

    public void updateTable(MibNodeDto dto) {
        ObservableList<MibInfoTableModel> data = createModelList(dto);
        setItems(data);
    }
}
