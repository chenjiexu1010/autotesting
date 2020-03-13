package com.redbookedit;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.jutf7.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RedbookLogin extends UiAutomatorTestCase {

    private static String account = "";
    private static String password = "";
    //记录执行到哪个账号   账号开始是0    备份开始名称为1
    private static String executePath = "/storage/emulated/0/QX_Backup/shellcmd/number.txt";
    //记录备份到哪里
    private static String filePath = "/storage/emulated/0/QX_Backup/shellcmd/shellstatus.txt";
    //执行到账号索引
    private static int start = 0;

    public void testMain() {
        try {
//            while (true) {
//                boolean flag = RedBookLogin("13569677234", "f68302690c");
//                if (flag){
//                    if (bj_backups()) {
//                        close_bj();
//                    }
//                    changeSystem();
//                }
//            }
            RedBookLogin("185/5195/2167", "APTX4869");
        } catch (Exception e) {

        }
    }

    /**
     * 小红书登录
     *
     * @param account
     * @param password
     * @return
     */
    private boolean RedBookLogin(String account, String password) {
        try {
            UiDevice device = getUiDevice();
            // 设置快捷输入法
            change_input_method();
            // 打开小红书
            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            sleep(8000);
            // 同意按钮
            UiObject agree_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意"));
            if (agree_btn.exists()) {
                agree_btn.click();
                sleep(5000);
            }
            // 点击微博登录
//            UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
//            if (weibo_btn.waitForExists(3000)) {
//                weibo_btn.click();
//                sleep(2000);
//            }
            // 点击手机号登录
            UiObject phone_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("手机号登录"));
            if (phone_btn.waitForExists(3000)) {
                phone_btn.clickAndWaitForNewWindow(3000);
            }
            // 点击其他号码登录
            UiObject other_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("其他号码登录"));
            if (other_btn.waitForExists(3000)) {
                other_btn.click();
                sleep(5000);
            }
            // 点击密码登录
            UiObject psw_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("密码登录"));
            if (psw_btn.waitForExists(3000)) {
                psw_btn.click();
                sleep(3000);
            }
            // 输入账号
            UiObject account_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.xingin.xhs:id/b5e"));
            if (account_input.waitForExists(2000)) {
                account_input.setText(account);
                sleep(1000);
            }
            // 输入密码
            UiObject password_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.xingin.xhs:id/b6b"));
            if (password_input.waitForExists(3000)) {
                password_input.setText(password);
                sleep(1000);
            }
            // 点击登录
            UiObject login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意协议并登录"));
            if (login_btn.exists()) {
                login_btn.click();
                sleep(2000);
            }
            // 点击确认
            UiObject confirm_btn = new UiObject(new UiSelector().className("android.view.View").text("确认"));
            if (confirm_btn.waitForExists(2000)) {
                confirm_btn.click();
                sleep(2000);
            }
            sleep(3000);
            UiObject home_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("首页"));
            if (home_btn.waitForExists(10000)) {
                home_btn.click();
            }

            // 关闭小红书
            close_xhs();

        } catch (Exception e) {
            jqhelper.writeSDFileEx("登录异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
        }
        return true;
    }

    /**
     * 变机大师（一键备份）
     *
     * @return
     */
    private boolean bj_backups() {
        try {
            // 获取名称
            String num = readFileContent(filePath);
            // 打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(15000);
            // 点击小红书
            UiObject xhs_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("小红书"));
            if (xhs_btn.waitForExists(2000)) {
                xhs_btn.click();
                sleep(2000);
            }
            // 点击备份
            UiObject backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("云端备份"));
            if (backup_btn.waitForExists(2000)) {
                backup_btn.click();
                sleep(2000);
            }
            // 填写备份名
            UiObject name_input = new UiObject(new UiSelector().className("android.widget.EditText"));
            if (name_input.waitForExists(2000)) {
                name_input.setText(Utf7ImeHelper.e(num));
                sleep(2000);
            }
            // 点击确定
            UiObject nameConfirm_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (nameConfirm_btn.waitForExists(2000)) {
                nameConfirm_btn.click();
                sleep(2000);
            }
            sleep(10000);
            UiObject confirm_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (confirm_btn.waitForExists(2000)) {
                confirm_btn.click();
                sleep(10000);
            }
            // 清除小红书数据缓存
            clear_data();
            // 记录执行情况
            start += 1;
            saveAsFileWriter(executePath, start + "");

        } catch (Exception e) {
            jqhelper.writeSDFileEx("备份异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
        }
        return true;
    }

    private boolean changeSystem() {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            // 打开变机大师
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
                // 点击确定
                UiObject confirm_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                if (change_btn.exists())
                    confirm_btn.clickAndWaitForNewWindow(2000);
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx("变机异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
        }
        return true;
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
}
