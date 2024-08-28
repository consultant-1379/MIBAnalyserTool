package com.ericsson.eniq.gui.components;

import java.util.*;

import com.ericsson.eniq.gui.controllers.MibsCheckBoxController;
import com.ericsson.eniq.threads.SharedMatMibOperations;
import com.ericsson.eniq.utils.Search;

import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;

/**
 * Class fro creating treeview of all the mibs
 *
 * @author etarvol
 *
 */
public class MibsTree extends TreeView<String> {
    public static final String DEPENDENCIES = "Dependencie Mibs";
    public static final String ERROR = "Error Mibs";
    public static final String PRIMARY = "Primary Mibs";
    public static final String ROOT = "All Mibs";

    private CheckBoxTreeItem<String> rootItem = new CheckBoxTreeItem<String>(ROOT);
    private CheckBoxTreeItem<String> primaryItem = new CheckBoxTreeItem<String>(PRIMARY);
    private CheckBoxTreeItem<String> dependenciesItem = new CheckBoxTreeItem<String>(DEPENDENCIES);
    private CheckBoxTreeItem<String> errorItem = new CheckBoxTreeItem<String>(ERROR);

    private HashMap<String, CheckBoxTreeItem<String>> structure = new HashMap<String, CheckBoxTreeItem<String>>();

    public MibsTree() {
        super();

        createTree();
    }

    private void resetItems() {
        rootItem = new CheckBoxTreeItem<String>(ROOT);
        primaryItem = new CheckBoxTreeItem<String>(PRIMARY);
        dependenciesItem = new CheckBoxTreeItem<String>(DEPENDENCIES);
        errorItem = new CheckBoxTreeItem<String>(ERROR);
        structure = new HashMap<String, CheckBoxTreeItem<String>>();

        structure.put(ROOT, rootItem);
        structure.put(PRIMARY, primaryItem);
        structure.put(DEPENDENCIES, dependenciesItem);
        structure.put(ERROR, errorItem);
    }

    public void createTree() {
        SharedMatMibOperations shared = SharedMatMibOperations.getInstance();

        resetItems();

        setRoot(rootItem);
        rootItem.setExpanded(true);
        primaryItem.setExpanded(true);

        setCellFactory(CheckBoxTreeCell.<String> forTreeView());

        rootItem.getChildren().add(primaryItem);
        rootItem.getChildren().add(dependenciesItem);
        rootItem.getChildren().add(errorItem);

        populateBranch(primaryItem, shared.getPrimaryMibList());
        populateBranch(dependenciesItem, shared.getDependancieMibList());
        populateBranch(errorItem, shared.getParsedWithErrorMibList());

    }

    public String getRootItem() {

        return null;
    }

    @SuppressWarnings("unchecked")
    private void populateBranch(TreeItem<String> item, ArrayList<String> arrayList) {
        Collections.sort(arrayList);

        for (String name : arrayList) {
            CheckBoxTreeItem<String> subItem = new CheckBoxTreeItem<String>(name);
            subItem.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), new MibsCheckBoxController());
            item.getChildren().add(subItem);
            if (!structure.containsKey(name)) {
                structure.put(name, subItem);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void populateBranch(TreeItem<String> item, Map<String, String> map) {
        // Collections.sort(map);
        Map<String, String> treeMap = new TreeMap<String, String>(map);

        for (String name : treeMap.keySet()) {
            CheckBoxTreeItem<String> subItem = new CheckBoxTreeItem<String>(name);
            subItem.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(), new MibsCheckBoxController());
            item.getChildren().add(subItem);
            if (!structure.containsKey(name)) {
                structure.put(name, subItem);
            }
        }

    }

    /**
     * Returns items which are only in the given list
     * 
     * @param map
     *            List of all mibs in given category, i.e primary list, error list
     * @param searchResultList
     *            List of all founded mibs
     * @return filtered list
     */
    private ArrayList<String> searchFilter(Map<String, String> map, List<String> searchResultList) {
        ArrayList<String> returnArraylist = new ArrayList<String>();

        for (String item : searchResultList) {
            if (map.containsKey(item)) {
                returnArraylist.add(item);
            }
        }

        return returnArraylist;
    }

    public void searchUpdate(String name) {
        SharedMatMibOperations shared = SharedMatMibOperations.getInstance();

        resetItems();

        rootItem.setExpanded(true);
        primaryItem.setExpanded(true);
        dependenciesItem.setExpanded(true);
        errorItem.setExpanded(true);

        setRoot(rootItem);

        setCellFactory(CheckBoxTreeCell.<String> forTreeView());

        rootItem.getChildren().add(primaryItem);
        rootItem.getChildren().add(dependenciesItem);
        rootItem.getChildren().add(errorItem);

        List<String> searchResultList = Search.getInstance().findMibsByName(name, true);

        populateBranch(primaryItem, searchFilter(shared.getPrimaryMibList(), searchResultList));
        populateBranch(dependenciesItem, searchFilter(shared.getDependancieMibList(), searchResultList));
        populateBranch(errorItem, searchFilter(shared.getParsedWithErrorMibList(), searchResultList));
    }

    public CheckBoxTreeItem<String> getTreeItem(String mibName) {
        return structure.get(mibName);
    }

}
