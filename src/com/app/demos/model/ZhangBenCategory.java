package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-9-15.
 */
public class ZhangBenCategory extends BaseModel {

    // model columns
    public final static String COL_RES_ID = "resId";// CategoryUtils.faceImgs[i]
    public final static String COL_CATEGORY_NAME = "category_name";
    public final static String COL_USED = "used";

    private String resId;
    private String categoryName;
    private String used;

    public ZhangBenCategory() {
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }
}