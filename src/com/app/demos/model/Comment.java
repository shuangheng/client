package com.app.demos.model;

import com.app.demos.base.BaseModel;

public class Comment extends BaseModel {
	
	// model columns
	public final static String COL_ID = "id";
	public final static String COL_CONTENT = "content";
	public final static String COL_UPTIME = "uptime";
	
	private String id;
	private String face;
	private String content;
	private String type;
	private String uptime;
	

	
	public String getId () {
		return this.id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getFace () {
		return this.face;
	}
	
	public void setFace (String face) {
		this.face = face;
	}
	
	public String getContent () {
		return this.content;
	}
	
	public void setContent (String content) {
		this.content = content;
	}
	
	public String getType () {
		return this.type;
	}
	
	public void setType (String type) {
		this.type = type;
	}
	
	public String getUptime () {
		return this.uptime;
	}
	
	public void setUptime (String uptime) {
		this.uptime = uptime;
	}
}