package com.app.demos.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.app.demos.base.BaseSqlite;
import com.app.demos.model.DromInfo;
import com.app.demos.model.Gonggao;

import java.util.ArrayList;

/**
 * Created by tom on 15-8-13.
 */
public class DromInfoSqlite extends BaseSqlite {

    public DromInfoSqlite(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return "drominfo";
    }

    @Override
    protected String[] tableColumns() {
        String[] columns = {
                DromInfo.COL_ID,
                DromInfo.COL_NUM,
                DromInfo.COL_NAME,
                DromInfo.COL_DISPLAY,
                DromInfo.COL_CONTENT
        };
        return columns;
    }

    @Override
    protected String createSql() {
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                DromInfo.COL_ID + " INTEGER PRIMARY KEY, " +
                DromInfo.COL_NUM + " TEXT, " +
                DromInfo.COL_NAME + " TEXT, " +
                DromInfo.COL_DISPLAY + " TEXT, " +
                DromInfo.COL_CONTENT + " TEXT" +

                ");";
    }

    @Override
    protected String upgradeSql() {
        return "DROP TABLE IF EXISTS " + tableName();
    }

    public boolean updateDromInfo(DromInfo g) {
        // prepare g data
        ContentValues values = new ContentValues();
        values.put(DromInfo.COL_ID, g.getId());
        values.put(DromInfo.COL_NUM, g.getNum());
        values.put(DromInfo.COL_NAME, g.getName());
        values.put(DromInfo.COL_CONTENT, g.getContent());
        values.put(DromInfo.COL_DISPLAY, g.getDisplay());

        // prepare sql
        String whereSql = DromInfo.COL_ID + "=?";
        String[] whereParams = new String[]{g.getId()};
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

    public boolean delete(DromInfo d) {
        // prepare sql
        String whereSql = DromInfo.COL_ID + "=?";
        String[] whereParams = new String[]{d.getId()};
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

    public ArrayList<DromInfo> getAll() {
        ArrayList<DromInfo> gList = new ArrayList<DromInfo>();
        try {
            ArrayList<ArrayList<String>> rList = this.query2(null, null);
            int rCount = rList.size();
            for (int i = 0; i < rCount; i++) {
                ArrayList<String> rRow = rList.get(i);
                DromInfo g = new DromInfo();
                g.setId(rRow.get(0));
                g.setNum(rRow.get(1));
                g.setName(rRow.get(2));
                g.setDisplay(rRow.get(3));
                g.setContent(rRow.get(4));


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
}