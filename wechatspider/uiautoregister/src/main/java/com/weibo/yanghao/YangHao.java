package com.weibo.yanghao;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.common.uiControl.UiEdit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class YangHao extends SuperRunner {
    //序列化
    public Gson gson = new Gson();
    //    private static String nickName = "";
    //读取备份文件
    private static String filePath = "/storage/emulated/0/QX_Backup/shellcmd/shellstatus.txt";
    // 读取执行操作 0 数据还原 1 执行下拉任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    // 保存博文内容
    private static String contentPathexe = "/storage/emulated/0/QX_Backup/shellcmd/content.txt";
    // 保存服务器id路径
    private static String saveBackUpIdPath = "/storage/emulated/0/QX_Backup/shellcmd/savebackupid.txt";
    // 保存图片路劲
    private static String savePicPath = "/storage/emulated/0/DCIM/Camera";
    // 获取发博文内容以及评论内容
    private static String contentPath = "http://v3.jqsocial.com:22018/api/phonemanager/getdata";
    // 提交数据
    private static String submitContentPath = "http://v3.jqsocial.com:22018/api/phonemanager/submitdata";
    // 全局昵称显示
    private static String weiBoAccountName = "";
    //
    private static String backUpIds = "";
    // 随机函数
    private static Random random = new Random();

    /**
     * 微博执行主方法
     */
    public void test_weiboexecute() {
        try {
            ShortMessageJudge();
            //获取设备
            Device = getUiDevice();
            while (true) {
                try {
                    // 读取当前执行顺序
                    String execute = readFileContent2(filePathexe);
                    // 获取博文数据先保存到本地
                    if (execute.equals("2")) {
                        Device.wakeUp();
                        close_bj();
                        clear_data();
                        HttpHelper.DeleteFile(new File(savePicPath));
                        HandleDataInfo info = ReadHotSearch("西藏杜怀");
                        if (info == null) {
                            jqhelper.writeSDFileEx2("获取数据为空" + "\n", "/sdcard/error.txt");
                            continue;
                        }
                        for (String pic : info.getPicUrls().split(";")) {
                            if (pic.equals(";") || pic.equals("")) {
                                continue;
                            }
                            DownLoadPicture(pic);
                            jqhelper.writeSDFileEx2("下载图片：" + pic + "\n", "/sdcard/error.txt");
                            sleep(2000);
                        }
                        jqhelper.writeSDFileEx(gson.toJson(info), contentPathexe);
                        saveAsFileWriter(filePathexe, "1");
                        //数据还原
                        reductionCloudData();
                    }
                    // 执行养号工作
                    if (execute.equals("1")) {
                        String id = jqhelper.readSDFile(saveBackUpIdPath);
                        if (!id.equals("")) {
                            backUpIds = id;
                        }
                        Device.wakeUp();
                        // 处理保存到本地的数据
                        String info = jqhelper.readSDFile(contentPathexe);
                        if (info.equals("")) {
                            // 未获取到数据
                            saveAsFileWriter(filePathexe, "2");
                            jqhelper.writeSDFileEx2("获取保存本地数据失败" + " \n", "/sdcard/error.txt");
                        }
                        close_bj();
                        Type type = new TypeToken<HandleDataInfo>() {
                        }.getType();
                        HandleDataInfo data = gson.fromJson(info, type);
                        boolean isInHomePage = WeiboLogin();
                        // 获取微博昵称
                        if (isInHomePage) {
                            GetNickName();
                            //设置输入法
                            change_input_method();
                            //点击签到
                            PerdaySignIn();
                            //浏览热搜
                            BrowseHotSearch();
                            //发送博文
                            SendBlog(data.getContent().replace("#", "").replace("@", "艾特") + data.getCheckCode().trim(), data.PicUrls, data.getCheckCode());
                            //回复评论
//                            ReplayComment(data.getComment());
                        }
                    }
                    saveAsFileWriter(filePathexe, "2");
                    HttpHelper.DeleteFile(new File(savePicPath));
                } catch (Exception ex) {
                    System.out.println(ex);
                    jqhelper.writeSDFileEx2("循环执行出现错误：" + ex.toString() + " \n", "/sdcard/error.txt");
                }
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    /**
     * 获取养号数据
     */
    public HandleDataInfo ReadHotSearch(String NickName) {
        try {
            String postData = String.format("{\"NickName\":\"%s\"}", new Object[]{NickName});
            Gson gson = new Gson();
            // request 200
            String rData = HttpHelper.httpPostForString(this.contentPath, postData);
            Type type = new TypeToken<HandleDataInfo>() {
            }.getType();
            if (!rData.contains("空")) {
                Object obj = gson.fromJson(rData, Object.class);
                HandleDataInfo data = gson.fromJson(obj.toString(), type);
                return data;
            } else {
                jqhelper.writeSDFileEx2("返回值为空" + "4 \n", "/sdcard/error.txt");
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("获取养号数据异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
        return null;
    }

    /**
     * 回复评论
     *
     * @param comment
     */
    public void ReplayComment(String comment) {
        try {
            UiObject info_btn = new UiObject(new UiSelector().className("android.view.View").descriptionContains("消息"));
            if (info_btn.waitForExists(2000)) {
                info_btn.click();
                sleep(2000);
            }
            //判断是否收到新的评论
            UiObject com_btn = new UiObject(new UiSelector().className("android.widget.ListView")).
                    getChild(new UiSelector().className("android.widget.LinearLayout").index(2)).
                    getChild(new UiSelector().className("android.widget.LinearLayout")).
                    getChild(new UiSelector().className("android.widget.LinearLayout")).
                    getChild(new UiSelector().className("android.widget.LinearLayout"));
            if (com_btn.waitForExists(2000)) {
                com_btn.click();
                sleep(2000);
                UiObject reply_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("回复").index(0));
                if (reply_btn.waitForExists(2000)) {
                    reply_btn.click();
                    sleep(2000);
                }
                UiEdit reply2_btn = new UiEdit(new UiSelector().className("android.widget.EditText").resourceId("com.sina.weibo:id/edit_view"));
                if (reply2_btn.waitForExists(2000)) {
                    reply2_btn.setText(Utf7ImeHelper.e(comment));
                    sleep(2000);
                }
                UiEdit send_btn = new UiEdit(new UiSelector().className("android.widget.TextView").text("发送"));
                if (send_btn.waitForExists(2000)) {
                    send_btn.click();
                    sleep(2000);
                }
                Device.pressBack();
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    /**
     * 浏览热搜
     */
    public void BrowseHotSearch() {
        try {
            // 点击发现按钮
            UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").descriptionContains("发现"));
            if (search_btn.waitForExists(5000)) {
                search_btn.click();
                jqhelper.delay(20000);
                Device.pressBack();
                sleep(2000);
            }
            int count = random.nextInt(20);
            for (int i = 0; i <= count; i++) {
                ScriptHelper.swipe(0, 1200, 0, 800);
                jqhelper.delay(5000);
            }
            while (true) {
                ob = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.sina.weibo:id/detail_activity_header_left_button_text"));
                if (ob.waitForExists(2000)) {
                    ob.clickAndWaitForNewWindow(2000);
                    jqhelper.delay(2000);
                }
                UiObject zan_btn = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("com.sina.weibo:id/rightButton"));
                if (zan_btn.waitForExists(2000)) {
//                    zan_btn.click();
                    jqhelper.delay(1000 * 30);
                    // 返回发现页
                    Device.pressBack();
                    break;
                }
                ScriptHelper.swipe(0, 1200, 0, 800);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("浏览热搜出现异常:" + ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 每日签到任务
     */
    public void PerdaySignIn() {
        try {
            // 点击首页
            UiObject home_btn = new UiObject(new UiSelector().className("android.view.View").description("首页"));
            if (home_btn.waitForExists(2000)) {
                home_btn.click();
                jqhelper.delay(2000);
            }
            // 点击签到按钮
            UiObject sign_btn = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.sina.weibo:id/redpacketSave"));
            if (sign_btn.waitForExists(2000)) {
                sign_btn.click();
                jqhelper.delay(5000);
            }
//            // 判断弹窗是否存在
//            UiObject close_btn = new UiObject(new UiSelector().className("android.view.View").text("连续签到获得更多积分"));
//            if (close_btn.waitForExists(5000)){
//
//            }
            // 返回首页
            Device.pressBack();
            jqhelper.delay(5000);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
    }

    /**
     * 读取手机文件目录
     *
     * @param filePath1
     * @return
     */
    public static String readFileContent2(String filePath1) {
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
            UiObject cloud_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("云端还原"));
            if (cloud_btn.waitForExists(5000)) {
                cloud_btn.click();
                sleep(3000);
            }
            // 输入项目名称  wbyanghao01
            String project_name = readFileContent2(filePath);
            UiObject pro_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.littlerich.holobackup:id/txt_projectname"));
            if (pro_btn.waitForExists(5000)) {
                pro_btn.setText(Utf7ImeHelper.e(project_name));
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
                    System.out.println("重启中");
                    // 保存项目ID
                    ob = new UiObject(new UiSelector().className("android.widget.TextView").textContains("请重启"));
                    if (ob.waitForExists(2000)) {
                        String backUpId = ob.getText();
                        if (!backUpId.equals("")) {
                            // 云还原成功ID:2234,请重启
                            backUpId = backUpId.split(":")[1].split(",")[0];
                            jqhelper.writeSDFileEx(backUpId, "/storage/emulated/0/QX_Backup/shellcmd/savebackupid.txt");
                        }
                    }
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
     * 微博登录需要进行的操作
     */
    public boolean WeiboLogin() {
        try {
            UiDevice device = getUiDevice();
            device.wakeUp();
            device.pressHome();
            Runtime.getRuntime().exec("am start com.sina.weibo/.SplashActivity");
            sleep(8000);
            // TODO  判断还原数据是否为空
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("用帐号密码登录"));
            if (ob.waitForExists(5000)) {
                System.out.println("微博还原数据为空");
                return false;
            }
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
            if (close_btn.waitForExists(5000)) {
                close_btn.clickAndWaitForNewWindow(2000);
                sleep(8000);
            }
            // 判断是否显示近期修改过密码
            UiObject change_password = new UiObject(new UiSelector().text("由于你近期修改过密码，或开启了登录保护，为保障你的帐号安全，请重新登录。"));
            if (change_password.waitForExists(5000)) {
                jqhelper.writeSDFileEx("修改密码:" + "\n", "/sdcard/error.txt");
            }
            // 点击账号密码登录
            UiObject login_btn = new UiObject(new UiSelector().className("android.widget.Button").text("用帐号密码登录"));
            if (login_btn.waitForExists(3000)) {
                // TODO 当前账号无效
                jqhelper.writeSDFileEx("无账号:" + " \n", "/sdcard/error.txt");
            }
            // 进入首页,观察是否有版本更新弹窗跳出
            sleep(10000);
            // 账号异常返回
            device.pressBack();
            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (update_btn.waitForExists(5000)) {
                update_btn.clickAndWaitForNewWindow(2000);
                sleep(5000);
            }
            UiObject search_btn = new UiObject(new UiSelector().className("android.view.View").descriptionContains("发现"));
            if (search_btn.waitForExists(10000)) {
                return true;
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("微博登录异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 返回登录账号的昵称
     *
     * @return
     */
    public void GetNickName() {
        try {
            UiObject me_btn = new UiObject(new UiSelector().className("android.view.ViewGroup").description("我"));
            if (me_btn.waitForExists(2000)) {
                me_btn.click();
                jqhelper.delay(2000);
            }
            // 点击以后再说
            UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
            if (update_btn.waitForExists(20000)) {
                update_btn.click();
                jqhelper.delay(10000);
            }
            // 获取昵称
            UiObject nickname = new UiObject(new UiSelector().className("android.widget.TextView").resourceId("com.sina.weibo:id/tvNick"));
            if (nickname.waitForExists(2000)) {
                weiBoAccountName = nickname.getText();
                jqhelper.writeSDFileEx2("获取昵称：" + weiBoAccountName + " \n", "/sdcard/error.txt");
            }
            // 判断账号是否异常
            UiObject exception_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("你的微博状态有异常"));
            if (exception_btn.waitForExists(2000)) {
                // 更新账号状态
                SubmitWeiBoRequest(weiBoAccountName, "异常", "");
                jqhelper.delay(2000);
            }
            //进入首页
            UiObject home_btn = new UiObject(new UiSelector().className("android.view.View").description("首页"));
            if (home_btn.waitForExists(5000)) {
                home_btn.click();
                sleep(5000);
            }
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("获取昵称：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    /**
     * 发博文操作
     */
    public boolean SendBlog(String blogContent, String picUrls, String checkCode) {
        try {
            int count = 0;
            if (!picUrls.equals("")) {
                count = picUrls.split(";").length;
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
                search_box.setText(Utf7ImeHelper.e(blogContent));
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
                // TODO 提交更新数据
                SubmitWeiBoRequest(weiBoAccountName, "成功", checkCode);
                sleep(5000);
            }
            UiObject ex_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("解除异常"));
            if (ex_btn.waitForExists(10000)) {
                // todo 更新账号异常状态
                SubmitWeiBoRequest(weiBoAccountName, "异常", checkCode);
                return false;
            }
            return true;
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("发博文异常:" + e + "\n", "/sdcard/error.txt");
        } finally {
            // 删除图片
//            HttpHelper.DeleteFile(new File(savePicPath));
        }
        return false;
    }

    /**
     * 下载图片到本地
     */
    public void DownLoadPicture(String url) {
        try {
            Random random = new Random(System.currentTimeMillis());
            String name = random.nextInt() + ".jpg";
            String savePath = savePicPath + "/" + "IMG_" + name;
            downloadPicture(url, savePath);
            // 移动保存的内容
//            copyFolder(savePicPath2, savePicPath);
//            HttpHelper.DeleteFile(new File(savePicPath2));
        } catch (Exception e) {
            jqhelper.writeSDFileEx2("下载图片异常：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    /**
     * 下载图片
     *
     * @param urlList
     * @param path
     */
    private static void downloadPicture(String urlList, String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            jqhelper.writeSDFileEx2("下载图片异常：" + urlList + path + e.toString() + " \n", "/sdcard/error.txt");
            e.printStackTrace();
        } catch (IOException e) {
            jqhelper.writeSDFileEx2("下载图片异常：" + urlList + path + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    /**
     * 提交微博数据
     *
     * @param nickName
     * @param message
     * @param checkCode
     */
    private void SubmitWeiBoRequest(String nickName, String message, String checkCode) {
        try {
            String postData = String.format("{\"NickName\":\"%s\",\"Message\":\"%s\",\"CheckCode\":\"%s\",\"BackUpId\":\"%s\"}", new Object[]{nickName, message, checkCode, backUpIds});
            String result = HttpHelper.httpPostForString(this.submitContentPath, postData);
            jqhelper.writeSDFileEx2(result, "/sdcard/error.txt");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("微博养号提交请求出现异常", "/sdcard/error.txt");
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
     * 设置快捷输入法
     *
     * @throws IOException
     */
    private void change_input_method() throws IOException {
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
        jqhelper.delay(1000);
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


}

