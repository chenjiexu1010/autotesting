package com.hot;

import android.graphics.Rect;

import com.WeChatNew.LoginInfo;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.uiControl.UiEdit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class oasis extends SuperRunner {
    //外网ip
    private String Host = "222.185.251.62";
    //添加日志
    private String addLogURL = "http://" + Host + ":30008/api/wx/addlog";
    public Gson gson = new Gson();
    private String pngPath = "/sdcard/home.png";

    //绿洲SEO
    private String oasis_seo_url = "http://222.185.251.62:22027/api/GetLvZhouData";
    private String oasis_seo_submit_url = "http://222.185.251.62:22027/api/UpdateLvZhouPicCheck";

    //绿洲业务管理
    private String oasis_type_url = "http://v3.jqsocial.com:22025/api/common/oasis/getshot?code=jeqeehot";
    private String oasis_type_submit_url = "http://v3.jqsocial.com:22025/api/common/oasis/postshot";

    //上传置图片处理服务
    private String pic_handle_insert_url = "http://222.185.251.62:22027/api/phone/pichandle";
    private String pic_change_url = "http://222.185.251.62:22027/api/phone/getpicurl";

    //启动方法
    public void testRunner() {
        StartMain();
    }

    public void Init() {
        try {
            Device = getUiDevice();
            Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
            PackegName = "com.sina.oasis";
        } catch (Exception e) {
        }
    }

    public void StartMain() {
        Init();
        //切换输入法
        ScriptHelper.switchImeToAdbKeyboard();
        while (true) {
            try {
                jqhelper.reportNormal();
                RestartApp();
                //获取SEO任务
                oasismodel oasis = GetSeoTask();
                if (oasis == null) {
                    AddLog("绿洲SEO", "无任务", "");
                    sleep(1000 * 10);
                    continue;
                }
                boolean isFind = Execute(oasis);
                if (isFind) {
                    AddLog("绿洲SEO", "找到笔记", oasis.getTitle());
                    //截图
                    Device.takeScreenshot(new File(pngPath));
                    ob = new UiObject(new UiSelector().text(oasis.getNickName()));
                    if (ob.exists()) {
                        byte[] picByte = ScriptHelper.readSDFileBytes(pngPath);
                        String picStr = gson.toJson(picByte);
                        String result = HttpHelper.httpPostForString(this.pic_change_url, picStr);
                        AddLog("绿洲SEO", result, "绿洲");
                        //序列化后的图片
                    }
                }
            } catch (Exception e) {
            } finally {
                //删除手机目录下的截图
                HttpHelper.DeleteFile(new File(pngPath));
                sleep(1000 * 10);
            }
        }
    }

    /**
     * 寻找截图
     *
     * @return
     */
    public boolean Execute(oasismodel oasis) {
        try {
            ob = new UiObject(new UiSelector().resourceId("com.sina.oasis:id/discovery"));
            if (ob.waitForExists(2000)) {
                ob.click();
                sleep(2000);
                ob = new UiObject(new UiSelector().text("搜索用户、动态和主题"));
                if (ob.waitForExists(2000)) {
                    ob.click();
                    sleep(2000);
                    UiEdit edit = new UiEdit(new UiSelector().className("android.widget.EditText"));
                    if (ob.waitForExists(2000)) {
                        ob.click();
                        sleep(1000);
                        edit.setChineseText(oasis.getKeyWord());
                        sleep(2000);
                    }
                }
            }
            ob = new UiObject(new UiSelector().text("相关用户"));
            if (!ob.waitForExists(2000)) {
                return false;
            }
            UiScrollable obj = new UiScrollable(new UiSelector().text("相关动态"));
            for (int i = 0; i < 3; i++) {
                if (obj.exists()) {
                    break;
                }
                ScriptHelper.swipe(0, 1700, 0, 1200);
            }
            if (!obj.exists()) {
                return false;
            }
            for (int i = 0; i < 6; i++) {
                ob = new UiObject(new UiSelector().text(oasis.getTitle()));
                // 标题是否存在
                if (ob.exists()) {
                    AddLog("绿洲SEO", "截图", "找到内容");
                    return true;
                }
                ScriptHelper.swipe(0, 1700, 0, 1200);
            }
        } catch (Exception e) {
            AddLog("绿洲SEO", e.toString(), "绿洲");
        }
        AddLog("绿洲SEO", "截图", "未找到内容");
        return false;
    }

    private oasismodel GetSeoTask() {
        try {
            jqhelper.reportNormal();
            String data = HttpHelper.httpGetString2(oasis_seo_url);
            if (data.contains("任务为空")) return null;
            Type type = new TypeToken<oasismodel>() {
            }.getType();
            return gson.fromJson(data, type);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 重启App
     */
    public void RestartApp() {
        try {
            jqhelper.reportNormal();
            Device.wakeUp();
            ob = new UiObject(new UiSelector().resourceId("com.sina.oasis:id/discovery"));
            if (!ob.waitForExists(2000)) {
                jqhelper.killApp(PackegName);
                jqhelper.killApp(PackegName + "：push");
                sleep(1000);
            }
            Runtime.getRuntime().exec("am start -n com.sina.oasis/.main.SplashActivity");
            sleep(5000);
        } catch (Exception e) {
        }
    }

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
}
