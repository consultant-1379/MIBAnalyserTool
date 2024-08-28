package com.ericsson.eniq.utils;

import java.util.ArrayList;

public class SelectedParserOptions {
    private static SelectedParserOptions thisInstance = null;
    private ArrayList<String> selectedAccess = new ArrayList<String>();
    private ArrayList<String> selectedStatus = new ArrayList<String>();
    private ArrayList<String> selectedSyntax = new ArrayList<String>();

    private SelectedParserOptions() {

    }

    public static SelectedParserOptions getInstance() {
        if (thisInstance == null) {
            thisInstance = new SelectedParserOptions();
        }

        return thisInstance;
    }

    public void selectAllAccess(boolean isSelected) {
        if (isSelected) {
            setSelectedAccess(ParserOptions.getInstance().getCounterAccess());
        } else {
            selectedAccess = new ArrayList<String>();
        }
    }

    public void selectAllStatus(boolean isSelected) {
        if (isSelected) {
            setSelectedStatus(ParserOptions.getInstance().getCounterStatus());
        } else {
            selectedStatus = new ArrayList<String>();
        }
    }

    public void selecteAllSyntax(boolean isSelected) {
        if (isSelected) {
            setSelectedSyntax(ParserOptions.getInstance().getCounterSyntax());
        } else {
            selectedSyntax = new ArrayList<String>();
        }
    }

    public void addAccessOption(String name, boolean isSelected) {
        if (isSelected) {
            getSelectedAccess().add(name);
        } else {
            getSelectedAccess().remove(name);
        }
    }

    public void addSyntaxOption(String name, boolean isSelected) {
        if (isSelected) {
            getSelectedSyntax().add(name);
        } else {
            getSelectedSyntax().remove(name);
        }
    }

    public void addStatusOption(String name, boolean isSelected) {
        if (isSelected) {
            getSelectedStatus().add(name);
        } else {
            getSelectedStatus().remove(name);
        }
    }

    public ArrayList<String> getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(ArrayList<String> selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public ArrayList<String> getSelectedSyntax() {
        return selectedSyntax;
    }

    public void setSelectedSyntax(ArrayList<String> selectedSyntax) {
        this.selectedSyntax = selectedSyntax;
    }

    public ArrayList<String> getSelectedAccess() {
        return selectedAccess;
    }

    public void setSelectedAccess(ArrayList<String> selectedAccess) {
        this.selectedAccess = selectedAccess;
    }

    public void reset() {
        selectedAccess = new ArrayList<String>();
        selectedSyntax = new ArrayList<String>();
        selectedStatus = new ArrayList<String>();

    }

}
