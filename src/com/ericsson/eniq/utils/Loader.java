package com.ericsson.eniq.utils;

import java.io.File;
import java.util.ArrayList;

import com.ericsson.eniq.dto.MibNodeDto;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Class for loading/scanning MIBs
 *
 * @author etarvol
 *
 */
public class Loader {
    //
    // Storing actual list of files
    //
    private static ArrayList<String> fileList = new ArrayList<String>();
    //
    // Instance storage
    //
    private static Loader thisInstance = null;
    //
    // Current selected path
    //
    private String path = null;
    //
    // Path to repository (BE AWARE OF SLASHES !!!)
    //
    private String repository_path = ".\\MIB_repository\\";
    //
    // Images for counters, tables, entries etc.
    //
    private Image tableImage;
    private Image entryImage;
    private Image indexImage;
    private Image counterImage;

    private Loader() {
        loadImages();
    }

    public static Loader getInstance() {
        if (thisInstance == null) {
            thisInstance = new Loader();
        }

        return thisInstance;
    }

    /**
     * Returns Image depending on node(counter) type
     * 
     * @param dto
     *            DTO of mib node
     * @return Returns Node
     */
    public Node getEntryTypeImage(MibNodeDto dto) {
        ImageView imageView = new ImageView();

        if (dto.getEntryType().equals(MibNodeDto.TABLE)) {
            imageView.setImage(tableImage);
        }

        if (dto.getEntryType().equals(MibNodeDto.TABLE_ENTRY)) {
            imageView.setImage(entryImage);
        }

        if (dto.getEntryType().equals(MibNodeDto.INDEX_COUNTER)) {
            imageView.setImage(indexImage);
        }

        if (dto.getEntryType().equals(MibNodeDto.COUNTER)) {
            imageView.setImage(counterImage);
        }

        return imageView;
    }

    /**
     * Returns ImagaView based on a give path
     * 
     * @param filePath
     *            path to the image
     * @return ImageView object
     */
    public static ImageView getImageView(String filePath) {
        return new ImageView(new Image("file:" + filePath + ""));
    }

    public ArrayList<String> getFileList() {
        return fileList;
    }

    public String getPath() {
        return path;
    }

    public String getRepositoryPath() {
        return repository_path;
    }

    /**
     * Method which scan path and returns list of files
     * 
     * @return ArrayList of filenames as Strings
     */
    public ArrayList<String> loadFiles() {
        ArrayList<String> list = new ArrayList<String>();

        File[] files = new File(path).listFiles();
        if (files != null) {

            int size = files.length;
            for (int i = 0; i < size; i++) {
                if (!files[i].isFile()) {
                    continue;
                }

                list.add(files[i].getName());

            }
        } else {
            LoadingLabelQueue.getInstance().insertString("No MIBS found", Color.RED);
        }
        Loader.fileList = list;
        return list;
    }

    /**
     * Method which scan given path and returns list of files
     * 
     * @return ArrayList of filenames as Strings
     */
    public ArrayList<String> loadFiles(String pathParam) {
        ArrayList<String> list = new ArrayList<String>();

        File[] files = new File(pathParam).listFiles();
        if (files != null) {
            int size = files.length;
            for (int i = 0; i < size; i++) {
                if (!files[i].isFile()) {
                    continue;
                }

                list.add(files[i].getName());

            }
        } else {
            LoadingLabelQueue.getInstance().insertString("No MIBS found", Color.RED);
        }
        Loader.fileList = list;
        return list;
    }

    /**
     * Method which loads necessary image for tree view
     */
    public void loadImages() {

        tableImage = new Image("file:./images/table.gif");
        entryImage = new Image("file:./images/entry.gif");
        indexImage = new Image("file:./images/index.gif");
        counterImage = new Image("file:./images/counter.gif");

    }

    public void setFileList(ArrayList<String> fileList) {
        Loader.fileList = fileList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRepositoryPath(String eXTERNAL_PATH) {
        repository_path = eXTERNAL_PATH;
    }
}
