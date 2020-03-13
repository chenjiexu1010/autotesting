package com.common.weedfs;


import com.common.volley.entity.MultipartEntity;
import com.common.weedfs.helper.HttpHelper;
import com.common.weedfs.helper.ScriptHelper;
import com.common.weedfs.post.PostParameter;
import com.google.gson.Gson;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jeqee on 2016-08-11.
 */
public class WeedFSClient {  // master address & port number
    // master address & port number
    private String masterAddress;
    private String masterPort;

    public WeedFSClient(String address, String port) {
        this.masterAddress = address;
        this.masterPort = port;
    }

    public RequestResult write(String path) {

        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        File inputFile = new File(path);
        RequestResult result = new RequestResult();
        WeedAssignedInfo assignedInfo = null;

        if (!inputFile.exists()) {
            throw new IllegalArgumentException("WeedFsClient 55 File doesn't exist " + path);
        }

        BufferedReader in = null;

        // 1. send assign request and get fid
        try {
//                AddLog(1, "", "post异常", "开始上传"+"http://"
//                        + this.masterAddress + ":" + this.masterPort + "/", "", 1, "00001");
            in = new BufferedReader(new InputStreamReader(sendHttpGetRequest("http://"
                            + this.masterAddress + ":" + this.masterPort + "/", "dir/assign",
                    "GET")));
//                AddLog(1, "", "post异常", "开始上传"+"http://"
//                        + this.masterAddress + ":" + this.masterPort + "/", "", 1, "00001");
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            Gson gson = new Gson();
            assignedInfo = gson.fromJson(response.toString(), WeedAssignedInfo.class);
//                AddLog(1, "", "post异常", "开始上传a"+assignedInfo.getPublicUrl() + " "+assignedInfo.getFid(), "", 1, "00001");
        } catch (Exception e) {
            //XLog.e("post异常" + "开始上传b" + e.toString() + " ", "", 1, "00001");
            throw new RuntimeException(e.toString());
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // assignedInfo.setPublicUrl(assignedInfo.getPublicUrl().replace("localhost",this.masterAddress));
//            AddLog(1, "", "post异常", "上传日志 前" +assignedInfo.getPublicUrl(), "", 1, "00001");
            String publicUrl = assignedInfo.getPublicUrl().replace("localhost", this.masterAddress);
//            AddLog(1, "", "post异常", "上传日志 后" +publicUrl, "", 1, "00001");
            String url = "http://" + publicUrl + "/"
                    + assignedInfo.getFid();
//            AddLog(1, "", "post异常", "上传日志url " +url, "", 1, "00001");
            // url = url.replace("localhost",this.masterAddress);
            List<PostParameter> params = new ArrayList<PostParameter>();
            params.add(new PostParameter<File>("file", new File(path)));

            String response = WFHttpClient.httpPostMultipart(url, "file", params);
            //String response = uploadFile(url,new File(path));
            Gson gson = new Gson();

            WeedUploadResponse uploadResponse = gson.fromJson(response.toString(), WeedUploadResponse.class);

            int size = uploadResponse.getSize();
//            AddLog(1, "", "post异常", "上传日志url 111" +assignedInfo.getPublicUrl(), "", 1, "00001");
            publicUrl = assignedInfo.getPublicUrl().replace("localhost", this.masterAddress);
//            AddLog(1, "", "post异常", "上传日志url 112" +assignedInfo.getPublicUrl(), "", 1, "00001");
            result.setPublicURL(publicUrl);
            result.setFid(assignedInfo.getFid());
            result.setSize(size);
            result.setSuccess(true);
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
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


    /*
     * example: fid = 3,01637037d6 write file to local file
     */

    public RequestResult read(String fid, String path) {

        if (fid == null || fid.length() == 0) {
            throw new IllegalArgumentException("Fid cannot be empty");
        }

        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException("File path cannot be empty");
        }

        File output = new File(path);
        RequestResult result = new RequestResult();

        if (output.exists()) {
            throw new IllegalArgumentException("output file ");
        }

        String volumnId = fid.split(",")[0];
        ServerLocations locations = null;

        BufferedReader in = null;

        // 1. send quest to get volume address
        try {
            in = new BufferedReader(new InputStreamReader(sendHttpGetRequest("http://"
                            + this.masterAddress + ":" + this.masterPort + "/",
                    "dir/lookup?volumeId=" + volumnId, "GET")));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            locations = gson.fromJson(response.toString(), ServerLocations.class);

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. download the file
        BufferedOutputStream wr = null;
        try {
            InputStream input = sendHttpGetRequest("http://" + locations.getOnePublicUrl()
                    + "/", fid, "GET");

            output.createNewFile();
            wr = new BufferedOutputStream(new FileOutputStream(output));

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = input.read(buffer)) != -1) {
                wr.write(buffer, 0, len);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            try {
                if (in != null && wr != null) {
                    in.close();
                    wr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*
     * example: fid = 3,01637037d6 write file to local file
     */

    public InputStream read(String fid) {

        if (fid == null || fid.length() == 0) {
            throw new IllegalArgumentException("Fid cannot be empty");
        }

        String volumnId = fid.split(",")[0];
        ServerLocations locations = null;

        BufferedReader in = null;

        // 1. send quest to get volume address
        try {
            in = new BufferedReader(new InputStreamReader(sendHttpGetRequest("http://"
                            + this.masterAddress + ":" + this.masterPort + "/",
                    "dir/lookup?volumeId=" + volumnId, "GET")));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            locations = gson.fromJson(response.toString(), ServerLocations.class);

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. get input stream
        try {
            return sendHttpGetRequest("http://" + locations.getOnePublicUrl() + "/", fid,
                    "GET");
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    /*
     * delete the file
     */

    public RequestResult delete(String fid) {

        if (fid == null || fid.length() == 0) {
            throw new IllegalArgumentException("Fid cannot be empty");
        }

        RequestResult result = new RequestResult();

        String volumnId = fid.split(",")[0];
        ServerLocations locations = null;

        BufferedReader in = null;

        // 1. send quest to get volume address
        try {
            in = new BufferedReader(new InputStreamReader(sendHttpGetRequest("http://"
                            + this.masterAddress + ":" + this.masterPort + "/",
                    "dir/lookup?volumeId=" + volumnId, "GET")));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            locations = gson.fromJson(response.toString(), ServerLocations.class);

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. delete the file
        try {

            HttpURLConnection con = null;
            URL requestUrl = new URL("http://" + locations.getOnePublicUrl() + "/" + fid);
            con = (HttpURLConnection) requestUrl.openConnection();

            con.setRequestMethod("DELETE");

            // add request header
            con.setRequestProperty("User-Agent", "");
            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        return result;
    }

    /*
     * Used to send request to WeedFS server
     */
    private InputStream sendHttpGetRequest(String host, String requestUrlDetail,
                                           String method) throws Exception {

        HttpURLConnection con = null;
        URL requestUrl = new URL(host.toString() + requestUrlDetail);
        con = (HttpURLConnection) requestUrl.openConnection();

        // optional default is GET
        con.setRequestMethod(method);

        // add request header
        con.setRequestProperty("User-Agent", "");
        int responseCode = con.getResponseCode();


        return con.getInputStream();
    }

    public static void main(String[] args) {
        RequestResult result = null;
        WeedFSClient client = new WeedFSClient("localhost", "9333");
        try {
            result = client.write("/WeedFS/test.data");
            client.read(result.getFid(), "/WeedFS/test.data1");
            client.delete(result.getFid());
            File file = new File("/WeedFS/test.data1");
            file.delete();
            client.read(result.getFid(), "/WeedFS/test.data1");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(result.toString());
    }


    private String uploadFile(String actionUrl, File file) {
        try {
            MultipartEntity multipartEntity = new MultipartEntity();
// 文本参数


// 二进制参数

// 文件参数
            multipartEntity.addFilePart("file", file);

// POST请求
            HttpPost post = new HttpPost(actionUrl);
// 将multipartEntity设置给post
            post.setEntity(multipartEntity);
// 使用http client来执行请求
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse res = httpClient.execute(post);
            HttpEntity entity3 = res.getEntity();
            //XLog.e("post异常" + "post异常 111111111112" + res.getStatusLine().getStatusCode(), "", 1, "00001");
            String verifySource = "";
            //显示内容
            if (entity3 != null) {
                // 显示结果
                //XLog.e("post异常" + "post异常 " + "" + entity3, 1, "00001");
                verifySource = EntityUtils.toString(entity3, "utf-8");


            }
            return verifySource;
        } catch (Exception e) {
            //XLog.e("post异常" + "post异常 " + e.toString(), "", 1, "00001");
            return "";
        }

    }

}
