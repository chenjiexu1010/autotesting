package com.WeChatNew;

import com.wechat.WeiXinUserInfo;

import java.util.List;

public class WeiXinNameV2 {
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
