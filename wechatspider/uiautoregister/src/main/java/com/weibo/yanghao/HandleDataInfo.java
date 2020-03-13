package com.weibo.yanghao;

public class HandleDataInfo {
    public String NickName;
    public String Content;
    public String PicUrls;
    public String VideoUrl;
    public String Comment;
    public String Uid;
    public String CheckCode;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getComment() {
        return Comment;
    }

    public String getContent() {
        return Content;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPicUrls() {
        return PicUrls;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setPicUrls(String picUrls) {
        PicUrls = picUrls;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getCheckCode() {
        return CheckCode;
    }

    public void setCheckCode(String checkCode) {
        CheckCode = checkCode;
    }
}


