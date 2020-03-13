package com.inSocial;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/28.
 */
public class InSocialCommentModel {
    public InSocialCommentModel(int id,int inid, String sd, String Rc, String tt, Byte op, Byte isS) {
       ID=id;
        InSocialID = inid;
        SendName = sd;
        ReceiveName = Rc;
        Text = tt;
        CreateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Operated = op;
        Flag = false;
        IsSender = isS;
    }
    public InSocialCommentModel(InSocialCommentModel m) {
        ID = m.ID;
        InSocialID = m.InSocialID;
        SendName = m.SendName;
        ReceiveName = m.ReceiveName;
        Text = m.Text;
        CreateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Operated = 4;
        Flag = false;
        IsSender = 1;
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

    /// <summary>
    ///
    /// </summary>
    public String ReceiveName;

    /// <summary>
    ///
    /// </summary>
    public String Text;

    /// <summary>
    ///
    /// </summary>
    public String CreateTime;

    /// <summary>
    ///0为未读信息 1为已读信息 2为自动回复的信息 3为主动发送的信息 4为主动发送执行后的标记
    /// </summary>
    public Byte Operated;

    /// <summary>
    ///
    /// </summary>
    public Boolean Flag;

    public Byte IsSender;
}