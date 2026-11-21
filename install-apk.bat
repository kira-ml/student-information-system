@echo off
setlocal
cd /d "%~dp0"

set "APK=%~dp0dist\StudentInformationSystem-debug.apk"
if not exist "%APK%" (
    echo APK not found.
    echo Run build-apk.bat first, then try again.
    pause
    exit /b 1
)

set "ADB=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
if not exist "%ADB%" (
    where adb >nul 2>nul
    if errorlevel 1 (
        echo adb was not found.
        echo Install Android SDK Platform-Tools or copy the APK to your phone manually.
        pause
        exit /b 1
    )
    set "ADB=adb"
)

echo ============================================
echo  Install Student SIS on a connected phone
echo ============================================
echo.
echo Make sure USB debugging is enabled and the "Allow USB debugging"
echo prompt on the phone has been accepted.
echo.
"%ADB%" devices
echo.

"%ADB%" install -r "%APK%"
if errorlevel 1 (
    echo.
    echo Installation failed.
    echo Check that the phone is listed as "device" above, then try again.
    pause
    exit /b 1
)

"%ADB%" shell am start -n com.example.sis/.LoginActivity
if errorlevel 1 (
    echo.
    echo The app was installed but could not be launched automatically.
    echo Open "Student SIS" from the phone's app drawer.
    pause
    exit /b 1
)

echo.
echo Student SIS is now running on your phone.
echo Optional: run mirror-phone.bat to show the phone screen on this laptop.
echo.
pause
