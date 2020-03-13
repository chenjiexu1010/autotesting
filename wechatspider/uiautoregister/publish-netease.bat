cmd /c ant build
cmd /c adb push %~dp0\bin\AutoRunner.jar data/local/tmp
cmd /c adb shell uiautomator runtest AutoRunner.jar -c com.netease.mailRegistor
rem -e debug true
pause
