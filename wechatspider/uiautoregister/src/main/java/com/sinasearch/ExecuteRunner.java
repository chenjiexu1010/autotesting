package com.sinasearch;

import com.WeChatNew.LoginInfo;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.uiControl.UiEdit;
import com.google.gson.Gson;

import java.util.Random;

public class ExecuteRunner extends SuperRunner {
    //外网ip
    private String Host = "222.185.251.62";
    //添加日志
    private String addLogURL = "http://" + Host + ":30008/api/wx/addlog";

    private String Type = "微博下拉词";

    private String NewBrushStr = "朱星杰劣迹艺人";

    Random random = new Random(10000L);

    //启动方法
    public void testRunner() {
        StartMain();
    }

    private boolean initialization() {
        try {
            Device = getUiDevice();
            PackegName = "com.sina.weibo";
            Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
            Device.wakeUp();
        } catch (Exception e) {
        }
        return true;
    }

    public void StartMain() {
        initialization();
        //切换输入法
        ScriptHelper.switchImeToAdbKeyboard();
        while (true) {
            try {
                jqhelper.reportNormal();
//                Search(this.OldBrushStr, false);
                jqhelper.reportNormal();
                Search(this.NewBrushStr, true);
                jqhelper.reportNormal();
//                Search(this.Test01, true);
            } catch (Exception e) {
                AddLog("微博下拉词", "异常", "");
            }
        }
    }

    /**
     * 搜索
     */
    private void Search(String key, boolean isSearch) {
        try {
            restartApp();
            jqhelper.reportNormal();

            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (ob.waitForExists(5000)) {
                ob.click();
            }
            ob = new UiObject(new UiSelector().className("android.view.View").description("发现"));
            jqhelper.reportNormal();
            if (ob.waitForExists(3000)) {
                ob.click();
                sleep(3000);
                UiEdit edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                if (edit.waitForExists(3000)) {
                    edit.click();
                    sleep(2000);
                    //输入搜索的微信名称
                    edit.setChineseText(key);
                    Device.pressEnter();
                    sleep(5000);
                }
                int number = random.nextInt(10000);
                String info = String.format("随机SleepMs:%s", random.nextInt(10000));
                AddLog(Type, info, key);
                Device.pressBack();
                Device.wakeUp();
                if (isSearch) {
                    jqhelper.reportNormal();
                    sleep(300000);
                    jqhelper.reportNormal();
                    sleep(300000);
                    jqhelper.reportNormal();
                    sleep(300000);
                    jqhelper.reportNormal();
                    sleep(300000);
                    jqhelper.reportNormal();
                    sleep(300000);
                    jqhelper.reportNormal();
                    sleep(number);
                }
            }
        } catch (Exception e) {
        }
    }

    private void restartApp() {
        try {
            Device.wakeUp();
            ob = new UiObject(new UiSelector().className("android.view.View").description("发现"));
            if (!ob.waitForExists(2000)) {
                jqhelper.killApp(PackegName);
                jqhelper.killApp(PackegName + "：push");
                sleep(1000);
                //打开新浪微博
                Runtime.getRuntime().exec("am start -n com.sina.weibo/.MainTabActivity");
                sleep(5000);
            }
            if (GetUIObject("android.widget.TextView", "不了，谢谢").waitForExists(2000L)) {
                GetUIObject("android.widget.TextView", "不了，谢谢").click();
            }
        } catch (Exception e) {
        }
    }

    public void AddLog(String targetName, String logType, String errorMsg) {
        try {
            LoginInfo logInfo = new LoginInfo();
            logInfo.setIsshow(0);
            logInfo.setType(logType);
            logInfo.setBatchNumber("");
            logInfo.setRemark(errorMsg);
            logInfo.setCloneid(0 + "");
            logInfo.setCode("");
            logInfo.setMobile(this.Vps);
            logInfo.setLogTargetName(targetName);
            logInfo.setStatus(1 + "");
            Gson gson = new Gson();
            String jsonStr = gson.toJson(logInfo);
            String postData = jsonStr;
            String rData = HttpHelper.httpPostForString(this.addLogURL, postData);
        } catch (Exception e) {
        }
    }
}

