@echo off
setlocal
cd /d "%~dp0"

where scrcpy >nul 2>nul
if errorlevel 1 (
    echo scrcpy was not found.
    echo.
    echo scrcpy is a very light phone-screen mirroring tool.
    echo Download it from: https://github.com/Genymobile/scrcpy/releases
    echo Extract it, add scrcpy.exe to PATH, connect your phone with USB debugging,
    echo and run this file again.
    echo.
    pause
    exit /b 1
)

echo Mirroring the connected phone. Close this window to stop.
echo Press Ctrl+C if the phone is not detected.
scrcpy --max-size 1024 --max-fps 30 --stay-awake
