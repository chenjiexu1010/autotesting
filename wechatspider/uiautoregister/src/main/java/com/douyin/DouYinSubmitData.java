package com.douyin;

public class DouYinSubmitData {
    /// <summary>
    /// 备份标记
    /// </summary>
    public String Code;
    /// <summary>
    /// 返回信息
    /// </summary>
    public String Message;

    /// <summary>
    /// 添加执行间隔时间
    /// </summary>
    public String Hour;
    /// <summary>
    /// 编号Id
    /// </summary>
    public String Id;

    public void setId(String id) {
        Id = id;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getId() {
        return Id;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public String getCode() {
        return Code;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }
}
