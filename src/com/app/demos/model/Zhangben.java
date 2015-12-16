package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-12-16.
 */
public class Zhangben extends BaseModel {

    // model columns
    public final static String COL_ID = "id";
    public final static String COL_TIME = "time";
    public final static String COL_ONE = "one";
    public final static String COL_TWO = "two";
    public final static String COL_THREE = "three";
    public final static String COL_LOCATION = "location";
    public final static String COL_MONEY = "money";

    private String id;
    private String time;
    private String one;
    private String two;
    private String three;
    private String location;
    private String money;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}