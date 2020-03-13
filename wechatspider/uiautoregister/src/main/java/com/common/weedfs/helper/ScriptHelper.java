package com.common.weedfs.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.common.weedfs.model.CaptchaResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/22.
 */
public class ScriptHelper {
    //切换输入法
    public static void switchImeToAdbKeyboard() {
        execShellCmd("ime set jp.jun_nama.test.utf7ime/.Utf7ImeService");
    }

    //切换搜狗输入法
    public static void switchSouGoToAdbKeyboard() {
        execShellCmd("ime set com.sohu.inputmethod.sogou/.SogouIME");
    }

    //执行命令
    private static void execShellCmd(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            System.out.println("ScriptHelper->execShellCmd->" + cmd + ":" + e.getMessage());
        }
    }

    //模拟点击一次屏幕
    public static void tap(int x, int y) {
        execShellCmd("input tap " + x + " " + y);
    }

    //模拟在触屏上滑动
    public static void swipe(int x1, int y1, int x2, int y2) {
        execShellCmd(String.format("input swipe %d %d %d %d", x1, y1, x2, y2));
    }

    //模拟输入文字
    public static void input(String text) {
        execShellCmd(String.format("input text %s", text));
        //execShellCmd(String.format("am broadcast -a ADB_INPUT_TEXT --es msg \'%s\'", text));
    }

    // 获取当前时间
    public static String getDateTime() {
        return new Timestamp(System.currentTimeMillis()).toString().substring(0, 19);
    }

    //region 【读写本地文件相关】

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     * 示例：ScriptHelper.writeSDFile("Content!", "/sdcard/test.txt");
     */
    public static void writeSDFile(String str, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        File f = new File(fileName);
        fw.write(str + "\r\n");//追加日志
        FileOutputStream os = new FileOutputStream(f);
        DataOutputStream out = new DataOutputStream(os);
        out.writeShort(2);
        out.writeUTF("");
        fw.flush();
        fw.close();
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     * 示例：ScriptHelper.appendToSDFile("Content!", "/sdcard/log.txt");
     */
    public static void appendToSDFile(String text, String filename) {
        File logFile = new File(filename);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
    保存图片到SdCard
    示例：ScriptHelper.saveImageToSdCard(bitmap, "/sdcard/a.png");
     */
    public static void savePngToSdCard(Bitmap bitmap, String filename) throws Exception {
        File f = new File(filename);
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /*
    * 从SD卡读取文件
     */
    public static byte[] readSDFileBytes(String filename) {
        try {
            File file = new File(filename);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bytes;
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * 读取SD卡中文本文件
     * 示例：ScriptHelper.readSDFile("/sdcard/test.txt");
     */
    public static String readSDFile(String fileName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
//        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "utf-8"));
        StringBuffer sb = new StringBuffer();
        String valueString = null;
        int row = 0;
        while ((valueString = br.readLine()) != null) {
            if (row > 0) {
                sb.append('\n');
            }
            sb.append(valueString);
            row = row + 1;
        }
        fileInputStream.close();
        String result = sb.toString();
        if (result.length() > 0 && result.charAt(0) == '\uFEFF') {
            sb.deleteCharAt(0);
        }
        //sb.deleteCharAt(0);
        return sb.toString();
    }

    //将图片保存到指定位置
    public static void saveBitmapToSDCard(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static void writeLog(String dateString) {
        try {
            System.out.println(dateString);
            writeSDFile(dateString, "/sdcard/appLogs.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reportNormal() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        try {
            writeSDFile(dateString, "/sdcard/appstatus.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//endregion


    //region 【系统命令】
    //强行关闭指定包名的APP
    //示例：Sh.killApp("com.jeqee.jeqeeautopilot");
    public static void killApp(String packageName) {
        execShellCmd("am force-stop " + packageName);
    }


    //清除指定应用的缓存
    public static void clearPackage(String packageName) {
        execShellCmd("pm clear " + packageName);
    }

    //打开指定页面
    public static void startActivity(String activityName) {
        execShellCmd("am start -n " + activityName);
    }

    //endregion


    //region 【获取当前页面元素集合】
//    private static void getUiHierarchyFile(boolean compressed) {
//        String command = "rm /storage/sdcard0/uidump.xml";
//        try {
//            ScriptHelper.execShellCmd(command);
//        } catch (Exception e1) {
//        }
//        if (compressed) {
//            command = String.format("%s %s --compressed %s", new
// Object[]{"/system/bin/uiautomator", "dump", "/storage/sdcard0/uidump.xml"});
//        } else {
//            command = String.format("%s %s %s", new Object[]{"/system/bin/uiautomator", "dump",
// "/storage/sdcard0/uidump.xml"});
//        }
//        try {
//            ScriptHelper.execShellCmd(command);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //获取当前页面元素集合
//    public static UiAutomatorResult takeSnapNodes() {
//        UiAutomatorModel model = null;
//        File file = new File("/storage/sdcard0/uidump.xml");
//        if (file.exists()) {
//            file.delete();
//        }
//        try {
//            getUiHierarchyFile(false);
//        } catch (Exception e) {
//            return null;
//        }
//        file = new File("/storage/sdcard0/uidump.xml");
//        try {
//            model = new UiAutomatorModel(file);
//        } catch (Exception e) {
//            return null;
//        }
//        return new UiAutomatorResult(file, model);
//    }
//
//    public static class UiAutomatorResult {
//        public final File uiHierarchy;
//        public final UiAutomatorModel model;
//
//        public UiAutomatorResult(File uiXml, UiAutomatorModel m) {
//            this.uiHierarchy = uiXml;
//            this.model = m;
//        }
//    }
    //endregion


    //region 【验证码相关】

    //获取指定图片上的验证码 目前只支持若快
    public static CaptchaResult getVerifyCode(Bitmap bitmap, String vcodeType, String appId,
                                              String appKey) throws Exception {
        byte[] picByte = bitmap2Bytes(bitmap);
        JSONObject job = new JSONObject();
        JSONArray imgbyte = new JSONArray();
        for (int i = 0; i < picByte.length; i++) {
            imgbyte.put(i, picByte[i] & 0xff);
        }
        job.put("captchaImage", imgbyte);
        job.put("appId", appId);
        job.put("typeId", vcodeType);
        job.put("appKey", appKey);
        job.put("count", 5);
        String str = job.toString();
        String res = HttpHelper.httpPostForString("http://222.185.195" +
                ".206:52015/captchaapi/ruokuai/captcha", str);
        System.out.println(res);
        JSONObject resJson = new JSONObject(res);
        return new CaptchaResult(resJson.getString("Id"), resJson.getString("CaptchaStr"));
    }

    public static CaptchaResult getVerifyCode(byte[] b, String vcodeType, String appId,
                                              String appKey) throws Exception {
        byte[] picByte = b;
        JSONObject job = new JSONObject();
        JSONArray imgbyte = new JSONArray();
        for (int i = 0; i < picByte.length; i++) {
            imgbyte.put(i, picByte[i] & 0xff);
        }
        job.put("captchaImage", imgbyte);
        job.put("appId", appId);
        job.put("typeId", vcodeType);
        job.put("appKey", appKey);
        job.put("count", 5);
        String str = job.toString();
        String res = HttpHelper.httpPostForString("http://222.185.195" +
                ".206:52015/captchaapi/ruokuai/captcha", str);
        System.out.println(res);
        JSONObject resJson = new JSONObject(res);
        return new CaptchaResult(resJson.getString("Id"), resJson.getString("CaptchaStr"));
    }

    //将Bitmap转换为字节流
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //截屏并返回
    public static Bitmap screenShot() {
        File fl = new File("/sdcard/tempscr.png");
        if (fl.exists()) {
            fl.delete();
        }
        execShellCmd("/system/bin/screencap -p /sdcard/tempscr.png");
        while (!fl.exists()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Bitmap he = null;
        try {
            FileInputStream fis = new FileInputStream("/sdcard/tempscr.png");
            int i = 0;
            while (he == null && i <= 5) {
                i += 1;
                he = BitmapFactory.decodeStream(fis);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return he;
    }

    // 截取图片
    public static Bitmap cutImage(Bitmap source, int x1, int y1, int x2, int y2) {
        return Bitmap.createBitmap(source, x1, y1, x2 - x1, y2 - y1);
    }

    //报告错误的验证码
    public static void reportError(CaptchaResult res) throws Exception {
        String ret = HttpHelper.httpGetString("http://222.185.195" +
                ".206:52015/captchaapi/reporterror/ruokuai/" + res.getId());
        // Log.i("JEQEE_DEBUG", ret);
    }
    //endregion

    //验证码

    public static CaptchaResult LoginCaptchaTap(int x1, int y1, int x2, int y2) {
        Bitmap me = screenShot();
        Bitmap he = cutImage(me, x1, y1, x2, y2);
        byte[] bmp = compressImage(he);
        try {
            return getVerifyCode(bmp, "3040", "41008", "52f81dc38b5b41f9ac3f12bd8556e4fd");
            //后三个参数代表百度手机APP项目，四位纯数字验证码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 90) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        /* ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        System.out.println("bmp SIZE:" + bitmap.getByteCount());*/
        return baos.toByteArray();
    }

    /**
     * 设置虚拟的Wifi名称
     *
     * @param str
     * @throws IOException
     */
    public static void setWifiName(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_wifiname.txt");
    }

    /**
     * 设置虚拟的Mac地址 格式 xx.xx.xx.xx.xx.xx
     *
     * @param str
     * @throws IOException
     */
    public static void setMacAddr(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_macaddr.txt");
    }

    /**
     * 设置虚拟的GPS地址，格式 xxx.xxx, xxx.xxx
     *
     * @param str
     * @throws IOException
     */
    public static void setGpsValue(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_gps.txt");
    }

    /**
     * 设置虚拟IMEI串号
     *
     * @param str
     * @throws IOException
     */
    public static void setImei(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_imei.txt");
    }

    public static void setBuildModel(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_bmodel.txt");
    }

    public static void setBuildSerial(String str) throws IOException {
        writeSDFile(str, "/sdcard/hack_bser.txt");
    }

    public static void setAndroidId(String aid) throws IOException {
        writeSDFile(aid, "/sdcard/hack_aid.txt");
    }

}
