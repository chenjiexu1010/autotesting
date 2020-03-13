package com.weibo;

import android.os.RemoteException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.uiControl.UiEdit;


/**
 * Created by Jeqee on 2015/9/24.石栋梁
 */
public class wbRegistor  extends UiAutomatorTestCase {

    private String curQQ;//当前流程qq号
    private String curUser;//当前用户名
    private String curPwd;//当前密码
    private String serviceUrl="http://222.185.195.206:11181/TakeAccount";//数据源服务器地址

    //微博注册
    public void testrg() throws UiObjectNotFoundException, RemoteException {

       while (true) {
           try {
//               //清理
//               cleanProcess();
//
//               //初始化
//             if(Init()==true) {

                 //启动
                 startApp();

//                 //逻辑界面控制
//                 bLLControl();
//             }

           }catch (Exception ex)
           {
               jqhelper.writeSDFileEx("异常：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
           }

       }
    }

    //业务逻辑控制
    private void bLLControl()
    {
        for(int i=0;i<3;i++)
        {
            if(i==0)
            {
                jqhelper.delay(5000);
            }
            //消息界面1
            if(ismessageActivity()==true) {
                messageActivity();
            }
            //微博登录界面2
            if(isWeibLogin()==true) {
                WeibLogin();
            }
            //qq登录界面 3
            if(isqqLogin()==true) {
                qqLogin(curQQ, curPwd);
            }
            //授权并登录4
            if(ispersistAndLogin()==true) {
                persistAndLogin();
            }
            //完善个人资料5
            if(isNextStep()==true) {
                nextStep();
            }
            //关注感兴趣的人6
            if(isCarefull()==true) {
                carefull();
            }
            //进入微博7
            if(isenterWeibo()==true) {
                enterWeibo();
            }
            //未关注人消息 -->首页8
            if(isnotcareMessageFistPage()) {
                notcareMessageFistPage();
            }
            //未关注人消息-》消息9
            int temp =isnotcareMessage();
            if(temp==2) {
                notcareMessage();
            }
            else if(temp==1)
            {
                break;
            }


            //判断http消息链接10
            if(ishttpLink()==true) {
                httpLink();
                //流程结束
                break;
            }


        }
    }

    //清理
    private void cleanProcess()
    {
        jqhelper.killApp("com.tencent.mobileqq");//关闭QQ
        jqhelper.delay(800);
        jqhelper.clearPackage("com.tencent.mobileqq");//清理QQ
        jqhelper.delay(800);
        jqhelper.killApp("com.sina.weibo");//关闭微博(多个进程是否清理光)
        jqhelper.delay(800);
        jqhelper.clearPackage("com.sina.weibo");//清理微博
        jqhelper.delay(800);
        jqhelper.killApp("com.sina.weibo.image");//关闭微博(多个进程是否清理光)
        jqhelper.delay(800);
        jqhelper.clearPackage("com.sina.weibo.image");//清理微博
        jqhelper.delay(800);
        jqhelper.killApp("com.sina.weibo:remote");//关闭微博(多个进程是否清理光)
        jqhelper.delay(800);
        jqhelper.clearPackage("com.sina.weibo:remote");//清理微博
        jqhelper.delay(800);
    }

    //启动目标app
    private void startApp()
    {
        try {
            UiDevice device = getUiDevice();
            // 唤醒屏幕
            device.wakeUp();
            // 回到HOME
            device.pressHome();
            UiObject weiboBtn = new UiObject(new UiSelector().className("android.widget.TextView").text("微博"));  //微博图标
            weiboBtn.clickAndWaitForNewWindow(30000);
        }
        catch (Exception ex){
            jqhelper.writeSDFileEx("异常startApp()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //初始化（从服务器获取数据）
    private boolean Init()
    {
        //获取QQ数据，并初始化类属性字段
        curQQ="";
        curUser="";
        curPwd="";
        try {
            //记录软件运行次数
//            jqhelper.writeSDFileEx("\n", "/sdcard/wbnum.txt");
//            String html = jqhelper.httpGetString(serviceUrl);
//            JSONObject jo =new  JSONObject(html);
//            curQQ = jo.getString("Uin");
//            curPwd =jo.getString("pass");
            if(curUser.equals("0")|| curUser.equals(""))
            {
                curQQ="3350079180";
                //curUser="f3350079180@asf.com.cn";
                curPwd="ax6cyQTioUQ";
//                jqhelper.delay(20000);
//                return false;
            }
            String TempStr = "bcdefghijklmnopqrstuvwx";
            curUser=TempStr.charAt((int) (Math.random() * 23))+curQQ+"@dotabetter.com";
            //记录获取QQ数目
            jqhelper.writeSDFileEx("用户名：" + curQQ + "  密码：" + curPwd + "\n", "/sdcard/wbquhao.txt");
            return true;
        }
        catch (Exception ex){
            jqhelper.writeSDFileEx("异常startApp()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
            jqhelper.delay(20000);
            return false;
        }
    }

    //判断消息界面1
    private boolean ismessageActivity()
    {
        //注册 && 登录 && 消息
        UiObject firstMenu = new UiObject(new UiSelector().className("android.view.View").description("首页").index(0));
        UiObject messageMenu = new UiObject(new UiSelector().className("android.view.View").description("消息").index(1));
        UiObject displayMenu = new UiObject(new UiSelector().className("android.view.View").description("发现").index(3));
        UiObject mytextMenu = new UiObject(new UiSelector().className("android.view.View").description("我的资料").index(4));
        if(firstMenu.exists() && messageMenu.exists() && displayMenu.exists() && mytextMenu.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //消息界面1
     private void messageActivity()
     {
         try {
             //消息
             UiObject messageMenu = new UiObject(new UiSelector().className("android.view.View").description("消息").index(1));
             messageMenu.click();

             //登录
             UiObject loginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(1));
             loginBtn.clickAndWaitForNewWindow(20000);
         }
         catch (Exception ex)
         {
             jqhelper.writeSDFileEx("异常messageActivity()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
         }
     }

    //判断微博登录界面2
    private boolean isWeibLogin()
    {
        //登录 && 注册账号 && QQ 登录
       // UiObject rgLink = new UiObject(new UiSelector().textContains("注册账号").index(0));
        UiObject wbloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登录").index(2));
        UiObject qqloginLink = new UiObject(new UiSelector().className("android.widget.TextView").text("QQ 登录").index(2));
        if(wbloginBtn.exists() && qqloginLink.exists())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    //微博登录界面2
    private void WeibLogin()
    {
        try {
            //消息
            UiObject qqloginLink = new UiObject(new UiSelector().className("android.widget.TextView").text("QQ 登录").index(2));
            qqloginLink.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常WeibLogin()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断qq登录界面 3
    private boolean isqqLogin()
    {
        //QQ登录 && 登 录 && qq号输入框
        UiObject headerText = new UiObject(new UiSelector().className("android.widget.TextView").text("QQ登录").index(1));
        UiObject qqloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("登 录").index(1));
        UiObject userTextbox = new UiObject(new UiSelector().className("android.widget.EditText").index(0));
        if(headerText.exists() && qqloginBtn.exists() && userTextbox.exists())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    //qq登录界面 3
    private void qqLogin(String usr,String pwd)
    {
        try {
            //用户名
            UiEdit userTextbox = new UiEdit(new UiSelector().className("android.widget.EditText").index(0));
            userTextbox.setChineseText(usr);
            //密码
            userTextbox = new UiEdit(new UiSelector().className("android.widget.EditText").index(1));
            userTextbox.setChineseText(pwd);
            //登录
            UiObject qqloginLink = new UiObject(new UiSelector().className("android.widget.Button").text("登 录").index(1));
            qqloginLink.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常qqLogin()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断授权并登录4
    private boolean ispersistAndLogin()
    {
        UiObject qqloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("授权并登录").index(1));
        if(qqloginBtn.exists())
        {
            return true;
        }
        else {
            return false;
        }
    }

    //授权并登录4
    private void persistAndLogin()
    {
        try {
            //授权并登录
            UiObject qqloginBtn = new UiObject(new UiSelector().className("android.widget.Button").text("授权并登录").index(1));
            qqloginBtn.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常persistAndLogin()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断完善个人资料5
    private boolean isNextStep()
    {
        //完善个人资料
        UiObject nextlable = new UiObject(new UiSelector().text("完善个人资料").index(0));
        if(nextlable.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    // 完善个人资料5
    private void nextStep()
    {
        try {
            //个人资料是否为空
            UiEdit nextTextBox = new UiEdit(new UiSelector().className("android.widget.EditText").index(2));
            if(nextTextBox.getText().equals(""))
            {
                nextTextBox.setText(curUser);
            }

            //下一步
            UiObject nextBtn = new UiObject(new UiSelector().className("android.widget.Button").text("下一步").index(4));
            nextBtn.clickAndWaitForNewWindow(20000);
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常nextStep()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断关注感兴趣的人6
    private boolean isCarefull()
    {
         // 关注感兴趣的人  && 进入微博
        UiObject nextlable = new UiObject(new UiSelector().text("关注感兴趣的人").index(0));
        UiObject enterWeibtn = new UiObject(new UiSelector().className("android.widget.Button").text("进入微博").index(3));
        if(nextlable.exists() && enterWeibtn.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //关注感兴趣的人6(注意，该步骤一般没响应，需要点击左上角返回)
    private void carefull()
    {
        try {
            //进入微博
            UiObject enterWeibtn = new UiObject(new UiSelector().className("android.widget.Button").text("进入微博").index(3));
            enterWeibtn.click();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常carefull()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断进入微博7
    private boolean isenterWeibo()
    {
        //两幅图片
        UiObject imagelable = new UiObject(new UiSelector().text("android.widget.ImageView").index(0));
        UiObject imageBtn = new UiObject(new UiSelector().className("android.widget.ImageView").index(1));
        if(imagelable.exists() && imageBtn.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //进入微博7
    private void enterWeibo()
    {
        try {
            // //进入微博
            int k=0;
            do {
                k+=1;
                if(k<3) {
                    UiObject enterWeibtn = new UiObject(new UiSelector().className("android.widget.ImageView").index(1));
                    enterWeibtn.click();
                }
                else {//返回键
                    UiObject topbackBtn = new UiObject(new UiSelector().className("android.widget.TextView").descriptionContains("返回").index(0));
                    topbackBtn.click();
                }
                jqhelper.delay(8000);
            }while (isenterWeibo()==true && k<5);
        }
         catch (Exception ex) {
             jqhelper.writeSDFileEx("异常enterWeibo()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
         }
    }

    //判断未关注人消息 -->首页8
    private boolean isnotcareMessageFistPage()
    {
        //首页 && 消息 && 发现
        UiObject firstPageMenu = new UiObject(new UiSelector().text("android.view.View").description("首页").index(0));
        UiObject messageMenu = new UiObject(new UiSelector().className("android.view.View").description("消息").index(1));
        UiObject displayMenu = new UiObject(new UiSelector().className("android.view.View").description("发现").index(3));
        if(firstPageMenu.exists() && messageMenu.exists() && displayMenu.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //未关注人消息 -->首页8
    private void notcareMessageFistPage()
    {
        try {
            UiObject messageMenu = new UiObject(new UiSelector().className("android.view.View").description("消息").index(1));
            messageMenu.click();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常notcareMessageFistPage()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }

    //判断未关注人消息-》消息9
    private int isnotcareMessage()
    {
        //消息 && 未关注人消息
        UiObject topMenu1 = new UiObject(new UiSelector().className("android.view.View").text("发现群").index(0));
        UiObject topMenu2 = new UiObject(new UiSelector().className("android.view.View").text("消息").index(0));
        UiObject topMenu3 = new UiObject(new UiSelector().className("android.view.View").text("发起聊天").index(0));
        UiObject lableText = new UiObject(new UiSelector().className("android.widget.TextView").text("评论"));
        UiObject nodisplayMenu = new UiObject(new UiSelector().className("android.widget.TextView").textContains("未关注人消息").index(0));
        if(topMenu1.exists() && topMenu2.exists() && topMenu3.exists() && lableText.exists())
        {
            if(nodisplayMenu.exists()==true)
            {
                return 2;
            }
            return 1;
        }
        else
        {
            return 0;
        }
    }

    //未关注人消息-》消息9
    private void notcareMessage()
    {
        try {
            UiObject nodisplayMenu = new UiObject(new UiSelector().className("android.widget.TextView").text("未关注人消息").index(0));
            nodisplayMenu.click();
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常notcareMessage()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }


    //判断http消息链接10
    private boolean ishttpLink()
    {
        //http消息
        UiObject httpLink = new UiObject(new UiSelector().className("android.widget.TextView").textContains("http:").index(0));
        if(httpLink.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //http消息链接10
    private void httpLink()
    {
        try {
            //http消息
            UiObject httpLink = new UiObject(new UiSelector().className("android.widget.TextView").textContains("http:").index(0));
            String str = httpLink.getText();
            String str2 = str.replaceFirst("您已通过QQ登录，请设置用户登录名和密码。", "");
            if(str2.equals("")==false) {
                jqhelper.httpGetString(str2);
            }
        }
        catch (Exception ex) {
            jqhelper.writeSDFileEx("异常notcareMessage()：" + ex.toString() + " \n", "/sdcard/wbyc.txt");
        }
    }


}
