package com.ericsson.eniq.utils;

import java.util.*;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.controllers.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class ParserOptions {
    private static ParserOptions thisInstance = null;

    private ArrayList<String> counterAccess = new ArrayList<String>();
    private ArrayList<String> counterSyntax = new ArrayList<String>();
    private ArrayList<String> counterStatus = new ArrayList<String>();

    public static final String CB_SELECT_ALL = "SELECT ALL";

    private ParserOptions() {

    }

    public static ParserOptions getInstance() {
        if (thisInstance == null) {
            thisInstance = new ParserOptions();
        }

        return thisInstance;
    }

    public ArrayList<String> getCounterAccess() {
        return counterAccess;
    }

    public ArrayList<String> getCounterSyntax() {
        return counterSyntax;
    }

    public ArrayList<String> getCounterStatus() {
        return counterStatus;
    }

    public void addAccess(String name) {
        if (!counterAccess.contains(name) && counterAccess != null) {
            counterAccess.add(name);
        }
    }

    public void addSyntax(String name) {
        if (!counterSyntax.contains(name) && counterSyntax != null) {
            counterSyntax.add(name);
        }
    }

    public void addStatus(String name) {
        if (!counterStatus.contains(name) && counterStatus != null) {
            counterStatus.add(name);
        }
    }

    public ListView<CheckBox> getAccessListView() {
        resetAndPopulate();
        ListView<CheckBox> list = new ListView<CheckBox>();
        ObservableList<CheckBox> cbList = FXCollections.observableArrayList();

        CheckBox selectAll = new CheckBox(CB_SELECT_ALL);
        selectAll.setOnAction(new OptionsAccessCheckBoxController());
        selectAll.setUserData(cbList);
        selectAll.setSelected(true);
        cbList.add(0, selectAll);

        for (String item : counterAccess) {
            if (item == null) {
                continue;
            }
            CheckBox cb = new CheckBox();
            cb.setOnAction(new OptionsAccessCheckBoxController());
            cb.setText(item);
            cbList.add(cb);
        }

        list.setItems(cbList);

        return list;
    }

    public ListView<CheckBox> getSyntaxListView() {
        resetAndPopulate();
        ListView<CheckBox> list = new ListView<CheckBox>();
        ObservableList<CheckBox> cbList = FXCollections.observableArrayList();

        CheckBox selectAll = new CheckBox(CB_SELECT_ALL);
        selectAll.setOnAction(new OptionsSyntaxCheckBoxController());
        selectAll.setUserData(cbList);
        selectAll.setSelected(true);
        cbList.add(0, selectAll);

        for (String item : counterSyntax) {
            if (item == null) {
                continue;
            }
            CheckBox cb = new CheckBox();
            cb.setOnAction(new OptionsSyntaxCheckBoxController());
            cb.setText(item);
            cbList.add(cb);
        }

        list.setItems(cbList);

        return list;
    }

    public ListView<CheckBox> getStatusListView() {
        resetAndPopulate();
        ListView<CheckBox> list = new ListView<CheckBox>();
        ObservableList<CheckBox> cbList = FXCollections.observableArrayList();

        CheckBox selectAll = new CheckBox(CB_SELECT_ALL);
        selectAll.setOnAction(new OptionsStatusCheckBoxController());
        selectAll.setUserData(cbList);
        selectAll.setSelected(true);
        cbList.add(0, selectAll);

        for (String item : counterStatus) {
            if (item == null) {
                continue;
            }
            CheckBox cb = new CheckBox();
            cb.setOnAction(new OptionsStatusCheckBoxController());
            cb.setText(item);
            cbList.add(cb);
        }

        list.setItems(cbList);

        return list;
    }

    private void resetAndPopulate() {
        //
        // Reseting available options
        //
        counterAccess.clear();
        counterSyntax.clear();
        counterStatus.clear();
        //
        // Defining variables
        //
        ArrayList<String> selectedMibs = MibsSelector.getInstance().getSelected();
        Search search = Search.getInstance();

        for (String mibName : selectedMibs) {
            List<MibNodeDto> counters = search.findCountersByMIB(mibName, false);
            for (MibNodeDto counter : counters) {
                if (counter.isCounter() || counter.isIndexCounter()) {
                    String syntax = counter.getSyntax();
                    String status = counter.getStatus();
                    String access = counter.getAccess();

                    if (!counterSyntax.contains(syntax)) {
                        counterSyntax.add(syntax);
                    }
                    if (!counterAccess.contains(access)) {
                        counterAccess.add(access);
                    }
                    if (!counterStatus.contains(status)) {
                        counterStatus.add(status);
                    }
                }

            }
        }
        //
        // Sorting
        //
        Collections.sort(counterAccess);
        Collections.sort(counterSyntax);
        Collections.sort(counterStatus);
        //
        // Reseting parser options
        //
        SelectedParserOptions.getInstance().reset();

    }

}
