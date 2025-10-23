package com.wellness.assistant;

import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

    // Period controls
    @FXML private ToggleButton todayBtn, thisWeekBtn, last30Btn, allTimeBtn;
    @FXML private ToggleGroup periodGroup;

    private DatabaseManager dbManager;
    private AIAdvisor aiAdvisor;

    private enum Period { TODAY, THIS_WEEK, LAST_30_DAYS, ALL_TIME }
    private Period selectedPeriod = Period.THIS_WEEK;

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
        
        // Initialize default selected period based on UI
        if (todayBtn != null && todayBtn.isSelected()) selectedPeriod = Period.TODAY;
        else if (thisWeekBtn != null && thisWeekBtn.isSelected()) selectedPeriod = Period.THIS_WEEK;
        else if (last30Btn != null && last30Btn.isSelected()) selectedPeriod = Period.LAST_30_DAYS;
        else selectedPeriod = Period.ALL_TIME;

        loadAnalyticsForPeriod();
        loadRecommendations();
        loadActivityHistoryForPeriod();
    }

    private void loadAnalyticsForPeriod() {
        try {
            LocalDate[] range = getDateRange(selectedPeriod);
            LocalDate start = range[0];
            LocalDate end = range[1];

            int totalActivities = dbManager.getLogsCountBetween(start, end);
            int completed = dbManager.getLogsCountByStatusBetween("Completed", start, end);
            int skipped = dbManager.getLogsCountByStatusBetween("Skipped", start, end);
            double completionRate = dbManager.getCompletionRateBetween(start, end);

            totalActivitiesValue.setText(String.valueOf(totalActivities));
            completionRateValue.setText(String.format("%.0f%%", completionRate));
            completedValue.setText(String.valueOf(completed));
            skippedValue.setText(String.valueOf(skipped));
            
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

    private void loadActivityHistoryForPeriod() {
        try {
            LocalDate[] range = getDateRange(selectedPeriod);
            List<HealthLog> logs = dbManager.getLogsBetween(range[0], range[1]);
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

    @FXML
    private void onPeriodChanged() {
        if (todayBtn.isSelected()) selectedPeriod = Period.TODAY;
        else if (thisWeekBtn.isSelected()) selectedPeriod = Period.THIS_WEEK;
        else if (last30Btn.isSelected()) selectedPeriod = Period.LAST_30_DAYS;
        else selectedPeriod = Period.ALL_TIME;
        syncPeriodButtonStyles();
        loadAnalyticsForPeriod();
        loadActivityHistoryForPeriod();
    }

    private void syncPeriodButtonStyles() {
        ToggleButton[] buttons = {todayBtn, thisWeekBtn, last30Btn, allTimeBtn};
        for (ToggleButton b : buttons) if (b != null) {
            if (b.isSelected()) {
                if (!b.getStyleClass().contains("active")) b.getStyleClass().add("active");
            } else {
                b.getStyleClass().remove("active");
            }
        }
    }

    private LocalDate[] getDateRange(Period p) {
        LocalDate today = LocalDate.now();
        return switch (p) {
            case TODAY -> new LocalDate[]{today, today};
            case THIS_WEEK -> {
                LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
                yield new LocalDate[]{startOfWeek, endOfWeek};
            }
            case LAST_30_DAYS -> new LocalDate[]{today.minusDays(29), today};
            case ALL_TIME -> new LocalDate[]{LocalDate.of(1970,1,1), today};
        };
    }
}
