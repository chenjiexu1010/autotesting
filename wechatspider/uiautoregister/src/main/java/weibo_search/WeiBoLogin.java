package weibo_search;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.jutf7.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WeiBoLogin extends SuperRunner {
    //账号登录
    private static String accountListPath = "/storage/emulated/0/QX_Backup/shellcmd/account.txt";
    //记录执行到哪个账号   账号开始是0    备份开始名称为1
    private static String executePath = "/storage/emulated/0/QX_Backup/shellcmd/number.txt";
    //当前执行账号信息
    private static String accountInfo = "";
    //执行到账号索引
    private static int start = 0;
    private static int account_length = 0;

    /**
     * 执行主体
     */
    public void test_login() {
        ShortMessageJudge();
        while (true) {
            try {
                //获取账号
                String accountInfo = LoadAccount();
                if (accountInfo.equals("")) {
                    // 一键变机
                    start += 1;
                    saveAsFileWriter(executePath, start + "");
                    changeSystem();
                }
                String account = accountInfo.split("\\s+")[0];
                String password = accountInfo.split("\\s+")[1];
                // 暂时关闭变机大师
                close_bj();
                boolean isSuccess = WeiBoLogin(account, password);
                if (isSuccess) {
                    if (bj_backups()) {
                        close_bj();
                        if (start >= account_length) {
                            jqhelper.writeSDFileEx("执行结束" + " \n", "/sdcard/error.txt");
                            break;
                        }
                    }
                    // 一键变机
                    changeSystem();
                }
                jqhelper.writeSDFileEx("登录失败" + " \n", "/sdcard/error.txt");
            } catch (Exception ex) {
                jqhelper.writeSDFileEx(ex + " \n", "/sdcard/error.txt");
            }
        }
    }

    /**
     * 微博登录
     *
     * @param account
     * @param password
     */
    private boolean WeiBoLogin(String account, String password) {
        try {
            //设置快捷输入法
            change_input_method();
            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
            sleep(8000);
            ShortMessageJudge();
            UiObject open_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("\t\t开启\t\t"));
            if (open_btn.exists()) {
                open_btn.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }
            // 点击允许两次
            UiObject allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.click();
                sleep(3000);
                allow_btn.click();
                sleep(3000);
            }
            // 点击账号密码登录
            UiObject login_btn = new UiObject(new UiSelector().className("android.widget.Button").text("用帐号密码登录"));
            if (login_btn.exists()) {
                login_btn.click();
                sleep(2000);
            }
            // 输入账号
            UiObject user_name = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.sina.weibo:id/et_pws_username"));
            if (user_name.exists()) {
                user_name.setText(Utf7ImeHelper.e(account));
                sleep(1000);
            }
            // 输入密码
            UiObject pass_word = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.sina.weibo:id/et_pwd"));
            if (pass_word.exists()) {
                pass_word.setText(Utf7ImeHelper.e(password));
                sleep(1000);
            }
            //点击登录
            UiObject login = new UiObject(new UiSelector().className("android.widget.Button").text("登录"));
            if (login.exists()) {
                login.click();
                sleep(2000);
            }
            // 判断是否存在验证身份
            UiObject verify_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("请先验证身份"));
            if (verify_btn.waitForExists(10000)) {
                sleep(30000);
            }
            while (true) {
                try {
                    sleep(10000);
                    UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").description("发现"));
                    if (search_btn.waitForExists(10000)) {
                        break;
                    }
                    UiObject jump_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("跳过"));
                    if (jump_btn.waitForExists(2000)) {
                        // 点击两次跳过
                        jump_btn.clickAndWaitForNewWindow(2000);
                        sleep(5000);
                        jump_btn.clickAndWaitForNewWindow(2000);
                    }
                    // 进入首页,判断是否有战役打卡行动弹窗
                    UiObject card_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
                    if (card_btn.exists()) {
                        card_btn.clickAndWaitForNewWindow(2000);
                        sleep(8000);
                    }
                    // 进入首页,判断是否有今日签到弹窗
                    UiObject close_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/iv_close"));
                    if (close_btn.waitForExists(2000)) {
                        close_btn.clickAndWaitForNewWindow(2000);
                        sleep(8000);
                    }
                    UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
                    if (update_btn.waitForExists(2000)) {
                        update_btn.click();
                        sleep(2000);
                    }
                    if (search_btn.waitForExists(10000)) {
                        break;
                    }
                    if (login.waitForExists(10000)) {
                        // 清除微博缓存
                        clear_data();
                        // 记录执行情况
                        start += 1;
                        saveAsFileWriter(executePath, start + "");
//                            saveAsFileWriter(filePath, num_start + "");
                        changeSystem();
                    }
                    sleep(10000);
                } catch (Exception ex) {
                    clear_data();
                    jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
                    changeSystem();
                    break;
                }
            }
            // 关闭微博
            close_weibo();
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return true;
    }

    /**
     * 关闭微博
     *
     * @throws IOException
     */
    private void close_weibo() throws IOException {
        // 关闭变机大师
        Runtime.getRuntime().exec("am force-stop com.sina.weibo");
        jqhelper.delay(1000);
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
            // 打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(15000);
            UiObject change_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
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
                ed.setText(Utf7ImeHelper.e("wbyanghao01 "));
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
            // 清除微博缓存
            clear_data();
            // 记录执行情况
            start += 1;
//            num_start += 1;
            saveAsFileWriter(executePath, start + "");
//            saveAsFileWriter(filePath, num_start + "");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return true;
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
     * 清除新浪微博数据
     *
     * @throws IOException
     */
    private void clear_data() throws IOException {
        // 关闭app
        Runtime.getRuntime().exec("am force-stop com.sina.weibo");
        jqhelper.delay(2000);
        // 清除app数据
        Runtime.getRuntime().exec("pm clear com.sina.weibo");
        jqhelper.delay(4000);
    }


    /**
     * 短信弹窗判断
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
     * 加载文件第一个账号
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
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return accountInfo;
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

}
