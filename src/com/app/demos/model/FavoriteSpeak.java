package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-9-15.
 */
public class FavoriteSpeak extends BaseModel {

    // model columns
    public final static String COL_CUSTOMERID = "customerid";
    public final static String COL_SPEAKID = "speakId";
    public final static String COL_UPTIME = "uptime";

    private String customerid;
    private String uptime;
    private String speakId;

    public FavoriteSpeak() {
    }

    public FavoriteSpeak(String customerId, String speakId, String uptime) {
        this.speakId = speakId;
        this.customerid = customerId;
        this.uptime = uptime;
    }

    public String getCustomerId() {
        return customerid;
    }

    public String getUptime() {
        return uptime;
    }

    public String getSpeakId() {
        return speakId;
    }

    public void setCustomerId(String customerId) {
        this.customerid = customerId;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setSpeakId(String speakId) {
        this.speakId = speakId;
    }
}