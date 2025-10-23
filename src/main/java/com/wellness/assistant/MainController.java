package com.wellness.assistant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Main controller for the Wellness Hub application.
 * - Loads SidebarView.fxml into the left region and passes a reference of this controller to SidebarController
 * - Hosts the center content and exposes loadView(String fxmlName)
 */
public class MainController {
    @FXML
    private StackPane contentArea;

    @FXML
    private VBox sidebarContainer;

    private SidebarController sidebarController;

    @FXML
    private void initialize() {
        // Load sidebar and set up controller linkage
        loadSidebar();
        // Load the default dashboard view
        loadView("DashboardView.fxml");
    }

    private void loadSidebar() {
        try {
            FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/SidebarView.fxml"));
            Node sidebar = sidebarLoader.load();
            this.sidebarController = sidebarLoader.getController();
            // Pass reference of MainController to SidebarController
            this.sidebarController.setMainController(this);
            // Place sidebar into container
            sidebarContainer.getChildren().clear();
            sidebarContainer.getChildren().add(sidebar);
        } catch (IOException e) {
            System.err.println("Error loading sidebar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads a view into the content area.
     * @param fxmlFile The FXML file to load (e.g., "DashboardView.fxml")
     */
    public void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wellness/assistant/" + fxmlFile));
            Node view = loader.load();

            // Clear existing content and add new view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);

            // Optionally call initialize on loaded controllers to refresh data
            switch (fxmlFile) {
                case "DashboardView.fxml" -> {
                    DashboardController controller = loader.getController();
                    controller.setMainController(this);
                    controller.initialize();
                }
                case "MyHabitsView.fxml" -> {
                    MyHabitsController controller = loader.getController();
                    controller.setMainController(this);
                    controller.initialize();
                }
                case "AnalyticsView.fxml" -> {
                    AnalyticsController controller = loader.getController();
                    // Currently no use, but keep for parity
                    try { controller.getClass().getMethod("setMainController", MainController.class);
                        // only call if method exists in future
                    } catch (NoSuchMethodException ignored) {}
                    controller.initialize();
                }
                default -> {
                    // no-op
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Refresh the quick stats shown in the sidebar (e.g., today's progress).
     */
    public void refreshQuickStats() {
        if (sidebarController != null) {
            sidebarController.updateTodayProgress();
        }
    }
}
