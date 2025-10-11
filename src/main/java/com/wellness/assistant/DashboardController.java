package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DashboardController {

    @FXML
    private ListView<Habit> habitListView;

    private ObservableList<Habit> habits;

    @FXML
    public void initialize() {
        // Sample data for now. We will replace this with database data.
        habits = FXCollections.observableArrayList(
            new Habit(1, "Drink Water", "Active"),
            new Habit(2, "10-min Stretch", "Completed Today"),
            new Habit(3, "Take Vitamin C", "Active")
        );
        habitListView.setItems(habits);
    }

    @FXML
    private void handleAddHabit() {
        // This will later open a new window to add a habit.
        System.out.println("Add New Habit button clicked!");
        // For now, just add a new sample habit to the list
        habits.add(new Habit(4, "New Test Habit", "Active"));
    }
}