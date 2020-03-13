package com.micrmsg;

/**
 * Created by Jeqee on 2016/2/18.
 */
public class WeChatDataInformation {
    /// <summary>
    /// ID
    /// </summary>
    private int ID;

    /// <summary>
    /// DataID
    /// </summary>
    private int DataID;

    /// <summary>
    /// UID
    /// </summary>
    private String UID;

    /// <summary>
    /// 0|null：初始化；1：被占用；2：存在；3：不存在
    /// </summary>
    private Byte Status;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDataID() {
        return DataID;
    }

    public void setDataID(int dataID) {
        DataID = dataID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Byte getStatus() {
        return Status;
    }

    public void setStatus(Byte status) {
        Status = status;
    }
}
