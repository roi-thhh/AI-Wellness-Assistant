package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private ListView<Habit> habitListView;

    private final ObservableList<Habit> habits = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        habitListView.setItems(habits);
        refreshHabitList();
    }

    private void refreshHabitList() {
        habits.clear();
        habits.addAll(DatabaseManager.getInstance().getAllHabits());
    }

    @FXML
    private void handleAddHabit() {
        try {
            // Get the URL for the FXML file
            java.net.URL dialogUrl = getClass().getResource("/com/wellness/assistant/AddHabitDialog.fxml");

            // IMPORTANT: Check if the file was actually found
            if (dialogUrl == null) {
                System.err.println("ERROR: Could not find 'AddHabitDialog.fxml'. Please check that the file is in the correct folder.");
                return; // Stop here if the file is missing
            }

            FXMLLoader fxmlLoader = new FXMLLoader(dialogUrl);
            Parent parent = fxmlLoader.load();

            // Create a new stage (window) for the dialog
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with main window
            dialogStage.setTitle("Add New Habit");
            
            Scene scene = new Scene(parent);
            // Re-use the same stylesheet
            java.net.URL cssUrl = getClass().getResource("/com/wellness/assistant/styles.css");
            if(cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            
            dialogStage.setScene(scene);

            // Show the dialog and wait for it to be closed
            dialogStage.showAndWait();

            // After the dialog is closed, refresh the list to show the new habit
            refreshHabitList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}