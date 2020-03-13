package com.wechat;

import java.util.List;

/**
 * Created by Jeqee on 2015/11/23.
 */
public class WechatData {
    private String name;
    private String content;
    private String time;
    private String sourceType;
    private List<String> image_list;
    private String pics;

    public WechatData(String name, String content, String time, List<String> imageList) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.image_list = imageList;
    }

    public WechatData(String name, String content, String time, String sourceType, String pics) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.sourceType = sourceType;
        this.pics = pics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<String> image_list) {
        this.image_list = image_list;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
