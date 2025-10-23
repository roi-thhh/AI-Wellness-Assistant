package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the dashboard view.
 */
public class DashboardController implements Initializable {
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label dateLabel;
    
    @FXML
    private Button addHabitButton;
    
    @FXML
    private Label todaysProgressValue;
    
    @FXML
    private Label activeHabitsValue;
    
    @FXML
    private Label completionRateValue;
    
    @FXML
    private Label totalLogsValue;
    
    @FXML
    private VBox habitsListContainer;
    
    @FXML
    private Label dailyQuoteLabel;

    private DatabaseManager dbManager;
    private AIAdvisor aiAdvisor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();
        initializeDashboard();
    }

    public void initialize() {
        initializeDashboard();
    }

    private void initializeDashboard() {
        dbManager = DatabaseManager.getInstance();
        aiAdvisor = new AIAdvisor();
        
        // Set welcome message and date
        welcomeLabel.setText("Welcome Back!");
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        
        // Load summary statistics
        loadSummaryStats();
        
        // Load today's habits
        loadTodaysHabits();
        
        // Load daily quote
        loadDailyQuote();
    }

    private void loadSummaryStats() {
        try {
            // Today's Progress
            int completedToday = dbManager.getTodaysCompletedActivities();
            int totalActiveHabits = dbManager.getTotalActiveHabits();
            todaysProgressValue.setText(completedToday + "/" + totalActiveHabits);
            
            // Active Habits
            activeHabitsValue.setText(String.valueOf(totalActiveHabits));
            
            // Completion Rate
            double completionRate = dbManager.getCompletionRate();
            completionRateValue.setText(String.format("%.0f%%", completionRate));
            
            // Total Logs Today
            totalLogsValue.setText(String.valueOf(completedToday));
            
        } catch (Exception e) {
            System.err.println("Error loading summary stats: " + e.getMessage());
            // Set default values
            todaysProgressValue.setText("0/0");
            activeHabitsValue.setText("0");
            completionRateValue.setText("0%");
            totalLogsValue.setText("0");
        }
    }

    private void loadTodaysHabits() {
        try {
            List<Habit> habits = dbManager.getAllHabits();
            habitsListContainer.getChildren().clear();
            
            for (Habit habit : habits) {
                if (habit.isActive()) {
                    // Create habit card UI element
                    createHabitCard(habit);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading today's habits: " + e.getMessage());
        }
    }

    private void createHabitCard(Habit habit) {
        // Create a proper habit card with icon and styling
        HBox habitCard = new HBox();
        habitCard.getStyleClass().add("habit-card");
        habitCard.setSpacing(15);
        habitCard.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));
        
        // Create icon based on habit type
        Rectangle iconRect = new Rectangle(40, 40);
        iconRect.setArcWidth(8);
        iconRect.setArcHeight(8);
        
        // Set icon color based on habit type
        String iconColor = getIconColorForHabitType(habit.getType());
        iconRect.setFill(javafx.scene.paint.Paint.valueOf(iconColor));
        
        // Create icon content (simplified - in real app you'd use proper icons)
        Label iconLabel = new Label(getIconTextForHabitType(habit.getType()));
        iconLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        iconLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        StackPane iconPane = new StackPane(iconRect, iconLabel);
        
        // Create habit details
        VBox detailsBox = new VBox();
        detailsBox.setSpacing(5);
        
        Label habitNameLabel = new Label(habit.getName());
        habitNameLabel.getStyleClass().add("habit-name");
        
        HBox timeBox = new HBox();
        timeBox.setSpacing(10);
        
        Label timeLabel = new Label(habit.getTime().toString());
        timeLabel.getStyleClass().add("habit-time");
        
        Label frequencyLabel = new Label(habit.getFrequency());
        frequencyLabel.getStyleClass().add("habit-frequency");
        
        timeBox.getChildren().addAll(timeLabel, frequencyLabel);
        detailsBox.getChildren().addAll(habitNameLabel, timeBox);
        
        // Create action buttons
        HBox actionBox = new HBox();
        actionBox.setSpacing(10);
        
        Button doneButton = new Button("Done");
        doneButton.getStyleClass().add("primary-button");
        doneButton.setOnAction(e -> markHabitDone(habit));
        
        Button editButton = new Button();
        editButton.getStyleClass().add("edit-button");
        editButton.setText("✏");
        editButton.setOnAction(e -> editHabit(habit));
        
        actionBox.getChildren().addAll(doneButton, editButton);
        
        // Add all elements to the habit card
        habitCard.getChildren().addAll(iconPane, detailsBox, actionBox);
        
        // Add to container
        habitsListContainer.getChildren().add(habitCard);
    }
    
    private String getIconColorForHabitType(String type) {
        return switch (type) {
            case "water" -> "#2196F3"; // Blue
            case "exercise" -> "#FF9800"; // Orange
            case "medicine" -> "#F44336"; // Red
            case "eye_rest" -> "#9C27B0"; // Purple
            default -> "#4CAF50"; // Green
        };
    }
    
    private String getIconTextForHabitType(String type) {
        return switch (type) {
            case "water" -> "💧";
            case "exercise" -> "🏃";
            case "medicine" -> "💊";
            case "eye_rest" -> "👁";
            default -> "⭐";
        };
    }
    
    private void markHabitDone(Habit habit) {
        try {
            HealthLog log = new HealthLog(habit.getId(), habit.getName(), "Completed");
            dbManager.logActivity(log);
            System.out.println("Marked habit as done: " + habit.getName());
            // Refresh the dashboard
            loadTodaysHabits();
            // Update progress in main controller
            if (MainController.class.getDeclaredMethod("updateTodayProgress") != null) {
                // This would update the progress in the main controller
            }
        } catch (Exception e) {
            System.err.println("Error marking habit as done: " + e.getMessage());
        }
    }
    
    private void editHabit(Habit habit) {
        // Open edit habit dialog
        System.out.println("Edit habit: " + habit.getName());
    }
    
    private void loadDailyQuote() {
        try {
            String quote = aiAdvisor.getDailyQuote();
            dailyQuoteLabel.setText(quote);
        } catch (Exception e) {
            System.err.println("Error loading daily quote: " + e.getMessage());
            dailyQuoteLabel.setText("Health is not valued until sickness comes.");
        }
    }

    @FXML
    private void handleAddHabitClick() {
        try {
            // Create and show the add habit dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/AddHabitDialog.fxml"));
            javafx.scene.Scene dialogScene = new javafx.scene.Scene(loader.load());
            
            AddHabitController controller = loader.getController();
            
            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Add New Habit");
            dialogStage.setScene(dialogScene);
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
            // Refresh the dashboard if a habit was added
            if (controller.isOkClicked()) {
                loadTodaysHabits();
                System.out.println("Habit added successfully!");
            }
            
        } catch (Exception e) {
            System.err.println("Error opening add habit dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
}