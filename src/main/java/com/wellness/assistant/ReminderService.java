package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Background service that monitors habits and shows reminders.
 */
public class ReminderService {
    private final ScheduledExecutorService scheduler;
    private final DatabaseManager dbManager;
    private boolean isRunning = false;

    public ReminderService() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.dbManager = DatabaseManager.getInstance();
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            // Check for reminders every 60 seconds
            scheduler.scheduleAtFixedRate(this::checkReminders, 0, 60, TimeUnit.SECONDS);
            System.out.println("ReminderService started");
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            scheduler.shutdown();
            System.out.println("ReminderService stopped");
        }
    }

    private void checkReminders() {
        try {
            List<Habit> habits = dbManager.getAllHabits();
            LocalTime currentTime = LocalTime.now();
            LocalDate today = LocalDate.now();

            for (Habit habit : habits) {
                if (habit.isActive() && shouldShowReminder(habit, currentTime, today)) {
                    showReminder(habit);
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking reminders: " + e.getMessage());
        }
    }

    private boolean shouldShowReminder(Habit habit, LocalTime currentTime, LocalDate today) {
        LocalTime habitTime = habit.getTime();
        
        // Check if current time is within 1 minute of habit time
        if (Math.abs(currentTime.toSecondOfDay() - habitTime.toSecondOfDay()) <= 60) {
            // Check frequency
            String frequency = habit.getFrequency();
            if ("daily".equals(frequency)) {
                return true;
            } else if ("weekdays".equals(frequency)) {
                // Check if today is a weekday (Monday = 1, Sunday = 7)
                int dayOfWeek = today.getDayOfWeek().getValue();
                return dayOfWeek >= 1 && dayOfWeek <= 5;
            }
        }
        return false;
    }

    private void showReminder(Habit habit) {
        Platform.runLater(() -> {
            // Show notification using ControlsFX
            Notifications.create()
                    .title("Wellness Hub Reminder")
                    .text("Time for " + habit.getName() + "!")
                    .showInformation();
        });
    }

    public void showManualReminder(Habit habit) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Wellness Hub");
            alert.setHeaderText("Time for " + habit.getName() + "!");
            alert.setContentText("Have you completed this activity?");

            ButtonType doneButton = new ButtonType("Done");
            ButtonType skipButton = new ButtonType("Skip");
            alert.getButtonTypes().setAll(doneButton, skipButton);

            alert.showAndWait().ifPresent(buttonType -> {
                try {
                    String status = buttonType == doneButton ? "Completed" : "Skipped";
                    HealthLog log = new HealthLog(habit.getId(), habit.getName(), status);
                    dbManager.logActivity(log);
                    System.out.println("Logged activity: " + habit.getName() + " - " + status);
                } catch (Exception e) {
                    System.err.println("Error logging activity: " + e.getMessage());
                }
            });
        });
    }
}