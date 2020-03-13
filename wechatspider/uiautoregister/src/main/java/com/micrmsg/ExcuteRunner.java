package com.micrmsg;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.uiControl.UiEdit;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeqee on 2015/10/19.
 */
public class ExcuteRunner extends SuperRunner {
    private String Host = "222.185.195.206";
    //private String Host = "222.185.195.206";
    private String JqsocialUrl = "www.jqsocial.com:34566";
    //private String JqsocialUrl = "www.jqsocial.com:34567";
    private String getWXNamesURL = "http://" + Host + ":34565/api/wechat/clonelist?v=";
    private String updateWXURL = "http://" + Host + ":34565/api/wechat/clonesubmit/";
    //private String addLogURL = "http://" + Host + ":34565/api/wx/addlog";
    private String updateImageURL = "http://" + JqsocialUrl + "/Files/UpLoadImageForWeixin2";
    private String path = "/sdcard/Tencent/MicroMsg/WeiXin";
    private long batchNumber = 0;
    private String mobile = "";
    private List<WeChatSpiderLog> logList = new ArrayList<WeChatSpiderLog>();

    //region 【初始化数据】
    private boolean initializtion() {
        ScriptHelper.switchImeToAdbKeyboard();
        Device = getUiDevice();
        jqhelper.reportNormal();
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        try {
            Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
            //mobile = jqhelper.readSDFile("/sdcard/name.txt");
            getWXNamesURL += Vps;
            HttpHelper.DeleteFile(new File(path));

        } catch (Exception e) {
            return false;
        }

        AddLog(888, "版本号：1，被外部重启：", "888");
        return true;
    }

    //endregion

    //region 【业务逻辑流程执行】
    public void testRunner() {
        if (!initializtion()) return;//如果初始化失败，则结束
        //循环执行任务
        List<String> friendList = new ArrayList<String>();
        int count = 15;
        while (true) {
            WeChatCloneSubmitModel wccsm = new WeChatCloneSubmitModel();
            List<CloneModel> crm = new ArrayList<CloneModel>();
            try {
                restartApp();
                //得到批次号
                batchNumber = new Date().getTime();
                crm = GetWXNames();
                count++;
                if (crm == null || crm.size() == 0) {
                    //System.out.println(1);
                    AddLog(batchNumber, "消息", "爬取wx_names返回为空");
                    sleep(60 * 1000 * 5);
                    continue;
                }
                if (count > 15) {
                    friendList = getFriendList();
                    count = 0;

                }
                //region friendList去重
                for (int i = 0; i < friendList.size() - 1; i++) {
                    for (int j = friendList.size() - 1; j > i; j--) {
                        if (friendList.get(j).equals(friendList.get(i))) {
                            friendList.remove(j);
                        }
                    }
                }
                //endregion
                List<String> addList = new ArrayList<String>();
                for (int i = 0; i < crm.size(); i++) {
                    System.out.println(0);
                    if (!friendList.contains(crm.get(i).getAccountName())) {
                        System.out.println(crm.get(i).getAccountName());
                        if (crm.get(i).getStatus() == (byte) 0) {
                            addList.add(crm.get(i).getAccountName());
                        }
                    } else if (crm.get(i).getStatus() != (byte) 2) {
                        //region 提交状态
                        if (crm.get(i).getStatus() != (byte) 3) {
                            crm.get(i).setStatus((byte) 2);
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(i));
                            UpdateTo(wccs);
                        } else if (crm.get(i).getStatus() == (byte) 3) {
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(i));
                            UpdateTo(wccs);
                        }
                        //endregion
                    }
                }
                addFriends(crm, addList);
                List<CloneModel> clonelist = crm;
                //获取微信List
                //List<WeiXinNameV2> wx_names = null;
                if (clonelist == null || clonelist.size() == 0) {
                    AddLog(batchNumber, "消息", "爬取wx_names返回为空");
                    sleep(60 * 1000 * 5);
                    continue;
                }

                AddLog(batchNumber, "消息", "爬取wx_names返回数量：" + clonelist.size());
                for (int i = 0; i < clonelist.size(); i++) {
                    if (clonelist.get(i).getStatus() >= (byte) 2) {
                        //int businessTypes = clonelist.get(i).getAccountType();
                        for (int j = 0; j < 3; j++) {
                            if (processOnePerson(clonelist.get(i), Vps, j)) {
                                break;
                            }
                            sleep(100);
                        }
                    }
                }
            } catch (Exception ex) {
                AddLog(batchNumber, "异常", ex.toString());
                System.out.println(10);
            } finally {
                wccsm.setCloneInfo(crm.get(0));
                UpdateTo(wccsm);
                sleep(500);
            }
        }
    }


    public boolean processOnePerson(CloneModel cm, String vps, int j) {
        try {
            AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息");
            System.out.println(6);
            //重启app
            restartApp();
            jqhelper.reportNormal();
            sleep(5000);
            //取消更新
            if (isUpdate()) {
                exitUpdate();
                sleep(2000);
            }
            //进入通讯录
            if (isAfterLoginMain()) {
                enterCallList();
            } else {
                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息结束 " +
                        "通讯录进入失败");
                return false;
            }
            sleep(1000);

            //找到并进入好友信息
            if (isCallList()) {
                enterFriendsList(cm.getAccountName());
            } else {
                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息结束 " +
                        "好友信息进入失败");
                return false;
            }
            sleep(1000);

            //找到并进入好友发的图片
            if (isPersonalInfo()) {
                enterFreindsPhotos(cm.getAccountName(), cm);
                if (cm.getStatus() == (byte) 3) {
                    AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j +
                            "】轮爬取朋友圈信息结束 " +
                            "用户非好友");
                    return true;
                }
            } else {
                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息结束 " +
                        "图片进入失败");
                return false;
            }
            sleep(5000);

            //循环保存遍历图片并提交
            if (isImageUI()) {
                boolean res = saveImageAndPost(cm, vps);
                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息结束 " +
                        "" + res);
                return res;
            } else {
                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】轮爬取朋友圈信息结束 " +
                        "isImageUI进入失败 ");
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 第【" + j + "】爬取朋友圈异常：" + ex
                    .toString());
        } finally {

        }
        return false;
    }

    //endregion

    //region 【判断界面】

    //region 【是否为登录后主界面面】
    public boolean isUpdate() {
        ob = new UiObject(new UiSelector().className("android.widget.Button").text("立刻安装"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }

    //endregion

    //region 【是否为登录后主界面面】
    public boolean isAfterLoginMain() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("通讯录"));
        if (!ob.exists()) {
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
        if (!ob1.exists() && !ob2.exists() && !ob3.exists() && !ob4.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否图片拉取界面】
    public boolean isImageUI() {
        UiObject ob1 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("赞"));
        UiObject ob2 = new UiObject(new UiSelector().className("android.widget.TextView").text
                ("评论"));
        if (!ob1.exists() || !ob2.exists() || !isImageLoaded()) {
            return false;
        }
        return true;
    }
    //endregion

    //region 【是否为个人信息界面】
    public boolean isPersonalInfo() {
        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("详细资料"));
        if (!ob.exists()) {
            return false;
        }
        return true;
    }
    //endregion

    //endregion

    //region 【界面操作】

    private void exitUpdate() {
        try {
            ob = new UiObject(new UiSelector().className("android.widget.Button").text("　取消　"));
            if (ob.exists()) {
                ob.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "取消更新失败", ex.toString());
        }
    }

    //region 【进入通讯录】
    private void enterCallList() {
        try {
            UiObject findText = new UiObject(new UiSelector().className(android.widget.TextView
                    .class.getName()).text("通讯录"));
            if (findText.exists()) {
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

    //region 【进入好友列表】
    private void enterFriendsList(String name) {
        try {
            new UiObject(new UiSelector().className("android.widget.TextView").description("搜索"))
                    .click();
            inputText("微信号：" + name);
            ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index
                    (1)).getChild(new UiSelector().className("android.widget.TextView"));
            if (!ob.exists() || ob.getText().contains("查找微信号") || ob.getText().contains("搜一搜")) {
                AddLog(batchNumber, "进入个人信息错误", "未找到好友：" + name);
                return;
            }
            ob.clickAndWaitForNewWindow();
            new UiObject(new UiSelector().className("android.widget.TextView").description
                    ("聊天信息")).clickAndWaitForNewWindow();
            new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0))
                    .getChild(new UiSelector().className("android.widget.ImageView").index(0))
                    .clickAndWaitForNewWindow();
            return;
        } catch (Exception ex) {
            AddLog(batchNumber, "用户：" + name + "进入个人信息异常", ex.toString());
            return;
        }
    }
    //endregion

    //region 【获取好友列表】
    private List<String> getFriendList() {
        try {
            ob = GetUIObject("android.widget.RelativeLayout", 1).getChild(GetUISelect("android" +
                    ".widget.TextView", "通讯录", 1));
            if (!ob.exists()) {
                return null;
            }
            ob.click();
            int index = 0;
            List<String> nameList = new ArrayList();
            do {
                UiScrollable scrollable = new UiScrollable(GetUISelect("android.widget.ListView"));
                if (!scrollable.exists()) {
                    return nameList;
                }
                if (index > 0) {
                    scrollable.scrollForward();
                } else {
                    scrollable.scrollToBeginning(20);
                }
                int count = scrollable.getChildCount();
                for (int i = 0; i < count; i++) {
                    ob = scrollable.getChild(new UiSelector().className("android.widget" +
                            ".LinearLayout").index(i)).getChild(new UiSelector().className
                            ("android.widget.LinearLayout")).getChild(new UiSelector().className
                            ("android.view.View").index(0));
                    if (ob.exists()) {
                        if (!ob.getText().equals("微信团队")) {
                            ob.click();
                            ob = GetUIObject("android.widget.ListView", 0).getChild(GetUISelect
                                    ("android.widget" +
                                            ".LinearLayout", 1)).getChild(GetUISelect("android" +
                                    ".widget" +
                                    ".LinearLayout", 0))
                                    .getChild(GetUISelect("android.widget.LinearLayout", 1));
                            if (ob.getChild(new UiSelector().className("android.widget.TextView")
                                    .textStartsWith
                                            ("微信号:")).exists()) {
                                String id = ob.getChild(new UiSelector().className("android" +
                                        ".widget.TextView")
                                        .textStartsWith("微信号:")).getText().substring(4);
                                System.out.println(id);
                                if (!nameList.contains(id.trim())) {
                                    nameList.add(id.trim());
                                }
                            }
                            Device.pressBack();
                        }
                    }
                }
                index = index + 1;
            }
            while (!new UiObject(new UiSelector().className("android.widget.ListView")).getChild
                    (new UiSelector().className("android.widget.FrameLayout")).getChild(new
                    UiSelector().className("android.widget.FrameLayout").index(0)).getChild(new
                    UiSelector().className("android.widget.TextView").index(0).textContains
                    ("位联系人")).exists());
            return nameList;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    //endregion

    //region 【进入可进入的照片】
    private void enterFreindsPhotos(String name, CloneModel cm) {
        try {
            UiObject OwnPhotos = new UiObject(new UiSelector().className("android.widget" +
                    ".TextView").text("个人相册"));
            if (OwnPhotos.exists()) {
                OwnPhotos.clickAndWaitForNewWindow();
            } else {
                AddLog(batchNumber, "进入图片", "用户：" + name + "未找到可进入的个人相册");
            }
            sleep(2000);

            //下拉加载操作
            for (int i = 0; i < 6; i++) {
                ScriptHelper.swipe(0, 1400, 0, 250);
                sleep(2000);
            }
            if (new UiObject(new UiSelector().className("android.widget.TextView").text
                    ("非朋友最多显示十张照片")).waitForExists(3000)) {
                cm.setStatus((byte) 3);
                WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                wccs.setPublishList(new ArrayList<PublishModel>());
                wccs.setCloneInfo(cm);
                UpdateTo(wccs);
                return;
            } else {
                cm.setStatus((byte) 2);
                WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                wccs.setPublishList(new ArrayList<PublishModel>());
                wccs.setCloneInfo(cm);
                UpdateTo(wccs);
            }
            sleep(2000);
            for (int i = 0; i < 7; i++) {
                ScriptHelper.swipe(0, 250, 0, 1400);
                sleep(500);
            }
            sleep(2000);
            UiObject FirstImage = new UiObject(new UiSelector().className("android.view.View")
                    .description("图片"));
            for (int i = 0; i < 10; i++) {
                if (FirstImage.exists()) {
                    break;
                }
                ScriptHelper.swipe(0, 1400, 0, 250);
                sleep(1000);
                UiObject ob = new UiObject(new UiSelector().className("android.widget.ListView"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.ImageView"));
                if (ob.exists()) {
                    AddLog(batchNumber, "进入图片", "用户：" + name + "未找到可进入的图片");
                    return;
                }
            }
            FirstImage.clickAndWaitForNewWindow();
            return;
        } catch (Exception ex) {
            AddLog(batchNumber, "进入个人相册异常", ex.toString());
            return;
        }
    }
    //endregion

    //region 【重启微信App】
    public void restartApp() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            jqhelper.killApp(PackegName);
            jqhelper.killApp(PackegName + ":push");
            System.out.println(3);
            sleep(1000);
            Runtime.getRuntime().exec("am start -n com.tencent.mm/.ui.LauncherUI");
            sleep(5000);
        } catch (Exception ex) {
        }
    }
    //endregion

    //region 【添加好友】
    public boolean addFriends(List<CloneModel> crm, List<String> addList) {
        if (addList.size() == 0) {
            return true;
        }
        try {
            ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index
                    (1).description("更多功能按钮"));
            if (!ob.exists()) {
                AddLog(batchNumber, "新增好友", "未找到更多功能按钮");
                return false;
            }
            ob.click();
            ob = new UiObject(new UiSelector().text("添加朋友").index(1).className("android.widget" +
                    ".TextView"));
            if (!ob.waitForExists(1000)) {
                AddLog(batchNumber, "新增好友", "未找到添加朋友按钮");
                return false;
            }
            ob.clickAndWaitForNewWindow();
            ob = new UiObject(new UiSelector().className("android.widget.TextView").text
                    ("微信号/QQ号/手机号"));
            if (!ob.waitForExists(3000)) {
                AddLog(batchNumber, "新增好友", "未找到搜索栏");
                return false;
            }
            ob.click();
            for (int i = 0; i < addList.size(); i++) {
                System.out.println("i:" + i);
                String id = addList.get(i);
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton"));
                if (ob.waitForExists(1000)) {
                    ob.click();
                }
                edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                if (!edit.exists()) {
                    AddLog(batchNumber, "新增好友", "未找到搜索栏");
                    continue;
                }
                edit.setChineseText(id);
                ob = new UiObject(new UiSelector().className("android.widget.TextView").text
                        ("搜索:" + id));
                if (!ob.waitForExists(1000)) {
                    if (GetUIObject("android.widget.TextView", "微信号/QQ号/手机号").waitForExists(3000)) {
                        GetUIObject("android.widget.TextView", "微信号/QQ号/手机号").click();
                        if (!ob.waitForExists(1000)) {
                            AddLog(batchNumber, "新增好友", "未找到搜索按钮");
                            continue;
                        }
                    } else {
                        AddLog(batchNumber, "新增好友", "未找到搜索按钮");
                        continue;
                    }
                }
                ob.clickAndWaitForNewWindow();
                if (new UiObject(new UiSelector().className("android.widget.TextView").text
                        ("用户不存在")).waitForExists(2000)) {
                    AddLog(batchNumber, "新增好友", "用户不存在");
                    for (int z = 0; z < crm.size(); z++) {
                        if (crm.get(z).getAccountName().equals(id)) {
                            System.out.println(id);
                            crm.get(z).setIsExists(false);
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(z));
                            UpdateTo(wccs);
                        }
                    }
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    continue;
                }
                if (new UiObject(new UiSelector().className("android.widget.TextView").text
                        ("被搜帐号状态异常，无法显示。")).exists()) {
                    AddLog(batchNumber, "新增好友", "被搜帐号状态异常，无法显示。");
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    for (int z = 0; z < crm.size(); z++) {
                        if (crm.get(z).getAccountName().equals(id)) {
                            System.out.println(id);
                            crm.get(z).setIsExists(false);
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(z));
                            UpdateTo(wccs);
                        }
                    }
                    continue;
                }
                if (new UiObject(new UiSelector().className("android.widget.TextView").text
                        ("操作过于频繁，请稍后再试。")).exists()) {
                    AddLog(batchNumber, "新增好友", "操作过于频繁，请稍后再试");
                    new UiObject(new UiSelector().className("android.widget.Button").text("确定"))
                            .click();
                    return true;
                }
                if (GetUIObject("android.widget.Button", "发消息", 0).exists()) {
                    AddLog(batchNumber, "新增好友", "已经是好友");
                    for (int z = 0; z < crm.size(); z++) {
                        if (crm.get(z).getAccountName().equals(id)) {
                            System.out.println(id);
                            crm.get(z).setStatus((byte) 2);
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(z));
                            UpdateTo(wccs);
                        }
                    }
                    continue;
                }
                ob = new UiObject(new UiSelector().className("android.widget.Button").text
                        ("添加到通讯录"));
                if (!ob.waitForExists(2000)) {
                    AddLog(batchNumber, "新增好友", "搜索用户异常，没有进入添加到通讯录界面");
                    AddLog(batchNumber, "新增好友", "用户不存在");
                    for (int z = 0; z < crm.size(); z++) {
                        if (crm.get(z).getAccountName().equals(id)) {
                            System.out.println(id);
                            crm.get(z).setIsExists(false);
                            WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                            wccs.setPublishList(new ArrayList<PublishModel>());
                            wccs.setCloneInfo(crm.get(z));
                            UpdateTo(wccs);
                        }
                    }
                    Device.pressBack();
                    continue;
                }
                ob.clickAndWaitForNewWindow();

                edit = new UiEdit(new UiSelector().className("android.widget.EditText").index(1));
                if (!edit.waitForExists(2000)) {
                    if (GetUIObject("android.widget.Button", "视频聊天").exists()) {
                        Device.pressBack();
                    } else {
                        AddLog(batchNumber, "新增好友", "未找到验证框");
                        continue;
                    }
                }
                if (!GetUIObject("android.widget.TextView", "发送", 0).exists()) {
                    AddLog(batchNumber, "新增好友", "未找到发送按钮");
                    continue;
                }
                GetUIObject("android.widget.TextView", "发送", 0).click();
                sleep(1000);
                Device.pressBack();
                if (GetUIObject("android.widget.TextView", "发送", 0).exists()) {
                    GetUIObject("android.widget.TextView", "发送", 0).click();
                    Device.pressBack();
                    sleep(500);
                    Device.pressBack();
                }
                AddLog(batchNumber, "新增好友", "新增好友完成");
                for (int z = 0; z < crm.size(); z++) {
                    if (crm.get(z).getAccountName().equals(id)) {
                        System.out.println(id);
                        crm.get(z).setStatus((byte) 1);
                        WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
                        wccs.setPublishList(new ArrayList<PublishModel>());
                        wccs.setCloneInfo(crm.get(z));
                        UpdateTo(wccs);
                    }
                }
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "新增好友", ex.getMessage() + ex.getStackTrace());
            return false;
        } finally {
            Device.pressBack();
            sleep(1000);
            Device.pressBack();
        }
        return true;
    }
    //endregion

    //region 【拉取图片或视频】
    private boolean saveImageAndPost(CloneModel cm, String vps) {
        int maxPublish = cm.getPublishCount();
        WeChatCloneSubmitModel wccs = new WeChatCloneSubmitModel();
        List<PublishModel> publishList = new ArrayList<PublishModel>();
        PublishModel publishModel = null;
        int tryCount = 0;
        Date lastTime = ParseTime(cm.getLastTime());

        String lastText = "", lastSourceType = "";
        Date lastPublishTime = null;
        boolean isFirst = true;
        try {
            while (true) {
                jqhelper.reportNormal();
                sleep(2000);
                //region 判断图片是否加载完成
                if (isImageLoaded()) {
                    //-1:视频,-2:单图,a/b:多图,空:异常
                    String sourceType = GetSourceType();
                    if (sourceType == null || sourceType.equals("")) {//异常
                        AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " 资源类型未识别 返回空");
                        tryCount++;
                    } else {
                        String text = GetText();
                        Date publishTime = GetDate();
                        String publishTimeStr = "";
                        if (publishTime != null) {
                            publishTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                                    (publishTime);
                        }

                        //region 退出判断
                        boolean isExit = isNeedExit(isFirst,
                                text, publishTime, sourceType,
                                lastText, lastPublishTime, lastSourceType, maxPublish,
                                cm, publishList, lastTime);
                        if (isExit) {
                            if (publishModel != null) {
                                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + "爬取到朋友圈 图片 "
                                        + publishModel.getPublishPicture() + " 发布时间：" + publishModel
                                        .getPublishTime() + " 类型：" + lastSourceType + " 已经爬取朋友圈数量：" +

                                        publishList.size());
                                if (!publishModel.getPublishPicture().equals("")) {
                                    publishList.add(publishModel);
                                } else {
                                    AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " " +
                                            publishModel.getPublishDesc() + " 没有资源 不入库");
                                }
                            }
                            break;
                        }
                        //endregion

                        //region 处理

                        if (text != null && publishTime != null) {
                            tryCount = 0;
                            boolean isNew = false;
                            if (lastSourceType.contains("/")) {
                                String a = lastSourceType.split("/")[0].trim();
                                String b = lastSourceType.split("/")[1].trim();

                                if (a.equals(b)) {
                                    isNew = true;
                                    AddLog(batchNumber, "多图判断", lastSourceType + " " + a + " " +
                                            b + " "
                                            + a.equals(b));
                                }
                            }

                            if (publishModel == null) {
                                publishModel = new PublishModel(text, publishTimeStr);
                            } else if (!publishModel.getPublishDesc().equals(text) ||
                                    !publishModel.getPublishTime()
                                            .equals(publishTimeStr) || isNew) {
                                AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + "爬取到朋友圈 图片 "
                                        + publishModel.getPublishPicture() + " 发布时间：" + publishModel
                                        .getPublishTime() + " 类型：" + lastSourceType + " " +
                                        "已经爬取朋友圈数量：" +
                                        publishList.size());
                                if (!publishModel.getPublishPicture().equals("")) {
                                    publishList.add(publishModel);
                                } else {
                                    AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + " " +
                                            publishModel
                                                    .getPublishDesc() + " 没有资源 不入库");
                                }
                                publishModel = new PublishModel(text, publishTimeStr);
                            }

                            //type: -1 视频
                            if (sourceType.equals("-1")) {//视频

                            } else {//图
                                for (int i = 0; i < 3; i++) {
                                    String imgID = saveImage(cm.getAccountName());
                                    if (imgID != null && !imgID.equals("") && !imgID.equals
                                            ("null")) {
                                        if (publishModel.getPublishPicture().equals("")) {
                                            publishModel.setPublishPicture(imgID);
                                        } else {
                                            publishModel.setPublishPicture(publishModel
                                                    .getPublishPicture() + ";" + imgID);
                                        }
                                        break;
                                    } else {
                                        sleep(2000);
                                        AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() +
                                                "图片上传失败 返回 " + imgID + "重试次数：" + i);
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

                if (tryCount > 1 && publishList.size() < 15) {
                    AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + "不在图片页面或者无法识别资源类型 需重试 ");
                    return false;
                }
                if (tryCount > 1 && publishList.size() >= 15) {
                    AddLog(batchNumber, "消息", "用户：" + cm.getAccountName() + "已获取足够数据 无需重试 ");
                    break;
                }

                //region 向右滑
                scrollToNext();
                HttpHelper.DeleteFile(new File(path));
                isFirst = false;
                //endregion
            }
        }catch (Exception ex){
            AddLog(batchNumber, "保存图片异常", "保存图片异常：" + ex.toString());
        }
        System.out.println(1000000);
        wccs.setCloneInfo(cm);
        wccs.setPublishList(publishList);
        UpdateTo(wccs);
        sleep(500);
        return true;
    }

    //图片右滑
    private boolean scrollToNext() {
        try {
            sleep(500);
            UiScrollable ImageScroll = new UiScrollable(new UiSelector().className("android" +
                    ".widget.Gallery"));
            ImageScroll.setAsHorizontalList();
            if (ImageScroll.exists()) {
                return ImageScroll.scrollForward();
            }
        } catch (Exception e) {
            AddLog(batchNumber, "图片右滑", "图片右滑失败 异常：" + e.toString());
        }
        return false;
    }

    //判断图片加载
    private boolean isImageLoaded() {
        UiObject info = new UiObject(new UiSelector().className("android.widget" +
                ".TextView").description("更多"));
        sleep(1000);
        if (info.exists()) {
            return true;
        }

        for (int i = 0; i < 2; i++) {
            try {
                sleep(5000);
                UiObject infoC = new UiObject(new UiSelector().className("android.widget" +
                        ".Gallery")).getChild
                        (new UiSelector().className("android.widget.ImageView"));
                if (infoC.exists()) {
                    infoC.click();
                }
                if (info.exists()) {
                    return true;
                }
            } catch (Exception e) {
                AddLog(batchNumber, "判断图片加载", "判断图片加载失败 异常：" + e.toString());
            }
        }
        return false;
    }

    //退出判断
    private boolean isNeedExit(boolean isFirst,
                               String text, Date publishTime, String sourceType,
                               String lastText, Date lastPublishTime, String lastSourceType, int
                                       maxCount, CloneModel cm, List<PublishModel> publishList,
                               Date lastTime) {


        Date chkDate = lastTime;
        System.out.println(DateToString(chkDate));
        if (!publishTime.after(chkDate)) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (publishTime);
            String chkDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format
                    (chkDate);
            AddLog(batchNumber, "退出判断", cm.getAccountName() + "当前朋友圈发布时间：" + dateStr + " " +
                    "不大于已入库的朋友圈最近发布时间： " +
                    chkDateStr);
            System.out.println(111);
            return true;
        }

        if (isFirst) {
            System.out.println(222);
            cm.setLastTime(DateToString(publishTime));
            return false;
        }

        if (text.equals(lastText) &&
                sourceType.equals(lastSourceType) &&
                publishTime.equals(lastPublishTime)) {
            AddLog(batchNumber, "退出判断", cm.getAccountName() + "该朋友圈已完成 当前已获取到朋友圈数量：" +
                    publishList.size());
            System.out.println(333);
            return true;
        }

        if (publishList.size() >= maxCount) {
            AddLog(batchNumber, "退出判断", cm.getAccountName() + "当前获取朋友圈数量：" + publishList.size() +
                    " " + "大于等于最大限制获取数量： " +
                    maxCount);
            System.out.println(444);
            return true;
        }
        System.out.println(555);
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
            UiObject timeob = ob.getChild(new UiSelector().className("android.widget" +
                    ".TextView").index(0));
            System.out.println(timeob.getText());
            Date date = ParseXYD(timeob.getText());
            System.out.println(11111);
            if (date.getMonth() > new Date().getMonth()) {
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(date);
                rightNow.add(Calendar.YEAR, -1);
                date = rightNow.getTime();
            }
            if (date == null) {
                System.out.println(123321);
            } else {
                System.out.println(DateToString(date));
            }
            return date;
        } catch (Exception e) {
            AddLog(batchNumber, "获取日期", "获取日期失败 异常：" + e.toString());
            System.out.println(222222222);
        }
        return null;
    }

    //获取Text
    private String GetText() {
        try {
            UiObject ob = new UiObject(new UiSelector().className("android.view.View").index(0))
                    .getChild(new UiSelector().className("android.widget.FrameLayout").index(0))
                    .getChild(new UiSelector().className("android.widget" + ".LinearLayout"))
                    .getChild(new UiSelector().className("android.widget" + ".LinearLayout"))
                    .getChild(new UiSelector().className("android.widget" + ".TextView").index(0));
            if(!ob.exists()){
                return "";
            }
            String text = ob.getText();
            if (text != null && !text.equals("")) {
                return text.replaceAll("\\n", " ").replaceAll("\\r", " ").replaceAll("\\\\", "")
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
            if (moreob.exists()) {
                //返回多图
                String count = moreob.getText();
                return count;
            } else {
                new UiObject(new UiSelector().className("android.widget.TextView")
                        .description("更多")).click();
                UiObject uo = new UiObject(new UiSelector().className("android.widget" +
                        ".TextView").text("保存到手机"));
                if (!uo.exists()) {
                    //返回视频
                    Device.pressBack();
                    System.out.println(-1);
                    return "-1";
                } else {
                    //返回图片
                    Device.pressBack();
                    System.out.println(-2);
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
            if (isImageLoaded()) {
                new UiObject(new UiSelector().className("android.widget" +
                        ".TextView").description("更多")).click();
                sleep(1000);
                new UiObject(new UiSelector().className("android.widget" + ".TextView").text
                        ("保存到手机")).click();
                System.out.print(path+"    qianpath");
                String filename = HttpHelper.GetFileName(new File(path));
                if (filename != null && !filename.equals("")) {
                    long startTime = new Date().getTime();
                    String id = UploadImage2(path + "/" + filename, UserID);
                    long finishTime = new Date().getTime();
                    long time = finishTime - startTime;
                    AddLog(batchNumber, "上传图片时间", String.valueOf(time) + "    取得的id：" + id);
                    System.out.println(id);
                    return id;
                }
                AddLog(batchNumber, "保存图片", "保存图片失败 未找到图片");
            }
        } catch (Exception e) {
            AddLog(batchNumber, "保存图片", "保存图片失败 异常：" + e.toString());
        }
        return "";
    }

    //endregion

    //endregion

    //region 【获取任务，提交结果】

    //获取微信对象
    private List<CloneModel> GetWXNames() {
        String data = "";
        List<CloneModel> crm = new ArrayList<CloneModel>();
        try {
            data = HttpHelper.httpGetString(getWXNamesURL);
            Type type = new TypeToken<CloneModel>() {
            }.getType();
            CloneModel cm = gson.fromJson(data, type);
            System.out.println(data);
            System.out.println(cm.toString());
            crm.add(cm);
            return crm;
        } catch (Exception ex) {
            AddLog(batchNumber, "获取微信对象", "获取微信对象 异常：" + ex.toString());
            return null;
        }
    }

    // 提交数据
    public String UpdateTo(WeChatCloneSubmitModel wccs) {
        wccs.setLogList(logList);
        String postData = "";
        try {
            postData = gson.toJson(wccs, new TypeToken<WeChatCloneSubmitModel>() {
            }.getType());
            System.out.println(postData);
            //System.out.println(postData);
            String resp = HttpHelper.httpPostForString(updateWXURL, postData);
            logList.clear();
            System.out.println(resp);
            return resp;
        } catch (Exception ex) {
            AddLog(batchNumber, "上传朋友圈异常", " 上传朋友圈异常 " + ex.toString());
            AddLog(batchNumber, "上传朋友圈异常", postData);
        }
        return "";
    }

    //上传图片
    private String UploadImage2(String filename, String accountName) {
        String resp = "";
        try {
            resp = HttpHelper.httpPostMultipart(updateImageURL, filename, accountName);
            JSONObject o = new JSONObject(resp);
            Integer nType = o.getInt("type");
            if (nType == 2) {
                Integer picId = o.getInt("message");
                return picId.toString();
            } else {
                AddLog(batchNumber, "上传图片错误", resp);
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传图片错误[Android]", ex.toString() + "返回结果：" + resp);
        }
        return "";
    }

    //endregion

    //region 【Utils】

    //region Date处理
    private Date ParseXYD(String s) {
        // 昨天 18:03
        if (s.contains("月")) {
            System.out.println(9871);
            return ParseXYD1(s);
        } else {
            System.out.println(9872);
            Date d = ParseXYD0(s);
            return d;
        }
    }

    //endregion

    //添加日志
    public void AddLog(long batchNumber, String type, String remark) {
        String resp = "";
        try {
            System.out.println(type + ">>>>>>>>>>>>>>>>>>>>>>>>>>" + remark);
            logList.add(new WeChatSpiderLog(batchNumber, type, remark, Vps, new SimpleDateFormat
                    ("yyyy-MM-dd").format(new Date())));

            //resp = HttpHelper.httpPostForString(addLogURL, postData);
        } catch (Exception ex) {
            System.out.println("添加日志发生异常" + ex.toString() + " resp:" + resp);
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
            String mm = s.substring(0, s.indexOf("月"));
            String dd = s.substring(s.indexOf("月") + 1, s.indexOf("日"));
            String hh = s.substring(s.indexOf(" ") + 1, s.indexOf(":"));
            String ss = s.substring(s.indexOf(":") + 1);
            // 11/26/2015 12:08:23
            String str = String.format("%s-%s-%s %s:%s:00",
                    "2016",
                    mm,
                    dd,
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
            //dateString = dateString.replaceAll("/", "-");
            // 11/26/2015 12:08:23
            // yyyy-MM-dd'T'HH:mm+s:SSSS
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public void inputText(String str) {
        for (int i = 0; i < str.length(); i++) {
            ScriptHelper.input(str.substring(i, i + 1));
            sleep(200);
        }
    }

    public String DateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    //endregion

}
