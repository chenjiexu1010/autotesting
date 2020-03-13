package com.micrmsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeqee on 2016/1/29.
 */
public class CloneRequestModel {
    private int maxPublish;
    private List<CloneModel> cloneList = new ArrayList<CloneModel>();

    public int getMaxPublish() {
        return maxPublish;
    }

    public void setMaxPublish(int maxPublish) {
        this.maxPublish = maxPublish;
    }

    public List<CloneModel> getCloneList() {
        return cloneList;
    }

    public void setCloneList(List<CloneModel> cloneList) {
        this.cloneList = cloneList;
    }
}


