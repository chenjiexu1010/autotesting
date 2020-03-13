package com.inSocial;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.google.gson.reflect.TypeToken;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeqee on 2015/12/22.
 */
public class RegisterRunner extends SuperRunner {
    private static String GET_INFO = "http://222.185.195.206:8010/get";
    private static String POST_INFO = "http://222.185.195.206:8010/add";
    private static String UPDATE_INFO = "http://222.185.195.206:8010/update";
    //region 【初始化数据】
    private boolean initializtion() {
        jqhelper.reportNormal();
        PackegName = "com.jiuyan.infashion";
        ActivityName = "login.MainActivity";
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
                setInfo();
                boolean isRegistered = false;
                InData inData = new InData();
                getQQInfo(inData);
                sleep(10000);
                //进入主页面
                enterMain();
                sleep(2000);
                //进入注册界面
                if (isWXMain()) {
                    enterRegister();
                    sleep(2000);
                } else {
                }
                //注册in号
                registerInAccount(inData);
                sleep(5000);
                //验证码验证
                if (isCode()) {
                    importCode(inData);
                    sleep(2000);
                }
                isRegistered = isAfterLoginMain();
                System.out.println(isRegistered);
                //设置基本信息
                //if (isSetInfo()) {
                sleep(5000);
                setInfo(inData);
                //}
                sleep(2000);
                //提交数据
                PostInfo(inData, isRegistered);
                //重启app
                restartAppAndClear();
            } catch (Exception ex) {

            }
        }
    }
    //endregion

    //region 【判断界面】

    //region 【是否为微信首页】
    public boolean isWXMain() {
        try {
            UiObject update = new UiObject(new UiSelector().className("android.widget.Button")
                    .text("以后再说"));
            if (update.exists()) {
                update.clickAndWaitForNewWindow();
                sleep(2000);
            }
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("登录"));
            if (!ob.exists()) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
    //endregion

    //region 【是否为验证码界面】
    public boolean isCode() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("输入验证码"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为信息设置界面】
    public boolean isSetInfo() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("填写资料"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为登录后主界面】
    public boolean isAfterLoginMain() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("发现"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【界面操作】

    //region 【进入主界面】
    public void enterMain() {
        try {
            UiScrollable scrollable = new UiScrollable(new UiSelector().className("android" +
                    ".webkit.WebView"));
            scrollable.setAsHorizontalList();
            scrollable.scrollForward();
            scrollable.scrollForward();
            scrollable.scrollForward();
            scrollable.scrollForward();
            sleep(1000);
            Device.click(Device.getDisplayWidth() / 2, (int) (Device.getDisplayHeight() * 0.85));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    //endregion

    //region 【进入注册界面】
    public void enterRegister() {
        try {
            UiObject findText = new UiObject(new UiSelector().className("android.widget.TextView")
                    .text("QQ"));
            if (findText.exists()) {
                findText.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            System.out.println("enterRegister>>>>>" + ex.toString());
        }
    }
    //endregion

    //region 【注册In号】
    public void registerInAccount(InData data) {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("切换帐号"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
            sleep(1000);
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("添加帐号"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(1))
                    .getChild(new UiSelector().className("android.widget.EditText")).click();
            inputText(data.getQQ());
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(3))
                    .getChild(new UiSelector().className("android.widget.EditText")).click();
            ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index
                    (3)).getChild(new UiSelector().className("android.widget.RelativeLayout")
                    .index(0)).getChild(new UiSelector().className("android.view.View").index(0));
            if (ob.exists()) {
                ob.click();
            }
            inputText(data.getQQPass());
            new UiObject(new UiSelector().className("android.widget.Button").text("登 录"))
                    .clickAndWaitForNewWindow();
            if (new UiObject(new UiSelector().className("android.widget.TextView").text("登录失败"))
                    .exists()) {
                new UiObject(new UiSelector().className("android.widget.TextView").text("确定"))
                        .click();
            }
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("授权并登录"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            System.out.println("registerInAccount>>>>>" + ex.toString());
        }
    }
    //endregion

    //region 【输入验证码】
    public void importCode(InData data) {
        try {
            UiObject image = new UiObject(new UiSelector().className("android.widget.ImageView")
                    .index(0));
            if (!image.exists()) {
                return;
            }
            String captchastr = ScriptHelper.LoginCaptchaTap(345, 261, 735, 420).getCode();//
            //Bitmap me = ScriptHelper.screenShot();
            //Bitmap he = ScriptHelper.cutImage(me, 345, 261, 735, 420);
            //ScriptHelper.saveBitmapToSDCard(he,"/sdcard/1.png");
            // captcha.getCode();
            if (captchastr.isEmpty()) {
                image.click();
                captchastr = ScriptHelper.LoginCaptchaTap(345, 261, 735, 420).getCode();//
                // captcha.getCode();
                if (captchastr.isEmpty()) {
                    // 验证码两次验证失败，提示退出
                    return;
                }
            }
            System.out.println(captchastr);
            new UiObject(new UiSelector().className("android.widget.EditText").index(2)).click();
            inputText(captchastr);
            new UiObject(new UiSelector().className("android.widget.TextView").text("完成"))
                    .clickAndWaitForNewWindow();
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("授权并登录"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("登录失败"));
            if (ob.exists()) {
                new UiObject(new UiSelector().className("android.widget.TextView").text("确定"))
                        .clickAndWaitForNewWindow();
                System.out.println("LoginError");
                PostError(data.getQQ());
            }
        } catch (Exception ex) {
            System.out.println("importC0ode>>>>>" + ex.toString());
        }
    }
    //endregion

    //region 【设置基本信息】
    public void setInfo(InData data) {
        try {
            // 设置昵称
            data.setNick(getCharAndNumr(new Random().nextInt(5) + 8));
            //region 跳过设置
            ob = new UiObject(new UiSelector().className("android.widget.EditText").text("昵称（必填）"));
            if (ob.exists()) {
                System.out.println("昵称");
                ob.click();
                inputText(data.getNick());
                new UiObject(new UiSelector().className("android.widget.TextView").text("注册"))
                        .clickAndWaitForNewWindow();
                sleep(1000);
            }
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("设置账号密码"));
            if (ob.exists()) {
                new UiObject(new UiSelector().className("android.widget.TextView").text("跳过"))
                        .clickAndWaitForNewWindow();
            }
            sleep(2000);
            Device.click(Device.getDisplayWidth() / 3, (int) (Device.getDisplayHeight() * 0.5));
            sleep(2000);
            System.out.println("F");
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("进入"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
                new UiObject(new UiSelector().className("android.widget.Button").text("确认进入"))
                        .clickAndWaitForNewWindow();
            }
            sleep(2000);
           /* Device.click(Device.getDisplayWidth() / 2, (int) (Device.getDisplayHeight() * 0.5));
            sleep(2000);*/
            /* new UiObject(new UiSelector().className("android.widget.TextView").text("直接进入 >"))
                    .clickAndWaitForNewWindow();*/
            UiObject update = new UiObject(new UiSelector().className("android.widget.Button")
                    .text("以后再说"));
            if (update.exists()) {
                update.clickAndWaitForNewWindow();
                sleep(2000);
            }
           /* new UiObject(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(1))
                    .click();
            sleep(2000);
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(7))
                    .click();
            sleep(2000);*/
            /*ob = new UiObject(new UiSelector().className("android.widget.TextView").text("玩字"));
            if (ob.exists()) {
                ob.click();
            }
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text
                    ("我也要这样记录"));
            if (ob.exists()) {
                ob.click();
                sleep(2000);
            }
            ob = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(3))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageButton").index(0));
            if (ob.exists()) {
                ob.click();
                sleep(2000);
            }*/
            //endregion
            Device.click(Device.getDisplayWidth() / 2, (int) (Device.getDisplayHeight() * 0.5));
            sleep(2000);
            new UiObject(new UiSelector().className("android.widget.TextView").text("中心"))
                    .clickAndWaitForNewWindow();
            sleep(2000);
            String inNum = new UiObject(new UiSelector().className("android.widget.ScrollView"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout").index
                            (0)).getChild(new UiSelector().className("android.widget.TextView")
                            .index(2)).getText();
            inNum = inNum.substring(inNum.indexOf(":") + 1);
            System.out.println(inNum);
            data.setInNum(inNum);
            UiScrollable scrollable = new UiScrollable(new UiSelector().className("android.widget" +
                    ".ScrollView"));
            scrollable.scrollForward();
            new UiObject(new UiSelector().className("android.widget.TextView").text("设置"))
                    .clickAndWaitForNewWindow();
            new UiObject(new UiSelector().className("android.widget.TextView").text("账号与安全"))
                    .clickAndWaitForNewWindow();
            if (new UiObject(new UiSelector().className("android.widget.TextView").text("未设置"))
                    .exists()) {
                data.setInPass(getCharAndNumr(10));
                new UiObject(new UiSelector().className("android.widget.TextView").text("登录密码"))
                        .clickAndWaitForNewWindow();
                new UiObject(new UiSelector().className("android.widget.EditText")).click();
                inputText(data.getInPass());
                new UiObject(new UiSelector().className("android.widget.TextView").text("完成"))
                        .clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            System.out.println("setInfo>>>>>" + ex.toString());
        }
    }
    //endregion

    //endregion

    //region 【获取任务，提交结果】
    //获取qq数据
    public static void getQQInfo(InData data) {
        try {
            String str = "{\"Url\":\"1\",\"DBName\":\"QQAccount\",\"Business\":\"Account\"," +
                    "\"Key\":\"Uin\"," +
                    "\"LockKey\":\"inFlag\",\"Data\":{\"Status\":{\"$in\":[1,2,4]}," +
                    "\"inFlag\":null},\"Limit\":1,\"Select\":{\"Uin\":1,\"pass\":1}}";
            String QQInfo = HttpHelper.httpPostForString(GET_INFO, str);
            if (QQInfo == null || QQInfo.equals("")) {
                QQInfo = HttpHelper.httpPostForString(GET_INFO, str);
            }
            Matcher m = Pattern.compile("\"Uin\":\"(.*?)\",.*?,\"pass\":\"(.*?)\"").matcher(QQInfo);
            while (m.find()) {
                String uin = m.group(1);
                String pass = m.group(2);
                data.setQQ(uin);
                data.setQQPass(pass);
            }
        } catch (Exception ex) {
            System.out.println("getQQInfo>>>>>" + ex.toString());
        }
    }

    //提交数据
    public void PostInfo(InData data, boolean b) {
        try {

            if ((data.getInPass() != null && !data.getInPass().equals("")) || (data.getInNum() != null
                    && !data.getInNum().equals(""))) {
                String Indata = gson.toJson(data, new TypeToken<InData>() {
                }.getType());
                System.out.println(Indata);
                String str = "{\"Url\":\"\",\"DBName\":\"IN\"," +
                        "\"Business\":\"insocialAccount\",\"Key\":\"InNum\",\"Data\":";
                str = str + Indata + "}";
                String result = HttpHelper.httpPostForString(POST_INFO, str);
                System.out.println("PostInInfo:" + result);
            } else {
                if (!b) {
                    String str = "{\"Url\":\"1\",\"DBName\":\"QQAccount\"," +
                            "\"Business\":\"Account\",\"Key\":\"Uin\"," +
                            "\"Data\":{\"Uin\":\"" + data.getQQ() + "\",\"inFlag\":-2}}";
                    String result = HttpHelper.httpPostForString(UPDATE_INFO, str);
                    System.out.println("PostQQInfo:" + result);
                }
            }
        } catch (Exception ex) {
            System.out.println("PostInfo>>>>>" + ex.toString());
        }
    }
    //endregion

    //账号异常
    public void PostError(String QQ) {
        try {
            String str = "{\"Url\":\"1\",\"DBName\":\"QQAccount\"," +
                    "\"Business\":\"Account\",\"Key\":\"Uin\"," +
                    "\"Data\":{\"Uin\":\"" + QQ + "\",\"Status\":3101}}";
            String result = HttpHelper.httpPostForString(UPDATE_INFO, str);
            System.out.println("PostQQError:" + result);
        } catch (Exception ex) {
            System.out.println("PostError>>>>>" + ex.toString());
        }
    }
    //endregion


    //region 【Utils】
    // 重启微信App
    public void restartApp() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.killApp("com.tencent.mobileqq");
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");
            sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    // 重启微信App并清理数据
    public void restartAppAndClear() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.clearPackage(PackegName);
            jqhelper.killApp("com.tencent.mobileqq");
            jqhelper.clearPackage("com.tencent.mobileqq");
            sleep(2000);
            ScriptHelper.startActivity(PackegName + "/." + ActivityName);
            sleep(5000);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void inputText(String str) {
        for (int i = 0; i < str.length(); i++) {
            ScriptHelper.input(str.substring(i, i + 1));
            sleep(200);
        }
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
    //endregion
}
