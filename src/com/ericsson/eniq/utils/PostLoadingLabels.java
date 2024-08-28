package com.ericsson.eniq.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class PostLoadingLabels {
    private final String loadedText = "Loaded MIBs: ";
    private final String errorText = "Error MIBS: ";
    private final String countersText = "Total Counters: ";
    private final String elapsedText = "Elapsed Time: ";

    private Label loadedMibsLabel = new Label(loadedText + 0);
    private Label errorMibsLabel = new Label(errorText + 0);
    private Label countersLabel = new Label(countersText + 0);
    private Label elapsedLabel = new Label(elapsedText + 0);

    private static int countersCounter = 0;
    private static long elapsedStartCounter = 0;
    private AnimationTimer timer = new AnimationTimer() {

        @Override
        public void handle(long now) {
            updateTimeLabel();
        }
    };

    private static PostLoadingLabels thisInstance = null;

    private PostLoadingLabels() {
        elapsedStartCounter = System.currentTimeMillis();
    }

    public static PostLoadingLabels getInstance() {
        if (thisInstance == null) {
            thisInstance = new PostLoadingLabels();
        }

        return thisInstance;
    }

    public void startTimer() {
        elapsedStartCounter = System.currentTimeMillis();
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void reset() {
        countersCounter = 0;
        elapsedStartCounter = System.currentTimeMillis();
        loadedMibsLabel.setText(loadedText + 0);
        errorMibsLabel.setText(errorText + 0);
        countersLabel.setText(countersText + 0);
        elapsedLabel.setText(elapsedText + 0);
    }

    private void updateTimeLabel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                long delta = System.currentTimeMillis() - elapsedStartCounter;
                Date date = new Date(delta);
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                String dateFormatted = formatter.format(date);
                elapsedLabel.setText(elapsedText + dateFormatted);

            }
        });
    }

    public void updateLoaded(final int value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                loadedMibsLabel.setText(loadedText + value);
            }
        });
    }

    public void updateError(final int value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                errorMibsLabel.setText(errorText + value);
            }
        });
    }

    public void updateCounters() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                countersCounter++;
                countersLabel.setText(countersText + countersCounter);
            }
        });
    }

    public Label getLoadedMibsLabel() {
        return loadedMibsLabel;
    }

    public void setLoadedMibsLabel(Label loadedMibsLabel) {
        this.loadedMibsLabel = loadedMibsLabel;
    }

    public Label getErrorMibsLabel() {
        return errorMibsLabel;
    }

    public void setErrorMibsLabel(Label errorMibsLabel) {
        this.errorMibsLabel = errorMibsLabel;
    }

    public Label getCountersLabel() {
        return countersLabel;
    }

    public void setCountersLabel(Label countersLabel) {
        this.countersLabel = countersLabel;
    }

    public Label getElapsedLabel() {
        return elapsedLabel;
    }

    public void setElapsedLabel(Label elapsedLabel) {
        this.elapsedLabel = elapsedLabel;
    }
}
