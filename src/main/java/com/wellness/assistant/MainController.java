package com.wellness.assistant;

import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Main controller for the Wellness Hub application.
 */
public class MainController {
    @FXML
    private StackPane contentArea;
    
    @FXML
    private Button dashboardButton;
    
    @FXML
    private Button myHabitsButton;
    
    @FXML
    private Button analyticsButton;
    
    @FXML
    private Label todayProgressLabel;
    
    @FXML
    private ProgressBar todayProgressBar;
    
    @FXML
    private Label userTitleLabel;
    
    @FXML
    private Label userMessageLabel;

    private DatabaseManager dbManager;

    @FXML
    private void initialize() {
        dbManager = DatabaseManager.getInstance();
        
        // Set user information
        userTitleLabel.setText("Health Champion");
        userMessageLabel.setText("Keep up the great work!");
        
        // Initialize progress
        updateTodayProgress();
        
        // Load the default dashboard view
        loadView("DashboardView.fxml");
    }
    
    /**
     * Updates the today's progress display.
     */
    public void updateTodayProgress() {
        try {
            int completedToday = dbManager.getTodaysCompletedActivities();
            int totalActiveHabits = dbManager.getTotalActiveHabits();
            
            if (totalActiveHabits > 0) {
                double progress = (double) completedToday / totalActiveHabits;
                todayProgressBar.setProgress(progress);
                todayProgressLabel.setText(completedToday + "/" + totalActiveHabits);
            } else {
                todayProgressBar.setProgress(0);
                todayProgressLabel.setText("--");
            }
        } catch (Exception e) {
            System.err.println("Error updating today's progress: " + e.getMessage());
            todayProgressBar.setProgress(0);
            todayProgressLabel.setText("--");
        }
    }
    
    @FXML
    private void handleDashboardClick() {
        loadView("DashboardView.fxml");
        setActiveButton(dashboardButton);
    }

    @FXML
    private void handleMyHabitsClick() {
        loadView("MyHabitsView.fxml");
        setActiveButton(myHabitsButton);
    }

    @FXML
    private void handleAnalyticsClick() {
        loadView("AnalyticsView.fxml");
        setActiveButton(analyticsButton);
    }

    /**
     * Sets the active button style and removes it from other buttons.
     * @param activeButton The button to make active
     */
    private void setActiveButton(Button activeButton) {
        // Remove active style from all buttons
        dashboardButton.getStyleClass().remove("sidebar-button-active");
        myHabitsButton.getStyleClass().remove("sidebar-button-active");
        analyticsButton.getStyleClass().remove("sidebar-button-active");
        
        // Add active style to the selected button
        activeButton.getStyleClass().add("sidebar-button-active");
    }

    /**
     * Loads a view into the content area.
     * @param fxmlFile The FXML file to load
     */
    public void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/" + fxmlFile));
            Node view = loader.load();
            
            // Clear existing content and add new view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
            
            // If loading dashboard, initialize it
            if (fxmlFile.equals("DashboardView.fxml")) {
                DashboardController dashboardController = loader.getController();
                dashboardController.initialize();
            }
            // If loading my habits, initialize it
            else if (fxmlFile.equals("MyHabitsView.fxml")) {
                MyHabitsController habitsController = loader.getController();
                habitsController.initialize();
            }
            // If loading analytics, initialize it
            else if (fxmlFile.equals("AnalyticsView.fxml")) {
                AnalyticsController analyticsController = loader.getController();
                analyticsController.initialize();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

}
