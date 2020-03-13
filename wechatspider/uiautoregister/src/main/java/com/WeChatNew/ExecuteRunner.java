package com.WeChatNew;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.common.SuperRunner;
import com.base.jqhelper;
import com.common.helper.ScriptHelper;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.common.uiControl.UiEdit;
import com.common.weedfs.RequestResult;
import com.common.weedfs.WeedFSClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.common.helper.HttpHelper;
import com.wechat.WechatData;
import com.wechat.WeiXinUserInfo;
import com.wechat.WxData;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.UUID;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecuteRunner extends SuperRunner {

    private static Date activedatetime = new Date();
    private static Date lastlogintime = new Date();
    //外网ip
    private String Host = "222.185.251.62";
    //获取微信名称
    private String getWeiXinNameUrl = "http://" + Host + ":30008/api/wx/getwxnamesv2?w=";
    //修改
    private String updateWXUrl = "http://" + Host + ":30008/api/wx/update";
    //添加日志
    private String addLogURL = "http://" + Host + ":30008/api/wx/addlog";
    //更新好友状态
    private String WeiXinUpdateIsFriendURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateIsFriend";
    //更新克隆对象IsMasked
    private String WeiXinUpdateIsMaskedURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateIsMasked";
    //更新克隆对象微信昵称
    private String WeiXinUpdateNickNameURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateNickName";
    //更新克隆对象UpdateTime
    private String WeiXinUpdateViewTimeURL = "http://" + this.Host + ":30008/api/wx/WeiXinUpdateViewTime";
    //微信目录
    private String path = "/sdcard/tencent/MicroMsg/WeiXin";
    //微信图片目录
    private String pathImg2 = "/sdcard/Tencent/MicroMsg/WeiXin";
    //private String path2 = "/sdcard/";
    private String videoPath = "";
    private String vpath = "/sdcard/tencent/MicroMsg";
    //
    private String isTest = "否";
    private String mobile = "积奇网络";
    private String selfName = "";
    private String cloneId = "0";
    private String batchNumber = "";
    //最后同意好友时间
    Date lastAgreeDate = new Date(System.currentTimeMillis());
    private Map<String, String> replacename = new HashMap<String, String>();

    /* 初始化数据*/
    private boolean initialization() {
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
        this.replacename.put("18050560923", "q804104671");
        //this.replacename.put("alicesushop", "alicesu");
        long currentTime = System.currentTimeMillis() + 20 * 60 * 1000;
        lastlogintime = new Date(currentTime);
        jqhelper.reportNormal();
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Device = getUiDevice();
        try {
            try {
                Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
                mobile = jqhelper.readSDFile("/sdcard/name.txt");
                AddLog("手机信息", "获取", "VPS:" + Vps + ",Mobile:" + mobile);
            } catch (IOException e) {
            }
            selfName = mobile.substring(mobile.indexOf("积"));

            getWeiXinNameUrl += Vps;
            if (new File(path).exists()) {
                HttpHelper.DeleteFile(new File(path));
            }
            if (new File(pathImg2).exists()) {
                HttpHelper.DeleteFile(new File(pathImg2));
            }
            if (new File(videoPath).exists()) {
                HttpHelper.DeleteFile(new File(videoPath));
            }
        } catch (Exception e) {
            AddLog("重启", "初始化失败", e.getMessage());
            return false;
        }
        jqhelper.killApp(this.PackegName);
        jqhelper.killApp(this.PackegName + ":push");
        sleep(3000);
        RestartApp();
        AddLog("重启", "重启中", "重启成功");
        ClearCache(true);
        return true;
    }

    //启动方法
    public void testRunner() {
        StartMain();
    }

    //循环执行任务
    public void StartMain() {
        // 初始化
        if (!initialization()) {
            return;
        }
        //循环执行任务
        while (true) {
            try {
                //得到批次号
                batchNumber = UUID.randomUUID().toString();
                //获取微信List
                List<WeiXinNameV2> wx_names = GetWXNames();
                //判断集合是否为空
                if (wx_names == null || wx_names.size() == 0) {
                    AddLog(batchNumber, "消息", "爬取wx_names返回为空");
                    sleep(60 * 1000);
                }
                AddLog(batchNumber, "消息", "爬取wx_names返回数量" + wx_names.size());
                //循环用户执行
                for (int i = 0; i < wx_names.size(); i++) {
                    //获取微信昵称
                    String name = wx_names.get(i).getName();
                    String businessTypes = wx_names.get(i).getBusinesstypes();
                    AddLog(batchNumber, "消息", "爬取第" + i + "个。共" + wx_names.size() + "个");
                    for (int z = 0; z < wx_names.get(i).getUsers().size(); z++) {
                        WeiXinUserInfo weiXinUserInfo = wx_names.get(i).getUsers().get(z);
                        if (weiXinUserInfo.getMaxPickCount() == 19) {
                            weiXinUserInfo.setMaxPickCount(50);
                        }
                        weiXinUserInfo.setMaxPickCount(weiXinUserInfo.getMaxPickCount() + 1);
                        if (weiXinUserInfo.getMaxPickCount() == 0) {
                            weiXinUserInfo.setMaxPickCount(1);
                        }
                        //开始同意好友添加
                        if (new Date(System.currentTimeMillis()).after(lastAgreeDate)) {
                            AddLog(batchNumber, "开始同意", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.lastAgreeDate));
                            //TODO 同意好友操作
                            AgreeFriendTask();
                            //同意好友时间间隔
                            lastAgreeDate = new Date(System.currentTimeMillis() + 600000L);
                        }

                        Date localDate = new Date(System.currentTimeMillis());
                        String str3 = new SimpleDateFormat("yyyy-MM-dd").format(localDate);
                        // 晚上时间大于23：35：00 不工作
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
                            ClearCache(false);
                            batchNumber = name;
                            cloneId = wx_names.get(i).getCloneid();
                            //TODO 克隆操作
                            if (ProcessOnePerson(name, weiXinUserInfo, businessTypes, Vps, j)) {
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

    //克隆朋友圈
    public boolean ProcessOnePerson(String name, WeiXinUserInfo weiXinUserInfo, String businessType, String vps, int j) {
        try {
            //添加日志
            AddLog(batchNumber, "消息", "版本：03，用户：" + name + " 第【" + j + "】轮爬取朋友圈信息");
            //重启App
            restartApp2();
            //判断是否在登录后的主页面 进入通讯录
            if (isAfterLoginMain()) {
                //进入好友列表
                EnterFriendList(name);
            } else {
                AddLog(batchNumber, "消息", "用户:" + name + "第【" + j + "】轮爬取朋友圈信息结束,用户聊天页面进入失败");
                return false;
            }
            //判断是否在用户详情页面
            if (IsPersonalInfo()) {
                jqhelper.reportNormal();
                //进入朋友圈
                EnterFriendPhotos(name);
            } else {
                AddLog(batchNumber, "消息", "用户:" + name + "第【" + j + "】轮爬取朋友圈信息结束,朋友圈界面获取失败");
                return false;
            }
            //判断是否朋友圈页面
            if (IsImageUi()) {
                boolean res = saveImageAndPost(name, weiXinUserInfo, businessType, vps);
                return res;
            } else {
                AddLog(batchNumber, "消息", "用户：" + name + " 第【" + j + "】轮爬取朋友圈信息结束 isImageUI进入失败 ");
            }

        } catch (Exception ex) {
            AddLog(batchNumber, "消息", "用户:" + name + "第【" + j + "】 爬取朋友圈异常" + ex.toString());
        }
        return false;
    }

    /**
     * 拉取图片或视频
     *
     * @param name
     * @param weiXinUserInfo
     * @param businessTypes
     * @param vps
     * @return
     */
    private boolean saveImageAndPost(String name, WeiXinUserInfo weiXinUserInfo, String businessTypes, String vps) {
        List<WechatData> wechatDatas = new ArrayList<WechatData>();
        WechatData wechatData = null;
        int tryCount = 0;
        int zuohuaxiangtong = 0;
        String lastText = "", lastSourceType = "";
        String LastA = "";
        Date lastPublishTime = null;
        boolean isFirst = true;
        while (true) {
            try {
                jqhelper.reportNormal();
                if (new File(path).exists()) {
                    HttpHelper.DeleteFile(new File(path));
                }
                if (new File(pathImg2).exists()) {
                    HttpHelper.DeleteFile(new File(pathImg2));
                }
                if (new File(videoPath).exists()) {
                    HttpHelper.DeleteFile(new File(videoPath));
                }
                sleep(200);
                //判断是否存在图片
                if (IsImageUi()) {
                    //-1:视频,-2:单图,a/b:多图,空:异常
                    String sourceType = GetSourceType();
                    if (sourceType == null || sourceType.equals("")) {//异常
                        AddLog(batchNumber, "消息", "用户：" + name + " 资源类型未识别 返回空");
                        tryCount++;
                    } else {
                        //获取内容
                        String text = GetText();
                        //获取日期
                        Date publishTime = GetDate();
                        //添加日志
                        AddLog(batchNumber, "消息", "用户:" + name + "获取到资源类型:" + sourceType + ",text:" + text + ", PublishTime:" + publishTime);
                        String publishStr = "";
                        if (publishStr != null) {
                            publishStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(publishTime);
                        }
                        // 判断多图是否滑动成功
                        if (text.equals(lastText) && sourceType.equals(lastSourceType) && publishTime.equals(lastPublishTime)) {
                            sleep(1000);
                            if (zuohuaxiangtong < 11) {
                                AddLog(this.batchNumber, "消息", "左滑相同,重试第" + zuohuaxiangtong + "次");
                                zuohuaxiangtong++;
                                //右滑
                                scrollToNext();
                                continue;
                            } else {
                                zuohuaxiangtong = 0;
                            }
                        } else {
                            zuohuaxiangtong = 0;
                        }
                        //时间处理
                        Date nowMin = new Date(System.currentTimeMillis());
                        String publishTimeminStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format
                                (publishTime);
                        String nowminstr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format
                                (nowMin);
                        //region 退出判断
                        boolean isExit = false;
                        if (isTest.equals("否")) {
                            isExit = IsNeedExit(isFirst, text, publishTime, sourceType,
                                    lastText, lastPublishTime, lastSourceType,
                                    wechatDatas, weiXinUserInfo.getMaxPickCount(),
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
                                LastA = a;
                            }
                            if (lastSourceType.equals("-1") || lastSourceType.equals("-2")) {
                                isNew = true;
                            }
                            if (wechatData == null) {
                                wechatData = new WechatData(name, text, publishStr, sourceType, "");
                            } else if (!wechatData.getContent().equals(text) ||
                                    !wechatData.getTime()
                                            .equals(publishStr) || isNew) {
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
                                wechatData = new WechatData(name, text, publishStr, sourceType, "");
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
                    }
                } else {
                    tryCount++;
                }
                if (tryCount > 1 && wechatDatas.size() >= 100) {
                    AddLog(batchNumber, "消息", "用户：" + name + "已获取足够数据 无需重试 ");
                    break;
                }
                if (wechatDatas.size() >= 100) {
                    AddLog(batchNumber, "消息", "用户：" + name + "已获取足够数据");
                    break;
                }
                //region 向右滑
                scrollToNext();
                isFirst = false;
            } catch (Exception ex) {
                try {
                    UiObject uiObject = new UiObject(new UiSelector().className("android.widget.TextView").text("取消"));
                    if (uiObject.waitForExists(2000)) {
                        uiObject.click();
                    } else {
                        // 回退
                        Device.pressBack();
                    }
                } catch (UiObjectNotFoundException e) {
                    AddLog(batchNumber, "异常", "未找到更新取消按钮");
                }
                AddLog(batchNumber, "异常", "拉取朋友圈出现异常");
            }
        }
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

    /**
     * 提交数据
     *
     * @param name
     * @param userID
     * @param timeString
     * @param content
     * @param pics
     * @param businessTypes
     * @param vps
     * @param sourceType
     * @param index
     * @return
     */
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
            AddLog(batchNumber, "上传朋友圈失败", "没有资源 不入库 pics:" + pics + " index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneId);
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
                    sourceType, cloneId);
//            String resp="";
            String resp = HttpHelper.httpPostForString(updateWXUrl, postData);
            AddLog(batchNumber, "上传朋友圈成功", "index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneId);
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
                        sourceType, cloneId);
//                String resp2="";
                String resp2 = HttpHelper.httpPostForString(updateWXUrl, postData);
                AddLog(batchNumber, "上传朋友圈成功2", "index:" + index + " 微信：" + name + " 发布时间： " + timeString + " 内容：" + content, cloneId);
                return resp2;
            } catch (Exception ex2) {
                AddLog(batchNumber, "上传朋友圈异常", "微信：" + name + "上传朋友圈异常 " + ex2.toString());
                AddLog(batchNumber, "上传朋友圈异常", postData);
            }
        }
        return "";
    }

    /**
     * 字符串转Json
     *
     * @param s
     * @return
     */
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

    /**
     * 是否加载
     *
     * @param isFirst
     * @param text
     * @param publishTime
     * @param sourceType
     * @param lastText
     * @param lastPublishTime
     * @param lastSourceType
     * @param wechatDatas
     * @param maxCount
     * @param weiXinUserInfo
     * @param name
     * @return
     */
    private boolean IsNeedExit(boolean isFirst, String text, Date publishTime, String sourceType, String lastText,
                               Date lastPublishTime, String lastSourceType, List<WechatData> wechatDatas, int maxCount, WeiXinUserInfo
                                       weiXinUserInfo, String name) {
        Date chkDate = TimeHandle.ParseTime(weiXinUserInfo.getTime());
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

    /**
     * 保存图片
     *
     * @param UserID
     * @return
     */
    private String saveImage(String UserID) {
        try {
//            boolean isLoaded = isImageLoaded();
            //AddLog(batchNumber, "保存图片", "图片加载状态：" + isLoaded);
            if (true) {
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));
                if (ob.waitForExists(2000)) {
                    ob.click();
                    UiObject saveob = new UiObject(new UiSelector().className("android.widget.TextView").text
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
                        filename = HttpHelper.GetFileName(new File(pathImg2));
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

    /**
     * 保存视频
     *
     * @param UserID
     * @return
     */
    private String saveVideo(String UserID) {
        try {
//            boolean isLoaded = isImageLoaded();
            //AddLog(batchNumber, "保存视频", "视频加载状态：" + isLoaded);
            if (true) {
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
                        filename = HttpHelper.GetFileName(new File(pathImg2));
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

    /**
     * 上传视频
     *
     * @param filename
     * @param userId
     * @return
     */
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

    /**
     * 上传图片
     *
     * @param filename
     * @param userId
     * @return
     */
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

    /**
     * 图片右滑
     *
     * @return
     */
    private boolean scrollToNext() {
        try {
            UiScrollable imageScroll = new UiScrollable(new UiSelector().className("android.view.View").index(0));
            //设置水平滚动
            imageScroll.setAsHorizontalList();
            if (imageScroll.waitForExists(3000)) {
                return imageScroll.scrollForward();
            }
        } catch (Exception e) {
            AddLog(batchNumber, "图片右滑", "图片右滑失败 异常:" + e.toString());
        }
        return false;
    }

    /**
     * 获取日期
     *
     * @return
     */
    private Date GetDate() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.view.View"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(1))
                    .getChild(new UiSelector().className("android.view.View"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                    .getChild(new UiSelector().className("android.widget.TextView").index(0));
            if (ob.waitForExists(3000)) {
                Date date = ParseXYD(ob.getText());
                return date;
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取日期", "获取日期失败 异常：" + e.toString());
        }
        return null;
    }

    /**
     * Date处理
     *
     * @param s
     * @return
     */
    private Date ParseXYD(String s) {
        // 昨天 18:03
        if (s.contains("月")) {
            return TimeHandle.ParseXYD1(s);
        } else {
            Date d = TimeHandle.ParseXYD0(s);
            return d;
        }
    }

    /**
     * 获取text
     *
     * @return
     */
    private String GetText() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.TextView"));
            String text = ob.getText();
            if (text != null && !text.equals("")) {
                return text.replaceAll("\\n", "|1|").replaceAll("\\r", " ").replaceAll("\\\\", "")
                        .replaceAll("\"", "").replaceAll("\\t", " ");
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取Text", "获取Text失败 异常：" + e.toString());
        }
        return "";
    }

    /**
     * 获取资源类型
     *
     * @return
     */
    public String GetSourceType() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout"))
                    .getChild(new UiSelector().className("android.view.View"))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(1))
                    .getChild(new UiSelector().className("android.view.View"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(1));
            UiObject moreOb = ob.getChild(new UiSelector().className("android.widget.TextView").index(1));
            //Text是否存在
            if (moreOb.waitForExists(3000)) {
                //TODO 返回多图
                String text = moreOb.getText();
                return text;
            } else {
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));
                for (int i = 0; i < 3; i++) {
                    try {
                        //TODO android.widget.Gallery
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
                        AddLog(batchNumber, "判断图片加载", "判断图片加载失败 异常!");
                    }
                }
                //判断更多按钮是否存在
                if (ob.waitForExists(3000)) {
                    ob.click();
                }
                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("保存图片"));
                if (ob.waitForExists(3000)) {
                    //返回图片
                    Device.pressBack();
                    return "-2";
                } else {
                    //返回视频
                    Device.pressBack();
                    return "-1";
                }
            }
        } catch (Exception e) {
            AddLog(batchNumber, "获取资源类型", "获取资源类型失败 异常：" + e.toString());
        }
        return "";
    }

    /**
     * 进入朋友圈页面
     */
    private void EnterFriendPhotos(String name) {
        try {
            sleep(2000);
            UiObject ownPhotos = new UiObject(new UiSelector().className("android.widget.LinearLayout").descriptionContains("个人相册"));
            //存在个人相册
            if (ownPhotos.waitForExists(3000)) {
                UiObject obx = new UiObject(new UiSelector().className("android.widget.TextView").textContains("微信号:"));
                if (obx.exists()) {
                    String number = obx.getText().replace("微信号: ", "");
                    AddLog(batchNumber, "微信号", "用户:" + name + "微信号: " + number);
                }
                //进入个人相册
                ownPhotos.click();
            } else {
                AddLog(batchNumber, "进入图片", "用户:" + name + "未找到可进入的相册");
                return;
            }
            //TODO 等待
            ob = new UiObject(new UiSelector().text("等待"));
            if (ob.waitForExists((1000))) {
                AddLog(batchNumber, "等待", "进入相册等待");
                ob.click();
            }
            // 寻找微信名称
            ob = new UiObject(new UiSelector().className("android.widget.ListView").index(0)).
                    getChild(new UiSelector().className("android.widget.LinearLayout").index(0)).
                    getChild(new UiSelector().className("android.widget.RelativeLayout").index(0)).
                    getChild(new UiSelector().className("android.widget.TextView").index(1));
            if (ob.exists()) {
                AddLog(batchNumber, "获取昵称", "昵称:" + this.ob.getText());
                WeiXinUpdateNickName(this.ob.getText(), cloneId + "");
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
                sleep(3000);
            }
            for (int i = 0; i < 7; i++) {
                ScriptHelper.swipe(0, 250, 0, 1700);
                sleep(2000);
                if (obz.exists()) {
                    obz.click();
                    sleep(1000);
                    ScriptHelper.swipe(0, 250, 0, 1700);
                    sleep(2000L);
                }
            }

            UiObject firstImage = new UiObject(new UiSelector().className("android.view.View").
                    description("图片"));
            for (int i = 0; i < 10; i++) {
                UiObject obx1 = new UiObject(new UiSelector().className("android.widget.TextView").text("该朋友暂未开启朋友圈"));
                if (obx1.exists()) {
                    AddLog(batchNumber, "进入图片", "用户:" + name + "朋友圈未打开");
                    WeiXinUpdateIsMasked("1", cloneId + "");
                }
                //存在图片
                if (firstImage.waitForExists(1000)) {
                    AddLog(batchNumber, "进入图片", "用户:" + name + "已经找到第一张图片");
                    WeiXinUpdateIsMasked("0", cloneId + "");
                    break;
                } else {
                    AddLog(batchNumber, "进入图片", "用户:" + name + "朋友圈列表页面(图片类型)信息未发现,在往下拉一点");
                    ScriptHelper.swipe(0, 800, 0, 200);
                    sleep(1000);
                    //下滑判断朋友圈是否加载内容
                    UiObject obx = new UiObject(new UiSelector().className("android.view.View").description("图片").index(0));
                    if ((!firstImage.exists()) && obx.waitForExists(1000)) {
                        AddLog(batchNumber, "进入图片", "用户:" + name + "未找到可进入的图片" + obx.getBounds());
                        WeiXinUpdateIsMasked("1", cloneId + "");
                        return;
                    }
                }
            }
            //存在图片
            if (firstImage.waitForExists(1000)) {
                //循环6次加载
                for (int k = 0; k <= 6; k++) {
                    if (!IsImageUi()) {
                        firstImage.click();
                        sleep(1000);
                    } else {
                        break;
                    }
                }
                return;
            }

        } catch (Exception ex) {
        }
    }

    /**
     * 判断是否朋友圈页面
     *
     * @return
     */
    public boolean IsImageUi() {
        UiObject ob = new UiObject(new UiSelector().className("android.widget.ImageButton").description("更多"));
        return ob.exists();
    }

    /**
     * 更新好友状态
     *
     * @param IsMasked 0 好友 1 非好友
     * @param cloneid
     */
    public void WeiXinUpdateIsMasked(String IsMasked, String cloneid) {
        try {
            String postdata = String.format("{\"IsMasked\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{IsMasked + "", cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateIsMaskedURL, postdata);
        } catch (Exception paramString1) {
//            System.out.println("跟新状态发生异常" + paramString1.toString() + " resp:" + "");
        }
    }

    /**
     * 是否在用户聊天页面
     *
     * @return
     */
    public boolean IsPersonalInfo() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("朋友圈"));
            if (ob.waitForExists(100000)) {
                return true;
            }

        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 搜索好友
     *
     * @param name
     */
    private void EnterFriendList(String name) {
        try {
            // 存在描述为搜索的控件
            ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").description("搜索"));
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
                //输入搜索的微信名称
                edit.setChineseText(name);
            }
            //是否存在名称匹配
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text(name));
            if (ob.waitForExists(2000)) {
                ob.clickAndWaitForNewWindow();
            } else { //不存在名称匹配
                //是否存在查找微信号
                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("微信号: " + name));
                if (ob.waitForExists(1000)) {
                    ob.clickAndWaitForNewWindow(2000);
                    sleep(2000);
                    //用户money0510-
                    ob = new UiObject(new UiSelector().className("android.widget.TextView").text("用户不存在"));
                    if (ob.waitForExists(2000)) {
                        AddLog(this.batchNumber, "用户存在", "用户不存在:" + name);
                        WeiXinUpdateIsFriend("0", cloneId);
                        return;
                    }
                    ob = new UiObject(new UiSelector().className("android.widget.Button").text("添加到通讯录"));
                    UiObject obx = new UiObject(new UiSelector().className("android.widget.TextView").text("发消息"));
                    if (ob.waitForExists(2000)) {
                        AddLog(this.batchNumber, "进入个人信息错误", "未找到好友:" + name);
                        WeiXinUpdateIsFriend("0", cloneId);
                        return;
                    }
                    if (obx.waitForExists(2000)) {
                        AddLog(this.batchNumber, "发消息", "找到发消息");
                        obx.clickAndWaitForNewWindow();
                    }
                } else {
                    //查找手机号/QQ号  查找手机/QQ号:735588059
                    ob = new UiObject(new UiSelector().className("android.widget.TextView").text("查找手机/QQ号:" + name));
                    if (ob.waitForExists(3000)) {
                        ob.clickAndWaitForNewWindow(2000);
                        //存在手机号/QQ号
                        AddLog(this.batchNumber, "搜索", "存在查找手机/QQ号");
                        sleep(2000);
                        UiObject localUiObject = new UiObject(new UiSelector().text("发消息"));
                        if (localUiObject.waitForExists(2000)) {
                            ob = new UiObject(new UiSelector().className("android.widget.Button").text("添加到通讯录"));
                            UiObject obx = new UiObject(new UiSelector().className("android.widget.TextView").text("发消息"));
                            if (ob.waitForExists(2000)) {
                                AddLog(this.batchNumber, "进入个人信息错误", "未找到好友:" + name);
                                WeiXinUpdateIsFriend("0", cloneId);
                                return;
                            }
                            if (obx.waitForExists(2000)) {
                                AddLog(this.batchNumber, "发消息", "找到发消息");
                                obx.clickAndWaitForNewWindow();
                            }
                        }
                    } else { //查找微信号
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").textContains(name));
                        if (ob.exists()) {
                            if (ob.getText().contains("查找微信号") || ob.getText().contains("搜一搜")) {
                                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("联系人"));
                                UiObject ob11 = new UiObject(new UiSelector().className("android.widget.TextView").text("最常使用"));
                                if (ob.exists() || ob11.exists()) {
                                    ob = new UiObject(new UiSelector().className("android.widget.ListView"))
                                            .getChild(new UiSelector().className("android.widget.RelativeLayout").index(1))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(1))
                                            .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                                            .getChild(new UiSelector().className("android.widget.TextView").index(0));
                                    if (ob.exists()) {
                                        ob.clickAndWaitForNewWindow(2000);
                                    }
                                } else {
                                    AddLog(batchNumber, "进入个人信息错误", "未找到好友：" + name);
                                    WeiXinUpdateIsFriend("0", cloneId);
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
            //判断是否进入聊天页面
            new UiObject(new UiSelector().className("android.widget.ImageButton").description
                    ("聊天信息")).clickAndWaitForNewWindow();
            //点击头像
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ListView").index(0))
                    .getChild(new UiSelector().className("android.widget.LinearLayout"))
                    .getChild(new UiSelector().className("android.widget.RelativeLayout"))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(0))
                    .clickAndWaitForNewWindow();
            WeiXinUpdateViewTime(cloneId);
            WeiXinUpdateIsFriend("1", cloneId);
        } catch (Exception ex) {
            AddLog(this.batchNumber, "搜索好友", "搜索好友异常:" + ex.getMessage());
        }
    }

    /**
     * 更新微信使用时间
     *
     * @param cloneid
     */
    public void WeiXinUpdateViewTime(String cloneid) {
        try {
            String postdata = String.format("{\"cloneid\":\"%s\"}", new Object[]{cloneid});
            HttpHelper.httpPostForString(this.WeiXinUpdateViewTimeURL, postdata);
            return;
        } catch (Exception paramString) {
//            System.out.println("跟新状态发生异常" + paramString.toString() + " resp:" + "");
        }
    }


    /**
     * 是否为登录后主界面
     *
     * @return
     */
    public boolean isAfterLoginMain() {
        try {
            ob = new UiObject(new UiSelector().text("确定"));
            if (ob.waitForExists(2000)) {
                ob.click();
            }
        } catch (Exception ex) {
            return false;
        }
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("通讯录"));
        if (!ob.waitForExists(10000)) {
            return false;
        }
        return true;
    }

    /**
     * 获取微信对象
     *
     * @return
     */
    private List<WeiXinNameV2> GetWXNames() {
        try {
//            AddLog(batchNumber, "获取微信对象", "getWXNamesURL"+getWXNamesURL );
            String data = HttpHelper.httpGetString2(getWeiXinNameUrl);
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

    /**
     * 重启App
     */
    private void RestartApp() {
        try {
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.killApp(PackegName + "：push");
            sleep(1000);
            //打开微信
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");
            ob = new UiObject(new UiSelector().text("很抱歉，“微信”已停止运行。"));
            if (ob.waitForExists(2000)) {
                UiObject q = new UiObject(new UiSelector().text("确定"));
                if (q.waitForExists(1000)) {
                    q.click();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 添加日志
     *
     * @param targetName
     * @param logType
     * @param errorMsg
     */
    public void AddLog(String targetName, String logType, String errorMsg) {
        try {
            LoginInfo logInfo = new LoginInfo();
            logInfo.setIsshow(0);
            logInfo.setType(logType);
            logInfo.setBatchNumber("");
            logInfo.setRemark(errorMsg);
            logInfo.setCloneid(0 + "");
            logInfo.setCode("");
            logInfo.setMobile(this.Vps);
            logInfo.setLogTargetName(targetName);
            logInfo.setStatus(1 + "");
            Gson gson = new Gson();
            String jsonStr = gson.toJson(logInfo);
            String postData = jsonStr;
            String rData = HttpHelper.httpPostForString(this.addLogURL, postData);
        } catch (Exception e) {
        }
    }

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

    /**
     * 清除緩存
     *
     * @param isNeed
     */
    private void ClearCache(boolean isNeed) {
        String lastTime = "2016-01-01 00:00:00";
        try {
            //取消更新按钮
            ob = new UiObject(new UiSelector().text("取消"));
            if (ob.waitForExists(2000)) {
                ob.click();
                sleep(2000);
            }
            if (new File("/sdcard/cleardate.txt").exists()) {
                lastTime = jqhelper.readSDFile("/sdcard/cleardate.txt");
            }
            if (isNeed) {
                AddLog(this.batchNumber, "清理文件", "之前清理缓存时间:" + lastTime);
            }
            Date lastTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastTime);
            if (new Date().getTime() - (lastTimeDate).getTime() >= 172800000L || isNeed) {
                AddLog(this.batchNumber, "清理文件", "开始");
                File vPathFile = new File(this.vpath);
                File[] arrayOfFile = (vPathFile).listFiles();
                for (int i = 0; i < arrayOfFile.length; i++) {
                    File localFile = arrayOfFile[i];
                    if ((localFile.getName().length() >= 32) && (localFile.isDirectory())) {
                        AddLog(this.batchNumber, "清理文件", "开始清理文件夹" + localFile.getName());
                        HttpHelper.deleteDir(localFile);
                        AddLog(this.batchNumber, "清理文件", "结束清理文件夹" + localFile.getName());
                    }
                }
                jqhelper.reportlearDate();
                AddLog(this.batchNumber, "清理文件", "强关微信开始");
                jqhelper.killApp(this.PackegName);
                jqhelper.killApp(this.PackegName + ":push");
                sleep(3000);
                RestartApp();
                AddLog(this.batchNumber, "清理文件", "强关微信结束");
            }
        } catch (Exception e) {
            AddLog(this.batchNumber, "清理文件错误", e.getMessage());
        }
    }

    /**
     * 重启App2
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
            RestartApp();
        } catch (Exception ex) {
            AddLog(batchNumber, "重启app2", ex.getMessage());
        }
    }

    /**
     * 同意添加好友操作
     */
    private void AgreeFriendTask() {
        try {
            //重启App
            restartApp2();
            GetMenu(2);
            GetMenu(1);
            //TODO 进入通讯录页面
            if (new UiScrollable(GetUISelect("android.widget.ListView", 0)).waitForExists(2000)) {
                new UiScrollable(GetUISelect("android.widget.ListView", 0)).flingToBeginning(30);
            }
            //是否存在新的朋友 文本
            UiObject obf = new UiObject(new UiSelector().className("android.widget.TextView").text("新的朋友"));
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text("朋友推荐"));
            if (obf.waitForExists(2000)) {
                AddLog(this.batchNumber, "通过好友", "存在新的朋友按钮");
                obf.click();
            } else if (ob.exists()) { // 存在朋友推荐按钮
                AddLog(this.batchNumber, "通过好友", "存在朋友推荐按钮");
                Device.click(ob.getBounds().left + 28, ob.getBounds().bottom + 10);
            } else {
                AddLog(this.batchNumber, "通过好友", "不存在新朋友对应按钮");
                return;
            }
            //查找新好友列表
            UiScrollable collection = new UiScrollable(GetUISelect("android.widget.ListView", 0));
            if (!collection.waitForExists(3000)) {
                AddLog(this.batchNumber, "通过好友", "不存在新的好友链接");
                Device.pressBack();
                return;
            }
            int count = collection.getChildCount();
            for (int i = 0; i < count; i++) {
                AddLog(this.batchNumber, "通过好友", "进入同意好友循环 Count:" + count);
                ob = new UiObject(new UiSelector().className("android.widget.Button").text("接受"));
                if (ob.exists()) {
                    AddLog(this.batchNumber, "通过好友", "存在接受按钮");
                    //存在接受按钮
                    ob.clickAndWaitForNewWindow();
                    sleep(3000L);
                    //TODO 验证页面
                    UiObject localObject2 = new UiObject(new UiSelector().textContains("去验证"));
                    if (localObject2.exists()) {
                        localObject2.clickAndWaitForNewWindow(2000L);
                        localObject2 = new UiObject(new UiSelector().className("android.widget.Button").textContains("通过"));
                        if (localObject2.waitForExists(3000L)) {
                            localObject2.clickAndWaitForNewWindow(2000L);
                        }
                    }
                    //点击完成按钮
                    if (GetUIObject("android.widget.Button", "完成", 0).waitForExists(3000)) {
                        GetUIObject("android.widget.Button", "完成", 0).click();
                        if (GetUIObject("android.widget.TextView", "发消息", 0).waitForExists(3000)) {
                            AddLog(this.batchNumber, "通过好友", "成功添加好友");
                        }
                    }
                    Device.pressBack();
                } else {
                    AddLog(this.batchNumber, "通过好友", "无可同意的好友");
                    Device.pressBack();
                    break;
                }
            }
        } catch (Exception ex) {
            AddLog(this.batchNumber, "通过好友出现异常", ex.getMessage());
        }
    }

    /**
     * 微信更改昵称
     */
    private void WeiXinUpdateNickName(String nickName, String cloneId) {
        try {
            String postdata = String.format("{\"NickName\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{nickName, cloneId});
            HttpHelper.httpPostForString(this.WeiXinUpdateNickNameURL, postdata);
        } catch (Exception paramString1) {
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

    /**
     * 更新数据库好友状态
     *
     * @param isFriend
     * @param cloneId
     */
    public void WeiXinUpdateIsFriend(String isFriend, String cloneId) {
        try {
            String postdata = String.format("{\"IsFriend\":\"%s\",\"cloneid\":\"%s\"}", new Object[]{isFriend + "", cloneId});
            HttpHelper.httpPostForString(this.WeiXinUpdateIsFriendURL, postdata);
        } catch (Exception ex) {
        }
    }

}
