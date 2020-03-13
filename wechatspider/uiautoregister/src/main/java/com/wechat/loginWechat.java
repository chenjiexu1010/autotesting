package com.wechat;

import android.graphics.Bitmap;
import android.graphics.Rect;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class loginWechat extends SuperRunner {
    // private String getWXNamesURL = "http://222.185.195.206:8007/api/wx/getwxnames"; //http://192.168.1.177:8007/api/wx/getwxnames
    private String getWXNamesURL = "http://222.185.251.62:8007/api/wx/getwxnameswithtime";
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

    public void testRunner() throws UiObjectNotFoundException, RemoteException {
        try {
            Initialization();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常 Initialization()：" + e.toString() + " \n", "/sdcard/error.txt");
        }

        while (true) {
            try {
                ScriptHelper.reportNormal();

                //String s = "11月20日 11:23";

                //startApp();
                //String filename = "/sdcard/3a64427a-5b43-486f-b8de-a42942adaf1d.png";
                //UploadImage2(filename, "4");

                //Date d = ParseWxDate("3小时前");
                //Date now = new Date();

                //lastUpdate = new Date();
                //Calendar calendar = new GregorianCalendar();
                //calendar.setTime(lastUpdate);

                //calendar.add(calendar.MINUTE, -20);
                //lastUpdate = calendar.getTime();

                //if (d.compareTo(now) < 0) {
                //    jqhelper.writeSDFileEx("异常 testRunner()：" + "1" + " \n", "/sdcard/error.txt");
                //}

                process();

                sleep(5000);

            } catch (Exception e) {
                jqhelper.writeSDFileEx("异常 testRunner()：" + e.toString() + " \n", "/sdcard/error.txt");
            }
        }
    }

    private Date ParseXYD(String s) {
        //String s = "11月20日 11:23";
        Pattern pattern = Pattern.compile("(\\d{2})月(\\d{2})日\\s+(\\d{2}):(\\d{2})");

        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            String mm = matcher.group(1).toString();
            String dd = matcher.group(2).toString();
            String hh = matcher.group(3).toString();
            String ss = matcher.group(4).toString();

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
        } else {
            return null;
        }
    }

    private void Initialization() throws UiObjectNotFoundException, RemoteException, IOException {
        PackegName = "com.tencent.mm";
        ActivityName = ".ui.LauncherUI";
        Vps = ScriptHelper.readSDFile("/sdcard/vps.txt");

        //jqhelper.initOCR();
        //ScriptHelper.switchImeToAdbKeyboard();
        //cleanProcess();
        Device = getUiDevice();
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
            //Device = getUiDevice();
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
        UiObject loginButton = new UiObject(new UiSelector().className("android.widget.Button").text("登录"));
        UiObject regButton = new UiObject(new UiSelector().className("android.widget.Button").text("注册"));
        return loginButton.exists() && regButton.exists();
    }

    // 判断手机登录页
    private boolean waitPhoneLoginView() {
        UiObject loginButton = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(3));
        UiObject smsLoginButton = new UiObject(new UiSelector().className("android.widget.Button").text("用短信验证码登录").index(4));
        return loginButton.exists() && smsLoginButton.exists();
    }

    // 判断微信主页面
    private boolean waitMainView() {
        UiObject myButton = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("我").index(1));
        return myButton.waitForExists(15000);
    }

    // 确认通讯录提醒
    private boolean doContactMsgClick() {
        try {
            UiObject contactText = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("看看手机通讯录里谁在使用微信？（不保存通讯录的任何资料，仅使用特征码作匹配识别）"));
            if (contactText.waitForExists(5000)) {
                //
                //UiObject yesButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("是").index(1));
                UiObject noButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("否").index(0));
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
            UiObject loginButton = new UiObject(new UiSelector().className("android.widget.Button").text("登录"));
            loginButton.clickAndWaitForNewWindow();
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 doLoginClick()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 手机号登录
    private void doLogin() {
        try {
            UiObject phoneEdit = new UiObject(new UiSelector().className("android.widget.EditText").text("你的手机号码").index(1));

            UiObject resultLayout = new UiObject(new UiSelector().className(android.widget.RelativeLayout.class.getName()).index(0));
            UiObject fantile = resultLayout.getChild(new UiSelector().className("android.widget.LinearLayout").index(0));
            UiObject pwdLayout = fantile.getChild(new UiSelector().className("android.widget.LinearLayout").index(2));
            UiObject pwdEdit = pwdLayout.getChild(new UiSelector().className("android.widget.EditText").index(1));

            UiObject loginButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("登录").index(3));

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
            UiObject otherLoginButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("使用其他方式登录").index(2));
            otherLoginButton.clickAndWaitForNewWindow();

            UiObject usernameEdit = new UiObject(new UiSelector().className(android.widget.EditText.class.getName()).text("QQ号/微信号/Email").index(1));
            UiObject pwdText = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("密   码").index(0));
            UiObject passwordEdit = pwdText.getFromParent(new UiSelector().className(android.widget.EditText.class.getName()).index(1));
            UiObject loginButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("登录").index(2));

            if (!usernameEdit.exists() || !passwordEdit.exists() || !loginButton.exists()) {
                return;
            }

            usernameEdit.setText(uin);
            passwordEdit.setText(password);

            loginButton.clickAndWaitForNewWindow();

            // 判断验证码
            /*UiObject changeCaptchaButton = new UiObject(new UiSelector().className(android.widget.Button.class.getName()).text("换一张图片"));
            UiObject captchaImage = changeCaptchaButton.getFromParent(new UiSelector().className(android.widget.ImageView.class.getName()));

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
            cr = ScriptHelper.getVerifyCode(captcha, "3000", "41206", "0c6c7d3efba743b68037d9f87d70c6f4");

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

    // 进入朋友圈
    private void enterFriends() {
        try {
            UiObject findText = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("发现").index(1));
            if (findText.exists()) {
                UiObject findImageButton = findText.getFromParent(new UiSelector().className(android.widget.ImageView.class.getName()).index(0));
                if (findImageButton.exists()) {
                    findImageButton.clickAndWaitForNewWindow();
                }
            }

            // 点击朋友圈
            UiObject friendsQQ = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("朋友圈"));
            if (friendsQQ.exists()) {
                friendsQQ.clickAndWaitForNewWindow();
            }
        } catch (Exception ex) {
            //jqhelper.writeSDFileEx("异常 enterFriends()：" + ex.toString() + " \n", "/sdcard/error.txt");
            AddLog(batchNumber, "进入朋友圈出错", ex.toString());
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
        }
        catch (Exception ex) {
            AddLog(batchNumber, "解析时间字符出错", ex.toString());
        }
        return date;
    }

    // 执行抓取
    private List<WechatData> execute(List<WxDataItem> wx_names, Date t) {
        List<WechatData> dataList = new ArrayList<WechatData>();
        try {
            UiScrollable listView = new UiScrollable(new UiSelector().className(android.widget.ListView.class.getName()));
            if (!listView.waitForExists(10000)) {
                return null;
            }
            listView.setAsVerticalList();
            for (;;) {
                // 滚动到最上
                // listView.scrollToBeginning(10, 10);
                ScriptHelper.swipe(0, 200, 0, 1600);
                UiObject u = new UiObject(new UiSelector().className(android.widget.ImageView.class.getName()).descriptionContains("朋友圈封面，再点一次可以改封面"));
                if (u.exists()) {
                    sleep(2000);
                    break;
                }
            }

            int n = 0;

            //

          /*  for (int i = 0; true; i++) {
                UiObject uo = listView.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName()).index(i));
                UiObject imageButton = uo.getChild(new UiSelector().className(android.widget.ImageButton.class.getName()));
                UiObject ll = uo.getChild(new UiSelector().className("android.widget.LinearLayout"));
                UiObject
                //UiObject
                if (uo.exists() && imageButton.exists() && ll.exists()) {

                } else {
                    i = -1;
                    listView.scrollForward();
                }
            }*/


            for (; ; ) {
                try {
                    listView = new UiScrollable(new UiSelector().className(android.widget.ListView.class.getName()));
                    listView.setAsVerticalList();

                    //int nCount = listView.getChildCount(new UiSelector().className(android.widget.ImageButton.class.getName()));
                    int nCount = listView.getChildCount(new UiSelector().className(android.widget.FrameLayout.class.getName()).childSelector(new UiSelector().className(android.widget.ImageButton.class.getName())));
                    if (nCount == 0) {
                        sleep(3000);
                        n++;

                        if (n > 3) break;

                        continue;
                    }

                    UiObject lastHead = null;
                    UiObject lastTime = null;
                    Boolean bBreak = false;

                    for (int i = 0; i < 10; i++) {
                        List<String> imgList = null;

                        String friendName = "";
                        String time = "";
                        String content = "";

                        listView = new UiScrollable(new UiSelector().className(android.widget.ListView.class.getName()));
                        listView.setAsVerticalList();
                        UiObject head = listView.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName()).index(i)).getChild(new UiSelector()
                                .className(android.widget.ImageButton.class.getName()).description("头像"));
                        if (!head.exists()) {
                            continue;
                        }

                        lastHead = head;

                        //UiObject head = listView.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName()).instance(i).childSelector(new UiSelector().className(android.widget.ImageButton.class.getName())));
                        //UiObject head = listView.getChild(new UiSelector().className(android.widget.ImageButton.class.getName()).instance(i)); // 头像
                        UiObject al = head.getFromParent(new UiSelector().className(android.widget.LinearLayout.class.getName()).index(1));
                        UiObject ala = al.getChild(new UiSelector().className(android.widget.RelativeLayout.class.getName()).index(0));
                        UiObject friendNameText = ala.getChild(new UiSelector().className(android.widget.TextView.class.getName()).index(0));
                        if (friendNameText.exists()) {
                            friendName = friendNameText.getText();   // 朋友名
                        }

                        UiObject timeText = al.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName()))
                                .getChild(new UiSelector().className(android.widget.TextView.class.getName()));
                        if (timeText.exists()) {
                            time = timeText.getText();   // 时间
                            lastTime = timeText;
                        }

                        UiObject bl = head.getFromParent(new UiSelector().className(android.widget.LinearLayout.class.getName()).index(1));
                        UiObject bla = bl.getChild(new UiSelector().className(android.widget.LinearLayout.class.getName()).index(1));
                        UiObject contentText = bla.getChild(new UiSelector().className(android.widget.TextView.class.getName()).index(0));
                        if (contentText.exists()) {
                            content = contentText.getText(); // 内容
                        }

                        // 寻找图片
                    /* UiObject img0 = listView.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName())
                            .childSelector(new UiSelector().className(android.widget.ImageButton.class.getName()).instance(i)
                                    .fromParent(new UiSelector().className(android.widget.LinearLayout.class.getName())
                                            .childSelector(new UiSelector().className(android.widget.FrameLayout.class.getName())
                                                    .childSelector(new UiSelector().className(android.view.View.class.getName())).descriptionContains("图片").instance(0)))));*/


                    /*listView.getChild(new UiSelector().className(android.widget.FrameLayout.class.getName())
                            .instance(i)
                            .childSelector(new UiSelector().className(android.widget.ImageButton.class.getName())));*/
                        UiObject img0 = head.getFromParent(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                                .getChild(new UiSelector().className(android.widget.FrameLayout.class.getName()))
                                .getChild(new UiSelector().className(android.view.View.class.getName()).descriptionContains("图片").instance(0));

                        //infoF("text %s-%s\n", friendName, content);

                        // 最后更新时间
                        if (!time.isEmpty() && lastUpdate != null) {
                            Date wxdate = ParseWxDate(time);
                            if (wxdate != null) {
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(lastUpdate);

                                calendar.add(calendar.MINUTE, -1);
                                Date d = calendar.getTime();   //这个时间就是日期往后推一天的结果
                                if (wxdate.compareTo(d) < 0) {
                                    bBreak = true;
                                    break;
                                }
                            }
                        }

                        // 判断微信是否需要抓取
                        if (!checkWx(wx_names, friendName))
                            continue;

                        boolean needSave = true;
                        Date wxdate = ParseWxDate(time);
                        Date mydate = GetTime(wx_names, friendName);
                        if (mydate != null && wxdate != null && wxdate.compareTo(mydate) < 0)
                            needSave = false;

                        // 检查是否已经上传
                        //String key = friendName + "|" + userId + "|" + d.getContent();
                        //if (!load.containsKey(key)) {

                        if (!friendName.isEmpty() && !time.isEmpty() && !content.isEmpty() && needSave && img0.exists()) {
                            String key = friendName + "|" + content;
                            if (!load.containsKey(key)) {
                                AddLog(batchNumber, "开始抓图", friendName + "|" + content);

                                img0.clickAndWaitForNewWindow();
                                imgList = fetchImages();

                                AddLog(batchNumber, "抓图完成", friendName + "|" + content + "|图片数: " + String.valueOf(imgList.size()));

                                getUiDevice().pressBack();

                                sleep(2000);
                            }
                        }

                        if (imgList != null && imgList.size() > 0 && !friendName.isEmpty() && !time.isEmpty() && !content.isEmpty()) {
                            boolean has = false;
                            for (WechatData x : dataList) {
                                if (x.getName() == friendName && x.getTime() == time && x.getContent() == content) {
                                    has = true;
                                    break;
                                }
                            }
                            //

                            if (!has && needSave) {
                                dataList.add(new WechatData(friendName, content, time, imgList));
                            }
                        }

                        //
                        if (!time.equals("")) {
                            if (t != null && wxdate != null && t.compareTo(wxdate) < 0) {
                                break;
                            }
                        }
                    /*if (!time.equals("") && time.contains("天前")) {
                        String timeString = time.replaceAll("天前", "");
                        Integer t = 0;
                        try {
                            t = Integer.parseInt(timeString);
                        }
                        catch (Exception ex) {

                        }
                        if (t > 15) {
                            bBreak = true;
                        }
                    }*/
                    }

                    // 退出条件,找到最底部的一条线
                    UiObject sp = listView.getChild(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                            .getChild(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                            .getChild(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                            .getChild(new UiSelector().className(android.widget.ImageView.class.getName()));

                    if (bBreak || sp.exists()) {
                        break;
                    }

                    // 滑动到最后一个头像
                    //if (lastHead != null) {
                    //if (lastTime != null) {
                    //listView.scrollIntoView(lastHead);
                    //listView.scrollIntoView(lastTime);
                    //} else {
                    //    listView.scrollForward();
                    Integer offset = 100; // 184
                    Integer y = lastHead.getBounds().top - offset;
                    if (y < 300) {
                        y = 500;
                    }
                    ScriptHelper.swipe(0, y, 0, 0);
                    sleep(3000);
                    //}
                }
                catch (Exception ex) {
                    AddLog(batchNumber, "提取信息出错A", "");
                    sleep(3000);
                }

                // 判断是否正在加载
                UiObject loading = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("正在加载..."));
                if (loading.exists()) {
                    sleep(10000);
                    loading = new UiObject(new UiSelector().className(android.widget.TextView.class.getName()).text("正在加载..."));
                    if (loading.exists()) {
                        Device.pressBack();
                        sleep(3000);
                        break;
                    }
                }
            }

            sleep(3000);

            // 退出菜单
            //UiObject backButton = new UiObject(new UiSelector().className(android.widget.ImageView.class.getName()).descriptionContains("返回"));
            //backButton.clickAndWaitForNewWindow();

        } catch (Exception ex) {
            //jqhelper.writeSDFileEx("异常 execute()：" + ex.toString() + " \n", "/sdcard/error.txt");
            AddLog(batchNumber, "提取信息出错", "");
        }
        finally {
            // 返回
            Device.pressBack();
        }

        return dataList;
    }

    // 抓取图片
    private List<String> fetchImages() {
        sleep(5000);

        List<String> list = new ArrayList<String>();

        try {
            // 检查多少张图片
            int ivCount = 1;
            for (int i = 8; i >= 0; i--) {
                UiObject iv0 = new UiObject(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                        .getChild(new UiSelector().className(android.widget.LinearLayout.class.getName()))
                        .getChild(new UiSelector().className(android.widget.ImageView.class.getName()).index(i));
                if (iv0.exists()) {
                    ivCount = i + 1;
                    break;
                }
            }

            // 抓取并翻页，知道最后一张
            for (int i = 0; i < ivCount; i++) {
                UiScrollable scroll = new UiScrollable(new UiSelector().className(android.widget.Gallery.class.getName())
                        .childSelector(new UiSelector().className(android.widget.ImageView.class.getName())));
                scroll.setAsHorizontalList();

                UiObject iv = new UiObject(new UiSelector().className(android.widget.Gallery.class.getName()))
                        .getChild(new UiSelector().className(android.widget.ImageView.class.getName()));

                String filename = String.format("/sdcard/%s.png", UUID.randomUUID().toString());

                Rect r = iv.getBounds();
                Bitmap screen = ScriptHelper.screenShot();
                int x1, y1, x2, y2 = 0;
                x1 = r.left;
                y1 = r.top;
                x2 = r.left + r.width();
                y2 = r.top + r.height();

                //infoF("Img X1=%d Y1=%d X2=%d Y2=%d\n", x1, y1, x2, y2);

                Bitmap captcha = ScriptHelper.cutImage(screen, x1, y1, x2, y2);
                ScriptHelper.savePngToSdCard(captcha, filename);

                list.add(filename);

                if (i < ivCount - 1) {
                    scroll.scrollForward();
                    sleep(5000);
                }
            }
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 fetchImages()：" + ex.toString() + " \n", "/sdcard/error.txt");
            AddLog(batchNumber, "提取图片异常", ex.toString());
        }

        return list;
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
    private List<WxDataItem> GetWXNames() {
        //http://192.168.1.177:8007/api/wx/getwxnames
        try {
            String data = HttpHelper.httpGetString(getWXNamesURL);
            Type type = new TypeToken<WxData>() {
            }.getType();
            WxData wx = gson.fromJson(data, type);

            List<WxDataItem> list = new ArrayList<WxDataItem>();

            JSONArray results = new JSONArray(wx.Text);

            for (int i = 0; i < results.length(); i++) {
                WxDataItem a = new WxDataItem();
                JSONObject result = results.getJSONObject(i);

                a.setName(result.getString("name"));
                a.setUserID(result.getString("userID"));
                a.setTime(result.getString("time"));
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

    public static String toHexString2(byte[] b)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i)
        {
            buffer.append(toHexString2(b[i]));
            //buffer.append(" ");
        }
        return buffer.toString();
    }

    public static String toHexString2(byte b)
    {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1)
        {
            return "0" + s;
        }
        else
        {
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
            jqhelper.writeSDFileEx("异常 UploadImage()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
        return "";
    }

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
            resp = EntityUtils.toString(HttpHelper.httpPost(updateImageURL, postData, "application/x-www-form-urlencoded"), "utf8");

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
            jqhelper.writeSDFileEx("异常 UploadImage()：" + ex.toString() + " \n", "/sdcard/error.txt");
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
                        String picId = UploadImage2(filename, userId);
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
        }
        catch (Exception ex) {
            AddLog(batchNumber, "上传异常", ex.toString());
            jqhelper.writeSDFileEx("异常 upload()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    private static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    // file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                // file.delete();
            }
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
                    //File file = new File(f);
                    //boolean deleted = file.delete();

                    DeleteFile(new File(f));
                    //String f1 = f.replaceAll("sdcard", "手机");
                    //File file1 = new File(f1);
                    //file1.delete();
                }
                catch (Exception ex) {
                    AddLog(batchNumber, "删除文件出错", ex.toString());
                    jqhelper.writeSDFileEx("异常 deleteTempFiles()：" + ex.toString() + " \n", "/sdcard/error.txt");
                }
            }
        }
    }

    // 记录日志
    public void AddLog(String batchNumber, String type, String remark) {
        try {
            // string batchNumber, string type, string remark, string moblie
            String postData = String.format("{\"batchNumber\":\"%s\",\"type\":\"%s\",\"remark\":\"%s\",\"moblie\":\"%s\"}",
                    batchNumber,
                    type,
                    remark,
                    Vps);
            String resp = HttpHelper.httpPostForString(addLogURL, postData);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常 AddLog()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 提交
    public void UpdateTo(String name, String userID, String timeString, String content, String pics) {
        try {
            String postData = String.format("{\"wxname\":\"%s\",\"uid\":\"%s\",\"time_string\":\"%s\",\"content\":\"%s\",\"pics\":\"%s\"}",
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

    // 注册
    public String process() {
        try {
            sleep(1000);

            infoF("点击 0\n");

            /*if (waitCoverView()) {
                sleep(1000);
                doLoginClick();
            } else {
                return "";
            }*/

            /*if (waitPhoneLoginView()) {
                sleep(1000);
                doLogin();
                //doLogin2();
                doContactMsgClick();
            }*/

            //if (waitMainView()) {
            sleep(1000);

            for (; ; ) {

                sleep(5000);

                try {

                    List<WxDataItem> wx_names = GetWXNames();

                    Date t = GetTime(wx_names, "");

                    if (wx_names == null || wx_names.size() == 0) {
                        sleep(5000);
                        continue;
                    }

                    if (t != null) {
                        lastUpdate = t;
                    }

                    batchNumber = UUID.randomUUID().toString();

                    AddLog(batchNumber, "消息", "进入朋友圈");

                    enterFriends();

                    List<WechatData> list = execute(wx_names, t);

                    // 记录当前时间
                    lastUpdate = new Date();

                    AddLog(batchNumber, "消息", "拉取信息完成");

                    upload(list, wx_names);

                    AddLog(batchNumber, "消息", "上传完成");

                    deleteTempFiles(list);

                    AddLog(batchNumber, "消息", "清除临时文件");
                }
                catch (Exception ex) {
                    AddLog(batchNumber, "执行出错", ex.toString());
                }

                sleep(3000);
            }
            //}

            //sleep(5000);
            //infoF("点击 1\n");

            //break;
            //}
        } catch (Exception e) {
            return "run 抛错：" + e.getMessage();
        }
        //return "";
    }
    //endregion
}
