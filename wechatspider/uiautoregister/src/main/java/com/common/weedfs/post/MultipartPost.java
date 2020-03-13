package com.common.weedfs.post;

import android.util.Log;


import com.common.weedfs.helper.HttpHelper;
import com.common.weedfs.helper.ScriptHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//http://www.codenet.ru/webmast/php/HTTP-POST.php
public class MultipartPost {

    private final String TAG = "MultipartPost";
    private List<PostParameter> params;
    private static final String CRLF = "\r\n";
    private String BOUNDARY = "---------------------------7d4a6d158c9";
    private List<String> BOUNDARYList = new ArrayList<String>();

    public MultipartPost(List<PostParameter> params) {
        BOUNDARYList.add(0, "7d4a6d158c91");
        BOUNDARYList.add(1, "32dc22a42dfaa");
        BOUNDARYList.add(2, "76acfaaexad345");
        BOUNDARYList.add(3, "3a2d1a8f3234ac6");
        this.params = params;
    }

    public String send(String urlString) throws Exception {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String response = null;
        InputStream is = null;

        try {
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; MI 3W Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30");

            dos = new DataOutputStream(conn.getOutputStream());

            for (PostParameter param : params) {
                Log.d(TAG, "Processning param: " + param.getParamName());
                if (param.getValue() == null) {
                    param.setValue("");
                }
                if (param.getValue().getClass() == File.class) {

                    postFileParameter(dos, param.getParamName(), (File) param.getValue(), param
                            .getContentType());
                } else {
                    postStringParameter(dos, param.getParamName(), param.getValue().toString());
                }
            }

            dos.writeBytes(closeBoundary());
            dos.flush();

            is = conn.getInputStream();
            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            response = b.toString();
        } catch (Exception ex) {
            System.out.println(new Date().toString() + "Post异常>>>>>>>>>" + ex.toString());
            return "";
        } finally {
            if (dos != null) try {
                dos.close();
            } catch (IOException ioe) { /* that's it */ }
            if (is != null) try {
                is.close();
            } catch (IOException ioe) { /* that's it */ }
        }

        return response;
    }

    public String send2(String urlString) throws Exception {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String response = null;
        InputStream is = null;

        try {
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
            System.setProperty("sun.net.client.defaultReadTimeout", "60000");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; MI 3W Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30");

            dos = new DataOutputStream(conn.getOutputStream());

            for (PostParameter param : params) {
                Log.d(TAG, "Processning param: " + param.getParamName());
                if (param.getValue() == null) {
                    param.setValue("");
                }

                if (param.getValue().getClass() == File.class) {

                    postFileParameter(dos, param.getParamName(), (File) param.getValue(), param
                            .getContentType());

                } else {
                    postStringParameter(dos, param.getParamName(), param.getValue().toString());
                }
            }

            dos.writeBytes(closeBoundary());
            dos.flush();
//            AddLog(1, "", "post异常", "post异常 22222", "", 1, "00001");
            is = conn.getInputStream();
            int ch;
//            AddLog(1, "", "post异常", "post异常 33333", "", 1, "00001");
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            response = b.toString();
        } catch (Exception ex) {
            System.out.println(new Date().toString() + "Post异常>>>>>>>>>" + ex.toString());
           // XLog.e("post异常", "post异常 " + ex.toString(), "", 1, "00001");

            return "";
        } finally {
            if (dos != null) try {
                dos.close();
            } catch (IOException ioe) { /* that's it */ }
            if (is != null) try {
                is.close();
            } catch (IOException ioe) { /* that's it */ }
        }

        return response;
    }

    public String send3(String urlString, int i) throws Exception {
        if (i == 0) {
            BOUNDARY = "---------------------------" + new java.text.SimpleDateFormat("SSSssmmHHddMMyyyy").format(new Date());
        } else if (i == 1) {
            BOUNDARY = getRandomString(10);
        } else if (i == 2) {
            BOUNDARY = getRandomString(15);
        } else if (i == 3) {
            BOUNDARY = getRandomString(20);
        } else {
            BOUNDARY = getRandomString(25);

        }
        //BOUNDARY="fsdfsdfse2121";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String response = null;
        InputStream is = null;

        try {
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
            System.setProperty("sun.net.client.defaultReadTimeout", "60000");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; MI 3W Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.0.0.488 U3/0.8.0 Mobile Safari/534.30");

            dos = new DataOutputStream(conn.getOutputStream());

            for (PostParameter param : params) {
//                AddLog(1, "", "post异常", "post异常 "+param.getParamName()+" "+param.getContentType(), "", 1, "00001");
                Log.d(TAG, "Processning param: " + param.getParamName());
                if (param.getValue() == null) {
//                    AddLog(1, "", "post异常", "post异常1getValue", "", 1, "00001");
                    param.setValue("");
                }

                if (param.getValue().getClass() == File.class) {

                    postFileParameter(dos, param.getParamName(), (File) param.getValue(), param
                            .getContentType());

                } else {
                    postStringParameter(dos, param.getParamName(), param.getValue().toString());
                }
            }

            dos.writeBytes(closeBoundary());
            dos.flush();

            is = conn.getInputStream();
            int ch;
//            AddLog(1, "", "post异常", "post异常 133333"+BOUNDARY, "", 1, "00001");
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            response = b.toString();
        } catch (Exception ex) {
            System.out.println(new Date().toString() + "Post异常>>>>>>>>>" + ex.toString());
           // XLog.e("post异常"+ "post异常 " + ex.toString() + " " + BOUNDARY, "", 1, "00001");

            return "";
        } finally {
            if (dos != null) try {
                dos.close();
            } catch (IOException ioe) { /* that's it */ }
            if (is != null) try {
                is.close();
            } catch (IOException ioe) { /* that's it */ }
        }

        return response;
    }

    public String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    public void AddLog(int isshow, String batchNumber, String type, String remark, String LogTargetName, int status, String Code) {
        String resp = "";
        try {
            //AddLog(0,"888", "上传组件：",remark+"222222" ,"",1,"00001");
            String Vps = ScriptHelper.readSDFile("/sdcard/VPS.txt");
            String addLogURL = "http://222.185.251.62:30008/api/wx/addlog";
            String postData = String.format("{\"isshow\":\"%s\",\"batchNumber\":\"%s\",\"type\":\"%s\"," +
                            "\"remark\":\"%s\",\"cloneid\":\"%s\",\"code\":\"%s\",\"mobile\":\"%s\",\"LogTargetName\":\"%s\",\"status\":\"%s\"}", isshow + "",
                    batchNumber,
                    type,
                    remark,
                    "0",
                    Code,
                    Vps, LogTargetName, status);
            resp = HttpHelper.httpPostForString(addLogURL, postData);
            ScriptHelper.writeSDFile((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .format(new Date()) + postData, "/sdcard/mylog.txt");
        } catch (Exception ex) {
            System.out.println("添加日志发生异常" + ex.toString() + " resp:" + resp);
        }
    }

    private void postStringParameter(DataOutputStream dos, String paramName, String paramValue)
            throws IOException {
        dos.writeBytes(boundary() + CRLF);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"" + CRLF + CRLF);
        dos.writeBytes(paramValue + CRLF);
    }

    private void postFileParameter(DataOutputStream dos, String paramName, File file, String
            contentType) throws IOException {
        dos.writeBytes(boundary() + CRLF);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\""
                + file.getName() + "\"" + CRLF);
        dos.writeBytes("Content-Type: " + contentType + CRLF);
        dos.writeBytes("Content-Transfer-Encoding: binary" + CRLF);
        dos.writeBytes(CRLF);

        FileInputStream fileInputStream = new FileInputStream(file);
        int bytesAvailable = fileInputStream.available();
        int maxBufferSize = 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {

            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = null;
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//        int length = -1;
//      /* 从文件读取数据到缓冲区 */
//        while ((length = fileInputStream.read(buffer)) != -1)
//        {
//        /* 将数据写入DataOutputStream中 */
//            dos.write(buffer, 0, length);
//        }
        dos.writeBytes(CRLF);
        dos.flush();
        fileInputStream.close();
        //AddLog(1, "", "post异常", "最后写", "", 1, "00001");
        System.gc();
    }


    private String closeBoundary() {
        return boundary() + "--" + CRLF;
    }

    private String boundary() {
        return "--" + BOUNDARY;
    }

}
