package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField; // Import TextField
import javafx.stage.Stage;

public class AddHabitController {

    @FXML
    private ComboBox<String> habitTypeComboBox;

    @FXML
    private TextField timeTextField; // Link to the FXML TextField

    @FXML
    private void initialize() {
        habitTypeComboBox.getItems().addAll(
            "Drink Water", "Exercise", "Medicine", "Eye Rest", "Sleep", "Mood Tracking"
        );
    }

    @FXML
    private void handleSaveHabit() {
        String selectedType = habitTypeComboBox.getValue();
        String time = timeTextField.getText(); // Get text from the time field

        // Check that both fields have values
        if (selectedType != null && !selectedType.isEmpty() && time != null && !time.isEmpty()) {
            // Create the new Habit object with the time included
            Habit newHabit = new Habit(0, selectedType, "Active", time);
            DatabaseManager.getInstance().addHabit(newHabit);

            Stage stage = (Stage) habitTypeComboBox.getScene().getWindow();
            stage.close();
        } else {
            System.err.println("Habit type and time must be specified.");
        }
    }
}