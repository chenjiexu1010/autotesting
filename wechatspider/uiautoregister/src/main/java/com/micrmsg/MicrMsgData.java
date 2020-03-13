package com.micrmsg;

/**
 * Created by Jeqee on 2015/12/23.
 */
public class MicrMsgData {
    public MicrMsgData(String nickName, String phoneNum, String password) {
        this.nickName = nickName;
        this.phoneNum = phoneNum;
        this.password = password;
    }

    private String nickName;
    private String phoneNum;
    private String password;
    private String macAddr;
    private String wifiName;
    private String gpsValue;
    private String imei;
    private String buildSerial;
    private String buildModel;
    private String androidId;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getGpsValue() {
        return gpsValue;
    }

    public void setGpsValue(String gpsValue) {
        this.gpsValue = gpsValue;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBuildSerial() {
        return buildSerial;
    }

    public void setBuildSerial(String buildSerial) {
        this.buildSerial = buildSerial;
    }

    public String getBuildModel() {
        return buildModel;
    }

    public void setBuildModel(String buildModel) {
        this.buildModel = buildModel;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
}
