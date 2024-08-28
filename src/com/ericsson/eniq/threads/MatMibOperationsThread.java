package com.ericsson.eniq.threads;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.adventnet.snmp.mibs.MibException;
import com.ericsson.eniq.utils.*;

/**
 *
 * @author etarvol
 *
 */
public class MatMibOperationsThread extends Thread {
    private ArrayList<String> fileNames = new ArrayList<String>();
    int fileNamesSize = 0;
    private String path = null;
    private Boolean load = true;// to run the loop for each dependency MIB once after loaded
    private String repositoryPath = null;
    private SharedMatMibOperations shared = SharedMatMibOperations.getInstance();

    public MatMibOperationsThread() {
        fileNamesSize = fileNames.size();
        path = Loader.getInstance().getPath();
        repositoryPath = Loader.getInstance().getRepositoryPath();
    }

    public void addFile(String name) {
        fileNames.add(name);
        fileNamesSize = fileNames.size();
    }

    @Override
    public void run() {
        for (int i = 0; i < fileNamesSize; i++) {
            load = true;
            String name = fileNames.get(i);
            LoadingLabelQueue.getInstance().insertString("Loading " + name);
            while (load) {// to run the loop for each dependency MIB
                load = false;
                try {
                    shared.get().loadMibModule(path + "/" + name);
                } catch (FileNotFoundException e) {
                    String lookupMibName = MibUtils.getMibNotFound(e.getMessage());
                    shared.addNotLocalyFound(lookupMibName, path + "/" + lookupMibName);
                    try {
                        //
                        //            Loading dependancy
                        //
                        load = true;
                        shared.get().loadMibModule(repositoryPath + "/" + lookupMibName);
                        shared.addDependancieMib(lookupMibName, repositoryPath + "/" + lookupMibName);
                        //
                        //            Trying to load again
                        //
                        //shared.get().loadMibModule(path + "/" + name);// commented to take care of loop by itself
                    } catch (FileNotFoundException e1) {
                        load = false;
                        shared.addNotRepositoryFound(lookupMibName, repositoryPath + "/" + name);
                        shared.addParsedWithError(lookupMibName, path + "/" + name);
                        shared.putParsedErrors(lookupMibName, e.getMessage());
                        // e.printStackTrace();
                    } catch (MibException e1) {
                        shared.addParsedWithError(lookupMibName, path + "/" + name);
                        shared.putParsedErrors(lookupMibName, e1.getMessage());
                        // e1.printStackTrace();
                    } catch (IOException e1) {
                        shared.addParsedWithError(lookupMibName, path + "/" + name);
                        shared.putParsedErrors(name, e1.getMessage());
                        // e1.printStackTrace();
                    }
                } catch (MibException e) {
                    shared.addParsedWithError(name, path + "/" + name);
                    shared.putParsedErrors(name, e.getMessage());
                    // e.printStackTrace();
                } catch (IOException e) {
                    shared.putParsedErrors(name, e.getMessage());
                    // e.printStackTrace();
                }
            }
        }
    }
}