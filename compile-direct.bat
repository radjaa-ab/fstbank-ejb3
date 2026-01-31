@echo off
REM Direct compilation without Maven
setlocal enabledelayedexpansion

cd /d "C:\Users\DELL\Desktop\adla project"

set JAVA_HOME=C:\jdk17\jdk-17.0.10+7
set JAVAC="%JAVA_HOME%\bin\javac.exe"
set JAR="%JAVA_HOME%\bin\jar.exe"

echo [*] Compiling FSTBANK Java files...
echo [*] Java Compiler: %JAVAC%
echo.

if not exist "bin" mkdir bin

REM Compile all Java files from src
%JAVAC% -d bin -encoding UTF-8 ^
  src\main\java\dz\fst\bank\entities\*.java ^
  src\main\java\dz\fst\bank\session\*.java ^
  src\main\java\dz\fst\bank\factories\*.java ^
  src\main\java\dz\fst\bank\observers\*.java ^
  src\main\java\dz\fst\bank\strategies\*.java ^
  src\main\java\dz\fst\bank\rest\*.java ^
  src\main\java\dz\fst\bank\demo\*.java ^
  src\main\java\dz\fst\bank\test\*.java 2>&1

if %errorlevel% equ 0 (
    echo [OK] Compilation successful!
    echo.
    echo [*] Creating JAR...
    %JAR% cvf target\fstbank.jar -C bin .
    echo [OK] JAR created: target\fstbank.jar
) else (
    echo [ERROR] Compilation failed
    exit /b 1
)
