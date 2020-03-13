package com.micrmsg;

public class CloneModel {
    /// <summary>
    /// 主键ID
    /// </summary>
    private int ID = 0;

    /// <summary>
    /// 三方账号名称
    /// </summary>
    private String AccountName = "";

    /// <summary>
    /// 三方账号类型(0:微博,1:微信,2微信公众号)
    /// </summary>
    private Byte AccountType = 1;

    /// <summary>
    /// 上次抓博文时间
    /// </summary>
    private String LastTime = "";

    private int CloneID = 0;

    private int WeChatID = 0;

    private String PlanTime = "";

    private int PublishCount = 0;

    private int PublishInterval = 0;

    private int TaskID = 0;

    /// <summary>
    /// 上次抓取博文ID
    /// </summary>
    private long LastID = 0;

    /// <summary>
    /// 最后执行时间
    /// </summary>
    private String UpdateTime = "";

    /// <summary>
    /// 该克隆对象是否存在
    /// </summary>
    private Boolean IsExists = false;

    /// <summary>
    /// 状态,0是非好友，1是发出好友申请未成为好友，2是已成为好友
    /// </summary>
    private Byte Status = 0;

    /// <summary>
    /// 执行手机名称
    /// </summary>
    private String VPS = "";

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public Byte getAccountType() {
        return AccountType;
    }

    public void setAccountType(Byte accountType) {
        AccountType = accountType;
    }

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public long getLastID() {
        return LastID;
    }

    public void setLastID(long lastID) {
        LastID = lastID;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public Boolean getIsExists() {
        return IsExists;
    }

    public void setIsExists(Boolean isExists) {
        IsExists = isExists;
    }

    public Byte getStatus() {
        return Status;
    }

    public void setStatus(Byte status) {
        Status = status;
    }

    public String getVPS() {
        return VPS;
    }

    public void setVPS(String VPS) {
        this.VPS = VPS;
    }

    public int getCloneID() {
        return CloneID;
    }

    public void setCloneID(int cloneID) {
        CloneID = cloneID;
    }

    public int getWeChatID() {
        return WeChatID;
    }

    public void setWeChatID(int weChatID) {
        WeChatID = weChatID;
    }

    public String getPlanTime() {
        return PlanTime;
    }

    public void setPlanTime(String planTime) {
        PlanTime = planTime;
    }

    public int getPublishCount() {
        return PublishCount;
    }

    public void setPublishCount(int publishCount) {
        PublishCount = publishCount;
    }

    public int getPublishInterval() {
        return PublishInterval;
    }

    public void setPublishInterval(int publishInterval) {
        PublishInterval = publishInterval;
    }

    public int getTaskID() {
        return TaskID;
    }

    public void setTaskID(int taskID) {
        TaskID = taskID;
    }

    @Override
    public String toString() {
        return "CloneModel{" +
                "ID=" + ID +
                ", AccountName='" + AccountName + '\'' +
                ", AccountType=" + AccountType +
                ", LastTime='" + LastTime + '\'' +
                ", CloneID=" + CloneID +
                ", WeChatID=" + WeChatID +
                ", PlanTime='" + PlanTime + '\'' +
                ", PublishCount=" + PublishCount +
                ", PublishInterval=" + PublishInterval +
                ", TaskID=" + TaskID +
                ", LastID=" + LastID +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", IsExists=" + IsExists +
                ", Status=" + Status +
                ", VPS='" + VPS + '\'' +
                '}';
    }
}
