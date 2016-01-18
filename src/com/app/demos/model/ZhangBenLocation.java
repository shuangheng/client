package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-9-15.
 */
public class ZhangBenLocation extends BaseModel {

    // model columns
    public final static String COL_LOCATON = "location";
    public final static String COL_USED = "used";

    private String location;
    private String used;

    public ZhangBenLocation() {
    }

    public ZhangBenLocation(String location, String used) {
        this.location = location;
        this.used = used;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}