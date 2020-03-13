package com.netease;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.RemoteException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.base.jqhelper;
import com.common.SuperRunner;
import com.common.helper.HttpHelper;
import com.common.helper.ScriptHelper;
import com.common.model.CaptchaResult;
import com.google.gson.reflect.TypeToken;
import com.googlecode.leptonica.android.Convert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mailRegistor extends SuperRunner {
    private String Name = "";
    private String Password = "";
    private String apiURL = "http://222.185.251.62:7000/api";
    //private String apiURL = "http://192.168.1.172:8008/api";

    public void testRunner() throws UiObjectNotFoundException, RemoteException {
        try {
            Initialization();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常Initialization()：" + e.toString() + " \n", "/sdcard/error.txt");
        }

        while (true) {
            try {
                ScriptHelper.reportNormal();

                startApp();

                process();
                //test();

            } catch (Exception e) {
                jqhelper.writeSDFileEx("异常testRunner()：" + e.toString() + " \n", "/sdcard/error.txt");
            }
        }
    }

    private void Initialization() throws UiObjectNotFoundException, RemoteException, IOException {
        PackegName = "com.netease.mail";
        ActivityName = "com.netease.mobimail.activity.LaunchActivity";
        Vps = ScriptHelper.readSDFile("/sdcard/vps.txt");
        //jqhelper.initOCR();
        ScriptHelper.switchImeToAdbKeyboard();
        cleanProcess();
    }

    //清理
    private void cleanProcess()
    {
        ScriptHelper.killApp(PackegName);   //关闭邮箱大师
        sleep(800);
        ScriptHelper.clearPackage(PackegName);//清理邮箱大师
        sleep(800);
    }

    //启动
    private void startApp() {
        try {
            cleanProcess();
            jqhelper.reConnNet();
            sleep(5000);
            Device = getUiDevice();
            Device.wakeUp();
            sleep(800);
            ScriptHelper.startActivity(PackegName + "/" + ActivityName);
            sleep(5000);
            ScriptHelper.reportNormal();
        } catch (Exception e) {
            jqhelper.writeSDFileEx("异常startApp()：" + e.toString() + " \n", "/sdcard/error.txt");
        }
    }

    //region Register

    // 判断登录界面
    private boolean waitLoginView() {
        //UiObject loginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(2));
        UiObject button = new UiObject(new UiSelector().className("android.widget.Button").text("注册新邮箱").index(2));
        //return loginBtn.waitForExists(10000);
        return button.exists();
    }

    // 判断输入手机号界面
    private boolean waitEnterPhoneNumberView() {
        UiObject phoneTipText = new UiObject(new UiSelector().className("android.widget.TextView").text("请输入你的手机号").index(1));
        UiObject charMailButton = new UiObject(new UiSelector().className("android.widget.Button").text("注册字母邮箱").index(3));
        return (phoneTipText.exists() && charMailButton.exists());
    }

    // 判断邮箱地址输入
    private boolean waitEnterEmailView() {
        UiObject mailTipText = new UiObject(new UiSelector().className("android.widget.TextView").text("请输入邮箱地址").index(1));
        UiObject phoneButton = new UiObject(new UiSelector().className("android.widget.Button").text("注册手机号码邮箱").index(3));
        return (mailTipText.exists() && phoneButton.exists());
    }

    // 判断输入密码
    private boolean waitEnterPasswordView() {
        UiObject pwdTipText = new UiObject(new UiSelector().className("android.widget.TextView").text("请设置你的邮箱密码").index(1));
        return pwdTipText.exists();
    }

    // 判断验证码窗口
    private boolean waitCaptchaView() {
        UiObject tipText = new UiObject(new UiSelector().className("android.widget.TextView").text("请输入图形验证码").index(1));
        return tipText.exists();
    }

    // 判断进入邮箱
    private boolean waitGotoMail() {
        UiObject goButton = new UiObject(new UiSelector().className("android.widget.Button").text("进入邮箱").index(1));
        return goButton.exists();
    }

    // 判断我的邮箱
    private boolean waitMyMail() {
        UiObject my = new UiObject(new UiSelector().className("android.widget.TextView").text("我").index(2));
        return my.exists();
    }

    // 判断错误对话框
    private boolean checkErrorMsg() {
        UiObject my = new UiObject(new UiSelector().className("android.widget.TextView").text("系统错误").index(0));
        return my.exists();
    }

    // 注册新邮箱
    private void doRegMail() {
        try {
            UiObject button = new UiObject(new UiSelector().className("android.widget.Button").text("注册新邮箱").index(2));
            button.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doRegMail()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 注册字母邮箱
    private void doCharMail() {
        try {
            UiObject charMailButton = new UiObject(new UiSelector().className("android.widget.Button").text("注册字母邮箱").index(3));
            charMailButton.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doCharMail()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 输入邮箱地址
    private void doEnterEmail() {
        try {
            UiObject mailEdit = new UiObject(new UiSelector().className("android.widget.EditText").text("字母开头，6～18个字符").index(0));
            mailEdit.setText(this.Name);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterEmail()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }

        sleep(800);

        // 点击下一步
        try {
            UiObject nextButton = new UiObject(new UiSelector().className("android.widget.Button").text("下一步").index(0));
            nextButton.clickAndWaitForNewWindow();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterEmail()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 输入密码
    private void doEnterPassword() {
        UiObject scroll = new UiObject(new UiSelector().className("android.widget.ScrollView").index(0));

        try {
            UiObject pwdEdit0 = scroll.getChild(new UiSelector().className("android.widget.RelativeLayout").index(0));
            UiObject pwdEdit = pwdEdit0.getChild(new UiSelector().className("android.widget.EditText").index(0));
            pwdEdit.setText(this.Password);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterPassword()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }

        try {
            UiObject cpwdEdit0 = scroll.getChild(new UiSelector().className("android.widget.RelativeLayout").index(1));
            UiObject cpwdEdit = cpwdEdit0.getChild(new UiSelector().className("android.widget.EditText").index(0));
            cpwdEdit.setText(this.Password);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterPassword()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }

        sleep(800);

        UiObject nextButton = new UiObject(new UiSelector().className("android.widget.Button").text("下一步").index(0));
        try {
            nextButton.clickAndWaitForNewWindow();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterPassword()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // 输入验证码
    private void doEnterCaptcha() {
        UiObject captchaImage = new UiObject(new UiSelector().className("android.widget.ImageButton").index(1));
        if (!captchaImage.exists()) {
            infoF("Captcha not found.");
            return;
        }
        infoF("Captcha found.");
        CaptchaResult cr = null;
        try {
            Rect r = captchaImage.getBounds();
            Bitmap screen = ScriptHelper.screenShot();
            int x1, y1, x2, y2 = 0;
            x1 = r.left;
            y1 = r.top;
            x2 = r.left + r.width();
            y2 = r.top + r.height();
            infoF("Captch X1=%d Y1=%d X2=%d Y2=%d\n", x1, y1, x2, y2);
            Bitmap captcha = ScriptHelper.cutImage(screen, x1, y1, x2, y2);
            ScriptHelper.savePngToSdCard(captcha, "/sdcard/jmr_captcha.png");
            cr = ScriptHelper.getVerifyCode(captcha, "3000", "41206", "0c6c7d3efba743b68037d9f87d70c6f4");
        }
        catch (Exception ex) {
            infoF("异常doEnterCaptcha()：" + ex.toString() + " \n");
        }

        if (cr == null || cr.getCode() == null || cr.getCode().length() < 3) {
            try {
                if (cr != null) {
                    ScriptHelper.reportError(cr);
                }
            }
            catch (Exception ex) {
                infoF("异常doEnterCaptcha()：" + ex.toString() + " \n");
            }
            return;
        }

        infoF("Captcha: %s", cr.getCode());

        // 输入验证码
        UiObject t = new UiObject(new UiSelector().className("android.widget.RelativeLayout").index(0));
        try {
            UiObject w = t.getChild(new UiSelector().className("android.widget.EditText").index(0));
            w.setText(cr.getCode());
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterCaptcha()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }

        // 点击注册按钮
        UiObject regButton = new UiObject(new UiSelector().className("android.widget.Button").text("注册").index(0));
        try {
            regButton.clickAndWaitForNewWindow();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常doEnterCaptcha()：" + ex.toString() + " \n", "/sdcard/error.txt");
        }
    }

    // Go
    private void doGo() {
        UiObject goButton = new UiObject(new UiSelector().className("android.widget.Button").text("进入邮箱").index(1));
        try {
            goButton.clickAndWaitForNewWindow();
        }
        catch (Exception ex) {
            infoF("异常doGo();" + ex.toString());
        }
    }

    /*
    记录信息
     */
    private void infoF(String fmt, Object... args) {
        String text = String.format(fmt, args);
        ScriptHelper.appendToSDFile(text, "/sdcard/error.txt");
    }

    /*
    提交
     */
    private boolean submit() {
        if (this.Name.isEmpty() || this.Password.isEmpty()) {
            return true;
        }
        try {
            String url = apiURL + "/account/new";
            // Account表 Type=19，网易邮箱
            String json = String.format("{\"name\":\"%s\",\"password\":\"%s\",\"type\":%d,\"vps\":\"%s\"}",
                    this.Name + "@163.com",
                    this.Password,
                    19,
                    Vps
            );
            HttpHelper.httpPostForString(url, json);
            return true;
        }
        catch (Exception ex) {
            infoF("异常submit();" + ex.toString());
            return false;
        }
    }

    // 测试
    public void test() {
        try {
            this.Name = "aab@163.com";
            this.Password = "wewfaf";
            submit();
        } catch (Exception ex) {
            jqhelper.writeSDFileEx("异常test()：" + ex.toString() + " \n", "/sdcard/error.txt");
            infoF("error %s\n", ex.toString());
        }
    }

    // 注册
    public String process() {
        try {
            this.Name = RandName();
            this.Password = RandPassword();

            sleep(5000);

            for (int i = 0; i < 3; i++) {
                infoF("点击 0\n");

                if (waitLoginView()) {
                    doRegMail();
                    //sleep(3000);
                }

                infoF("点击 1\n");

                //sleep(3000);
                if (waitEnterPhoneNumberView()) {
                    doCharMail();
                    //sleep(3000);
                }

                infoF("点击 2\n");
                //sleep(3000);
                if (waitEnterEmailView()) {
                    doEnterEmail();
                }

                infoF("点击 3\n");
                if (waitEnterPasswordView()) {
                    doEnterPassword();
                }

                infoF("点击 4\n");
                if (waitCaptchaView()) {
                    doEnterCaptcha();
                }

                infoF("点击 5\n");
                if (waitGotoMail()) {
                    doGo();
                }

                infoF("点击 6\n");
                if (waitMyMail()) {
                    do {
                        if (submit()) {
                            break;
                        }
                        sleep(1000);
                    } while (true);

                    break;
                }
            }
        } catch (Exception e) {
            return "run抛错：" + e.getMessage();
        }
        return "";
    }

    // 随机用户名
    private String RandName() {
        char[] CharArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        String sCode = String.valueOf(CharArray[random.nextInt(52-9)]);
        for (int i = 0; i < 15; i++) {
            sCode += CharArray[random.nextInt(CharArray.length)];
        }
        return sCode.toLowerCase();
    }

    // 随机密码
    private String RandPassword() {
        char[] CharArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random random = new Random();
        String sCode = "";
        for (int i = 0; i < 8; i++) {
            sCode += CharArray[random.nextInt(CharArray.length)];
        }
        return sCode;
    }

    //endregion
}
