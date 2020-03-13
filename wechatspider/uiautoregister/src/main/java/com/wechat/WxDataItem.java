package com.wechat;

import java.util.List;

public class WxDataItem {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return this.time; }

    private String name;
    private String userID;
    private String time;
}
 class WxDataItem2 {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public void setTime(String time) { this.time = time; }

    public String getTime() { return this.time; }

    private String name;
    private List<String> userIDs;
    private String time;
}
