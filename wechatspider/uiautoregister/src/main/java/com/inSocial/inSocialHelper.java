package com.inSocial;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jeqee on 2015/9/24.
 */
public class inSocialHelper {
    //设置日志信息
    public static LogInfoModel getLogInfo(String LogType, String ErrorCode, String ErrorMessage, String Target, int Status, String VPS) {
        if (Status == 0)
            System.out.println("error:" + ErrorCode);
        LogInfoModel logInfoModel = new LogInfoModel();
        logInfoModel.setLogType(LogType);
        logInfoModel.setErrorCode(ErrorCode);
        logInfoModel.setErrorMessage(ErrorMessage);
        logInfoModel.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//"yyyy-MM-dd HH:mm:ss"
        logInfoModel.setTarget(Target);
        logInfoModel.setStatus(Status);
        logInfoModel.setVPS(VPS);
        return logInfoModel;
    }


}
