package weibo_search;

import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.common.uiControl.UiEdit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wechat.WxData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Search2 extends SuperRunner {
    public Gson gson = new Gson();
    private int listcount = 0;
    private int start = 0;
    private static String num = "";
    private static String search_word = "";
    private static ArrayList<String> RestroeList = null;
    private static ArrayList<String> search_word_list = null;

    //获取下拉词任务
    private static String httpPostUrl = "http://222.185.251.62:22027/api/weibo/getdropdownnewwordtask";
    //提交下拉词任务
    private static String httpSubmitUrl = "http://222.185.251.62:22027/api/weibo/submitdropdowntask";
    //读取备份文件
    private static String filePath = "/storage/emulated/0/QX_Backup/shellcmd/shellstatus.txt";
    //读取执行操作 0 数据还原 1 执行下拉任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //备份列表
    private static String ApkStorePath = "/storage/emulated/0/QX_Backup/App/";

    /**
     * 执行主体
     *
     * @throws UiObjectNotFoundException
     * @throws RemoteException
     * @throws IOException
     */
    public void test_main() {
        /*
            短信停止运行判断
         */
        while (true) {
            try {
                jqhelper.close_bj();
                ShortMessageJudge();
                // 读取txt中的数字,0代表数据还原,1代表微博操作,
                String Flag_exe = readFileContent(filePathexe);
                if (Flag_exe.equals("0")) {
                    clear_data();
                    if (reductionCloudData()) {
                        Date date = new Date();
                        System.out.println(date.toString());
                        jqhelper.writeSDFileEx("今日工作完成:" + date.toString() + " \n", "/sdcard/WorkLog.txt");
                        changeSystem();
                    }
                }
                if (Flag_exe.equals("1")) {
                    // 切换输入法
                    change_input_method();
                    int times = 3;
                    // 执行下拉词搜索
                    search_read(times);
                    // 清空微博数据
                    clear_data();
                    // 关闭变机大师
                    close_bj();
                    if (reductionCloudData()) {
                        Date date = new Date();
                        System.out.println(date.toString());
                        jqhelper.writeSDFileEx("今日工作完成:" + date.toString() + " \n", "/sdcard/WorkLog.txt");
                        changeSystem();
                    }
                }
            } catch (Exception ex) {
                jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/error.txt");
            }
        }
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
     * 搜索下拉词
     *
     * @param times
     * @return
     */
    private boolean search_read(int times) {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            // 【打开微博】
            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
            sleep(8000);
            UiObject open_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("\t\t开启\t\t"));
            if (open_btn.exists()) {
                open_btn.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }
            // 点击允许两次
            UiObject allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(1000);
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(10000);
            }
            // 进入首页,判断是否有战役打卡行动弹窗
            UiObject card_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
            if (card_btn.waitForExists(5000)) {
                card_btn.click();
                sleep(8000);
            }
            // 进入首页,判断是否有今日签到弹窗
            UiObject close_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/iv_close"));
            if (close_btn.exists()) {
                close_btn.clickAndWaitForNewWindow(2000);
                sleep(8000);
            }
            // 进入首页,观察是否有版本更新弹窗跳出
            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (update_btn.exists()) {
                update_btn.clickAndWaitForNewWindow(2000);
                sleep(5000);
            }
            // 判断是否显示近期修改过密码
            UiObject change_password = new UiObject(new UiSelector().text("由于你近期修改过密码，或开启了登录保护，为保障你的帐号安全，请重新登录。"));
            if (change_password.waitForExists(5000)) {
                //记录当前备份的序号
                jqhelper.writeSDFileEx("修改密码:" + start + " \n", "/sdcard/error.txt");
            }
            // 点击账号密码登录
            UiObject login_btn = new UiObject(new UiSelector().className("android.widget.Button").text("用帐号密码登录"));
            if (login_btn.waitForExists(3000)) {
                //记录当前备份的序号
                jqhelper.writeSDFileEx("无账号:" + start + " \n", "/sdcard/error.txt");
            }
            // 点击搜索按钮
            UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").descriptionContains("发现"));
            if (search_btn.exists()) {
                search_btn.clickAndWaitForNewWindow(2000);
                sleep(15000);
                device.pressBack();
                sleep(2000);

//                search_word_list = get_words_by_server();
                // 拉取搜索词
                search_word_list = get_hot_words_by_server();
//                search_word_list = get_words_by_txt();
                for (int i = 0; i < search_word_list.size(); i++) {
                    search_word = search_word_list.get(i);
                    System.out.println(search_word_list.get(i));
                    UiObject search_box = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.sina.weibo:id/tv_search_keyword"));
                    if (search_box.exists()) {
                        search_box.click();
                        ob = new UiObject(new UiSelector().className("android.widget.ImageView").description("清空"));
                        if (ob.waitForExists(2000)) {
                            ob.click();
                            jqhelper.delay(2000);
                        } else {
                            // 清空搜索栏
                            String name = search_box.getText();
                            //如果光标在最后
                            pressTimes(KeyEvent.KEYCODE_DEL, name.length());
                        }
                        sleep(2000);
                        search_box.setText(Utf7ImeHelper.e(search_word));
                        device.pressEnter();
                        sleep(2000);
                        System.out.println("搜索成功");
                        // 滑动页面,浏览博文
                        UiScrollable scroll = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                        if (scroll.exists()) {
                            scroll.setSwipeDeadZonePercentage(0.15);
                            for (int m = 0; m < times; m++) {
                                scroll.scrollForward();
                                sleep(3000);
                            }
                        }
                    }
//                    //更新刷下拉词次数
                    SubmitDropDownData(search_word, false, 0, "", 0);
                }
            }

            // 将0写入文件
            saveAsFileWriter(filePathexe, "0");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常read()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 变机大师(云端还原)
     *
     * @return
     */
    private boolean reductionCloudData() {
        try {
            // 获取设备信息
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            // 打开变机大师
            Runtime.getRuntime().exec("am start com.littlerich.holobackup/.MainActivity");
            sleep(10000);
            //点击微博
            UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
            if (weibo_btn.waitForExists(5000)) {
                weibo_btn.click();
                sleep(3000);
            }
            // 点击云端还原
            UiObject cloud_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("养号云还原"));
            if (cloud_btn.waitForExists(5000)) {
                cloud_btn.click();
                sleep(3000);
            }
            UiObject pro_btn = new UiObject(new UiSelector().className("android.widget.EditText"));
            if (pro_btn.waitForExists(5000)) {
                pro_btn.setText(Utf7ImeHelper.e("wbyanghao01 "));
                sleep(3000);
            }
            // 点击确认按钮
            UiObject sure_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (sure_btn.waitForExists(5000)) {
                sure_btn.click();
                sleep(3000);
            }
            // 等待云数据还原成功
            while (true) {
                if (sure_btn.waitForExists(10000)) {
                    // 将文件写为1
                    saveAsFileWriter(filePathexe, "1");
                    sure_btn.click();
                }
                sleep(10000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常reductionData()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 变机大师(数据还原)
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
            reduction_btn.clickAndWaitForNewWindow(3000);
            sleep(2000);
            // 获取本次需要还原的备份文件名
            UiObject list_view = new UiObject(new UiSelector().className("android.widget.ListView").resourceId("com.littlerich.holobackup:id/listView"));
            if (list_view.exists()) {
                // 获取列表个数
                listcount = list_view.getChildCount();
                listcount = RestroeList.size();
                if (start <= listcount) {
                    // 拼接出需要备份的data名
                    String name = String.format("%s_9.10.2", start + "");
                    // 获取滑动元素并滑到相应位置
                    UiScrollable scroll_btn = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                    scroll_btn.scrollTextIntoView(name);
                    // 点击还原微博数据
                    UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
                    weibo_btn.clickAndWaitForNewWindow(2000);
                    // 点击数据还原
                    UiObject data_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("数据还原"));
                    data_btn.clickAndWaitForNewWindow(2000);
                    sleep(10000);
                    // 将新的数字写入txt文件中,
                    start += 1;
                    saveAsFileWriter(filePath, start + "");
                    saveAsFileWriter(filePathexe, "1");
                    // 数据还原完成,点击确定,sleep80秒,等待手机重启
                    UiObject yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                    yes_btn.clickAndWaitForNewWindow(2000);
                    sleep(80000);
                    Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
                } else {
                    saveAsFileWriter(filePath, "1");
                    saveAsFileWriter(filePathexe, "0");
                    return true;
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常reductionData()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
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
     * 热门获取下拉词任务服雾
     *
     * @return
     */
    private ArrayList<String> get_hot_words_by_server() {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            URL url = new URL(httpPostUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    Type type = new TypeToken<String>() {
                    }.getType();
                    String jsonStr = gson.fromJson(temp, type);
                    sbf.append(jsonStr);
                }
                result = sbf.toString();
                String[] words_array = result.split("&");
                search_word_list = new ArrayList<String>();
                for (int i = 0; i < words_array.length; i++) {
                    if (!words_array[i].equals("&") && !words_array[i].equals("")) {
                        search_word_list.add(words_array[i]);
                    }
                }
                System.out.println(search_word_list);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }
        return search_word_list;
    }

    /**
     * 提交下拉词任务
     *
     * @param searchWord   搜索下拉词
     * @param isOnPage     是否在下拉版面
     * @param currentIndex 当前指数
     * @param message      返回信息
     * @param currentRank  当前排名
     */
    private void SubmitDropDownData(String searchWord, boolean isOnPage, int currentIndex, String message, int currentRank) {
        try {
            String postData = String.format("{\"SearchWord\":\"%s\"}", new Object[]{searchWord});
            HttpHelper.httpPostForString(this.httpSubmitUrl, postData);
        } catch (Exception ex) {
        }
    }
}

