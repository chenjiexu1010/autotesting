package com.inSocial;

/**
 * Created by Jeqee on 2015/9/24.
 * 日志的Model
 */
public class LogInfoModel {
    public String getLogType() {
        return LogType;
    }

    public void setLogType(String logType) {
        LogType = logType;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getVPS() {
        return VPS;
    }

    public void setVPS(String VPS) {
        this.VPS = VPS;
    }

    private String LogType;

    private String ErrorCode;

    private String ErrorMessage;

    private String Time;

    private String Target;
    // 0-失败 1-成功
    private int Status;

    private String VPS;
}
