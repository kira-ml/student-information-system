@echo off
setlocal
cd /d "%~dp0"

echo ============================================
echo  Student Information System - build APK
echo ============================================
echo.

call gradlew.bat :app:assembleDebug :app:testDebugUnitTest --console=plain
if errorlevel 1 (
    echo.
    echo Build failed. Review the messages above.
    pause
    exit /b 1
)

if not exist "dist" mkdir "dist"
copy /y "app\build\outputs\apk\debug\app-debug.apk" "dist\StudentInformationSystem-debug.apk" >nul
if errorlevel 1 (
    echo.
    echo The APK was built but could not be copied to the dist folder.
    pause
    exit /b 1
)

echo.
echo Build complete.
echo APK: %~dp0dist\StudentInformationSystem-debug.apk
echo.
pause
