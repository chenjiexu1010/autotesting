package com.wechat;

import android.bluetooth.BluetoothClass;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.uiControl.UiEdit;
import com.common.weedfs.RequestResult;
import com.common.weedfs.WeedFSClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeqee on 2015/10/19.
 */
public class ExcuteRunner extends SuperRunner {
    private static Date activedatetime = new Date();
    private static Date lastlogintime = new Date();

    private int relaytime = 30;//时间
    private String Host = "222.185.251.62";
    //private String Host = "222.185.195.206";

    //private String JqsocialUrl = "192.168.2.191";
    //private String JqsocialUrl = "www.jqsocial.com:34567";
    private String getWXNamesURL = "http://" + Host + ":30008/api/wx/getwxnamesv2?w=";
    private String updateWXURL = "http://" + Host + ":30008/api/wx/update";
    private String addLogURL = "http://" + Host + ":30008/api/wx/addlog";
    private String WeiXinUpdateIsFriendURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateIsFriend";
    private String WeiXinUpdateIsMaskedURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateIsMasked";
    private String WeiXinUpdateNickNameURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateNickName";
    private String WeiXinUpdateViewTImeURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateViewTime";
    private String path = "/sdcard/tencent/MicroMsg/WeiXin";

    private String pathimg2 = "/sdcard/Tencent/MicroMsg/WeiXin";
    //private String path2 = "/sdcard/";
    private String vpath = "/sdcard/tencent/MicroMsg";
    private String videoPath = "";
    private String istest = "否";
    private Map<String, WechatData> load = new HashMap<String, WechatData>();

    private Map<String, String> replacename = new HashMap();
    private String batchNumber = "";
    private String mobile = "积奇网络";
    private String selfName = "";
    private String cloneid = "0";
    Date lastAgreeDate = new Date(System.currentTimeMillis());

    //region 【初始化数据】
    private boolean initializtion() {

        this.replacename.put("15817984110", "liao930078027");
        this.replacename.put("13038939601", "lipsticks_A");
        this.replacename.put("18151210028", "KGJ18151210028");
        this.replacename.put("494868376", "zhangshaoshao33");
        this.replacename.put("16620002285", "AAKKBV");
        this.replacename.put("317366670", "chfl66");
        this.replacename.put("329233418", "zhongchao769596");
        this.replacename.put("187376214", "iamnotalonebecause--");
        this.replacename.put("15818834740", "liao15818834740");
        this.replacename.put("573689335", "yyg1985666");
        this.replacename.put("2594228637", "mengmengdaxiaoqian");
        this.replacename.put("1195572768", "lanxiezi289212");
        this.replacename.put("1925942753", "in-sneaker");
        this.replacename.put("18258870001", "Newfacenew");
        this.replacename.put("13668999979", "zn13668999979");
        this.replacename.put("421919297", "mnn_0730");
        this.replacename.put("979125156", "Dandan979125156");
        this.replacename.put("17666660392", "mediv666");
        this.replacename.put("31310333", "C-BoAr");
        this.replacename.put("18368859522", "newface0818");
        this.replacename.put("3067896185", "NB7777JJ");
        this.replacename.put("13035103303", "mm334651996");
        this.replacename.put("18350052060", "b88016855");
        this.replacename.put("584544682", "Amy-0905");
        this.replacename.put("18522871365", "yz18522871365");
        this.replacename.put("15588417567", "benbenhenshuai");
        this.replacename.put("13771006111", "liluodelei68686");
        this.replacename.put("18701970772", "hengmiexiu");
        this.replacename.put("18844591211", "miao18844591211");
        this.replacename.put("236266142", "yuki948520");
        this.replacename.put("18267156793", "skin697");
        this.replacename.put("18913605790", "FG18913605790");
        this.replacename.put("328130970", "luckycong007");
        this.replacename.put("Jojosos09", "Jojosos");
        this.replacename.put("13921168251", "hellolove8484");
        this.replacename.put("11053875", "topbiao8");
        this.replacename.put("123091558", "yuanzhifopai");
        this.replacename.put("13784880080", "z13784880080");
        this.replacename.put("13777852226", "yebaobao1520");
        this.replacename.put("13775222554", "Love_doraemen");
        this.replacename.put("13794964758", "Xx13794964758");
        this.replacename.put("56756679", "TopBiao_bing");
        this.replacename.put("18538005302", "Cariooo");
        this.replacename.put("180112234", "langyun");
        this.replacename.put("2510158788", "JXQ98668");
        this.replacename.put("168822299", "zp18801");
        this.replacename.put("335658250", "feifeixiaobaobei001");
        this.replacename.put("235369963", "hansisi725253");
        this.replacename.put("17317228364", "ybs2222222");
        this.replacename.put("423682003", "zhuzhuhuoyuan");
        this.replacename.put("15549652311", "you2015cheng");
        this.replacename.put("13418036313", "w13826143478");
        this.replacename.put("794243489", "xuanxuan20091017");
        this.replacename.put("13818445006", "dreamvvvvb");
        this.replacename.put("2846722983", "zxccctv000000");
        this.replacename.put("2024249621", "Achaoxiezhongku");
        this.replacename.put("741341232", "ttttl520");
        this.replacename.put("13590746960", "LaiHiyan008");
        this.replacename.put("2320242081", "lzc2320242081");
        this.replacename.put("18050560923", "q804104671");
        //this.replacename.put("alicesushop", "alicesu");

        //videoPath = getVideoPath();
        long currentTime = System.currentTimeMillis() + 20 * 60 * 1000;
        lastlogintime = new Date(currentTime);
        jqhelper.reportNormal();
        //AddLog(batchNumber, "部署", "提交");
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Device = getUiDevice();
        try {
            try {
                Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
                mobile = jqhelper.readSDFile("/sdcard/name.txt");


            } catch (IOException e) {
                //System.out.println("文件不存在");

            }
            selfName = mobile.substring(mobile.indexOf("积"));


            getWXNamesURL += Vps;
            if (new File(path).exists()) {
                HttpHelper.DeleteFile(new File(path));
            }
            if (new File(pathimg2).exists()) {
                HttpHelper.DeleteFile(new File(pathimg2));
            }
            if (new File(videoPath).exists()) {
                HttpHelper.DeleteFile(new File(videoPath));
            }
        } catch (Exception e) {
//            System.out.println(e.toString());
            return false;
        }
        jqhelper.killApp(this.PackegName);
        jqhelper.killApp(this.PackegName + ":push");
        sleep(3000);
        restartApp();
        AddLog("888", "是否被外部重启：", "888");
        clearache(true);
        return true;
    }

    //endregion

    //region 【业务逻辑流程执行】 启动方法
    public void testRunner() {
        StartMain();
    }

    public void StartMain() {
        if (!initializtion()) return;//如果初始化失败，则结束
//         zhudongtianjia();
        //循环执行任务
        while (true) {
            try {
                //得到批次号
                batchNumber = UUID.randomUUID().toString();

//                 clearcache();
                //获取微信List
                List<WeiXinNameV2> wx_names = GetWXNames();
                if (wx_names == null || wx_names.size() == 0) {
                    //System.out.println(1);
                    AddLog(batchNumber, "消息", "爬取wx_names返回为空");
                    sleep(60 * 1000);
                    continue;
                }

                AddLog(batchNumber, "消息", "爬取wx_names返回数量：" + wx_names.size());
                for (int i = 0; i < wx_names.size(); i++) {
                    String name = wx_names.get(i).getName();

                    AddLog(batchNumber, "消息", "爬取第" + i + "个，共" + wx_names.size() + "个");

                    String businessTypes = wx_names.get(i).getBusinesstypes();
                    for (int z = 0; z < wx_names.get(i).getUsers().size(); z++) {

                        WeiXinUserInfo weiXinUserInfo = wx_names.get(i).getUsers().get(z);
//                        if(name.equals("mingyun3334"))
//                        {
//
//                            wx_names.get(i).getUsers().get(z).setTime("08/16/2018 23:21:00");
//                            wx_names.get(i).getUsers().get(z).setMaxPickCount(60);
//                        }
//                        else
//                        {
//                            continue;
//                        }
                        if (weiXinUserInfo.getMaxPickCount() == 19) {
                            weiXinUserInfo.setMaxPickCount(50);
                        }
                        weiXinUserInfo.setMaxPickCount(weiXinUserInfo.getMaxPickCount() + 1);
                        if (weiXinUserInfo.getMaxPickCount() == 0) {
                            weiXinUserInfo.setMaxPickCount(1);
                        }

                        if (new Date(System.currentTimeMillis()).after(lastAgreeDate)) {
                            AddLog(batchNumber, "开始同意", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.lastAgreeDate));
                            AgreeFriendTask();
                            lastAgreeDate = new Date(System.currentTimeMillis() + 600000L);
                        }

                        Date localDate = new Date(System.currentTimeMillis());
                        String str3 = new SimpleDateFormat("yyyy-MM-dd").format(localDate);
                        if (localDate.after(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str3 + " 23:35:00"))) {
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(300000);
                            Device.wakeUp();
                            jqhelper.reportNormal();
                            sleep(180000);
                            Device.wakeUp();
                        }
                        for (int j = 0; j < 1; j++) {
                            clearache(false);
                            batchNumber = name;
                            cloneid = wx_names.get(i).getCloneid();
                            if (processOnePerson(name, weiXinUserInfo,
                                    businessTypes, Vps, j)) {
                                break;
                            }
                            sleep(100);
                        }
                    }
                }

                sleep(10000);

                restartApp2();
            } catch (Exception ex) {
                AddLog(batchNumber, "异常", ex.toString());
            }
        }
    }

    public boolean processOnePerson(String name, WeiXinUserInfo weiXinUserInfo, String
            businessTypes, String vps, int j) {
        try {

            //添加日志
            AddLog(batchNumber, "消息", "版本：03，用户：" + name + " 第【" + j + "】轮爬取朋友圈信息");
            //重启app
            //deleteVideo();
            restartApp2();
            //videoPath = "/sdcard/tencent/MicroMsg";
            //deleteVideo();
            //System.out.println(videoPath);

            //AddLog(batchNumber, "部署", "提交");

            //进入通讯录
            if (isAfterLoginMain()) {
                enterFriendsList(name);
                //enterCallList();
            } else {
                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 通讯录进入失败");
                return false;
            }
//            //找到并进入好友信息
//            if (isCallList()) {
//                enterFriendsList(name);
//            } else {
//                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 好友信息进入失败");
//                return false;
//            }
            //找到并进入好友发的图片
            if (isPersonalInfo()) {
                jqhelper.reportNormal();
                enterFreindsPhotos(name);
            } else {
                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 图片进入失败");
                return false;
            }

            //循环保存遍历图片并提交
            if (isImageUI()) {

                boolean res = saveImageAndPost(name, weiXinUserInfo, businessTypes, vps);
//                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 " + res);
                return res;
            } else {
                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 isImageUI进入失败 ");
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】爬取朋友圈异常：" + ex.toString());
        }
        return false;
    }

    //endregion

    //region 【判断界面】
    //region 【是否为登录后主界面面】
    public boolean isUpdate() {
        ob = new UiObject(new UiSelector().className("android.widget.Button").text("立刻安装"));
        if (!ob.waitForExists(5000)) {
            return false;
        }
        return true;
    }

    //endregion
    //region 【是否为登录后主界面面】
    public boolean isAfterLoginMain() {
        try {
            ob = new UiObject(new UiSelector().text("确定"));
            if (ob.waitForExists(1000)) {
                ob.click();
            }
        } catch (Exception e) {
            return false;
        }
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("通讯录"));
        if (!ob.waitForExists(10000)) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为通讯录界面】
    public boolean isCallList() {
        UiObject ob1 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("新的朋友"));
        UiObject ob2 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("群聊"));
        UiObject ob3 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("标签"));
        UiObject ob4 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("公众号"));
        if (!ob1.waitForExists(5000) && !ob2.exists() && !ob3.exists() && !ob4.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否图片拉取界面】
    public boolean isImageUI() {
//        UiObject ob1 = new UiObject(new UiSelector().className("android.widget.TextView").text
//                ("赞"));
//        UiObject ob2 = new UiObject(new UiSelector().className("android.widget.TextView").text
//                ("评论"));
        UiObject ob3 = new UiObject(new UiSelector().className("android.widget.ImageButton").description
                ("更多"));
        //if (!ob3.waitForExists(1500) || !ob2.exists() || !ob3.exists()|| !isImageLoaded()) {
        if (!ob3.waitForExists(1500) || !isImageLoaded()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为个人信息界面】
    public boolean isPersonalInfo() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("详细资料"));
        if (!ob.waitForExists(1000)) {
            return false;
        }
        return true;
    }
    //endregion

    //endregion

    //region 【界面操作】
//region 【进入通讯录】
    private void exitUpdate() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("　取消　"));
            if (ob.waitForExists(3000)) {
                ob.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "取消更新失败", ex.toString());
        }
    }


    private void enterCallList() {
        try {
            UiObject findText = new UiObject(new UiSelector().className(android.widget.TextView
                    .class.getName()).text("通讯录"));
            if (findText.waitForExists(3000)) {
                UiScrollable scrollable = new UiScrollable(new UiSelector().className("android" +
                        ".widget.ListView"));
                scrollable.setAsHorizontalList();
                scrollable.scrollForward();
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "进入通讯录异常", ex.toString());
        }
    }
    //endregion


    //region 【进入可进入的照片】
    private void enterFreindsPhotos(String name) {
        try {
            sleep(2000);
            UiObject OwnPhotos = new UiObject(new UiSelector().className("android.widget" +
                    ".TextView").text("个人相册"));
            if (OwnPhotos.waitForExists(3000)) {

//                AddLog(batchNumber, "个人相册", "用户：" + name + "已经找到个人相册图标");
                UiObject obx = new UiObject(new UiSelector().className("android.widget.TextView").textContains("微信号: "));
                if (obx.exists()) {
                    String weixinhao = obx.getText().replace("微信号: ", "");
                    AddLog(batchNumber, "微信号", "用户：" + name + " 微信号: " + weixinhao + "");
                }
                OwnPhotos.click();
            } else {
                AddLog(batchNumber, "进入图片", "用户：" + name + "未找到可进入的个人相册");
                return;
            }
            //sleep(10000);

            ob = new UiObject(new UiSelector().text("等待"));
            if (ob.waitForExists(1000)) {
                AddLog(batchNumber, "等待", "用户：" + name + "进入相册等待");
                ob.click();
            }
            ob = new UiObject(new UiSelector().className("android.widget.ListView").index(0)).getChild(new UiSelector().className("android.widget.LinearLayout").index(0)).getChild(new UiSelector().className("android.widget.RelativeLayout").index(0)).getChild(new UiSelector().className("android.widget.TextView").index(1));
            if (this.ob.exists()) {
                AddLog(batchNumber, "获得昵称", "昵称：" + this.ob.getText());
                WeiXinUpdateNickName(this.ob.getText(), cloneid + "");
            }
            UiObject obz = new UiObject(new UiSelector().className("android.widget.TextView").text("赞这个封面"));
            for (int i = 0; i < 2; i++) {
                ScriptHelper.swipe(0, 250, 0, 1700);
                sleep(2000);
                if (obz.exists()) {
                    obz.click();
                    sleep(1000L);
                    ScriptHelper.swipe(0, 250, 0, 1700);
                    sleep(2000L);
                }
            }

            //下拉加载操作
            for (int i = 0; i < 5; i++) {
                ScriptHelper.swipe(0, 1700, 0, 250);
                sleep(2000);
            }
            //sleep(1000);
            for (int i = 0; i < 7; i++) {
                ScriptHelper.swipe(0, 250, 0, 1700);
                sleep(2000);

                if (obz.exists()) {
                    obz.click();
                    sleep(1000L);
                    ScriptHelper.swipe(0, 250, 0, 1700);
                    sleep(2000L);
                }
            }
//                sleep(2000);
//            ScriptHelper.swipe(0, 1700, 0, 250);
//            sleep(2000);
//            sleep(2000);
//            ScriptHelper.swipe(0, 1300, 0, 250);
//            sleep(2000);
            //region 【？？？】
            UiObject FirstImage = new UiObject(new UiSelector().className("android.view.View")
                    .description("图片"));
            for (int i = 0; i < 10; i++) {
                UiObject obx1 = new UiObject(new UiSelector().className("android.widget.TextView").text("该朋友暂未开启朋友圈"));

                if (obx1.exists()) {
                    AddLog(batchNumber, "进入图片", "用户：" + name + "朋友圈未打开");
                    WeiXinUpdateIsMasked("1", cloneid + "");
                    return;
                }

                if (FirstImage.waitForExists(1000)) {
                    AddLog(batchNumber, "进入图片", "用户：" + name + "已经找到第一张图片");
                    WeiXinUpdateIsMasked("0", cloneid + "");
                    break;
                } else {
                    AddLog(batchNumber, "进入图片", "用户：" + name + "朋友圈列表页面（图片类型）信息未发现，再往下拉一下");
                    ScriptHelper.swipe(0, 800, 0, 200);

                    sleep(1000);
                    UiObject obx = new UiObject(new UiSelector().className("android.widget.ListView"))
                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                            .getChild(new UiSelector().className("android.widget.LinearLayout"))
                            .getChild(new UiSelector().className("android.widget.LinearLayout"))
                            .getChild(new UiSelector().className("android.widget.ImageView"));
                    if ((!FirstImage.exists()) && obx.waitForExists(1000)) {
                        AddLog(batchNumber, "进入图片", "用户：" + name + "未找到可进入的图片" + obx.getBounds());
                        WeiXinUpdateIsMasked("1", cloneid + "");
                        return;
                    }
                }

            }

            //endregion
            if (FirstImage.waitForExists(1000)) {

//                FirstImage.click();
//                sleep(1000);
//                sleep(1000);
//                scrollToNext();
//                sleep(2000);
                for (int k = 0; k <= 6; k++) {

                    if (!isImageUI()) {
                        FirstImage.click();
                        sleep(1000);

                    } else {

                        break;

                    }
                }
            }
            //AddLog(batchNumber, "进入图片", "用户：" + name + "已点击了第一个图片");
            return;
        } catch (Exception ex) {
            AddLog(batchNumber, "进入个人相册异常", ex.toString());
            return;
        }
    }
    //endregion

    /**
     * 主动添加好友
     */
    public void zhudongtianjia() {
        restartApp2();
        String name = "oots1127";
        try {
            AddLog(this.batchNumber, "zhudong搜索", "主动添加开始");
            ob = new UiObject(new UiSelector().className("android.widget.TextView").description("搜索"));
            if (ob.waitForExists(3000)) {

                ob.click();
            }
            ScriptHelper.switchImeToAdbKeyboard();

            UiEdit edit = new UiEdit(new UiSelector().className("android.widget.EditText")
                    .text("搜索"));
            if (this.replacename.containsKey(name)) {

                name = replacename.get(name);
            }
            if (edit.waitForExists(3000)) {
                edit.setChineseText(name);
            }


            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("微信号: " + name));
            if (ob.exists()) {
                AddLog(this.batchNumber, "zhudong搜索", "zhudong搜索6");
                ob.clickAndWaitForNewWindow();
            } else {

                ob = new UiObject(new UiSelector().className("android.widget.TextView").textContains("查找手机/QQ号"));

                if (ob.exists()) {

                    AddLog(this.batchNumber, "zhudong搜索", "zhudong搜索7");
                    ob.clickAndWaitForNewWindow();


                    ob = new UiObject(new UiSelector().className("android.widget.Button").text("添加到通讯录"));


                    if (ob.waitForExists(1500)) {


                        AddLog(this.batchNumber, "zhudong搜索", "添加好友：" + name);
                        ob.clickAndWaitForNewWindow(2000);
                        ob = new UiObject(new UiSelector().text("发送"));
                        if (ob.waitForExists(2000)) {
                            ob.clickAndWaitForNewWindow(2000);
                            AddLog(this.batchNumber, "zhudong搜索", "主动添加完成");
                        }
                        return;
                    }
                }
            }
            return;
        } catch (Exception ex) {
            AddLog(batchNumber, "用户：" + name + "zhudong进入个人信息异常", ex.toString());
            return;
        }
    }

    //region 【重启微信App】
    private void restartApp() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.killApp(PackegName + ":push");
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");

            ob = new UiObject(new UiSelector().text("很抱歉，“微信”已停止运行。"));
            if (ob.waitForExists(1000)) {
                UiObject q = new UiObject(new UiSelector().text("确定"));
                if (q.waitForExists(1000)) {
                    q.click();
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * 重启App
     */
    private void restartApp2() {
        try {
            Device.wakeUp();
            ob = new UiObject(new UiSelector().text("很抱歉，“微信”已停止运行。"));
            int i = 0;
            while ((GetUIObject("android.widget.TextView", "更新").waitForExists(2000)) && (i < 20)) {
                AddLog(batchNumber, "取消更新", "有取消更新");

                int j = i + 1;
                GetUIObject("android.widget.Button", 0).click();
                i = j;
                if (GetUIObject("android.widget.TextView", "是否取消安装？").waitForExists(2000L)) {
                    AddLog(batchNumber, "取消更新", "有是否取消安装");

                    GetUIObject("android.widget.Button", "是").click();
                    i = j;
                }
            }
            if (this.ob.exists()) {
                UiObject localUiObject = new UiObject(new UiSelector().text("确定"));
                if (localUiObject.waitForExists(1000L)) {
                    localUiObject.click();
                }
            }

            UiObject ob1 = new UiObject(new UiSelector().className("android.widget.TextView").text("我"));
            UiObject ob2 = new UiObject(new UiSelector().className("android.widget.TextView").text("通讯录"));
            i = 0;
            while (i < 16) {
                if ((ob2.exists()) && (ob1.exists())) {
                    return;
                }
                this.Device.pressBack();
                i += 1;
            }
            restartApp();
            return;
        } catch (Exception localException) {
        }
    }


    //region 【拉取图片或视频】
    private boolean saveImageAndPost(String name, WeiXinUserInfo weiXinUserInfo, String
            businessTypes, String vps) {
        List<WechatData> wechatDatas = new ArrayList<WechatData>();
        WechatData wechatData = null;
        int tryCount = 0;
        int zuohuaxiangtong = 0;
        String lastText = "", lastSourceType = "";
        String LastA = "";
        Date lastPublishTime = null;
        boolean isFirst = true;

        while (true) {
            jqhelper.reportNormal();
            if (new File(path).exists()) {
                HttpHelper.DeleteFile(new File(path));
            }
            if (new File(pathimg2).exists()) {
                HttpHelper.DeleteFile(new File(pathimg2));
            }
            if (new File(videoPath).exists()) {
                HttpHelper.DeleteFile(new File(videoPath));
            }
            //AddLog(batchNumber, "部署", "提交");
            sleep(200);
            //region 判断图片是否加载完成
            if (isImageLoaded()) {
                //-1:视频,-2:单图,a/b:多图,空:异常
                String sourceType = GetSourceType();
                if (sourceType == null || sourceType.equals("")) {//异常
                    AddLog(batchNumber, "消息", "用户：" + name + " 资源类型未识别 返回空");
                    tryCount++;
                } else {
                    String text = GetText();
                    Date publishTime = GetDate();


                    String publishTimeStr = "";
                    if (publishTime != null) {
                        publishTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                                (publishTime);
                    }


                    if (text.equals(lastText) &&
                            sourceType.equals(lastSourceType) &&
                            publishTime.equals(lastPublishTime)) {
                        sleep(1000);
                        //AddLog(this.batchNumber, "消息", "左滑相同1，重试第" + zuohuaxiangtong + "次");
                        if (zuohuaxiangtong < 11) {
                            AddLog(this.batchNumber, "消息", "左滑相同，重试第" + zuohuaxiangtong + "次");
                            zuohuaxiangtong++;
                            scrollToNext();
                            continue;
                        } else {
                            zuohuaxiangtong = 0;
                        }
                    } else {
                        zuohuaxiangtong = 0;
                    }
                    Date nowmin = new Date(System.currentTimeMillis());
                    String publishTimeminStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format
                            (publishTime);
                    String nowminstr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format
                            (nowmin);


                    //region 退出判断
                    boolean isExit = false;

                    if (istest.equals("否")) {
                        isExit = isNeedExit(isFirst,
                                text, publishTime, sourceType,
                                lastText, lastPublishTime, lastSourceType, wechatDatas, weiXinUserInfo
                                        .getMaxPickCount(),
                                weiXinUserInfo, name);
                    }
                    if (isFirst && !isExit) {
                        if (publishTimeminStr.equals(nowminstr)) {
                            AddLog(this.batchNumber, "消息", "抓取时间" + publishTimeminStr + "当前时间" + nowminstr + "分钟数相同，退出");
                            break;
                        } else {
                            AddLog(this.batchNumber, "消息", "抓取时间" + publishTimeminStr + "当前时间" + nowminstr + "分钟数不相同，开始");
                        }


                    }
                    if (isExit) {
                        if (wechatData != null) {
                            AddLog(batchNumber, "消息", " 已经爬取朋友圈数量：" +
                                    wechatDatas.size() + " 用户：" + name + "爬取到朋友圈 图片 " + wechatData
                                    .getPics() + " 发布时间：" + wechatData.getTime() + " 类型：" +
                                    lastSourceType);
                            if (!wechatData.getPics().equals("")) {
                                wechatDatas.add(wechatData);
                            } else {
                                AddLog(batchNumber, "消息", "用户：" + name + " " + wechatData
                                        .getContent() + " 没有资源 不入库");
                            }
                        }
                        break;
                    }
                    //endregion

                    //region 处理
                    AddLog(batchNumber, "资源类型", sourceType);
                    if (text != null && !text.equals("") && publishTime != null) {
                        tryCount = 0;
                        boolean isNew = false;

                        if (lastSourceType.contains("/")) {
                            String a = lastSourceType.split("/")[0].trim();
                            String b = lastSourceType.split("/")[1].trim();

                            if (a.equals(b)) {
                                isNew = true;
//                                AddLog(batchNumber, "多图判断", lastSourceType + " " + a + " " + b + " "
//                                        + a.equals(b));
                            } else {
                                if (sourceType.contains("/")) {
                                    String thisa = sourceType.split("/")[0].trim();
                                    if (Integer.parseInt(thisa) <= Integer.parseInt(a)) {
                                        AddLog(batchNumber, "isimage未加载isnew", "isimage未加载isnew" + " " + lastSourceType + " " + sourceType);
                                        isNew = true;
                                    }
                                }
                            }
//                            else if(Integer.parseInt(a)<=Integer.parseInt(LastA)){
//
//                                isNew = true;
//                            }
                            LastA = a;
                        }
                        if (lastSourceType.equals("-1") || lastSourceType.equals("-2")) {
                            isNew = true;
                        }

                        if (wechatData == null) {
                            wechatData = new WechatData(name, text, publishTimeStr, sourceType, "");
                        } else if (!wechatData.getContent().equals(text) ||
                                !wechatData.getTime()
                                        .equals(publishTimeStr) || isNew) {
                            AddLog(batchNumber, "消息", "已经爬取朋友圈数量：" +
                                    wechatDatas.size() + "用户：" + name + "爬取到朋友圈 图片 " + wechatData
                                    .getPics() + " 文本：" + wechatData.getContent() + " 发布时间：" + wechatData.getTime() + " 类型：" +
                                    lastSourceType);
                            if (!wechatData.getPics().equals("")) {
                                wechatDatas.add(wechatData);
                            } else {
                                AddLog(batchNumber, "消息", "用户：" + name + " " + wechatData
                                        .getContent() + " 没有资源 不入库");
                            }
                            wechatData = new WechatData(name, text, publishTimeStr, sourceType, "");
                        }

                        //type: -1 视频
                        if (sourceType.equals("-1")) {//视频
                            for (int i = 0; i < 2; i++) {
                                String videoID = saveVideo(weiXinUserInfo.getUserID());
                                if (videoID != null && !videoID.equals("") && !videoID.equals
                                        ("null") && !videoID.equals
                                        ("|")) {
                                    wechatData.setPics(videoID);
                                    break;
                                } else {
                                    sleep(60000);
                                    AddLog(batchNumber, "消息", "用户：" + name + "视频上传失败 返回 " +
                                            videoID +
                                            "重试次数：" + i);
                                }
                            }
                        } else {//图
                            for (int i = 0; i < 2; i++) {

                                String imgID = saveImage(weiXinUserInfo.getUserID());


                                if (imgID != null && !imgID.equals("") && !imgID.equals("null")) {
                                    if (wechatData.getPics().equals("")) {
                                        wechatData.setPics(imgID);
                                    } else {
                                        wechatData.setPics(wechatData.getPics() + ";" + imgID);
                                    }
                                    break;
                                } else {
                                    sleep(60000);
                                    AddLog(batchNumber, "消息", "用户：" + name + "图片上传失败 返回 " + imgID +
                                            "重试次数：" + i);
                                    wechatData.setPics("");
                                }
                            }
                        }
                    }

                    lastText = text;
                    lastPublishTime = publishTime;
                    lastSourceType = sourceType;
                    //endregion
                }
            } else {
                tryCount++;
            }
            //endregion

//            if (tryCount > 1 && wechatDatas.size() < 20) {
//                AddLog(batchNumber, "消息", "用户：" + name + "不在图片页面或者无法识别资源类型 需重试 ");
//                return false;
//            }
            if (tryCount > 1 && wechatDatas.size() >= 20) {
                AddLog(batchNumber, "消息", "用户：" + name + "已获取足够数据 无需重试 ");
                break;
            }

            //region 向右滑
            scrollToNext();

            isFirst = false;
            //endregion
        }
        // AddLog(batchNumber, "上传数据信息", "该用户上传数量 " +wechatDatas.size() );
        if (wechatDatas.size() > 0) {
            AddLog(batchNumber, "上传提交", "用户：" + name + " 上传提交数量：" + wechatDatas.size());
            for (int i = wechatDatas.size() - 1; i >= 0; i--) {
                WechatData wechat = wechatDatas.get(i);
                UpdateTo(wechat.getName(), weiXinUserInfo.getUserID(), wechat
                                .getTime(), wechat.getContent(), wechat.getPics(), businessTypes,
                        vps, wechat.getSourceType(), i);
                sleep(500);
            }
        }
        return true;
    }

    //图片右滑
    private boolean scrollToNext() {
        try {


            UiScrollable ImageScroll = new UiScrollable(new UiSelector().className("android" +
                    ".widget.Gallery"));
            ImageScroll.setAsHorizontalList();
            if (ImageScroll.waitForExists(3000)) {
                return ImageScroll.scrollForward();
            }

            sleep(2000);
        } catch (Exception e) {
            AddLog(batchNumber, "图片右滑", "图片右滑失败 异常：" + e.toString());
        }
        return false;
    }

    //判断图片加载
    private boolean isImageLoaded() {
        UiObject info = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));
        UiObject info2 = new UiObject(new UiSelector().className("android.widget" +
                ".ProgressBar"));
        //  sleep(1000);
        if (info.exists() && (!info2.exists())) {
//            AddLog(batchNumber, "isimageload2", "isimageload通过");
            return true;
        }

        for (int i = 0; i < 10; i++) {
            try {
                UiObject infoC = new UiObject(new UiSelector().className("android.widget" +
                        ".Gallery")).getChild
                        (new UiSelector().className("android.widget.ImageView"));
                UiObject infoC2 = new UiObject(new UiSelector().className("android.widget" +
                        ".Gallery")).getChild
                        (new UiSelector().className("android.view.View"));
                if (infoC.waitForExists(1000)) {
                    infoC.click();
                } else if (infoC2.exists()) {
                    infoC2.click();
                }
                if (info.waitForExists(1000) && (!info2.exists())) {
//                    AddLog(batchNumber, "isimageload", "isimageload通过");
                    return true;
                }
                sleep(2000);
            } catch (Exception e) {
                AddLog(batchNumber, "判断图片加载", "判断图片加载失败 异常：" + e.toString());
            }
        }
        AddLog(batchNumber, "isimageload", "isimageload不通过");
        return false;
    }

    //退出判断
    private boolean isNeedExit(boolean isFirst,
                               String text, Date publishTime, String sourceType,
                               String lastText, Date lastPublishTime, String lastSourceType,
                               List<WechatData> wechatDatas, int maxCount, WeiXinUserInfo
                                       weiXinUserInfo, String name) {
        Date chkDate = ParseTime(weiXinUserInfo.getTime());
        if (!isFirst) {
            AddLog(batchNumber, "抓取时间", "当前抓取时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (publishTime) + ";之前的抓取时间" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (chkDate));
        }
        if (!publishTime.after(chkDate)) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (publishTime);
            String chkDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (chkDate);
            AddLog(batchNumber, "退出判断", name + "当前朋友圈发布时间：" + dateStr + " 不大于已入库的朋友圈最近发布时间： " +
                    chkDateStr);

            return true;
        }

        if (isFirst) {
            return false;
        }


        if (text.equals(lastText) &&
                sourceType.equals(lastSourceType) &&
                publishTime.equals(lastPublishTime)) {

            AddLog(batchNumber, "退出判断", name + "该朋友圈已完成 当前已获取到朋友圈数量：" + wechatDatas.size());

            return true;

        }


        if (wechatDatas.size() >= maxCount) {
            AddLog(batchNumber, "退出判断等待", name + "当前获取朋友圈数量：" + wechatDatas.size() + " " +
                    "大于等于最大限制获取数量1： " +
                    maxCount);

        }

        if (wechatDatas.size() >= maxCount && !text.equals(lastText) &&
                !sourceType.equals(lastSourceType) &&
                !publishTime.equals(lastPublishTime) && !lastText.equals("") && !lastSourceType.equals("")) {
            AddLog(batchNumber, "退出判断", name + "当前获取朋友圈数量：" + wechatDatas.size() + " " +
                    "大于等于最大限制获取数量： " +
                    maxCount);
            return true;
        }
        return false;
    }

    //region 【获取信息】

    //获取日期
    private Date GetDate() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget" +
                    ".FrameLayout").index(1)).getChild(new UiSelector().className
                    ("android.view" +
                            ".View")).getChild(new UiSelector()
                    .className("android.widget" +
                            ".LinearLayout").index(0)).getChild(new UiSelector().className
                    ("android.widget.LinearLayout").index(1));
            if (ob.waitForExists(3000)) {
                UiObject timeob = ob.getChild(new UiSelector().className("android.widget" +
                        ".TextView").index(0));

                Date date = ParseXYD(timeob.getText());

                return date;
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取日期", "获取日期失败 异常：" + e.toString());
        }
        return null;
    }

    //获取Text
    private String GetText() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget" +
                    ".FrameLayout")).getChild(new UiSelector().className("android.widget" +
                    ".LinearLayout")).getChild(new UiSelector().className("android.widget" +
                    ".LinearLayout")).getChild(new UiSelector().className("android.widget" +
                    ".TextView").index(0));
            String text = ob.getText();
            //  AddLog(batchNumber, "包含回车日志1", "文章："+text.replaceAll("\\n", "|1|").replaceAll("\\r", " ").replaceAll("\\\\", "")
            //        .replaceAll("\"", "").replaceAll("\\t", " "));


            //AddLog(batchNumber, "包含回车日志1", "文章：" +text);
            if (text != null && !text.equals("")) {
//                if(text.contains("八字合婚"))
//                {
//                    try {
//                        AddLog(batchNumber, "包含回车日志", "文章：" + text);
//                    }
//                    catch (Exception e)
//                    {
//                        AddLog(batchNumber, "包含回车日志错误", "包含回车日志错误");
//
//                    }
//                }
                return text.replaceAll("\\n", "|1|").replaceAll("\\r", " ").replaceAll("\\\\", "")
                        .replaceAll("\"", "").replaceAll("\\t", " ");
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取Text", "获取Text失败 异常：" + e.toString());
        }
        return "";
    }

    //获取资源类型
    /*
     * @return -1:视频,-2:单图,a/b:多图,空:异常
     * */
    private String GetSourceType() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget" +
                    ".FrameLayout").index(1)).getChild(new UiSelector().className
                    ("android.view" + ".View")).getChild(new UiSelector().className("android" +
                    ".widget" + ".LinearLayout").index(0)).getChild(new UiSelector().className
                    ("android.widget.LinearLayout").index(1));
            UiObject moreob = ob.getChild(new UiSelector().className("android.widget" +
                    ".TextView").index(1));
            if (moreob.waitForExists(3000)) {
                //返回多图
                String count = moreob.getText();
                return count;
            } else {
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton")
                        .description("更多"));
                for (int i = 0; i < 3; i++) {
                    try {
                        UiObject infoC = new UiObject(new UiSelector().className("android.widget" +
                                ".Gallery")).getChild
                                (new UiSelector().className("android.widget.ImageView"));
                        if (infoC.waitForExists(1000)) {
                            infoC.click();
                        }
                        if (ob.waitForExists(1000)) {
                            break;
                        }
                        sleep(500);
                    } catch (Exception e) {
                        AddLog(batchNumber, "判断图片加载", "判断图片加载失败 异常：" + e.toString());
                    }
                }
                if (ob.waitForExists(3000)) {

                    ob.click();
                }
//                new UiObject(new UiSelector().className("android.widget.TextView")
//                        .description("更多")).click();
                UiObject uo = new UiObject(new UiSelector().className("android.widget" +
                        ".TextView").text("保存图片"));
                if (!uo.waitForExists(3000)) {
                    //返回视频
                    Device.pressBack();
                    return "-1";
                } else {
                    //返回图片
                    Device.pressBack();
                    return "-2";
                }
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取资源类型", "获取资源类型失败 异常：" + e.toString());
        }
        return "";
    }

    //endregion

    //保存图片
    private String saveImage(String UserID) {
        try {
            boolean isLoaded = isImageLoaded();
            //AddLog(batchNumber, "保存图片", "图片加载状态：" + isLoaded);
            if (isLoaded) {
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));

                if (ob.waitForExists(1000)) {

                    ob.click();

                    UiObject saveob = new UiObject(new UiSelector().className("android.widget" + ".TextView").text
                            ("保存图片"));

                    if (saveob.waitForExists(3000)) {

                        saveob.click();
                        if (saveob.exists()) {
                            sleep(3000);
                            saveob.click();
                        }
                    } else {
                        AddLog(batchNumber, "上传图片时间", "未找到保存图片1");
                        sleep(2000);
                        saveob = new UiObject(new UiSelector().className("android.widget" + ".TextView").text
                                ("保存图片"));
                        if (saveob.waitForExists(3000)) {
                            AddLog(batchNumber, "上传图片时间", "仍然存在");
                            saveob.click();

                        } else {
                            AddLog(batchNumber, "上传图片时间", "未找到保存图片2");
                        }
                    }
                }

                String filename = "";
                for (int k = 0; k <= 100; k++) {


                    filename = HttpHelper.GetFileName(new File(path));
                    if (filename != null && !filename.equals("")) {
                        filename = HttpHelper.GetFileName(new File(pathimg2));
                    }
                    if (filename != null && !filename.equals("")) {
                        break;
                    }

                    sleep(300);

                }

                if (filename != null && !filename.equals("")) {
                    long startTime = new Date().getTime();
                    //AddLog(batchNumber, "上传图片", "开始上传");
                    String id = UploadImage(path + "/" + filename, UserID);
                    long finishTime = new Date().getTime();
                    long time = finishTime - startTime;

                    AddLog(batchNumber, "上传图片时间", String.valueOf(time) + "    取得的id：" + id);


                    return id;
                }
                AddLog(batchNumber, "保存图片", "保存图片失败 未找到图片");
            }
        } catch (Exception e) {
            AddLog(batchNumber, "保存图片", "保存图片失败 异常：" + e.toString());
        }
        return "";
    }

    //保存视频
    private String saveVideo(String UserID) {
        try {
            boolean isLoaded = isImageLoaded();
            //AddLog(batchNumber, "保存视频", "视频加载状态：" + isLoaded);
            if (isLoaded) {
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));

                if (ob.waitForExists(1000)) {

                    ob.click();

                    UiObject saveob = new UiObject(new UiSelector().className("android.widget" + ".TextView").text
                            ("保存视频"));

                    if (saveob.waitForExists(3000)) {

                        saveob.click();
                        if (saveob.exists()) {
                            sleep(3000);
                            saveob.click();
                        }
                    } else {
                        AddLog(batchNumber, "上传视频时间", "未找到保存视频1");
                        sleep(2000);
                        saveob = new UiObject(new UiSelector().className("android.widget" + ".TextView").text
                                ("保存视频"));
                        if (saveob.waitForExists(3000)) {
                            AddLog(batchNumber, "上传图片视频", "仍然存在");
                            saveob.click();

                        } else {
                            AddLog(batchNumber, "上传图片视频", "未找到保存视频2");
                        }
                    }
                }

                String filename = "";
                for (int k = 0; k <= 100; k++) {


                    filename = HttpHelper.GetFileName(new File(path));
                    if (filename != null && !filename.equals("")) {
                        filename = HttpHelper.GetFileName(new File(pathimg2));
                    }
                    if (filename != null && !filename.equals("")) {
                        break;
                    }

                    sleep(300);

                }

                if (filename != null && !filename.equals("")) {
                    long startTime = new Date().getTime();
                    //AddLog(batchNumber, "上传视频", "开始上传");
                    String id = UploadVideo(path + "/" + filename, UserID);
                    long finishTime = new Date().getTime();
                    long time = finishTime - startTime;

                    AddLog(batchNumber, "上传视频时间", String.valueOf(time) + "    取得的地址：" + id);


                    return id + "|";
                }
                AddLog(batchNumber, "保存视频", "保存视频失败 未找到视频");
            }
        } catch (Exception e) {
            AddLog(batchNumber, "保存视频", "保存视频失败 异常：" + e.toString());
        }
        return "";
    }

    //endregion

    //endregion

    //region 【获取任务，提交结果】

    //添加日志
    public void AddLog(String targetname, String LogType, String errormsg, String cloneid) {

        try {
            String postdata = String.format("{\"isshow\":\"%s\",\"batchNumber\":\"%s\",\"type\":\"%s\",\"remark\":\"%s\",\"cloneid\":\"%s\",\"code\":\"%s\",\"mobile\":\"%s\",\"LogTargetName\":\"%s\",\"status\":\"%s\"}", new Object[]{0 + "", "", LogType, errormsg, cloneid, "", this.Vps, targetname, 1});

            String rdata = HttpHelper.httpPostForString(this.addLogURL, postdata);
//            System.out.println(errormsg);
            return;
        } catch (Exception ee) {
            //System.out.println("添加日志发生异常" + ee.toString() + " resp:" + str1);
        }
    }

    //添加日志
    public void AddLog(String targetname, String LogType, String errormsg) {

        try {
            Loginfo logoinfo = new Loginfo();
            logoinfo.setIsshow(0);
            logoinfo.setType(LogType);
            logoinfo.setBatchNumber("");
            logoinfo.setRemark(errormsg);
            logoinfo.setCloneid(0 + "");
            logoinfo.setCode("");
            logoinfo.setMobile(this.Vps);
            logoinfo.setLogTargetName(targetname);
            logoinfo.setStatus(1 + "");
            Gson gson = new Gson();
            String json11 = gson.toJson(logoinfo);

            String postdata = json11;
            //  String postdata = String.format("{\"isshow\":\"%s\",\"batchNumber\":\"%s\",\"type\":\"%s\",\"remark\":\"%s\",\"cloneid\":\"%s\",\"code\":\"%s\",\"mobile\":\"%s\",\"LogTargetName\":\"%s\",\"status\":\"%s\"}", new Object[] { 0 + "", "", LogType, errormsg, 0, "", this.Vps, targetname, 1});
//            System.out.println(errormsg);
            String rdata = HttpHelper.httpPostForString(this.addLogURL, postdata);

            return;
        } catch (Exception ee) {
            //System.out.println("添加日志发生异常" + ee.toString() + " resp:" + str1);
        }
    }

    /**
     * 登录信息
     */
    class Loginfo {

        private int isshow;


        private String batchNumber;
        private String type;
        private String remark;
        private String cloneid;
        private String code;
        private String mobile;
        private String LogTargetName;
        private String status;


        public int getIsshow() {
            return isshow;
        }

        public void setIsshow(int isshow) {
            this.isshow = isshow;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCloneid() {
            return cloneid;
        }

        public void setCloneid(String cloneid) {
            this.cloneid = cloneid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLogTargetName() {
            return LogTargetName;
        }

        public void setLogTargetName(String logTargetName) {
            LogTargetName = logTargetName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    //获取微信对象
    private List<WeiXinNameV2> GetWXNames() {
        try {
//            AddLog(batchNumber, "获取微信对象", "getWXNamesURL"+getWXNamesURL );
            String data = HttpHelper.httpGetString2(getWXNamesURL);
//            AddLog(batchNumber, "获取微信对象", "getWXNamesURL"+getWXNamesURL +" " +data.substring(0,10));
            //           String data = "{\"Text\":\"[{\\\"name\\\":\\\"jxm880916\\\",\\\"cloneid\\\":\\\"8924\\\",\\\"type\\\":0,\\\"businesstypes\\\":null,\\\"users\\\":[{\\\"userID\\\":null,\\\"maxPickCount\\\":19,\\\"time\\\":\\\"04\\/01\\/2018 08:12:00\\\"}]}]\"}";
            //System.out.println(data);
            Type type = new TypeToken<WxData>() {
            }.getType();

            WxData wx = gson.fromJson(data, type);
//            System.out.println(wx.Text);
            List<WeiXinNameV2> list = new ArrayList<WeiXinNameV2>();
            JSONArray results = new JSONArray(wx.Text);
            for (int i = 0; i < results.length(); i++) {
                WeiXinNameV2 a = new WeiXinNameV2();
                List<WeiXinUserInfo> infos = new ArrayList<WeiXinUserInfo>();
                JSONObject result = results.getJSONObject(i);

                a.setName(result.getString("name"));
                a.setType(Integer.parseInt(result.getString("type")));
                a.setBusinesstypes(result.getString("businesstypes"));
                a.setCloneid(result.getString("cloneid"));

                String Users = result.getString("users");
                JSONArray usersResult = new JSONArray(Users);
                for (int k = 0; k < usersResult.length(); k++) {
                    WeiXinUserInfo info = new WeiXinUserInfo();
                    JSONObject usersR = usersResult.getJSONObject(k);
                    info.setTime(usersR.getString("time"));
                    //info.setTime("12/21/2015 5:30:58");
                    info.setMaxPickCount(Integer.parseInt(usersR.getString("maxPickCount")));
                    info.setUserID(usersR.getString("userID"));
                    //info.setUserID("1");
                    infos.add(info);
                }
                a.setUsers(infos);
                list.add(a);
            }
            return list;
        } catch (Exception ex) {
            AddLog(batchNumber, "获取微信对象", "获取微信对象 异常：" + ex.toString());
            return null;
        }
    }

    // 提交数据
    public String UpdateTo(String name, String userID, String timeString, String content, String
            pics, String businessTypes, String vps, String sourceType, int index) {
        String postData = "";
        String rexp = "[0-5][0-9]:[0-5][0-9]$";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(content);

        boolean isriqi = mat.find();
        if (isriqi) {
            content = "【分享图片】";
        }
        if (pics == null || pics.trim().equals("") || pics.trim().equals("|")) {
            AddLog(batchNumber, "上传朋友圈失败", "没有资源 不入库 pics:" + pics + " index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneid);
            return "";
        }
        try {
            postData = String.format("{\"wxname\":\"%s\",\"uid\":\"%s\"," +
                            "\"time_string\":\"%s\",\"content\":\"%s\",\"pics\":\"%s\"," +
                            "\"businessTypes\":\"%s\",\"vps\":\"%s\",\"sourceType\":\"%s\",\"cloneid\":\"%s\"}",
                    name,
                    userID,
                    timeString,
                    content,
                    pics,
                    businessTypes,
                    vps,
                    sourceType, cloneid);
//            String resp="";
            String resp = HttpHelper.httpPostForString(updateWXURL, postData);
            AddLog(batchNumber, "上传朋友圈成功", "index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneid);
            return resp;
        } catch (Exception ex) {
            try {
                postData = String.format("{\"wxname\":\"%s\",\"uid\":\"%s\"," +
                                "\"time_string\":\"%s\",\"content\":\"%s\",\"pics\":\"%s\"," +
                                "\"businessTypes\":\"%s\",\"vps\":\"%s\",\"sourceType\":\"%s\",\"cloneid\":\"%s\"}",
                        name,
                        userID,
                        timeString,
                        stringToJson(content),
                        pics,
                        businessTypes,
                        vps,
                        sourceType, cloneid);
//                String resp2="";
                String resp2 = HttpHelper.httpPostForString(updateWXURL, postData);
                AddLog(batchNumber, "上传朋友圈成功2", "index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneid);
                return resp2;
            } catch (Exception ex2) {
                AddLog(batchNumber, "上传朋友圈异常", "微信：" + name + "上传朋友圈异常 " + ex2.toString());
                AddLog(batchNumber, "上传朋友圈异常", postData);
            }
        }
        return "";
    }

    public static String stringToJson(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
//["\u0000", "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\b", "\t", "\n", "\u000b", "\f", "\r", "\u000e", "\u000f", "\u0010", "\u0011", "\u0012", "\u0013", "\u0014", "\u0015", "\u0016", "\u0017", "\u0018", "\u0019", "\u001a", "\u001b", "\u001c", "\u001d", "\u001e", "\u001f", "\"", "\\"];

            char c = s.charAt(i);
            switch (c) {
                case '\u0000':
                    break;
                case '\u0001':
                    break;
                case '\u0002':
                    break;
                case '\u0003':
                    break;
                case '\u0004':
                    break;
                case '\u0005':
                    break;
                case '\u0006':
                    break;
                case '\u0007':
                    break;
                case '\u000e':
                    break;
                case '\u000f':
                    break;
                case '\u0010':
                    break;
                case '\u0011':
                    break;
                case '\u000b':
                    break;
                case '\u0012':
                    break;
                case '\u0013':
                    break;
                case '\u0014':
                    break;
                case '\u0015':
                    break;
                case '\u0016':
                    break;
                case '\u0017':
                    break;
                case '\u0018':
                    break;
                case '\u0019':
                    break;
                case '\u001a':
                    break;
                case '\u001b':
                    break;
                case '\u001c':
                    break;
                case '\u001d':
                    break;
                case '\u001e':
                    break;
                case '\u001f':
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
                    break;

            }

        }
        return sb.toString();
    }

    //上传图片
    private String UploadImage(String filename, String userId) {
        try {
            WeedFSClient localWeedFSClient = new WeedFSClient("222.185.251.62", "9933");
            RequestResult rs = null;
            //int i = 0;
            for (int i = 0; i < 3; i++) {
                rs = localWeedFSClient.write(filename);
                if (rs.isSuccess()) {
                    if (rs.isSuccess()) {
//                        String md5 = md5HashCode32(filename);
//                        AddLog(batchNumber, "md5", "md5:"+md5,cloneid);
                        String path = "http://" + rs.getPublicURL() + "/" + rs.getFid();
                        return path;
                    }
                }

            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传图片错误[Android]", ex.toString() + "返回结果：" + ex);
        }
        return "";


    }
    //endregion

    //上传视频
    private String UploadVideo(String filename, String userId) {
        try {
            WeedFSClient localWeedFSClient = new WeedFSClient("222.185.251.62", "9933");
            RequestResult rs = null;
            //int i = 0;
            for (int i = 0; i < 3; i++) {
                rs = localWeedFSClient.write(filename);
                if (rs.isSuccess()) {
                    if (rs.isSuccess()) {
//                        String md5 = md5HashCode32(filename);
                        String path = "http://" + rs.getPublicURL() + "/" + rs.getFid();
                        return path;
                    }
                }

            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传视频错误[Android]", ex.toString() + "返回结果：" + ex);
        }
        return "";
    }
    //endregion

    //region 【Utils】

    //region Date处理
    private Date ParseXYD(String s) {
        // 昨天 18:03
        if (s.contains("月")) {
            return ParseXYD1(s);
        } else {
            Date d = ParseXYD0(s);
            return d;
        }
    }

    private Date ParseXYD0(String s) {
        //String s = "11月20日 11:23";
        String text = "(\\d{2}):(\\d{2})";
        boolean yestday = false;
        if (s.contains("昨天")) {
            text = "昨天\\s+(\\d{2}):(\\d{2})";
            yestday = true;
        }
        Pattern pattern = Pattern.compile(text);

        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            String hh = matcher.group(1).toString();
            String ss = matcher.group(2).toString();

            Date now = new Date();

            String.valueOf(now.getYear());

            String astr = new SimpleDateFormat("yyyy-MM-dd").format(now);
            // 11/26/2015 12:08:23
            String str = String.format(" %s:%s:00",
                    hh,
                    ss
            );
            str = astr + str;
            Date d = ParseTime1(str);
            if (yestday) {
                d = addMinutes(d, -60 * 24);
            }
            return d;
        } else {
            return null;
        }
    }

    private Date ParseXYD1(String s) {
        //String s = "11月20日 11:23";
        //Pattern pattern = Pattern.compile("(\\d{2})月(\\d{2})日\\s+(\\d{2}):(\\d{2})");

        //Matcher matcher = pattern.matcher(s);

        try {
            String yy = s.substring(0, s.indexOf("年"));
            String mm = s.substring(s.indexOf("年") + 1, s.indexOf("月"));
            String dd = s.substring(s.indexOf("月") + 1, s.indexOf("日"));
            String hh = s.substring(s.indexOf(" ") + 1, s.indexOf(":"));
            String ss = s.substring(s.indexOf(":") + 1);

            // 11/26/2015 12:08:23
            String str = String.format("%s/%s/%s %s:%s:00",
                    mm,
                    dd,
                    yy,// String.valueOf(new Date().getYear()),
                    hh,
                    ss
            );
            Date d = ParseTime(str);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private Date ParseTime(String dateString) {
        if (dateString.equals("")) return null;
        Date date = null;
        try {
            dateString = dateString.replaceAll("/", "-");
            // 11/26/2015 12:08:23
            // yyyy-MM-dd'T'HH:mm+s:SSSS
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            date = format.parse(dateString);
            //format = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            //dateString = format.format(date);
        } catch (Exception ex) {
            AddLog(batchNumber, "解析时间字符出错", ex.toString());
        }
        return date;
    }

    private Date ParseTime1(String dateString) {
        if (dateString.equals("")) return null;
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateString);
            //format = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            //dateString = format.format(date);
        } catch (Exception ex) {
            AddLog(batchNumber, "解析时间字符出错", ex.toString());
        }
        return date;
    }

    public Date addMinutes(Date d, int minutes) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.MINUTE, minutes);
        d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return d;
    }
    //endregion

    //获取用户video路径
    private String getVideoPath() {

        List<String> list = new ArrayList<String>();
        File file = new File(videoPath);
        if (file.exists() == false) {
            return "";
        } else {
            if (file.isFile()) {
                return file.getName();
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    // file.delete();
                    return "";
                }
                Arrays.sort(childFile, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified();
                        if (diff < 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    }

                    public boolean equals(Object obj) {
                        return true;
                    }
                });
                for (File f : childFile) {
                    if (f.getName().length() >= 32) {
                        list.add(f.getName());

                    }
                }
                if (list.size() == 1) {

                    return videoPath + "/" + list.get(0) + "/video";
                } else if (list.size() == 0) {

                    AddLog(batchNumber, "getVideoPath", "未找到文件夹");
                    return videoPath;
                } else {

                    AddLog(batchNumber, "getVideoPath", "出现多个文件夹");
                    return videoPath + "/" + list.get(list.size() - 1) + "/video";
                }
                // file.delete();
            }
        }
        return "";
    }


    private String getVideoPath2() {

        List<String> list = new ArrayList<String>();
        File file = new File(vpath);
        if (file.exists() == false) {
            return "";
        } else {
            if (file.isFile()) {
                return file.getName();
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    // file.delete();
                    return "";
                }
                Arrays.sort(childFile, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified();
                        if (diff < 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    }

                    public boolean equals(Object obj) {
                        return true;
                    }
                });
                for (File f : childFile) {

                    if (f.getName().length() >= 32) {
                        if (f.listFiles().length > 0) {


                            AddLog(batchNumber, "getVideoPath", "出现多个文件夹");
                            return vpath + "/" + f.getName() + "/video";
                        }

                    }
                }
                if (list.size() == 1) {

                    return videoPath + "/" + list.get(0) + "/video";
                } else if (list.size() == 0) {

                    AddLog(batchNumber, "getVideoPath", "未找到文件夹");
                    return videoPath;
                } else {

                    AddLog(batchNumber, "getVideoPath", "出现多个文件夹");
                    return videoPath + "/" + list.get(list.size() - 1) + "/video";
                }
                // file.delete();
            }
        }
        return "";
    }


    //获取video名
    private String getVideoName() {
        File file = new File(videoPath);
        if (file.exists() == false) {
            return "";
        } else {
            if (file.isFile()) {
                return file.getName();
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    // file.delete();
                    return "";
                }
                for (File f : childFile) {
                    if (f.getName().endsWith(".mp4")) {
                        return f.getName();
                    }
                }
            }
        }
        return "";
    }

    //endregion

    //region 【进入好友列表】

    public void inputText(String str) {
        for (int i = 0; i < str.length(); i++) {
            ScriptHelper.input(str.substring(i, i + 1));
            sleep(200);
        }
    }


    private void enterFriendsList(String name) {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.TextView").description("搜索"));
            if (ob.waitForExists(3000)) {

                ob.click();
            }
            //切换输入法
            ScriptHelper.switchImeToAdbKeyboard();

            UiEdit edit = new UiEdit(new UiSelector().className("android.widget.EditText")
                    .text("搜索"));
            if (this.replacename.containsKey(name)) {

                name = replacename.get(name);
            }
            if (edit.waitForExists(3000)) {
                edit.setChineseText(name);
            }


            ob = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
            if (ob.waitForExists(1500)) {

                ob.clickAndWaitForNewWindow();


            } else {
                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("微信号: " + name));
                if (ob.exists()) {
                    ob.clickAndWaitForNewWindow();
                } else {

                    ob = new UiObject(new UiSelector().className("android.widget.TextView").textContains("查找手机/QQ号"));

                    if (ob.exists()) {

                        AddLog(this.batchNumber, "搜索", "搜索7");

                        new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0)).getChild(new UiSelector().className("android.widget.TextView")).clickAndWaitForNewWindow();

                        UiObject localUiObject = new UiObject(new UiSelector().text("详细资料"));

                        if (!localUiObject.waitForExists(1500)) {

                            ob.clickAndWaitForNewWindow();
                        }

                        if (localUiObject.waitForExists(1500)) {

                            ob = new UiObject(new UiSelector().className("android.widget.Button").text("添加到通讯录"));

                            UiObject obx = new UiObject(new UiSelector().className("android.widget.Button").text("发消息"));

                            if (ob.waitForExists(1500)) {


                                AddLog(this.batchNumber, "进入个人信息错误2", "未找到好友：" + name);

                                WeiXinUpdateIsFriend("0", cloneid);
                                return;
                            }

                            if (obx.waitForExists(1500)) {
                                AddLog(this.batchNumber, "发消息", "找到发消息");
                                obx.clickAndWaitForNewWindow();
                            }
                        }
                    } else {
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").textContains(name));
                        if (ob.exists()) {
                            if (ob.getText().contains("查找微信号") || ob.getText()
                                    .contains("搜一搜")) {
                                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("联系人"));
                                UiObject ob11 = new UiObject(new UiSelector().className("android.widget.TextView").text("最常使用"));
                                if (ob.exists() || ob11.exists()) {
                                    ob = new UiObject(new UiSelector().className("android.widget.ListView"))
                                            .getChild(new UiSelector().className("android.widget.RelativeLayout").index(1))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                                            .getChild(new UiSelector().className("android.widget.TextView").index(0))
                                    ;
//                                        //ob = new UiObject(new UiSelector().className("android.widget.TextView").);
//                                        ob =  new UiObject(new UiSelector().className("android.widget.RelativeLayout"))
//
//                                                .getChild(new UiSelector().className("android.widget.TextView"));
                                    if (ob.exists()) {
                                        // UiObject ob2 =  ob.getFromParent(new UiSelector().className("android.widget.LinearLayout")).getChild(new UiSelector().className("android.widget.TextView"));
                                        if (ob.getText().equals("大倜傥")) {
                                            ob.clickAndWaitForNewWindow(2000);
                                        } else {
                                            AddLog(this.batchNumber, "可能非全字匹配2", name + " " + ob.getText());
                                            //
                                            return;
                                        }
                                    }
                                } else {
                                    AddLog(batchNumber, "进入个人信息错误2", "未找到好友：" + name);
                                    WeiXinUpdateIsFriend("0", cloneid);
                                    return;
                                }
                            } else {
                                AddLog(this.batchNumber, "可能非全字匹配", name + " " + ob.getText());
                                ob.clickAndWaitForNewWindow();
                            }
                        }


                    }


                }
            }
//            }

            new UiObject(new UiSelector().className("android.widget.ImageButton").description
                    ("聊天信息")).clickAndWaitForNewWindow();
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(0))
                    .clickAndWaitForNewWindow();
            WeiXinUpdateViewTime(cloneid);
            WeiXinUpdateIsFriend("1", cloneid);
            return;
        } catch (Exception ex) {
            AddLog(batchNumber, "用户：" + name + "进入个人信息异常", ex.toString());
            return;
        }
    }
    //endregion

    public String md5HashCode32(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();

            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = md.digest();
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;//解释参见最下方
                if (val < 16) {
                    /**
                     * 如果小于16，那么val值的16进制形式必然为一位，
                     * 因为十进制0,1...9,10,11,12,13,14,15 对应的 16进制为 0,1...9,a,b,c,d,e,f;
                     * 此处高位补0。
                     */
                    hexValue.append("0");
                }
                //这里借助了Integer类的方法实现16进制的转换
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void WeiXinUpdateIsFriend(String isfriend, String cloneid) {
        try {
            String postdata = String.format("{\"IsFriend\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{isfriend + "", cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateIsFriendURL, postdata);
            return;
        } catch (Exception paramString1) {
//            System.out.println("跟新状态发生异常" + paramString1.toString() + " resp:" + "");
        }
    }

    public void WeiXinUpdateIsMasked(String IsMasked, String cloneid) {
        try {
            String postdata = String.format("{\"IsMasked\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{IsMasked + "", cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateIsMaskedURL, postdata);
            return;
        } catch (Exception paramString1) {
//            System.out.println("跟新状态发生异常" + paramString1.toString() + " resp:" + "");
        }
    }

    public void WeiXinUpdateNickName(String NickName, String cloneid) {
        try {
            String postdata = String.format("{\"NickName\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{NickName, cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateNickNameURL, postdata);
            return;
        } catch (Exception paramString1) {
//            System.out.println("跟新状态发生异常" + paramString1.toString() + " resp:" + "");
        }
    }

    public void WeiXinUpdateViewTime(String cloneid) {
        try {
            String postdata = String.format("{\"cloneid\":\"%s\"}", new Object[]{cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateViewTImeURL, postdata);
            return;
        } catch (Exception paramString) {
//            System.out.println("跟新状态发生异常" + paramString.toString() + " resp:" + "");
        }
    }

    private void GetMenu(int paramInt) {
        int i = this.Device.getDisplayWidth();
        int j = this.Device.getDisplayHeight();
        if (paramInt == 0) {
            this.Device.click(i / 8, j - 50);
            return;
        }
        if (paramInt == 1) {
            this.Device.click(i / 2 - i / 8, j - 50);
            return;
        }
        if (paramInt == 2) {
            this.Device.click(i / 2 + i / 8, j - 50);
            return;
        }
        this.Device.click(i - 50, j - 50);
    }

    private boolean AgreeFriendTask() {


        try {
            restartApp2();
            GetMenu(2);
            GetMenu(1);
            if (new UiScrollable(GetUISelect("android.widget.ListView", 0)).waitForExists(2000)) {
                new UiScrollable(GetUISelect("android.widget.ListView", 0)).flingToBeginning(30);
            }
            UiObject obF = new UiObject(new UiSelector().className("android.widget.TextView").text("新的朋友"));
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("朋友推荐"));
            if (obF.waitForExists(2000)) {
                AddLog(this.batchNumber, "通过好友", "1");
                obF.click();
            } else if (ob.exists()) {

                AddLog(this.batchNumber, "通过好友", "2");
                Device.click(ob.getBounds().left + 28, ob.getBounds().bottom + 10);
            } else {

                AddLog(this.batchNumber, "通过好友", "3");
                return true;
            }
            UiScrollable collection = new UiScrollable(GetUISelect("android.widget.ListView", 0));
            if (!collection.waitForExists(3000)) {
                //logList.add(ScriptHelper.getLogInfo(type, "352", "未找到新朋友列表", request.Account,
                // 0, Vps));
                Device.pressBack();
                return true;
            }
            int cc = collection.getChildCount();
//            System.out.println("新好友列表" + cc);
            for (int i = 0; i < cc; i++) {
                AddLog(this.batchNumber, "通过好友", "22222");
                ob = collection.getChild(GetUISelect("android.widget.RelativeLayout", i)).getChild(GetUISelect("android.widget.Button", "接受", 0));

                if (ob.exists()) {
                    AddLog(this.batchNumber, "通过好友", "33333");
                    ob.clickAndWaitForNewWindow();
                    sleep(3000L);
                    UiObject localObject2 = new UiObject(new UiSelector().textContains("去验证"));
                    if (localObject2.exists()) {
                        localObject2.clickAndWaitForNewWindow(2000L);
                        localObject2 = new UiObject(new UiSelector().className("android.widget.Button").textContains("通过验证"));
                        if (localObject2.waitForExists(3000L)) {
                            localObject2.clickAndWaitForNewWindow(2000L);
                        }
                    }

                    if (GetUIObject("android.widget.TextView", "完成", 0).waitForExists(3000)) {
                        GetUIObject("android.widget.TextView", "完成", 0).click();
                        if (GetUIObject("android.widget.Button", "发消息", 0).waitForExists(2000)) {
                            AddLog(this.batchNumber, "通过好友", "4444");
                        }
                    }


                    Device.pressBack();
                }
            }
            //commandSubmitModel.LogList.add(ScriptHelper.getLogInfo(type, "356", "确认好友成功",
            // request.Account, 1, Vps));
//            if (!newFriend) {
//                return true;
//            }
        } catch (Exception ex) {

            return true;
        } finally {

            return true;
        }
    }

    private void clearache(boolean isneed) {
        String lasttime = "2016-01-01 00:00:00";
        try {
            if (new File("/sdcard/cleardate.txt").exists()) {
                lasttime = jqhelper.readSDFile("/sdcard/cleardate.txt");
            }
            if (isneed) {
                AddLog(this.batchNumber, "清理缓存", "之前清理缓存时间" + lasttime);
            }
            Date lasttimedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lasttime);
            if (new Date().getTime() - (lasttimedate).getTime() >= 172800000L || isneed) {
                AddLog(this.batchNumber, "清理缓存", "清理缓存开始");
                File vpathfile = new File(this.vpath);
                File[] arrayOfFile = (vpathfile).listFiles();
//                int j = arrayOfFile.length;
//                int i = 0;
//                if (i < j)
//                {
                for (int i = 0; i < arrayOfFile.length; i++) {
                    File localFile = arrayOfFile[i];
                    if ((localFile.getName().length() >= 32) && (localFile.isDirectory())) {
                        AddLog(this.batchNumber, "清理缓存", "开始清理文件夹" + localFile.getName());
                        HttpHelper.deleteDir(localFile);
                        AddLog(this.batchNumber, "清理缓存", "结束清理文件夹" + localFile.getName());
                    }
                }
                jqhelper.reportlearDate();
                AddLog(this.batchNumber, "清理缓存", "强关微信开始");
                jqhelper.killApp(this.PackegName);
                jqhelper.killApp(this.PackegName + ":push");
                sleep(3000);
                restartApp();
                AddLog(this.batchNumber, "清理缓存", "强关微信结束");
            }


            return;
        } catch (Exception localException) {
            AddLog(this.batchNumber, "清理缓存错误", localException.getMessage());
        }
    }
}
