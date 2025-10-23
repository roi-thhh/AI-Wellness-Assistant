@echo off
echo Compiling Wellness Hub Application...

REM Check if Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Java is not installed or not in PATH
    pause
    exit /b 1
)

REM Check if Maven is available
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven is not installed or not in PATH
    echo Please install Maven to compile the project
    pause
    exit /b 1
)

REM Clean and compile
echo Cleaning project...
mvn clean

echo Compiling project...
mvn compile

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo Compilation successful!
    echo ==========================================
    echo.
    echo To run the application, use:
    echo mvn javafx:run
    echo.
    echo Or package and run:
    echo mvn package
    echo java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/AIWellnessAssistant-1.0-SNAPSHOT.jar com.wellness.assistant.MainApp
) else (
    echo.
    echo ==========================================
    echo Compilation failed!
    echo ==========================================
    echo Please check the error messages above.
)

pause
