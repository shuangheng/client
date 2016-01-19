package com.app.demos.model;

import com.app.demos.base.BaseModel;

/**
 * Created by tom on 15-9-15.
 */
public class ZhangBenCategory extends BaseModel {

    // model columns
    public final static String COL_RES_ID = "resId";// i -- CategoryUtils.faceImgs[i]
    public final static String COL_CATEGORY_NAME = "category_name";
    public final static String COL_USED = "used";

    private int resId;
    private String categoryName;
    private int used;

    public ZhangBenCategory() {
    }

    public ZhangBenCategory(int resId, String categoryName, int used) {
        this.resId = resId;
        this.categoryName = categoryName;
        this.used = used;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}