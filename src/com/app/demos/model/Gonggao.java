package com.app.demos.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.demos.base.BaseModel;

public class Gonggao extends BaseModel implements Parcelable {
	
	// model columns
	public final static String COL_ID = "id";
    public final static String COL_FACE = "face";
    public final static String COL_BGIMAGE = "bgimage";
	public final static String COL_TITLE = "title";	
	public final static String COL_CONTENT = "content";
	public final static String COL_COMMENT = "comment";
	public final static String COL_UPTIME = "uptime";
    public final static String COL_LIKECOUNT = "likecount";
    public final static String COL_SEECOUNT = "seecount";
    public final static String COL_USER = "user";
    public final static String COL_TYPE = "type";
    public final static String COL_COMMENTCOUNT = "commentcount";
    public final static String COL_FAVORITE = "favorite";

    public static final Parcelable.Creator<Gonggao> CREATOR = new Parcelable.Creator<Gonggao>() {
        @Override
        public Gonggao createFromParcel(Parcel source) {
            Gonggao g = new Gonggao();
            g.id = source.readString();
            g.face = source.readString();
            g.bgimage = source.readString();
            g.title = source.readString();
            g.content = source.readString();
            g.comment = source.readString();
            g.uptime = source.readString();
            g.likecount = source.readString();
            g.seecount = source.readString();
            g.user = source.readString();
            g.type = source.readString();
            g.commentcount = source.readString();
            g.favorite = source.readString();
            return g;
        }

        @Override
        public Gonggao[] newArray(int size) {
            return new Gonggao[size];
        }
    };

	private String id;
    private String face;
    private String bgimage;
	private String title;	
	private String content;
	private String comment;
	private String uptime;
    private String likecount;
    private String seecount;
	private String user;
    private String type;
    private String commentcount;
	private String favorite;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(face);
        dest.writeString(bgimage);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(comment);
        dest.writeString(uptime);
        dest.writeString(likecount);
        dest.writeString(seecount);
        dest.writeString(user);
        dest.writeString(type);
        dest.writeString(commentcount);
        dest.writeString(favorite);
    }

    public Gonggao () {}
	
	public Gonggao (String id, String user, String title, String content, String seecount, String uptime ) {
		this.id = id;
		this.user = user;
		this.title = title;
		this.content = content;
		this.seecount = seecount;
		this.uptime =  uptime;
	}

	@Override
	public int describeContents() {
		return 0;
	}


	
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
	
		
	public String getTitle () {
		return this.title;
	}
	
	public void setTitle (String title) {
		this.title = title;
	}
	
	
	public String getContent () {
		return this.content;
	}
	
	public void setContent (String content) {
		this.content = content;
	}

    public String getLikeCount () {
        return this.likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }
	public String getComment () {
		return this.comment;
	}
	
	public void setComment (String comment) {
		this.comment = comment;
	}
	
	public String getUptime () {
		return this.uptime;
	}
	
	public void setUptime (String uptime) {
		this.uptime = uptime;
	}
	
	public String getSeecount () {
		return this.seecount;
	}
	
	public void setSeecount (String uptime) {
		this.seecount = uptime;
	}
	
	public String getUser () {
		return this.user;
	}
	
	public void setUser (String uptime) {
		this.user = uptime;
	}

    public String getType(){
        return this.type;
    }

    public void setType (String type){
        this.type = type;
    }

    public String getCommentcount() {
        return this.commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

	public String getFavorite() {
		return favorite;
	}

	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}


	public String getBgimage() {
        return this.bgimage;
    }

    public void setBgimage(String bgimage) {
        this.bgimage = bgimage;
    }
    
    public String getTypeAll() {
		return "评论 "+this.commentcount;
    }

}