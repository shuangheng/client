package com.app.demos.sqlite;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import com.app.demos.base.BaseSqlite;
import com.app.demos.model.DromInfo;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.Gonggao;

public class GonggaoSqlite extends BaseSqlite {

	public GonggaoSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "gonggao";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
				Gonggao.COL_ID,
				Gonggao.COL_USER,
				Gonggao.COL_BGIMAGE,
				Gonggao.COL_TITLE,
				Gonggao.COL_CONTENT,
				Gonggao.COL_SEECOUNT,
				Gonggao.COL_COMMENTCOUNT,
				Gonggao.COL_LIKECOUNT,
				Gonggao.COL_TYPE,
				Gonggao.COL_UPTIME,
				Gonggao.COL_FAVORITE
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE IF NOT EXISTS " + tableName() + " (" +
				Gonggao.COL_ID + " INTEGER PRIMARY KEY, " +
				Gonggao.COL_USER + " TEXT, " +
				Gonggao.COL_BGIMAGE + " TEXT, " +
				Gonggao.COL_TITLE + " TEXT, " +
				Gonggao.COL_CONTENT + " TEXT, " +
				Gonggao.COL_SEECOUNT + " TEXT, " +
				Gonggao.COL_COMMENTCOUNT + " TEXT, " +
				Gonggao.COL_LIKECOUNT + " TEXT, " +
				Gonggao.COL_TYPE + " TEXT, " +
				Gonggao.COL_UPTIME + " TEXT, " +
				Gonggao.COL_FAVORITE + " TEXT" +
				");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public boolean updateGonggao (Gonggao g) {
		// prepare g data
		ContentValues values = new ContentValues();
		values.put(Gonggao.COL_ID, g.getId());
		values.put(Gonggao.COL_USER, g.getUser());
		values.put(Gonggao.COL_BGIMAGE, g.getBgimage());
		values.put(Gonggao.COL_TITLE, g.getTitle());
		values.put(Gonggao.COL_CONTENT, g.getContent());
		values.put(Gonggao.COL_SEECOUNT, g.getSeecount());
		values.put(Gonggao.COL_COMMENTCOUNT, g.getCommentcount());
		values.put(Gonggao.COL_LIKECOUNT, g.getLikeCount());
		values.put(Gonggao.COL_TYPE, g.getType());
		values.put(Gonggao.COL_UPTIME, g.getUptime());
		values.put(Gonggao.COL_FAVORITE, g.getFavorite());
		// prepare sql
		String whereSql = Gonggao.COL_ID + "=?";
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
		return false;
	}

	public ArrayList<Gonggao> getAllGonggao () {
		ArrayList<Gonggao> gList = new ArrayList<Gonggao>();
		try {
			ArrayList<ArrayList<String>> rList = this.query2(null, null, null);
			int rCount = rList.size();
			for (int i = 0; i < rCount; i++) {
				ArrayList<String> rRow = rList.get(i);
				Gonggao g = new Gonggao();
				g.setId(rRow.get(0));
				g.setUser(rRow.get(1));
				g.setBgimage(rRow.get(2));
				g.setTitle(rRow.get(3));
				g.setContent(rRow.get(4));
				g.setSeecount(rRow.get(5));
				g.setCommentcount(rRow.get(6));
				g.setLikecount(rRow.get(7));
				g.setType(rRow.get(8));
				g.setUptime(rRow.get(9));
				g.setFavorite(rRow.get(10));
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
		return "CREATE TABLE IF NOT EXISTS " + "drominfo" + " (" +
				DromInfo.COL_ID + " INTEGER PRIMARY KEY, " +
				DromInfo.COL_NUM + " TEXT, " +
				DromInfo.COL_NAME + " TEXT, " +
				DromInfo.COL_DISPLAY + " TEXT, " +
				DromInfo.COL_CONTENT + " TEXT" +

				");";
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