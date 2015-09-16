package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-9-15.
 */
public class FavoriteSpeak extends BaseModel {

    // model columns
    public final static String COL_CUSTOMERID = "customerId";
    public final static String COL_SPEAKID = "speakId";
    public final static String COL_UPTIME = "uptime";

    private String customerId;
    private String uptime;
    private String speakId;

    public FavoriteSpeak() {
    }

    public FavoriteSpeak(String customerId, String speakId, String uptime) {
        this.speakId = speakId;
        this.customerId = customerId;
        this.uptime = uptime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getUptime() {
        return uptime;
    }

    public String getSpeakId() {
        return speakId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public void setSpeakId(String speakId) {
        this.speakId = speakId;
    }
}