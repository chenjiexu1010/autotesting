package com.common.uiControl;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;

import com.common.jutf7.Utf7ImeHelper;

/**
 * Created by Jeqee on 2015/9/23.
 */
public class UiEdit extends UiObject {
    public UiEdit(UiSelector selector) {
        super(selector);
    }
    public UiEdit(int index){
        super(new UiSelector().className("android.widget.EditText").index(index));
    }
    public void setChineseText(String text) {
        try {
            this.setText(Utf7ImeHelper.e(text));
        } catch (Exception ee) {
            System.out.println("setChineseText:" + ee.getMessage());
        }
    }
}
