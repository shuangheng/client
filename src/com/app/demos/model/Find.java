package com.app.demos.model;

import com.app.demos.base.BaseModel;

public class Find extends BaseModel{
	private String id;
	private String lost_id;
	private String lost_item;
	private String summary;
	private String face;
	private String images;
	private String images_content;
	private String where;
	private String clue_count;
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

    public String getClue_count() {
        return clue_count;
    }

    public void setClue_count(String clue_count) {
        this.clue_count = clue_count;
    }

    public String getWhere() {

        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getFace() {

        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getLost_id() {

        return lost_id;
    }

    public void setLost_id(String lost_id) {
        this.lost_id = lost_id;
    }

    public String getImages () {
		return this.images;
	}
	
	public void setImages (String url) {
		this.images = url;
	}

	public String getImages_content() {
		return images_content;
	}

	public void setImages_content(String images_content) {
		this.images_content = images_content;
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

    public void setLost_item(String lost_item) {
        this.lost_item = lost_item;
    }

	public String getLost_item() {
        return lost_item;

    }

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
