package com.ericsson.eniq.gui.components;

import java.util.*;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.models.ParsedCounterModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParsedCountersTable extends TableView<ParsedCounterModel> {
    public ParsedCountersTable() {
        createColumns();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createColumns() {

        TableColumn name = new TableColumn("Counter Name");
        name.prefWidthProperty().set(150);
        name.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("name"));
        getColumns().add(name);

        TableColumn oid = new TableColumn("Counter OID");
        oid.prefWidthProperty().set(150);
        oid.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("oid"));
        getColumns().add(oid);

        TableColumn access = new TableColumn("Counter Access");
        access.prefWidthProperty().set(150);
        access.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("access"));
        getColumns().add(access);

        TableColumn status = new TableColumn("Counter Status");
        status.prefWidthProperty().set(150);
        status.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("status"));
        getColumns().add(status);

        TableColumn syntax = new TableColumn("Counter Syntax");
        syntax.prefWidthProperty().set(150);
        syntax.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("syntax"));
        getColumns().add(syntax);

        TableColumn units = new TableColumn("Counter Units");
        units.prefWidthProperty().set(150);
        units.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("units"));
        getColumns().add(units);

        TableColumn mibName = new TableColumn("MIB Name");
        mibName.prefWidthProperty().set(150);
        mibName.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("mibName"));
        getColumns().add(mibName);

        TableColumn type = new TableColumn("Counter Type");
        type.prefWidthProperty().set(150);
        type.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("type"));
        getColumns().add(type);

        TableColumn parentName = new TableColumn("Parent Name");
        parentName.prefWidthProperty().set(150);
        parentName.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("parentName"));
        getColumns().add(parentName);

        TableColumn parentOid = new TableColumn("Parent OID");
        parentOid.prefWidthProperty().set(150);
        parentOid.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("parentOid"));
        getColumns().add(parentOid);

        TableColumn description = new TableColumn("Description");
        description.prefWidthProperty().bind(widthProperty());
        description.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("description"));
        getColumns().add(description);

        TableColumn mibType = new TableColumn("MIB Type");
        mibType.prefWidthProperty().bind(widthProperty());
        mibType.setCellValueFactory(new PropertyValueFactory<ParsedCountersTable, String>("mibType"));
        getColumns().add(mibType);

    }

    public void updateTable(List<MibNodeDto> list) {
        ObservableList<ParsedCounterModel> data = FXCollections.observableArrayList(createModelList(list));
        setItems(data);
    }

    private ArrayList<ParsedCounterModel> createModelList(List<MibNodeDto> list) {
        ArrayList<ParsedCounterModel> models = new ArrayList<ParsedCounterModel>();
        HashMap<String, ParsedCounterModel> modelsMap = new HashMap<String, ParsedCounterModel>();

        for (MibNodeDto dto : list) {
            if (dto.isTable() || dto.isTableEntry()) {
                continue;
            }

            ParsedCounterModel model = new ParsedCounterModel();

            ParsedCounterModel existingModel = modelsMap.get(dto.getOid());
            if (existingModel != null) {
                existingModel.setMibName(existingModel.getMibName() + "," + dto.getMibName());
            } else {
                model.setName(dto.getName());
                model.setOid(dto.getOid());
                model.setAccess(dto.getAccess());
                model.setStatus(dto.getStatus());
                model.setSyntax(dto.getSyntax());
                model.setUnits(dto.getSyntaxDescription());

                if (dto.getMibType().equals(MibNodeDto.PRIMARY)) {
                    model.setMibType("PRIMARY");
                } else if (dto.getMibType().equals(MibNodeDto.DEPENDENCIE)) {
                    model.setMibType("DEPENDENCY");
                } else {
                    model.setMibType("ERROR");
                }

                model.setMibName(dto.getMibName());
                model.setType(dto.getType());
                if (dto.getParent() != null) {
                    model.setParentName(dto.getParent().getName());
                    model.setParentOid(dto.getParent().getOid());
                }
                model.setDescription(dto.getDescription());

                models.add(model);
                //
                // Adding into map
                //
                modelsMap.put(dto.getOid(), model);
            }

        }

        return models;
    }
}
