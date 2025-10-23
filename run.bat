@echo off
echo Starting Wellness Hub Application...

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
    echo Please install Maven to run the project
    pause
    exit /b 1
)

REM Run the application
echo Starting Wellness Hub...
mvn javafx:run

pause
