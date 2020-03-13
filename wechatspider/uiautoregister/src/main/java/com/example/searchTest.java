package com.example;

import android.os.RemoteException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import java.io.IOException;

import com.common.jutf7.Utf7ImeHelper;

/**
 * Created by Jeqee on 2015/9/23.
 * 简单逻辑控制Demo
 */
public class searchTest extends UiAutomatorTestCase {

    //搜索 app 逻辑控制
    public void testInput() throws UiObjectNotFoundException, RemoteException {
        UiDevice device = getUiDevice();
        // 唤醒屏幕
        device.wakeUp();
        assertTrue("screenOn: can't wakeup", device.isScreenOn());
        // 回到HOME
        device.pressHome();


        // 启动 搜索 App
        try {
            //Runtime.getRuntime().exec("am start -n com.android.quicksearchbox/.SearchA");
            UiObject inButton = new UiObject(new UiSelector().text("搜索"));  //in是app 图标元素Text
            inButton .clickAndWaitForNewWindow();

            UiObject _intext= new UiObject(new UiSelector().className("android.widget.EditText"));
            //输入文字
            //_intext.setText(Utf7ImeHelper.e("aabb"));

            _intext.setText(Utf7ImeHelper.e("こにUi我呵呵Aotorで入力。"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        UiObject _inputText =new UiObject(new UiSelector().className("android.widget.EditText"));
//        _inputText.setText("aabb");
    }
}
