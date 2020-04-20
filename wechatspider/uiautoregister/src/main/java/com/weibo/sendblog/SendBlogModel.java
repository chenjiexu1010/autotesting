package com.weibo.sendblog;

public class SendBlogModel {
    public String TaskId;
    public String Account;
    public String BlogContent;
    public String PicUrls;
    public String BackUpId;

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }
    public String getAccount() {
        return Account;
    }
    public void setAccount(String account) {
        Account = account;
    }
    public void setPicUrls(String picUrls) {
        PicUrls = picUrls;
    }
    public String getPicUrls() {
        return PicUrls;
    }
    public String getBlogContent() {
        return BlogContent;
    }
    public void setBlogContent(String blogContent) {
        BlogContent = blogContent;
    }
    public String getTaskId() {
        return TaskId;
    }

    public String getBackUpId() {
        return BackUpId;
    }

    public void setBackUpId(String backUpId) {
        BackUpId = backUpId;
    }
}
