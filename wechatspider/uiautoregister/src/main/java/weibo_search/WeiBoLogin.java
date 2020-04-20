package weibo_search;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.jutf7.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WeiBoLogin extends SuperRunner {
    // TODO 服务器重启需要重新切换ip地址
    private static String getAccountUrl = "http://180.116.110.84:5003/getaccount/";
    //当前执行账号信息
    private static String accountInfo = "";
    //执行到账号索引
    private static int start = 0;

    /**
     * 执行主体
     */
    public void test_login() {
        Device = getUiDevice();
//        changeSystem();
        while (true) {
            try {
                close_bj();
                ShortMessageJudge();
                // 暂时关闭变机大师
                boolean isSuccess = WeiBoLogin();
                close_weibo();
                if (isSuccess) {
                    // 备份
                    bj_backups();
//                    close_bj();
                } else {
                    // 一键变机
                    close_weibo();
                    changeSystem();
                }
                changeSystem();
            } catch (Exception ex) {
                jqhelper.writeSDFileEx(ex + " \n", "/sdcard/error.txt");
            }
        }
    }

    /**
     * 微博登录
     */
    private boolean WeiBoLogin() {
        try {
            //设置快捷输入法
            change_input_method();
            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
            sleep(20000);
            Device.pressHome();
            sleep(5000);
            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
            sleep(2000);
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
            //获取账号
            String accountInfo = GetAccountServer(getAccountUrl);
            if (accountInfo.equals("")) {
                jqhelper.writeSDFileEx("获取账号为空" + " \n", "/sdcard/error.txt");
                return false;
            }
            String account = accountInfo.split("\\s+")[0];
            String password = accountInfo.split("\\s+")[1];
            System.out.println("账号:" + account + " 密码:" + password);
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
            // TODO 尚未注册微博
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("立即注册"));
            if (!ob.waitForExists(2000)) {
                UiObject verify_btn = new UiObject(new UiSelector().className("android.view.View").text("点击按钮进行验证"));
                if (verify_btn.waitForExists(30000)) {
                    System.out.println("存在点击按钮进行验证");
                    jqhelper.delay(5000);
                    Device.click(498, 455);
                    System.out.println("点击按钮进行验证已点击");
                    sleep(5000);
                }
                ob = new UiObject(new UiSelector().className("android.view.View").text("拖动滑块完成拼图"));
                if (ob.waitForExists(5000)) {
                    while (true) {
                        if (ob.waitForExists(2000)) {
                            System.out.println("存在滑块验证码");
                            jqhelper.delay(20000);
                            continue;
                        }
                        jqhelper.delay(5000);
                        if (!ob.waitForExists(2000)) {
                            Device.pressHome();
                            jqhelper.delay(2000);
                            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
                            System.out.println("滑块验证码不存在");
                            if (login.waitForExists(2000)) {
                                return false;
                            }
                            UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").description("发现"));
                            if (search_btn.waitForExists(2000)) {
                                System.out.println("登录成功-存在发现按钮");
                                return true;
                            }
                            // TODO  点击两次跳过
                            Device.click(965, 149);
                            jqhelper.delay(5000);
                            ob = new UiObject(new UiSelector().className("android.widget.Button").text("登录/注册"));
                            if (ob.waitForExists(2000)) {
                                System.out.println("登录注册存在-登录失败");
                                return false;
                            }
                            Device.click(965, 149);
                            jqhelper.delay(5000);
                            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("写微博"));
                            if (ob.waitForExists(2000)) {
                                Device.pressBack();
                                return true;
                            }
                            // 进入首页,判断是否有战役打卡行动弹窗
                            UiObject card_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
                            if (card_btn.exists()) {
                                card_btn.clickAndWaitForNewWindow(2000);
                                sleep(2000);
                            }
                            // 进入首页,判断是否有今日签到弹窗
                            UiObject close_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/iv_close"));
                            if (close_btn.waitForExists(2000)) {
                                close_btn.clickAndWaitForNewWindow(2000);
                                sleep(2000);
                            }
                            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
                            if (update_btn.waitForExists(2000)) {
                                update_btn.click();
                                sleep(2000);
                            }
                            if (search_btn.waitForExists(2000)) {
                                System.out.println("登录成功-存在发现按钮");
                                return true;
                            }
                        }
                    }
                } else {
                    System.out.println("不存在滑块验证码");
                    while (true) {
                        jqhelper.delay(10000);
                        Device.pressHome();
                        jqhelper.delay(2000);
                        Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
                        UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").description("发现"));
                        if (search_btn.waitForExists(2000)) {
                            System.out.println("登录成功-存在发现按钮");
                            return true;
                        } else {
                            if (login.waitForExists(2000)) {
                                System.out.println("存在登录按钮");
                                return false;
                            }
                            Device.click(965, 149);
                            jqhelper.delay(10000);
                            Device.click(965, 149);
                            jqhelper.delay(10000);
                            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("写微博"));
                            if (ob.waitForExists(2000)) {
                                Device.pressBack();
                            }
                            // 进入首页,判断是否有战役打卡行动弹窗
                            UiObject card_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
                            if (card_btn.exists()) {
                                card_btn.clickAndWaitForNewWindow(2000);
                                sleep(2000);
                            }
                            // 进入首页,判断是否有今日签到弹窗
                            UiObject close_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/iv_close"));
                            if (close_btn.waitForExists(2000)) {
                                close_btn.clickAndWaitForNewWindow(2000);
                                sleep(2000);
                            }
                            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
                            if (update_btn.waitForExists(2000)) {
                                update_btn.click();
                                sleep(2000);
                            }
                            if (search_btn.waitForExists(2000)) {
                                return true;
                            }
                        }
                    }

                }
            } else {
                System.out.println("存在立即注册按钮");
                clear_data();
                return false;
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
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
                    break;
                }
                sleep(10000);
            }
            // 清除微博缓存
            clear_data();
            // 记录执行情况
//            start += 1;
//            saveAsFileWriter(executePath, start + "");
            // 点击更多
            UiObject more_btn = new UiObject(new UiSelector().className("android.widget.ImageView").description("更多选项"));
            more_btn.clickAndWaitForNewWindow(3000);
            sleep(2000);
            // 点击一键变机
            UiObject change_btn2 = new UiObject(new UiSelector().className("android.widget.TextView").text("一键变机"));
            if (change_btn2.exists()) {
                change_btn2.clickAndWaitForNewWindow(2000);
                sleep(2000);
                // 点击确认变机
                UiObject sure_btn2 = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                if (sure_btn2.exists()) {
                    sure_btn2.clickAndWaitForNewWindow(2000);
                }
            }
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
            Device.wakeUp();
            Device.pressHome();
            // 通过shell命令打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(8000);
            // 点击更多
            UiObject more_btn = new UiObject(new UiSelector().className("android.widget.ImageView").description("更多选项"));
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

    public String GetAccountServer(String url) {
        try {
            String res = HttpHelper.httpGetString(url);
            System.out.println("获取到账号信息:" + res);
            return res;
        } catch (Exception e) {

        }
        return "";
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
