package com.wellness.assistant;

import com.wellness.assistant.services.ReminderService; // Import the service
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private final ReminderService reminderService = new ReminderService(); // Create an instance

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlUrl = getClass().getResource("/com/wellness/assistant/Dashboard.fxml");
        if (fxmlUrl == null) {
            System.err.println("Cannot find FXML file. Check the path!");
            return;
        }
        Parent root = FXMLLoader.load(fxmlUrl);
        
        URL cssUrl = getClass().getResource("/com/wellness/assistant/styles.css");
         if (cssUrl == null) {
            System.err.println("Cannot find CSS file. Check the path!");
            return;
        }
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(cssUrl.toExternalForm());

        primaryStage.setTitle("AI Wellness Assistant");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start scheduling some demo reminders
        startReminders();
    }

    private void startReminders() {
        // For demonstration, schedule reminders with short intervals
        reminderService.scheduleReminder("Drink Water", 15); // Reminder every 15 seconds
        reminderService.scheduleReminder("Eye Rest", 30);   // Reminder every 30 seconds
    }

    @Override
    public void stop() {
        // This is called when you close the window
        reminderService.stopScheduler();
    }

    public static void main(String[] args) {
        launch(args);
    }
}