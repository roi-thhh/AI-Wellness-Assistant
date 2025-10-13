package com.wellness.assistant.services;

import com.wellness.assistant.model.Reminder;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ReminderService {

    private final Timer scheduler = new Timer("ReminderScheduler", true);

    public void scheduleDailyReminder(Reminder reminder) {
        TimerTask reminderTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> showNotification(reminder.getHabitType()));
            }
        };

        LocalDateTime now = LocalDateTime.now();
        LocalTime reminderTime = reminder.getTime();
        
        // Calculate the first execution time
        LocalDateTime firstExecutionTime = now.toLocalDate().atTime(reminderTime);
        if (now.isAfter(firstExecutionTime)) {
            // If the time has already passed for today, schedule it for tomorrow
            firstExecutionTime = firstExecutionTime.plusDays(1);
        }

        long initialDelay = Duration.between(now, firstExecutionTime).toMillis();
        long period = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS); // 24 hours in millis

        scheduler.scheduleAtFixedRate(reminderTask, initialDelay, period);
        System.out.println("Scheduled daily reminder for '" + reminder.getHabitType() + "' at " + reminderTime);
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