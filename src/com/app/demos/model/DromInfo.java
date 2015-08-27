package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-8-13.
 */
public class DromInfo extends BaseModel {

    // model columns
    public final static String COL_ID = "id";
    public final static String COL_NUM = "num";
    public final static String COL_NAME = "name";
    public final static String COL_CONTENT = "content";
    public final static String COL_DISPLAY = "display";

    private String id;
    private String num;
    private String name;
    private String content;
    private String display;


    public DromInfo (String id, String num, String name, String content, String display) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.content = content;
        this.display = display;

    }

    public DromInfo() {

    }

    public String getId () {
        return this.id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent () {
        return this.content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
	
