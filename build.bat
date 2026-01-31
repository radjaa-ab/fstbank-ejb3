@echo off
REM Build script for FSTBANK
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo Cleaning previous builds...
if exist target rmdir /s /q target

echo Setting up environment...
set "JAVA_HOME=C:\jdk17\jdk-17.0.10+7"
set "MAVEN_HOME=C:\Users\DELL\maven-3.8.6"
set "PATH=%MAVEN_HOME%\bin;%JAVA_HOME%\bin;%PATH%"

echo.
echo Java Version:
java -version

echo.
echo Maven Version:
mvn -version

echo.
echo Building FSTBANK with Maven...
call mvn clean package -DskipTests -q

if errorlevel 1 (
    echo Build FAILED!
    pause
    exit /b 1
)

echo.
echo Build SUCCESSFUL!
echo.
echo Artifacts in: target\
dir /b target\*.ear

pause
