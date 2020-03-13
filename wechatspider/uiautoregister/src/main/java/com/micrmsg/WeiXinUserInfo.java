package com.micrmsg;

import java.util.List;

/**
 * Created by Jeqee on 2015/12/2.
 */
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