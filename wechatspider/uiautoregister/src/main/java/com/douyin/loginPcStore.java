package com.douyin;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.base.jqhelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class loginPcStore extends UiAutomatorTestCase {
    private static String login_status_weibo = "normal";
    private static String login_status_douyin = "normal";
    private static String login_status_toutiao = "normal";
    private static String backup_status = "normal";
    private static String message_function_status = "false";

    private static String weibo_account = "";
    private static ArrayList<String> backup_list;
    private static String apk_path_toutiao = "/sdcard/toutiao_v7.6.1.apk";
    private static String apk_store_path = "/storage/emulated/0/QX_Backup/App/";

    private static String bj_package = "com.littlerich.holobackup";
    private static String bj_activity = "com.littlerich.holobackup/.MainActivity";

    private static String weibo_project = "wb";
    private static String weibo_version = "9.10.2";
    private static String weibo_package = "com.sina.weibo";
    private static String weibo_activity = "com.sina.weibo/.SplashActivity";
    private static int weibo_sleep_second = 30;

    private static String douyin_project = "dy";
    private static String douyin_version = "10.0.0";
    private static String douyin_package = "com.ss.android.ugc.aweme";
    private static String douyin_activity = "com.ss.android.ugc.aweme/.splash.SplashActivity";
    private static int douyin_sleep_second = 100;

    private static String toutiao_project = "tt";
    private static String toutiao_version = "7.6.1";
    private static String toutiao_package = "com.ss.android.article.news";
    private static String toutiao_activity = "com.ss.android.article.news/.activity.MainActivity";
    private static int toutiao_sleep_second = 100;

    private static String httpUrl = "http://114.226.193.13:8889/fileStore/";

    public void test_backup() throws UiObjectNotFoundException, RemoteException, IOException {
        while (true) {
            try {
                // TODO: CLOSE BJ
                close_app(bj_package, 3);
                // TODO: LOGIN WEIBO
                login_weibo();
                // TODO: CHECK LOGIN WEIBO
                check_weibo();
                if (login_status_weibo.equals("true")) {
                    // TODO: CLOSE WEIBO
                    close_app(weibo_package, 3);
                    // TODO: LOGIN DOUYIN
                    login_douyin();
                    // TODO: CHECK LOGIN DOUYIN
                    if (login_status_douyin.equals("true")) {
                        // TODO: INSTALL TOUTIAO
                        install_app(apk_path_toutiao, 25);
                        // TODO: LOGIN TOUTIAO
                        login_toutiao();
                        if (login_status_toutiao.equals("true")) {
//                                // TODO: TOUTIAO GROW
//                                toutiao_grow();
//                                // TODO: CHECK MESSAGE
//                                check_message_function();
                            // TODO: CLOSE TOUTIAO
                            close_app(toutiao_package, 3);
                            // TODO: BACKUP ALL APP DATA
                            // TODO: BACKUP WEIBO
                            backup(weibo_project);
                            close_app(bj_package, 3);
                            if (backup_status.equals("true")) {
                                post_data(weibo_project, weibo_package, weibo_version, backup_status, weibo_account, weibo_sleep_second);
                            } else {
                                System.out.println("Weibo backup is fail");
                            }
                            backup_status = "normal";
                            // TODO: BACKUP DOUYIN
                            backup(douyin_project);
                            close_app(bj_package, 3);
                            if (backup_status.equals("true")) {
                                post_data(douyin_project, douyin_package, douyin_version, backup_status, weibo_account, douyin_sleep_second);
                            } else {
                                System.out.println("Douyin backup is fail");
                            }
                            backup_status = "normal";
                            // TODO: BACKUP TOUTIAO
                            backup(toutiao_project);
                            close_app(bj_package, 3);
                            if (backup_status.equals("true")) {
                                post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                            } else {
                                System.out.println("Toutiao backup is fail, account callback to redis");
                                post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                            }

                            // TODO: CLEAR DATA
                            clear_data(weibo_package);
                            clear_data(douyin_package);
                            clear_data(toutiao_package);
                            // TODO: UNINSTLL APP
                            uninstall_app(toutiao_package, 10);
                            // TODO: CHANGE SYSTEM
                            change_system();
                        } else {
                            // TODO: CLOSE TOUTIAO
                            close_app(toutiao_package, 3);
                            // TODO: CLEAR TOUTIAO
                            clear_data(toutiao_package);
                            // TODO: L0GIN TOUTIAO
                            login_toutiao();
                            if (login_status_toutiao.equals("true")) {
//                                    // TODO: TOUTIAO GROW
//                                    toutiao_grow();
//                                    // TODO: CHECK MESSAGE
//                                    check_message_function();
                                // TODO: CLOSE TOUTIAO
                                close_app(toutiao_package, 3);
                                // TODO: BACKUP
                                // TODO: BACKUP ALL APP DATA
                                // TODO: BACKUP WEIBO
                                backup(weibo_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(weibo_project, weibo_package, weibo_version, backup_status, weibo_account, weibo_sleep_second);
                                } else {
                                    System.out.println("Weibo backup is fail");
                                }
                                backup_status = "normal";
                                // TODO: BACKUP DOUYIN
                                backup(douyin_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(douyin_project, douyin_package, douyin_version, backup_status, weibo_account, douyin_sleep_second);
                                } else {
                                    System.out.println("Douyin backup is fail");
                                }
                                backup_status = "normal";
                                // TODO: BACKUP TOUTIAO
                                backup(toutiao_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                } else {
                                    System.out.println("Toutiao backup is fail, account callback to redis");
                                    post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                }
                                // TODO: CLEAR DATA
                                clear_data(weibo_package);
                                clear_data(douyin_package);
                                clear_data(toutiao_package);
                                // TODO: UNINSTLL APP
                                uninstall_app(toutiao_package, 10);
                                // TODO: CHANGE SYSTEM
                                change_system();
                            } else {
                                System.out.println("Login toutiao is fail, pass");
                                // TODO: CLOSE TOUTIAO
                                close_app(toutiao_package, 3);
                                // TODO: CLEAR TOUTIAO
                                clear_data(toutiao_package);
                                // TODO: CLOSE DOUYIN
                                close_app(douyin_package, 3);
                                // TODO: CLEAR DOUYIN
                                clear_data(douyin_package);
                                // TODO: CLOSE WEIBO
                                close_app(weibo_package, 3);
                                // TODO: CLEAR WEIBO
                                clear_data(weibo_package);
                                // TODO: UNINSTLL APP
                                uninstall_app(toutiao_package, 10);
                                // TODO: ACCOUNT SUBMIT TO REDIS
                                post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                // TODO: CHANGE SYSTEM
                                change_system();
                            }
                        }
                    } else {
                        // TODO: CLEAR DOUYIN DATA
                        clear_data(douyin_package);
                        // TODO: LOGIN DOUYIN
                        login_douyin();
                        // TODO: CHECK LOGIN DOUYIN
                        if (login_status_douyin.equals("true")) {
                            // TODO: INSTALL TOUTIAO
                            install_app(apk_path_toutiao, 25);
                            // TODO: LOGIN TOUTIAO
                            login_toutiao();
                            if (login_status_toutiao.equals("true")) {
//                                    // TODO: TOUTIAO GROW
//                                    toutiao_grow();
//                                    // TODO: CHECK MESSAGE
//                                    check_message_function();
                                // TODO: CLOSE TOUTIAO
                                close_app(toutiao_package, 3);
                                // TODO: BACKUP ALL APP DATA
                                // TODO: BACKUP WEIBO
                                backup(weibo_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(weibo_project, weibo_package, weibo_version, backup_status, weibo_account, weibo_sleep_second);
                                } else {
                                    System.out.println("Weibo backup is fail");
                                }
                                backup_status = "normal";
                                // TODO: BACKUP DOUYIN
                                backup(douyin_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(douyin_project, douyin_package, douyin_version, backup_status, weibo_account, douyin_sleep_second);
                                } else {
                                    System.out.println("Douyin backup is fail");
                                }
                                backup_status = "normal";
                                // TODO: BACKUP TOUTIAO
                                backup(toutiao_project);
                                close_app(bj_package, 3);
                                if (backup_status.equals("true")) {
                                    post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                } else {
                                    System.out.println("Toutiao backup is fail, account callback to redis");
                                    post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                }
                                // TODO: CLEAR DATA
                                clear_data(weibo_package);
                                clear_data(douyin_package);
                                clear_data(toutiao_package);
                                // TODO: UNINSTLL APP
                                uninstall_app(toutiao_package, 10);
                                // TODO: CHANGE SYSTEM
                                change_system();
                            } else {
                                // TODO: CLOSE TOUTIAO
                                close_app(toutiao_package, 3);
                                // TODO: CLEAR TOUTIAO
                                clear_data(toutiao_package);
                                // TODO: L0GIN TOUTIAO
                                login_toutiao();
                                if (login_status_toutiao.equals("true")) {
//                                        // TODO: TOUTIAO GROW
//                                        toutiao_grow();
//                                        // TODO: CHECK MESSAGE
//                                        check_message_function();
                                    // TODO: CLOSE TOUTIAO
                                    close_app(toutiao_package, 3);
                                    // TODO: BACKUP ALL APP DATA
                                    // TODO: BACKUP WEIBO
                                    backup(weibo_project);
                                    close_app(bj_package, 3);
                                    if (backup_status.equals("true")) {
                                        post_data(weibo_project, weibo_package, weibo_version, backup_status, weibo_account, weibo_sleep_second);
                                    } else {
                                        System.out.println("Weibo backup is fail");
                                    }
                                    backup_status = "normal";
                                    // TODO: BACKUP DOUYIN
                                    backup(douyin_project);
                                    close_app(bj_package, 3);
                                    if (backup_status.equals("true")) {
                                        post_data(douyin_project, douyin_package, douyin_version, backup_status, weibo_account, douyin_sleep_second);
                                    } else {
                                        System.out.println("Douyin backup is fail");
                                    }
                                    backup_status = "normal";
                                    // TODO: BACKUP TOUTIAO
                                    backup(toutiao_project);
                                    close_app(bj_package, 3);
                                    if (backup_status.equals("true")) {
                                        post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                    } else {
                                        System.out.println("Toutiao backup is fail, account callback to redis");
                                        post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                    }
                                    // TODO: CLEAR DATA
                                    clear_data(weibo_package);
                                    clear_data(douyin_package);
                                    clear_data(toutiao_package);
                                    // TODO: UNINSTLL APP
                                    uninstall_app(toutiao_package, 10);
                                    // TODO: CHANGE SYSTEM
                                    change_system();
                                } else {
                                    System.out.println("Login toutiao is fail, pass");
                                    // TODO: CLOSE TOUTIAO
                                    close_app(toutiao_package, 3);
                                    // TODO: CLEAR TOUTIAO
                                    clear_data(toutiao_package);
                                    // TODO: CLOSE DOUYIN
                                    close_app(douyin_package, 3);
                                    // TODO: CLEAR DOUYIN
                                    clear_data(douyin_package);
                                    // TODO: CLOSE WEIBO
                                    close_app(weibo_package, 3);
                                    // TODO: CLEAR WEIBO
                                    clear_data(weibo_package);
                                    // TODO: UNINSTLL APP
                                    uninstall_app(toutiao_package, 10);
                                    // TODO: ACCOUNT SUBMIT TO REDIS
                                    post_data(toutiao_project, toutiao_package, toutiao_version, backup_status, weibo_account, toutiao_sleep_second);
                                    // TODO: CHANGE SYSTEM
                                    change_system();
                                }
                            }
                        } else {
                            System.out.println("Login douyin is fail, pass");
                            // TODO: CLOSE DOUYIN
                            close_app(douyin_package, 3);
                            // TODO: CLEAR DOUYIN
                            clear_data(douyin_package);
                            // TODO: CLOSE WEIBO
                            close_app(weibo_package, 3);
                            // TODO: CLEAR WEIBO
                            clear_data(weibo_package);
                            // TODO: ACCOUNT SUBMIT TO REDIS
                            post_data(douyin_project, douyin_package, douyin_version, login_status_douyin, weibo_account, douyin_sleep_second);
                        }
                    }
                } else {
                    System.out.println("Check weibo is fail, pass");
                    // TODO: CLOSE WEIBO
                    close_app(weibo_package, 3);
                    // TODO: CLEAR WEIBO
                    clear_data(weibo_package);
                    // TODO: ACCOUNT SUBMIT TO REDIS
                    post_data(weibo_project, weibo_package, weibo_version, "false", weibo_account, weibo_sleep_second);
                }
                if (login_status_weibo.equals("normal")) {
                } else {
                    System.out.println("Login weibo is fail, pass");
                    // TODO: CLOSE WEIBO
                    close_app(weibo_package, 3);
                    // TODO: CLEAR WEIBO
                    clear_data(weibo_package);
                    // TODO: ACCOUNT SUBMIT TO REDIS
                    post_data(weibo_project, weibo_package, weibo_version, "false", weibo_account, weibo_sleep_second);
                }
            } catch (Exception ex) {
                System.out.println("Main program is exception");
            }
        }
    }

//    public void test_test() throws IOException, UiObjectNotFoundException, RemoteException {
//    }

    private void post_data(String project, String app_package, String app_version, String status, String account, int second) {
        // TODO: GET SN
        String devicesId = execCMD("getprop ro.serialno");
        System.out.println("***devicesId***: " + devicesId);
        // TODO: GET DIRECTORY NAME
        String hash_str = SHA256(project);
        String directoryName = app_package + "-" + hash_str.substring(0, 5) + "_" + app_version;
        System.out.println("***directoryName***: " + directoryName);

        if (status.equals("true")) {
            if (project.equals("tt")) {
                // TODO: DELETE STATELESS DIRECTORY （TOUTIAO)
                String statelessDirectoryPath = "/storage/emulated/0/QX_Backup/app/data/" + directoryName + "/A/files/stateless";
                System.out.println("***statelessDirectoryPath***: " + statelessDirectoryPath);
                deleteDirectory(statelessDirectoryPath);

                // TODO: SUBMIT DATA
                String Data = String.format("status=%s&devicesId=%s&account=%s&directoryName=%s&project=%s", status, devicesId, account, directoryName, project);
                submit(Data, httpUrl);
                System.out.println("Toutiao login is success, wait for 2 min...");
                sleep(second * 1000);
            } else if (project.equals("dy")) {
                // TODO: DELETE STATELESS DIRECTORY 1（DOUYIN)
                String statelessDirectoryPath1 = "/storage/emulated/0/QX_Backup/app/data/" + directoryName + "/A/files/stateless";
                System.out.println("***statelessDirectoryPath1***: " + statelessDirectoryPath1);
                deleteDirectory(statelessDirectoryPath1);
                // TODO: DELETE STATELESS DIRECTORY 2（DOUYIN)
                String statelessDirectoryPath2 = "/storage/emulated/0/QX_Backup/app/data/" + directoryName + "/A/files/keva/repo";
                System.out.println("***statelessDirectoryPath***: " + statelessDirectoryPath2);
                deleteDirectory(statelessDirectoryPath2);

                // TODO: SUBMIT DATA
                String Data = String.format("status=%s&devicesId=%s&account=%s&directoryName=%s&project=%s", status, devicesId, account, directoryName, project);
                submit(Data, httpUrl);
                System.out.println("Toutiao login is success, wait for 2 min...");
                sleep(second * 1000);
            } else {
                System.out.println("Backup weibo need not delete file");

                // TODO: SUBMIT DATA
                String Data = String.format("status=%s&devicesId=%s&account=%s&directoryName=%s&project=%s", status, devicesId, account, directoryName, project);
                submit(Data, httpUrl);
                System.out.println("Toutiao login is success, wait for 2 min...");
                sleep(second * 1000);
            }

        } else {
            // TODO: SUBMIT DATA
            String Data = String.format("status=%s&devicesId=%s&account=%s&directoryName=%s&project=%s", status, devicesId, account, directoryName, project);
            submit(Data, httpUrl);
            System.out.println("Toutiao login is success, wait for 5 second...");
            sleep(5 * 1000);
        }
    }

    private void login_weibo() {
        try {
            open_app(weibo_activity, 5);
            UiObject open_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("\t\t开启\t\t"));
            for (int i = 0; i < 10; i++) {
                if (open_btn.exists()) {
                    open_btn.clickAndWaitForNewWindow(2000);
                    sleep(2000);
                    System.out.println("Open weibo is finished");
                    break;
                }
                open_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("\t\t开启\t\t"));
                sleep(2000);
            }

            UiObject allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(1000);
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(3000);
                System.out.println("Click 2 times allow_btn is finished");
            } else {
                System.out.println("Click 2 times allow_btn is failed");
            }

            UiObject APLogin_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/tv_for_psw_login").text("用帐号密码登录"));
            for (int j = 0; j < 10; j++) {
                if (APLogin_btn.exists()) {
                    APLogin_btn.clickAndWaitForNewWindow(3000);
                    sleep(2000);
                    break;
                }
                sleep(2000);
                APLogin_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/tv_for_psw_login").text("用帐号密码登录"));
            }

            UiObject username_box = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/et_pws_username").text("手机号或者邮箱"));
            if (username_box.exists()) {
                username_box.clickAndWaitForNewWindow(3000);

                weibo_account = get_account(httpUrl);
                String[] account_array = weibo_account.split(" ");
                String username = account_array[0];
                String password = account_array[1].replaceAll("[\\t\\n\\r]", "");
                username_box.setText(username);
                sleep(1000);

                UiObject password_box = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/et_pwd"));
                if (password_box.exists()) {
                    password_box.click();
                    sleep(1000);
                    password_box.setText(password);
                    sleep(2000);

                    UiObject login_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/bn_pws_Login").text("登录"));
                    if (login_btn.exists()) {
                        login_btn.clickAndWaitForNewWindow(3000);
                        sleep(10000);
                    } else {
                        login_status_weibo = "false";
                        System.out.println("Not found login_btn!");
                    }
                } else {
                    login_status_weibo = "false";
                    System.out.println("Not found password_box!");
                }
            } else {
                login_status_weibo = "false";
                System.out.println("Not found username_box!");
            }

        } catch (Exception ex) {
            login_status_weibo = "abnormal";
            System.out.println("Login weibo is exception");
        }
    }

    private void check_weibo() {
        try {
            UiDevice device = getUiDevice();

            UiObject check_text = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/titleText").text("请先验证身份"));
            if (check_text.exists()) {
                for (int i = 0; i < 40; i++) {
                    sleep(6 * 1000);
                    check_text = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/titleText").text("请先验证身份"));
                    if (!check_text.exists()) {
                        break;
                    }
                }
                // 停留10s用来打码
                sleep(5000);
                // 因为抓取跳过元素不准确，根据坐标点击两次
                device.click(1000, 135);
                sleep(5000);
                device.click(1000, 135);
                sleep(5000);
                device.click(1000, 135);
                sleep(5000);

                UiObject close_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
                if (close_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Close_btn is exists");
                    sleep(2000);
                }

                UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
                if (update_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Update_btn is exists");
                    sleep(2000);
                }

                UiObject hot_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/tv_groupName").text("热门"));
                if (hot_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Hot_btn is exists");
                    sleep(2000);
                }

                UiObject more_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/titleSave"));
                if (more_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("More_btn is exists");
                    sleep(2000);
                }

                UiObject writer_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("写微博"));
                if (writer_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Writer_btn is exists");
                    sleep(2000);
                }

            } else {
                sleep(5000);
                // 因为抓取跳过元素不准确，根据坐标点击两次
                device.click(1000, 135);
                sleep(5000);
                device.click(1000, 135);
                sleep(5000);
                device.click(1000, 135);
                sleep(5000);

                UiObject close_btn = new UiObject(new UiSelector().className("android.view.View").text("关闭"));
                if (close_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Close_btn is exists");
                    sleep(2000);
                }

                UiObject update_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
                if (update_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Update_btn is exists");
                    sleep(2000);
                }

                UiObject hot_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/tv_groupName").text("热门"));
                if (hot_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Hot_btn is exists");
                    sleep(2000);
                }

                UiObject more_btn = new UiObject(new UiSelector().resourceId("com.sina.weibo:id/titleSave"));
                if (more_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("More_btn is exists");
                    sleep(2000);
                }

                UiObject writer_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("写微博"));
                if (writer_btn.exists()) {
                    login_status_weibo = "true";
                    System.out.println("Writer_btn is exists");
                    sleep(2000);
                }
            }

        } catch (Exception ex) {
            login_status_weibo = "abnormal";
            System.out.println("Check_status program is exception" + ex.toString());
        }
    }

    private void login_douyin() {
        try {
            UiDevice device = getUiDevice();
            open_app(douyin_activity, 10);
            UiObject personal_privacy = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/title").text("个人信息保护指引"));
            if (personal_privacy.exists()) {
                UiObject ok_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("好的"));
                if (ok_btn.exists()) {
                    ok_btn.clickAndWaitForNewWindow(3000);
                    sleep(3000);
                }
            }

            device.pressBack();
            sleep(5000);

            UiObject ikonw_btn = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/een").text("我知道了"));
            if (ikonw_btn.exists()) {
                device.pressBack();
                sleep(2000);
            }

            UiObject allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(1000);
            }

            allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(3000);
            }

            allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(3000);
            }

            device.pressBack();
            sleep(5000);

            // TODO: SCROLL FORWARD VIDEO
//            mScrollForward();
            mScrollForward();

            sleep(6000);
            UiObject me_btn = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/eew").text("我"));
            if (me_btn.exists()) {
                System.out.println("me_btn is exists");
                me_btn.clickAndWaitForNewWindow(3000);
                sleep(10000);
            } else {
                System.out.println("me_btn is not found, click 970, 1830");
                device.click(970, 1830);
                sleep(10000);
            }

//            UiObject other_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("其他方式登录"));
//            UiObject phone_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("本机号码一键登录"));
//            if (other_login_btn.exists()) {
//                System.out.println("other_login_btn is exists");
//                other_login_btn.clickAndWaitForNewWindow(3000);
//                sleep(6000);
//            } else if (phone_login_btn.exists()) {
//                System.out.println("phone_login_btn is exists");
//                // TODO: CLICK OTHER LOGIN BUTTON
//                System.out.println("other_login_btn is not found, click 550,1800");
//                device.click(550, 1800);
//                sleep(5000);
//            } else {
//                // TODO: CLICK OTHER LOGIN BUTTON
//                System.out.println("other_login_btn is not found, click 880,1010");
//                device.click(880, 1010);
//                sleep(6000);
//            }

            UiObject other_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("其他方式登录"));
            if (other_login_btn.exists()) {
                System.out.println("other_login_btn is exists");
                other_login_btn.clickAndWaitForNewWindow(3000);
                sleep(6000);
            } else {
                // TODO: CLICK OTHER LOGIN BUTTON
                System.out.println("other_login_btn is not found, click 880,1010");
                device.click(880, 1010);
                sleep(6000);
            }

            // TODO: WEIBO LOGIN BUTTON STYLE 1
            UiObject weibo_login_btn1 = new UiObject(new UiSelector().className("android.widget.TextView").text("微博登录"));
            if (weibo_login_btn1.exists()) {
                System.out.println("weibo_login_btn1 is exists");
                weibo_login_btn1.clickAndWaitForNewWindow(3000);
                sleep(20000);
            }

            // TODO: WEIBO LOGIN BUTTON STYLE 2
            UiObject weibo_login_btn2 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/bfl"));
            if (weibo_login_btn2.exists()) {
                System.out.println("weibo_login_btn2 is exists");
                weibo_login_btn2.clickAndWaitForNewWindow(3000);
                sleep(20000);
            }

            if (!weibo_login_btn1.exists() && !weibo_login_btn2.exists()) {
                // TODO: CLICK OTHER LOGIN BUTTON
                System.out.println("other_login_btn is not found, click 550,1800");
                device.click(550, 1800);
                sleep(8000);

                // TODO: CLICK OTHER WEIBO LOGIN BUTTON
                System.out.println("weibo_login_btn is not found, click 810, 1730");
                device.click(810, 1730);
                sleep(20000);
            }

            UiObject yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (yes_btn.exists()) {
                System.out.println("yes_btn is exists");
                yes_btn.clickAndWaitForNewWindow(3000);
                sleep(20000);
            } else {
                System.out.println("yes_btn is not found, click 528,912");
                device.click(528, 912);
                sleep(20000);
            }

            UiObject skip_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("跳过"));
            if (skip_btn.exists()) {
                System.out.println("skip_btn is exists");
                skip_btn.clickAndWaitForNewWindow(3000);
                sleep(3000);
            } else {
                System.out.println("skip_btn is not found, click 980, 170");
                device.click(980, 170);
                sleep(3000);
            }

            skip_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("跳过"));
            if (skip_btn.exists()) {
                System.out.println("skip_btn1 is exists");
                skip_btn.clickAndWaitForNewWindow(3000);
                login_status_douyin = "true";
                sleep(3000);
            } else {
                System.out.println("skip_btn1 is not found, click 980, 170");
                device.click(980, 170);
                sleep(3000);
            }

            UiObject ikonw_btn1 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/een").text("我知道了"));
            if (ikonw_btn1.exists()) {
                System.out.println("cancle_btn is exists");
                login_status_douyin = "true";
                sleep(2000);
            }

            UiObject cancle_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("取消"));
            if (cancle_btn.exists()) {
                System.out.println("cancle_btn is exists");
                login_status_douyin = "true";
                sleep(2000);
            } else {
                System.out.println("cancle_btn is not found, click 330, 1480");
                device.click(330, 1480);
                sleep(2000);
            }

            UiObject recommend_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("推荐"));
            if (recommend_btn.exists()) {
                System.out.println("recommend_btn is exists");
                login_status_douyin = "true";
            }

            UiObject user_btn = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.aweme:id/emr"));
            if (user_btn.exists()) {
                System.out.println("user_btn is exists");
                login_status_douyin = "true";
            }
        } catch (Exception ex) {
            login_status_douyin = "abnormal";
            System.out.println("Login douyin is exception");
        }
    }

    private void toutiao_version() throws UiObjectNotFoundException {
        // TODO: UPDATE VERSION WINDOWS
        UiObject update_version = new UiObject(new UiSelector().className("android.widget.TextView").text("以后再说"));
        if (update_version.exists()) {
            System.out.println("update_version is exists");
            update_version.clickAndWaitForNewWindow(3000);
            sleep(5000);
        }
    }

    private void login_toutiao() {
        try {
            UiDevice device = getUiDevice();
            open_app(toutiao_activity, 10);
            toutiao_version();
            UiObject ikonw_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我知道了"));
            for (int i = 0; i < 5; i++) {
                if (ikonw_btn.exists()) {
                    System.out.println("ikonw_btn is exitst");
                    ikonw_btn.clickAndWaitForNewWindow(3000);
                    sleep(3000);
                    break;
                }
                sleep(2000);
                ikonw_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我知道了"));
            }

            UiObject allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(1000);
                allow_btn.clickAndWaitForNewWindow(2000);
                sleep(3000);
                System.out.println("Click 2 times allow_btn is finished");
            } else {
                System.out.println("Click 2 times allow_btn is failed");
            }

            allow_btn = new UiObject(new UiSelector().className("android.widget.Button").text("允许"));
            if (allow_btn.exists()) {
                allow_btn.clickAndWaitForNewWindow();
                sleep(3000);
            }
            toutiao_version();
            UiObject ikonw_btn1 = new UiObject(new UiSelector().className("android.widget.TextView").text("我知道了"));
            for (int i = 0; i < 5; i++) {
                if (ikonw_btn1.exists()) {
                    System.out.println("ikonw_btn1 is exitst");
                    ikonw_btn1.clickAndWaitForNewWindow(3000);
                    sleep(3000);
                    break;
                }
                sleep(2000);
                ikonw_btn1 = new UiObject(new UiSelector().className("android.widget.TextView").text("我知道了"));
            }

            UiObject logout_btn = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/dgu").text("未登录"));
            if (logout_btn.exists()) {
                System.out.println("logout_btn is exists");
                logout_btn.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                //  TODO: CLICK LOGOUT BUTTON
                System.out.println("logout_btn is not found, click 940, 1850");
                device.click(940, 1850);
                sleep(5000);
            }

            UiObject login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("登录"));
            if (login_btn.exists()) {
                System.out.println("login_btn is exists");
                login_btn.clickAndWaitForNewWindow(3000);
                sleep(10000);
            } else {
                // TODO: CLICK LOGIN BUTTON
                System.out.println("login_btn is not found, click 530, 320");
                device.click(530, 320);
                sleep(10000);
            }
            toutiao_version();
            // TODO: MORE LOGIN METHOD
            UiObject more_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("更多登录"));
            if (more_login_btn.exists()) {
                System.out.println("more_login_btn is exists");
                more_login_btn.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                System.out.println("more_login_btn is not found, click 530, 320");
                device.click(410, 1815);
                sleep(5000);
            }
            toutiao_version();
            // TODO: REGISTER PAGE
            UiObject login_phone_btn = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/bze").text("手机登录"));
            if (login_phone_btn.exists()) {
                System.out.println("login_phone_btn is find");
                login_phone_btn.clickAndWaitForNewWindow(3000);
                sleep(5000);
            } else {
                // TODO: CLICK LOGIN BUTTON BY PHONE
                System.out.println("login_phone_btn is not found, click 270, 1815");
                device.click(270, 1815);
                sleep(5000);
            }

            UiObject not_password_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("免密登录"));
            if (not_password_login_btn.exists()) {
                System.out.println("not_password_login_btn is exists");
                UiObject login_douyin_btn = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/cr").index(0));
                if (login_douyin_btn.exists()) {
                    System.out.println("login_douyin_btn is exists");
                    login_douyin_btn.clickAndWaitForNewWindow(3000);
                    sleep(20000);
                } else {
                    // TODO: CLICK LOGIN BUTTON BY DOUYIN
                    System.out.println("login_douyin_btn is not found, click 445, 1630");
                    device.click(445, 1630);
                    sleep(20000);
                }
            } else {
                // TODO: CLICK LOGIN BUTTON BY DOUYIN
                System.out.println("not_password_login_btn is not found, click 445, 1630");
                device.click(445, 1630);
                sleep(20000);
            }
            toutiao_version();
            UiObject authorization_login_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("授权并登录"));
            if (authorization_login_btn.exists()) {
                System.out.println("authorization_login_btn is exists");
                authorization_login_btn.clickAndWaitForNewWindow(3000);
                sleep(10000);
            } else {
                // TODO: CLICK AUTHORIZATION BUTTON
                System.out.println("authorization_login_btn is not found, click 530, 1515");
                device.click(530, 1515);
                sleep(10000);
            }

            UiObject me_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我的"));
            for (int j = 0; j < 10; j++) {
                if (me_btn.exists()) {
                    System.out.println("me_btn is exists");
                    login_status_toutiao = "true";
                    break;
                }
                sleep(2000);
                me_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("我的"));
            }

            UiObject first_page = new UiObject(new UiSelector().className("android.widget.TextView").text("首页"));
            if (first_page.exists()) {
                System.out.println("first_page is exists");
                login_status_toutiao = "true";
            } else {
                System.out.println("authorization_login_btn is not found, click 135, 1850");
                device.click(135, 1850);
                sleep(3000);
                UiObject news_box = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/wk"));
                if (news_box.exists()) {
                    System.out.println("news_box is exists");
                    login_status_toutiao = "true";
                }
            }

        } catch (Exception ex) {
            System.out.println("Login toutiao is exception");
        }
    }

    private void toutiao_grow() throws UiObjectNotFoundException {
        UiDevice device = getUiDevice();
        // TODO: CLICK FIRST PAGE
        device.click(135, 1840);
        sleep(2000);

        // TODO: Circulate scroll news
        UiScrollable scroll = new UiScrollable(new UiSelector().className("android.support.v4.view.ViewPager"));
        if (scroll.exists()) {
            System.out.println("scroll is exists");
            scroll.setSwipeDeadZonePercentage(0.15);
            System.out.println("circulate scan news");
            for (int i = 0; i < 5; i++) {
                scroll.scrollForward();
                sleep(6000);

                // TODO: ENTER NEWS
                UiObject news = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/wk"));
                if (news.exists()) {
                    news.clickAndWaitForNewWindow(5000);
                    sleep(5000);

                    // TODO: READ NEWS
                    UiScrollable scroll_1 = new UiScrollable(new UiSelector().className("android.widget.ListView"));
                    if (scroll_1.exists()) {
                        System.out.println("scroll_1 is exists");
                        scroll_1.setSwipeDeadZonePercentage(0.15);
                        // TODO: RANDOM READ 8-15 TIMES
                        Random rd = new Random();
                        int times = rd.nextInt(7) + 8;
                        for (int j = 0; j < times; j++) {
                            scroll_1.scrollForward();
                            sleep(6000);
                        }
                    } else {
                        System.out.println("scroll_1 is not found");
                        // TODO: RANDOM READ 8-15 TIMES
                        Random rd = new Random();
                        int times = rd.nextInt(7) + 8;
                        for (int j = 0; j < times; j++) {
                            mScrollForward();
                            sleep(3000);
                        }
                    }
                    // TODO: BACK FIRST PAGE
                    device.pressBack();
                    sleep(4000);
                }
            }
        } else {
            System.out.println("scroll is not found, use my scroll forward");
            for (int i = 0; i < 5; i++) {
                mScrollForward();
                sleep(3000);

                // TODO: ENTER NEWS
                UiObject news = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/wk"));
                if (news.exists()) {
                    news.clickAndWaitForNewWindow(5000);
                    sleep(5000);

                    // TODO: READ NEWS
                    UiScrollable scroll_1 = new UiScrollable(new UiSelector().className("android.widget.ListView"));
                    if (scroll_1.exists()) {
                        System.out.println("scroll_1 is exists");
                        scroll_1.setSwipeDeadZonePercentage(0.15);
                        // TODO: RANDOM READ 8-15 TIMES
                        Random rd = new Random();
                        int times = rd.nextInt(7) + 8;
                        for (int j = 0; j < times; j++) {
                            scroll_1.scrollForward();
                            sleep(6000);
                        }
                    } else {
                        System.out.println("scroll_1 is not found");
                        // TODO: RANDOM READ 8-15 TIMES
                        Random rd = new Random();
                        int times = rd.nextInt(7) + 8;
                        for (int j = 0; j < times; j++) {
                            mScrollForward();
                            sleep(3000);
                        }
                    }
                    // TODO: BACK FIRST PAGE
                    device.pressBack();
                    sleep(4000);
                }
            }
        }
    }

    private void check_message_function() throws IOException, RemoteException, UiObjectNotFoundException {
        open_app(toutiao_activity, 10);
        UiDevice device = getUiDevice();
        UiObject me_btn = new UiObject(new UiSelector().resourceId("com.ss.android.article.news:id/dgu").text("我的"));
        if (me_btn.exists()) {
            System.out.println("me_btn is exists");
            me_btn.clickAndWaitForNewWindow(3000);
            sleep(6000);
            UiObject message_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("私信"));
            if (message_btn.exists()) {
                System.out.println("This account is have message function");
                message_function_status = "true";
            }
        } else {
            System.out.println("me_btn is not foud, click 940, 1850");
            device.click(940, 1850);
            sleep(6000);
            UiObject message_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("私信"));
            if (message_btn.exists()) {
                System.out.println("This account is have message function");
                message_function_status = "true";
            }
        }
    }

    private void backup(String project) {
        try {
            open_bj();
            String app_name = get_appname(project);
            UiObject toutiao_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(app_name));
            for (int i = 0; i < 5; i++) {
                if (toutiao_btn.exists()) {
                    System.out.println("toutiao_btn is exists");
                    toutiao_btn.clickAndWaitForNewWindow(3000);
                    sleep(2000);
                    break;
                }
                sleep(2000);
                toutiao_btn = new UiObject(new UiSelector().className("android.widget.TextView").text(app_name));
            }

            UiObject backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("本地备份"));
            if (backup_btn.exists()) {
                backup_btn.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject name_edit = new UiObject(new UiSelector().className("android.widget.EditText"));
            if (name_edit.exists()) {
                name_edit.setText(project);
                sleep(2000);
            }

            UiObject sure_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (sure_btn.exists()) {
                sure_btn.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            for (int j = 0; j < 20; j++) {
                if (yes_btn.exists()) {
                    System.out.println("Backup is over");
                    yes_btn.clickAndWaitForNewWindow(3000);
                    backup_status = "true";
                    sleep(3000);
                    break;
                }
                sleep(3000);
                yes_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            }

        } catch (Exception ex) {
            backup_status = "abnormal";
            System.out.println("Backup is exception: " + ex.toString());
        }
    }

    private void change_system() {
        try {
            open_bj();
            UiObject more_btn = new UiObject(new UiSelector().className("android.widget.ImageView").descriptionContains("更多选项"));
            if (more_btn.exists()) {
                more_btn.clickAndWaitForNewWindow(3000);
                sleep(2000);
            }

            UiObject change_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键变机"));
            if (change_btn.exists()) {
                change_btn.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }

            UiObject sure_btn = new UiObject(new UiSelector().className("android.widget.Button").text("确定"));
            if (sure_btn.exists()) {
                sure_btn.clickAndWaitForNewWindow(2000);
                sleep(2000);
            }

        } catch (Exception ex) {
            System.out.println("Change system is exception：" + ex.toString());
        }
    }

    private void open_bj() throws IOException, RemoteException {
        open_app(bj_activity, 5);
        UiObject backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键备份"));
        for (int i = 0; i < 15; i++) {
            if (backup_btn.exists()) {
                break;
            }
            backup_btn = new UiObject(new UiSelector().className("android.widget.TextView").text("一键备份"));
            sleep(2000);
        }
    }

    private void clear_data(String app_package) throws IOException {
        execShellCmd("pm clear " + app_package);
        jqhelper.delay(4000);
    }

    private void install_app(String apk_path, int second) throws IOException {
        UiDevice device = getUiDevice();
        device.pressHome();
        jqhelper.delay(1000);
        execShellCmd("pm install " + apk_path);
        jqhelper.delay(second * 1000);
    }

    private void uninstall_app(String app_package, int second) throws IOException {
        System.out.println("Uninstalling...");
        execShellCmd("pm uninstall " + app_package);
        jqhelper.delay(second * 1000);
        System.out.println("Uninstall is over");
    }

    private void check_packages() throws IOException {
        execShellCmd("pm list packages");
        jqhelper.delay(5000);

    }

    private void open_app(String activity, int second) throws RemoteException, IOException {
        UiDevice device = getUiDevice();
        device.wakeUp();
        sleep(1000);
        device.pressHome();
        sleep(1000);
        execShellCmd("am start " + activity);
        sleep(second * 1000);
    }

    private void close_app(String app_package, int second) throws IOException {
        UiDevice device = getUiDevice();
        device.pressHome();
        sleep(2000);
        execShellCmd("am force-stop " + app_package);
        jqhelper.delay(second * 1000);
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

    private static void saveAsFileWriter(String filePath1, String content) {
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(filePath1);
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
//                sb.append(line+"\n");
                sb.append(line);
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

    public static String get_account(String httpUrl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                System.out.println("*********" + result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    private void submit(String Data, String httpUrl) {
        //捕获异常
        try {
            URL url = new URL(httpUrl);

            //HttpURLConnection 打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //设置POST访问
            conn.setRequestMethod("POST");
            //超时等待
            conn.setReadTimeout(5000);
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
                String Sew = "";

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
                //调试输出Sew
                System.out.println(Sew);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> get_apk_list() {
        ArrayList<String> filenamelist = new ArrayList<String>();
        File file = new File(apk_store_path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    continue;
                } else {
                    filenamelist.add(files[i].getName().replace(".apk", ""));
                    Log.i("AutoScript", "所有备份apk名称下标=" + i + "=" + files[i].getName());
                }

            }
        }
        return filenamelist;
    }

    public static String get_appname(String project) {
        //创建Map对象
        Map<String, String> map = new HashMap<String, String>();
        //给map中添加元素
        map.put("wb", "微博");
        map.put("dy", "抖音短视频");
        map.put("ks", "快手");
        map.put("tt", "今日头条");
        map.put("xhs", "小红书");
        return map.get(project).toString();
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


