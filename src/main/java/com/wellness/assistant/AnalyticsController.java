package com.wellness.assistant;

import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Analytics view.
 */
public class AnalyticsController implements Initializable {
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label subtitleLabel;
    
    @FXML
    private Label totalActivitiesValue;
    
    @FXML
    private Label completionRateValue;
    
    @FXML
    private Label completedValue;
    
    @FXML
    private Label skippedValue;
    
    @FXML
    private VBox recommendationsContainer;
    
    @FXML
    private VBox activityHistoryContainer;

    private DatabaseManager dbManager;
    private AIAdvisor aiAdvisor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();
        aiAdvisor = new AIAdvisor();
        initializeView();
    }

    public void initialize() {
        initializeView();
    }

    private void initializeView() {
        titleLabel.setText("Your Wellness Analytics");
        subtitleLabel.setText("Track your progress and get AI-powered insights");
        
        loadAnalytics();
        loadRecommendations();
        loadActivityHistory();
    }

    private void loadAnalytics() {
        try {
            // Load summary statistics
            int totalActiveHabits = dbManager.getTotalActiveHabits();
            int completedToday = dbManager.getTodaysCompletedActivities();
            double completionRate = dbManager.getCompletionRate();
            
            // Get all logs for additional statistics
            List<HealthLog> allLogs = dbManager.getAllLogs();
            long totalCompleted = allLogs.stream()
                    .filter(log -> "Completed".equals(log.getStatus()))
                    .count();
            long totalSkipped = allLogs.stream()
                    .filter(log -> "Skipped".equals(log.getStatus()))
                    .count();
            
            // Update UI elements
            totalActivitiesValue.setText(String.valueOf(totalActiveHabits));
            completionRateValue.setText(String.format("%.0f%%", completionRate));
            completedValue.setText(String.valueOf(totalCompleted));
            skippedValue.setText(String.valueOf(totalSkipped));
            
        } catch (Exception e) {
            System.err.println("Error loading analytics: " + e.getMessage());
            // Set default values
            totalActivitiesValue.setText("0");
            completionRateValue.setText("0%");
            completedValue.setText("0");
            skippedValue.setText("0");
        }
    }

    private void loadRecommendations() {
        try {
            List<String> recommendations = aiAdvisor.generateRecommendations();
            recommendationsContainer.getChildren().clear();
            
            if (recommendations.isEmpty()) {
                // Add default recommendations
                addRecommendationCard("🌟", "Welcome to Wellness Hub! Start by completing your first habit to get personalized recommendations.");
                addRecommendationCard("💡", "Consistency is key to building lasting wellness habits. Try to complete at least one habit daily!");
            } else {
                for (String recommendation : recommendations) {
                    String icon = getRecommendationIcon(recommendation);
                    addRecommendationCard(icon, recommendation);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading recommendations: " + e.getMessage());
            
            // Add default recommendation
            addRecommendationCard("🌟", "Keep up the great work with your wellness journey!");
        }
    }
    
    private void addRecommendationCard(String icon, String text) {
        HBox recCard = new HBox();
        recCard.getStyleClass().add("recommendation-card");
        recCard.setSpacing(15);
        recCard.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        Label textLabel = new Label(text);
        textLabel.setWrapText(true);
        textLabel.getStyleClass().add("recommendation-text");
        
        recCard.getChildren().addAll(iconLabel, textLabel);
        recommendationsContainer.getChildren().add(recCard);
    }
    
    private String getRecommendationIcon(String recommendation) {
        if (recommendation.toLowerCase().contains("water")) return "💧";
        if (recommendation.toLowerCase().contains("exercise")) return "🏃";
        if (recommendation.toLowerCase().contains("fire") || recommendation.toLowerCase().contains("on fire")) return "🔥";
        if (recommendation.toLowerCase().contains("streak")) return "🔥";
        if (recommendation.toLowerCase().contains("consistent")) return "⭐";
        if (recommendation.toLowerCase().contains("momentum")) return "🚀";
        return "💡";
    }

    private void loadActivityHistory() {
        try {
            List<HealthLog> logs = dbManager.getAllLogs();
            activityHistoryContainer.getChildren().clear();
            
            // Limit to last 10 entries for display
            int displayLimit = Math.min(10, logs.size());
            for (int i = 0; i < displayLimit; i++) {
                HealthLog log = logs.get(i);
                createActivityHistoryEntry(log);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading activity history: " + e.getMessage());
        }
    }

    private void createActivityHistoryEntry(HealthLog log) {
        // Create activity history entry UI elements
        String timestamp = log.getTimestamp().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' h:mm a"));
        String entryText = log.getHabitName() + " - " + timestamp;
        
        Label entryLabel = new Label(entryText);
        entryLabel.getStyleClass().add("activity-history-entry");
        
        // Add status indicator
        Label statusLabel = new Label(log.getStatus());
        statusLabel.getStyleClass().add("status-" + log.getStatus().toLowerCase());
        
        activityHistoryContainer.getChildren().add(entryLabel);
    }
}
