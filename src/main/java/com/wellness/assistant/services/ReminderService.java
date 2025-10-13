package com.wellness.assistant.services;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReminderService {

    private final Timer scheduler = new Timer("ReminderScheduler", true);

    public void scheduleReminder(String habitType, int intervalInSeconds) {
        TimerTask reminderTask = new TimerTask() {
            @Override
            public void run() {
                // UI updates must be run on the JavaFX Application Thread
                Platform.runLater(() -> {
                    showNotification(habitType);
                });
            }
        };

        long intervalInMillis = intervalInSeconds * 1000L;
        // Schedule the task to run after an initial delay, and then repeat
        scheduler.scheduleAtFixedRate(reminderTask, intervalInMillis, intervalInMillis);
        
        System.out.println("Scheduled reminder for: " + habitType + " every " + intervalInSeconds + " seconds.");
    }

    private void showNotification(String habitType) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Health Reminder");
        alert.setHeaderText("Time for your " + habitType + " habit!");
        alert.setContentText("A friendly reminder to take a moment for your well-being.");
        alert.showAndWait();
    }

    public void stopScheduler() {
        scheduler.cancel();
        System.out.println("Reminder scheduler stopped.");
    }
}