package com.inSocial;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/25.
 */
public class MessageModel {
    public MessageModel(){
        creatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public MessageModel(String s, String r, String m, int o) {
        sender = s;
        receiver = r;
        message = m;
        issender = o;
        creatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String sender = "";
    public String receiver = "";
    public String message = "";
    public int issender = 1;
    public String creatTime = "";
}
