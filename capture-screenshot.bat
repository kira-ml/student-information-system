@echo off
setlocal EnableExtensions
cd /d "%~dp0"

set "ADB=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
if not exist "%ADB%" (
    where adb >nul 2>nul
    if errorlevel 1 (
        echo adb was not found.
        echo Install Android SDK Platform-Tools, or take the screenshot with
        echo the phone's Power + Volume Down buttons and copy it manually.
        pause
        exit /b 1
    )
    set "ADB=adb"
)

if not exist "screenshots" mkdir "screenshots"

set "NAME=%~1"
if "%NAME%"=="" (
    set /p "NAME=Enter a name for this screenshot (example: login-01-screen): "
)
if "%NAME%"=="" set "NAME=screenshot"
set "TARGET=screenshots\%NAME%.png"
if exist "%TARGET%" set "TARGET=screenshots\%NAME%-%RANDOM%.png"

"%ADB%" shell true >nul 2>nul
if errorlevel 1 (
    echo.
    echo No phone is available through adb.
    echo.
    echo Option 1: take the screenshot with Power + Volume Down, then copy it
    echo           into the screenshots folder.
    echo Option 2: enable Developer options and USB debugging, connect the
    echo           phone, accept the Allow prompt, then run this file again.
    echo.
    pause
    exit /b 1
)

"%ADB%" shell screencap -p /sdcard/sis-screenshot.png
"%ADB%" pull /sdcard/sis-screenshot.png "%TARGET%"
"%ADB%" shell rm /sdcard/sis-screenshot.png

echo.
echo Saved: %~dp0%TARGET%
echo.
pause
