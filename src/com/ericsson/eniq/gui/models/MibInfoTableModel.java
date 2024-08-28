package com.ericsson.eniq.gui.models;

import javafx.beans.property.SimpleStringProperty;

public class MibInfoTableModel {
    private SimpleStringProperty name;
    private SimpleStringProperty value;

    public MibInfoTableModel(SimpleStringProperty name, SimpleStringProperty value) {
        super();
        this.name = name;
        this.value = value;
    }

    public MibInfoTableModel(String name, String value) {
        super();
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public String getName() {
        return name.get();
    }

    public String getValue() {
        return value.get();
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public void setValue(SimpleStringProperty value) {
        this.value = value;
    }

}
