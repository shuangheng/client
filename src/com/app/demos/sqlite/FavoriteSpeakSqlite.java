package com.app.demos.sqlite;

import android.content.ContentValues;
import android.content.Context;

import com.app.demos.base.BaseSqlite;
import com.app.demos.model.DromInfo;
import com.app.demos.model.FavoriteSpeak;

import java.util.ArrayList;

/**
 * Created by tom on 15-9-15.
 */
public class FavoriteSpeakSqlite extends BaseSqlite {

    public FavoriteSpeakSqlite(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return "favorite_speak";
    }

    @Override
    protected String[] tableColumns() {
        String[] columns = {
                FavoriteSpeak.COL_CUSTOMERID,
                FavoriteSpeak.COL_SPEAKID,
                FavoriteSpeak.COL_UPTIME
        };
        return columns;
    }

    @Override
    protected String createSql() {
        return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
                FavoriteSpeak.COL_CUSTOMERID + " TEXT, " +
                FavoriteSpeak.COL_SPEAKID + " TEXT, " +
                FavoriteSpeak.COL_UPTIME + " TEXT" +
                ");";
    }

    @Override
    protected String upgradeSql() {
        return "DROP TABLE IF EXISTS " + tableName();
    }

    public boolean updateFavoriteSpeak(FavoriteSpeak g) {
        // prepare g data
        ContentValues values = new ContentValues();
        values.put(FavoriteSpeak.COL_CUSTOMERID, g.getCustomerId());
        values.put(FavoriteSpeak.COL_SPEAKID, g.getSpeakId());
        values.put(FavoriteSpeak.COL_UPTIME, g.getUptime());

        // prepare sql
        String whereSql = FavoriteSpeak.COL_SPEAKID + "=?";
        String[] whereParams = new String[]{g.getSpeakId()};
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

    public boolean delete(FavoriteSpeak d) {
        // prepare sql
        String whereSql = FavoriteSpeak.COL_SPEAKID + "=?";
        String[] whereParams = new String[]{d.getSpeakId()};
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

    public ArrayList<FavoriteSpeak> getAll() {
        ArrayList<FavoriteSpeak> gList = new ArrayList<FavoriteSpeak>();
        try {
            ArrayList<ArrayList<String>> rList = this.query2(null, null, null);
            int rCount = rList.size();
            for (int i = 0; i < rCount; i++) {
                ArrayList<String> rRow = rList.get(i);
                FavoriteSpeak g = new FavoriteSpeak();
                g.setCustomerId(rRow.get(0));
                g.setSpeakId(rRow.get(1));
                g.setUptime(rRow.get(2));

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