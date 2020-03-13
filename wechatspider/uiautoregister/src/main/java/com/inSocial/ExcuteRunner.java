package com.inSocial;

import android.os.RemoteException;
import com.android.uiautomator.core.*;

import com.google.gson.reflect.TypeToken;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.uiControl.UiEdit;
import com.common.helper.ScriptHelper;


import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


//*******************************************
//自动化执行框架
//********************************************
public class ExcuteRunner extends SuperRunner {

        //region 【初始化数据】
        private List<String> OverList=new ArrayList<String>();
        private boolean initializtion() {
                PackegName = "com.jiuyan.infashion";
                ActivityName = "login.MainActivity";
                Device = getUiDevice();
                try {
                        Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
                } catch (Exception e) {
                        Loge("initializtion", "get the vpn name error：" + e.getMessage());
                        return false;
                }
                urlMap.put("getUrl", "http://172.18.80.3:8008/api/insocial/list?v=" + Vps + "&t=50&n=1&f=20&fl=20");
                urlMap.put("postUrl", "http://172.18.80.3:8008/api/insocial/updatecomplete/");
                urlMap.put("commentUrl", "http://172.18.80.3:8008/api/insocial/getinsocialcomment?id=");
                return true;
        }
        //endregion

        //region 【业务逻辑流程执行】
        public void testRunner() throws UiObjectNotFoundException, RemoteException {
                if (!initializtion()) return;//如果初始化失败，则结束
                //循环执行任务
                while (true) {
                        try {
                                ScriptHelper.reportNormal();
                                List<RequestModel> requestList = GetHttpStringByUrl(urlMap.get("getUrl"));
                                if (requestList == null || requestList.size() == 0) {
                                        Logi("there is no response data,request url:" + urlMap.get("getUrl"));
                                        sleep(600000);
                                        continue;
                                }
                                for (int i = 0; i < requestList.size(); i++) {
                                        RequestModel request = requestList.get(i);
                                        //初始化提交model
                                        SubmitModel submit = new SubmitModel(request.ID);
                                        List<LogInfoModel> logList = new ArrayList<LogInfoModel>();//初始化日志列表

                                        ScriptHelper.switchImeToAdbKeyboard();//切换输入法

                                        //重启并登录应用
                                        if (!restartAndLoginAPP(request, submit, logList)) {
                                                continue;
                                        }
                                        //执行发图任务
                                        if (!PictureTask(submit, request, logList)) {
                                                //重启
                                                restartAPP();
                                        }

                                        //克隆信息
                                        if (!CloneTask(submit, request, logList)) {
                                                //重启
                                                restartAPP();
                                        }
                                        //爬取系统消息
                                        SystemInfoTask(submit, request, logList);
                                        if (submit.Status == 2) {
                                                //账号被封，回送替换账号命令，并重新执行
                                                PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                                                continue;
                                        }

                                        //关注和好友操作
                                        if (!FollowAndFriendTask(submit, request, logList)) {
                                                restartAPP();
                                        }

                                        //评论
                                        if (!CommentTask(submit, request, logList)) {
                                                restartAPP();
                                        }
                                        if (!SendCommentTask(submit, request, logList)) {
                                                restartAPP();
                                        }
                                        //聊天
                                        if (!MessageTask(submit, request, logList)) {
                                                restartAPP();
                                        }
                                        if (!SendMessageTask(submit, request, logList)) {
                                                restartAPP();
                                        }
                                        //提交运行结果
                                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                                }
                        }catch (Exception ee){
                                ScriptHelper.writeLog(ee.getMessage());
                                sleep(600000);
                        }
                }
        }
        //endregion

        //region 【获取任务，提交结果】
        private List<RequestModel> GetHttpStringByUrl(String url) {
                try {
                        String html = HttpHelper.httpGetString(url);
                        Type type = new TypeToken<List<RequestModel>>() {
                        }.getType();
                        return gson.fromJson(html, type);
                } catch (Exception e) {
                        Loge("GetHttpStringByUrl", e.getMessage());
                        return null;
                }
        }

        private List<InSocialCommentModel> GetCommentByUrl(String url) {
                try {
                        String html = HttpHelper.httpGetString(url);
                        Type type = new TypeToken<List<InSocialCommentModel>>() {
                        }.getType();
                        return gson.fromJson(html, type);
                } catch (Exception e) {
                        Loge("GetHttpStringByUrl", e.getMessage());
                        return null;
                }
        }


        private void PostHttpSubmit(String url, SubmitModel submitModel, List<LogInfoModel> logList) {
                // if (true) return;
                try {
                        submitModel.Logs = gson.toJson(logList);
                        String json = "{\"list\":" + gson.toJson(submitModel, new TypeToken<SubmitModel>() {
                        }.getType()) + "}";
                        HttpHelper.httpPostForString(url, json);
                } catch (Exception e) {
                        ScriptHelper.writeLog("连接远程服务器异常：" + e.getMessage());
                }
        }
        //endregion

        //region 【重启并登录应用 001】
        private boolean restartAPP()throws UiObjectNotFoundException {
                ScriptHelper.killApp(PackegName);
                sleep(1000);
                ScriptHelper.startActivity(PackegName + "/." + ActivityName);
                sleep(5000);
                ob = new UiObject(new UiSelector().className("android.widget.Button").text("以后再说"));
                if (ob.waitForExists(10000)) {
                        ob.clickAndWaitForNewWindow();
                }
                return true;
        }

        private boolean restartAndLoginAPP(RequestModel request, SubmitModel submit, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                ScriptHelper.switchImeToAdbKeyboard();//切换输入法
                ScriptHelper.killApp(PackegName);
                ScriptHelper.clearPackage(PackegName);
                sleep(1000);
                ScriptHelper.startActivity(PackegName + "/." + ActivityName);
                UiScrollable aa = new UiScrollable(new UiSelector().className("android.webkit.WebView"));
                if (aa.waitForExists(10000)) {
                        aa.setAsHorizontalList();
                        aa.scrollForward();
                        aa.scrollForward();
                        aa.scrollForward();
                        aa.scrollForward();
                }
                Device.click(367, 1005);
                UiObject login = new UiObject(new UiSelector().clickable(true).text("登录"));
                if (!login.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "001", "操作异常，未能进入登录界面，请检查", request.InID, 0, Vps));
                        Logi("can't enter the login interface");
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }

                edit = new UiEdit(1);
                if (edit.exists()) edit.setChineseText(request.InID);

                edit = new UiEdit(3);
                if (edit.exists()) edit.setChineseText(request.Password);

                ob = new UiObject(new UiSelector().text("登录"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "002", "未能进入登录界面，请检查", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                ob.clickAndWaitForNewWindow(5000);
                if (new UiObject(new UiSelector().text("登录")).exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Login.GetDes(), "003", "未输入账号密码，登录失败", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }

                if (new UiObject(new UiSelector().text("账号或密码错误")).exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Login.GetDes(), "004", "账号或密码错误，登录失败", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                if (new UiObject(new UiSelector().text("没有注册过哦")).exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Login.GetDes(), "005", "账号没有注册过，登录失败", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }

                ob = new UiObject(new UiSelector().text("完成"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "006", "未能进入推荐好友界面，请检查", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                ob = new UiObject(new UiSelector().className("android.widget.Button").text("以后再说"));
                if (ob.exists()) {
                        ob.clickAndWaitForNewWindow();
                }
                ob = new UiObject(new UiSelector().className("android.widget.ImageView").instance(1));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "007", "未能进入in记之旅邀请信，请检查", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                ob = new UiObject(new UiSelector().clickable(true).instance(0));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "008", "未能进入玩字说明，请检查", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                ob.clickAndWaitForNewWindow();

                if (!new UiObject(new UiSelector().text(request.InID)).exists()) {//判断是否进去个人主页
                        logList.add(inSocialHelper.getLogInfo(OperationType.Others.GetDes(), "009", "未能进入个人主页界面，请检查", request.InID, 0, Vps));
                        PostHttpSubmit(urlMap.get("postUrl"), submit, logList);
                        return false;
                }
                return true;
        }
        //endregion

        //region 【爬取系统消息 101】
        private boolean SystemInfoTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                try {
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo("爬取系统消息", "101", "未找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.clickAndWaitForNewWindow();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("我的消息"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo("爬取系统消息", "102", "未找到我的消息按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.clickAndWaitForNewWindow();


                        UiScrollable list = new UiScrollable(new UiSelector().className("android.widget.ListView"));
                        if (!list.exists()) {
                                logList.add(inSocialHelper.getLogInfo("爬取系统消息", "103", "未找到系统消息", request.InID, 0, Vps));
                                return false;
                        }
                        list.setAsVerticalList();
                        if (list.scrollIntoView(list.getChild(new UiSelector().className("android.widget.TextView").clickable(true).textContains("个人简介或者发布涉嫌广告")))) {
                                submitModel.Status = 2;
                                logList.add(inSocialHelper.getLogInfo("爬取系统消息", "104", "账号被封", request.InID, 0, Vps));
                                return false;
                        }
                        ////////////////////////////////////////////////////////////////////
                        logList.add(inSocialHelper.getLogInfo("爬取系统消息", "105", "爬取成功", request.InID, 1, Vps));
                } catch (Exception ee) {

                } finally {
                        Device.pressBack();
                }
                return true;
        }
        //endregion

        //region 【发图任务 201】
        private boolean PictureTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                String type = OperationType.Picture.GetDes();

                if (request.PublishPicture.equals(""))
                        return true;

                ////////////////////////////////////////////////////////////////////
                //开始下载图片
                try {
                        HttpHelper.downFile("http://222.185.251.62:34567/files/download?ID=" + request.PublishPicture, "/sdcard/DCIM/Camera/tempscr.png");
                        if (!ScriptHelper.fileIsExists("/sdcard/DCIM/Camera/tempscr.png")) {
                                logList.add(inSocialHelper.getLogInfo(type, "200", "下载图片失败：图片ID为" + request.PublishPicture, request.InID, 0, Vps));
                                return true;//不关闭，直接执行下一个任务
                        }
                } catch (Exception ee) {
                        logList.add(inSocialHelper.getLogInfo(type, "200", "下载图片失败：图片ID为" + request.PublishPicture, request.InID, 0, Vps));
                        return false;
                }
                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("in记"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "201", "未找到in记按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();

                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").index(3));
                if (!ob.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "202", "未找到发图按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();//点击发图按钮

                ob = new UiObject(new UiSelector().className("android.support.v7.widget.RecyclerView"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "203", "未找到图片框", request.InID, 0, Vps));
                        return false;
                }
                ob = ob.getChild(new UiSelector().className("android.widget.ImageView").instance(0));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "204", "未找到第一张图片", request.InID, 0, Vps));
                        return false;
                }
                ob.click();
                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("下一步"));
                if (!ob.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "205", "未找到下一步按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();
                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("完成"));
                if (!ob.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "206", "未找到完成按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();
                if (!request.PublishDesc.equals("")) {
                        edit = new UiEdit(0);
                        if (!edit.waitForExists(1000)) {
                                if (!ob.waitForExists(2000)) {
                                        logList.add(inSocialHelper.getLogInfo(type, "207", "未找到描述控件", request.InID, 0, Vps));
                                        return false;
                                }
                        }
                }
                edit.setChineseText(request.PublishDesc);

                Device.click(400, 150);

                ob = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(2).clickable(true).childSelector(new UiSelector().className("android.widget.TextView").index(0).text("发布")));
                if (!ob.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "208", "未找到发布按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                ob = new UiObject(new UiSelector().className("android.widget.ImageButton").index(2));
                if (!ob.waitForExists(3000)) {
                        submitModel.Status = 2;
                        logList.add(inSocialHelper.getLogInfo(type, "209", "发送图片失败", request.InID, 0, Vps));
                        return false;
                }
                logList.add(inSocialHelper.getLogInfo(type, "210", "发布图片成功", request.InID, 1, Vps));
                submitModel.PublishPictureState = 1;
                submitModel.PublishPictureTime = getDateTime();
                return true;
        }
        //endregion

        //region 【关注和加好友任务 301】
        List<String> followList = new ArrayList<String>();
        List<String> friendList = new ArrayList<String>();

        private boolean FollowAndFriendTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                String type = OperationType.Follow.GetDes();
                int Follows = 0;
                if (request.FollowList.size() > 0) {//如果有关注任务，则先获得当前关注数
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "301", "未找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("关注数")).getFromParent(new UiSelector().className("android.widget.TextView").index(0));
                        if (ob.exists()) {
                                Follows = Integer.parseInt(ob.getText());
                        }
                }


                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("发现"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "302", "未找到发现按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();


                edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                if (!edit.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "303", "未找到搜索用户控件", request.InID, 0, Vps));
                        return false;
                }
                if (OverList.contains(request.InID)) {
                        OverList.remove(request.InID);
                }


                followList = request.FollowList;
                friendList = request.FriendList;
                //执行关注任务
                for (int j = 0; j < followList.size(); j++) {
                        if (request.FollowRemained > 0) {
                                logList.addAll(ExcuteFollowAndFriend(followList.get(j), 0, submitModel, request));
                        }
                }
                //执行交友任务
                for (int k = 0; k < friendList.size(); k++) {
                        if(OverList.contains(request.InID)){
                                break;
                        }
                        if (request.FriendRemained > 0) {
                                logList.addAll(ExcuteFollowAndFriend(friendList.get(k), 1, submitModel, request));
                        }
                }

                if (request.FollowList.size() > 0) {//如果有关注任务，则先获得当前关注数
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "304", "未找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("关注数")).getFromParent(new UiSelector().className("android.widget.TextView").index(0));
                        if (ob.exists()) {
                                if (Follows == Integer.parseInt(ob.getText())) {
                                        submitModel.Status = 2;
                                        logList.add(inSocialHelper.getLogInfo(type, "305", "关注未成功,可能是被禁号", request.InID, 0, Vps));
                                        return false;
                                }
                        }
                }
                logList.add(inSocialHelper.getLogInfo(type, "320", "关注和加好友成功", request.InID, 1, Vps));
                return true;
        }

        private List<LogInfoModel> ExcuteFollowAndFriend(String InID, int type, SubmitModel submitModel, RequestModel request) throws UiObjectNotFoundException {
                List<LogInfoModel> logList = new ArrayList<LogInfoModel>();
                try {

                        edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                        if (!edit.waitForExists(3000)) {
                                logList.add(inSocialHelper.getLogInfo(OperationType.Search.GetDes(), "306", "未找到搜索用户控件", InID, 0, Vps));
                                return logList;
                        }
                        edit.setChineseText(InID);
                        sleep(3000);
                        //判断是否有匹配的搜索内容
                        UiScrollable roll = new UiScrollable(new UiSelector().className("android.widget.ListView").index(1));
                        if (!roll.waitForExists(5000) || roll.getChildCount() == 0) {
                                logList.add(inSocialHelper.getLogInfo(OperationType.Search.GetDes(), "307", "未搜索到符合条件的", InID, 0, Vps));
                                return logList;
                        }
                        ob = new UiObject(new UiSelector().text("in号:" + InID));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(OperationType.Search.GetDes(), "308", "未搜索到用户", InID, 0, Vps));
                                return logList;
                        }


                        ob.clickAndWaitForNewWindow();//选中搜索到的人
                        if (type == 0 && request.FollowRemained > 0) {//如果是关注功能
                                submitModel.FollowCurrentID = InID;
                                if (!new UiObject(new UiSelector().className("android.widget.TextView").text("关注TA").index(0)).exists()) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Follow.GetDes(), "309", "该用户已经关注过", InID, 0, Vps));
                                } else {
                                        ob = new UiObject(new UiSelector().className("android.widget.ImageView").clickable(true).index(1));
                                        if (!ob.exists()) {
                                                logList.add(inSocialHelper.getLogInfo(OperationType.Follow.GetDes(), "310", "未找到关注按钮", InID, 0, Vps));
                                        } else {
                                                ob.click();
                                                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("朋友"));
                                                if (ob.waitForExists(3000)) {
                                                        ob.clickAndWaitForNewWindow(1000);
                                                        if (!new UiObject(new UiSelector().className("android.widget.TextView").text("关注TA").index(0)).exists()) {
                                                                logList.add(inSocialHelper.getLogInfo(OperationType.Follow.GetDes(), "311", "关注成功", InID, 1, Vps));
                                                                submitModel.FollowSuccessNum = submitModel.FollowSuccessNum + 1;
                                                                request.FollowRemained = request.FollowRemained - 1;//余值-1
                                                        } else {
                                                                logList.add(inSocialHelper.getLogInfo(OperationType.Follow.GetDes(), "312", "关注失败", InID, 0, Vps));
                                                        }
                                                }
                                        }
                                }
                        }

                        if (request.FriendRemained > 0 && request.FriendList.contains(InID) && !OverList.contains(request.InID)) {//关注的同时还有加好友任务
                                friendList.remove(InID);//从好友列表中删除该人员
                                submitModel.FriendCurrentID = InID;
                                ob = new UiObject(new UiSelector().className("android.widget.ImageView").index(4).clickable(true));
                                if (!ob.exists()) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "313", "没有找到聊天按钮", InID, 0, Vps));
                                        return logList;
                                }
                                ob.clickAndWaitForNewWindow();

                                if (new UiObject(new UiSelector().className("android.widget.TextView").text("你今天与未相互关注的人的10次聊天机会已经用完了，明天再聊吧~")).exists()) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "314", "达到每日聊天次数限制", InID, 0, Vps));
                                        submitModel.IsOverFriend = 1;
                                        OverList.add(request.InID);
                                        return logList;
                                }
                                edit = new UiEdit(new UiSelector().className("android.widget.EditText").index(2).text("留言~"));
                                if (!edit.exists()) {
                                        if(new UiObject(new UiSelector().className("android.widget.ListView")).exists()){//进入到好友聊天界面，则跳出
                                                logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "318", "已经是好友", InID, 0, Vps));
                                                Device.pressBack();
                                        }else {
                                                logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "315", "新增好友失败，可能是该用户版本过低", InID, 0, Vps));
                                        }
                                        return logList;
                                }
                                String message = request.ReplyList.get(new Random().nextInt(request.ReplyList.size()));
                                edit.setChineseText(message);

                                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).clickable(true).text("发送"));
                                if (!ob.exists()) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "316", "没有找到发送按钮", InID, 0, Vps));
                                        return logList;
                                }
                                ob.clickAndWaitForNewWindow();
                                logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "317", "验证好友成功,发送信息：" + message, InID, 1, Vps));
                                submitModel.FriendSuccessNum = submitModel.FriendSuccessNum + 1;
                                request.FriendRemained = request.FriendRemained - 1;//余值-1
                        }
                } catch (Exception ee) {
                        System.out.print(ee.getMessage());
                } finally {
                        Device.pressBack();
                }
                return logList;
        }

        //endregion

        //region 【克隆任务 401】
        private boolean CloneTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                if (request.InName == null || request.InName.equals("") || request.InName.equals(request.Name))
                        return true;
                ////////////////////////////////////////////////////////////////////
                String type = OperationType.Clone.GetDes();
                ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "401", "未找到中心按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();

                //判断是否修改了名字
                if (new UiObject(new UiSelector().className("android.widget.TextView").index(1).text(request.InName)).exists()) {
                        //如果已经存在该名字则不执行克隆
                        return true;
                }


                ob = new UiObject(new UiSelector().className("android.widget.ScrollView").index(0).childSelector(new UiSelector().className("android.widget.TextView").index(3).textStartsWith("in号:")));
                if (!ob.waitForExists(3000)) {
                        logList.add(inSocialHelper.getLogInfo(type, "402", "未找到我的信息", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();


                if (request.InImage != "") {//设置头像
                        try {
                                //开始下载图片
                                HttpHelper.downFile("http://222.185.251.62:34567/files/download?ID=" + request.PublishPicture, "/sdcard/DCIM/Camera/tempscr.png");
                                if (ScriptHelper.fileIsExists("/sdcard/DCIM/Camera/tempscr.png")) {
                                        ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0).childSelector(new UiSelector().className("android.widget.TextView").index(0).text("头像")));
                                        if (!ob.exists()) {
                                                logList.add(inSocialHelper.getLogInfo(type, "403", "未找到头像按钮", request.InID, 0, Vps));
                                                return false;
                                        }
                                        ob.clickAndWaitForNewWindow();

                                        ob = new UiObject(new UiSelector().className("android.widget.FrameLayout").index(1)).getChild(new UiSelector().className("android.widget.ImageView").clickable(true));
                                        if (!ob.exists()) {
                                                logList.add(inSocialHelper.getLogInfo(type, "404", "未找到第二中图片", request.InID, 0, Vps));
                                                return false;
                                        }
                                        ob.click();
                                        ob = new UiObject(new UiSelector().className("android.widget.Button").index(1).text("下一步"));
                                        if (!ob.exists()) {
                                                logList.add(inSocialHelper.getLogInfo(type, "405", "未找到下一步按钮", request.InID, 0, Vps));
                                                return false;
                                        }
                                        ob.clickAndWaitForNewWindow(2000);
                                        ob = new UiObject(new UiSelector().className("android.widget.ImageView").index(4));
                                        if (!ob.exists()) {
                                                logList.add(inSocialHelper.getLogInfo(type, "406", "未找到确认按钮", request.InID, 0, Vps));
                                                return false;
                                        }
                                        ob.clickAndWaitForNewWindow();
                                }
                                else{
                                        logList.add(inSocialHelper.getLogInfo(type, "416", "下载图片失败：图片ID为" + request.PublishPicture, request.InID, 0, Vps));
                                }
                        } catch (Exception ee) {
                                logList.add(inSocialHelper.getLogInfo(type, "407", "下载图片失败：图片ID为" + request.InImage, request.InID, 0, Vps));
                                return false;
                        }
                }
                //设置昵称
                //连续两次，共点击24次删除
                edit = new UiEdit(new UiSelector().className("android.widget.EditText").index(0).longClickable(true).clickable(true));
                if (!edit.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "408", "未找到昵称按钮", request.InID, 0, Vps));
                        return false;
                }
                edit.click();
                edit.click();

                for (int i = edit.getText().length(); i >= 0; i--) {
                        Device.pressDelete();
                }
                edit.setChineseText(request.InName);

                //设置简介
                ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(8));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "409", "未找到简介按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                if (!edit.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "410", "未找到简介输入框", request.InID, 0, Vps));
                        return false;
                }
                edit.setChineseText(request.InDesc);

                ob = new UiObject(new UiSelector().index(2).text("完成"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "411", "未找到完成按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();
                //设置性别
                ob = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(12).childSelector(new UiSelector().className("android.widget.TextView").index(0).text("性别")));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "412", "未找到性别按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                ob = new UiObject(new UiSelector().className("android.widget.TextView").text(request.InGender));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "413", "未找到性别按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.click();

                ob = new UiObject(new UiSelector().className("android.widget.Button").text("完成"));
                if (!ob.exists()) {
                        logList.add(inSocialHelper.getLogInfo(type, "414", "未找到完成按钮", request.InID, 0, Vps));
                        return false;
                }
                ob.clickAndWaitForNewWindow();
                ////////////////////////////////////////////////////////////////////

                logList.add(inSocialHelper.getLogInfo(type, "415", "克隆资料完成", request.InID, 1, Vps));
                return true;
        }
        //endregion

        //region 【评论任务 501】
        private boolean CommentTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                List<InSocialCommentModel> commentList = new ArrayList<InSocialCommentModel>();
                String type = OperationType.Comment.GetDes();
                try {

                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "501", "没有找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("我的消息"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "502", "没有找到我的消息按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.clickAndWaitForNewWindow();
                        //获取新评论标签
                        ob = new UiObject(new UiSelector().className("android.widget.ListView")).getChild(new UiSelector().instance(0)).getChild(new UiSelector().instance(2)).getChild(new UiSelector().className("android.widget.TextView").index(2));
                        if (!ob.exists()) {//没有最新的评论则跳出
                                return true;
                        }
                        //获取新评论数量
                        int newComment = Integer.parseInt(ob.getText());
                        ob.clickAndWaitForNewWindow();//进入评论列表
                        UiScrollable roll = new UiScrollable(new UiSelector().className("android.widget.ListView"));
                        if (!roll.waitForExists(2000)) {
                                logList.add(inSocialHelper.getLogInfo(type, "503", "没有找到评论列表", request.InID, 0, Vps));
                                return false;
                        }

                        List<InSocialCommentModel> clist = GetCommentByUrl(urlMap.get("commentUrl") + request.ID);//获取系统中存在的评论列表
                        if (clist == null) {
                                clist = new ArrayList<InSocialCommentModel>();
                        }
                        for (int i = 0; i < newComment; i++) {
                                ob = roll.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), i);
                                if (!ob.exists()) {//未找到则下拉一把
                                        roll.scrollForward();
                                }
                                if (!ob.exists()) {//还未找到则执行下一个
                                        continue;
                                }
                                String sender = ob.getChild(new UiSelector().className("android.widget.LinearLayout").index(1).childSelector(new UiSelector().className("android.widget.TextView").index(0))).getText();
                                String receive = request.InName;
                                String text = ob.getChild(new UiSelector().className("android.widget.TextView").index(2)).getText();
                                if (!sender.equals(request.InName) && !ExistsInList(clist, sender, receive, text)) {
                                        InSocialCommentModel model = new InSocialCommentModel(-1, request.ID, sender, request.InName, text, (byte) 0, (byte) 2);
                                        commentList.add(model);
                                        clist.add(model);
                                }
                        }
                } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                } finally {
                        submitModel.CommentInfo = commentList;
                        Device.pressBack();
                        Device.pressBack();
                }
                logList.add(inSocialHelper.getLogInfo(type, "508", "抓取评论信息完成", request.InID, 1, Vps));
                return true;
        }


        //主动发送评论
        private  boolean SendCommentTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {

                List<InSocialCommentModel> commentList = new ArrayList<InSocialCommentModel>();
                String type = OperationType.Comment.GetDes();
                try {
                        List<InSocialCommentModel> clist = request.CommentList;//获取系统中存在的评论列表
                        if (clist == null || clist.size() == 0) {
                                return true;
                        }

                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "504", "没有找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("我的消息"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "505", "没有找到我的消息按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.clickAndWaitForNewWindow();
                        //获取新评论标签
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("评论"));
                        if (!ob.exists()) {//没有找到评论按钮
                                return true;
                        }
                        //获取新评论数量

                        ob.clickAndWaitForNewWindow(3000);//进入评论列表
                        UiScrollable roll = new UiScrollable(new UiSelector().className("android.widget.ListView"));
                        int newComment = roll.getChildCount();
                        if (!roll.waitForExists(2000)) {
                                logList.add(inSocialHelper.getLogInfo(type, "506", "没有找到评论列表", request.InID, 0, Vps));
                                return false;
                        }
                        //遍历新的评论,找到有此人的信息
                        for (int k = clist.size() - 1; k >= 0; k--) {
                                InSocialCommentModel c = clist.get(k);
                                for (int i = 0; i < newComment; i++) {
                                        ob = roll.getChild(new UiSelector().className("android.widget.LinearLayout").index(1).childSelector(new UiSelector().className("android.widget.TextView").index(0)));
                                        if (ob.exists() && c.ReceiveName.equals(ob.getText())) {//找到对应人员
                                                ob.clickAndWaitForNewWindow();//点击评论获取详细评论信息
                                                UiScrollable r = new UiScrollable(new UiSelector().className("android.widget.ListView").index(0));
                                                if (!r.exists()) {
                                                        logList.add(inSocialHelper.getLogInfo(type, "507", "没有找到评论信息", request.InID, 0, Vps));
                                                        Device.pressBack();
                                                        continue;
                                                }
                                                r.scrollIntoView(new UiSelector().childSelector(new UiSelector().className("android.widget.TextView").text(c.ReceiveName)));
                                                r.scrollForward();
                                                ob = new UiObject(new UiSelector().className("android.widget.TextView").text(c.ReceiveName));
                                                if (ob.exists()) {
                                                        Device.click(ob.getBounds().left, ob.getBounds().top + 60);
                                                        edit = new UiEdit(new UiSelector().className("android.widget.EditText").index(0));
                                                        if (edit.exists()) {
                                                                edit.setChineseText(c.Text);
                                                                if (new UiObject(new UiSelector().className("android.widget.TextView").text("发送")).exists()) {
                                                                        new UiObject(new UiSelector().className("android.widget.TextView").text("发送")).click();
                                                                        commentList.add(new InSocialCommentModel(c));
                                                                        clist.remove(k);
                                                                        Device.pressBack();
                                                                        break;
                                                                }
                                                        }
                                                }
                                                Device.pressBack();
                                                roll.scrollToBeginning(10, 10);//滚动到最上
                                        }
                                }
                        }
                } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                } finally {
                        submitModel.CommentInfo = commentList;
                        Device.pressBack();
                        if (!new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心")).waitForExists(2000))
                                Device.pressBack();
                }
                logList.add(inSocialHelper.getLogInfo(type, "509", "回复评论信息完成", request.InID, 1, Vps));
                return true;
        }
        //endregion

        //region 【聊天任务 601】
        private boolean MessageTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                List<InSocialChatModel> chatList = new ArrayList<InSocialChatModel>();
                String type = OperationType.Message.GetDes();
                try {
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("中心"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "601", "没有找到中心按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(1).text("我的聊天"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "602", "没有找到我的聊天按钮", request.InID, 0, Vps));
                                return false;
                        }
                        //寻找兄弟元素  聊天数量
                        ob = ob.getFromParent(new UiSelector().className("android.widget.TextView"));
                        if (!ob.exists()) {
                                Device.pressBack();
                                return true;//没有新的聊天信息，则返回
                        }
                        ob.clickAndWaitForNewWindow();
                        //获取新评论标签
                        UiCollection co = new UiCollection(new UiSelector().className("android.widget.ListView"));
                        int children = co.getChildCount();
                        if (!co.exists() || children < 2) {
                                //没有最新的评论则跳出
                                logList.add(inSocialHelper.getLogInfo(type, "603", "没有找到评论列表", request.InID, 0, Vps));
                                return false;
                        }
                        //遍历新的评论
                        for (int i = 0; i < children - 1; i++) {
                                UiObject child = co.getChild(new UiSelector().className("android.widget.RelativeLayout").instance(i));
                                if (!child.exists()) continue;
                                String Name = child.getChild(new UiSelector().className("android.widget.TextView").instance(0)).getText();
                                if (Name.equals("客服君")) continue;
                                child.clickAndWaitForNewWindow();//点击评论获取详细评论信息

                                ob = new UiObject(new UiSelector().className("android.widget.Button").text("忽略全部"));
                                if (ob.exists()) {//  //如果是好友验证信息，则点击同意按钮
                                        UiCollection co2 = new UiCollection(new UiSelector().className("android.widget.ListView"));
                                        for (int j = 0; j < co2.getChildCount(); j++) {
                                                co2.getChild(new UiSelector().instance(j)).getChild(new UiSelector().className("android.widget.Button").text("同意")).clickAndWaitForNewWindow();
                                                GetMessgae(chatList, logList, request, Name);
                                        }
                                } else {
                                        GetMessgae(chatList, logList, request, Name);
                                }
                                while (!new UiObject(new UiSelector().className("android.widget.TextView").text("聊天通讯录")).exists()) {
                                        Device.pressBack();
                                }
                        }
                } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                        logList.add(inSocialHelper.getLogInfo(type, "612", "执行聊天过程出错", request.InID, 0, Vps));
                } finally {
                        submitModel.MessageInfo.addAll(chatList);
                        Device.pressBack();
                }
                logList.add(inSocialHelper.getLogInfo(type, "613", "完成获取最新消息命令", request.InID, 1, Vps));
                return true;
        }

        ///遍历消息列表 获取数据
        private void GetMessgae(List<InSocialChatModel> chatList, List<LogInfoModel> logList, RequestModel request, String Name) throws UiObjectNotFoundException {
                String type = OperationType.Message.GetDes();
                try {
                        UiCollection cc = new UiCollection(new UiSelector().className("android.widget.ListView"));
                        int imgcount = cc.getChildCount(new UiSelector().className("android.widget.ImageView"));
                        String targetID = "";
                        for (int k = 0; k < imgcount; k++) {
                                ob = cc.getChildByInstance(new UiSelector().className("android.widget.ImageView"), k);
                                if (ob.exists() && ob.getBounds().left == 15) {
                                        ob.clickAndWaitForNewWindow();
                                        ob = new UiObject(new UiSelector().className("android.widget.TextView").text(Name)).getFromParent(new UiSelector().className("android.widget.TextView").index(2));
                                        if (ob.exists()) {
                                                targetID = ob.getText();
                                                Device.pressBack();
                                                break;
                                        }
                                        Device.pressBack();
                                }
                        }
                        if (targetID.equals("")) {
                                //没有找到对应的InID则报错
                        }
                        int textcount = cc.getChildCount(new UiSelector().className("android.widget.TextView"));
                        for (int k = 0; k < textcount; k++) {
                                ob = cc.getChildByInstance(new UiSelector().className("android.widget.TextView"), k);// new UiObject.instance(k));
                                if (ob.exists() && ob.getBounds().left == 120) {
                                        chatList.add(new InSocialChatModel(-1, request.ID, Name, targetID, request.InName, request.InID, ob.getText(), (byte) 0, (byte) 2, ""));
                                }
                        }

                        edit = new UiEdit(new UiSelector().className("android.widget.EditText").clickable(true).index(0));
                        if (edit.exists()) {
                                String m = "";
                                if (request.ReplyList.size() > 1)
                                        m = request.ReplyList.get(new Random().nextInt(request.ReplyList.size() - 1));
                                else
                                        m = request.ReplyList.get(0);
                                edit.setChineseText(m);
                                ob = new UiObject(new UiSelector().className("android.widget.Button").text("发送").index(2));
                                if (ob.exists()) {
                                        ob.click();//点击发送
                                }
                                chatList.add(new InSocialChatModel(-1, request.ID, request.InName, request.InID, Name, targetID, m, (byte) 2, (byte) 1, ""));

                                ob = new UiObject(new UiSelector().className("android.widget.Button").index(2));
                                if (ob.exists()) {
                                        ob.click();
                                }
                                String pic = request.ReplyPicture.split("\\|")[new Random().nextInt(request.ReplyPicture.split("\\|").length)];
                                HttpHelper.downFile("http://222.185.251.62:34567/files/download?ID=" + pic, "/sdcard/InCamera/tempscr.png");
                                if (ScriptHelper.fileIsExists("/sdcard/InCamera/tempscr.png")) {
                                        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("图片")).getFromParent(new UiSelector().className("android.widget.ImageView"));
                                        if (ob.exists()) {
                                                ob.clickAndWaitForNewWindow();
                                                if (!new UiObject(new UiSelector().className("android.widget.Button").text("相机")).exists()) {
                                                        Device.click(360, 660);
                                                        sleep(1000);
                                                        Device.click(360, 660);
                                                        sleep(1000);
                                                        chatList.add(new InSocialChatModel(-1, request.ID, request.InName, request.InID, targetID, Name, "", (byte) 2, (byte) 1, pic));
                                                } else {
                                                        logList.add(inSocialHelper.getLogInfo(type, "604", "没有找到下载的图片", "图片ID" + pic, 0, Vps));
                                                }
                                                Device.pressBack();
                                        }
                                }
                        }
                } catch (Exception ee) {

                }
        }

        //发送消息任务
        private boolean SendMessageTask(SubmitModel submitModel, RequestModel request, List<LogInfoModel> logList) throws UiObjectNotFoundException {
                if (request.MessageList == null || request.MessageList.size() == 0)
                        return true;
                List<InSocialChatModel> chatList = new ArrayList<InSocialChatModel>();
                String type = OperationType.Message.GetDes();
                try {

                        ob = new UiObject(new UiSelector().className("android.widget.TextView").index(0).text("发现"));
                        if (!ob.exists()) {
                                logList.add(inSocialHelper.getLogInfo(type, "605", "未找到发现按钮", request.InID, 0, Vps));
                                return false;
                        }
                        ob.click();
                        edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                        if (!edit.waitForExists(3000)) {
                                logList.add(inSocialHelper.getLogInfo(type, "606", "未找到搜索用户控件", request.InID, 0, Vps));
                                return false;
                        }
                        List<InSocialChatModel> messageList = request.MessageList;
                        //执行关注任务
                        for (int j = 0; j < messageList.size(); j++) {
                                edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                                if (!edit.waitForExists(3000)) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Search.GetDes(), "607", "未找到搜索用户控件", messageList.get(j).ReceiveInID, 0, Vps));
                                        continue;
                                }
                                edit.setChineseText(messageList.get(j).ReceiveInID);
                                sleep(2000);
                                //判断是否有匹配的搜索内容
                                UiScrollable roll = new UiScrollable(new UiSelector().className("android.widget.ListView").index(1));
                                if (!roll.waitForExists(5000) || roll.getChildCount() == 0) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Search.GetDes(), "608", "未搜索到符合条件的", messageList.get(j).ReceiveInID, 0, Vps));
                                        continue;
                                }
                                ob = new UiObject(new UiSelector().text("in号:" + messageList.get(j).ReceiveInID));
                                if (!ob.exists()) {
                                        logList.add(inSocialHelper.getLogInfo(type, "609", "未搜索到用户", messageList.get(j).ReceiveInID, 0, Vps));
                                        continue;
                                }
                                ob.clickAndWaitForNewWindow();//选中搜索到的人

                                ob = new UiObject(new UiSelector().className("android.widget.ImageView").index(4).clickable(true));
                                if (!ob.exists()) {
                                        logList.add(inSocialHelper.getLogInfo(type, "610", "没有找到聊天按钮", messageList.get(j).ReceiveInID, 0, Vps));
                                        Device.pressBack();
                                        continue;
                                }
                                ob.clickAndWaitForNewWindow();
                                //还未加为好友，则发送聊天请求
                                if (new UiObject(new UiSelector().className("android.widget.TextView").text("发起聊天请求")).exists()) {
                                        edit=new UiEdit(new UiSelector().className("android.widget.EditText").text("留言~"));
                                        if(edit.exists()){
                                                edit.setChineseText(messageList.get(j).Text);
                                                ob = new UiObject(new UiSelector().className("android.widget.TextView").text("发送").index(1));
                                                if (ob.exists()) {
                                                        ob.click();//点击发送
                                                        chatList.add(new InSocialChatModel(messageList.get(j).ID, request.ID, request.Name, request.InID, messageList.get(j).ReceiveName, messageList.get(j).ReceiveInID, messageList.get(j).Text, (byte) 4, (byte) 1, ""));
                                                }
                                        }
                                        Device.pressBack();
                                        Device.pressBack();
                                        continue;
                                }
                                if (new UiObject(new UiSelector().className("android.widget.TextView").text("你今天与未相互关注的人的10次聊天机会已经用完了，明天再聊吧~")).exists()) {
                                        logList.add(inSocialHelper.getLogInfo(OperationType.Friend.GetDes(), "615", "达到每日聊天次数限制", messageList.get(j).ID+"", 0, Vps));
                                        Device.pressBack();
                                        Device.pressBack();
                                        continue;
                                }
                                if (!new UiObject(new UiSelector().className("android.widget.TextView").text(messageList.get(j).ReceiveName).index(1)).exists()) {
                                        logList.add(inSocialHelper.getLogInfo(type, "611", "没有进入好友聊天界面", messageList.get(j).ReceiveInID, 0, Vps));
                                        Device.pressBack();
                                        Device.pressBack();
                                        continue;
                                }
                                //发送信息
                                edit = new UiEdit(new UiSelector().className("android.widget.EditText").clickable(true).index(0));
                                if (edit.exists()) {
                                        if (!messageList.get(j).Text.equals("")) {
                                                edit.setChineseText(messageList.get(j).Text);
                                                ob = new UiObject(new UiSelector().className("android.widget.Button").text("发送").index(2));
                                                if (ob.exists()) {
                                                        ob.click();//点击发送
                                                }
                                                chatList.add(new InSocialChatModel(messageList.get(j).ID, request.ID, request.Name, request.InID, messageList.get(j).ReceiveName, messageList.get(j).ReceiveInID, messageList.get(j).Text, (byte) 4, (byte) 1, ""));
                                        }
                                        String pic = messageList.get(j).Picture;
                                        if (!pic.equals("")) {
                                                ob = new UiObject(new UiSelector().className("android.widget.Button").index(2));
                                                if (ob.exists()) {
                                                        ob.click();
                                                }
                                                HttpHelper.downFile("http://222.185.251.62:34567/files/download?ID=" + pic, "/sdcard/DCIM/Camera/tempscr.png");
                                                if (ScriptHelper.fileIsExists("/sdcard/DCIM/Camera/tempscr.png")) {
                                                        ob = new UiObject(new UiSelector().className("android.widget.TextView").text("图片")).getFromParent(new UiSelector().className("android.widget.ImageView"));
                                                        if (ob.exists()) {
                                                                ob.clickAndWaitForNewWindow();
                                                                Device.click(360, 660);
                                                                sleep(1000);
                                                                Device.click(360, 660);
                                                                sleep(1000);
                                                                chatList.add(new InSocialChatModel(messageList.get(j).ID, request.ID, request.Name, request.InID, messageList.get(j).ReceiveName, messageList.get(j).ReceiveInID, "", (byte) 4, (byte) 1, pic));
                                                                Device.pressBack();
                                                        }
                                                }
                                        }
                                }
                                Device.pressBack();
                                Device.pressBack();//返回好友搜索界面
                        }
                } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                        logList.add(inSocialHelper.getLogInfo(type, "613", "执行聊天过程出错", request.InID, 0, Vps));
                } finally {
                        submitModel.MessageInfo.addAll(chatList);
                }
                logList.add(inSocialHelper.getLogInfo(type, "614", "完成发送信息任务", request.InID, 1, Vps));
                return true;
        }
        //endregion

        //region 【通用】
        //是否包含在列表中
        private boolean ExistsInList(List<InSocialCommentModel> cList, String Sender, String Receive, String Text) {
                for (InSocialCommentModel model : cList) {
                        if (model.SendName.equals(Sender) && model.ReceiveName.equals(Receive) && model.Text.equals(Text)) {
                                return true;
                        }
                }
                return false;
        }

        private enum OperationType {
                Login("登录"),
                Picture("发布图片"),
                Comment("评论"),
                Message("消息"),
                System("系统消息"),
                Clone("克隆"),
                Follow("关注"),
                Friend("好友"),
                Search("搜索"),
                Others("其他异常");

                private String des;

                OperationType(String string) {
                        des = string;
                }

                public String GetDes() {
                        return des;
                }
        }
        //endregion
}