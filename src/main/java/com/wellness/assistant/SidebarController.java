package com.wellness.assistant;

import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the sidebar navigation.
 */
public class SidebarController implements Initializable {
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

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set default active button (Dashboard)
        setActiveButton(dashboardButton);
        
        // Set user information
        userTitleLabel.setText("Health Champion");
        userMessageLabel.setText("Keep up the great work!");
        
        // Initialize progress
        updateTodayProgress();
        
        // Try to get the main controller reference
        // This is a workaround since we can't easily get it from FXML includes
        // We'll set it up in the MainController later
    }

    @FXML
    private void handleDashboardClick() {
        if (mainController != null) {
            mainController.loadView("DashboardView.fxml");
            setActiveButton(dashboardButton);
        }
    }

    @FXML
    private void handleMyHabitsClick() {
        if (mainController != null) {
            mainController.loadView("MyHabitsView.fxml");
            setActiveButton(myHabitsButton);
        }
    }

    @FXML
    private void handleAnalyticsClick() {
        if (mainController != null) {
            mainController.loadView("AnalyticsView.fxml");
            setActiveButton(analyticsButton);
        }
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
     * Updates the today's progress display.
     */
    public void updateTodayProgress() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
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

    /**
     * Sets the main controller reference.
     * @param mainController The main controller instance
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
