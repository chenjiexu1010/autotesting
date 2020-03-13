package com.weibo.yanghao;

public class SubmitDataInfo {
    public String NickName;
    public String Message;
    public String IsSuccess;

    public String getNickName() {
        return NickName;
    }

    public String getIsSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

}
