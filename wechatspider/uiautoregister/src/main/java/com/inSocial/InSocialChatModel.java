package com.inSocial;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/28.
 */
public class InSocialChatModel {
    public InSocialChatModel(int uid,int id, String sd,String sdid, String Rc,String Rcid, String tt, Byte op, Byte isS,String pic) {
        ID=uid;
        InSocialID = id;
        SendName = sd;
        SendInID=sdid;
        ReceiveInID=Rcid;

        ReceiveName = Rc;
        Text = tt;
        CreateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Operated = op;
        Flag = false;
        IsSender = isS;
        Picture = pic;
    }

    /// <summary>
    ///
    /// </summary>
    public int ID;

    /// <summary>
    ///
    /// </summary>
    public int InSocialID;

    /// <summary>
    ///
    /// </summary>
    public String SendName;


    public  String SendInID;
    /// <summary>
    ///
    /// </summary>
    public String ReceiveName;
    public  String ReceiveInID;
    /// <summary>
    ///
    /// </summary>
    public String Text;

    /// <summary>
    ///
    /// </summary>
    public String CreateTime;

    /// <summary>
    ///
    /// </summary>
    public Byte Operated;

    /// <summary>
    ///
    /// </summary>
    public Boolean Flag;

    /// <summary>
    ///
    /// </summary>
    public String Picture;

    public Byte IsSender;
}
