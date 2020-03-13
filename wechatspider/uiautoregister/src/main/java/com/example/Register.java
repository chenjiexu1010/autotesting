package com.example;

import android.os.RemoteException;

import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

/**
 * Created by Jeqee on 2015/9/23.
 * 中文输入法验证Demo
 */

public class Register  extends UiAutomatorTestCase {



    //计算器app 逻辑控制
    public void testBLL() throws UiObjectNotFoundException, RemoteException {


        /*UiDevice device = getUiDevice();
        // 唤醒屏幕
        device.wakeUp();
        assertTrue("screenOn: can't wakeup", device.isScreenOn());
        // 回到HOME
        device.pressHome();

        //qq账号web服务地址
//        String html = jqhelper.httpGetString("http://222.185.195.206:11181/TakeAccount");
//        JSONObject jo =new  JSONObject(html);
//        String username = jo.getString("Uin");
//        String password =jo.getString("pass");

        sleep(1000);

        // 启动计算器App
        try {
            Runtime.getRuntime().exec("am start -n com.android.calculator2/.Calculator");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sleep(1000);

        //得到一个1控件
        UiObject oneButton = new UiObject(new UiSelector().text("1"));
        assertTrue("oneButton not found", oneButton.exists());
        UiObject plusButton = new UiObject(new UiSelector().text("+"));
        assertTrue("plusButton not found", plusButton.exists());

        sleep(100);

        UiObject equalButton = new UiObject(new UiSelector().text("="));
        assertTrue("equalButton not found", equalButton.exists());

        oneButton.click();
        sleep(100);
        plusButton.click();
        sleep(100);
        oneButton.click();

        equalButton.click();
        sleep(100);

        UiObject switcher = new UiObject(new UiSelector().resourceId("com.android.calculator2:id/display"));
        UiObject result = switcher.getChild(new UiSelector().index(0));
        System.out.print("text is :" + result.getText());
        assertTrue("result != 2", result.getText().equals("2"));*/
    }





}
