package com.weibo.sendblog;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

public class SendBlog extends SuperRunner {
    // 获取发送博文内容
    private static String getTaskPath = "http://v3.jqsocial.com:22018/api/sendblog/gettask";
    // 提交发送博文内容
    private static String submitTaskPath = "http://v3.jqsocial.com:22018/api/sendblog/submittask";
    // 博文内容保存路径
    private static String contentSacePath = "/storage/emulated/0/QX_Backup/shellcmd/content.txt";
    // 保存图片路径
    private static String savePicPath = "/storage/emulated/0/DCIM/Camera";
    // 读取执行操作 0 数据还原 1 执行下拉任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    // 初始化
    private static Gson gson = new Gson();
    // 还原备份Id
    private static int backUpId = 0;

    public void test_sendBlog() {
        try {
            // 短信验证弹窗判断
            ShortMessageJudge();
            //获取设备
            Device = getUiDevice();
            // 读取当前执行顺序
            String execute = jqhelper.readSDFile(filePathexe);
            int exe = Integer.parseInt(execute);
            switch (exe) {
                case 1:
                    HttpHelper.DeleteFile(new File(savePicPath));
                    GenerateTask();
                    break;
                case 2:
                    Execute();
                    test_sendBlog();
                    break;
                default:
                    System.out.println("获取到未知类型的任务");
                    break;
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 执行主体
     */
    public void Execute() {
        try {
            // 关闭变机大师
            jqhelper.close_bj();
            // 处理保存到本地的数据
            String info = jqhelper.readSDFile(contentSacePath);
            if (info.equals("")) {
                jqhelper.saveAsFileWriter(filePathexe, "1");
                System.out.println("执行发博文获取内容为空");
                return;
            }
            Type type = new TypeToken<SendBlogModel>() {
            }.getType();
            SendBlogModel model = gson.fromJson(info, type);
            if (model != null) {
                //TODO send blog
                SendBlog(model);
                // 关闭微博
                clear_data();
            } else {
                jqhelper.saveAsFileWriter(filePathexe, "1");
                test_sendBlog();
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    public void SendBlog(SendBlogModel model) {
        try {
            Device.wakeUp();
            Device.pressHome();
            // 打开微博
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
            ob = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
            if (ob.waitForExists(5000)) {
                ob.click();
                sleep(8000);
            }
            // 进入首页,判断是否有今日签到弹窗
            ob = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/iv_close"));
            if (ob.waitForExists(5000)) {
                ob.clickAndWaitForNewWindow(2000);
                sleep(8000);
            }
            // 判断是否显示近期修改过密码
            ob = new UiObject(new UiSelector().text("由于你近期修改过密码，或开启了登录保护，为保障你的帐号安全，请重新登录。"));
            if (ob.waitForExists(5000)) {
                // TODO 提交失败
                SubmitWeiBoRequest(model, "修改密码");
                return;
            }
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("用帐号密码登录"));
            if (ob.waitForExists(3000)) {
                // TODO 提交失败
                SubmitWeiBoRequest(model, "需要重新登录");
                return;
            }
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (ob.waitForExists(5000)) {
                ob.clickAndWaitForNewWindow(2000);
                sleep(5000);
            }
            Blog(model);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    public boolean Blog(SendBlogModel model) {
        try {
            int count = 0;
            if (!model.getPicUrls().equals("")) {
                count = model.getPicUrls().split(";").length;
            }
            // 进入首页
            UiObject home_btn = new UiObject(new UiSelector().className("android.view.View").description("首页"));
            if (home_btn.waitForExists(5000)) {
                home_btn.click();
                sleep(5000);
            }
            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (update_btn.waitForExists(5000)) {
                update_btn.clickAndWaitForNewWindow(2000);
                sleep(5000);
            }
            // 点击添加博文
            UiObject add_btn = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.sina.weibo:id/titleSave"));
            if (add_btn.waitForExists(2000)) {
                add_btn.click();
            }
            UiObject write_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("写微博"));
            if (write_btn.waitForExists(2000)) {
                write_btn.click();
            }
            // TODO 获取博文内容
            UiObject search_box = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.sina.weibo:id/edit_view"));
            if (search_box.waitForExists(2000)) {
                search_box.setText(Utf7ImeHelper.e(model.getBlogContent()));
                jqhelper.delay(10000);
            }
            //选择图片
            if (count > 0) {
                UiObject insert_img_btn = new UiObject(new UiSelector().className("android.widget.ImageButton").description("插入图片"));
                if (insert_img_btn.waitForExists(2000)) {
                    insert_img_btn.clickAndWaitForNewWindow(2000);
                }
                sleep(5000);
                for (int i = 1; i <= count; i++) {
                    //选中图片
                    UiObject check_pic_btn = new UiObject(new UiSelector().className("android.support.v7.widget.RecyclerView")).
                            getChild(new UiSelector().className("android.widget.RelativeLayout").index(i)).
                            getChild(new UiSelector().className("android.widget.RelativeLayout")).
                            getChild(new UiSelector().className("android.widget.TextView"));
                    if (check_pic_btn.waitForExists(2000)) {
                        check_pic_btn.click();
                        sleep(2000);
                    }
                }
                UiObject next_btn = new UiObject(new UiSelector().className("android.widget.TextView").description("下一步"));
                if (next_btn.waitForExists(2000)) {
                    next_btn.clickAndWaitForNewWindow(2000);
                }
                UiObject next2_btn = new UiObject(new UiSelector().className("android.widget.Button").textContains("下一步"));
                if (next2_btn.waitForExists(2000)) {
                    next2_btn.clickAndWaitForNewWindow(2000);
                }
            }
            UiObject phone_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("无视频或照片"));
            if (phone_btn.waitForExists(5000)) {
                Device.pressBack();
                sleep(2000);
            }
            //点击发送
            UiObject send_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("发送"));
            if (send_btn.waitForExists(5000)) {
                send_btn.click();
                SubmitWeiBoRequest(model, "成功");
                jqhelper.saveAsFileWriter(filePathexe, "1");
                sleep(5000);
            }
            UiObject ex_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("解除异常"));
            if (ex_btn.waitForExists(10000)) {
                // todo 更新账号异常状态
                SubmitWeiBoRequest(model, "账号异常");
                return false;
            }
            return true;
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("发博文异常:" + e + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    public void GenerateTask() {
        while (true) {
            try {
                Device.wakeUp();
                if (SaveSendContent()) {
                    ReductionByBackUpId();
                } else {
                    jqhelper.delay(10000);
                }
            } catch (Exception ex) {
                jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
            }
        }
    }

    public boolean SaveSendContent() {
        try {
            SendBlogModel model = GetBlogContent();
            if (model == null) {
                System.out.println("获取任务为空");
                return false;
            }
            for (String pic : model.getPicUrls().split(";")) {
                if (pic.equals(";") || pic.equals("")) {
                    continue;
                }
                DownLoadPicture(pic);
                jqhelper.writeSDFileEx2("下载图片：" + pic + "\n", "/sdcard/error.txt");
                System.out.println("下载图片成功");
            }
            backUpId = Integer.parseInt(model.getBackUpId());
            jqhelper.writeSDFileEx(gson.toJson(model), contentSacePath);

            return true;
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * @return
     */
    private boolean ReductionByBackUpId() {
        try {
            if (backUpId == 0) {
                System.out.println("还原数据获取服务器Id为空");
                return false;
            }
            Device.wakeUp();
            Device.pressHome();
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
            UiObject cloud_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("云端还原"));
            if (cloud_btn.waitForExists(5000)) {
                cloud_btn.click();
                sleep(3000);
            }
            UiObject pro_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.littlerich.holobackup:id/txt_projectid"));
            if (pro_btn.waitForExists(5000)) {
                pro_btn.setText(Utf7ImeHelper.e(backUpId + ""));
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
                    jqhelper.saveAsFileWriter(filePathexe, "2");
                    sure_btn.click();
                }
                sleep(10000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex.toString() + "\n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 获取发送博文内容
     *
     * @return
     */
    public SendBlogModel GetBlogContent() {
        try {
            String data = HttpHelper.httpPostForString(this.getTaskPath, "");
            if (data.contains("空")) {
                return null;
            }
            Type type = new TypeToken<SendBlogModel>() {
            }.getType();
            SendBlogModel model = gson.fromJson(data, type);
            return model;
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("获取博文内容异常：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return null;
    }

    /**
     * 短信弹窗判断
     */
    private void ShortMessageJudge() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("关闭应用"));
            if (ob.waitForExists(5000)) {
                ob.click();
                jqhelper.delay(2000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 下载图片到本地
     */
    public void DownLoadPicture(String url) {
        try {
            Random random = new Random(System.currentTimeMillis());
            String name = random.nextInt() + ".jpg";
            String savePath = savePicPath + "/" + "IMG_" + name;
            jqhelper.downloadPicture(url, savePath);
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("下载图片异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    private void SubmitWeiBoRequest(SendBlogModel model, String message) {
        try {
            String postData = String.format("{\"TaskId\":\"%s\",\"Message\":\"%s\",\"Success\":\"%s\"}", new Object[]{model.getTaskId(), message, ""});
            String result = HttpHelper.httpPostForString(this.submitTaskPath, postData);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx(ex + "\n", "/sdcard/error.txt");
        }
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

}
