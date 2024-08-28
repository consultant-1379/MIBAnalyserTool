package com.ericsson.eniq.threads;

import java.lang.reflect.Field;
import java.util.*;

import com.adventnet.snmp.mibs.MibOperations;
import com.ericsson.eniq.utils.MemoryManager;

/**
 *
 * @author etarvol
 *
 */
public class SharedMatMibOperations {

    public static long lastTime = 0;
    public static long startTime = 0;
    private static SharedMatMibOperations thisInstance = null;

    private volatile MibOperations mibOps = null;

    private Map<String, String> dependancieMibList = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, String> notLocalyFoundMibList = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, String> notRepositoryFoundMibList = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, String> parsedErrors = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, String> parsedWithErrorMibList = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, String> primaryMibList = Collections.synchronizedMap(new HashMap<String, String>());

    public synchronized static SharedMatMibOperations getInstance() {
        if (thisInstance == null) {
            startTime = System.currentTimeMillis();

            thisInstance = new SharedMatMibOperations();
        }

        return thisInstance;
    }

    private SharedMatMibOperations() {
        mibOps = new MibOperations();
    }

    public synchronized void destroy() {
        mibOps.unloadAllMibModules();
        mibOps = null;
        MemoryManager.getInstance().gc();

        // System.out.println("correctMibList: " + correctMibList);
        // System.out.println("dependancieMibList: "+dependancieMibList);
        // System.out.println("notLocalyFoundMibList: "+notLocalyFoundMibList);
        // System.out.println("notRepositoryFoundMibList: "+notRepositoryFoundMibList);
        // System.out.println("parsedErrors: "+parsedErrors);
        // System.out.println("parsedWithErrorMibList: "+parsedWithErrorMibList);
        // System.out.println("primaryMibList: "+primaryMibList);
    }

    public synchronized MibOperations get() {
        lastTime = System.currentTimeMillis();
        if (mibOps == null) {
            mibOps = new MibOperations();
            resetLists();
        }
        return mibOps;
    }

    private synchronized void resetLists() {
        dependancieMibList = Collections.synchronizedMap(new HashMap<String, String>());
        notLocalyFoundMibList = Collections.synchronizedMap(new HashMap<String, String>());
        notRepositoryFoundMibList = Collections.synchronizedMap(new HashMap<String, String>());
        parsedErrors = Collections.synchronizedMap(new HashMap<String, String>());
        parsedWithErrorMibList = Collections.synchronizedMap(new HashMap<String, String>());
        primaryMibList = Collections.synchronizedMap(new HashMap<String, String>());

        MemoryManager.getInstance().gc();
    }

    @SuppressWarnings("rawtypes")
    public synchronized String getlastLoadedMibName() {
        String name = "";

        try {
            Field lastModule = mibOps.getClass().getDeclaredField("moduleNameVector");
            lastModule.setAccessible(true);

            name = ((Vector) lastModule.get(mibOps)).get(0).toString();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return name;
    }

    public synchronized Map<String, String> getDependancieMibList() {
        return dependancieMibList;
    }

    public synchronized Map<String, String> getNotLocalyFoundMibList() {
        return notLocalyFoundMibList;
    }

    public synchronized Map<String, String> getNotRepositoryFoundMibList() {
        return notRepositoryFoundMibList;
    }

    public synchronized Map<String, String> getParsedErrors() {
        return parsedErrors;
    }

    public synchronized Map<String, String> getParsedWithErrorMibList() {
        return parsedWithErrorMibList;
    }

    public synchronized Map<String, String> getPrimaryMibList() {
        return primaryMibList;
    }

    public synchronized void setDependancieMibList(Map<String, String> dependancieMibList) {
        this.dependancieMibList = dependancieMibList;
    }

    public synchronized void setNotLocalyFoundMibList(Map<String, String> notLocalyFoundMibList) {
        this.notLocalyFoundMibList = notLocalyFoundMibList;
    }

    public synchronized void setNotRepositoryFoundMibList(Map<String, String> notRepositoryFoundMibList) {
        this.notRepositoryFoundMibList = notRepositoryFoundMibList;
    }

    public synchronized void setParsedErrors(HashMap<String, String> parsedError) {
        this.parsedErrors = parsedError;
    }

    public synchronized void setParsedWithErrorMibList(Map<String, String> parsedWithErrorMibList) {
        this.parsedWithErrorMibList = parsedWithErrorMibList;
    }

    public synchronized void setPrimaryMibList(Map<String, String> primaryMibList) {
        this.primaryMibList = primaryMibList;
    }

    public synchronized void addPrimaryMib(String lookupMibName, String path) {
        if (!primaryMibList.containsKey(lookupMibName)) {
            primaryMibList.put(lookupMibName, path);
        }
    }

    public synchronized void addNotLocalyFound(String lookupMibName, String path) {
        if (!notLocalyFoundMibList.containsKey(lookupMibName)) {
            notLocalyFoundMibList.put(lookupMibName, path);
        }
    }

    public synchronized void addDependancieMib(String lookupMibName, String path) {
        if (!dependancieMibList.containsKey(lookupMibName)) {
            dependancieMibList.put(lookupMibName, path);
        }
    }

    public synchronized void addNotRepositoryFound(String lookupMibName, String path) {
        if (!notRepositoryFoundMibList.containsKey(lookupMibName)) {
            notRepositoryFoundMibList.put(lookupMibName, path);
        }
    }

    public synchronized void putParsedErrors(String lookupMibName, String message) {
        if (!parsedErrors.containsKey(lookupMibName)) {
            parsedErrors.put(lookupMibName, message);
        }
    }

    public synchronized void addParsedWithError(String lookupMibName, String path) {
        if (!parsedWithErrorMibList.containsKey(lookupMibName)) {
            parsedWithErrorMibList.put(lookupMibName, path);
        }
    }
}
