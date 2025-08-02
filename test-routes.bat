@echo off
echo ======================================
echo    Testing HealthTracker Routes
echo ======================================
echo.
echo Starting application in background...
start /B .\mvnw.cmd spring-boot:run > app.log 2>&1

echo Waiting for application to start...
timeout /t 30 > nul

echo.
echo Testing routes...
echo.

echo Testing /dashboard route:
curl -s -o nul -w "%%{http_code}" http://localhost:8090/dashboard
echo.

echo Testing /health/entries route:
curl -s -o nul -w "%%{http_code}" http://localhost:8090/health/entries
echo.

echo Testing /mental-health route:
curl -s -o nul -w "%%{http_code}" http://localhost:8090/mental-health
echo.

echo.
echo Route testing complete. Check app.log for any errors.
echo Press any key to stop the application...
pause > nul

echo Stopping application...
taskkill /f /im java.exe > nul 2>&1

echo Done!
