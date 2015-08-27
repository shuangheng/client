package com.app.demos.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;


import com.app.demos.base.BaseSqlite;
import com.app.demos.model.Blogg;
import com.app.demos.model.DromInfo;
import com.app.demos.model.Gonggao;

public class BlogSqlite extends BaseSqlite {

	public BlogSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "blogs";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = {
			Blogg.COL_ID,
			Blogg.COL_FACE,
			Blogg.COL_TITLE,
			Blogg.COL_CONTENT,
			Blogg.COL_COMMENT,			
			Blogg.COL_UPTIME
		};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" +
			Blogg.COL_ID + " INTEGER PRIMARY KEY, " +
			Blogg.COL_FACE + " TEXT, " +
			Blogg.COL_TITLE + " TEXT, " +
			Blogg.COL_CONTENT + " TEXT, " +
			Blogg.COL_COMMENT + " TEXT, " +
			//Blogg.COL_AUTHOR + " TEXT, " +
			Blogg.COL_UPTIME + " TEXT" +
			");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public boolean updateBlog (Blogg blog) {
		// prepare blog data
		ContentValues values = new ContentValues();
		values.put(Blogg.COL_ID, blog.getId());
		values.put(Blogg.COL_FACE, blog.getFace());
		values.put(Blogg.COL_TITLE, blog.getTitle());
		values.put(Blogg.COL_CONTENT, blog.getContent());
		values.put(Blogg.COL_COMMENT, blog.getComment());	
		values.put(Blogg.COL_UPTIME, blog.getUptime());	
		// prepare sql
		String whereSql = Blogg.COL_ID + "=?";
		String[] whereParams = new String[]{blog.getId()};
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

	public ArrayList<Blogg> getAllBlogs () {
		ArrayList<Blogg> blogList = new ArrayList<Blogg>();
		try {			
			ArrayList<ArrayList<String>> rList = this.query2(null, null);
			int rCount = rList.size();
			for (int i = 0; i < rCount; i++) {				
				ArrayList<String> rRow = rList.get(i);
				Blogg blog = new Blogg();
				blog.setId(rRow.get(0));				
				blog.setFace(rRow.get(1));
				blog.setTitle(rRow.get(2));
				blog.setContent(rRow.get(3));
				blog.setComment(rRow.get(4));				
				blog.setUptime(rRow.get(5));
				blogList.add(blog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blogList;
	}

	@Override
	protected String createSql2() {
		// TODO Auto-generated method stub
        return "CREATE TABLE " + "drominfo" + " (" +
				DromInfo.COL_ID + " INTEGER PRIMARY KEY, " +
				DromInfo.COL_NAME + " TEXT, " +
				DromInfo.COL_CONTENT + " TEXT" +

				");";
	}
}