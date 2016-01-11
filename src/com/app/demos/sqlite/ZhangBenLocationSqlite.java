package com.app.demos.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.app.demos.base.BaseSqlite;
import com.app.demos.base.C;
import com.app.demos.model.DromInfo;
import com.app.demos.model.ZhangBenLocation;

import java.util.ArrayList;

/**
 * Created by tom on 15-9-15.
 */
public class ZhangBenLocationSqlite extends BaseSqlite {

    public ZhangBenLocationSqlite(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return C.string.zhangBenLocation;
    }

    @Override
    protected String[] tableColumns() {
        String[] columns = {
                ZhangBenLocation.COL_LOCATON,
                ZhangBenLocation.COL_USED,
        };
        return columns;
    }

    @Override
    protected String createSql() {
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                ZhangBenLocation.COL_LOCATON + " TEXT, " +
                ZhangBenLocation.COL_USED + " TEXT" +
                ");";
    }

    @Override
    protected String upgradeSql() {
        return "DROP TABLE IF EXISTS " + tableName();
    }

    public boolean updateZhangBenLocation(ZhangBenLocation g) {
        // prepare g data
        ContentValues values = new ContentValues();
        values.put(ZhangBenLocation.COL_LOCATON, g.getLocation());
        values.put(ZhangBenLocation.COL_USED, g.getUsed());

        // prepare sql
        String whereSql = ZhangBenLocation.COL_LOCATON + "=?";
        String[] whereParams = new String[]{g.getLocation()};
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

    public boolean delete(ZhangBenLocation d) {
        // prepare sql
        String whereSql = ZhangBenLocation.COL_LOCATON + "=?";
        String[] whereParams = new String[]{d.getLocation()};
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

    public Boolean createData() {
        Boolean createOk = false;
        ContentValues values = new ContentValues();
        for (int i = 0; i < C.array.locations.length; i++) {
            values.put(ZhangBenLocation.COL_LOCATON, C.array.locations[i]);
            values.put(ZhangBenLocation.COL_USED, "0");
            if (this.create(values))
                createOk = true;
            values.clear();
        }
        return createOk;
    }

    public int getLocationUsed(String location) {
        String whereSql = ZhangBenLocation.COL_LOCATON + "=?";
        String[] whereParams = new String[]{location};
        ArrayList<ArrayList<String>> rList = this.query2(null, null, null, ZhangBenLocation.COL_USED +" desc");
        String used = rList.get(0).get(1);
        return Integer.parseInt(used);
    }
    public ArrayList<String> getAllLocation(String limit) {
        ArrayList<String> gList = new ArrayList<String>();
        try {
            ArrayList<ArrayList<String>> rList = this.query2(null, null, limit, ZhangBenLocation.COL_USED +" desc");
            int rCount = rList.size();
            for (int i = 0; i < rCount; i++) {
                ArrayList<String> rRow = rList.get(i);
                String location = (rRow.get(0));
                gList.add(location);
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