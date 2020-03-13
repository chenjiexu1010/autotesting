package com.common.weedfs.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.common.weedfs.post.MultipartPost;
import com.common.weedfs.post.PostParameter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeqee on 2015/9/23.
 */
public class HttpHelper {
    public static String httpGetString(String url) throws Exception {
        return EntityUtils.toString(httpGet(url), "utf8");
    }

    public static byte[] httpGetBytes(String url) throws Exception {
        return EntityUtils.toByteArray(httpGet(url));
    }

    public static Bitmap httpGetBitmap(String url) throws Exception {
        byte[] bytes = httpGetBytes(url);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static HttpEntity httpGet(String url) throws Exception {
        HttpEntity ret;
        HttpGet hg = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
        HttpResponse res = client.execute(hg);
        if (res.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Error: status code is " + res.getStatusLine().getStatusCode());
        } else {
            //Success
            ret = res.getEntity();
        }
        return ret;
    }

    public static String httpPostForString(String url, String postData, String contentType)
            throws Exception {
        return EntityUtils.toString(httpPost(url, postData, contentType), "utf8");
    }

    public static String httpPostForString(String url, String postData) throws Exception {
        return httpPostForString(url, postData, "application/json; charset=utf-8");
    }

    public static String httpPostForStringLog(String url, String postData, String contentType)
            throws Exception {
        return EntityUtils.toString(httpPostLog(url, postData, contentType), "utf8");
    }
    public static String httpPostForStringLog(String url, String postData) throws Exception {
        return httpPostForStringLog(url, postData, "application/json; charset=utf-8");
    }

    public static byte[] httpPostForBytes(String url, String postData, String contentType) throws
            Exception {
        return EntityUtils.toByteArray(httpPost(url, postData, contentType));
    }

    public static HttpEntity httpPost(String url, String postData, String contentType) throws
            Exception {
        HttpEntity ret;

        HttpPost hg = new HttpPost(url);
//        hg.setHeader("Content-Type", "application/json; charset=utf-8");
        hg.setHeader("Content-Type", contentType);
        hg.setEntity(new StringEntity(postData, "utf8"));
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000);
        HttpResponse res = client.execute(hg);
        if (res.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Error: status code is " + res.getStatusLine().getStatusCode());
        } else {
            //Success
            ret = res.getEntity();
        }

        return ret;
    }


    public static HttpEntity httpPostLog(String url, String postData, String contentType) throws
            Exception {
        HttpEntity ret;

        HttpPost hg = new HttpPost(url);
//        hg.setHeader("Content-Type", "application/json; charset=utf-8");
        hg.setHeader("Content-Type", contentType);
        hg.setEntity(new StringEntity(postData, "utf8"));
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
        HttpResponse res = client.execute(hg);
        if (res.getStatusLine().getStatusCode() != 200) {
            throw new Exception("Error: status code is " + res.getStatusLine().getStatusCode());
        } else {
            //Success
            ret = res.getEntity();
        }
        return ret;
    }

    // 上传文件，微信专用
    public static String httpPostMultipart(String url, String filename, String
            accountNmae) {
        try {
            List<PostParameter> params = new ArrayList<PostParameter>();
            params.add(new PostParameter<String>("key", "xuv9zsfzisvuzsor79wer2y9057g29574"));
            params.add(new PostParameter<String>("userId", "1"));
            params.add(new PostParameter<String>("wxID", accountNmae));
            params.add(new PostParameter<File>("file", new File(filename)));
            MultipartPost post = new MultipartPost(params);

            String content = post.send(url);

            return content;
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    // 上传文件，微信专用
    public static String httpPostMultipart2(String url, String filename, String
            accountNmae) {

        try {
            List<PostParameter> params = new ArrayList<PostParameter>();
            params.add(new PostParameter<String>("key", "xuv9zsfzisvuzsor79wer2y9057g29574"));
            params.add(new PostParameter<String>("userId", "1"));
            params.add(new PostParameter<String>("wxID", accountNmae));
            params.add(new PostParameter<File>("file", new File(filename)));
            MultipartPost post = new MultipartPost(params);

            String content = post.send2(url);

            return content;
        } catch (Exception ex) {
            return ex.toString();
        }

    }



    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    //file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                //file.delete();
            }
        }
    }

    public static void DeleteAllFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static String GetFileName(File file) {
        if (file.exists() == false) {
            System.out.println(false);
            return "";
        } else {
            if (file.isFile()) {
                return file.getName();
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();

                if (childFile == null || childFile.length == 0) {
                    // file.delete();
                    return "";
                }
                return childFile[0].getName();
                // file.delete();
            }
        }
        return "";
    }

    //从指定URL下载文件，保存到指定位置
    //示例：ScriptHelper.downfile("http://www.baidu.com/logo.gif", "/sdcard/logo.gif");
    public static void downFile(String urlStr, String fileName) throws Exception {
        //先清空文件夹再下载
        DeleteFile(new File("/sdcard/Pictures"));
        DeleteFile(new File("/sdcard/InCamera"));
        DeleteFile(new File("/sdcard/DCIM/Camera"));
        DeleteFile(new File("/sdcard/tempscr.png"));

        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(file);
            // 开始读取
            try {
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
            } finally {
                os.close();
            }
        } finally {
            is.close();
        }
    }
}
