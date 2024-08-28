package com.ericsson.eniq.utils;

/**
 * Class which run garbage collection
 * 
 * @author etarvol
 *
 */
public class MemoryManager {
    private static MemoryManager thisInstance = null;

    private MemoryManager() {

    }

    public static MemoryManager getInstance() {
        if (thisInstance == null) {
            thisInstance = new MemoryManager();
        }

        return thisInstance;
    }

    public void gc() {
        Runtime.getRuntime().runFinalization();
        System.gc();
        Runtime.getRuntime().gc();
    }
}
