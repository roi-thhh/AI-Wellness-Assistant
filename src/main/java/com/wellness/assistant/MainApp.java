package com.wellness.assistant;

import com.wellness.assistant.storage.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class for the Wellness Hub.
 */
public class MainApp extends Application {
    private static ReminderService reminderService;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("[MainApp] Starting Wellness Hub application...");
        try {
            System.out.println("[MainApp] Loading MainView.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/MainView.fxml"));
            Scene scene = new Scene(loader.load());
            System.out.println("[MainApp] MainView.fxml loaded successfully.");

            // Load CSS styles
            try {
                scene.getStylesheets().add(getClass().getResource("/com/wellness/assistant/styles.css").toExternalForm());
                System.out.println("[MainApp] styles.css loaded.");
            } catch (Exception cssEx) {
                System.err.println("[MainApp] Error loading styles.css: " + cssEx.getMessage());
            }

            // Set up the stage
            primaryStage.setTitle("Wellness Hub");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);

            // Set application icon (if available)
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/wellness/assistant/icon.png")));
                System.out.println("[MainApp] Application icon loaded.");
            } catch (Exception e) {
                System.out.println("[MainApp] Application icon not found, continuing without icon");
            }

            primaryStage.show();
            primaryStage.toFront();
            System.out.println("[MainApp] Main window shown.");

            // Start the reminder service
            try {
                reminderService = new ReminderService();
                reminderService.start();
                System.out.println("[MainApp] ReminderService started.");
            } catch (Exception rsEx) {
                System.err.println("[MainApp] Error starting ReminderService: " + rsEx.getMessage());
            }

        } catch (Exception e) {
            System.err.println("[MainApp] Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Stop the reminder service when application closes
        if (reminderService != null) {
            reminderService.stop();
        }
        
        // Close database connection
        try {
            DatabaseManager.getInstance().close();
        } catch (Exception e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}