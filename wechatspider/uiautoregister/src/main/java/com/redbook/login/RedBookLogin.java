package com.redbook.login;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.jutf7.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RedBookLogin extends SuperRunner {

    //账号登录
    private static String accountListPath = "/storage/emulated/0/QX_Backup/shellcmd/account.txt";
    //记录执行到哪个账号   账号开始是0    备份开始名称为1
    private static String executePath = "/storage/emulated/0/QX_Backup/shellcmd/number.txt";
    //记录备份到哪里
    private static String backPath = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //获取当前执行的账号信息
    private static String accountInfo = "";
    //执行到账号索引
    private static int start = 0;
    private static int num_start = 0;
    private static int account_length = 0;

    //启动方法
    public void testRunner() {
        try {
            // 短信弹窗判断
            ShortMessageJudge();
            //设置快捷输入法
            change_input_method();
            Device = getUiDevice();
            // 加载账号
            String accountInfo = LoadAccount();
            String number = readFileContent(backPath);
            num_start = Integer.parseInt(number);
            while (true) {
                if (accountInfo.equals("")) {
                    jqhelper.writeSDFileEx("获取账号为空。", "/sdcard/error.txt");
                    jqhelper.delay(2000);
                    continue;
                }
                String account = accountInfo.split("\\s+")[0];
                String password = accountInfo.split("\\s+")[1];
                jqhelper.writeSDFileEx2(account + " " + password, "/sdcard/error.txt");
                if (account.equals("") || password.equals("")) {
                    // 记录执行情况
                    start += 1;
                    saveAsFileWriter(executePath, start + "");
                    continue;
                }
                // 关闭变机大师
                close_bj();
                boolean isSuccess = RedBookLogin(account, password);
                if (isSuccess) {
                    // 关闭小红书
                    close_xhs();
                    if (bj_backups()) {
                        close_bj();
                        if (start >= account_length) {
                            jqhelper.writeSDFileEx("执行结束" + " \n", "/sdcard/error.txt");
                            break;
                        }
                    }
                    // 清除
                    clear_data();
                    // 一键变机
                    changeSystem();
                } else {
                    jqhelper.writeSDFileEx("小红书登录账号失败" + "\n", "/sdcard/error.txt");
                }
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx(e.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 小红书登录
     *
     * @param account
     * @param passWord
     * @return
     */
    private boolean RedBookLogin(String account, String passWord) {
        try {
            // 打开小红书
            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            sleep(5000);
            // 同意按钮
            UiObject agree_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意"));
            if (agree_btn.waitForExists(5000)) {
                agree_btn.click();
                sleep(2000);
            }
            UiObject cancel_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("取消"));
            if (cancel_btn.waitForExists(5000)) {
                cancel_btn.click();
                sleep(2000);
            }
            //点击微博授权登录
            UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
            while (true) {
                UiObject noResponse = new UiObject(new UiSelector().className("android.widget.Button").text("等待"));
                if (noResponse.waitForExists(2000)) {
                    noResponse.click();
                    sleep(2000);
                }
                if (weibo_btn.waitForExists(2000)) {
                    weibo_btn.click();
                    sleep(10000);
                    break;
                } else {
                    jqhelper.delay(2000);
                }
            }
            // 输入密码
            UiObject user_name = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginName"));
            if (user_name.waitForExists(2000)) {
                user_name.setText(Utf7ImeHelper.e(account));
                sleep(1000);
            }
            // 输入密码
            UiObject pass_word = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginPassword"));
            if (pass_word.waitForExists(2000)) {
                pass_word.setText(Utf7ImeHelper.e(passWord));
                sleep(1000);
            }
            // 点击登录
            Device.click(545, 1162);
            sleep(5000);
            //点击确认
            UiObject sure_btn = new UiObject(new UiSelector().className("android.view.View").text("确认"));
            if (sure_btn.waitForExists(10000)) {
                sure_btn.click();
                sleep(2000);
            }
            while (true) {
                try {
                    sleep(5000);
                    UiObject home_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("首页"));
                    if (home_btn.waitForExists(2000)) {
                        return true;
                    }
                } catch (Exception e) {
                    jqhelper.writeSDFileEx(e.toString(), "/sdcard/error.txt");
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 修改输入法
     *
     * @throws IOException
     */
    private void change_input_method() throws IOException {
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
        jqhelper.delay(1000);
    }

    /**
     * 关闭小红书
     *
     * @throws IOException
     */
    private void close_xhs() throws IOException {
        // 关闭变机大师
        Runtime.getRuntime().exec("am force-stop com.xingin.xhs");
        jqhelper.delay(1000);
    }

    /**
     * 变机大师(一键变机)
     *
     * @return
     */
    private boolean changeSystem() {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            // 通过shell命令打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(8000);
            // 点击更多
            UiObject more_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("更多选项"));
            more_btn.clickAndWaitForNewWindow(3000);
            sleep(2000);
            // 点击一键变机
            UiObject change_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键变机"));
            if (change_btn.exists()) {
                change_btn.clickAndWaitForNewWindow(2000);
                sleep(2000);
                // 点击确认变机
                UiObject sure_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                if (sure_btn.exists()) {
                    sure_btn.clickAndWaitForNewWindow(2000);
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常changeSystem()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 加载账号
     *
     * @return
     */
    public String LoadAccount() {
        try {
            ArrayList<String> accountList = readFileContent2(accountListPath);
            account_length = accountList.size();
            if (accountList.size() <= 0) {
                return "";
            }
            String text = readFileContent(executePath);
            start = Integer.parseInt(text);
            accountInfo = accountList.get(start);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
        return accountInfo;
    }

    /**
     * 关闭变机大师
     *
     * @throws IOException
     */
    private void close_bj() throws IOException {
        // 关闭变机大师
        Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
        jqhelper.delay(1000);
    }

    /**
     * 变机大师(一键备份)
     *
     * @return
     */
    private boolean bj_backups() {
        try {
            String text = readFileContent(backPath);
            int number = Integer.parseInt(text);
            // 打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(10000);
            UiObject change_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("小红书"));
            if (change_btn.waitForExists(10000)) {
                change_btn.click();
                sleep(2000);
            }
            // 点击确认变机
            UiObject sure_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("云端备份"));
            if (sure_btn.waitForExists(2000)) {
                sure_btn.click();
                sleep(5000);
            }

            UiObject ed = new UiObject(new UiSelector().className("android.widget.EditText"));
            if (ed.waitForExists(10000)) {
                ed.setText(Utf7ImeHelper.e(number + ""));
                sleep(3000);
            }
            UiObject ed2 = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (ed2.waitForExists(10000)) {
                ed2.click();
                sleep(10000);
            }
            while (true) {
                UiObject ed3 = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                if (ed3.waitForExists(10000)) {
                    ed3.click();
                    sleep(10000);
                    break;
                }
                sleep(10000);
            }

            // 记录执行情况
            start += 1;
            num_start += 1;
            saveAsFileWriter(executePath, start + "");
            saveAsFileWriter(backPath, num_start + "");
//            saveAsFileWriter(filePath, num_start + "");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return true;
    }

    /**
     * 清除小红书应用数据
     *
     * @throws IOException
     */
    private void clear_data() throws IOException {
        // 关闭app
        Runtime.getRuntime().exec("am force-stop com.xingin.xhs");
        jqhelper.delay(2000);
        // 清除app数据
        Runtime.getRuntime().exec("pm clear com.xingin.xhs");
        jqhelper.delay(4000);
    }

    /**
     * 修改手机文件内容
     *
     * @param filePath1
     * @param content
     */
    private static void saveAsFileWriter(String filePath1, String content) {
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath1);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 读取手机文件目录
     *
     * @param filePath1
     * @return
     */
    public static ArrayList<String> readFileContent2(String filePath1) {
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(filePath1);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                arrayList.add(tempStr);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    /**
     * 读取手机文件目录
     *
     * @param filePath1
     * @return
     */
    public static String readFileContent(String filePath1) {
        File file = new File(filePath1);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    /**
     * 重启弹窗判断
     */
    private void ShortMessageJudge() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("关闭应用"));
            if (ob.waitForExists(2000)) {
                ob.click();
                jqhelper.delay(2000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }
}
