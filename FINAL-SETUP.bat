@echo off
setlocal enabledelayedexpansion

echo ===============================================
echo FSTBANK - FINAL SETUP & DEPLOYMENT
echo ===============================================
echo.

:: Check if WildFly zip exists
if not exist "C:\Users\DELL\Downloads\FSTBANK-Setup\wildfly.zip" (
    echo ❌ ERROR: WildFly ZIP not found at C:\Users\DELL\Downloads\FSTBANK-Setup\wildfly.zip
    echo.
    echo Please download WildFly from:
    echo https://download.jboss.org/wildfly/20.0.0.Final/wildfly-20.0.0.Final.zip
    echo.
    pause
    exit /b 1
)

:: Extract WildFly if not already extracted
if not exist "C:\Users\DELL\wildfly-20.0.0.Final" (
    echo 📦 Extracting WildFly 20...
    powershell -Command "Expand-Archive -Path 'C:\Users\DELL\Downloads\FSTBANK-Setup\wildfly.zip' -DestinationPath 'C:\Users\DELL' -Force" 2>nul
    
    if errorlevel 1 (
        echo ❌ Failed to extract WildFly
        echo Trying alternative method...
        cd C:\Users\DELL\Downloads\FSTBANK-Setup
        tar -xf wildfly.zip -C C:\Users\DELL\ 2>nul
        if errorlevel 1 (
            echo ❌ Both extraction methods failed
            pause
            exit /b 1
        )
    )
    echo ✅ WildFly extracted
    echo.
) else (
    echo ✅ WildFly already extracted
    echo.
)

:: Run complete deployment
echo 🚀 Starting Complete Deployment...
echo.
call COMPLETE-DEPLOYMENT.bat

if errorlevel 1 (
    echo ❌ Deployment failed
    pause
    exit /b 1
)

echo.
echo ✅ DEPLOYMENT COMPLETE!
echo.
echo 📝 NEXT STEPS:
echo 1. Start WildFly: C:\Users\DELL\wildfly-20.0.0.Final\bin\standalone.bat
echo 2. Wait for "started" message
echo 3. Open browser: http://localhost:8080/fstbank
echo.
pause
