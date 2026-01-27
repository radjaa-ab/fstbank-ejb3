@echo off
REM FSTBANK Quick Start Script for WildFly Deployment (Windows)

echo.
echo ============================================================
echo   FSTBANK - Enterprise Banking Application
echo   Quick Deployment Script for WildFly
echo ============================================================
echo.

REM Check if Java is installed
echo [1] Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo X Java not found. Please install Java 11+
    exit /b 1
)
for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| find "version"') do set JAVA_VER=%%v
echo OK Java %JAVA_VER% found
echo.

REM Check if Maven is installed
echo [2] Checking Maven installation...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo X Maven not found. Please install Maven 3.6+
    exit /b 1
)
echo OK Maven found
echo.

REM Build the application
echo [3] Building FSTBANK application...
call mvn clean package -DskipTests
if errorlevel 1 (
    echo X Build failed
    exit /b 1
)
echo OK Build successful
echo.

REM Check if WildFly is running
echo [4] Checking WildFly server...
powershell -Command "Test-Connection -ComputerName localhost -Port 8080 -ErrorAction SilentlyContinue" >nul 2>&1
if not errorlevel 1 (
    echo OK WildFly is running
) else (
    echo WARNING WildFly is not running
    echo Start WildFly with: wildfly-20.0.0.Final\bin\standalone.bat
    echo.
)
echo.

REM Deploy the application
echo [5] Deploying to WildFly...
set ARTIFACT=target\fstbank-ear-1.0.0.ear
set WILDFLY_HOME=%USERPROFILE%\wildfly-20.0.0.Final

if exist "%ARTIFACT%" (
    copy "%ARTIFACT%" "%WILDFLY_HOME%\standalone\deployments\"
    echo OK Application deployed
) else (
    echo X EAR file not found at %ARTIFACT%
    exit /b 1
)
echo.

REM Display access information
echo [6] Access Information:
echo    Dashboard: http://localhost:8080/fstbank
echo    Admin Console: http://localhost:9990
echo    API Endpoint: http://localhost:8080/fstbank/api/dashboard
echo.

echo ============================================================
echo   FSTBANK is ready! Open your browser and visit:
echo   http://localhost:8080/fstbank
echo ============================================================
echo.

REM Open browser
start http://localhost:8080/fstbank

pause
