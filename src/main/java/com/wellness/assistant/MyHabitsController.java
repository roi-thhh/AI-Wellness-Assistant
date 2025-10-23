package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the My Habits view.
 */
public class MyHabitsController implements Initializable {
    @FXML
    private Label titleLabel;
    
    @FXML
    private Button addHabitButton;
    
    @FXML
    private VBox filterContainer;
    
    @FXML
    private Label habitsCountLabel;
    
    @FXML
    private VBox habitsListContainer;

    private DatabaseManager dbManager;
    private ObservableList<Habit> habitsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();
        habitsList = FXCollections.observableArrayList();
        initializeView();
    }

    public void initialize() {
        initializeView();
    }

    private void initializeView() {
        titleLabel.setText("Manage Your Habits");
        loadHabits();
        setupFilters();
    }

    private void loadHabits() {
        try {
            List<Habit> habits = dbManager.getAllHabits();
            habitsList.clear();
            habitsList.addAll(habits);
            
            habitsCountLabel.setText(habits.size() + " Habits");
            displayHabits();
            
        } catch (Exception e) {
            System.err.println("Error loading habits: " + e.getMessage());
        }
    }

    private void displayHabits() {
        habitsListContainer.getChildren().clear();
        
        for (Habit habit : habitsList) {
            createHabitCard(habit);
        }
    }

    private void createHabitCard(Habit habit) {
        // Create habit card UI elements
        // This is a simplified version - in a real implementation, you'd create custom components
        
        Label habitLabel = new Label(habit.getName() + " - " + habit.getType() + " - " + habit.getTime());
        habitLabel.getStyleClass().add("habit-card");
        
        Button doneButton = new Button("Done");
        doneButton.setOnAction(e -> markHabitDone(habit));
        
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> editHabit(habit));
        
        // Add to container
        habitsListContainer.getChildren().add(habitLabel);
    }

    private void markHabitDone(Habit habit) {
        try {
            // Create health log entry
            HealthLog log = new HealthLog(habit.getId(), habit.getName(), "Completed");
            dbManager.logActivity(log);
            
            System.out.println("Marked habit as done: " + habit.getName());
            // Refresh the view
            loadHabits();
            
        } catch (Exception e) {
            System.err.println("Error marking habit as done: " + e.getMessage());
        }
    }

    private void editHabit(Habit habit) {
        // Open edit habit dialog
        System.out.println("Edit habit: " + habit.getName());
    }

    private void setupFilters() {
        // Setup filter buttons and functionality
        // This would be implemented to filter habits by type and status
    }

    @FXML
    private void handleAddHabitClick() {
        // Open add habit dialog
        System.out.println("Add habit button clicked");
    }

    @FXML
    private void handleFilterByType(String type) {
        // Filter habits by type
        if ("All".equals(type)) {
            displayHabits();
        } else {
            // Filter logic would go here
        }
    }

    @FXML
    private void handleFilterByStatus(String status) {
        // Filter habits by status
        if ("All".equals(status)) {
            displayHabits();
        } else {
            // Filter logic would go here
        }
    }
}
