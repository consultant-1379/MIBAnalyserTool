package com.ericsson.eniq.utils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ericsson.eniq.threads.SharedMatMibOperations;

/**
 * Class for creating ZIP Archives
 *
 * @author etarvol
 *
 */
public class ZipCreator {
    private static ZipCreator thisInstance = null;

    private ZipCreator() {

    }

    public static ZipCreator getInstance() {
        if (thisInstance == null) {
            thisInstance = new ZipCreator();
        }

        return thisInstance;
    }

    /**
     * Creates zip file with currently selected MIBs
     * 
     * @param saveToFile
     *            File Handler for where ZIP will be saved
     * @throws IOException
     *             Exception in case of an error
     */
    public void createZip(File saveToFile) throws IOException {
        //
        // Getting our paths
        //
        String primaryPath = Loader.getInstance().getPath();
        String dependancyPath = Loader.getInstance().getRepositoryPath();
        //
        // Getting required files
        //
        File[] files = getMibFiles();
        saveToFile.createNewFile();
        //
        // Creating file output stream for our zip output stream
        //
        FileOutputStream fileOut = new FileOutputStream(saveToFile);
        //
        // Creating zip output stream
        //
        ZipOutputStream zipOut = new ZipOutputStream(fileOut);
        //
        // Creating buffer for writing our zip entries into zip output stream
        //
        byte[] buffer = new byte[1024];
        //
        // Creating input streams
        //
        FileInputStream[] streams = new FileInputStream[files.length];
        List<String> filePaths = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            String subFolder = "./";
            streams[i] = new FileInputStream(files[i]);
            //
            // Checking into which folder put the mib
            //
            if (files[i].getAbsolutePath().contains(primaryPath)) {
                subFolder = "primary/";
            }
            if (files[i].getAbsolutePath().contains(dependancyPath)) {
                subFolder = "dependancy/";
            }
            String currPath = subFolder + "" + files[i].getName();
            if (filePaths.contains(currPath)) {
                continue;
            }
            filePaths.add(currPath);
            //
            // Inserting new entry with given filename
            //
            try {
                zipOut.putNextEntry(new ZipEntry(currPath));
            } catch (IOException entryException) {
                continue;
            }
            //
            // Writing our entry
            //
            int length;
            while ((length = streams[i].read(buffer)) > 0) {
                zipOut.write(buffer, 0, length);
            }
            //
            // Closing streams
            //
            zipOut.closeEntry();
            streams[i].close();
        }
        //
        // Closing all necessary streams
        //
        zipOut.flush();
        zipOut.finish();
        zipOut.close();
        fileOut.flush();
        fileOut.close();

    }

    /**
     * Returns array of Files based on loaded MIBs
     * 
     * @return File[] array
     */
    private File[] getMibFiles() {
        Map<String, String> primarySet = SharedMatMibOperations.getInstance().getPrimaryMibList();
        Map<String, String> errorSet = SharedMatMibOperations.getInstance().getParsedWithErrorMibList();
        Map<String, String> dependancieSet = SharedMatMibOperations.getInstance().getDependancieMibList();

        ArrayList<File> toExport = new ArrayList<File>();
        //
        // Getting all primary mibs (excluding error mibs)
        //
        for (String key : primarySet.keySet()) {
            if (!errorSet.containsKey(key)) {
                String link = primarySet.get(key);
                if (link != null) {
                    toExport.add(new File(link));
                }
            }
        }
        //
        // Getting only dependencies required
        //
        for (String key : dependancieSet.keySet()) {
            String link = dependancieSet.get(key);
            if (link != null) {
                toExport.add(new File(link));
            }
        }
        //
        // Creating File array
        //
        File[] allArr = toExport.toArray(new File[toExport.size()]);

        return allArr;
    }
}
