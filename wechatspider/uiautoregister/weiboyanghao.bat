cmd /c ant build
cmd /c adb -s 0414023638 push %~dp0\bin\AutoRunner.jar data/local/tmp
cmd /c adb -s 0414023638 shell uiautomator runtest AutoRunner.jar -c com.weibo.yanghao.YangHao
rem -e debug true
pause
