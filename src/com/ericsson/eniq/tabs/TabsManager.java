package com.ericsson.eniq.tabs;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Manager for creating and removing tabs
 * 
 * @author etarvol
 *
 */
public class TabsManager {
    private TabPane pane = new TabPane();

    private static TabsManager thisInstance = null;
    private MainTab currentMainTab = null;

    private TabsManager() {

    }

    public static TabsManager getInstance() {
        if (thisInstance == null) {
            thisInstance = new TabsManager();
        }

        return thisInstance;
    }

    public void addTab(Tab tab) {
        if (tab instanceof MainTab) {
            setCurrentMainTab((MainTab) tab);
        }

        pane.getTabs().add(tab);
    }

    public void removeTab(Tab tab) {
        pane.getTabs().remove(tab);
    }

    public TabPane getPane() {
        return pane;
    }

    public void setPane(TabPane pane) {
        this.pane = pane;
    }

    public MainTab getCurrentMainTab() {
        return currentMainTab;
    }

    public void setCurrentMainTab(MainTab currentMainTab) {
        this.currentMainTab = currentMainTab;
    }
}
