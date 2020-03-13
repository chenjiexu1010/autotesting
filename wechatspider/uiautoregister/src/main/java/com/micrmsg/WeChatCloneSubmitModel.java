package com.micrmsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeqee on 2016/1/29.
 */
public class WeChatCloneSubmitModel {
    /// <summary>
    /// 克隆的对象ID
    /// </summary>
    private CloneModel cloneInfo = new CloneModel();

    private List<WeChatSpiderLog> logList = new ArrayList<WeChatSpiderLog>();

    /// <summary>
    ///  朋友圈信息
    /// </summary>
    private List<PublishModel> publishList = new ArrayList<PublishModel>();

    public CloneModel getCloneInfo() {
        return cloneInfo;
    }

    public void setCloneInfo(CloneModel cloneInfo) {
        this.cloneInfo = cloneInfo;
    }

    public List<WeChatSpiderLog> getLogList() {
        return logList;
    }

    public void setLogList(List<WeChatSpiderLog> logList) {
        this.logList = logList;
    }

    public List<PublishModel> getPublishList() {
        return publishList;
    }

    public void setPublishList(List<PublishModel> publishList) {
        this.publishList = publishList;
    }
}

class WeChatSpiderLog {
    public WeChatSpiderLog(long batchID, String type, String text, String VPS, String
            createTime) {
        BatchID = batchID;
        Type = type;
        Text = text;
        this.VPS = VPS;
        CreateTime = createTime;
    }


    /// <summary>
    /// BatchID
    /// </summary>
    private long BatchID;

    /// <summary>
    /// Type
    /// </summary>
    private String Type;

    /// <summary>
    /// Text
    /// </summary>
    private String Text;

    /// <summary>
    /// VPS
    /// </summary>
    private String VPS;

    /// <summary>
    /// CreateTime
    /// </summary>
    private String CreateTime;

    public long getBatchID() {
        return BatchID;
    }

    public void setBatchID(long batchID) {
        BatchID = batchID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getVPS() {
        return VPS;
    }

    public void setVPS(String VPS) {
        this.VPS = VPS;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}

class PublishModel {
    public PublishModel(String publishDesc, String publishTime) {
        PublishDesc = publishDesc;
        PublishTime = publishTime;
    }
    /// <summary>
    /// 发布的图片
    /// </summary>
    private String PublishPicture = "";
    /// <summary>
    /// 发布的简介
    /// </summary>
    private String PublishDesc = "";
    /// <summary>
    /// 发布的时间
    /// </summary>
    private String PublishTime = "";

    public String getPublishPicture() {
        return PublishPicture;
    }

    public void setPublishPicture(String publishPicture) {
        PublishPicture = publishPicture;
    }

    public String getPublishDesc() {
        return PublishDesc;
    }

    public void setPublishDesc(String publishDesc) {
        PublishDesc = publishDesc;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }
}