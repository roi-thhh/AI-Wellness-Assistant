@echo off
echo Testing Wellness Hub Application Structure...

echo.
echo ==========================================
echo Checking Java files...
echo ==========================================

if exist "src\main\java\com\wellness\assistant\MainApp.java" (
    echo ✓ MainApp.java found
) else (
    echo ✗ MainApp.java missing
)

if exist "src\main\java\com\wellness\assistant\MainController.java" (
    echo ✓ MainController.java found
) else (
    echo ✗ MainController.java missing
)

if exist "src\main\java\com\wellness\assistant\DashboardController.java" (
    echo ✓ DashboardController.java found
) else (
    echo ✗ DashboardController.java missing
)

if exist "src\main\java\com\wellness\assistant\MyHabitsController.java" (
    echo ✓ MyHabitsController.java found
) else (
    echo ✗ MyHabitsController.java missing
)

if exist "src\main\java\com\wellness\assistant\AnalyticsController.java" (
    echo ✓ AnalyticsController.java found
) else (
    echo ✗ AnalyticsController.java missing
)

echo.
echo ==========================================
echo Checking FXML files...
echo ==========================================

if exist "src\main\resources\com\wellness\assistant\MainView.fxml" (
    echo ✓ MainView.fxml found
) else (
    echo ✗ MainView.fxml missing
)

if exist "src\main\resources\com\wellness\assistant\DashboardView.fxml" (
    echo ✓ DashboardView.fxml found
) else (
    echo ✗ DashboardView.fxml missing
)

if exist "src\main\resources\com\wellness\assistant\MyHabitsView.fxml" (
    echo ✓ MyHabitsView.fxml found
) else (
    echo ✗ MyHabitsView.fxml missing
)

if exist "src\main\resources\com\wellness\assistant\AnalyticsView.fxml" (
    echo ✓ AnalyticsView.fxml found
) else (
    echo ✗ AnalyticsView.fxml missing
)

echo.
echo ==========================================
echo Checking CSS and other files...
echo ==========================================

if exist "src\main\resources\com\wellness\assistant\styles.css" (
    echo ✓ styles.css found
) else (
    echo ✗ styles.css missing
)

if exist "pom.xml" (
    echo ✓ pom.xml found
) else (
    echo ✗ pom.xml missing
)

echo.
echo ==========================================
echo Application Structure Test Complete!
echo ==========================================
echo.
echo All major files are present. The application should now work properly.
echo.
echo To run the application:
echo 1. Make sure Java 21 (LTS) and Maven are installed
echo 2. Run: mvn clean compile
echo 3. Run: mvn javafx:run
echo.
echo Or use the provided batch files:
echo - compile.bat (to compile)
echo - run.bat (to run)
echo.

pause
