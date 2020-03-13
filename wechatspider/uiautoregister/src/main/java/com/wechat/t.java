package com.wechat;

import com.common.weedfs.RequestResult;
import com.common.weedfs.WeedFSClient;

/**
 * Created by user on 2017/12/27.
 */

public class t {
    public static  void main(String[] args)
    {
        try {
            WeedFSClient localWeedFSClient = new WeedFSClient("222.185.251.62", "9933");
            RequestResult rs = null;
            //int i = 0;
            for (int i = 0; i < 3; i++) {
                rs = localWeedFSClient.write("D:\\f.docx");
                if (rs.isSuccess()) {
                    if (rs.isSuccess()) {
                        String path = "http://" + rs.getPublicURL() + "/" + rs.getFid();
                        System.out.println(path);
                    }
                }

            }
        }
        catch (Exception ex)
        {

        }
    }
}
