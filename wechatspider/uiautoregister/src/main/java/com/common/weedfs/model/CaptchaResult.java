package com.common.weedfs.model;

public class CaptchaResult {
    private String id;
    private String code;

    public CaptchaResult(String id, String code) {
        this.id = id;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
