@echo off
echo =======================================
echo   Starting HealthTracker Application
echo =======================================
echo.
echo Please wait while the application starts...
echo Once started, you can access it at: http://localhost:8090
echo.
echo Press Ctrl+C to stop the application
echo.

.\mvnw.cmd spring-boot:run

pause
