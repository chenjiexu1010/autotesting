package com.micrmsg;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.google.gson.reflect.TypeToken;

import java.util.Random;

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
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Device = getUiDevice();

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
                ScriptHelper.switchImeToAdbKeyboard();
                MicrMsgData data = new MicrMsgData(getCharAndNumr(new Random().nextInt(5)+8), "", getCharAndNumr(new Random().nextInt(5)+6));
                setInfo(data);
                sleep(10000);
                //进入注册界面
                if (isWXMain()) {
                    enterRegister();
                } else {
                }
                sleep(2000);
                //注册微信号
                if (isRegister()) {
                    registerWXAccount(data);
                } else {
                }
                sleep(2000);
                if (isAfterRegister()) {
                    PostInfo(data);
                } else {
                }
                sleep(2000);
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
        ob = new UiObject(new UiSelector().className("android.widget.Button").text("注册"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册界面】
    public boolean isRegister() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("国家/地区"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为注册后界面】
    public boolean isAfterRegister() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("好"));
            while (true) {
                if (ob.exists()) {
                    ob.click();
                    return true;
                }
                sleep(2000);
            }
        }catch (Exception ex){
            return false;
        }
    }
    //endregion

    //endregion

    //region 【界面操作】

    //region 【进入注册界面】
    public void enterRegister() {
        try {
            UiObject findText = new UiObject(new UiSelector().className("android.widget.Button")
                    .text("注册"));
            if (findText.exists()) {
                findText.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【注册微信号】
    public void registerWXAccount(MicrMsgData data) {
        try {
            //设置昵称
            new UiObject(new UiSelector().className("android.widget" + ".LinearLayout").index(0)
                    .childSelector(new UiSelector().className("android.widget.EditText"))).click();
            inputText(Utf7ImeHelper.e(data.getNickName()));

            //设置密码
            new UiObject(new UiSelector().className("android.widget" + ".LinearLayout").index(3)
                    .childSelector(new UiSelector().className("android.widget.EditText"))).click();
            inputText(data.getPassword());
            //设置手机号
            new UiObject(new UiSelector().className("android.widget" + ".LinearLayout").index(2)
                    .childSelector(new UiSelector().className("android.widget.EditText").index(1)
                    )).click();
            //inputText(data.getPhoneNum());
            //点击注册
            /*new UiObject(new UiSelector().className("android.widget.Button")
                    .text("注册")).click();
            sleep(2000);
            new UiObject(new UiSelector().className("android.widget.Button")
                    .text("确定")).click();*/
            ScriptHelper.switchSouGoToAdbKeyboard();
            while (true) {
                if (new UiObject(new UiSelector().className("android.widget.TextView").text
                        ("验证码")).exists()) {
                    String text = new UiObject(new UiSelector().className("android.widget.LinearLayout"))
                            .getChild(new UiSelector().className("android.widget.TextView").index(1)).getText();
                    text = text.replaceAll("\\+86", "").replaceAll(" ","");
                    System.out.println(text);
                    data.setPhoneNum(text);
                    break;
                }
                sleep(2000);
            }
            new UiObject(new UiSelector().className("android.widget.EditText")).click();
            //inputText("888666");
            //new UiObject(new UiSelector().className("android.widget.Button").text("下一步")).click();
        } catch (Exception ex) {
        }
    }
    //endregion

    //endregion

    //region 【获取任务，提交结果】
    //提交数据
    public void PostInfo(MicrMsgData data) {
        try {

            if ((data.getNickName() != null && !data.getNickName().equals("")) || (data.getPhoneNum() != null
                    && !data.getPhoneNum().equals(""))|| (data.getPassword() != null
                    && !data.getPassword().equals(""))) {
                String MicrMsgData = gson.toJson(data, new TypeToken<MicrMsgData>() {
                }.getType());
                System.out.println(MicrMsgData);
                String str = "{\"Url\":\"\",\"DBName\":\"spider\"," +
                        "\"Business\":\"WXAccount\",\"Key\":\"phoneNum\",\"Data\":";
                str = str + MicrMsgData + "}";
                String result = HttpHelper.httpPostForString(POST_INFO, str);
                System.out.println("PostInInfo:" + result);
            }
        } catch (Exception ex) {
            System.out.println("PostInfo>>>>>" + ex.toString());
        }
    }
    //endregion
    //endregion


    //region 【Utils】
    // 重启微信App
    public void restartApp() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.killApp(PackegName + ":push");
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");
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
            jqhelper.killApp(PackegName + ":push");
            jqhelper.clearPackage(PackegName);
            jqhelper.clearPackage(PackegName + ":push");
            sleep(2000);
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");
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

    public void setInfo(MicrMsgData data) {
        try {
            data.setMacAddr(randomName() + "." + randomName() + "." + randomName() + "." +
                    randomName() + "." + randomName());
            data.setWifiName(randomName() + randomName() + randomName());
            data.setGpsValue("129.97087933760199,34.833552100078347");
            data.setImei(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
            data.setBuildSerial(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
            data.setBuildModel("Samsung Exynos 74201");
            data.setAndroidId(randomName() + randomName() + randomName() + randomName() +
                    randomName() + randomName());
            ScriptHelper.setMacAddr(data.getMacAddr());
            ScriptHelper.setWifiName(data.getWifiName());
            ScriptHelper.setGpsValue(data.getGpsValue());
            ScriptHelper.setImei(data.getImei());
            ScriptHelper.setBuildSerial(data.getBuildSerial());
            ScriptHelper.setBuildModel(data.getBuildModel());
            ScriptHelper.setAndroidId(data.getAndroidId());
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
