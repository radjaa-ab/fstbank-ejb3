@echo off
REM ========================================================
REM FSTBANK - Complete Deployment Setup for Windows
REM ========================================================
REM This script will:
REM 1. Download Maven
REM 2. Download WildFly
REM 3. Configure WildFly
REM 4. Build the application
REM 5. Deploy to WildFly
REM ========================================================

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo ========================================================
echo   FSTBANK - Enterprise Banking Application
echo   Complete Deployment Setup for Windows
echo ========================================================
echo.

REM Color codes
set ERROR_COLOR=
set SUCCESS_COLOR=
set WARNING_COLOR=

REM ========================================================
REM 1. Check/Install Maven
REM ========================================================
echo [Step 1/5] Checking Maven...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo  - Maven not found. Downloading...
    
    set MAVEN_HOME=%USERPROFILE%\maven-3.8.6
    
    if not exist "!MAVEN_HOME!" (
        echo  - Downloading Maven 3.8.6...
        powershell -Command "Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip' -OutFile '%TEMP%\maven.zip'" 2>nul
        
        if exist "%TEMP%\maven.zip" (
            echo  - Extracting Maven...
            powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%'" 2>nul
            ren "%USERPROFILE%\apache-maven-3.8.6" "maven-3.8.6"
            del "%TEMP%\maven.zip"
        ) else (
            echo  ERROR: Could not download Maven
            echo  Please install Maven manually from: https://maven.apache.org/download.cgi
            pause
            exit /b 1
        )
    )
    
    setx PATH "%MAVEN_HOME%\bin;!PATH!"
    set PATH=%MAVEN_HOME%\bin;!PATH!
)
echo [OK] Maven is ready
echo.

REM ========================================================
REM 2. Check/Install WildFly
REM ========================================================
echo [Step 2/5] Checking WildFly...
set WILDFLY_HOME=%USERPROFILE%\wildfly-20.0.0.Final

if not exist "!WILDFLY_HOME!" (
    echo  - WildFly not found. Downloading...
    echo  - Downloading WildFly 20.0.0.Final...
    
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/wildfly/wildfly/releases/download/20.0.0.Final/wildfly-20.0.0.Final.zip' -OutFile '%TEMP%\wildfly.zip'" 2>nul
    
    if exist "%TEMP%\wildfly.zip" (
        echo  - Extracting WildFly...
        powershell -Command "Expand-Archive -Path '%TEMP%\wildfly.zip' -DestinationPath '%USERPROFILE%'" 2>nul
        del "%TEMP%\wildfly.zip"
    ) else (
        echo  ERROR: Could not download WildFly
        echo  Please download manually from: https://wildfly.org/downloads/
        pause
        exit /b 1
    )
)
echo [OK] WildFly is ready at: !WILDFLY_HOME!
echo.

REM ========================================================
REM 3. Create WildFly Admin User
REM ========================================================
echo [Step 3/5] Setting up WildFly Admin User...
set USERS_PROPERTIES=!WILDFLY_HOME!\standalone\configuration\application-users.properties

if not exist "!USERS_PROPERTIES!" (
    echo  - Creating admin user...
    
    REM Create application-users.properties with admin user
    (
        echo admin=8a9487b97cf0b77da3a39ac5ce6c11b1
        echo admin=admin123
    ) > "!USERS_PROPERTIES!"
    
    echo [OK] Admin user created (username: admin, password: admin123)
) else (
    echo [OK] Admin user already exists
)
echo.

REM ========================================================
REM 4. Build Application
REM ========================================================
echo [Step 4/5] Building FSTBANK application...
echo  - Running: mvn clean package -DskipTests
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo [ERROR] Build failed!
    pause
    exit /b 1
)
echo [OK] Application built successfully
echo.

REM ========================================================
REM 5. Deploy to WildFly
REM ========================================================
echo [Step 5/5] Deploying to WildFly...

set EAR_FILE=target\fstbank-ear-1.0.0.ear
set DEPLOY_DIR=!WILDFLY_HOME!\standalone\deployments

if not exist "!EAR_FILE!" (
    echo [WARNING] EAR file not found at: !EAR_FILE!
    echo [INFO] Searching for EAR files...
    
    for /f "delims=" %%A in ('dir /b /s "target\*.ear" 2^>nul ^| findstr /i ear') do (
        set EAR_FILE=%%A
        echo [INFO] Found: %%A
        goto :found_ear
    )
    
    echo [ERROR] No EAR file found!
    pause
    exit /b 1
    
    :found_ear
)

echo  - Copying !EAR_FILE! to !DEPLOY_DIR!
copy "!EAR_FILE!" "!DEPLOY_DIR!" >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Failed to copy EAR file
    pause
    exit /b 1
)
echo [OK] Application deployed
echo.

REM ========================================================
REM 6. Start WildFly
REM ========================================================
echo ========================================================
echo   DEPLOYMENT COMPLETE!
echo ========================================================
echo.
echo [INFO] Starting WildFly server...
echo.
echo Dashboard will be available at:
echo   http://localhost:8080/fstbank
echo.
echo Admin Console:
echo   http://localhost:9990
echo   Username: admin
echo   Password: admin123
echo.
echo REST API:
echo   http://localhost:8080/fstbank/api/dashboard/status
echo.
echo Press any key to start WildFly...
pause

REM Start WildFly
call "!WILDFLY_HOME!\bin\standalone.bat"
