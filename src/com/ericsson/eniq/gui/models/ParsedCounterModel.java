package com.ericsson.eniq.gui.models;

import javafx.beans.property.SimpleStringProperty;

public class ParsedCounterModel {
    private SimpleStringProperty name;
    private SimpleStringProperty oid;
    private SimpleStringProperty access;
    private SimpleStringProperty status;
    private SimpleStringProperty syntax;
    private SimpleStringProperty units;
    private SimpleStringProperty mibName;
    private SimpleStringProperty type;
    private SimpleStringProperty parentName;
    private SimpleStringProperty parentOid;
    private SimpleStringProperty description;
    private SimpleStringProperty mibType;

    public String[] getAllData() {
        return new String[] { getName(), getOid(), getAccess(), getStatus(), getSyntax(), getUnits(), getMibName(), getType(), getParentName(),
                getParentOid(), getDescription(), getMibType() };
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getOid() {
        return oid.get();
    }

    public void setOid(String oid) {
        this.oid = new SimpleStringProperty(oid);
    }

    public String getAccess() {
        return access.get();
    }

    public void setAccess(String access) {
        this.access = new SimpleStringProperty(access);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
    }

    public String getSyntax() {
        return syntax.get();
    }

    public void setSyntax(String syntax) {
        this.syntax = new SimpleStringProperty(syntax);
    }

    public String getUnits() {
        return units.get();
    }

    public void setUnits(String units) {
        this.units = new SimpleStringProperty(units);
    }

    public String getMibName() {
        return mibName.get();
    }

    public void setMibName(String mibName) {
        this.mibName = new SimpleStringProperty(mibName);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }

    public String getParentName() {
        if (parentName == null) {
            return "";
        }
        return parentName.get();
    }

    public void setParentName(String parentName) {
        this.parentName = new SimpleStringProperty(parentName);
    }

    public String getParentOid() {
        if (parentOid == null) {
            return "";
        }
        return parentOid.get();
    }

    public void setParentOid(String parentOid) {
        this.parentOid = new SimpleStringProperty(parentOid);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public String getMibType() {
        return mibType.get();
    }

    public void setMibType(String mibType) {
        this.mibType = new SimpleStringProperty(mibType);
    }

}
