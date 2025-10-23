package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

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
    private MainController mainController;

    // Filters
    @FXML private ToggleButton typeAllBtn, typeWaterBtn, typeExerciseBtn, typeMedicineBtn, typeEyeRestBtn;
    @FXML private ToggleButton statusAllBtn, statusActiveBtn, statusPausedBtn;
    @FXML private ToggleGroup typeGroup, statusGroup;

    private String selectedType = "All";   // All | water | exercise | medicine | eye_rest
    private String selectedStatus = "All"; // All | Active | Paused

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();
        habitsList = FXCollections.observableArrayList();
        initializeView();
    }

    public void initialize() {
        initializeView();
    }

    /** Inject MainController for refreshing quick stats after actions. */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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
        // Create a polished habit card similar to dashboard cards
        HBox habitCard = new HBox();
        habitCard.getStyleClass().add("habit-card");
        habitCard.setSpacing(15);
        habitCard.setPadding(new javafx.geometry.Insets(15, 20, 15, 20));

        // Icon rectangle with color per type
        Rectangle iconRect = new Rectangle(40, 40);
        iconRect.setArcWidth(8);
        iconRect.setArcHeight(8);
        iconRect.setFill(javafx.scene.paint.Paint.valueOf(getIconColorForHabitType(habit.getType())));

        Label iconLabel = new Label(getIconTextForHabitType(habit.getType()));
        iconLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        iconLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        StackPane iconPane = new StackPane(iconRect, iconLabel);

        // Details
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
        Label statusLabel = new Label(habit.isActive() ? "Active" : "Paused");
        statusLabel.getStyleClass().add("habit-status");
        if (habit.isActive()) statusLabel.getStyleClass().add("active"); else statusLabel.getStyleClass().add("paused");
        timeBox.getChildren().addAll(timeLabel, frequencyLabel, statusLabel);
        detailsBox.getChildren().addAll(habitNameLabel, timeBox);

        // Actions
        HBox actionBox = new HBox();
        actionBox.setSpacing(10);
        Button doneButton = new Button("Done");
        doneButton.getStyleClass().add("primary-button");
        doneButton.setOnAction(e -> markHabitDone(habit));
        Button editButton = new Button();
        editButton.getStyleClass().add("edit-button");
        editButton.setText("✏");
        editButton.setOnAction(e -> editHabit(habit));
        Button pauseResumeButton = new Button(habit.isActive() ? "Pause" : "Resume");
        pauseResumeButton.getStyleClass().add(habit.isActive() ? "pause-button" : "resume-button");
        pauseResumeButton.setOnAction(e -> toggleHabitActive(habit));
        actionBox.getChildren().addAll(doneButton, editButton, pauseResumeButton);

        habitCard.getChildren().addAll(iconPane, detailsBox, actionBox);
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

    private void toggleHabitActive(Habit habit) {
        try {
            habit.setActive(!habit.isActive());
            dbManager.updateHabit(habit);
            loadHabits();
            if (mainController != null) mainController.refreshQuickStats();
        } catch (Exception ex) {
            System.err.println("Error toggling habit status: " + ex.getMessage());
        }
    }

    private void markHabitDone(Habit habit) {
        try {
            // Create health log entry
            HealthLog log = new HealthLog(habit.getId(), habit.getName(), "Completed");
            dbManager.logActivity(log);
            
            System.out.println("Marked habit as done: " + habit.getName());
            // Refresh the view
            loadHabits();
            if (mainController != null) {
                mainController.refreshQuickStats();
            }
            
        } catch (Exception e) {
            System.err.println("Error marking habit as done: " + e.getMessage());
        }
    }

    private void editHabit(Habit habit) {
        // Open edit habit dialog using the AddHabitDialog in edit mode
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/AddHabitDialog.fxml"));
            javafx.scene.Scene dialogScene = new javafx.scene.Scene(loader.load());

            AddHabitController controller = loader.getController();
            controller.setHabit(habit); // edit mode

            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Edit Habit");
            dialogStage.setScene(dialogScene);
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);

            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                loadHabits();
                if (mainController != null) mainController.refreshQuickStats();
            }
        } catch (Exception e) {
            System.err.println("Error opening edit habit dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupFilters() {
        // Ensure toggle groups work exclusively
        if (typeGroup != null && typeAllBtn != null) {
            typeAllBtn.setSelected(true);
        }
        if (statusGroup != null && statusAllBtn != null) {
            statusAllBtn.setSelected(true);
        }

        refreshFilterStyles();
    }

    @FXML
    private void onTypeFilterChanged() {
        if (typeAllBtn.isSelected()) selectedType = "All";
        else if (typeWaterBtn.isSelected()) selectedType = "water";
        else if (typeExerciseBtn.isSelected()) selectedType = "exercise";
        else if (typeMedicineBtn.isSelected()) selectedType = "medicine";
        else if (typeEyeRestBtn.isSelected()) selectedType = "eye_rest";
        applyFilters();
    }

    @FXML
    private void onStatusFilterChanged() {
        if (statusAllBtn.isSelected()) selectedStatus = "All";
        else if (statusActiveBtn.isSelected()) selectedStatus = "Active";
        else if (statusPausedBtn.isSelected()) selectedStatus = "Paused";
        applyFilters();
    }

    private void applyFilters() {
        try {
            List<Habit> all = dbManager.getAllHabits();
            habitsList.clear();
            for (Habit h : all) {
                if (!matchesType(h)) continue;
                if (!matchesStatus(h)) continue;
                habitsList.add(h);
            }
            habitsCountLabel.setText(habitsList.size() + " Habits");
            displayHabits();
            refreshFilterStyles();
        } catch (Exception e) {
            System.err.println("Error applying filters: " + e.getMessage());
        }
    }

    private boolean matchesType(Habit h) {
        if ("All".equals(selectedType)) return true;
        return selectedType.equalsIgnoreCase(h.getType());
    }

    private boolean matchesStatus(Habit h) {
        if ("All".equals(selectedStatus)) return true;
        if ("Active".equals(selectedStatus)) return h.isActive();
        if ("Paused".equals(selectedStatus)) return !h.isActive();
        return true;
    }

    private void refreshFilterStyles() {
        // Toggle 'active' class according to selection to match CSS
        ToggleButton[] typeBtns = {typeAllBtn, typeWaterBtn, typeExerciseBtn, typeMedicineBtn, typeEyeRestBtn};
        for (ToggleButton b : typeBtns) if (b != null) {
            if (b.isSelected()) {
                if (!b.getStyleClass().contains("active")) b.getStyleClass().add("active");
            } else {
                b.getStyleClass().remove("active");
            }
        }
        ToggleButton[] statusBtns = {statusAllBtn, statusActiveBtn, statusPausedBtn};
        for (ToggleButton b : statusBtns) if (b != null) {
            if (b.isSelected()) {
                if (!b.getStyleClass().contains("active")) b.getStyleClass().add("active");
            } else {
                b.getStyleClass().remove("active");
            }
        }
    }

    @FXML
    private void handleAddHabitClick() {
        // Open add habit dialog similar to dashboard
        try {
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

            if (controller.isOkClicked()) {
                loadHabits();
            }
        } catch (Exception e) {
            System.err.println("Error opening add habit dialog: " + e.getMessage());
            e.printStackTrace();
        }
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
