package com.wechat;

import java.util.List;

/**
 * Created by Jeqee on 2015/12/2.
 */

//{"Text":"[{\"name\":\"tr13142015\",\"cloneid\":\"9978\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"09\/12\/2017 15:38:00\"}]},{\"name\":\"宋恩兵\",\"cloneid\":\"9084\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"11\/23\/2017 21:03:00\"}]},{\"name\":\"张腾\",\"cloneid\":\"9074\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"11\/27\/2017 11:03:00\"}]},{\"name\":\"米家\",\"cloneid\":\"8822\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/09\/2017 03:56:00\"}]},{\"name\":\"Alice\",\"cloneid\":\"8825\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 01:25:00\"}]},{\"name\":\"xr16777\",\"cloneid\":\"9998\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 10:33:00\"}]},{\"name\":\"ACGJ168\",\"cloneid\":\"9990\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 11:09:00\"}]},{\"name\":\"肖雯3\",\"cloneid\":\"9080\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 11:37:00\"}]},{\"name\":\"missnicee1\",\"cloneid\":\"9977\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 12:11:00\"}]},{\"name\":\"JF53330305\",\"cloneid\":\"9979\",\"type\":0,\"businesstypes\":null,\"users\":[{\"userID\":null,\"maxPickCount\":19,\"time\":\"12\/10\/2017 12:13:00\"}]}]"}

public class WeiXinUserInfo {
    private String UserID;
    private int maxPickCount;
    private String time;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getMaxPickCount() {
        return maxPickCount;
    }

    public void setMaxPickCount(int maxPickCount) {
        this.maxPickCount = maxPickCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

class WeiXinNameV2 {
    private String name;
    private List<WeiXinUserInfo> users;
    private int type;
    private String businesstypes;
    private String cloneid;

    public String getCloneid() {
        return cloneid;
    }

    public void setCloneid(String cloneid) {
        this.cloneid = cloneid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeiXinUserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<WeiXinUserInfo> users) {
        this.users = users;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBusinesstypes() {
        return businesstypes;
    }

    public void setBusinesstypes(String businesstypes) {
        this.businesstypes = businesstypes;
    }
}