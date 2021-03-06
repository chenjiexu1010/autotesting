package com.redbookedit;

import android.util.Log;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiWatcher;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedBookSearch extends SuperRunner {
    public Gson gson = new Gson();
    private int listcount = 0;
    private int start = 0;
    private static String num = "";
    private static String search_word = "";
    private static ArrayList<String> RestroeList = null;
    private static ArrayList<String> search_word_list = null;
    //获取下拉词任务
    private static String httpPostUrl = "http://222.185.251.62:22027/api/redbookdropdown/gettask";
    //提交下拉词任务
    private static String httpSubmitUrl = "http://222.185.251.62:22027/api/redbookdropdown/submittask";
    //读取备份文件
    private static String filePath = "/storage/emulated/0/QX_Backup/shellcmd/shellstatus.txt";
    //读取执行操作 0 数据还原 1 执行下拉任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //备份列表
    private static String ApkStorePath = "/storage/emulated/0/QX_Backup/App/";
    private static String file = "/storage/emulated/0/QX_Backup/shellcmd/uselessNumber.txt";

    public void test_main() {
        try {
            getUiDevice().registerWatcher("watcher", new UiWatcher() {
                @Override
                public boolean checkForCondition() {
                    getUiDevice().pressBack();
                    return true;
                }
            });
        } catch (Exception e) {
            jqhelper.writeSDFileEx2(e.toString(), "/sdcard/error.txt");
        }
        while (true) {
            try {
                // 读取txt中的数字，0：数据还原，1：小红书搜索操作
                String flag_exe = readFileContent(filePathexe);
                String text = readFileContent(filePath);
                start = Integer.parseInt(text);
                RestroeList = GetFileName();
                if ("0".equals(flag_exe)) {
                    clear_data();
                    if (reductionData()) {
                        Date date = new Date();
                        jqhelper.writeSDFileEx("今日工作完成：" + date.toString() + "\n", "/sdcard/WorLog.txt");
                        changeSystem();
                    }
                }
                if ("1".equals(flag_exe)) {
                    // 更换输入法
                    change_input_method();
                    int times = 3;
                    // 执行下拉词搜索
                    search(times);
                    // 清空数据
                    clear_data();
                    // 关闭变机大师
                    close_bj();
                    if (reductionData()) {
                        Date date = new Date();
                        jqhelper.writeSDFileEx("今日工作完成：" + date.toString() + "\n", "/sdcard/WorLog.txt");
                        changeSystem();
                    }
                }
            } catch (
                    Exception e) {
                jqhelper.writeSDFileEx("异常：" + e.toString() + " \n", "/sdcard/error.txt");
            }
        }

    }

    /**
     * 判断是否登录账号
     *
     * @return
     */
    public boolean CheckIsHome() {
        try {
            // 同意按钮
            UiObject agree_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意"));
            if (agree_btn.waitForExists(5000)) {
                writer(file, start + "_6.36.0");
                return false;
            }
            // 手机号登录界面
            UiObject phone_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("手机号登录"));
            if (phone_btn.waitForExists(5000)) {
                writer(file, start + "_6.36.0");
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 小红书下拉词搜索
     *
     * @param times
     * @return
     */
    private boolean search(int times) {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            sleep(30000);
            // 返回首页
//            device.pressHome();
//            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            if (!CheckIsHome()) {
                System.out.println("当前账号不在首页无法进行工作");
                return false;
            }
            // 判断取消更新按钮
            UiObject cancelBtn = new UiObject(new UiSelector().className("android.widget.Button").text("取消"));
            if (cancelBtn.waitForExists(2000)) {
                cancelBtn.click();
                jqhelper.delay(2000);
            } else {
                System.out.println("取消按钮不存在");
            }
            // 提示绑定手机号
            UiObject binding_btn = new UiObject(new UiSelector().className("android.widget.ImageView").text("com.xingin.xhs:id/b3w"));
            if (binding_btn.waitForExists(5000)) {
                binding_btn.click();
                sleep(3000);
            } else {
                System.out.println("绑定手机按钮不存在");
            }
            // 点击暂时不用
            UiObject useless_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("暂时不用"));
            if (useless_btn.waitForExists(5000)) {
                useless_btn.click();
                sleep(3000);
            } else {
                System.out.println("暂时不用按钮不存在");
            }
            // 获取下拉词
            search_word_list = get_hot_words_by_server();
            for (int i = 0; i < search_word_list.size(); i++) {
                search_word = search_word_list.get(i);
                if (search_word.equals("")) {
                    continue;
                }
                device.click(457, 137);
                jqhelper.delay(5000);
                UiObject search_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.xingin.xhs:id/b9e"));
                if (search_input.waitForExists(5000)) {
                    // 清空搜索栏
                    String name = search_input.getText();
                    //如果光标在最后
                    pressTimes(KeyEvent.KEYCODE_DEL, name.length());
                    search_input.setText(Utf7ImeHelper.e(search_word));
                    device.pressEnter();
                    sleep(2000);
                    for (int j = 0; j < times; j++) {
                        if (useless_btn.waitForExists(3000)) {
                            useless_btn.click();
                            sleep(2000);
                        }
                        ScriptHelper.swipe(0, 1200, 0, 400);
//                            scroll.scrollForward();
                        sleep(10000);
                    }
                    if (useless_btn.waitForExists(3000)) {
                        useless_btn.click();
                        sleep(2000);
                    }
//                        scroll.scrollBackward();
                    ScriptHelper.swipe(0, 400, 0, 1200);
                    if (useless_btn.waitForExists(3000)) {
                        useless_btn.click();
                        sleep(2000);
                    }
                    device.pressBack();
                    sleep(2000);
                    // 更新刷下拉次数
                    SubmitDropDownData(search_word);
                }
            }
            // 将0写入文件
            saveAsFileWriter(filePathexe, "0");
        } catch (Exception e) {
            jqhelper.writeSDFileEx("搜索异常：" + e.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 变机大师-云端还原
     *
     * @return
     */
    private boolean reductionCloudData() {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            // 打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(10000);
            // 点击小红书
            UiObject redbook_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("小红书"));
            if (redbook_btn.waitForExists(5000)) {
                redbook_btn.click();
                sleep(3000);
            }
            // 点击云端还原
            UiObject cloud_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("云端还原"));
            if (cloud_btn.waitForExists(5000)) {
                cloud_btn.click();
                sleep(3000);
            }
            // 填入项目名称
            UiObject pro_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.littlerich.holobackup:id/txt_projectname"));
            if (pro_input.waitForExists(5000)) {
                pro_input.setText("xhs_dropdown01");
                sleep(3000);
            }
            // 点击确定
            UiObject confirm_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (confirm_btn.waitForExists(5000)) {
                confirm_btn.click();
                sleep(3000);
            }
            // 等待云数据还原成功
            while (true) {
                if (confirm_btn.waitForExists(10000)) {
                    // 将文件写为1
                    saveAsFileWriter(filePathexe, "1");
                    confirm_btn.click();
                    sleep(3000);
                }
                sleep(10000);
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx("还原异常：" + e.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 变机大师-本地还原
     *
     * @return
     */

    private boolean reductionData() {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(10000);
            UiObject reduction_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键还原"));
            if (reduction_btn.waitForExists(5000)) {
                reduction_btn.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }
            // 获取本次需要还原的备份文件名
            UiObject list_view = new UiObject(new UiSelector().className("android.widget.ListView").resourceId("com.littlerich.holobackup:id/listView"));
            if (list_view.exists()) {
                // 获取列表个数
                listcount = RestroeList.size();
                if (start <= listcount) {
                    // 拼接出需要备份的data名
                    String name = String.format("%s_6.36.0", start + "");
                    System.out.println(name);
                    // 获取滑动元素并滑到相应位置
                    UiScrollable scroll_btn = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                    scroll_btn.scrollTextIntoView(name);
                    // 点击还原小红书数据
                    UiObject redbook_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
                    if (redbook_btn.waitForExists(5000)) {
                        redbook_btn.clickAndWaitForNewWindow(2000);
                        sleep(2000);
                    }
                    // 点击数据还原
                    UiObject data_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("数据还原"));
                    if (data_btn.waitForExists(5000)) {
                        data_btn.clickAndWaitForNewWindow(2000);
                        sleep(10000);
                    }
                    // 将新的数字写入txt文件中
                    start += 1;
                    saveAsFileWriter(filePath, start + "");
                    saveAsFileWriter(filePathexe, "1");
                    // 数据还原完成，点击确定，sleep80秒，等待手机重启
                    UiObject yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                    while (true) {
                        if (yes_btn.waitForExists(3000)) {
                            yes_btn.clickAndWaitForNewWindow(2000);
                            break;
                        }
                        sleep(2000);
                    }
                    Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
                } else {
                    saveAsFileWriter(filePath, "1");
                    saveAsFileWriter(filePathexe, "0");
                    return true;
                }
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx("还原异常：" + e.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
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
            if (more_btn.waitForExists(5000)) {
                more_btn.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }
            // 点击一键变机
            UiObject change_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键变机"));
            if (change_btn.waitForExists(3000)) {
                change_btn.clickAndWaitForNewWindow(5000);
                sleep(5000);
                // 点击确定
                UiObject confirm_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                if (confirm_btn.waitForExists(5000))
                    confirm_btn.clickAndWaitForNewWindow(3000);
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx("变机异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
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
     * 清除搜索的数据 一个字一个字清除
     *
     * @param keyCode
     * @param times
     */
    public void pressTimes(int keyCode, int times) {
        for (int i = 0; i < times; i++) {
            UiDevice.getInstance().pressKeyCode(keyCode);
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
     * 获取备份列表
     *
     * @return
     */
    private ArrayList<String> GetFileName() {
        ArrayList<String> filenamelist = new ArrayList<String>();
        File file = new File(ApkStorePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    continue;
                } else {
                    filenamelist.add(files[i].getName().replace(".apk", ""));
                    Log.i("AutoScript", "所有备份apk名称下标=" + i + "=" + files[i].getName());
                }

            }
        }
        return filenamelist;
    }

    /**
     * 追加写入流
     */
    private void writer(String file, String content) {
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(file);
            fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 热门获取下拉词任务服务
     *
     * @return
     */
    private ArrayList<String> get_hot_words_by_server() {
        try {
            String result = HttpHelper.httpPostForString(this.httpPostUrl, gson.toJson(""));
            if (!"".equals(result)) {
                Type type = new TypeToken<String>() {
                }.getType();
                String str = gson.fromJson(result, type);
                String[] words_array = str.split(";");
                search_word_list = new ArrayList<String>();
                for (int i = 0; i < words_array.length; i++) {
                    if (!words_array[i].equals(";") && !words_array[i].equals(""))
                        search_word_list.add(words_array[i]);
                }
                System.out.println(search_word_list);
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2(e + " 异常 \n", "/sdcard/error.txt");
        }
        return search_word_list;
    }

    /**
     * 提交下拉词任务
     *
     * @param searchWord 搜索下拉词
     */
    private void SubmitDropDownData(String searchWord) {
        try {
            String result = HttpHelper.httpPostForString(this.httpSubmitUrl, gson.toJson(searchWord));
            System.out.println(result);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

}
