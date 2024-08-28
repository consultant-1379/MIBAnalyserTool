package com.ericsson.eniq.filebrowser;

import java.io.File;

import com.ericsson.eniq.utils.Loader;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 * Class which represents tree item for file browser
 *
 * @author etarvol
 *
 */
public class FileTreeItem extends TreeItem<String> {
    private String filePath = "";

    private ImageView folderClosed = Loader.getImageView("images/filebrowser/folder.png");
    TreeItem<String> invisibleItem = new TreeItem<String>();

    @SuppressWarnings({ "unchecked", "rawtypes", })
    public FileTreeItem(final FileTreeView fileTreeView, String path) {

        filePath = path;

        setValue(getLastPathSegment(path));
        setGraphic(folderClosed);
        //
        // Adding event handler for when branch of this TreeItem is being
        // expanded
        //
        this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {

            @Override
            public void handle(Event event) {
                FileTreeItem item = (FileTreeItem) event.getSource();
                File dir = new File(item.getFilePath());
                //
                // If item has children
                //
                if (item.getChildren().size() > 0) {
                    //
                    // If first child is an invisible item, then remove it
                    //
                    if (item.getChildren().get(0).equals(invisibleItem)) {
                        item.getChildren().remove(0);
                    }
                }
                //
                // If size amount of children is less than 1
                //
                if (item.getChildren().size() < 1) {
                    //
                    // Get list of files within this current filepath
                    //
                    File[] fileList = dir.listFiles();

                    if (fileList != null) {
                        for (File file : fileList) {
                            //
                            // Check only for directories and visible files
                            //
                            if (!file.isDirectory() || file.isHidden()) {
                                continue;
                            }

                            FileTreeItem subItem = new FileTreeItem(fileTreeView, file.getAbsolutePath());

                            subItem.getChildren().add(invisibleItem);
                            item.getChildren().add(subItem);
                        }
                    }

                }
            }
        });
    }

    /**
     * Returns file path assigned to this TreeItem
     *
     * @return path as string
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns last segment of a given path
     *
     * @param path
     *            Filepath
     * @return last segment of a path as string
     */
    private String getLastPathSegment(String path) {
        String[] paths = path.split("\\\\");

        if (paths.length > 0) {
            return paths[paths.length - 1];
        } else {
            return path;
        }
    }

    /**
     * Sets current item filepath
     *
     * @param filePath
     *            Filepath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
