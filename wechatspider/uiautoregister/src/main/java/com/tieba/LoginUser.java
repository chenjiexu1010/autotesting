package com.tieba;

import com.base.jqhelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2015/7/23.
 */

public class LoginUser{
    @Expose
    @SerializedName("Account")
    String Account;
    @Expose
    @SerializedName("AutoReplyIsSame")
    String AutoReplyIsSame;
    @Expose
    @SerializedName("AutoReplyType")
    String AutoReplyType;
    @Expose
    @SerializedName("Cookies")
    String Cookies;
    @Expose
    @SerializedName("FollowName")
    String FollowName;
    @Expose
    @SerializedName("FriendCurrentName")
    String FriendCurrentName;
    @Expose
    @SerializedName("FriendDataID")
    String FriendDataID;
    @Expose
    @SerializedName("FriendNumPerDay")
    String FriendNumPerDay;
    @Expose
    @SerializedName("FriendFinishCount")
    String FriendFinishCount;
    @Expose
    @SerializedName("FriendMessageContent")
    String FriendMessageContent;
    @Expose
    @SerializedName("AutoReplyMessageContent")
    String AutoReplyMessageContent;
    @Expose
    @SerializedName("ManualReplyMessageContent")
    String ManualReplyMessageContent;
    @Expose
    @SerializedName("ManualReplyPicture")
    String ManualReplyPicture;
    @Expose
    @SerializedName("FriendNeedWork")
    String FriendNeedWork;
    @Expose
    @SerializedName("FriendNextName")
    String FriendNextName;
    @Expose
    @SerializedName("AutoReplyNextName")
    String AutoReplyNextName;
    @Expose
    @SerializedName("ManualReplyNextName")
    String ManualReplyNextName;
    @Expose
    @SerializedName("FriendNextID")
    String FriendNextID;
    @Expose
    @SerializedName("ID")
    String ID;
    @Expose
    @SerializedName("Name")
    String Name;
    @Expose
    @SerializedName("Password")
    String Password;
    @Expose
    @SerializedName("UserID")
    String UserID;
    @Expose
    @SerializedName("Status")
    String Status;
    @Expose
    @SerializedName("GetTime")
    String GetTime;
    @Expose
    @SerializedName("Picture")
    String Picture;
}

class BaiduFriendSubmitData {
    @Expose
    @SerializedName("ID")
    String ID;
    @Expose
    @SerializedName("Logs")
    String Logs;
    @Expose
    @SerializedName("FriendCurrentName")
    String FriendCurrentName;
    @Expose
    @SerializedName("Cookie")
    String Cookie;
    @Expose
    @SerializedName("Status")
    String Status;
    @Expose
    @SerializedName("SuccessNum")
    Integer SuccessNum = 0;
    @Expose
    @SerializedName("GetTime")
    String GetTime;
    @Expose
    @SerializedName("AutoReplySuccessNameList")
    String AutoReplySuccessNameList;
    @Expose
    @SerializedName("ManualReplySuccessNameList")
    String ManualReplySuccessNameList;
    @Expose
    @SerializedName("SuccessReplyList")
    String SuccessReplyList;
    @Expose
    @SerializedName("DeleteNameList")
    String DeleteNameList;
    @Expose
    @SerializedName("AutoReplyPictureList")
    String AutoReplyPictureList;

    public void setAll (String ID, String Logs, String Status, String GetTime, String AutoReplySuccessNameList, String SuccessReplyList, String ManualReplySuccessNameList, String DeleteNameList, String AutoReplyPictureList) {
        this.Cookie = "";
        this.ID = ID;
        this.Logs = Logs;
        this.Status = Status;
        this.GetTime = GetTime;
        this.AutoReplySuccessNameList = AutoReplySuccessNameList;
        this.SuccessReplyList = SuccessReplyList;
        this.ManualReplySuccessNameList = ManualReplySuccessNameList;
        this.DeleteNameList = DeleteNameList;
        this.AutoReplyPictureList = AutoReplyPictureList;
    }
}

class BaiduLogInfo {
    @Expose
    @SerializedName("LogType")
    String LogType;
    @Expose
    @SerializedName("ErrorCode")
    String ErrorCode;
    @Expose
    @SerializedName("ErrorMessage")
    String ErrorMessage;
    @Expose
    @SerializedName("Time")
    String Time;
    @Expose
    @SerializedName("Target")
    String Target;
    @Expose
    @SerializedName("Status")
    String Status;
    @Expose
    @SerializedName("VPS")
    String VPS;
    @Expose
    @SerializedName("Text")
    String Text;
    @Expose
    @SerializedName("UserID")
    String UserID;
    @Expose
    @SerializedName("TaskID")
    String TaskID;
    @Expose
    @SerializedName("PicID")
    String PicID;

    public BaiduLogInfo setAll(String LogType, String ErrorMessage, String Target, String Status, String VPS, String Text, String UserID, String TaskID, String PicID){
        BaiduLogInfo logInfo = new BaiduLogInfo();
        logInfo.ErrorCode = "";
        logInfo.LogType = LogType;
        logInfo.ErrorMessage = ErrorMessage;
        logInfo.Target = Target;
        logInfo.Status = Status;
        logInfo.VPS = VPS;
        logInfo.Text = Text;
        logInfo.Time = jqhelper.getDateTime();
        logInfo.UserID = UserID;
        logInfo.TaskID = TaskID;
        logInfo.PicID = PicID;
        return logInfo;
    }

}


