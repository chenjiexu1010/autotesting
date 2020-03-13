package com.qq;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.ScriptHelper;
import com.wechat.WechatData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jeqee on 2015/12/22.
 */
public class RegisterRunner extends SuperRunner {
    private String Host = "222.185.251.62";
    //private String Host = "222.185.195.206";
    private String JqsocialUrl = "v2.jqsocial.com";
    //private String JqsocialUrl = "www.jqsocial.com:34567";
    private String getWXNamesURL = "http://" + Host + ":8007/api/wx/getwxnamesv2?w=";
    private String updateWXURL = "http://" + Host + ":8007/api/wx/update";
    private String addLogURL = "http://" + Host + ":8007/api/wx/addlog";
    private String updateImageURL = "http://" + JqsocialUrl + "/Files/UpLoadImageForWeixin2";
    private String updateVideoURL = "http://" + JqsocialUrl + "/Files/UpLoadVideoForWeixin";
    private String path = "/sdcard/Tencent/MicroMsg/WeiXin";
    private String path2 = "/sdcard/";
    private String videoPath = "/sdcard/tencent/MicroMsg";
    private Map<String, WechatData> load = new HashMap<String, WechatData>();
    private String batchNumber = "";
    private String mobile = "";
    private String selfName = "";

    //region 【初始化数据】
    private boolean initializtion() {
        jqhelper.reportNormal();
        PackegName = "com.tencent.mobileqq";
        ActivityName = ".activity.SplashActivity";
        System.out.println(1);
        Device = getUiDevice();
        ScriptHelper.switchImeToAdbKeyboard();
        try {
            Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
            restartAppAndClear();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //endregion

    //region 【业务逻辑流程执行】
    public void testRunner() {
        if (!initializtion()) return;//如果初始化失败，则结束
        //循环执行任务
        while (true) {
            try {
                sleep(5000);
                //设置手机基本信息
                setInfo();
                //初始化data
                QQDate date = new QQDate(getCharAndNumr(new Random().nextInt(5) + 10), "Jeqee789");
                //进入qq主界面
                enterQQMain();
                sleep(2000);
                //进入注册页面
                if (isQQMain()) {
                    enterRegister();
                }
                sleep(2000);
                //开始注册流程
                if (isRegisterS1()) {
                    registerS1(date);
                }
                sleep(2000);
                if (isRegisterS2()) {
                    registerS2();
                }
                sleep(2000);
                if (isRegisterS3()) {
                    registerS3(date);
                }
                sleep(2000);
                if (isRegisterSuccess()) {
                    getQQUin(date);
                }
                sleep(100000);
                //重启app
                restartAppAndClear();
            } catch (Exception ex) {

            }
        }
    }
    //endregion

    //region 【判断界面】

    //region 【是否为QQ首页】
    public boolean isQQMain() {
        ob = new UiObject(new UiSelector().className("android.widget.Button").text("新用户"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册第一步】
    public boolean isRegisterS1() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("填写手机号码"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册第二步】
    public boolean isRegisterS2() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("填写验证码"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册第三步】
    public boolean isRegisterS3() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("QQ注册"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册成功】
    public boolean isRegisterSuccess() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("注册成功"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //endregion

    //region 【界面操作】

    //region 【进入qq主界面】
    public void enterQQMain() {
        try {
            while (true) {
                UiObject findText = new UiObject(new UiSelector().className("android.widget.Button")
                        .text("进入QQ"));
                if (findText.exists()) {
                    findText.clickAndWaitForNewWindow();
                    return;
                } else {
                    sleep(1000);
                }
            }
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【进入注册界面】
    public void enterRegister() {
        try {
            UiObject findText = new UiObject(new UiSelector().className("android.widget.Button")
                    .text("新用户"));
            if (findText.exists()) {
                findText.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【注册QQ号第一步】
    public void registerS1(QQDate date) {
        try {
            //设置手机号
            new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0)
                    .childSelector(new UiSelector().className("android.widget.EditText").index(2)
                    )).click();
            String phoneNum = "15050398896";
            if (phoneNum == null || phoneNum.equals("")) {
                return;
            }
            date.setPhoneNum(phoneNum);
            inputText(phoneNum);
            new UiObject(new UiSelector().className("android.widget.Button").text("下一步")).click();
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【注册QQ号第二步】
    public void registerS2() {
        try {
            //设置验证码
            new UiObject(new UiSelector().className("android.widget.EditText")).click();
            //inputText("15050398896");
            new UiObject(new UiSelector().className("android.widget.Button").text("下一步")).click();
            sleep(1000);
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text
                    ("保留原号码的绑定"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【注册QQ号第三步】
    public void registerS3(QQDate date) {
        try {
            //设置昵称
            new UiObject(new UiSelector().className("android.widget.LinearLayout").index(0)
                    .childSelector(new UiSelector().className("android.widget.EditText").index(1)
                    )).click();
            inputText(date.getNick());
            //设置密码
            new UiObject(new UiSelector().className("android.widget.LinearLayout").index(1)
                    .childSelector(new UiSelector().className("android.widget.EditText").index(1)
                    )).click();
            inputText(date.getPassword());
            new UiObject(new UiSelector().className("android.widget.Button").text("注册")).click();
            sleep(1000);
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【获取qq号】
    public void getQQUin(QQDate date) {
        try {
            UiObject button = new UiObject(new UiSelector().className("android.widget.Button")
                    .text("进入QQ"));
            String QQUin = new UiObject(new UiSelector().className("android.widget" +
                    ".RelativeLayout")).getChild(new UiSelector().className("android.widget" +
                    ".FrameLayout")).getChild(new UiSelector().className("android.widget" +
                    ".TextView").index(1)).getText();
            date.setUin(QQUin);
            button.clickAndWaitForNewWindow();
        } catch (Exception ex) {
        }
    }
    //endregion

    //endregion

    //region 【获取任务，提交结果】
    //endregion


    //region 【Utils】
    // 重启微信App
    public void restartApp() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mobileqq/.activity.SplashActivity");
            sleep(5000);
        } catch (Exception ex) {
        }
    }

    // 重启微信App并清理数据
    public void restartAppAndClear() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.clearPackage(PackegName);
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mobileqq/.activity.SplashActivity");
            sleep(5000);
        } catch (Exception ex) {
        }
    }

    public void inputText(String str) {
        for (int i = 0; i < str.length(); i++) {
            ScriptHelper.input(str.substring(i, i + 1));
            sleep(200);
        }
    }
    //endregion

    public void setInfo() {
        try {
            ScriptHelper.setMacAddr(randomName() + "." + randomName() + "." + randomName() + "." +
                    randomName() + "." + randomName());
            ScriptHelper.setWifiName(randomName() + randomName() + randomName());
            ScriptHelper.setGpsValue("129.97087933760199,34.833552100078347");
            ScriptHelper.setImei(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
            ScriptHelper.setBuildSerial(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
            ScriptHelper.setBuildModel("Samsung Exynos 74201");
            ScriptHelper.setAndroidId(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
        } catch (Exception e) {

        }
    }

    private static String randomName() {
        String str = "0123456789abcdefghijklmnopqrstuvwkyz";
        Random r = new Random();
        return str.charAt(r.nextInt(str.length())) + "" + str.charAt(r.nextInt(str.length()));
    }

    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    //endregion
}
