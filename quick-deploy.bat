@echo off
setlocal enabledelayedexpansion

REM Maven build workaround for JAVA_HOME with + character
cd /d "C:\Users\DELL\Desktop\adla project"

REM Try to build with Maven using explicit java exe path
echo [*] Building FSTBANK with Maven...
echo [*] Java: C:\jdk17\jdk-17.0.10+7\bin\java.exe
echo [*] Maven: C:\Users\DELL\maven-3.8.6
echo.

REM Set environment for Maven
set "M2_HOME=C:\Users\DELL\maven-3.8.6"
set "PATH=%M2_HOME%\bin;C:\jdk17\jdk-17.0.10+7\bin;%PATH%"

REM Use explicit java.exe path with Maven
call "%M2_HOME%\bin\mvn.cmd" ^
  "-Djava.home=C:\jdk17\jdk-17.0.10+7" ^
  "-Dexec.executable=C:\jdk17\jdk-17.0.10+7\bin\java.exe" ^
  clean package -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo [OK] Build successful!
    echo [OK] Output: target\fstbank-1.0.0.jar
    echo [OK] Starting WildFly deployment...
    echo.
    pause
    
    REM Deploy to WildFly
    if exist "C:\Users\DELL\wildfly-20.0.0.Final\standalone\deployments" (
        echo [*] Deploying to WildFly...
        copy "target\fstbank-1.0.0.jar" "C:\Users\DELL\wildfly-20.0.0.Final\standalone\deployments\fstbank.jar"
        echo [OK] Deployed!
    )
) else (
    echo.
    echo [ERROR] Build failed with code %errorlevel%
    echo.
    pause
    exit /b 1
)
