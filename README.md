# Wellness Hub - AI-Powered Health Reminder and Wellness Assistant

A modern desktop application built with JavaFX 17, designed to help users track and manage their wellness habits with AI-powered recommendations.

## Features

- **Dashboard View**: Overview of daily progress, active habits, and completion rates
- **My Habits Management**: Create, edit, and organize health routines with filtering options
- **Analytics & Insights**: AI-powered recommendations based on activity patterns
- **Smart Reminders**: Background service with notification system for habit reminders
- **Modern UI**: Clean, responsive design with gradient colors and modern styling

## Technology Stack

- **Java 17** - Programming language
- **JavaFX 17.0.6** - UI framework
- **FXML** - Layout definition
- **SQLite** - Local database
- **ControlsFX** - Modern UI components
- **FontAwesomeFX** - Icon library
- **Maven** - Build and dependency management

## Project Structure

```
src/
├── main/
│   ├── java/com/wellness/assistant/
│   │   ├── MainApp.java                    # Main application class
│   │   ├── MainController.java             # Main view controller
│   │   ├── SidebarController.java          # Sidebar navigation controller
│   │   ├── DashboardController.java        # Dashboard view controller
│   │   ├── MyHabitsController.java         # Habits management controller
│   │   ├── AnalyticsController.java        # Analytics view controller
│   │   ├── AddHabitController.java         # Add habit dialog controller
│   │   ├── ReminderService.java            # Background reminder service
│   │   ├── AIAdvisor.java                  # AI recommendation engine
│   │   ├── model/
│   │   │   ├── Habit.java                  # Habit model
│   │   │   ├── HealthLog.java              # Health log model
│   │   │   ├── User.java                   # User model
│   │   │   └── Reminder.java               # Reminder model
│   │   └── storage/
│   │       └── DatabaseManager.java        # SQLite database manager
│   └── resources/com/wellness/assistant/
│       ├── MainView.fxml                   # Main application layout
│       ├── SidebarView.fxml                # Sidebar layout
│       ├── DashboardView.fxml              # Dashboard layout
│       ├── MyHabitsView.fxml               # Habits management layout
│       ├── AnalyticsView.fxml              # Analytics layout
│       ├── AddHabitDialog.fxml             # Add habit dialog layout
│       └── styles.css                      # Application stylesheet
└── test/
    └── java/com/wellness/assistant/
        └── AppTest.java                    # Unit tests
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- JavaFX 17 (included in dependencies)

## Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd AI-Wellness-Assistant
   ```

2. **Install dependencies**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn javafx:run
   ```

   Or run the JAR file directly:
   ```bash
   mvn package
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/AIWellnessAssistant-1.0-SNAPSHOT.jar com.wellness.assistant.MainApp
   ```

## Quick Start (Windows)

If you're on Windows and have Maven installed, you can use the provided batch files:

1. **Compile the project**:
   ```cmd
   compile.bat
   ```

2. **Run the application**:
   ```cmd
   run.bat
   ```

## Usage

### Getting Started

1. **Launch the application** - The dashboard will show your current wellness overview
2. **Add habits** - Click "Add New Habit" to create your first wellness routine
3. **Set reminders** - Configure times and frequencies for your habits
4. **Track progress** - Mark habits as completed to build your wellness history
5. **View analytics** - Get AI-powered insights and recommendations

### Features Overview

#### Dashboard
- View today's progress and completion rates
- See active habits at a glance
- Quick access to add new habits

#### My Habits
- Manage all your wellness habits
- Filter by type (Water, Exercise, Medicine, Eye Rest)
- Edit or delete existing habits
- Mark habits as completed

#### Analytics
- View completion statistics
- Get AI-powered recommendations
- Track activity history
- Analyze patterns and trends

## Database Schema

The application uses SQLite with two main tables:

### Habits Table
- `id` - Primary key
- `name` - Habit name
- `type` - Habit category (water, exercise, medicine, eye_rest)
- `time` - Scheduled time (HH:MM format)
- `frequency` - Repeat frequency (daily, weekdays, weekends, weekly)
- `active` - Whether the habit is active

### Health Logs Table
- `log_id` - Primary key
- `habit_id` - Foreign key to habits table
- `habit_name` - Habit name (denormalized for performance)
- `status` - Completion status (Completed, Skipped)
- `timestamp` - When the activity was logged

## AI Recommendations

The AI advisor analyzes your activity patterns to provide personalized recommendations:

- **Consistency Analysis** - Tracks daily completion patterns
- **Habit Performance** - Identifies your most successful habits
- **Time Optimization** - Suggests optimal scheduling based on completion times
- **Motivational Messages** - Provides encouragement based on progress

## Customization

### Adding New Habit Types

1. Update the `Habit` model to include new types
2. Add new type options to the `AddHabitController` ComboBox
3. Update the CSS stylesheet for new type-specific styling
4. Modify the `AIAdvisor` to handle new types in recommendations

### Styling

The application uses a comprehensive CSS stylesheet (`styles.css`) with:
- Modern gradient colors
- Responsive design principles
- Consistent spacing and typography
- Hover effects and transitions

## Development

### Building from Source

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package

# Run the application
mvn javafx:run
```

### Adding New Features

1. **New Views**: Create FXML files and corresponding controllers
2. **Database Changes**: Update `DatabaseManager` and run migrations
3. **UI Components**: Add new controls and update CSS styling
4. **Business Logic**: Implement new services and update existing controllers

## Troubleshooting

### Common Issues

1. **JavaFX Module Issues**: Ensure JavaFX is properly configured in your Java runtime
2. **Database Connection**: Check that SQLite driver is available in classpath
3. **FXML Loading Errors**: Verify FXML file paths and controller references
4. **Missing Dependencies**: Run `mvn clean install` to resolve dependency issues

### Logs

The application logs important events to the console:
- Database operations
- Reminder service activities
- Error messages and stack traces

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please create an issue in the repository or contact the development team.

---

**Wellness Hub** - Your Health Companion for a Better Life! 🌟
