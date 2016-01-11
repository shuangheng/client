package com.app.demos.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.app.demos.base.BaseSqlite;
import com.app.demos.base.C;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.Zhangben;

import java.util.ArrayList;

/**
 * Created by tom on 15-12-16.
 */
public class ZhangbenSqlite extends BaseSqlite {

    public ZhangbenSqlite(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return C.string.zhanBen;
    }

    @Override
    protected String[] tableColumns() {
        String[] columns = {
                Zhangben.COL_ID,
                Zhangben.COL_TIME,
                Zhangben.COL_ONE,
                Zhangben.COL_TWO,
                Zhangben.COL_THREE,
                Zhangben.COL_LOCATION,
                Zhangben.COL_MONEY
        };
        return columns;
    }

    @Override
    protected String createSql() {
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                Zhangben.COL_ID + " INTEGER PRIMARY KEY, " +
                Zhangben.COL_TIME + " TEXT, " +
                Zhangben.COL_ONE + " TEXT, " +
                Zhangben.COL_TWO + " TEXT, " +
                Zhangben.COL_THREE + " TEXT, " +
                Zhangben.COL_LOCATION + " TEXT, " +
                Zhangben.COL_MONEY + " TEXT " +
                ");";
    }

    @Override
    protected String upgradeSql() {
        return "DROP TABLE IF EXISTS " + tableName();
    }

    public boolean updateZhangben (Zhangben g) {
        // prepare g data
        ContentValues values = new ContentValues();
        //values.put(Zhangben.COL_ID, g.getId());
        values.put(Zhangben.COL_TIME, g.getTime());
        values.put(Zhangben.COL_ONE, g.getOne());
        values.put(Zhangben.COL_TWO, g.getTwo());
        values.put(Zhangben.COL_THREE, g.getThree());
        values.put(Zhangben.COL_LOCATION, g.getLocation());
        values.put(Zhangben.COL_MONEY, g.getMoney());
        // prepare sql
        String whereSql = Zhangben.COL_ID + "=?";
        String[] whereParams = new String[]{g.getId()};
        // create or update
        try {
            if (whereParams[0] != null && this.exists(whereSql, whereParams)) {
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

    public ArrayList<Zhangben> getAllZhangben (String limit) {
        ArrayList<Zhangben> gList = new ArrayList<Zhangben>();
        try {
            ArrayList<ArrayList<String>> rList = this.query2(null, null, limit, "id desc");
            int rCount = rList.size();
            for (int i = 0; i < rCount; i++) {
                ArrayList<String> rRow = rList.get(i);
                Zhangben g = new Zhangben();
                g.setId(rRow.get(0));
                g.setTime(rRow.get(1));
                g.setOne(rRow.get(2));
                g.setTwo(rRow.get(3));
                g.setThree(rRow.get(4));
                g.setLocation(rRow.get(5));
                g.setMoney(rRow.get(6));
                gList.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gList;
    }

    @Override
    protected String createSql2() {

        return null;
    }

    @Override
    protected String createSql3() {
        return "CREATE TABLE IF NOT EXISTS " + "favorite_speak" + " (" +
                FavoriteSpeak.COL_CUSTOMERID + " TEXT, " +
                FavoriteSpeak.COL_SPEAKID + " TEXT, " +
                FavoriteSpeak.COL_UPTIME + " TEXT" +
                ");";
    }
}