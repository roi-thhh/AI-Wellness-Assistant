package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.application.Platform;
import org.controlsfx.control.Notifications;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start() {
        // This task will run once every minute to check for upcoming reminders
        final Runnable reminderChecker = this::scheduleRemindersForToday;

        // Schedule to run every 60 seconds
        scheduler.scheduleAtFixedRate(reminderChecker, 0, 60, TimeUnit.SECONDS);
    }

    private void scheduleRemindersForToday() {
        System.out.println("Checking for reminders... Current time: " + LocalTime.now());
        List<Habit> habits = DatabaseManager.getInstance().getAllHabits();

        for (Habit habit : habits) {
            try {
                LocalTime habitTime = LocalTime.parse(habit.getTime());
                LocalTime now = LocalTime.now();

                // Check if the habit time is within the next minute
                if (habitTime.isAfter(now) && habitTime.isBefore(now.plusMinutes(1))) {
                    // Schedule the notification to appear at the exact time
                    long delay = ChronoUnit.MILLIS.between(now, habitTime);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // UI updates must be run on the JavaFX Application Thread
                            Platform.runLater(() -> showNotification(habit));
                        }
                    }, delay);
                }
            } catch (Exception e) {
                // Ignore habits with invalid time formats
            }
        }
    }

    private void showNotification(Habit habit) {
        Notifications.create()
            .title("Wellness Reminder!")
            .text("It's time for your habit: " + habit.getType())
            .showInformation();
    }

    public void stop() {
        scheduler.shutdown();
    }
}