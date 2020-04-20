package com.weibo.sendblog;

public class SubmitSendBlogModel {
    public String TaskId;
    public String Message;
    public String Success;

    public String getTaskId() {
        return TaskId;
    }
    public void setTaskId(String taskId) {
        TaskId = taskId;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public String getMessage() {
        return Message;
    }
    public String getSuccess() {
        return Success;
    }
    public void setSuccess(String success) {
        Success = success;
    }
}
