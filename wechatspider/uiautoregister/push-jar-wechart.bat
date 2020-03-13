cmd /c ant build
cmd /c adb push %~dp0\bin\AutoRunner.jar data/local/tmp
cmd /c adb shell uiautomator runtest AutoRunner.jar -c com.wechat.loginWechat -e debug true
rem -e debug true
pause
