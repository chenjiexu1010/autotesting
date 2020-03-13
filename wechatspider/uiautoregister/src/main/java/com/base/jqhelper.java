package com.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.common.helper.HttpHelper;
import com.common.model.CaptchaResult;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/24.石栋梁
 */
public class jqhelper {
    private static TessBaseAPI _tessApi;
    private static boolean _useOcr = false;

    //初始化OCR组件
    public static void initOCR() {
        //初始化OCR组件
        _tessApi = new TessBaseAPI();
        _tessApi.setDebug(false);
        try {
            _tessApi.init(Environment.getExternalStorageDirectory().getPath(), "chi_sim");
            _useOcr = true;
        } catch (Exception ex) {
            _useOcr = false;
        }
    }

    //从图片中获取文字
    public static String getTextIn(Bitmap source) {
        if (_useOcr) {
            _tessApi.setImage(source);
            return _tessApi.getUTF8Text();
        } else {
            return "#ERROR: OCR component not used. Check tess data.";
        }
    }

    // 从当前屏幕指定坐标获取字符串
    public static String GetTextFromScreen(int x1, int y1, int x2, int y2) {
        try {
            Bitmap pic = screenShot();
            Bitmap image = cutImage(pic, x1, y1, x2, y2);
            return getTextIn(image);
        } catch (Exception e) {
            return "";
        }
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

    public static Bitmap cutImage(Bitmap source, int x1, int y1, int x2, int y2) {
        return Bitmap.createBitmap(source, x1, y1, x2 - x1, y2 - y1);
    }

    // 验证坐标内是否包含指定字符
    //返回值：0：为空，1：包含 2：不为空且不包含
    public static byte VerifyText(int x1, int y1, int x2, int y2, String text, int time) {
        try {
            long starttime = System.currentTimeMillis();
            String result = null;
            String[] strlist = text.split(",");
            while (true) {
                if (System.currentTimeMillis() - starttime > time) {
                    if (result.isEmpty())
                        return 0; //空白
                    else
                        return 2;
                }
                result = GetTextFromScreen(x1, y1, x2, y2);
                if (result.isEmpty()) {
                    Thread.sleep(10);
                    continue;
                } else {
                    for (String item : strlist) {
                        if (result.contains(item))
                            return 1;
                    }
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static CaptchaResult LoginCaptchaTap() {
        Bitmap me = screenShot();
        Bitmap he = cutImage(me, 432, 452, 630, 530);
        try {
            CaptchaResult cr = getVerifyCode(he, "3040", "42052",
                    "9cc5b14da3474449b83c29acc5c80736");//后三个参数代表百度手机APP项目，四位纯数字验证码
            return cr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取指定图片上的验证码 目前只支持若快
    public static CaptchaResult getVerifyCode(Bitmap bitmap, String vcodeType, String appId, String appKey) throws Exception {
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
        String res = HttpHelper.httpPostForString("http://222.185.195.206:52015/captchaapi/ruokuai/captcha", str);
        JSONObject resJson = new JSONObject(res);
        return new CaptchaResult(resJson.getString("Id"), resJson.getString("CaptchaStr"));
    }

    //将Bitmap转换为字节流
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String httpGetString(String url) throws Exception {
        return EntityUtils.toString(httpGet(url), "utf8");
    }

    public static HttpEntity httpGet(String url) throws Exception {
        HttpEntity ret;
        HttpGet hg = new HttpGet(url);
        HttpResponse res = new DefaultHttpClient().execute(hg);
        if (res.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Error: status code is " + res.getStatusLine().getStatusCode());
        } else {
            //Success
            ret = res.getEntity();
        }
        return ret;
    }

    /**
     * 读取SD卡中文本文件
     * 示例：ScriptHelper.readSDFile("/sdcard/test.txt");
     */
    public static String readSDFile(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        String valueString = null;
        while ((valueString = br.readLine()) != null) {
            sb.append('\n');
            sb.append(valueString);
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }

    //追加写
    public static void writeSDFileEx(String str, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            File f = new File(fileName);
            fw.write(str);
            FileOutputStream os = new FileOutputStream(f);
            DataOutputStream out = new DataOutputStream(os);
            out.writeShort(2);
            out.writeUTF("");
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    //追加写
    public static void writeSDFileEx2(String str, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(str);
            fw.flush();
            fw.close();
        } catch (Exception e) {
        }
    }

    //强行关闭指定包名的APP
    //示例：Sh.killApp("com.jeqee.jeqeeautopilot");
    public static void killApp(String packageName) {
        execShellCmd("am force-stop " + packageName);
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

    //等待一段时间
    public static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //打开指定页面
    public static void startActivity(String activityName) {
        execShellCmd("am start -n " + activityName);
    }

    //清除指定应用的缓存
    public static void clearPackage(String packageName) {
        execShellCmd("pm clear " + packageName);
    }

    //尝试切换输入法为ADB Keyboard
    public static void switchImeToAdbIme() {
        execShellCmd("ime set com.android.adbkeyboard/.AdbIME");
    }

    /**
     * 设置快捷输入法
     *
     * @throws IOException
     */
    public static void change_input_method() throws IOException {
        Runtime.getRuntime().exec("settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
        jqhelper.delay(1000);
    }

    /**
     * 关闭变机大师
     *
     * @throws IOException
     */
    public static void close_bj() throws IOException {
        // 关闭变机大师
        Runtime.getRuntime().exec("am force-stop com.littlerich.holobackup");
        jqhelper.delay(1000);
    }

    //手机断线重连
    public static void reConnNet() {
        execShellCmd("svc data disable");
        execShellCmd("svc wifi disable");
        delay(1000);
        execShellCmd("svc data enable");
        execShellCmd("svc wifi enable");
    }

    //模拟输入文字
    public static void input(String text) {
        execShellCmd(String.format("am broadcast -a ADB_INPUT_TEXT --es msg \'%s\'", text));
    }

    public static void reportError(CaptchaResult res) throws Exception {
        String ret = HttpHelper.httpGetString("http://222.185.195.206:52015/captchaapi/reporterror/ruokuai/" + res.getId());
        // Log.i("JEQEE_DEBUG", ret);
    }

    public static void saveJPEGToSDCard(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 更新图片
    public static boolean updateImage() {
        try {
            startActivity("zte.com.cn.filer/.FilerActivity");
            delay(4000);
            UiObject mobileText = new UiObject(new UiSelector().className("android.widget.TextView").text("手机").index(0));
            if (!mobileText.exists())
                return false;
            mobileText.click();
            delay(500);
            UiObject a1Text = new UiObject(new UiSelector().className("android.widget.TextView").text("a1").index(0));
            if (!a1Text.exists())
                return false;
            a1Text.clickAndWaitForNewWindow();
            UiObject optionButton = new UiObject(new UiSelector().className("android.widget.ImageButton").index(2));
            if (!optionButton.exists())
                return false;
            optionButton.click();
            UiObject selectText = new UiObject(new UiSelector().className("android.widget.TextView").text("多选").index(0));
            if (!selectText.exists())
                return false;
            selectText.click();
            UiObject picText = new UiObject(new UiSelector().className("android.widget.TextView").text("pic.jpg").index(0));
            if (!picText.exists())
                return false;
            picText.click();
            optionButton = new UiObject(new UiSelector().className("android.widget.LinearLayout").index(4));
            if (!optionButton.exists())
                return false;
            optionButton.click();
            UiObject renameText = new UiObject(new UiSelector().className("android.widget.TextView").text("重命名").index(0));
            if (!renameText.exists())
                return false;
            renameText.click();
            delay(500);
            input("abc");
            delay(500);
            UiObject enterButton = new UiObject(new UiSelector().text("确定").index(1));
            if (!enterButton.exists())
                return false;
            enterButton.click();
            delay(500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 加载自动回复的图片到指定路径
    public static boolean LoadPicture(String id, String url, UiDevice device) {
        try {
            if (!id.isEmpty()) {
                URL Url = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                myBitmap = myBitmap.createScaledBitmap(myBitmap, 450, 600, true);

                if (myBitmap == null)
                    return false;
                else {
                    // 保存图片到指定路径
                    File fl = new File("/sdcard/a1/");
                    if (!fl.exists())
                        fl.mkdirs();
                    fl = new File("/sdcard/a1/abc.jpg");
                    if (fl.exists())
                        fl.delete();
                    saveJPEGToSDCard(myBitmap, "/sdcard/a1/pic.jpg");
//                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("/sdcard")));
//                    SingleMediaScanner SMS = new SingleMediaScanner(context, fl);
                    Boolean result = updateImage();
                    killApp("zte.com.cn.filer");
                    clearPackage("zte.com.cn.filer");
                    startActivity("com.baidu.tieba/.addresslist.im.searchfriend.SearchFriendActivity");
                    delay(3000);
                    device.click(160, 250); //点击输入框
                    delay(800);
                    if (result) {
                        fl = new File("/sdcard/a1/abc.jpg");
                        if (!fl.exists())
                            return false;
                        else
                            return true;
                    } else return false;
                }
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    //获取当前时间
    public static String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static void reportlearDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        try {
            writeSDFileEx(dateString, "/sdcard/cleardate.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportNormal() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        try {
            writeSDFileEx(dateString, "/sdcard/appstatus.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportNormalWithoutLog() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        try {
            writeSDFileEx(dateString, "/sdcard/appstatus.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;

    public SingleMediaScanner(Context context, File f) {
        mFile = f;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }

}
