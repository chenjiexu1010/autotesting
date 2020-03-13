package com.redbookedit;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.douyin.DouYinGetData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.leptonica.android.Convert;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class RedBookEdit extends SuperRunner {

    // 获取小红书工作数据
    private static String getRedBookDataPath = "http://222.185.251.62:22027/api/redbookedit/getdata";
    // 提价小红书工作数据
    private static String submitRedBookDataPath = "http://222.185.251.62:22027/api/redbookedit/submitdata";
    //读取执行操作 0 数据还原 1 执行小红书再编辑
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //保存小红书内容
    private static String contentPathexe = "/storage/emulated/0/QX_Backup/shellcmd/content.txt";
    private static Gson gson = new Gson();

    /**
     * 执行测试方法
     */
    public void test_redbookedit() {
        // 获取设备信息
        Device = getUiDevice();
        while (true) {
            try {
                Device.wakeUp();
                // 切换输入法
                jqhelper.change_input_method();
                // 读取当前执行顺序
                String execute = jqhelper.readSDFile(filePathexe);
                if (execute.equals("0")) {
                    // 关闭变机
                    jqhelper.close_bj();
                    // 清空小红书数据
                    clear_data();
                    // 获取执行数据
                    RedBookTaskEdit rbData = GetRedBookEditTask();
                    if (rbData == null) {
                        jqhelper.delay(1000 * 60 * 10);
                        jqhelper.writeSDFileEx2("获取任务为空" + "\n", "/sdcard/error.txt");
                        continue;
                    }
                    jqhelper.writeSDFileEx(gson.toJson(rbData), contentPathexe);
                    // 数据还原
                    reductionData(rbData.NickName);
                } else {
                    // 处理保存到本地的数据
                    String info = jqhelper.readSDFile(contentPathexe);
                    jqhelper.writeSDFileEx2(info + " \n", "/sdcard/error.txt");
                    if (info.equals("")) {
                        // 未获取到数据
                        saveAsFileWriter(filePathexe, "0");
                        jqhelper.writeSDFileEx2("获取保存本地数据失败" + " \n", "/sdcard/error.txt");
                        continue;
                    }
                    jqhelper.close_bj();
                    Type type = new TypeToken<RedBookTaskEdit>() {
                    }.getType();
                    RedBookTaskEdit rb = gson.fromJson(info, type);
                    // 小红书重新登录
                    RedBookAccountLoginAgain(rb.Account, rb.Password);
                    // 小红书执行再编辑
                    if (ExecuteRedBookEdit(rb.Title, rb.NickName)) {
                        // 提交小红书数据
                        SubmitRedBookTaskEdit(rb.Id + "", "成功");
                    } else {
                        // 提交小红书失败数据
                        SubmitRedBookTaskEdit(rb.Id + "", "失败");
                    }
                    // 执行结束
                    saveAsFileWriter(filePathexe, "0");
                }
            } catch (Exception ex) {
                jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
            }
        }
    }

    /**
     * 清除小红书数据
     */
    public void clear_data() throws IOException {
        // 关闭app
        Runtime.getRuntime().exec("am force-stop com.xingin.xhs");
        jqhelper.delay(2000);
    // 清除app数据
        Runtime.getRuntime().exec("pm clear com.xingin.xhs");
        jqhelper.delay(4000);
}

    /**
     * 小红书重新登录
     *
     * @return
     */
    public void RedBookAccountLoginAgain(String account, String password) {
        try {
            // 打开小红书
            Runtime.getRuntime().exec("am start com.xingin.xhs/.activity.SplashActivity");
            jqhelper.delay(10000);
            // 判断是否需要重新登录
            UiObject mobile_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("手机号登录"));
            if (!mobile_btn.waitForExists(5000)) {
                return;
            }
            // 点击微博授权登录
            UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));
            if (weibo_btn.waitForExists(5000)) {
                weibo_btn.click();
                jqhelper.delay(2000);
            }
            // 输入账号
            UiObject account_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginName"));
            if (account_btn.waitForExists(5000)) {
                account_btn.setText(Utf7ImeHelper.e(account));
                jqhelper.delay(2000);
            }
            // 输入密码
            UiObject pass_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("loginPassword"));
            if (pass_btn.waitForExists(5000)) {
                pass_btn.setText(Utf7ImeHelper.e(password));
                jqhelper.delay(2000);
            }
            // 点击登录
            UiObject login_btn = new UiObject(new UiSelector().className("android.view.View").text("登录"));
            if (login_btn.waitForExists(5000)) {
                login_btn.click();
                jqhelper.delay(2000);
            }
            // 判断是否开启设备权限
            UiObject device_auth__btn = new UiObject(new UiSelector().className("android.widget.TextView").text("暂时不用"));
            if (device_auth__btn.waitForExists(5000)) {
                device_auth__btn.click();
                jqhelper.delay(2000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("小红书-重新登录出现异常:" + ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 执行再编辑操作
     *
     * @param title
     */
    public boolean ExecuteRedBookEdit(String title, String nickName) {
        try {
            // 判断弹窗属性
            JudgeControl();
            // 判断是否在小红书首页
//            UiObject home_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("首页"));
//            if (!home_btn.waitForExists(2000)) {
//                // 首页按钮不存在
//                return false;
//            }
            // 点击我
            UiObject me_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我"));
            if (me_btn.waitForExists(5000)) {
                me_btn.click();
                jqhelper.delay(2000);
            }
            // 获取当前笔记数量
//            int note_count = 0;
//            UiObject note_btn = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.xingin.xhs:id/cru"));
//            if (note_btn.waitForExists(2000)) {
//                //所有笔记·2
//                note_count = Integer.getInteger(note_btn.getText().split("·")[1]);
//            }
            int count = 1;
            int execute = 1;
            // TODO 循环匹配到笔记为止
            while (true) {
                UiObject note = new UiObject(new UiSelector().className("androidx.recyclerview.widget.RecyclerView"))
                        .getChild(new UiSelector().className("android.widget.FrameLayout").index(count))
                        .getChild(new UiSelector().className("android.widget.TextView").index(execute).textContains(title));
                if (note.waitForExists(5000)) {
                    execute += 1;
                    count += 1;
                    note.click();
                    jqhelper.delay(2000);
                    // 下拉三次匹配标题
                    for (int i = 0; i <= 1; i++) {
                        UiObject title_ele = new UiObject(new UiSelector().className("android.widget.TextView").textContains("2020年地狱班的开局，希望一切都好好的"));
                        if (title_ele.waitForExists(5000)) {
                            // 点击更多 执行再编辑功能
                            UiObject more_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.xingin.xhs:id/moreOperateIV"));
                            if (more_btn.waitForExists(5000)) {
                                more_btn.click();
                                jqhelper.delay(2000);
                            }
                            // 视频的话点击share
                            UiObject video_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.xingin.xhs:id/shareButton"));
                            if (video_btn.waitForExists(5000)) {
                                video_btn.click();
                                jqhelper.delay(2000);
                            }
                            UiObject edit_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("编辑"));
                            if (edit_btn.waitForExists(2000)) {
                                edit_btn.click();
                                jqhelper.delay(5000);
                            }
                            // TODO 暂时模糊点击发布笔记按钮  不知道为何发布笔记按钮无法选中
                            Device.click(622, 1796);
                            jqhelper.delay(2000);
                            return true;
//                            UiObject send_btn = new UiObject(new UiSelector().className("android.widget.Button").text("发布笔记"));
//                            if (send_btn.waitForExists(5000)) {
//                                send_btn.click();
//                                jqhelper.delay(2000);
//                                return true;
//                            }
                        } else {
                            ScriptHelper.swipe(0, 1200, 0, 800);
                        }
                        Device.pressBack();
                    }
                } else {
                    count += 1;
                    // 下滑
                    ScriptHelper.swipe(0, 1200, 0, 800);
                    jqhelper.delay(2000);
                }

            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 判断控件是否出现
     */
    public void JudgeControl() {
        try {
            UiObject cancel_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("取消"));
            if (cancel_btn.waitForExists(3000)) {
                cancel_btn.click();
                jqhelper.delay(2000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 获取小红书执行数据
     *
     * @return
     */
    public RedBookTaskEdit GetRedBookEditTask() {
        try {
            String postData = HttpHelper.httpPostForString(this.getRedBookDataPath, "");
            jqhelper.writeSDFileEx2(postData + "\n", "/sdcard/error.txt");
            if (postData.equals("")) {
                return null;
            }
            Type type = new TypeToken<RedBookTaskEdit>() {
            }.getType();
            RedBookTaskEdit rbTaskEdit = gson.fromJson(postData, type);
            return rbTaskEdit;
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
        return null;
    }

    /**
     * 提交小红书数据
     */
    public void SubmitRedBookTaskEdit(String id, String message) {
        try {
            String postData = String.format("{\"Id\":\"%s\",\"Message\":\"%s\"}", new Object[]{id, message});
            String result = HttpHelper.httpPostForString(submitRedBookDataPath, postData);
            jqhelper.writeSDFileEx2("提交小红书返回数据:" + result + "\n", "/sdcard/error.txt");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 变机大师(数据还原)
     *
     * @return
     */
    private boolean reductionData(String rbName) {
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
            if (list_view.waitForExists(5000)) {
                for (int i = 0; i <= 10; i++) {
                    // 拼接出需要备份的data名
                    String name = String.format("%s_6.35.0", rbName);
                    // 获取滑动元素并滑到相应位置
                    UiScrollable scroll_btn = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                    scroll_btn.scrollTextIntoView(name);
                    // 点击还原微博数据
                    UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
                    if (!weibo_btn.waitForExists(2000)) {
                        ScriptHelper.swipe(0, 1200, 0, 800);
                        jqhelper.delay(2000);
                        continue;
                    }
                    weibo_btn.click();
                    jqhelper.delay(2000);
                    // 点击数据还原
                    UiObject data_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("数据还原"));
                    data_btn.clickAndWaitForNewWindow(2000);
                    jqhelper.delay(10000);
                    while (true) {
                        saveAsFileWriter(filePathexe, "1");
                        jqhelper.delay(10000);
                        // 数据还原完成,点击确定,sleep80秒,等待手机重启
                        UiObject yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
                        if (yes_btn.waitForExists(5000)) {
                            yes_btn.click();
                            jqhelper.delay(80000);
                            break;
                        }
                    }
                    Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("异常reductionData()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
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
