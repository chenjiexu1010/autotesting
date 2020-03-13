package com.douyin;

import android.bluetooth.BluetoothClass;
import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.jutf7.Utf7ImeHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weibo.yanghao.HandleDataInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import jdk.nashorn.internal.ir.WhileNode;

public class douyinsupport extends SuperRunner {
    // 日期格式
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    // 获取备份列表
    private static ArrayList<String> RestroeList = null;
    // 获取抖音工作数据
    private static String getDouYinDataPath = "http://v3.jqsocial.com:22018/api/douyinphone/getdata";
    // 提价抖音工作数据
    private static String submitDouYinDataPath = "http://v3.jqsocial.com:22018/api/douyinphone/submitdata";
    //读取执行操作 0 数据还原 1 执行养号任务
    private static String filePathexe = "/storage/emulated/0/QX_Backup/shellcmd/shellexe.txt";
    //保存抖音内容
    private static String contentPathexe = "/storage/emulated/0/QX_Backup/shellcmd/content.txt";

    private static DouYinGetData douYinData = null;

    private static List<String> keyWords = null;
    private static List<String> douyinAccounts = null;
    private static List<String> comments = null;

    private static Random random = new Random();

    public void test_run() {
        try {
            // 获取设备信息
            Device = getUiDevice();
            change_input_method();
            while (true) {
                try {
                    // 读取当前执行顺序
                    String execute = readFileContent(filePathexe);
                    // 数据还原
                    if (execute.equals("0")) {
                        close_bj();
                        // 关闭抖音
                        close_douyin();
                        // 清除抖音数据
                        clear_data();
                        DouYinGetData douYinGetData = ReadHotSearch();
                        if (douYinGetData == null) {
                            jqhelper.writeSDFileEx2("获取数据为空" + "\n", "/sdcard/error.txt");
                            jqhelper.delay(1000 * 60 * 10);
                            continue;
                        }
                        jqhelper.writeSDFileEx(gson.toJson(douYinGetData), contentPathexe);
                        // 数据还原
                        jqhelper.writeSDFileEx2(douYinGetData.Code + " \n", "/sdcard/error.txt");
                        reductionData(Integer.parseInt(douYinGetData.Code));
                    }
                    // 执行养号操作
                    if (execute.equals("1")) {
                        // 处理保存到本地的数据
                        String info = jqhelper.readSDFile(contentPathexe);
                        if (info.equals("")) {
                            // 未获取到数据
                            saveAsFileWriter(filePathexe, "0");
                            jqhelper.writeSDFileEx2("获取保存本地数据失败" + " \n", "/sdcard/error.txt");
                        }
                        close_bj();
                        Type type = new TypeToken<DouYinGetData>() {
                        }.getType();
                        douYinData = gson.fromJson(info, type);
                        if (douYinData == null) {
                            saveAsFileWriter(filePathexe, "0");
                        } else {
                            String[] keyArray = douYinData.KeyWords.split("/");
                            keyWords = Arrays.asList(keyArray);
                            String[] accArray = douYinData.LiveAccounts.split("/");
                            douyinAccounts = Arrays.asList(accArray);
                            String[] commArray = douYinData.Comments.split("/");
                            comments = Arrays.asList(commArray);
                            // 开始养号
                            douyin_execute(Integer.parseInt(douYinData.WorkDay));
                        }
                    }
                } catch (Exception ex) {
                    jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
    }

    /**
     * 执行主体
     */
    public void douyin_execute(int day) {
        try {
            // 打开抖音
            Runtime.getRuntime().exec("am start com.ss.android.ugc.aweme/.splash.SplashActivity");
            jqhelper.delay(10000);
            // 发现通讯录按钮
            UiObject find_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("取消"));
            if (find_btn.waitForExists(5000)) {
                find_btn.click();
                jqhelper.delay(2000);
            }
            // 点击好的按钮
            UiObject ok_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("好的"));
            if (ok_btn.waitForExists(5000)) {
                ok_btn.click();
                jqhelper.delay(2000);
            }
            // 点击两次允许按钮
            UiObject yunxu_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (yunxu_btn.waitForExists(5000)) {
                yunxu_btn.click();
                jqhelper.delay(2000);
                yunxu_btn.click();
                jqhelper.delay(2000);
            }
            // 点击我知道了按钮
            UiObject know_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我知道了"));
            if (know_btn.waitForExists(5000)) {
                know_btn.click();
                jqhelper.delay(2000);
            }
            // 滑动一次
            ScriptHelper.swipe(0, 800, 0, 200);
            // 开始工作
            switch (day) {
                case 1:
                    OneDay();
                    break;
                case 2:
                    TwoDay();
                    break;
                case 3:
                    ThirdDay();
                    break;
                default:
                    jqhelper.writeSDFileEx2("超过指定天数的任务进入", "/sdcard/error.txt");
            }
            saveAsFileWriter(filePathexe, "0");
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
    }

    /**
     * 抖音代运营第一天
     */
    public void OneDay() {
        // 开始时间
        Date date = new Date(System.currentTimeMillis());
        int zanCount = 5;
        while (true) {
            try {
                /*
                    持续一小时，刷推荐页作品 随机点赞5个。每个视频观看30秒
                 */
                if (date.getTime() + 3600000L < System.currentTimeMillis()) {
                    SubmitDouYinData("第一天结束", 0 + "");
                    break;
                }
                // 先sleep在进行点赞
                jqhelper.delay(1000 * 30);
                // 下滑
                ScriptHelper.swipe(0, 800, 0, 200);
                if (zanCount > 0 && random.nextInt(10) >= 5) {
                    //判断点赞量是否超过1万
                    UiObject zan_btn = new UiObject(new UiSelector().className("android.widget.ImageView").textContains("w"));
                    if (zan_btn.waitForExists(5000)) {
                        // 点赞博文
                        UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("选中"));
                        if (follow_btn.waitForExists(2000)) {
                            follow_btn.click();
                            jqhelper.delay(2000);
                            zanCount -= 1;
                        }
                    }
                }
            } catch (Exception ex) {
                jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
            }
        }
    }

    /**
     * 抖音代运营第二天
     */
    public void TwoDay() {
        try {
            /*
                8:00-8:45、12:00-12:45 、21:00-21:45,刷推荐页观看视频  每个视频观看30s
                互动量 关注2个、点赞5个、评论5个、转发3条。作品要求：点赞量在1w以上
                	视频观看结束之后（21:45时间往后），根据提供的抖音号列表中遍历，查看对应的主播是否在直播
             */
            // 六个时间点
//            String time1 = "08:00";
//            String time2 = "08:45";
//            String time3 = "12:00";
//            String time4 = "12:45";
//            String time5 = "21:00";
//            String time6 = "21:45";
//            Date eightTop = simpleDateFormat.parse(time1);
//            Date eightDown = simpleDateFormat.parse(time2);
//            Date twelveTop = simpleDateFormat.parse(time3);
//            Date twelveDown = simpleDateFormat.parse(time4);
//            Date nightNineTop = simpleDateFormat.parse(time5);
//            Date nightNineDown = simpleDateFormat.parse(time6);
            // 夜间时间点
//            String time7 = "21:45";
//            Date nightAfterNine = simpleDateFormat.parse(time7);
            // 当前时间
            Calendar calendar = Calendar.getInstance();
            Date now = new Date(System.currentTimeMillis());
            String now2 = simpleDateFormat.format(now);
            now = simpleDateFormat.parse(now2);
            calendar.setTime(now);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            jqhelper.writeSDFileEx2("当前小时数:" + hour, "/sdcard/error.txt");
            switch (hour) {
                case 8:
                    TwoDayExecute();
                    SubmitDouYinData("上午", 3 + "");
                    break;
                case 12:
                    TwoDayExecute();
                    SubmitDouYinData("中午", 8 + "");
                    break;
                case 21:
                    TwoDayExecute();
                    SubmitDouYinData("晚上", 1 + "");
                    break;
                case 22:
                    WatchLive();
                    SubmitDouYinData("第二天结束", 0 + "");
                    break;
                default:
                    if (hour < 22) {
                        TwoDayExecute();
                        SubmitDouYinData("中午", 3 + "");
                    }
                    if (hour > 22) {
                        WatchLive();
                        SubmitDouYinData("第二天结束", 0 + "");
                    }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
    }

    /**
     * 第二日执行代码     执行45分钟
     */

    public void TwoDayExecute() {
        int follow = 2;
        int zan = 5;
        int comment = 5;
        int forword = 3;
        // 开始时间
        Date date = new Date(System.currentTimeMillis());

        while (true) {
            try {
                // 限时45分钟
                if (date.getTime() + 2700000L < System.currentTimeMillis()) {
                    break;
                }
                // 下滑
                ScriptHelper.swipe(0, 800, 0, 200);
                //判断点赞量是否超过1万
                UiObject zan_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("w"));
                if (!zan_btn.waitForExists(5000)) {
                    jqhelper.delay(40 * 1000);
                    continue;
                }
                //点赞
                if (zan > 0 && random.nextInt(10) >= 5) {
                    UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("选中"));
                    if (follow_btn.waitForExists(2000)) {
                        follow_btn.click();
                        jqhelper.delay(2000);
                        zan -= 1;
                    }
                }
                //关注
                if (follow > 0 && random.nextInt(10) >= 5) {
                    UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.Button").description("关注"));
                    if (follow_btn.waitForExists(2000)) {
                        follow_btn.click();
                        jqhelper.delay(2000);
                        follow -= 1;
                    }
                }
                //评论
                if (comment > 0 && random.nextInt(10) >= 5) {
                    UiObject comment_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("评论"));
                    if (comment_btn.waitForExists(2000)) {
                        comment_btn.click();
                        jqhelper.delay(3000);
                    }
                    // 输入评论
                    UiObject write_comment_btn = new UiObject(new UiSelector().className("android.widget.EditText").text("留下你的精彩评论吧"));
                    if (write_comment_btn.waitForExists(2000)) {
                        write_comment_btn.click();
                        jqhelper.delay(2000);
                        write_comment_btn.setText(Utf7ImeHelper.e(comments.get(random.nextInt(comments.size()))));
                    }
                    // 点击评论并转发按钮
                    UiObject comment_and_forword_btn = new UiObject(new UiSelector().className("android.widget.CheckBox").text("评论并转发"));
                    if (comment_and_forword_btn.waitForExists(2000)) {
                        comment_and_forword_btn.click();
                        jqhelper.delay(2000);
                        forword -= 1;
                    }
                    // 点击小飞机发送
                    UiObject plane_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/a6z"));
                    if (plane_btn.waitForExists(2000)) {
                        plane_btn.click();
                        jqhelper.delay(2000);
                        comment -= 1;
                    }
                    // 点击关闭评论窗口
                    UiObject close_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/kp"));
                    if (close_btn.waitForExists(2000)) {
                        close_btn.click();
                        jqhelper.delay(2000);
                    }
                }
                //转发
                if (forword > 0 && random.nextInt(10) >= 5) {
                    UiObject forward_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("分享"));
                    if (forward_btn.waitForExists(2000)) {
                        forward_btn.click();
                        jqhelper.delay(3000);
                    }
                    UiObject forward2_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("转发"));
                    if (forward2_btn.waitForExists(2000)) {
                        forward2_btn.click();
                        jqhelper.delay(3000);
                    }
                    UiObject send_btn = new UiObject(new UiSelector().className("android.widget.EditText").text("有爱转发 说点好听的"));
                    if (send_btn.waitForExists(2000)) {
                        send_btn.setText(Utf7ImeHelper.e(comments.get(random.nextInt(comments.size()))));
                        jqhelper.delay(2000);
                    }
                    UiObject plane_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/a6z"));
                    if (plane_btn.waitForExists(2000)) {
                        plane_btn.click();
                        forword -= 1;
                        jqhelper.delay(3000);
                    }
                }
                jqhelper.delay(40 * 1000);
            } catch (Exception ex) {
                jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
            }
        }
    }

    /**
     * 第二日晚上看直播 30 分钟 期间评论 1 - 2 条   如果都没在直播 "点击右上角推荐" 随机选择一个直播观看 30 分钟
     */

    public void WatchLive() {
        // 开始时间
        Date date = new Date(System.currentTimeMillis());
        while (true) {
            try {
                // 限时30分钟
                if (date.getTime() + 1800000L < System.currentTimeMillis()) {
                    break;
                }
                jqhelper.writeSDFileEx2("即将点击搜索按钮：" + douyinAccounts + "\n", "/sdcard/error.txt");
                // 点击搜索按钮
                UiObject search_btn = new UiObject(new UiSelector().className("android.widget.Button").description("搜索"));
                if (search_btn.waitForExists(2000)) {
                    jqhelper.writeSDFileEx2("点击搜索按钮：" + "\n", "/sdcard/error.txt");
                    search_btn.click();
                    jqhelper.delay(2000);
                }
                jqhelper.writeSDFileEx2("搜索按钮点击完成" + "\n", "/sdcard/error.txt");
                for (String douyinaccount : douyinAccounts) {
                    if (date.getTime() + 1800000L < System.currentTimeMillis()) {
                        break;
                    }
                    // TODO 需要遍历本地抖音Id 如果无人在直播就点击推荐页
                    UiObject edit_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.ss.android.ugc.aweme:id/ahw"));
                    if (edit_btn.waitForExists(2000)) {
                        // TODO 输入抖音id
                        edit_btn.setText(Utf7ImeHelper.e(douyinaccount));
                        // 模拟回车
                        Device.pressEnter();
                        jqhelper.delay(2000);

                    }
                    UiObject user_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("用户"));
                    if (user_btn.waitForExists(2000)) {
                        user_btn.click();
                        jqhelper.delay(2000);
                    }
                    // 判断用户是否在直播
                    UiObject image_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/bcw"));
                    if (image_btn.waitForExists(2000)) {
                        // 点击头像进入直播页面
                        UiObject live_btn = new UiObject(new UiSelector().className("android.widget.FrameLayout").resourceId("com.ss.android.ugc.aweme:id/bn5"));
                        if (live_btn.waitForExists(2000)) {
                            // 点击头像进入直播页面
                            live_btn.click();
                            // sleep 30 分钟
                            jqhelper.delay(1000 * 60 * 30);
                        }
                    }
                }
                if (date.getTime() + 1800000L < System.currentTimeMillis()) {
                    break;
                }
                // 走到这里说明未观看直播 返回主页
                Device.pressBack();
                jqhelper.delay(2000);
                Device.pressBack();
                jqhelper.delay(2000);
                UiObject live_btn = new UiObject(new UiSelector().className("android.widget.LinearLayout").resourceId("com.ss.android.ugc.aweme:id/cfb"));
                if (live_btn.waitForExists(2000)) {
                    live_btn.click();
                    jqhelper.delay(1000 * 60 * 30);
                }
            } catch (Exception ex) {
                jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
            }
        }
    }

    /**
     * 抖音代运营第三天
     */
    public void ThirdDay() {
        try {
             /*
                   视频观看时间段：每天8:00-8:45、12:00-12:45 21:00-21:45,刷推荐页观看视频
                	根据上传的关键词，随机获取一个进行搜索，例如“化妆教程”，进入“视频”栏目
                	点进视频详情，浏览完整个视频之后切换到下一个(30秒)
                	互动量：关注2个、点赞5个、评论5个、转发3条。作品要求：点赞量在1w以上，同一作品不重复操作。评论内容从上传内容中获取。
              */
             /*
                8:00-8:45、12:00-12:45 、21:00-21:45,刷推荐页观看视频  每个视频观看30s
                互动量 关注2个、点赞5个、评论5个、转发3条。作品要求：点赞量在1w以上
                	视频观看结束之后（21:45时间往后），根据提供的抖音号列表中遍历，查看对应的主播是否在直播
             */
            Calendar calendar = Calendar.getInstance();
            Date now = new Date(System.currentTimeMillis());
            String now2 = simpleDateFormat.format(now);
            now = simpleDateFormat.parse(now2);
            calendar.setTime(now);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            switch (hour) {
                case 8:
                    ThirdDayExecute();
                    SubmitDouYinData("上午", 3 + "");
                    break;
                case 12:
                    ThirdDayExecute();
                    SubmitDouYinData("中午", 8 + "");
                    break;
                case 21:
                    ThirdDayExecute();
                    SubmitDouYinData("晚上", 1 + "");
                    break;
                case 22:
                    WatchLive();
                    SubmitDouYinData("第三天结束", 0 + "");
                    break;
                default:
                    if (hour < 22) {
                        ThirdDayExecute();
                        SubmitDouYinData("上午", 3 + "");
                    }
                    if (hour > 22) {
                        WatchLive();
                        SubmitDouYinData("第三天结束", 0 + "");
                    }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
    }

    /**
     * 第三日执行内容
     */

    public void ThirdDayExecute() {
        /*
            TODO 获取关键词，遍历搜索关键词看视频
         */
        Date date = new Date(System.currentTimeMillis());
        try {
            String keyWord = keyWords.get(random.nextInt(keyWords.size()));
            // 点击搜索按钮
            UiObject search_btn = new UiObject(new UiSelector().className("android.widget.Button").description("搜索"));
            if (search_btn.waitForExists(10000)) {
                search_btn.click();
                jqhelper.delay(2000);
            }
            UiObject edit_btn = new UiObject(new UiSelector().className("android.widget.EditText").resourceId("com.ss.android.ugc.aweme:id/ahw"));
            if (edit_btn.waitForExists(2000)) {
                edit_btn.setText(Utf7ImeHelper.e(keyWord));
                // 模拟回车
//                Device.pressEnter();
                jqhelper.delay(2000);
            }
            // 点击搜索项
            UiObject search_word_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(keyWord));
            if (search_word_btn.waitForExists(10000)) {
                search_word_btn.click();
                jqhelper.delay(2000);
            }
            // 点击视频
            UiObject video_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("视频"));
            if (video_btn.waitForExists(5000)) {
                video_btn.click();
                jqhelper.delay(2000);
            }
            // 进入视频
            UiObject on_video = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/ah"));
            if (on_video.waitForExists(2000)) {
                on_video.click();
                jqhelper.delay(2000);
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2(ex.toString(), "/sdcard/error.txt");
        }
        int follow = 2;
        int zan = 5;
        int comment = 5;
        int forword = 3;
        while (true) {
            try {
                if (date.getTime() + 2700000L < System.currentTimeMillis()) {
                    break;
                }
                // 停留 40 秒
                jqhelper.delay(40 * 1000);
//                //判断点赞量是否超过1万
//                UiObject zan_btn = new UiObject(new UiSelector().className("android.widget.ImageView").textContains("w"));
//                if (!zan_btn.waitForExists(2000)) {
//                    jqhelper.delay(40 * 1000);
//                    ScriptHelper.swipe(0, 800, 0, 200);
//                    continue;
//                }
                //点赞
                if (zan > 0 && random.nextInt(10) >= 5) {
                    Device.click(985, 753);
                    zan -= 1;
                    jqhelper.delay(2000);
//                    UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("选中"));
//                    if (follow_btn.waitForExists(2000)) {
//                        follow_btn.click();
//                        jqhelper.delay(2000);
//                        zan -= 1;
//                    }
                }
                //关注
                if (follow > 0 && random.nextInt(10) >= 5) {
                    Device.click(985, 753);
                    follow -= 1;
                    jqhelper.delay(2000);
//                    UiObject follow_btn = new UiObject(new UiSelector().className("android.widget.Button").description("关注"));
//                    if (follow_btn.waitForExists(2000)) {
//                        follow_btn.click();
//                        jqhelper.delay(2000);
//                        follow -= 1;
//                    }
                }
                //评论
                if (comment > 0 && random.nextInt(10) >= 5) {
                    Device.click(977, 1130);
//                    UiObject comment_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("评论"));
//                    if (comment_btn.waitForExists(2000)) {
//                        comment_btn.click();
//                        jqhelper.delay(3000);
//                    }
                    // 输入评论
                    UiObject write_comment_btn = new UiObject(new UiSelector().className("android.widget.EditText").text("留下你的精彩评论吧"));
                    if (write_comment_btn.waitForExists(2000)) {
                        write_comment_btn.click();
                        jqhelper.delay(2000);
                        write_comment_btn.setText(Utf7ImeHelper.e(comments.get(random.nextInt(comments.size()))));
                    }
                    // 点击评论并转发按钮
                    UiObject comment_and_forword_btn = new UiObject(new UiSelector().className("android.widget.CheckBox").text("评论并转发"));
                    if (comment_and_forword_btn.waitForExists(2000)) {
                        comment_and_forword_btn.click();
                        jqhelper.delay(2000);
                        forword -= 1;
                    }
                    // 点击小飞机发送
                    UiObject plane_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/a6z"));
                    if (plane_btn.waitForExists(2000)) {
                        plane_btn.click();
                        jqhelper.delay(2000);
                        comment -= 1;
                    }
                    // 点击关闭评论窗口
                    UiObject close_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/kp"));
                    if (close_btn.waitForExists(2000)) {
                        close_btn.click();
                        jqhelper.delay(2000);
                    }
                }
                //转发
                if (forword > 0 && random.nextInt(10) >= 5) {
                    Device.click(981, 1342);
//                    UiObject forward_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("分享"));
//                    if (forward_btn.waitForExists(2000)) {
//                        forward_btn.click();
//                        jqhelper.delay(3000);
//                    }
                    UiObject forward2_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("转发"));
                    if (forward2_btn.waitForExists(2000)) {
                        forward2_btn.click();
                        jqhelper.delay(3000);
                    }
                    UiObject send_btn = new UiObject(new UiSelector().className("android.widget.EditText").text("有爱转发 说点好听的"));
                    if (send_btn.waitForExists(2000)) {
                        send_btn.setText(Utf7ImeHelper.e(comments.get(random.nextInt(comments.size()))));
                        jqhelper.delay(2000);
                    }
                    UiObject plane_btn = new UiObject(new UiSelector().className("android.widget.ImageView").resourceId("com.ss.android.ugc.aweme:id/a6z"));
                    if (plane_btn.waitForExists(2000)) {
                        plane_btn.click();
                        forword -= 1;
                        jqhelper.delay(3000);
                    }
                    Device.click(500, 1109);
                }
                // 下滑
                ScriptHelper.swipe(0, 800, 0, 200);
            } catch (Exception e) {
                jqhelper.writeSDFileEx2(e.toString() + "\n", "/sdcard/error.txt");
            }
        }
    }

    /**
     * 提交抖音数据
     */
    private void SubmitDouYinData(String message, String hour) {
        try {
            String code = douYinData.Code;
            String id = douYinData.Id;
            String postData = String.format("{\"Code\":\"%s\",\"Message\":\"%s\",\"Hour\":\"%s\",\"Id\":\"%s\"}",
                    new Object[]{code, message, hour, id});
            HttpHelper.httpPostForString(this.submitDouYinDataPath, postData);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("提交抖音数据出现异常" + ex.toString() + "\n", "/sdcard/error.txt");
        }
    }

    /**
     * 获取养号数据
     */
    public DouYinGetData ReadHotSearch() {
        try {
            Gson gson = new Gson();
            String rData = HttpHelper.httpPostForString(this.getDouYinDataPath, "");
            Type type = new TypeToken<DouYinGetData>() {
            }.getType();
            if (!rData.contains("空")) {
                DouYinGetData data = gson.fromJson(rData, type);
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
     * 变机大师(数据还原)
     *
     * @return
     */
    private boolean reductionData(int count) {
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
                saveAsFileWriter(filePathexe, "1");
                // 拼接出需要备份的data名
                String name = String.format("%s_9.9.0", count + "");
                // 获取滑动元素并滑到相应位置
                UiScrollable scroll_btn = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                scroll_btn.scrollTextIntoView(name);
                // 点击还原微博数据
                UiObject weibo_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
                weibo_btn.clickAndWaitForNewWindow(2000);
                // 点击数据还原
                UiObject data_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("数据还原"));
                data_btn.clickAndWaitForNewWindow(2000);
                jqhelper.delay(10000);
                while (true) {
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
        } catch (Exception ex) {
            jqhelper.writeSDFileEx2("异常reductionData()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return false;
    }

    /**
     * 清除抖音数据  com.ss.android.ugc.aweme/.splash.SplashActivity
     */
    public void clear_data() throws IOException {
        // 关闭app
        Runtime.getRuntime().exec("am force-stop com.ss.android.ugc.aweme");
        jqhelper.delay(2000);
        // 清除app数据
        Runtime.getRuntime().exec("pm clear com.ss.android.ugc.aweme");
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
     * 关闭抖音程序
     *
     * @throws IOException
     */
    private void close_douyin() throws IOException {
        // 关闭变机大师
        Runtime.getRuntime().exec("am force-stop com.ss.android.ugc.aweme");
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
     * 设置快捷输入法
     *
     * @throws IOException
     */
    private void change_input_method() throws IOException {
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
        jqhelper.delay(1000);
    }
}
