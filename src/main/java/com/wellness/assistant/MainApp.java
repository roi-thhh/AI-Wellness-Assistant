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
        try {
            // Load the main view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/MainView.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Load CSS styles
            scene.getStylesheets().add(getClass().getResource("/com/wellness/assistant/styles.css").toExternalForm());
            
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
            } catch (Exception e) {
                // Icon not found, continue without it
                System.out.println("Application icon not found, continuing without icon");
            }
            
            primaryStage.show();
            
            // Start the reminder service
            reminderService = new ReminderService();
            reminderService.start();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
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