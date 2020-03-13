package com.micrmsg;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeqee on 2016/1/21.
 */
public class QQvalidate extends SuperRunner {
    private static String GET_INFO = "http://222.185.195.206:8013/get";
    private static String POST_INFO = "http://222.185.195.206:8013/add";
    private static String UPDATE_INFO = "http://222.185.195.206:8013/update";
    private int count = 0;

    //region 【初始化数据】
    private boolean initializtion() {
        ScriptHelper.switchImeToAdbKeyboard();
        jqhelper.reportNormal();
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Device = getUiDevice();
        restartApp();
        try {
            Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
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
                List<String> list = GetQQList();
                if (isAfterLoginMain()) {
                    enterSearch();
                }
                if (isSearch()) {
                    validateQQAndCheck(list);
                }
                restartApp();
            } catch (Exception ex) {
                restartApp();
            }
        }
    }
    //endregion

    //region 【判断界面】

    //region 【是否为登录后主界面面】
    public boolean isAfterLoginMain() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("通讯录"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为搜索界面】
    public boolean isSearch() {
        ob = new UiObject(new UiSelector().className("android.widget.EditText"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //endregion

    //region 【界面操作】

    //region 【注册微信号】
    public void enterSearch() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout")
                    .description("更多功能按钮"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
            new UiObject(new UiSelector().className("android.widget.TextView")
                    .text("添加朋友")).clickAndWaitForNewWindow();
            new UiObject(new UiSelector().className("android.widget.TextView")
                    .text("微信号/QQ号/手机号")).clickAndWaitForNewWindow();
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【验证qq号】
    public void validateQQAndCheck(List<String> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                String Uin = list.get(i);
                System.out.println(Uin);
                count++;
                System.out.println(count);
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton")
                        .description("清除"));
                if (ob.exists()) {
                    ob.click();
                }
                new UiObject(new UiSelector().className("android.widget.EditText")).click();
                inputText(Uin);
                new UiObject(new UiSelector().className("android.widget.TextView").index(1))
                        .click();

                sleep(1000);
                UiObject ob1 = new UiObject(new UiSelector().className("android.widget.TextView")
                        .text("用户不存在"));
                UiObject ob2 = new UiObject(new UiSelector().className("android.widget.TextView")
                        .text("详细资料"));
                UiObject ob3 = new UiObject(new UiSelector().className("android.widget.TextView")
                        .text("操作过于频繁，请稍后再试。"));
                UiObject ob4 = new UiObject(new UiSelector().className("android.widget.TextView")
                        .text("被搜帐号状态异常，无法显示。"));
                if (ob1.exists()) {
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    UpdateInfo(Uin, 0);
                } else if (ob2.exists()) {
                    new UiObject(new UiSelector().className("android.widget.ImageView").description
                            ("返回")).click();
                    UpdateInfo(Uin, 1);
                } else if (ob3.exists()) {
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    UpdateInfo(Uin, 1);
                } else if (ob4.exists()) {
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    UpdateInfo(Uin, 1);
                }
            }
        } catch (Exception ex) {
            System.out.println("validateQQAndCheck>>>>>" + ex.toString());
        }
    }
    //endregion

    //endregion

    //region 【获取任务，提交结果】
    //提交数据
    public void UpdateInfo(String Uin, int Status) {
        try {
            String str = "{\"Url\":\"\",\"DBName\":\"QQ\"," +
                    "\"Business\":\"DetailSheet\",\"Key\":\"Uin\"," +
                    "\"Data\":{\"Uin\":\"" + Uin + "\",\"IsExists\":" + Status + "}}";
            String result = HttpHelper.httpPostForString(UPDATE_INFO, str);
            System.out.println("PostQQInfo:" + result);
        } catch (Exception ex) {
            System.out.println("PostInfo>>>>>" + ex.toString());
        }
    }
    //endregion

    //提交数据
    public List<String> GetQQList() {
        List<String> dateList = new ArrayList<String>();
        try {
            String str = "{\"Url\":\"\",\"DBName\":\"QQ\",\"Business\":\"DetailSheet\"," +
                    "\"Key\":\"Uin\",\"LockKey\":\"IsExists\",\"Data\":{\"IsExists\":null}," +
                    "\"Limit\":10,\"Select\":{\"Uin\":1}}";
            System.out.println(1);
            String result = HttpHelper.httpPostForString(GET_INFO, str);
            Matcher m = Pattern.compile("\"Uin\":\"(.*?)\"").matcher(result);
            while (m.find()) {
                String uin = m.group(1);
                dateList.add(uin);
            }
            System.out.println(result);
            return dateList;
        } catch (Exception ex) {
            System.out.println("GetQQList>>>>>" + ex.toString());
            return dateList;
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
