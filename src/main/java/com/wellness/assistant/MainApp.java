package com.wellness.assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private ReminderService reminderService;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Start the background reminder service
        reminderService = new ReminderService();
        reminderService.start();

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
    }

    @Override
    public void stop() {
        // Properly shut down the reminder service when the app is closed
        System.out.println("Shutting down reminder service...");
        reminderService.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}