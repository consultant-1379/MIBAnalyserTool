package com.ericsson.eniq.utils;

import java.util.*;

import com.ericsson.eniq.dto.MibNodeDto;
import com.ericsson.eniq.gui.components.MibsTree;
import com.ericsson.eniq.threads.SharedMatMibOperations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author etarvol
 *
 */
public class Search {

    public static enum SearchBy {
        NAME, OID, MIB, STATUS, SYNTAX
    }

    private static Search thisInstance = null;

    public static Search getInstance() {
        if (thisInstance == null) {
            thisInstance = new Search();
        }

        return thisInstance;
    }

    // ComboBox names
    public static final String COMBO_COUNTER_NAME = "Counter Name";
    public static final String COMBO_COUNTER_OID = "Counter Oid";
    public static final String COMBO_COUNTER_MIB = "Mib Name";
    public static final String COMBO_COUNTER_STATUS = "Counter Status";
    public static final String COMBO_COUNTER_SYNTAX = "Counter Syntax";

    // Contains all MibCounters
    private volatile List<MibNodeDto> mibInfoList = Collections.synchronizedList(new ArrayList<MibNodeDto>());
    // Counters->MIB mapping
    private volatile Map<String, String> uniqueMap = Collections.synchronizedMap(new HashMap<String, String>());

    private Search() {
        resetAll();
    }

    public void resetAll() {
        mibInfoList = null;
        uniqueMap = null;
        MemoryManager.getInstance().gc();

        mibInfoList = new ArrayList<MibNodeDto>();
        uniqueMap = new HashMap<String, String>();
    }

    public List<MibNodeDto> filter(String lookUp, SearchBy attr, boolean useRegExp) {
        ArrayList<MibNodeDto> return_list = new ArrayList<MibNodeDto>();

        if (lookUp != null) {
            //
            // Getting rid of any regular expression
            //
            lookUp = formatLookup(lookUp);
            if (lookUp.equals(MibsTree.ROOT) || lookUp.equals(MibsTree.PRIMARY)) {
                return mibInfoList;
            } else if (lookUp.length() == 0) {
                return return_list;
            }
        }
        //
        // If user picks All mibs, then find all counters
        //
        for (MibNodeDto item : mibInfoList) {

            String currentAttrValue = getAttrValue(item, attr);

            if (currentAttrValue == null) {
                continue;
            }

            if (useRegExp) {
                if (currentAttrValue.matches("(?i:" + lookUp + ".*)")) {
                    return_list.add(item);
                }
            } else {
                if (currentAttrValue.equals(lookUp)) {
                    return_list.add(item);
                }
            }

        }

        //
        // Sorting results by oid
        //
        Collections.sort(return_list, new Comparator<MibNodeDto>() {

            @Override
            public int compare(MibNodeDto dto1, MibNodeDto dto2) {
                if (dto1.getOid().hashCode() > dto2.getOid().hashCode()) {
                    return 1;
                } else if (dto1.getOid().hashCode() < dto2.getOid().hashCode()) {
                    return -1;
                }
                return 0;
            }
        });

        return return_list;
    }

    public List<MibNodeDto> getFilteredCounters() {

        List<String> selectedMibs = MibsSelector.getInstance().getSelected();
        List<MibNodeDto> mibNodes = new ArrayList<MibNodeDto>();
        SelectedParserOptions options = SelectedParserOptions.getInstance();
        //
        // Going over the mib
        //
        for (String mibName : selectedMibs) {
            // System.out.println("Mib Name: " + mibName);
            //
            // Going over the counters of this mib
            //
            List<MibNodeDto> counters = findCountersByMIB(mibName, false);
            for (MibNodeDto counter : counters) {
                if (counter.isCounter() || counter.isIndexCounter()) {

                    boolean accessMatch = options.getSelectedAccess().isEmpty() ? true : (options.getSelectedAccess().contains(counter.getAccess()));
                    boolean syntaxMatch = options.getSelectedSyntax().isEmpty() ? true : (options.getSelectedSyntax().contains(counter.getSyntax()));
                    boolean statusMatch = options.getSelectedStatus().isEmpty() ? true : (options.getSelectedStatus().contains(counter.getStatus()));

                    if (accessMatch && syntaxMatch && statusMatch) {
                        mibNodes.add(counter);
                    }
                }

            }
        }

        return mibNodes;
    }

    public List<MibNodeDto> findCounterByStatus(String status, boolean useRegExp) {
        return filter(status, SearchBy.STATUS, useRegExp);
    }

    public List<MibNodeDto> findCountersByMIB(String mib, boolean useRegExp) {
        return filter(mib, SearchBy.MIB, useRegExp);
    }

    public List<MibNodeDto> findCounterByName(String name, boolean useRegExp) {
        return filter(name, SearchBy.NAME, useRegExp);
    }

    public List<MibNodeDto> findCounterByOID(String oid, boolean useRegExp) {
        return filter(oid, SearchBy.OID, useRegExp);
    }

    public List<MibNodeDto> findCounterBySyntax(String syntax, boolean useRegExp) {
        return filter(syntax, SearchBy.SYNTAX, useRegExp);
    }

    public List<String> findMibsByName(String lookUp, boolean useRegExp) {
        List<String> return_list = new ArrayList<String>();

        Map<String, String> allMibs = new HashMap<String, String>();
        allMibs.putAll(SharedMatMibOperations.getInstance().getPrimaryMibList());
        allMibs.putAll(SharedMatMibOperations.getInstance().getDependancieMibList());
        allMibs.putAll(SharedMatMibOperations.getInstance().getParsedWithErrorMibList());

        if (lookUp != null) {
            //
            // Getting rid of any regular expression
            //
            lookUp = formatLookup(lookUp);
        }

        for (String name : allMibs.keySet()) {
            if (useRegExp) {
                if (name.matches("(?i:" + lookUp + ".*)")) {
                    return_list.add(name);
                }
            } else {
                if (name.equals(lookUp)) {
                    return_list.add(name);
                }
            }
        }
        Collections.sort(return_list);

        return return_list;
    }

    private String getAttrValue(MibNodeDto d, SearchBy attr) {
        String r = "";

        switch (attr) {
            case NAME:
                r = d.getName();
                break;
            case OID:
                r = d.getOid();
                break;
            case STATUS:
                r = d.getStatus();
                break;
            case MIB:
                r = d.getMibName();
                break;
            case SYNTAX:
                r = d.getSyntax();
                break;

            default:
                // nothing
                break;

        }

        return r;
    }

    public void setMibInfoList(ArrayList<MibNodeDto> mibInfoList) {
        this.mibInfoList = mibInfoList;
    }

    public synchronized void add(MibNodeDto mibInfo) {
        String oid = mibInfo.getOid();
        String mibName = mibInfo.getMibName();

        if (!uniqueMap.containsKey(oid)) {
            mibInfoList.add(mibInfo);
            uniqueMap.put(oid, mibName);

            if (mibInfo.isCounter() || mibInfo.isIndexCounter()) {
                PostLoadingLabels.getInstance().updateCounters();
            }
        }

        if (uniqueMap.containsKey(oid)) {
            String mibNameByOid = uniqueMap.get(oid);
            if (!mibNameByOid.equals(mibName)) {
                mibInfoList.add(mibInfo);
                uniqueMap.put(oid, mibName);

                if (mibInfo.isCounter() || mibInfo.isIndexCounter()) {
                    PostLoadingLabels.getInstance().updateCounters();
                }
            }
        }

    }

    public ObservableList<String> getSearchOptionsNames() {
        SearchBy[] s = SearchBy.values();
        String[] res = new String[s.length - 1];

        res[0] = COMBO_COUNTER_NAME;
        res[1] = COMBO_COUNTER_OID;
        res[2] = COMBO_COUNTER_STATUS;
        res[3] = COMBO_COUNTER_SYNTAX;

        return FXCollections.observableList(Arrays.asList(res));
    }

    private String formatLookup(String input) {
        input = input.replaceAll("\\*", "");
        input = input.replaceAll("\\[", "");
        input = input.replaceAll("\\]", "");

        return input;
    }

}
