package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Controller for the Add Habit dialog.
 */
public class AddHabitController implements Initializable {
    @FXML
    private TextField habitNameField;
    
    @FXML
    private ComboBox<String> habitTypeComboBox;
    
    @FXML
    private TextField timeField;
    
    @FXML
    private ComboBox<String> frequencyComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;

    private DatabaseManager dbManager;
    private Stage dialogStage;
    private boolean okClicked = false;
    private boolean editMode = false;
    private Habit editingHabit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        // Setup habit type options
        habitTypeComboBox.getItems().addAll(
            "water",
            "exercise", 
            "medicine",
            "eye_rest",
            "nutrition",
            "sleep",
            "mindfulness"
        );
        habitTypeComboBox.setValue("water");
        
        // Setup frequency options
        frequencyComboBox.getItems().addAll(
            "daily",
            "weekdays",
            "weekends",
            "weekly"
        );
        frequencyComboBox.setValue("daily");
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            try {
                if (editMode && editingHabit != null) {
                    editingHabit.setName(habitNameField.getText());
                    editingHabit.setType(habitTypeComboBox.getValue());
                    editingHabit.setTime(LocalTime.parse(timeField.getText()));
                    editingHabit.setFrequency(frequencyComboBox.getValue());
                    editingHabit.setActive(true);
                    dbManager.updateHabit(editingHabit);
                } else {
                    Habit habit = new Habit();
                    habit.setName(habitNameField.getText());
                    habit.setType(habitTypeComboBox.getValue());
                    habit.setTime(LocalTime.parse(timeField.getText()));
                    habit.setFrequency(frequencyComboBox.getValue());
                    habit.setActive(true);
                    dbManager.addHabit(habit);
                }
                okClicked = true;
                dialogStage.close();
                
            } catch (Exception e) {
                System.err.println("Error saving habit: " + e.getMessage());
                showErrorDialog("Error saving habit", e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        
        if (habitNameField.getText() == null || habitNameField.getText().trim().isEmpty()) {
            errorMessage += "Habit name is required!\n";
        }
        
        if (timeField.getText() == null || timeField.getText().trim().isEmpty()) {
            errorMessage += "Time is required!\n";
        } else {
            try {
                LocalTime.parse(timeField.getText());
            } catch (Exception e) {
                errorMessage += "Time must be in HH:MM format!\n";
            }
        }
        
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showErrorDialog("Invalid Input", errorMessage);
            return false;
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the stage of this dialog.
     * @param dialogStage The dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return True if OK was clicked
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Configure the dialog to edit an existing habit and prefill fields.
     */
    public void setHabit(Habit habit) {
        if (habit == null) return;
        this.editMode = true;
        this.editingHabit = habit;
        habitNameField.setText(habit.getName());
        habitTypeComboBox.setValue(habit.getType());
        timeField.setText(habit.getTime() != null ? habit.getTime().toString() : "");
        frequencyComboBox.setValue(habit.getFrequency());
    }
}