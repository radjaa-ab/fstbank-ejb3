@echo off
REM ============================================================================
REM FSTBANK - Complete One-Click Deployment
REM This script will build and deploy everything automatically
REM ============================================================================

setlocal enabledelayedexpansion

REM Get the script directory
cd /d "%~dp0"
set PROJECT_DIR=%cd%

REM Setup directories
set JAVA_DIR=C:\jdk17
set MAVEN_DIR=C:\Users\DELL\maven-3.8.6
set WILDFLY_DIR=C:\Users\DELL\wildfly-20.0.0.Final

echo.
echo ============================================================================
echo   FSTBANK - COMPLETE ONE-CLICK DEPLOYMENT
echo ============================================================================
echo.

REM ============================================================================
REM STEP 1: Check/Download WildFly if needed
REM ============================================================================
if exist "%WILDFLY_DIR%" (
    echo [OK] WildFly found at: %WILDFLY_DIR%
) else (
    echo [INFO] WildFly not found
    echo [INFO] Please download from: https://download.jboss.org/wildfly/20.0.0.Final/wildfly-20.0.0.Final.zip
    echo [INFO] Extract to: C:\Users\DELL\
    echo [INFO] Waiting...
    pause
)

REM ============================================================================
REM STEP 2: Configure WildFly Admin User
REM ============================================================================
if exist "%WILDFLY_DIR%\standalone\configuration\application-users.properties" (
    echo [OK] WildFly admin user already configured
) else (
    echo [INFO] Creating WildFly admin user...
    (
        echo admin=admin123
    ) > "%WILDFLY_DIR%\standalone\configuration\application-users.properties"
    echo [OK] Admin user created: admin / admin123
)

REM ============================================================================
REM STEP 3: Build Project
REM ============================================================================
echo.
echo [*] Building FSTBANK application...
echo [*] Java: %JAVA_DIR%
echo [*] Maven: %MAVEN_DIR%
echo.

REM Use javahome without special characters
for /f "tokens=*" %%A in ('dir /b "%JAVA_DIR%"') do set JAVA_HOME=%JAVA_DIR%\%%A

setlocal
set PATH=%MAVEN_DIR%\bin;%JAVA_HOME%\bin;%PATH%

REM Navigate to project
cd /d "%PROJECT_DIR%"

REM Clean
echo [*] Cleaning previous builds...
if exist target rmdir /s /q target >nul 2>&1

REM Build
echo [*] Compiling...
call "%MAVEN_DIR%\bin\mvn" clean package -DskipTests -q

if !ERRORLEVEL! NEQ 0 (
    echo [ERROR] Build failed!
    echo [INFO] Check your Maven and Java installation
    pause
    exit /b 1
)

echo [OK] Build successful!

REM ============================================================================
REM STEP 4: Deploy to WildFly
REM ============================================================================
if not exist "%WILDFLY_DIR%\standalone\deployments" (
    echo [ERROR] WildFly deployments directory not found
    echo [INFO] Please ensure WildFly is properly extracted to: %WILDFLY_DIR%
    pause
    exit /b 1
)

echo [*] Deploying to WildFly...

REM Find EAR file
for /f "tokens=*" %%F in ('dir /b target\*.ear 2^>nul') do (
    set EAR_FILE=target\%%F
    echo [*] Found: %%F
    copy "!EAR_FILE!" "%WILDFLY_DIR%\standalone\deployments\" >nul
    if !ERRORLEVEL! EQU 0 (
        echo [OK] Deployed: %%F
    ) else (
        echo [ERROR] Failed to copy EAR file
        pause
        exit /b 1
    )
)

if not defined EAR_FILE (
    echo [ERROR] No EAR file found in target\
    pause
    exit /b 1
)

REM ============================================================================
REM STEP 5: Ready to Start
REM ============================================================================
echo.
echo ============================================================================
echo   DEPLOYMENT COMPLETE!
echo ============================================================================
echo.
echo Next Steps:
echo [1] Start WildFly Server:
echo     Command: %WILDFLY_DIR%\bin\standalone.bat
echo.
echo [2] Access Dashboard:
echo     URL: http://localhost:8080/fstbank
echo.
echo [3] Admin Console:
echo     URL: http://localhost:9990
echo     Username: admin
echo     Password: admin123
echo.
echo [4] REST API:
echo     URL: http://localhost:8080/fstbank/api/dashboard/status
echo.
echo ============================================================================
echo.

REM Ask to start WildFly
echo Do you want to start WildFly now? (Y/N)
set /p RESPONSE=
if /i "%RESPONSE%"=="Y" (
    echo [*] Starting WildFly...
    call "%WILDFLY_DIR%\bin\standalone.bat"
)
