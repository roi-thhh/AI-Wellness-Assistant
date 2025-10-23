PROJECT GOALS: AI Wellness Hub Refactor

This document outlines the refactoring of a JavaFX application. We are moving from a simple TabPane UI to a modern, polished UI with a sidebar, based on a new design.

Core Technologies:

Java 17, JavaFX, FXML, Maven, SQLite

controlsfx (for notifications)

fontawesomefx (for icons)

1. Main Window Structure (MainView.fxml & MainController.java)

The main window MainApp.java must load MainView.fxml.

MainView.fxml is a BorderPane.

The <left> region loads SidebarView.fxml.

The <center> region is a StackPane or BorderPane that will hold the content of the selected page.

MainController.java has a public method loadView(String fxmlName) that loads the correct FXML file into the center region. It must also pass a reference to itself to the SidebarController.

2. Sidebar (SidebarView.fxml & SidebarController.java)

A VBox with the class .sidebar.

Header: "Wellness Hub" title.

Navigation Buttons:

"Dashboard" (Icon: HOME, active by default)

"My Habits" (Icon: HEART)

"Analytics" (Icon: BAR_CHART)

SidebarController.java gets a reference to MainController.

Each button click calls mainController.loadView(...) to load the correct page (e.g., DashboardView.fxml).

It must manage the .sidebar-button-active style class.

3. Dashboard Screen (DashboardView.fxml & DashboardController.java)

Based on image image_6c4f5a.png.

Header: "Welcome Back!" and an "Add New Habit" button.

Summary Cards: A GridPane (2x2) of four .card elements:

"Today's Progress" (e.g., "2/4")

"Active Habits" (e.g., "4")

"Completion Rate" (e.g., "50%")

"Total Logs" (e.g., "2")

Habit List: A VBox below for "Today's Habits," which will list habit cards.

Controller: Must fetch data from DatabaseManager to populate all these stats (e.g., count getAllHabits(), calculate progress from getAllLogs()).

4. "My Habits" Screen (MyHabitsView.fxml & MyHabitsController.java)

Based on image image_6c4ec2.png.

Header: "Manage Your Habits" and "Add New Habit" button.

Filtering: (Optional if time is short) Filter buttons for type and status.

Habit List: A VBox showing all habits from DatabaseManager.getAllHabits().

Habit Card Item: Each item in the list should be a custom card (like in image_6c4ef8.png) with an icon, name, time, "Done" button, and "Edit" button.

Controller: The "Add New Habit" button must open the AddHabitDialog.fxml as a popup. The "Done" button on a card should log the activity (call DatabaseManager.logActivity(...)).

5. "Analytics" Screen (AnalyticsView.fxml & AnalyticsController.java)

Based on images image_6c4e9f.png and image_6c4e60.png.

Header: "Your Wellness Analytics."

Summary Cards: GridPane of stat cards (e.g., "Total Activities," "Completion Rate").

AI Section: "AI-Powered Recommendations" section. This is a ListView that will show the list of strings from the AIAdvisor.

History Section: "Activity History" section. This is a ListView that shows all logs from DatabaseManager.getAllLogs().

Controller: Must fetch data for stats, call AIAdvisor.generateRecommendations() for the AI list, and call DatabaseManager.getAllLogs() for the history list.

6. CSS (styles.css)

Must be completely rewritten to match the demo app.

Must include styles for:

.sidebar (white, shadow)

.sidebar-button (gray text, transparent background)

.sidebar-button-active (gradient green/blue background, white text)

.card (white, rounded, shadow)

.primary-button (gradient green)

Color-coded habit icons (e.g., .habit-item-water)