package com.ericsson.eniq.threads;

import java.util.ArrayList;

import com.ericsson.eniq.utils.PostLoadingLabels;
import com.ericsson.eniq.utils.Search;

import javafx.concurrent.Task;

/**
 *
 * @author etarvol
 *
 */
public class TasksHandler extends Task<Void> {
    // Current thread in execution
    private MatMibOperationsThread currentThread = null;

    // List of files
    private ArrayList<String> filenames = new ArrayList<String>();

    private int maxThreadProgress = 0;

    // Current thread index
    private int threadIndex = 0;

    private int threadProgress = 0;

    // List of threads
    private ArrayList<MatMibOperationsThread> threads = new ArrayList<MatMibOperationsThread>();
    // 1 file per thread (default)
    private int threadsPerFiles = 1;

    public TasksHandler(ArrayList<String> filenamesParam) {
        filenames = filenamesParam;
    }

    @Override
    protected Void call() throws Exception {
        //
        // Creating threads
        //
        createThreads();
        //
        //	MAKE SURE YOU RESET IN SOMEWHERE ELSE IN CASE YOU REMOVE IT FROM HERE
        //
        Search.getInstance().resetAll();

        PostLoadingLabels postLoadingLabels = PostLoadingLabels.getInstance();
        //
        //	Starting timer
        //
        postLoadingLabels.startTimer();

        while (threadIndex != -1) {
            if (currentThread == null) {
                currentThread = threads.get(threadIndex);
                currentThread.start();
                threadProgress++;
            }

            if (!currentThread.isAlive()) {
                threadIndex--;
                if (threadIndex > -1) {
                    threads.remove(currentThread);
                    currentThread = threads.get(threadIndex);
                    currentThread.start();
                    threadProgress++;
                }
                //
                //	Updating labels
                //
                postLoadingLabels.updateLoaded(SharedMatMibOperations.getInstance().get().getModuleSize());
                postLoadingLabels.updateError(SharedMatMibOperations.getInstance().getParsedWithErrorMibList().size());
            }
            //
            //	Progress control
            //
            updateProgress(threadProgress, maxThreadProgress);
        }

        //
        //	Stopping timer
        //
        postLoadingLabels.stopTimer();

        return null;
    }

    private void createThreads() {
        int filesAmount = filenames.size();
        int threadsAmount = (int) Math.ceil(filesAmount / (threadsPerFiles * 1.0));
        int fileStep = (int) Math.ceil(filesAmount / (threadsAmount * 1.0));
        int start = 0;
        int end = fileStep;
        for (int i = 0; i < threadsAmount; i++) {
            MatMibOperationsThread thread = new MatMibOperationsThread();

            for (int j = start; j < end; j++) {
                if (j < filesAmount) {
                    thread.addFile(filenames.get(j));
                }
            }

            threads.add(thread);
            start = end;
            end += fileStep;
        }
        //
        // Setting our index to max size of an array - 1
        //
        threadIndex = threads.size() - 1;
        //
        //	Progress control
        //
        threadProgress = 0;
        maxThreadProgress = threads.size();
        updateProgress(0.0, maxThreadProgress);
    }

    public int getThreadsPerFiles() {
        return threadsPerFiles;
    }

    public void setThreadsPerFiles(int threadsPerFiles) {
        this.threadsPerFiles = threadsPerFiles;
    }
}
