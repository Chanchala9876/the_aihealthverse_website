@echo off
echo 🏥 HealthTracker Doctor Login Test
echo =====================================
echo.
echo 📋 Available Doctor Accounts for Login:
echo.
echo 👨‍⚕️ Dr. Jhal Kumari (Mental Health Specialist)
echo    Email: jhalkumari2003@gmail.com
echo    Password: doctor123
echo    Specialization: Mental Health
echo.
echo 👩‍⚕️ Dr. Sarah Wilson (Clinical Psychologist)
echo    Email: sarah.wilson@healthverse.com
echo    Password: doctor123
echo    Specialization: Mental Health
echo.
echo 👨‍⚕️ Dr. Rajesh Kumar (Psychiatrist)
echo    Email: rajesh.kumar@healthverse.com
echo    Password: doctor123
echo    Specialization: Mental Health
echo.
echo 🔐 How to Login:
echo 1. Start the application: mvn spring-boot:run
echo 2. Go to: http://localhost:8090
echo 3. Click "Doctor Portal" in navbar
echo 4. Use any of the above credentials
echo 5. After login, you'll see chat requests from patients
echo.
echo 💬 Testing Real-time Chat:
echo 1. Open two browsers (Chrome + Firefox/Edge)
echo 2. Browser 1: Login as patient, request consultation
echo 3. Browser 2: Login as doctor, accept consultation
echo 4. Both browsers: Chat in real-time!
echo.
pause
