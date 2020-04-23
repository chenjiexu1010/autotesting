package com.redbookedit.BrushQuantity;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RbBrushQuantity extends SuperRunner {

    public Gson gson = new Gson();
    private int listcount = 0;
    private int start = 0;
    private static ArrayList<String> RestroeList = null;
    private static ArrayList<String> search_word_list = null;
    private static String keyword = "";
    private static String httpPostUrl = "http://my-test.qicp.vip:44200/api/redbookbrush/getdata";
    private static String httpSubmitUrl = "http://my-test.qicp.vip:44200/api/redbookbrush/submitdata";
    //private static String httpSubmitUrl = "http://my-test.qicp.vip:44200/api/redbookphone/submitdata";
    private static String httpRestartUrl = "http://v3.jqsocial.com:22018/api/rb/parentNumber/restart";
    //读取备份文件
    private static String filePath = "/storage/emulated/0/QX_Backup/shellcmd/shellstatus.txt";
    //读取执行操作 0 数据还原 1 执行任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //备份列表
    private static String ApkStorePath = "/storage/emulated/0/QX_Backup/App/";
    private static String file = "/storage/emulated/0/QX_Backup/shellcmd/uselessNumber.txt";

    public void test_main() {
        while (true) {
            try {
                // 读取txt中的数字，0：数据还原，1：小红书搜索操作
                String flag_exe = readFileContent(filePathexe);
                String text = readFileContent(filePath);
                start = Integer.parseInt(text);
                RestroeList = GetFileName();
////                    boolean loginFlag = redBookLogin(rb.getLoginAccount(), rb.getLoginPassword());
//                    if (!loginFlag)
//                        continue;
//                    jqhelper.delay(3000);

                ob = new UiObject(new UiSelector().className("android.widget.Button").text("关闭应用"));
                if (ob.waitForExists(2000)) {
                    ob.click();
                    jqhelper.delay(2000);
                }
                if ("0".equals(flag_exe)) {
                    clear_data();
                    if (reductionData()) {
                        Date date = new Date();
                        System.out.println(date.toString());
                        jqhelper.writeSDFileEx2("今日工作完成：" + date.toString() + "\n", "/sdcard/WorLog.txt");
                        changeSystem();
                    }
                }
                if ("1".equals(flag_exe)) {
                    // 切换输入法
                    change_input_method();
                    BrushNotes();
                    clear_data();
                    // 关闭变机大师
                    close_bj();
                    if (reductionData()) {
                        Date date = new Date();
                        System.out.println(date.toString());
                        jqhelper.writeSDFileEx2("今日工作完成：" + date.toString() + "\n", "/sdcard/WorLog.txt");
                        changeSystem();
                    }
                }

            } catch (Exception e) {
                jqhelper.writeSDFileEx2("执行主体出现异常：" + e.toString() + "\n", "/sdcard/error.txt");
                System.out.println(e);
            }
        }
//        try {
////            skimNotes();
////            getRedBookYangHao("908916986");
////            redBookLogin("yaolianggu@126.com","yaogu717..12");
//            change_input_method();
//            BrushNotes();
//            System.out.println("我结束了");
//        } catch (Exception e) {
//            System.out.println(e);
//        }

//        try {
//            UiObject collect_btn = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("com.xingin.xhs:id/bkd"));
//            System.out.println(collect_btn.hashCode());
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    /**
     * 笔记刷量
     *
     * @return
     * @throws Exception
     */
    private boolean BrushNotes() throws Exception {
        System.out.println("我进入浏览了");
        UiDevice device = getUiDevice();
        String rbNum = "";
        Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
        sleep(5000);
        int Id = 0;
        try {
            // 同意按钮
            UiObject agree_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意"));
            if (agree_btn.waitForExists(5000)) {
//                writer(file, start + "_6.36.0");
                return false;
            }
            // 手机号登录界面
            UiObject phone_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("手机号登录"));
            if (phone_btn.waitForExists(5000)) {
//                writer(file, start + "_6.36.0");
                return false;
            }
            Runtime.getRuntime().exec("am force-stop com.xingin.xhs");
            sleep(3000);
            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            sleep(20000);
            UiObject number_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("本机号码一键绑定"));
            if (number_btn.waitForExists(5000)) {
                device.pressBack();
                sleep(3000);
            }
            // 提示绑定手机号
            UiObject binding_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("立即绑定"));
            if (binding_btn.waitForExists(5000)) {
                device.pressBack();
                sleep(3000);
            }
            UiObject me_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我"));
            if (me_btn.waitForExists(5000)) {
                me_btn.click();
                sleep(3000);
            }
            UiObject rbNum_text = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/bsp").textContains("小红书号："));
            if (rbNum_text.waitForExists(5000)) {
                String text = rbNum_text.getText();
                rbNum = text.split("：")[1];
                sleep(3000);
            }
            device.pressBack();
            List<RbBrushTaskEntity> list = getRedBookBrush(rbNum);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    RbBrushTaskEntity task = list.get(i);
                    System.out.println(task);
                    if (task == null) {
                        continue;
                    }
                    sleep(3000);
                    // 点击搜索框
                    device.click(514, 253);
                    UiObject search_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.xingin.xhs:id/b9e"));
                    if (search_input.waitForExists(5000)) {
                        search_input.setText(Utf7ImeHelper.e(task.getNoteTitle()));
                        device.pressEnter();
                        sleep(2000);
                        UiObject useless_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("暂时不用"));
                        UiObject seek_bar = new UiObject(new UiSelector().className("android.widget.SeekBar").resourceId("com.xingin.xhs:id/cwj"));
                        UiObject grade_win = new UiObject(new UiSelector().className("android.widget.TextView").text("您对小红书的评分如何?"));
                        UiScrollable scroll = new UiScrollable(new UiSelector().className("androidx.viewpager.widget.ViewPager").resourceId("com.xingin.xhs:id/b9o"));
                        UiObject end_text = new UiObject(new UiSelector().className("android.widget.TextView").text("- THE END -"));
                        boolean flag1 = false;
                        boolean flag2 = false;
                        boolean flag3 = false;
                        while (true) {
                            // 点击笔记
                            UiObject all = new UiObject(new UiSelector().className("androidx.recyclerview.widget.RecyclerView").resourceId("com.xingin.xhs:id/b9n"));
                            System.out.println("count:" + all.getChildCount());

                            for (int j = 0; j < all.getChildCount(); j++) {
                                System.out.println("j:" + j);
                                UiObject note_title = null;
                                UiObject auth_title = null;
                                UiObject block = null;
                                try {
//                                    note_title = all.getChild(new UiSelector().className("android.widget.FrameLayout").index(j).childSelector(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7a")));
//                                    auth_title = all.getChild(new UiSelector().className("android.widget.FrameLayout").index(j).childSelector(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7c")));
                                    block = all.getChild(new UiSelector().className("android.widget.FrameLayout").index(j));
                                    note_title = block.getChild(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7a"));
                                    auth_title = block.getChild(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7c"));
                                    System.out.println(note_title.getText());
                                    System.out.println(auth_title.getText());
                                } catch (Exception e) {
                                    System.out.println(e);
                                    continue;
                                }
//                                UiObject block = all.getChild(new UiSelector().className("android.widget.FrameLayout").index(j));
//                                if (block.getChildCount() == 0)
//                                    continue;
//                                UiObject note_title = block.getChild(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7a"));
//                                UiObject auth_title = block.getChild(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/b7c"));
//                                System.out.println(note_title.getText());
//                                System.out.println(auth_title.getText());
                                if (note_title != null && auth_title != null) {
                                    if (note_title.getText().contains(task.getNoteTitle()) && auth_title.getText().contains(task.getAuthName())) {
                                        block.click();
                                        sleep(3000);
                                        UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("关注"));
                                        if (follow_btn.exists()) {
                                            follow_btn.click();
                                            sleep(2000);
                                        }
                                        UiObject like_btn = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("com.xingin.xhs:id/bl_"));

                                        if (like_btn.exists()) {
                                            like_btn.click();
                                            flag1 = true;
                                            sleep(2000);
                                        }
                                        if (grade_win.waitForExists(2000)) {
                                            device.pressBack();
                                            sleep(2000);
                                        }
                                        UiObject collect_btn = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("com.xingin.xhs:id/bkd"));
                                        collect_btn.isFocused();
                                        if (collect_btn.exists()) {
                                            collect_btn.click();
                                            flag2 = true;
                                            sleep(2000);
                                        }
                                        if (grade_win.waitForExists(2000)) {
                                            device.pressBack();
                                            sleep(2000);
                                        }
                                        flag3 = true;
                                        device.pressBack();
                                        break;
                                    }
                                }
                            }
                            if (flag1 && flag2) {
                                submitRedBookBrush(task.getId(), task.getNoteTitle(),rbNum);
                                device.pressBack();
                                break;
                            }
                            if (flag3)
                                break;
                            scroll.scrollForward();
                            sleep(2000);

                            if (end_text.exists()) {
                                flag3 = true;
                            }
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("刷量异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
        }

    }

    /**
     * 小红书登录
     *
     * @param account
     * @param password
     * @return
     */
//    private boolean redBookLogin(String account, String password) {
//        try {
//            System.out.println("我进入登录了");
//            UiDevice device = getUiDevice();
//            // 设置快捷输入法
//            change_input_method();
//            // 打开小红书
//            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
//            sleep(8000);
//            // 同意按钮
//            UiObject agree_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("同意"));
//            if (agree_btn.exists()) {
//                agree_btn.click();
//                sleep(3000);
//            }
//            // 点击微博登录
//            UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
//            if (weibo_btn.waitForExists(3000)) {
//                weibo_btn.clickAndWaitForNewWindow(5000);
//                sleep(3000);
//            }
//            // 点击手机号登录
////            UiObject phone_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("手机号登录"));
////            if (phone_btn.waitForExists(3000)) {
////                phone_btn.clickAndWaitForNewWindow(3000);
////            }
//            // 点击其他号码登录
////            UiObject other_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("其他号码登录"));
////            if (other_btn.waitForExists(3000)) {
////                other_btn.click();
////                sleep(5000);
////            }
//            // 点击密码登录
////            UiObject psw_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("密码登录"));
////            if (psw_btn.waitForExists(3000)) {
////                psw_btn.click();
////                sleep(3000);
////            }
//            // 输入账号
//            UiObject account_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginName"));
//            if (account_input.waitForExists(5000)) {
//                account_input.setText(account);
//                sleep(3000);
//            }
//            // 输入密码
//            UiObject password_input = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginPassword"));
//            if (password_input.waitForExists(5000)) {
//                password_input.setText(password);
//                sleep(3000);
//            }
//            // 点击登录
//            UiObject login_btn = new UiObject(new UiSelector().className("android.view.View").text("登录"));
//            if (login_btn.exists()) {
//                login_btn.click();
//                sleep(10000);
//            }
//            // 提示绑定手机号
//            UiObject binding_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("立即绑定"));
//            if (binding_btn.waitForExists(5000)) {
//                device.pressBack();
//                sleep(3000);
//            }
//            // 点击暂时不用
//            UiObject useless_title = new UiObject(new UiSelector().className("android.widget.TextView").text("开启设备权限"));
//            if (useless_title.waitForExists(5000)) {
//                device.click(329, 1156);
//                sleep(3000);
//            }
//            UiObject home_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("首页"));
//            if (home_btn.waitForExists(5000)) {
//                home_btn.click();
//            }
//            return true;
//        } catch (Exception e) {
//            jqhelper.writeSDFileEx("登录异常：" + e.toString() + "\n", "/sdcard/error.txt");
//            return false;
//        }
//    }

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
            jqhelper.writeSDFileEx2("还原异常：" + e.toString() + "\n", "/sdcard/error.txt");
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
            jqhelper.writeSDFileEx2("变机异常：" + e.toString() + "\n", "/sdcard/error.txt");
            return false;
        }
        return true;
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
     * 修改输入法
     *
     * @throws IOException
     */
    private void change_input_method() throws IOException {
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
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
     * 获取备份列表
     *
     * @return
     */
    private ArrayList<String> GetFileName() throws Exception {
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
     * 小红书刷量获取任务
     *
     * @return
     */
    private ArrayList<RbBrushTaskEntity> getRedBookBrush(String rbNum) throws Exception {
        System.out.println("我进入了取任务");
        ArrayList<RbBrushTaskEntity> list = null;
        String postData = String.format("{\"RedBookAccount\":\"%s\"}",
                new Object[]{rbNum});
        String result = HttpHelper.httpPostForString(this.httpPostUrl, postData);
        System.out.println("result：" + result);
        if (result != null && !"获取异常".equals(result)) {
            Type type = new TypeToken<ArrayList<RbBrushTaskEntity>>() {
            }.getType();
            list = gson.fromJson(result, type);
            System.out.println("list：" + list);
        }
        return list;
    }

    /**
     * 提交小红书刷量任务
     *
     * @param id 刷量任务id
     */
    private void submitRedBookBrush(int id, String noteTitle, String rbNumber) throws Exception {
        String postData = String.format("{\"Id\":\"%s\",\"NoteTitle\":\"%s\",\"RbNumber\":\"%s\"}", new Object[]{id, noteTitle, rbNumber});
        System.out.println();
        String result = HttpHelper.httpPostForString(this.httpSubmitUrl, postData);
        System.out.println(result);
    }

    private void restartRedBookYangHao(int id) throws Exception {
        String postData = String.format("{\"Id\":\"%s\"}", new Object[]{id});
        String result = HttpHelper.httpPostForString(this.httpRestartUrl, postData);
        System.out.println(result);
    }
}
