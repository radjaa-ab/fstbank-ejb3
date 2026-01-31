@echo off
echo ============================================
echo   Creating WildFly Admin User
echo ============================================
echo.
echo Username: admin
echo Password: admin
echo.

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
cd /d "C:\Users\DELL\FSTBANK-Setup\wildfly-new\wildfly-20.0.0.Final\bin"

(
echo a
echo admin
echo ManagementRealm
echo admin
echo admin
echo yes
) | add-user.bat

echo.
echo ============================================
echo   Admin user created!
echo   URL: http://127.0.0.1:9990/console
echo   Username: admin
echo   Password: admin
echo ============================================
pause
