package com.common.weedfs;

/**
 * Created by Jeqee on 2016-08-11.
 */
public class RequestResult {
    boolean success;
    int size;
    String fid;

    public String getPublicURL() {
        return PublicURL;
    }

    public void setPublicURL(String publicURL) {
        PublicURL = publicURL;
    }

    String PublicURL;
    @Override
    public String toString() {
        return "WriteRequestResult [success=" + success + ", size=" + size + ", fid="
                + fid + "]";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RequestResult () {
    }

    public RequestResult (boolean success, String fid) {
        this.success = success;
        this.fid = fid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

}
