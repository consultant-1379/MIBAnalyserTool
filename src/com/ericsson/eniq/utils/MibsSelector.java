package com.ericsson.eniq.utils;

import java.util.ArrayList;

import com.ericsson.eniq.threads.SharedMatMibOperations;

/**
 * Class for tracking selected mibs for future parsing
 *
 * @author etarvol
 *
 */
public class MibsSelector {
    private ArrayList<String> selected = new ArrayList<String>();

    private static MibsSelector thisInstance = null;

    private MibsSelector() {

    }

    public static MibsSelector getInstance() {
        if (thisInstance == null) {
            thisInstance = new MibsSelector();
        }

        return thisInstance;
    }

    public void addMib(String name, boolean isSelected) {
        if (SharedMatMibOperations.getInstance().getParsedWithErrorMibList().containsKey(name)) {
            return;
        }

        if (isSelected) {
            selected.add(name);
        } else {
            selected.remove(name);
        }
    }

    public ArrayList<String> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<String> selected) {
        this.selected = selected;
    }
}
