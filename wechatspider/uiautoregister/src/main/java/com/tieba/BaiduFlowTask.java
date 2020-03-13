package com.tieba;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.model.CaptchaResult;
import com.google.gson.reflect.TypeToken;
import com.common.helper.HttpHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaiduFlowTask extends SuperRunner {
    public void testRunner() throws UiObjectNotFoundException, RemoteException {
        Type listStrType = new TypeToken<List<String>>() {
        }.getType();
        Type logList = new TypeToken<List<BaiduLogInfo>>() {
        }.getType();
        Type submitList = new TypeToken<List<BaiduFriendSubmitData>>() {
        }.getType();
        List<BaiduFriendSubmitData> submitDataList = new ArrayList<BaiduFriendSubmitData>();
        try {
            Initialization();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常Initialization()：" + e.toString() + " \n", "/sdcard/error.txt");
        }

        while (true) {
            try {
                jqhelper.reportNormal();
                String htmlstr = "";
                try {
                    htmlstr = HttpHelper.httpGetString(urlMap.get("getUrl")).replace("[", "").replace("]", "");
                } catch (Exception e) {
                    jqhelper.writeSDFileEx("HttpGet抛错：" + e.toString() + " \n", "/sdcard/error.txt");
                    jqhelper.delay(10000);
                    continue;
                }
                LoginUser user = null;
                if (htmlstr != null && !htmlstr.isEmpty()) {
                    user = gson.fromJson(htmlstr, LoginUser.class);
                    if (user == null || (user != null && (user.AutoReplyNextName == null || user.FriendNextID == null)))
                        continue;
                } else {
                    jqhelper.delay(10000);
                    continue;
                }

                BaiduFriendSubmitData submitData = new BaiduFriendSubmitData();
                List<BaiduLogInfo> loglist = new ArrayList<BaiduLogInfo>();
                BaiduLogInfo logInfo = new BaiduLogInfo();

                startApp();
                String msg = LoginTieba(user.Account, user.Password);
                if (msg != "登录成功") {
                    loglist.add(logInfo.setAll("手机登录", msg, "", "0", Vps, "", "", "",""));
                } else {
                    List<String> namelist = gson.fromJson("[" + user.AutoReplyNextName + "]", listStrType);
                    List<String> msglist = gson.fromJson("[" + user.AutoReplyMessageContent + "]", listStrType);
                    List<String> taskidlist = gson.fromJson("[" + user.FriendNextID + "]", listStrType);
                    List<String> piclist = gson.fromJson("[" + user.Picture + "]", listStrType);
                    boolean isLoad = false;
                    String picidlast = "";
                    for (int j = 0; j < namelist.size(); j++) {
                        jqhelper.reportNormal();
                        String name = namelist.get(j);
                        String msgcontent = msglist.get(j);
                        String taskid = taskidlist.get(j);
                        String picid = piclist.get(j);
                        if (!name.isEmpty()) {
                            if (j == 0 || !picid.equals(picidlast) ) {
                                isLoad = jqhelper.LoadPicture(picid, urlMap.get("picUrl") + picid, Device);
                            }
                            picidlast = picid;
                            if (!msgcontent.isEmpty() || isLoad) {
                                msg = AutoReply(name, msgcontent, isLoad);
                                if (msg.isEmpty()) // || (msg == "回复图片失败;" && !msgcontent.isEmpty())
                                    loglist.add(logInfo.setAll("任务投放", msg, name, "1", Vps, msgcontent, user.ID, taskid, picid));
                                else
                                    loglist.add(logInfo.setAll("任务投放", msg, name, "0", Vps, msgcontent, user.ID, taskid, picid));
                            } else {
                                loglist.add(logInfo.setAll("任务投放", "投放内容为空", name, "0", Vps, "", user.ID, taskid, picid));
                            }
                        } else {
                            loglist.add(logInfo.setAll("任务投放", "投放对象为空", "", "0", Vps, "", user.ID, taskid, picid));
                    }
                    }
                }
                cleanProcess(PackegName);
                submitData.setAll(user.ID, gson.toJson(loglist, logList), user.Status, user.GetTime, "[]", "[]", "[]", "[]", "[]");
                submitDataList.add(submitData);
                String submitstr = "{\"list\":" + gson.toJson(submitDataList, submitList) + "}";
                try {
                    HttpHelper.httpPostForString(urlMap.get("postUrl"), submitstr);
                    submitDataList.clear();
                } catch (Exception e) {
                    jqhelper.writeSDFileEx("HttpPost抛错：" + e.toString() + " \n", "/sdcard/error.txt");
                    continue;
                }
            } catch (Exception e) {
                jqhelper.writeSDFileEx("异常testRunner()：" + e.toString() + " \n", "/sdcard/error.txt");
                continue;
            }
        }

    }

    private void Initialization() throws UiObjectNotFoundException, RemoteException, IOException {
        PackegName = "com.baidu.tieba";
        ActivityName = "LogoActivity";
        Vps = jqhelper.readSDFile("/sdcard/Vps.txt");
        urlMap.put("getUrl", "http://222.185.251.62:8009/api/baiduflow/tasklist?t=30&n=1&m=20&v=" + Vps);
        urlMap.put("postUrl", "http://222.185.251.62:8009/api/baiduflow/updatetasklist/");
        urlMap.put("picUrl", "http://222.185.251.62:34567/files/download?ID=");
        jqhelper.initOCR();
        jqhelper.switchImeToAdbIme();
        cleanProcess(PackegName);
    }

    //清理
    private void cleanProcess(String PackageName) {
        jqhelper.killApp(PackageName);
        jqhelper.delay(800);
        jqhelper.clearPackage(PackageName);
        jqhelper.delay(800);
    }

    //启动
    private void startApp() {
        try {
            Device = getUiDevice();
            Device.wakeUp();
            jqhelper.delay(800);
            jqhelper.startActivity(PackegName + "/." + ActivityName);
            jqhelper.delay(5000);
            jqhelper.reportNormal();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常startApp()：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    //region LoginTieba

    //判断贴吧登录界面1
    private boolean isTiebaLogin1() {
        UiObject tbloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(1));
        if (tbloginBtn.waitForExists(10000)) {
            return true;
        } else {
            return false;
        }
    }

    //贴吧登录1
    private void TiebaLogin1() {
        try {
            //登录
            UiObject tbloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(1));
            tbloginBtn.clickAndWaitForNewWindow(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断贴吧登录界面2
    private boolean isTiebaLogin2() {
        UiObject tbLoginText = new UiObject(new UiSelector().className("android.widget.TextView").text("登录").index(0));
        UiObject tbRegisterText = new UiObject(new UiSelector().className("android.widget.TextView").text("注册").index(1));
        if (tbLoginText.exists() && tbRegisterText.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //贴吧登录2
    private void TiebaLogin2(String name, String password) {
        try {
            jqhelper.delay(1000);
            Device.click(326, 244);//点击账号输入框
            jqhelper.delay(2000);
            jqhelper.input(name);//输入邮箱
            jqhelper.delay(2000);
            Device.click(264, 360);//点击密码输入框
            jqhelper.delay(1000);
            jqhelper.input(password);//输入密码
            jqhelper.delay(1000);
            Device.click(360, 486);//点击登录按钮
            jqhelper.delay(3500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断贴吧"消息提醒"
    private boolean isTiebaLogin4() {
        UiObject tbWindowText = new UiObject(new UiSelector().className("android.widget.TextView").text("消息提醒").index(0));
        if (tbWindowText.waitForExists(6000)) {
            return true;
        } else {
            return false;
        }
    }

    public String OpenSearchFriend() {
        jqhelper.startActivity("com.baidu.tieba/.setting.more.MsgRemindActivity");
        if (isTiebaLogin4()) {
            Device.click(620, 798);
            jqhelper.delay(800);
            jqhelper.startActivity("com.baidu.tieba/.addresslist.im.searchfriend.SearchFriendActivity");
            jqhelper.delay(3000);
            if (isBaiduFriendAdd1()) {
                Device.click(160, 250); //点击输入框
                jqhelper.delay(800);
                return "登录成功";
            }
            return "未跳转至‘查找好友’页面";
        } else
            return "未跳转至‘消息提醒’页面";
    }

    public String LoginTieba(String name, String password) {
        try {
            if (isTiebaLogin1()) {
                TiebaLogin1();
                if (isTiebaLogin2()) {
                    TiebaLogin2(name, password);
                    String msg = null;
                    CaptchaResult captcha = null;
                    String captchastr = null;
                    for (int i = 0; i < 4; i++) {
                        if (isTiebaLogin2()) {
                            if (jqhelper.VerifyText(450, 556, 660, 606, "发,送,验,证,码", 3000) == 1) {
                                Device.click(630, 785); //点击"下次验证"
                                jqhelper.delay(800);
                                msg = OpenSearchFriend();
                                break;
                            } else {
                                String text = jqhelper.GetTextFromScreen(48, 146, 300, 194);
                                if (text.contains("验证") || jqhelper.VerifyText(28, 458, 148, 514, "验,证,码", 3000) == 1) {
                                    if (i == 3) {
                                        msg = "打码失败";
                                        break;
                                    }
                                    try {
                                        Device.click(668, 492); //刷新验证码
                                        jqhelper.delay(2000);
                                        captcha = jqhelper.LoginCaptchaTap();
                                        if (captcha == null)
                                            msg = "打码失败";
                                        else {
                                            captchastr = captcha.getCode();
                                            if (captchastr.isEmpty()) {
                                                jqhelper.reportError(captcha);//验证码报错
                                                msg = "打码失败";
                                            } else {
                                                Device.click(230, 486);//点击验证码输入框
                                                jqhelper.delay(900);
                                                jqhelper.input(captchastr);//输入验证码
                                                jqhelper.delay(1500);
                                                Device.click(116, 606); //点击登录
                                                jqhelper.delay(3500);
                                            }
                                        }
                                    } catch (Exception e) {
                                        msg = "打码失败";
                                    }
                                } else if (text.contains("密码有误")) {
                                    msg = "密码错误";
                                    break;
                                } else if (text.contains("连接")) {
                                    jqhelper.reConnNet();
                                    msg = "网络连接失败";
                                    break;
                                } else if (text.contains("填写密码")) {
                                    msg = "密码为空";
                                    break;
                                } else if (text.contains("亲爱的")) {
                                    msg = "用户昵称未创建";
                                    break;
                                } else {
                                    msg = "未知错误";
                                    break;
                                }
                            }
                        } else {
                            msg = OpenSearchFriend();
                            break;
                        }
                    }
                    return msg;
                }
                return "未跳转至登录页面";
            } else
                return "未找到‘登录’按钮";
        } catch (Exception e) {
            return "LoginTieba抛错：" + e.getMessage();
        }
    }

    //endregion

    //region AddBaiduFriend

    //判断贴吧搜索按钮
    private boolean isBaiduFriendAdd1() {
        UiObject tbSearchText = new UiObject(new UiSelector().className("android.widget.TextView").text("搜索").index(1));
        if (tbSearchText.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //输入姓名，点击“搜索”
    private void BaiduFriendAdd1(String name) {
        try {
            jqhelper.input(name);
            jqhelper.delay(800);
            UiObject tbSearchText = new UiObject(new UiSelector().className("android.widget.TextView").text("搜索").index(1));
            tbSearchText.clickAndWaitForNewWindow(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断贴吧"关注"
    private boolean isBaiduFriendAdd2() {
        UiObject tbFocusText = new UiObject(new UiSelector().className("android.widget.TextView").text("关注").index(0));
        if (tbFocusText.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //判断贴吧"加为好友"
    private boolean isBaiduFriendAdd3() {
        UiObject tbAddFriend = new UiObject(new UiSelector().className("android.widget.TextView").text("加为好友").index(1));
        if (tbAddFriend.waitForExists(2000)) {
            return true;
        } else {
            return false;
        }
    }

    //判断贴吧"发消息"
    private boolean isBaiduFriendAdd4() {
        UiObject tbSendMsg = new UiObject(new UiSelector().className("android.widget.TextView").text("发消息").index(1));
        if (tbSendMsg.waitForExists(8000)) {
            return true;
        } else {
            return false;
        }
    }

    //点击“发消息”
    private void BaiduFriendAdd4() {
        try {
            UiObject tbSendMsg = new UiObject(new UiSelector().className("android.widget.TextView").text("发消息").index(1));
            tbSendMsg.clickAndWaitForNewWindow(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region AutoReply

    //判断贴吧发消息输入框及发送按钮
    private boolean isAutoReply1() {
        UiObject tbSendBtn = new UiObject(new UiSelector().className("android.widget.Button").text("发送").index(3));
        if (tbSendBtn.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //输入文字，点击“发送”
    private void AutoReply1(String text) {
        try {
            jqhelper.input(text);
            jqhelper.delay(800);
            UiObject tbSendBtn = new UiObject(new UiSelector().className("android.widget.Button").text("发送").index(3));
            tbSendBtn.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断贴吧发消息页面图片按钮
    private boolean isAutoReply2() {
        UiObject tbPicBtn = new UiObject(new UiSelector().className("android.widget.TextView").text("图片").index(0));
        if (tbPicBtn.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //点击“图片”
    private void AutoReply2() {
        try {
            UiObject tbPicBtn = new UiObject(new UiSelector().className("android.widget.TextView").text("图片").index(0));
            tbPicBtn.clickAndWaitForNewWindow(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断贴吧"全部图片"
    private boolean isAutoReply3() {
        UiObject tbPicText = new UiObject(new UiSelector().className("android.widget.TextView").text("全部图片").index(0));
        if (tbPicText.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //点击“全部图片”
    private void AutoReply3() {
        try {
            UiObject tbPicText = new UiObject(new UiSelector().className("android.widget.TextView").text("全部图片").index(0));
            tbPicText.click();
            jqhelper.delay(800);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断“送达”“已读”
    private boolean isAutoReply4() {
        UiObject tbSendText = new UiObject(new UiSelector().className("android.widget.TextView").text("送达").index(0));
        UiObject tbReadText = new UiObject(new UiSelector().className("android.widget.TextView").text("已读"));
        if (tbSendText.waitForExists(2000) || tbReadText.waitForExists(2000)) {
            return true;
        } else {
            return false;
        }
    }

    //判断“！”（未送达）
    private boolean isAutoReply5() {
        UiObject tbSendText = new UiObject(new UiSelector().className("android.widget.ImageButton").index(0));
        if (tbSendText.waitForExists(5000)) {
            return true;
        } else {
            return false;
        }
    }

    //判断“a1(1)”
    private boolean isAutoReply6() {
        UiObject tba1Text = new UiObject(new UiSelector().className("android.widget.TextView").text("a1(1)").index(1));
        if (tba1Text.waitForExists(7000)) {
            return true;
        } else {
            return false;
        }
    }

    //点击“a1(1)”
    private void AutoReply6() {
        try {
            UiObject tba1Text = new UiObject(new UiSelector().className("android.widget.TextView").text("a1(1)").index(1));
            tba1Text.clickAndWaitForNewWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断“发送”
    private boolean isAutoReply7() {
        UiObject tbSendText = new UiObject(new UiSelector().className("android.widget.TextView").text("发送").index(1));
        if (tbSendText.waitForExists(7000)) {
            return true;
        } else {
            return false;
        }
    }

    //点击“发送”
    private void AutoReply7() {
        try {
            UiObject tbSendText = new UiObject(new UiSelector().className("android.widget.TextView").text("发送").index(1));
            tbSendText.clickAndWaitForNewWindow();
            jqhelper.delay(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //回复图片
    public String ReplyPic() {
        Device.click(120, 1116);
        jqhelper.delay(1200);
        if (isAutoReply2()) {
            AutoReply2();
            if (isAutoReply3()) {
                AutoReply3();
                if (isAutoReply6()) {
                    AutoReply6();
                    Device.click(204, 202);
                    jqhelper.delay(800);
                    Device.click(626, 1112);
                    jqhelper.delay(1000);
                    if (isAutoReply7()) {
                        AutoReply7();
//                        if (msg == "" && !text.isEmpty()) {
//                            if (!isAutoReply5())
//                                return "";
//                        } else {
//                            if (isAutoReply4())
//                                return "";
//                        }
                    }
                }
            }
        }
        return "回复图片失败;";
    }

    //回复文字
    public void ReplyText(String text) {
        for (String s:text.split("\n")) {
            AutoReply1(s);
            jqhelper.delay(1000);
        }
    }

    public String AutoReply(String name, String text, boolean isLoad) {
        String msg = "";
        try {
            if (isBaiduFriendAdd1()) {
                jqhelper.delay(1500);
                Device.click(160, 250); //点击输入框
                jqhelper.delay(800);
                BaiduFriendAdd1(name);
                if (!isBaiduFriendAdd2()) {
                    Device.click(550, 244);  //清空输入框
                    return "好友不存在或网络异常";
                } else {
                    if (isBaiduFriendAdd4()) {
                        BaiduFriendAdd4();
                        if (isAutoReply1()) {
                            if (!text.isEmpty())
                                ReplyText(text);
                            if (isLoad)
                                ReplyPic();
                            if (isAutoReply4())
                                msg = "";
                            else
                                msg = "回复失败";
                            Device.pressBack(); //点击返回，当前页面处于好友详情页面
                            jqhelper.delay(1000);
                        } else
                            msg = "未跳转至‘发消息’页面";
                    } else if (isBaiduFriendAdd3())
                        msg = "该好友未添加";
                    else
                        msg = "网络异常";
                    jqhelper.startActivity("com.baidu.tieba/.addresslist.im.searchfriend.SearchFriendActivity");
                    jqhelper.delay(2000);
                }
            } else
                msg = "未跳转至‘查找好友’页面";
        } catch (Exception e) {
            return "AutoReply抛错" + e.getMessage();
        }
        return msg;
    }

    //endregion
}
