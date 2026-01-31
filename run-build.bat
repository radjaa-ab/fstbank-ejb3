@echo off
setlocal enabledelayedexpansion

:: Set Java Home using direct path without special characters issue
set "JAVA_HOME=C:\jdk17\jdk-17.0.10+7"
set "MAVEN_HOME=C:\Users\DELL\maven-3.8.6"
set "PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

:: Change to project directory
cd /d "C:\Users\DELL\Desktop\adla project"

:: Run Maven build
echo [*] Building FSTBANK...
echo [*] JAVA_HOME: %JAVA_HOME%
echo [*] MAVEN_HOME: %MAVEN_HOME%
echo.

call "%MAVEN_HOME%\bin\mvn.cmd" clean package -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo [OK] Build successful!
    echo [OK] EAR file: target\fstbank-ear-1.0.0.ear
) else (
    echo.
    echo [ERROR] Build failed with code %errorlevel%
)

pause
