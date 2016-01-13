package com.app.demos.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.app.demos.base.BaseSqlite;
import com.app.demos.base.C;
import com.app.demos.model.DromInfo;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.ZhangBenCategory;
import com.app.demos.model.ZhangBenLocation;
import com.app.demos.ui.test.zhangBen.CategoryUtils;

import java.util.ArrayList;

/**
 * Created by tom on 15-9-15.
 */
public class ZhangBenCategorySqlite extends BaseSqlite {

    public ZhangBenCategorySqlite(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return C.string.zhangBenCategory;
    }

    @Override
    protected String[] tableColumns() {
        String[] columns = {
                ZhangBenCategory.COL_RES_ID,
                ZhangBenCategory.COL_CATEGORY_NAME,
                ZhangBenCategory.COL_USED
        };
        return columns;
    }

    @Override
    protected String createSql() {
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                ZhangBenCategory.COL_RES_ID + " TEXT, " +
                ZhangBenCategory.COL_CATEGORY_NAME + " TEXT, " +
                ZhangBenCategory.COL_USED + " integer" +
                ");";
    }

    @Override
    protected String upgradeSql() {
        return "DROP TABLE IF EXISTS " + tableName();
    }

    public boolean updateZhangBenCategory(ZhangBenCategory g) {
        // prepare g data
        ContentValues values = new ContentValues();
        values.put(ZhangBenCategory.COL_RES_ID, g.getResId());
        values.put(ZhangBenCategory.COL_CATEGORY_NAME, g.getCategoryName());
        values.put(ZhangBenCategory.COL_USED, g.getUsed());

        // prepare sql
        String whereSql = ZhangBenCategory.COL_CATEGORY_NAME + "=?";
        String[] whereParams = new String[]{g.getCategoryName()};
        // create or update
        try {
            if (this.exists(whereSql, whereParams)) {
                this.update(values, whereSql, whereParams);
            } else {
                this.create(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(ZhangBenCategory d) {
        // prepare sql
        String whereSql = ZhangBenCategory.COL_CATEGORY_NAME + "=?";
        String[] whereParams = new String[]{d.getCategoryName()};
        // create or update
        try {
            if (this.exists(whereSql, whereParams)) {
                this.delete(whereSql, whereParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 初始化 data
     * @return
     */
    public Boolean createData() {
        Boolean isCreateOk = false;
        ContentValues values = new ContentValues();
        for (int i = 0; i < CategoryUtils.faceImgNames.length; i++) {
            values.put(ZhangBenCategory.COL_RES_ID, CategoryUtils.faceImgs[i]);
            values.put(ZhangBenCategory.COL_CATEGORY_NAME, CategoryUtils.faceImgNames[i]);
            values.put(ZhangBenCategory.COL_USED, "0");
            if (this.create(values))
                isCreateOk = true;
            values.clear();
        }
        return isCreateOk;
    }

    public ArrayList<ZhangBenCategory> getAll() {
        ArrayList<ZhangBenCategory> gList = new ArrayList<ZhangBenCategory>();
        try {
            ArrayList<ArrayList<String>> rList = this.query2(null, null, null, ZhangBenCategory.COL_USED+" desc");
            int rCount = rList.size();
            for (int i = 0; i < rCount; i++) {
                ArrayList<String> rRow = rList.get(i);
                ZhangBenCategory g = new ZhangBenCategory();
                g.setResId(rRow.get(0));
                g.setCategoryName(rRow.get(1));
                g.setUsed(rRow.get(2));

                gList.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gList;
    }

    @Override
    protected String createSql2() {
        // TODO Auto-generated method stub
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                DromInfo.COL_ID + " INTEGER PRIMARY KEY, " +
                DromInfo.COL_NUM + " TEXT, " +
                DromInfo.COL_NAME + " TEXT, " +
                DromInfo.COL_DISPLAY + " TEXT, " +
                DromInfo.COL_CONTENT + " TEXT" +

                ");";
    }

    @Override
    protected String createSql3() {
        return null;
    }
}