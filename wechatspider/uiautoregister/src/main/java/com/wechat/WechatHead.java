package com.wechat;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeqee on 2015/11/27.
 */
public class WechatHead extends SuperRunner {
    // private String getWXNamesURL = "http://222.185.195.206:8007/api/wx/getwxnames";
    // http://192.168.1.177:8007/api/wx/getwxnames

    private String getWXNamesURL = "http://222.185.251.62:8007/api/wx/getwxnamesv2?w=";
    private String updateWXURL = "http://222.185.251.62:8007/api/wx/update";
    //private String updateImageURL = "http://www.jqsocial.com:34567/Files/UpLoadImageForWeixin";
    //private String updateImageURL = "http://192.168.1.176:34567/Files/UpLoadImageForWeixin";
    private String updateImageURL = "http://v2.jqsocial.com/Files/UpLoadImageForWeixin";
    private String updateImageURL2 = "http://v2.jqsocial.com/Files/UpLoadImageForWeixin2";
    private String addLogURL = "http://222.185.251.62:8007/api/wx/addlog";
    //private String updateImageURL = "http://192.168.1.168:22230/Files/UpLoadImageForWeixin";
    //private String apiURL = "http://222.185.251.62:7000/api";
    private Map<String, WechatData> load = new HashMap<String, WechatData>();
    private String batchNumber = "";
    private Date lastUpdate = null;
    private int MaxCount = 20;
    String WeiXinName = "";
    private String path = "/sdcard/Tencent/MicroMsg/WeiXin";

    public void testRunner() throws UiObjectNotFoundException, RemoteException {
        try {
            Initialization();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常 Initialization()：" + e.toString() + " \n", "/sdcard/error" +
                    ".txt");
        }

        while (true) {
            try {
                ScriptHelper.reportNormal();

                //startApp();

                process();

                sleep(5000);

            } catch (Exception e) {
                jqhelper.writeSDFileEx("异常 testRunner()：" + e.toString() + " \n", "/sdcard/error" +
                        ".txt");
            }
        }
    }

    // 注册
    public String process() {
        try {
            sleep(1000);
            infoF("点击 0\n");
            sleep(1000);
            for (; ; ) {
                sleep(5000);
                List<WeiXinNameV2> wx_names = GetWXNames();
                //Date t = GetTime(wx_names, "");
                batchNumber = UUID.randomUUID().toString();

                for (int i = 0; i < wx_names.size(); i++) {
                    /*  String str = "桐桐 王杉杉 小晶定制 echo-1";
                    if (str.contains(wx_names.get(i).getName())) {
                        System.out.println("  aaaa");
                    }*/
                    //重启app
                    restartApp1();
                    sleep(2000);
                    String name = wx_names.get(i).getName();
                    //进入通讯录

                    enterCallList();
                    AddLog(batchNumber, "消息", "进入" + name + "的通讯录成功");
                    //找到并进入好友信息
                    //
                    boolean b = enterFriendsList(name);
                    if (!b) {
                        continue;
                    }
                    AddLog(batchNumber, "消息", "进入" + name + "的个人信息成功");
                    sleep(2000);
                    //找到并进入好友发的图片
                    int code = enterFreindsPhotos(name);
                    if (code == -1) {
                        AddLog(batchNumber, "进入图片", "用户：" + name + "未找到可进入的图片");
                        continue;
                    }
                    AddLog(batchNumber, "消息", "进入" + name + "的图片成功");
                    sleep(5000);
                    //循环保存遍历图片并提交
                    int code2 = saveImageAndPost(wx_names.get(i));
                    if (code2 == -1) {
                        i = i - 1;
                        continue;
                    }
                    AddLog(batchNumber, "消息", "上传" + name + "的图片与个人信息成功");

                }


                // 记录当前时间
                lastUpdate = new Date();
                sleep(3000);
            }
            //}

            //sleep(5000);
            //infoF("点击 1\n");

            //break;
            //}
        } catch (Exception e) {
            AddLog(batchNumber, "抛错", e.getMessage());
            return "run 抛错：" + e.getMessage();
        }
        //return "";
    }

    //endregion
    private void Initialization() throws UiObjectNotFoundException, RemoteException, IOException {
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Vps = ScriptHelper.readSDFile("/sdcard/vps.txt");
        WeiXinName = ScriptHelper.readSDFile("/sdcard/name.txt");
        getWXNamesURL += WeiXinName;
        Device = getUiDevice();
        File file = new File(path);
        boolean b = file.exists();
        HttpHelper.DeleteFile(file);
        //Date d = ParseXYD("1月2日 19:00");
        //jqhelper.initOCR();
        //ScriptHelper.switchImeToAdbKeyboard();
        //cleanProcess();
    }

    //清理
    private void cleanProcess() {
        ScriptHelper.killApp(PackegName);   //关闭
        sleep(800);
        ScriptHelper.clearPackage(PackegName);//清理
        sleep(800);
    }

    //启动
    private void startApp() {
        try {
            cleanProcess();
            jqhelper.reConnNet();
            sleep(5000);
            Device.wakeUp();
            sleep(800);
            //Device.pressHome();
            //sleep(3000);
            //Device.click(620, 146);
            ScriptHelper.startActivity(PackegName + "/" + ActivityName);
            sleep(15000);
            ScriptHelper.startActivity(PackegName + "/" + ActivityName);
            sleep(10000);
            ScriptHelper.reportNormal();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常 startApp()：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    //region Login

    // 判断首页
    private boolean waitCoverView() {
        UiObject loginButton = new UiObject(new UiSelector().className("android.widget.Button")
                .text("登录"));
        UiObject regButton = new UiObject(new UiSelector().className("android.widget.Button")
                .text("注册"));
        return loginButton.exists() && regButton.exists();
    }

    // 判断手机登录页
    private boolean waitPhoneLoginView() {
        UiObject loginButton = new UiObject(new UiSelector().className("android.widget.Button")
                .text("登录").index(3));
        UiObject smsLoginButton = new UiObject(new UiSelector().className("android.widget" +
                ".Button").text("用短信验证码登录").index(4));
        return loginButton.exists() && smsLoginButton.exists();
    }

    // 判断微信主页面
    private boolean waitMainView() {
        UiObject myButton = new UiObject(new UiSelector().className(android.widget.TextView.class
                .getName()).text("我").index(1));
        return myButton.waitForExists(15000);
    }

    // 确认通讯录提醒
    private boolean doContactMsgClick() {
        try {
            UiObject contactText = new UiObject(new UiSelector().className(android.widget
                    .TextView.class.getName()).text("看看手机通讯录里谁在使用微信？（不保存通讯录的任何资料，仅使用特征码作匹配识别）"));
            if (contactText.waitForExists(5000)) {
                //
                //UiObject yesButton = new UiObject(new UiSelector().className(android.widget
                // .Button.class.getName()).text("是").index(1));
                UiObject noButton = new UiObject(new UiSelector().className(android.widget.Button
                        .class.getName()).text("否").index(0));
                noButton.clickAndWaitForNewWindow();

                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 doLogin()：" + ex.toString() + " \n", "/sdcard/error.txt");
            return false;
        }
    }

    // Login click
    private void doLoginClick() {
        try {
            UiObject loginButton = new UiObject(new UiSelector().className("android.widget" +
                    ".Button").text("登录"));
            loginButton.clickAndWaitForNewWindow();
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 doLoginClick()：" + ex.toString() + " \n", "/sdcard/error" +
                    ".txt");
        }
    }

    // 手机号登录
    private void doLogin() {
        try {
            UiObject phoneEdit = new UiObject(new UiSelector().className("android.widget" +
                    ".EditText").text("你的手机号码").index(1));

            UiObject resultLayout = new UiObject(new UiSelector().className(android.widget
                    .RelativeLayout.class.getName()).index(0));
            UiObject fantile = resultLayout.getChild(new UiSelector().className("android.widget" +
                    ".LinearLayout").index(0));
            UiObject pwdLayout = fantile.getChild(new UiSelector().className("android.widget" +
                    ".LinearLayout").index(2));
            UiObject pwdEdit = pwdLayout.getChild(new UiSelector().className("android.widget" +
                    ".EditText").index(1));

            UiObject loginButton = new UiObject(new UiSelector().className(android.widget.Button
                    .class.getName()).text("登录").index(3));

            SetInputText(phoneEdit, "18068510605");
            //SetInputText(pwdEdit, "c098756");

            pwdEdit.setText("c098756");

            loginButton.clickAndWaitForNewWindow();
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 doLogin()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 为控件设值
    private void SetInputText(UiObject c, String text) {
        try {
            //String[] need = text.split("");
            //for (int i = 0; i < need.length; i++) {
            //    c.setText(need[i]);
            //    sleep(800);
            //}
            //c.click();
            //sleep(800);

            //for(int i=0;i<text.length();i++) {
            //    ScriptHelper.input(text.substring(i, i + 1));
            //    sleep(200);
            //}
            for (; ; ) {
                c.setText(text);
                String s = c.getText();
                if (s == text) {
                    break;
                }
                sleep(1000);
            }
        } catch (Exception ex) {

        }
    }

    // QQ号、微信号、Email登录
    private void doLogin2() {
        String uin = "2201441955";
        String password = "c098756";

        try {
            UiObject otherLoginButton = new UiObject(new UiSelector().className(android.widget
                    .Button.class.getName()).text("使用其他方式登录").index(2));
            otherLoginButton.clickAndWaitForNewWindow();

            UiObject usernameEdit = new UiObject(new UiSelector().className(android.widget
                    .EditText.class.getName()).text("QQ号/微信号/Email").index(1));
            UiObject pwdText = new UiObject(new UiSelector().className(android.widget.TextView
                    .class.getName()).text("密   码").index(0));
            UiObject passwordEdit = pwdText.getFromParent(new UiSelector().className(android
                    .widget.EditText.class.getName()).index(1));
            UiObject loginButton = new UiObject(new UiSelector().className(android.widget.Button
                    .class.getName()).text("登录").index(2));

            if (!usernameEdit.exists() || !passwordEdit.exists() || !loginButton.exists()) {
                return;
            }

            usernameEdit.setText(uin);
            passwordEdit.setText(password);

            loginButton.clickAndWaitForNewWindow();

            // 判断验证码
            /*UiObject changeCaptchaButton = new UiObject(new UiSelector().className(android
            .widget.Button.class.getName()).text("换一张图片"));
            UiObject captchaImage = changeCaptchaButton.getFromParent(new UiSelector().className
            (android.widget.ImageView.class.getName()));

            if (!changeCaptchaButton.exists() || !captchaImage.exists()) {
                return;
            }

            // 答题
            CaptchaResult cr = null;
            Rect r = captchaImage.getBounds();
            Bitmap screen = ScriptHelper.screenShot();
            int x1, y1, x2, y2 = 0;
            x1 = r.left;
            y1 = r.top;
            x2 = r.left + r.width();
            y2 = r.top + r.height();
            infoF("Captch X1=%d Y1=%d X2=%d Y2=%d\n", x1, y1, x2, y2);
            Bitmap captcha = ScriptHelper.cutImage(screen, x1, y1, x2, y2);
            ScriptHelper.savePngToSdCard(captcha, "/sdcard/wc_captcha.png");
            cr = ScriptHelper.getVerifyCode(captcha, "3000", "41206",
            "0c6c7d3efba743b68037d9f87d70c6f4");

            if (cr == null || cr.getCode() == null || cr.getCode().length() < 3) {
                try {
                    if (cr != null) {
                        ScriptHelper.reportError(cr);
                    }
                }
                catch (Exception ex) {
                    infoF("异常 doEnterCaptcha()：" + ex.toString() + " \n");
                }
                return;
            }*/
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 doLogin2()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }


    private void enterCallList() {
        try {
            UiObject findText = new UiObject(new UiSelector().className(android.widget.TextView
                    .class.getName()).text("通讯录").index(1));
            if (findText.exists()) {
                findText.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "进入通讯录异常", ex.toString());
        }
    }

    // 进入个人信息
    private boolean enterFriendsList(String name) {
        try {
            UiScrollable FriendsList = new UiScrollable(new UiSelector().className("android" +
                    ".widget.ListView"));
            boolean b = FriendsList.scrollIntoView(new UiSelector().className("android.view" +
                    ".View").text(name));
            if (!b) {
                AddLog(batchNumber, "进入个人信息错误", "未找到好友：" + name);
                return false;
            }
            new UiObject(new UiSelector().className("android.view.View").text(name))
                    .clickAndWaitForNewWindow();
            return true;
        } catch (Exception ex) {
            //jqhelper.writeSDFileEx("异常 enterFriends()：" + ex.toString() + " \n", "/sdcard/error
            // .txt");
            AddLog(batchNumber, "用户：" + name + "进入个人信息异常", ex.toString());
            return false;
        }
    }

    //进入个人相册并找到一张可以进入的图片
    private int enterFreindsPhotos(String name) {
        try {
            UiObject OwnPhotos = new UiObject(new UiSelector().className("android.widget" +
                    ".TextView").text("个人相册"));
            if (OwnPhotos.exists()) {
                OwnPhotos.clickAndWaitForNewWindow();
            }
            /*UiScrollable ImageScroll = new UiScrollable(new UiSelector().className("android" +
                    ".widget.ListView").index(0));*/
            UiObject FirstImage = new UiObject(new UiSelector().className("android.view.View")
                    .description("图片"));
            while (!FirstImage.exists()) {
                ScriptHelper.swipe(0, 800, 0, 200);
                sleep(1000);
                UiObject ob = new UiObject(new UiSelector().className("android.widget.ListView"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.LinearLayout"));
                ob = ob.getChild(new UiSelector().className("android.widget.ImageView"));
                if (ob.exists()) {
                    return -1;
                }
            }
            FirstImage.clickAndWaitForNewWindow();
            return 0;
        } catch (Exception ex) {
            //jqhelper.writeSDFileEx("异常 enterFriends()：" + ex.toString() + " \n", "/sdcard/error
            // .txt");
            AddLog(batchNumber, "进入个人相册异常", ex.toString());
            return -1;
        }
    }

    //保存并发送图片
    private int saveImageAndPost(WeiXinNameV2 WxName) {
        try {
            int maxCount = 0;
            while (true) {
                Map<String, String> map = new HashMap<String, String>();
                String text = "";
                Date date = new Date();
                UiScrollable ImageScroll = new UiScrollable(new UiSelector().className("android" +
                        ".widget.Gallery"));
                ImageScroll.setAsHorizontalList();
                UiObject info = new UiObject(new UiSelector().className("android.widget" +
                        ".TextView").description("更多"));
                //图片未加载重试处理
                for (int i = 0; i < 3; i++) {
                    if (info.exists()) {
                        //System.out.println("1");
                        //AddLog(batchNumber, "执行步骤", "01");
                        break;
                    }
                    new UiObject(new UiSelector().className("android.widget.Gallery")).getChild
                            (new UiSelector().className("android.widget.ImageView")).click();
                    if (info.exists()) {
                        //AddLog(batchNumber, "执行步骤", "01");
                        break;
                    }
                    sleep(5000);
                }
                UiObject ob = new UiObject(new UiSelector().className("android.widget" +
                        ".FrameLayout")).getChild(new UiSelector().className("android.widget" +
                        ".LinearLayout")).getChild(new UiSelector().className("android.widget" +
                        ".LinearLayout")).getChild(new UiSelector().className("android.widget" +
                        ".TextView"));
                text = ob.getText();
                if (text != null) {
                    //AddLog(batchNumber, "执行步骤", "02");
                    text = text.replaceAll("\\n", " ").replaceAll("\\r", " ").replaceAll("\\\\",
                            "").replaceAll("\\t", " ");
                    UiObject ob1 = new UiObject(new UiSelector().className("android.view" +
                            ".View")).getChild(new UiSelector().className("android.widget" +
                            ".LinearLayout").index(0)).getChild(new UiSelector().className
                            ("android.widget.LinearLayout").index(1));
                    UiObject timeob = ob1.getChild(new UiSelector().className("android.widget" +
                            ".TextView").index(0));
                    date = ParseXYD(timeob.getText());
                    //AddLog(batchNumber, "执行步骤", "02-1");
                    for (int i = WxName.getUsers().size() - 1; i >= 0; i--) {
                        Date maxDate = ParseTime(WxName.getUsers().get(i).getTime());
                        //AddLog(batchNumber, "执行步骤", "02-2");
                        if (date == null) {

                            AddLog(batchNumber, "执行错误", timeob.getText() + "   " + "date为空");
                        }
                        if (maxDate == null) {
                            AddLog(batchNumber, "执行错误", "maxDate为空");
                        }
                        if (!date.after(maxDate) || maxCount > WxName.getUsers().get(i)
                                .getMaxPickCount()) {
                            //AddLog(batchNumber, "执行步骤", "02-2-a");
                            AddLog(batchNumber, "执行完成", "用户：" + WxName.getName() + ",超过最大时间：" +
                                    maxDate
                                            .toString() + "或数量：" + maxCount + ",平台id：" + WxName
                                    .getUsers
                                            ().get(i).getUserID());
                            //AddLog(batchNumber, "执行步骤", "02-2-b");
                            WxName.getUsers().remove(i);
                            //AddLog(batchNumber, "执行步骤", "02-3");
                        }
                        if (WxName.getUsers().size() == 0) {
                            return 0;
                        }
                    }
                    UiObject moreob = ob1.getChild(new UiSelector().className("android.widget" +
                            ".TextView").index(1));
                    //AddLog(batchNumber, "执行步骤", "02-4");
                    int c = 1;
                    if (moreob.exists()) {
                        String count = moreob.getText();
                        //System.out.println(count);
                        c = Integer.parseInt(count.substring(count.lastIndexOf(" ") + 1));
                        //AddLog(batchNumber, "执行步骤", "03");
                    }
                    new UiObject(new UiSelector().className("android.widget.TextView")
                            .description("更多")).click();
                    //AddLog(batchNumber, "执行步骤", "04");
                    sleep(1000);
                    UiObject uo = new UiObject(new UiSelector().className("android.widget" +
                            ".TextView").text
                            ("保存到手机"));
                    if (!uo.exists()) {
                        Device.pressBack();
                        boolean b = ImageScroll.scrollForward();
                        if (!b) {
                            AddLog(batchNumber, "执行完成", "用户：" + WxName.getName() + "执行完成，" +
                                    "完成数量：" + maxCount);
                            return 0;
                        }
                        continue;
                    } else {
                        uo.click();
                        //AddLog(batchNumber, "执行步骤", "05");
                    }
                    String filename = HttpHelper.GetFileName(new File(path));
                    //AddLog(batchNumber, "执行步骤", "06");
                    for (int i = 0; i < WxName.getUsers().size(); i++) {
                        String id = UploadImage2(path + "/" + filename, WxName.getUsers().get
                                (i).getUserID());
                        if (id.equals("") || id == null) {
                            continue;
                        }
                        map.put(WxName.getUsers().get(i).getUserID(), id);
                    }
                    //maxCount++;
                    HttpHelper.DeleteFile(new File(path));
                    //AddLog(batchNumber, "执行步骤", "07");
                    for (int i = 0; i < c - 1; i++) {
                        ImageScroll.scrollForward();
                        sleep(100);
                        for (int z = 0; z < 3; z++) {
                            if (info.exists()) {
                                break;
                            }
                            new UiObject(new UiSelector().className("android.widget.Gallery"))
                                    .getChild
                                            (new UiSelector().className("android.widget.ImageView"))
                                    .click();
                            //AddLog(batchNumber, "执行步骤", "08");
                            if (info.exists()) {
                                break;
                            }
                            sleep(5000);
                        }
                        new UiObject(new UiSelector().className("android.widget.TextView")
                                .description("更多")).click();
                        sleep(1000);
                        if (!uo.exists()) {
                            Device.pressBack();
                            boolean b = ImageScroll.scrollForward();
                            if (!b) {
                                AddLog(batchNumber, "执行完成", "用户：" + WxName.getName() + "执行完成，" +
                                        "完成数量：" + maxCount);
                                return 0;
                            }
                            continue;
                        } else {
                            uo.click();
                        }
                        //AddLog(batchNumber, "执行步骤", "09");
                        String filename2 = HttpHelper.GetFileName(new File(path));
                        for (int k = 0; k < WxName.getUsers().size(); k++) {
                            String id = UploadImage2(path + "/" + filename2, WxName.getUsers().get
                                    (k).getUserID());
                            if (id.equals("") || id == null) {
                                continue;
                            }
                            map.put(WxName.getUsers().get(k).getUserID(), map.get(WxName.getUsers
                                    ().get
                                    (k).getUserID()) + "," + id);
                        }
                        //maxCount++;
                        HttpHelper.DeleteFile(new File(path));
                        // AddLog(batchNumber, "执行步骤", "10");
                    }
                }
                //提交数据
                for (int i = 0; i < WxName.getUsers().size(); i++) {
                    //list.add(new WechatData(wdi2.getName(),text,new SimpleDateFormat
                    // ("yyyy-MM-dd hh:mm:ss").format(date), map.get(wdi2.getUserIDs().get(i))));
                    UpdateTo(WxName.getName(), WxName.getUsers().get(i).getUserID(), new
                            SimpleDateFormat
                            ("yyyy-MM-dd HH:mm:ss").format(date), text, map.get(WxName.getUsers()
                            .get(i).getUserID()));
                }
                maxCount++;

                boolean b = ImageScroll.scrollForward();
                if (!b) {
                    AddLog(batchNumber, "执行完成", "用户：" + WxName.getName() + "执行完成，" + "完成数量：" +
                            maxCount);
                    return 0;
                }
                sleep(100);
            }
        } catch (Exception ex) {
            //jqhelper.writeSDFileEx("异常 enterFriends()：" + ex.toString() + " \n", "/sdcard/error
            // .txt");
            AddLog(batchNumber, "用户：" + WxName.getName() + "保存个人信息异常", ex.toString());
            return -1;
        }
    }

    // 检查是否需要抓取
    private boolean checkWx(List<WxDataItem> wx_names, String name) {
        for (int i = 0; i < wx_names.size(); i++) {
            WxDataItem a = wx_names.get(i);
            if (a.getName().equals(name))
                return true;
        }
        return false;
    }

    // 获取时间
    private Date GetTime(List<WxDataItem> wx_names, String name) {
        // 总的时间
        if (wx_names == null || wx_names.size() == 0) {
            return null;
        }

        Date t = null;

        for (int i = 0; i < wx_names.size(); i++) {
            WxDataItem w = wx_names.get(i);

            if (name.equals("")) {
                if (w.getTime().equals(""))
                    return null;
                Date x = ParseTime(w.getTime());
                if (x != null && (t == null || x.compareTo(t) < 0)) {
                    t = x;
                }
            }

            // 11/26/2015 12:08:23
            if (w.getName().equals(name)) {
                Date x = ParseTime(w.getTime());
                if (x == null) {
                    return null;
                }
                if (t != null && (t == null || x.compareTo(t) < 0)) {
                    t = x;
                }
            }
            ///if (w.getName())
        }

        return t;
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


    /*
    记录信息
     */
    private void infoF(String fmt, Object... args) {
        String text = String.format(fmt, args);
        ScriptHelper.appendToSDFile(text, "/sdcard/error.txt");
    }

    /*
    获取需要更新的微信号
     */
    private List<WeiXinNameV2> GetWXNames() {
        //http://192.168.1.177:8007/api/wx/getwxnames
        try {
            String data = HttpHelper.httpGetString(getWXNamesURL);
            Type type = new TypeToken<WxData>() {
            }.getType();
            WxData wx = gson.fromJson(data, type);

            List<WeiXinNameV2> list = new ArrayList<WeiXinNameV2>();

            JSONArray results = new JSONArray(wx.Text);

            for (int i = 0; i < results.length(); i++) {
                WeiXinNameV2 a = new WeiXinNameV2();
                List<WeiXinUserInfo> infos = new ArrayList<WeiXinUserInfo>();
                JSONObject result = results.getJSONObject(i);
                a.setName(result.getString("name"));
                String Users = result.getString("users");
                JSONArray usersResult = new JSONArray(Users);
                for (int k = 0; k < usersResult.length(); k++) {
                    WeiXinUserInfo info = new WeiXinUserInfo();
                    JSONObject usersR = usersResult.getJSONObject(k);
                    info.setTime(usersR.getString("time"));
                    //info.setTime("11/30/2015 15:30:58");
                    info.setMaxPickCount(Integer.parseInt(usersR.getString("maxPickCount")));
                    info.setUserID(usersR.getString("userID"));
                    infos.add(info);
                }
                a.setUsers(infos);
                //a.setUsers(result.getString("userID"));
                list.add(a);
            }

            return list;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 字符串转换为16进制数组
     *
     * @param ss
     * @return
     */
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    /**
     * 16进制数组转换为字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    public static String toHexString2(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString2(b[i]));
            //buffer.append(" ");
        }
        return buffer.toString();
    }

    public static String toHexString2(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    static public String join(List<String> list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list) {
            if (first)
                first = false;
            else
                sb.append(conjunction);
            sb.append(item);
        }
        return sb.toString();
    }

    /*public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }*/


    // 上传图片
    private String UploadImage(String filename, String userId) {
        String resp = "";
        try {
            byte[] bytes = ScriptHelper.readSDFileBytes(filename);
            //String b64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            String hex_string = toHexString2(bytes);
            String postData = String.format("base64=%s&userId=%s",
                    hex_string,
                    userId);
            resp = EntityUtils.toString(HttpHelper.httpPost(updateImageURL, postData,
                    "application/x-www-form-urlencoded"), "utf8");

            JSONObject o = new JSONObject(resp);
            Integer nType = o.getInt("type");
            if (nType == 2) {
                Integer picId = o.getInt("message");
                return picId.toString();
            } else {
                // log
                // String batchNumber, String type, ""
                AddLog(batchNumber, "上传图片错误", resp);
                jqhelper.writeSDFileEx("异常 UploadImage()：" + resp + " \n", "/sdcard/error.txt");
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传图片错误[Android]", ex.toString());
            jqhelper.writeSDFileEx("异常 UploadImage()：" + ex.toString() + " \n", "/sdcard/error" +
                    ".txt");
        }
        return "";
    }

    // 上传图片2
    private String UploadImage2(String filename, String userId) {
        String resp = "";
        try {
            //byte[] bytes = ScriptHelper.readSDFileBytes(filename);
            //String b64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            //String hex_string = toHexString2(bytes);
            //String postData = String.format("base64=%s&userId=%s",
            //        hex_string,
            //        userId);
            resp = HttpHelper.httpPostMultipart(updateImageURL2, filename, userId);

            JSONObject o = new JSONObject(resp);
            Integer nType = o.getInt("type");
            if (nType == 2) {
                Integer picId = o.getInt("message");
                return picId.toString();
            } else {
                // log
                // String batchNumber, String type, ""
                AddLog(batchNumber, "上传图片错误", resp);
                jqhelper.writeSDFileEx("异常 UploadImage()：" + resp + " \n", "/sdcard/error.txt");
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传图片错误[Android]", ex.toString());
            jqhelper.writeSDFileEx("异常 UploadImage()：" + ex.toString() + " \n", "/sdcard/error" +
                    ".txt");
        }
        return "";
    }

    // 获取Userid列表
    private List<String> GetWxUserIds(List<WxDataItem> wx_names, String name) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < wx_names.size(); i++) {
            WxDataItem item = wx_names.get(i);
            if (item.getName().equals(name)) {
                list.add(item.getUserID());
            }
        }
        return list;
    }

    // 上传
    private void upload(List<WechatData> list, List<WxDataItem> wx_names) {
        try {
            //UploadImage
            if (list == null || list.size() == 0) {
                return;
            }

            for (int i = 0; i < list.size(); i++) {
                WechatData d = list.get(i);

                List<String> userIdList = GetWxUserIds(wx_names, d.getName());

                for (int j = 0; j < userIdList.size(); j++) {
                    String userId = userIdList.get(j);

                    // 上传图片
                    List<String> imageList = d.getImage_list();
                    if (imageList == null || imageList.size() == 0) {
                        continue;
                    }

                    List<String> picList = new ArrayList<String>();
                    for (int k = 0; k < imageList.size(); k++) {
                        String filename = imageList.get(k);
                        String picId = UploadImage(filename, userId);
                        picList.add(picId);
                    }
                    String pics = join(picList, ",");
                    d.setPics(pics);

                    // 提交
                    UpdateTo(d.getName(), userId, d.getTime(), d.getContent(), d.getPics());
                }

                // 保存
                String key = d.getName() + "|" + d.getContent();
                if (!load.containsKey(key)) {
                    load.put(key, d);
                }
            }
        } catch (Exception ex) {
            AddLog(batchNumber, "上传异常", ex.toString());
            jqhelper.writeSDFileEx("异常 upload()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

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
            String mm = s.substring(0, s.indexOf("月"));
            String dd = s.substring(s.indexOf("月") + 1, s.indexOf("日"));
            String hh = s.substring(s.indexOf(" ") + 1, s.indexOf(":"));
            String ss = s.substring(s.indexOf(":") + 1);

            // 11/26/2015 12:08:23
            String str = String.format("%s/%s/%s %s:%s:00",
                    mm,
                    dd,
                    "2015",
                    hh,
                    ss
            );
            Date d = ParseTime(str);
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    // 删除临时文件
    private void deleteTempFiles(List<WechatData> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            WechatData w = list.get(i);
            List<String> files = w.getImage_list();

            if (files == null) continue;

            for (int j = 0; j < files.size(); j++) {
                String f = files.get(j);

                if (f.equals("")) continue;

                // delete file
                // /sdcard/YourCustomDirectory/ExampleFile.mp3
                try {
                    File file = new File(f);
                    boolean deleted = file.delete();

                    String f1 = f.replaceAll("sdcard", "手机");
                    File file1 = new File(f1);
                    file1.delete();
                } catch (Exception ex) {
                    AddLog(batchNumber, "删除文件出错", ex.toString());
                    jqhelper.writeSDFileEx("异常 deleteTempFiles()：" + ex.toString() + " \n",
                            "/sdcard/error.txt");
                }
            }
        }
    }

    // 记录日志
    public void AddLog(String batchNumber, String type, String remark) {
        try {
            // string batchNumber, string type, string remark, string moblie
            String postData = String.format("{\"batchNumber\":\"%s\",\"type\":\"%s\"," +
                            "\"remark\":\"%s\",\"moblie\":\"%s\"}",
                    batchNumber,
                    type,
                    remark,
                    Vps);
            String resp = HttpHelper.httpPostForString(addLogURL, postData);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 AddLog()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    public void restartApp1() {
        try {
            // 唤醒屏幕
            Device.wakeUp();
            ScriptHelper.killApp(PackegName);
            sleep(1000);
            Runtime.getRuntime().exec(
                    "am start -n com.tencent.mm/.ui.LauncherUI");
            sleep(5000);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常startApp()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    // 提交
    public void UpdateTo(String name, String userID, String timeString, String content, String
            pics) {
        try {
            String postData = String.format("{\"wxname\":\"%s\",\"uid\":\"%s\"," +
                            "\"time_string\":\"%s\",\"content\":\"%s\",\"pics\":\"%s\"}",
                    name,
                    userID,
                    timeString,
                    content,
                    pics);
            String resp = HttpHelper.httpPostForString(updateWXURL, postData);
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 UpdateTo()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 比较时间
    public Date ParseWxDate(String str) {
        Date d = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        if (str.contains("昨天")) {
            calendar.add(calendar.DATE, -1);
            d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        } else if (str.contains("天前")) {
            String timeString = str.replaceAll("天前", "");
            Integer t = 0;
            try {
                t = Integer.parseInt(timeString);
            } catch (Exception ex) {

            }
            calendar.add(calendar.DATE, -t);
            d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        } else if (str.contains("小时前")) {
            String timeString = str.replaceAll("小时前", "");
            Integer t = 0;
            try {
                t = Integer.parseInt(timeString);
            } catch (Exception ex) {

            }
            calendar.add(calendar.HOUR, -t);
            d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        } else if (str.contains("分钟前")) {
            String timeString = str.replaceAll("分钟前", "");
            Integer t = 0;
            try {
                t = Integer.parseInt(timeString);
            } catch (Exception ex) {

            }
            calendar.add(calendar.MINUTE, -t);
            d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        }
        return d;
    }

    /// 时间加多少分钟
    public Date addMinutes(Date d, int minutes) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.MINUTE, minutes);
        d = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return d;
    }
}
