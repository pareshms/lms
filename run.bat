@echo off
title Library Management System - College OOP Mini Project
color 0B

echo ===================================================================
echo     LIBRARY MANAGEMENT SYSTEM - COLLEGE OOP MINI PROJECT
echo ===================================================================
echo.

:: 1. Detect JAVA and set JAVA_HOME
if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\Microsoft\jdk-17.0.21.102-hotspot" (
        set "JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.21.102-hotspot"
    )
)

if not "%JAVA_HOME%"=="" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java 17 is not found!
    echo Please install Java 17 from: https://adoptium.net/temurin/releases/?version=17
    pause
    exit /b 1
)

:: 2. Detect Maven and add to PATH
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    if exist "%USERPROFILE%\maven\bin" (
        set "PATH=%USERPROFILE%\maven\bin;%PATH%"
    ) else if exist "C:\maven\bin" (
        set "PATH=C:\maven\bin;%PATH%"
    )
)

echo [*] Java detected.
echo [*] Starting Spring Boot Library Backend...
echo [*] Server will be available at: http://localhost:8080
echo [*] Login credentials:
echo       Username: admin
echo       Password: admin123
echo.
echo -------------------------------------------------------------------

:: Open browser automatically after 8 seconds in the background
start "" cmd /c "timeout /t 8 /nobreak >nul && start http://localhost:8080"

:: Start the Spring Boot application using CALL
cd /d "%~dp0"
call mvn spring-boot:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Server stopped or encountered an issue.
    pause
)
