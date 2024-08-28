package com.ericsson.eniq.gui.controllers;

import java.util.ArrayList;

import com.ericsson.eniq.tabs.MainTab;
import com.ericsson.eniq.tabs.TabsManager;
import com.ericsson.eniq.threads.*;
import com.ericsson.eniq.utils.*;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class LoadingButtonController implements EventHandler<ActionEvent> {
    private TextField pathTextField;
    private ProgressBar progressBar = null;

    public LoadingButtonController(ProgressBar bar, TextField pathTextField) {
        this.progressBar = bar;
        this.pathTextField = pathTextField;
    }

    @Override
    public void handle(ActionEvent event) {
        final TabsManager tabManager = TabsManager.getInstance();
        //
        // Setting user selected path
        //
        Loader.getInstance().setPath(pathTextField.getText());
        PostLoadingLabels.getInstance().reset();
        //
        // Getting list of files
        //
        ArrayList<String> files = Loader.getInstance().loadFiles();
        //
        // If no files found then exit
        //
        if (files.isEmpty()) {
            return; // exit
        }
        final TasksHandler taskHandler = new TasksHandler(files);
        taskHandler.setThreadsPerFiles(1);
        Thread taskHandlerThread = new Thread(taskHandler);
        taskHandlerThread.setDaemon(true);

        final DataCollectorTask dataCollectorTask = new DataCollectorTask();
        final Thread dataCollectorThread = new Thread(dataCollectorTask);
        dataCollectorThread.setDaemon(true);

        progressBar.progressProperty().bind(taskHandler.progressProperty());
        progressBar.setVisible(true);
        //
        // Starting thread and attaching event
        //
        taskHandlerThread.start();
        //
        //	Adding listener for when thread starts
        //
        taskHandler.setOnScheduled(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                LoadingLabelQueue.getInstance().setVisible(true);
                progressBar.setVisible(true);
                //
                //	Removing current Main Tab
                //
                tabManager.removeTab(tabManager.getCurrentMainTab());
            }
        });
        //
        // Adding listener for when handler is done
        //
        taskHandler.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                dataCollectorThread.start();

            }
        });

        dataCollectorTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                // TODO Auto-generated method stub
                //
                // Creating our tabs
                //
                MainTab tab = new MainTab();
                tab.setText("MIB's Tab");
                tab.setClosable(false);
                tabManager.addTab(tab);
                //
                //	Setting focus on currently created main tab
                //
                tabManager.getPane().getSelectionModel().select(tab);
                //
                //	Make elements invisible
                //
                LoadingLabelQueue.getInstance().setVisible(false);
                progressBar.setVisible(false);

                //
                //	Destroy the object
                //
                SharedMatMibOperations.getInstance().destroy();
            }
        });

    }

}
