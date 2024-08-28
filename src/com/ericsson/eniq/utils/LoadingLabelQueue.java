package com.ericsson.eniq.utils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoadingLabelQueue {
    private static LoadingLabelQueue thisInstance = null;

    private int size = 5;
    private int index = size;
    private Label[] labels = new Label[size];

    private LoadingLabelQueue() {
        createLables();
    }

    public LoadingLabelQueue(int newSize) {
        size = newSize;
    }

    public static LoadingLabelQueue getInstance() {
        if (thisInstance == null) {
            thisInstance = new LoadingLabelQueue();
        }

        return thisInstance;
    }

    private void createLables() {
        double fontSizeValue = 4.2;
        double fontSize = fontSizeValue;
        for (int i = 0; i < size; i++) {
            if (i <= size / 2) {
                fontSize += fontSizeValue;
            } else {
                fontSize -= fontSizeValue;
            }
            labels[i] = new Label();
            labels[i].setTextFill(Color.BLACK);
            Font font = Font.font(null, fontSize);
            labels[i].setFont(font);
        }
    }

    public Label[] getLabels() {
        return labels;
    }

    public synchronized void insertString(final String str) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                index--;
                if (index < 0) {
                    index = size - 1;
                }

                labels[index].setText(str);
                labels[index].setTextFill(Color.BLACK);
            }
        });
    }

    public synchronized void insertString(final String str, final Color color) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                index--;
                if (index < 0) {
                    index = size - 1;
                }

                labels[index].setText(str);
                labels[index].setTextFill(color);
            }
        });
    }

    public void setVisible(boolean isVisible) {
        for (int i = 0; i < size; i++) {
            labels[i].setVisible(isVisible);
        }
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
    }
}
