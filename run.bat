REM filepath: c:\Users\ashfa\Documents\Projects2\OOAD-Project\run.bat
@echo off
echo ===== Airline Management System =====

REM Create output directory if it doesn't exist
mkdir out 2>nul

echo Compiling Java files...
REM Compile all Java files by listing each package separately
javac -d out -cp ".;C:/Program Files/Java/javafx-sdk-17.0.14/lib/*" ^
  src/main/java/com/airline/*.java ^
  src/main/java/com/airline/controller/*.java ^
  src/main/java/com/airline/model/*.java ^
  src/main/java/com/airline/view/*.java ^
  src/main/java/com/airline/util/*.java

if %errorlevel% neq 0 (
    echo Compilation failed! Please fix the errors and try again.
    pause
    exit /b %errorlevel%
)

echo Running application...
java --module-path "C:/Program Files/Java/javafx-sdk-17.0.14/lib" --add-modules javafx.controls,javafx.fxml -cp out com.airline.Main

if %errorlevel% neq 0 (
    echo Application exited with error code: %errorlevel%
) else (
    echo Application closed successfully.
)

pause