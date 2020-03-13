package com.common;


import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.common.uiControl.UiEdit;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeqee on 2015/9/23.
 */
public class SuperRunner extends UiAutomatorTestCase {
    public Gson gson = new Gson();

    //获取当前设备
    protected UiDevice Device = null;
    //object临时变量testRunner
    protected UiObject ob = null;
    //中文输入框临时变量
    protected UiEdit edit = null;
    /**
     * 目标应用的PackegName
     */
    protected String PackegName = "";

    /**
     * 目标应用的ActivityName
     */
    protected String ActivityName = "";
    //手机名
    protected String Vps = "";
    //url集合
    protected Map<String, String> urlMap = new HashMap<String, String>();


    //错误日志
    protected void Loge(String stack, String logs) {
        System.out.println("[error]" + "[" + stack + "]" + "[" + logs + "]");
    }

    //信息日志
    protected void Logi(String logs) {
        System.out.println("[info]" + "[" + logs + "]");
    }

    //获取当前时间
    protected String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    ///获取控件
    protected UiObject GetUIObject(String ClassName) {
        return new UiObject(new UiSelector().className(ClassName));
    }

    protected UiObject GetUIObject(String ClassName, String Text, int index, String Desc) {
        return new UiObject(new UiSelector().className(ClassName).text(Text).index(index).description(Desc));
    }

    protected UiObject GetUIObject(String ClassName, int index, String Desc) {
        return new UiObject(new UiSelector().className(ClassName).index(index).description(Desc));
    }

    protected UiObject GetUIObject(String ClassName, String Text, int index) {
        return new UiObject(new UiSelector().className(ClassName).text(Text).index(index));
    }

    protected UiObject GetUIObject(String ClassName, String Text) {
        return new UiObject(new UiSelector().className(ClassName).text(Text));
    }

    protected UiObject GetUIObject(String ClassName, String Text, String Desc) {
        return new UiObject(new UiSelector().className(ClassName).text(Text).description(Desc));
    }

    protected UiObject GetUIObject(String ClassName, int index) {
        return new UiObject(new UiSelector().className(ClassName).index(index));
    }

    protected UiSelector GetUISelect(String ClassName) {
        return new UiSelector().className(ClassName);
    }

    protected UiSelector GetUISelect(String ClassName, int index) {
        return new UiSelector().className(ClassName).index(index);
    }

    protected UiSelector GetUISelect(String ClassName, String Text) {
        return new UiSelector().className(ClassName).text(Text);
    }

    protected UiSelector GetUISelect(String ClassName, String Text, String Desc) {
        return new UiSelector().className(ClassName).text(Text).description(Desc);
    }

    protected UiSelector GetUISelect(String ClassName, String Text, int index, String Desc) {
        return new UiSelector().className(ClassName).index(index).text(Text).description(Desc);
    }

    protected UiSelector GetUISelect(String ClassName, int index, String Desc) {
        return new UiSelector().className(ClassName).index(index).description(Desc);
    }

    protected UiSelector GetUISelect(String ClassName, String Text, int index) {
        return new UiSelector().className(ClassName).index(index).text(Text);
    }
}

