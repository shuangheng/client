package com.app.demos.model;

import com.app.demos.base.BaseModel;

public class Find extends BaseModel{
	private String id;
	private String image;
	private String content;
	private String uptime;
	
	public Find () {}
	
	public Find(String string, String string2) {
		// TODO Auto-generated constructor stub
		this.content=string;
		this.uptime= string2;
	}

	public String getId () {
		return this.id;
	}
	
	public void setId (String id) {
		this.id = id;
	}
	
	public String getImage () {
		return this.image;
	}
	
	public void setImage (String url) {
		this.image = url;
	}
	
	public String getContent () {
		return this.content;
	}
	
	public void setContent (String type) {
		this.content = type;
	}
	
	public String getUptime () {
		return this.uptime;
	}
	
	public void setUptime (String uptime) {
		this.uptime = uptime;
	}
}
