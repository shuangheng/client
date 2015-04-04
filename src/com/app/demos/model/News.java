package com.app.demos.model;

import com.app.demos.base.BaseModel;

public class News extends BaseModel {
	// model columns
		public final static String COL_ID = "id";
		public final static String COL_FACE = "face";
		public final static String COL_FACEURL = "faceurl";
		public final static String COL_WHO = "who";
		public final static String COL_TITLE = "title";		
		public final static String COL_CONTENT = "content";		
		public final static String COL_UPTIME = "uptime";
		
		
		
		private String id;
		private String face;
		private String faceurl;
		private String title;
		private String who;
		private String content;		
		private String uptime;		
		
		public News () {}
		
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
		
		public String getWho () {
			return this.who;
		}
		
		public void setWho (String who) {
			this.who = who;
		}
		
		public String getFaceUrl () {
			return this.faceurl;
		}
		
		public void setFaceUrl (String faceurl) {
			this.faceurl = faceurl;
		}
		
		public String getTitle () {
			return this.title;
		}
		
		public void setTitle (String id) {
			this.title = id;
		}
		
		public String getContent () {
			return this.content;
		}
		
		public void setContent (String id) {
			this.content = id;
		}
		
		public String getUptime () {
			return this.uptime;
		}
		
		public void setUptimt (String id) {
			this.uptime = id;
		}
}
