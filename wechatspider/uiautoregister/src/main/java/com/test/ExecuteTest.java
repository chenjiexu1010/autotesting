package com.test;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.common.SuperRunner;
import com.common.uiControl.UiEdit;


public class ExecuteTest extends SuperRunner {

    /**
     * 初始化信息
     */
    private void InitInfo() {
        /**
         * 目标应用的PackegName
         */
        PackegName = "com.taobao.taobao";
        /**
         * 目标应用的ActivityName
         */
        ActivityName = "";
        //获取设备
        Device = getUiDevice();
        System.out.println("初始化完成");
    }

    public void testRunner() {
        try {
            //初始化
            InitInfo();
            Device.wakeUp();
            System.out.println("屏幕以唤起");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
