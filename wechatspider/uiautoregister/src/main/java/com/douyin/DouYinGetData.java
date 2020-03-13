package com.douyin;

public class DouYinGetData {
    public String Id;
    /// <summary>
    /// 备份标记
    /// </summary>
    public String Code;

    /// <summary>
    /// 评论内容
    /// </summary>
    public String Comments;

    /// <summary>
    /// 关键词
    /// </summary>
    public String KeyWords;

    /// <summary>
    /// 直播账号
    /// </summary>
    public String LiveAccounts;

    public String WorkDay;

    public String getWorkDay() {
        return WorkDay;
    }

    public void setWorkDay(String workDay) {
        WorkDay = workDay;
    }

    public String getCode() {
        return Code;
    }

    public String getId() {
        return Id;
    }

    public String getComments() {
        return Comments;
    }

    public String getKeyWords() {
        return KeyWords;
    }

    public String getLiveAccounts() {
        return LiveAccounts;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setKeyWords(String keyWords) {
        KeyWords = keyWords;
    }

    public void setLiveAccounts(String liveAccounts) {
        LiveAccounts = liveAccounts;
    }
}
