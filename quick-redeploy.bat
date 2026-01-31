@echo off
echo ============================================
echo   FSTBANK Quick Redeploy
echo ============================================
echo.

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set MVN_CMD=C:\Users\DELL\maven-3.8.6\bin\mvn.cmd
set WILDFLY_DEPLOY=C:\Users\DELL\FSTBANK-Setup\wildfly-new\wildfly-20.0.0.Final\standalone\deployments

echo [1/3] Building WAR...
cd /d "%~dp0"
call "%MVN_CMD%" -q -DskipTests package

if errorlevel 1 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo [2/3] Removing old deployment...
del /F /Q "%WILDFLY_DEPLOY%\fstbank.war*" 2>nul

echo [3/3] Deploying new WAR...
copy /Y "target\fstbank-1.0.0.war" "%WILDFLY_DEPLOY%\fstbank.war" >nul

echo.
echo ============================================
echo   DONE! Wait 5 seconds, then refresh browser
echo   Press Ctrl+F5 to force reload
echo ============================================
timeout /t 5 /nobreak >nul
