package com.douyin;

import android.os.RemoteException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;
import com.common.helper.HttpHelper;
import com.common.jutf7.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class executePcStore extends UiAutomatorTestCase {
    private static String execute = "";
    private static String executePath = "/storage/emulated/0/QX_Backup/shellcmd/execute.txt";
    // 项目
    private static String prkjectPath = "/storage/emulated/0/QX_Backup/shellcmd/project.txt";
    //
    private static String prkjectsPath = "/storage/emulated/0/QX_Backup/shellcmd/projects.txt";
    // my pc
    private static String httpUrl = "http://218.93.102.26:8899/execute/";
    // servers
//    private static String httpUrl = "http://:8899/execute/";

    public void testMain() throws IOException, UiObjectNotFoundException, RemoteException {
        while (true) {
            try {
                // TODO: CLOSE BJ
                closeBJ();
                // TODO: JUGDE BY EXESTATUS
                execute = readFile(executePath);
                if (execute.equals("0")) {
                    String projectAndSearchWord;
                    String project;
                    String searchWords;
                    projectAndSearchWord = readFile(prkjectPath);
                    String[] strArray = projectAndSearchWord.split(":");
                    project = strArray[0];
                    searchWords = strArray[1];
                    appExecute(project, searchWords);
                    changeSystem();
                } else if (execute.equals("1")) {
                    // TODO: GET PROJECT
                    String projectAndSearchWord;
                    String project;
                    // TODO
                    projectAndSearchWord = getProjectAndSearchWord(httpUrl);
                    String[] strArray = projectAndSearchWord.split(":");
                    project = strArray[0];
                    // TODO: GET BACKUP NAME BY PROJECT
                    String backupName = getBackupName(project);
                    // TODO: SET GET DATA WAIT TIME
                    int waitTime = 120;
                    // TODO: GET BACKUP DATA BY SERVER
                    String status = getBackupData(backupName, httpUrl, waitTime);
                    status = status.replaceAll("[\\t\\n\\r]", "");
                    if (status.equals("true")) {
                        String reductionStatus = reductionPc(backupName, projectAndSearchWord);
                        if (reductionStatus.equals("exception")) {
                            System.out.println("REDUCTION IS OVER, BUT IT IS EXCEPTION");
                        }
                    } else if (status.equals("false")) {
                        System.out.println("GET BACKUP DATA AGAIN");
                        status = getBackupData(backupName, httpUrl, waitTime);
                        if (status.equals("true")) {
                            String reductionStatus = reductionPc(backupName, projectAndSearchWord);
                            if (reductionStatus.equals("exception")) {
                                System.out.println("REDUCTION IS OVER, BUT IT IS EXCEPTION");
                            }
                        }
                    } else {
                        System.out.println("GET BACKUP DATA BY HTTP SERVER IS FAILED");
                        System.out.println("GET BACKUP DATA AGAIN");
                        status = getBackupData(backupName, httpUrl, waitTime);
                        if (status.equals("true")) {
                            String reductionStatus = reductionPc(backupName, projectAndSearchWord);
                            if (reductionStatus.equals("exception")) {
                                System.out.println("REDUCTION IS OVER, BUT IT IS EXCEPTION");
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("MAIN PROGRAM IS EXCEPTION : " + ex.toString());
            }
        }
    }

//    public void test_test() throws IOException, UiObjectNotFoundException, RemoteException {
////    }

    private void appExecute(String project, String searchWords) throws IOException, RemoteException, UiObjectNotFoundException {
        if (project.equals("TTXL")) {
            toutiaoSearch(project, searchWords);
        } else if (project.equals("DYXL")) {
            douyinSearchWord(project, searchWords);
        } else if (project.equals("DYPM")) {
            douyinSearchUser(project, searchWords);
        }
    }

    private void toutiaoSearch(String project, String searchWords) throws IOException, RemoteException, UiObjectNotFoundException {
        String backupName = getBackupName(project);
        String appPackage = getAppPackage(backupName);
        String appActivity = getAppActivity(backupName);
        UiDevice device = getUiDevice();
        try {
            // TODO: OPEN APP
            openApp(appActivity, 15);

            // TODO: CLEAR USELESS WINDOWS
            toutiaoUpdateVersion();

            for (int i = 0; i < 3; i++) {
                clickAllowButton();
            }

            UiObject searchBox = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/bph"));
            if (searchBox.exists()) {
                changeUtf7InputMethod();
                searchBox.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                device.click(500, 155);
                sleep(5000);
            }

            String[] wordsArray = searchWords.split(";");
            for (int i = 0; i < wordsArray.length; i++) {
                String word = wordsArray[i];
                UiObject editBox = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/w8"));
                if (editBox.exists()) {
//                    changeInputMethod();
                    editBox.setText(Utf7ImeHelper.e(word));
                    sleep(10 * 1000);

                    UiObject searchButton = new UiObject(new UiSelector().className("android.widget.TextView").text("搜索"));
                    for (int j = 0; j < 10; j++) {
                        if (searchButton.exists()) {
                            searchButton.clickAndWaitForNewWindow(3000);
                            sleep(20 * 1000);
                            break;
                        }
                        sleep(3000);
                        searchButton = new UiObject(new UiSelector().className("android.widget.TextView").text("搜索"));
                    }
                }

                // TODO: READ NEWS
                UiScrollable scroll = new UiScrollable(new UiSelector().className("android.view.View"));
                if (scroll.exists()) {
                    System.out.println("scroll is exists");
                    scroll.setSwipeDeadZonePercentage(0.2);
                    for (int k = 0; k < 5; k++) {
                        scroll.scrollForward();
                        sleep(10 * 1000);
                    }
                }

                // TODO: DELETE SEARCH WORD
                UiObject deleteButton = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("清除"));
                if (deleteButton.exists()) {
                    System.out.println("DELETE BUTTON IS EXISTS");
                    deleteButton.clickAndWaitForNewWindow(3000);
                    sleep(6000);
                }

            }

            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");
        } catch (Exception ex) {
            System.out.println("TOUTIAO SEARCH IS EXCEPTION : " + ex.toString());
            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");
        }
    }

    private void douyinSearchWord(String project, String searchWords) throws IOException {
        String backupName = getBackupName(project);
        String appPackage = getAppPackage(backupName);
        String appActivity = getAppActivity(backupName);
        UiDevice device = getUiDevice();
        try {
            // TODO: OPEN APP
            openApp(appActivity, 15);

            // TODO: CLEAR USELESS WINDOWS
            UiObject personalPrivacy = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/title").text("个人信息保护指引"));
            if (personalPrivacy.exists()) {
                UiObject okButton = new UiObject(new UiSelector().className("android.widget.TextView").text("好的"));
                if (okButton.exists()) {
                    okButton.clickAndWaitForNewWindow(3000);
                    sleep(3000);
                }
            }

            for (int i = 0; i < 3; i++) {
                clickAllowButton();
            }

            mScrollForward();
            sleep(10);
            mScrollForward();
            sleep(10);

            UiObject searchBox = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/ay8"));
            if (searchBox.exists()) {
                searchBox.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                device.click(1000, 130);
                sleep(5000);
            }
            String[] wordsArray = searchWords.split(";");
            for (int i = 0; i < wordsArray.length; i++) {
                String word = wordsArray[i];
                UiObject editBox = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/ahx"));
                if (editBox.exists()) {
                    changeUtf7InputMethod();
                    sleep(2000);
                    editBox.setText(Utf7ImeHelper.e(word));
                    sleep(2000);
                    changeAOSPInputMethod();
                    sleep(2000);
                    device.click(1000, 1825);
                    sleep(15 * 1000);
                }

                for (int j = 0; j < 5; j++) {
                    mScrollForward();
                    sleep(10);
                }

                // TODO: DELETE SEARCH WORD
                UiObject deleteButton = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/qf"));
                if (deleteButton.exists()) {
                    System.out.println("DELETE BUTTON IS EXISTS");
                    deleteButton.clickAndWaitForNewWindow(3000);
                    sleep(3000);
                } else {
                    System.out.println("DELETE BUTTON IS NOT FOUND, CLICK 850, 150");
                    device.click(850, 150);
                    sleep(3000);
                }
            }

            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");

        } catch (Exception ex) {
            System.out.println("DOUYIN SEARCH PROGRAM IS EXCEPTION : " + ex.toString());
            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");
        }
    }

    private void douyinSearchUser(String project, String usersInfo) throws IOException {
        String backupName = getBackupName(project);
        String appPackage = getAppPackage(backupName);
        String appActivity = getAppActivity(backupName);
        UiDevice device = getUiDevice();
        try {
            // TODO: OPEN APP
            openApp(appActivity, 15);

            // TODO: CLEAR USELESS WINDOWS
            UiObject personalPrivacy = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/title").text("个人信息保护指引"));
            for (int l = 0; l < 10; l++) {
                if (personalPrivacy.exists()) {
                    UiObject okButton = new UiObject(new UiSelector().className("android.widget.TextView").text("好的"));
                    if (okButton.exists()) {
                        okButton.clickAndWaitForNewWindow(3000);
                        sleep(3000);
                    } else {
                        System.out.println("click x,y");
                    }
                    break;
                }
                sleep(3 * 1000);
                personalPrivacy = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/title").text("个人信息保护指引"));
            }

            for (int i = 0; i < 3; i++) {
                clickAllowButton();
            }

            mScrollForward();
            sleep(10);
            mScrollForward();
            sleep(10);

            UiObject searchButton = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/ay8"));
            if (searchButton.exists()) {
                searchButton.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                device.click(1000, 130);
                sleep(5000);
            }


            String[] usersInfoArry = usersInfo.split(";");
            for (int i = 0; i < usersInfoArry.length; i++) {
                String userInfo = usersInfoArry[i];
                String[] userInfoArry = userInfo.split("/");
                String username = userInfoArry[0];
                String douyinNum = userInfoArry[1];

                for (int j = 0; j < 2; j++) {
                    UiObject editBox = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/ahx"));
                    if (editBox.exists()) {
                        changeUtf7InputMethod();
                        sleep(2000);
                        editBox.setText(Utf7ImeHelper.e(username));
                        sleep(2000);
                        changeAOSPInputMethod();
                        sleep(2000);
                        device.click(1000, 1825);
                        sleep(10 * 1000);
                    }

                    // USER BUTTON
                    UiObject userButoon = new UiObject(new UiSelector().className("android.widget.TextView").text("用户"));
                    if (userButoon.exists()) {
                        userButoon.clickAndWaitForNewWindow(3000);
                        sleep(5000);
                    } else {
                        device.click(514, 286);
                        sleep(5000);
                    }

                    // SCROLL BUTTON
                    UiScrollable scroll_btn = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
                    scroll_btn.scrollTextIntoView("抖音号:" + douyinNum);
                    sleep(2000);

                    // DOUYIN NUMBER
                    UiObject douyinNumButton = new UiObject(new UiSelector().className("android.widget.TextView").text("抖音号:" + douyinNum));
                    if (douyinNumButton.exists()) {
                        douyinNumButton.clickAndWaitForNewWindow(3000);
                        sleep(8 * 1000);
                    }

                    // VIDEO BUTTON
                    UiObject videoButton = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/abo"));
                    if (videoButton.exists()) {
                        videoButton.clickAndWaitForNewWindow(3000);
                        sleep(30 * 1000);
                    }

                    for (int k = 0; k < 2; k++) {
                        mScrollForward();
                        sleep(30 * 1000);
                    }

                    // TODO: SEARCH BUTTON X,Y
                    device.click(991, 133);
                    sleep(5000);

//                // TODO: DELETE SEARCH WORD
//                UiObject deleteButton = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/qf"));
//                if (deleteButton.exists()) {
//                    System.out.println("DELETE BUTTON IS EXISTS");
//                    deleteButton.clickAndWaitForNewWindow(3000);
//                    sleep(3000);
//                } else {
//                    System.out.println("DELETE BUTTON IS NOT FOUND, CLICK 850, 150");
//                    device.click(850, 150);
//                    sleep(3000);
//                }
                }
            }

            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");

        } catch (Exception ex) {
            System.out.println("DOUYIN SEARCH PROGRAM IS EXCEPTION : " + ex.toString());
            closeApp(appPackage, 3);
            clearData(appPackage);
            writerFile(executePath, "1");
        }
    }

    public String reductionPc(String backupName, String projectAndSearchWord) {
        String reductionStatus = null;
        try {
            openBJ();
            String appVersion = getAppVersion(backupName);
            String reductionName = backupName + '_' + appVersion;

            UiObject reductionButton = new UiObject(new UiSelector().className("android.widget.TextView").text("一键还原"));
            if (reductionButton.exists()) {
                reductionButton.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject appButton = new UiObject(new UiSelector().resourceId("com.littlerich.holobackup:id/name").text(reductionName));
            if (appButton.exists()) {
                appButton.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject dataReductionButton = new UiObject(new UiSelector().className("android.widget.TextView").text("数据还原"));
            if (dataReductionButton.exists()) {
                dataReductionButton.clickAndWaitForNewWindow(3000);
                sleep(2000);
                UiObject MessageBox = new UiObject(new UiSelector().resourceId("com.littlerich.holobackup:id/alertTitle"));
                for (int i = 0; i < 60; i++) {
                    if (MessageBox.exists()) {
                        String message = MessageBox.getText();
                        if (message.contains("成功")) {
                            System.out.println("REDUCTION IS SUCCESS!");
                            reductionStatus = "true";
                            writerFile(executePath, "0");
                            writerFile(prkjectPath, projectAndSearchWord);
                        } else if (message.contains("失败")) {
                            System.out.println("REDUCTION IS FAILED!");
                            reductionStatus = "false";
                        } else {
                            System.out.println("REDUCTION IS FALSE!");
                            reductionStatus = "false";
                        }
                        break;
                    }
                    sleep(5 * 1000);
                    MessageBox = new UiObject(new UiSelector().resourceId("com.littlerich.holobackup:id/alertTitle"));
                }
            }

            UiObject yesButton = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (yesButton.exists()) {
                yesButton.clickAndWaitForNewWindow(3000);
                sleep(2000);
                System.out.println("REDUCTION IS FINISH!");
            } else {
                reductionStatus = "exception";
                System.out.println("YES BUTTON IS NOT FOUND");
            }
        } catch (Exception ex) {
            System.out.println("REDUCTION IS EXCEPTION : " + ex.toString());
            reductionStatus = "exception";
        }
        return reductionStatus;
    }

    public String getAppVersion(String backupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("wb", "9.10.2");
        map.put("dy", "10.0.0");
        map.put("ks", "");
        map.put("tt", "7.6.1");
        map.put("xhs", "6.15.0");
        return map.get(backupName).toString();
    }

    public String getAppActivity(String backupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("wb", "com.sina.weibo/.SplashActivity");
        map.put("dy", "com.ss.android.ugc.aweme/.splash.SplashActivity");
        map.put("ks", "");
        map.put("tt", "com.ss.android.article.news/.activity.MainActivity");
        map.put("xhs", "com.xingin.xhs.activity.SplashActivity");
        return map.get(backupName).toString();
    }

    public String getAppPackage(String backupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("wb", "com.sina.weibo");
        map.put("dy", "com.ss.android.ugc.aweme");
        map.put("ks", "快手");
        map.put("tt", "com.ss.android.article.news");
        map.put("xhs", "com.xingin.xhs");
        return map.get(backupName).toString();
    }

    public String getBackupName(String project) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("TTXL", "tt");
        map.put("DYXL", "dy");
        map.put("DYPM", "dy");
        return map.get(project).toString();
    }

    public String getAppName(String backupName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("wb", "微博");
        map.put("dy", "抖音短视频");
        map.put("ks", "快手");
        map.put("tt", "今日头条");
        map.put("xhs", "小红书");
        return map.get(backupName).toString();
    }

    private void toutiaoUpdateVersion() throws UiObjectNotFoundException {
        // TODO: UPDATE VERSION WINDOWS
        UiObject updateVersion = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
        if (updateVersion.exists()) {
            System.out.println("update_version is exists");
            updateVersion.clickAndWaitForNewWindow(3000);
            sleep(3000);
        }
    }

    private void clickAllowButton() throws UiObjectNotFoundException {
        // TODO: UPDATE VERSION WINDOWS
        UiObject allowButton = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
        if (allowButton.exists()) {
            System.out.println("update_version is exists");
            allowButton.clickAndWaitForNewWindow(3000);
            sleep(3000);
        }
    }

    private void changeSystem() {
        try {
            openBJ();
            UiObject moreButton = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("更多选项"));
            if (moreButton.exists()) {
                moreButton.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject changeButton = new UiObject(new UiSelector().className("android.widget.TextView").text("一键变机"));
            if (changeButton.exists()) {
                changeButton.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }

            UiObject yesButton = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (yesButton.exists()) {
                yesButton.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }

        } catch (Exception ex) {
            System.out.println("Change system is exception：" + ex.toString());
        }
    }

    private void changeUtf7InputMethod() throws IOException {
        // TODO: change input method
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
        jqhelper.delay(2000);
    }

    private void changeAOSPInputMethod() throws IOException {
        // TODO: change input method
        Runtime.getRuntime().exec("settings put secure default_input_method com.android.inputmethod.latin/.LatinIME");
        jqhelper.delay(2000);
    }

    private void openApp(String appActivity, int second) throws RemoteException, IOException {
        UiDevice device = getUiDevice();
        device.wakeUp();
        sleep(1000);
        device.pressHome();
        sleep(1000);
        execShellCmd("am start " + appActivity);
        sleep(second * 1000);
    }

    private void closeApp(String appPackage, int second) throws IOException {
        UiDevice device = getUiDevice();
        device.pressHome();
        sleep(2000);
        execShellCmd("am force-stop " + appPackage);
        jqhelper.delay(second * 1000);
    }

    private void openBJ() throws RemoteException {
        UiDevice device = getUiDevice();
        device.wakeUp();
        sleep(1000);
        device.pressHome();
        sleep(1000);
        execShellCmd("am start " + "com.littlerich.holobackup/.MainActivity");
        sleep(5 * 1000);
        UiObject backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键备份"));
        for (int i = 0; i < 15; i++) {
            if (backup_btn.exists()) {
                break;
            }
            backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键备份"));
            sleep(2000);
        }
    }

    private void closeBJ() throws IOException {
        UiDevice device = getUiDevice();
        device.pressHome();
        sleep(2000);
        // TODO: close bj apk
        Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
        jqhelper.delay(1000);
    }

    private void clearData(String appPackage) throws IOException {
        execShellCmd("pm clear " + appPackage);
        jqhelper.delay(4000);
    }

    private void mScrollForward() {
        UiDevice device = getUiDevice();
        Random rd = new Random();
        int num = rd.nextInt(100);
        int startX = num + 450;
        int startY = num + 1400;
        int endX = num + 480;
        int endY = num + 530;
        int steps = 10;
        device.swipe(startX, startY, endX, endY, steps);
        sleep(3000);
    }

    private static void execShellCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.write(cmd.getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String execCMD(String command) {
        StringBuilder sb = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
//                sb.append(line + "\n");
                sb.append(line);
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    public static String postData(String Data, String httpUrl, int waitTime) {
        String Sew = "";
        //捕获异常
        try {
//            Sew = HttpHelper.httpPostForString(httpUrl, Data);
            URL url = new URL(httpUrl);

            //HttpURLConnection 打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //设置POST访问
            conn.setRequestMethod("POST");
            //超时等待
            conn.setReadTimeout(waitTime * 1000);
            //可读取为真
            conn.setDoInput(true);
            //可发送数据为真
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            //打开输出流操作
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            //发送数据
            out.write(Data);
            //刷新缓存数据
            out.flush();

            //判断是否发送成功
            if (conn.getResponseCode() == 200) {

                //BufferedReader 用于查看返回数据的实例化输出变换
                BufferedReader Ne = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String Sen = "空";
//                String Sew = "";

                //循环   注意：此处Ne.readLine()用一次便是一行
                /*
                 * 之前这么写的
                 * while(Ne.readLine!=null){
                 * 		System.out.printf(readLine());
                 * }
                 *
                 *
                 * 结果发现不行，因为循环的时候执行过一次readLine函数
                 * 在输出的时候就是下一行了 而不是循环时判断的那一行
                 */
                while ((Sen = Ne.readLine()) != null) {
                    Sew += Sen;
                }
                System.out.println("****: " + Sew);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Sew;
    }

    private static ArrayList<String> getTaskByServer(String httpUrl) {
        ArrayList<String> taskList = new ArrayList<String>();
        try {
            String result = HttpHelper.httpGetString(httpUrl);
            if (!result.equals("")) {
                System.out.println(result);
                String[] words_array = result.split("&");
                for (int i = 0; i < words_array.length; i++) {
                    taskList.add(words_array[i]);
                }
            } else {
                System.out.println(httpUrl + "：  url返回数据为空");
            }

//            System.out.println(taskList);
//            URL url = new URL(httpUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(15000);
//            connection.setReadTimeout(60000);
//            connection.connect();
//            if (connection.getResponseCode() == 200) {
//                is = connection.getInputStream();
//                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                StringBuffer sbf = new StringBuffer();
//                String temp = null;
//                while ((temp = br.readLine()) != null) {
//                    sbf.append(temp);
//                    sbf.append("\r\n");
//                }
//                result = sbf.toString();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public static String getProjectAndSearchWord(String httpUrl) throws IOException {
        // TODO: get file path
        File file = new File(prkjectsPath);
        String project = null;
        ArrayList<String> taskList = null;

        if (file.exists() && file.length() == 0) {
            // TODO: save project to txt by http server
            taskList = getTaskByServer(httpUrl);
            System.out.println("taskList : " + taskList);
            FileOutputStream fileOutputStream = new FileOutputStream(prkjectsPath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);
            for (int j = 0; j < taskList.size(); j++) {
                bw.write(taskList.get(j).toString() + "\r\n");
                System.out.println("writer project to txt: " + taskList.get(j) + " " + Integer.toString(j));
            }
            bw.flush();
            bw.close();

            // TODO: get project
            FileInputStream src = new FileInputStream(prkjectsPath);
            InputStreamReader ir = new InputStreamReader(src);
            BufferedReader br = new BufferedReader(ir);
            String str = null;
            ArrayList<String> list = new ArrayList<String>();
            int i = -1;
            while ((str = br.readLine()) != null) {
                list.add(str.toString() + "\r\n");
            }
            // TODO: get fist content and remove it
            project = list.get(0).toString().trim();
            list.remove(0);

            // TODO: writer project to txt again
            if (list == null || list.isEmpty()) {
                System.out.println("list is empty");

                // TODO: save project again
                // TODO: save project to txt by http server
                taskList = getTaskByServer(httpUrl);
                System.out.println("project_list" + taskList);
                fileOutputStream = new FileOutputStream(prkjectsPath);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                bw = new BufferedWriter(outputStreamWriter);
                for (int j = 0; j < taskList.size(); j++) {
                    bw.write(taskList.get(j).toString() + "\r\n");
                    System.out.println("writer project to txt: " + taskList.get(j) + " " + Integer.toString(j));
                }
                bw.flush();
                bw.close();
            } else {
                fileOutputStream = new FileOutputStream(prkjectsPath);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                bw = new BufferedWriter(outputStreamWriter);

                for (int j = 0; j < list.size(); j++) {
                    bw.write(list.get(j).toString());
                }
                bw.flush();
                bw.close();
            }
            return project;
        } else {
            System.out.println("file is exists and have content");
            FileInputStream src = new FileInputStream(prkjectsPath);
            InputStreamReader ir = new InputStreamReader(src);
            BufferedReader br = new BufferedReader(ir);
            String str = null;
            ArrayList<String> list = new ArrayList<String>();
            int i = -1;
            while ((str = br.readLine()) != null) {
                list.add(str.toString() + "\r\n");
            }
            // TODO: get fist content and remove it
            project = list.get(0).toString().trim();
            list.remove(0);

            // TODO: writer project to txt again
            if (list == null || list.isEmpty()) {
                System.out.println("list is empty");

                // TODO: save project again
                // TODO: save project to txt by http server
                taskList = getTaskByServer(httpUrl);
                System.out.println("project_list" + taskList);
                FileOutputStream fileOutputStream = new FileOutputStream(prkjectsPath);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                for (int j = 0; j < taskList.size(); j++) {
                    bw.write(taskList.get(j).toString() + "\r\n");
                    System.out.println("writer project to txt: " + taskList.get(j) + " " + Integer.toString(j));
                }
                bw.flush();
                bw.close();
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(prkjectsPath);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                BufferedWriter bw = new BufferedWriter(outputStreamWriter);

                for (int j = 0; j < list.size(); j++) {
                    bw.write(list.get(j).toString());
                }
                bw.flush();
                bw.close();
            }
            return project;
        }
    }

    public String getBackupData(String backupName, String httpUrl, int waitTime) {
        String status;
        try {
            // TODO: GET SN
            String devicesId = execCMD("getprop ro.serialno");
            System.out.println("devicesId: " + devicesId);

            // TODO: GET DIRECTORY NAME
            String directoryName = getDirectoryName(backupName);
            System.out.println("***directoryName***: " + directoryName);

            // TODO: GET BACKUP DATA BY HTTP SERVER
//            String postData = String.format("{\"backupName\":\"%s\",\"devicesId\":\"%s\",\"directoryName\":\"%s\"}", new Object[]{backupName, devicesId, directoryName});
            String data = String.format("backupName=%s&devicesId=%s&directoryName=%s", backupName, devicesId, directoryName);
            status = postData(data, httpUrl, waitTime);
        } catch (Exception ex) {
            return ex.toString();
        }
        return status.toString();
    }

    public String getDirectoryName(String backupName) {
        String directoryName;
        try {
            String hashStr = SHA256(backupName);
            String appPackage = getAppPackage(backupName);
            String appVersion = getAppVersion(backupName);
            directoryName = appPackage + "-" + hashStr.substring(0, 5) + "_" + appVersion;
        } catch (Exception ex) {
            return ex.toString();
        }
        return directoryName.toString();
    }

    public String readFile(String file_path) {
        File file = new File(file_path);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static void writerFile(String filePath, String content) {
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @return
     */
    private String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    /**
     * 传入文本内容，返回 SHA-256 串
     *
     * @param strText
     * @return
     */
    public String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param strText
     * @return
     */
    public String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }
}


