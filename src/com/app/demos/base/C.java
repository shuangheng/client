package com.app.demos.base;

import com.app.demos.R;

public final class C {
	public static final int[] find_imageIds = new int[]{R.drawable.s_15,
			R.drawable.s_19,
			R.drawable.s_2,
			R.drawable.s_8,
			R.drawable.s_24,
			R.drawable.s_28};;

	////////////////////////////////////////////////////////////////////////////////////////////////
	// core settings (important)
	
	public static final class dir {
		public static final String base				= "/sdcard/demos";
		public static final String faces			= base + "/faces";
		public static final String images			= base + "/images";
	}
	
	public static final class api {
		public static final String base				= "http://10.0.2.2:8001";
		public static final String index			= "/index/index";
		public static final String register			= "/index/register";
		public static final String login			= "/index/login";
		public static final String logout			= "/index/logout";
		public static final String faceView 		= "/image/faceView";
		public static final String faceList 		= "/image/faceList";
		public static final String blogList			= "/blog/blogList";
		public static final String blogView			= "/blog/blogView";
		public static final String blogCreate		= "/blog/blogCreate";
		public static final String commentList		= "/comment/commentList";
		public static final String commentAllList	= "/comment/allCommentList";
		public static final String commentCreate	= "/comment/commentCreate";
		public static final String customerView		= "/customer/customerView";
		public static final String customerEdit		= "/customer/customerEdit";
		public static final String fansAdd			= "/customer/fansAdd";
		public static final String fansDel			= "/customer/fansDel";
		public static final String notice			= "/notify/notice";
		public static final String gg				= "/gonggao/ggList";
		public static final String ggnew			= "/gonggao/newList";
		public static final String newsList			= "/news/updateNews";
        public static final String find             = "/find/findList";
        public static final String find_release     = "/find/findCreate";
    }
	
	public static final class task {
		public static final int index				= 1001;
		public static final int login				= 1002;
		public static final int logout				= 1003;
		public static final int faceView			= 1004;
		public static final int faceList			= 1005;
		public static final int blogList			= 1006;
		public static final int blogView			= 1007;
		public static final int blogCreate			= 1008;
		public static final int commentList			= 1009;
		public static final int commentCreate		= 1010;
		public static final int customerView		= 1011;
		public static final int customerEdit		= 1012;
		public static final int fansAdd				= 1013;
		public static final int fansDel				= 1014;
		public static final int notice				= 1015;
		public static final int register			= 1016;
		public static final int gg					= 1017;
		public static final int gg1					= 1018;
		public static final int gg2					= 1019;
		public static final int newsList			= 1020;
		public static final int commentAllList		= 1021;
		public static final int commentListMore		= 1022;
        public static final int find                = 1023;
		public static final int find_more			= 1024;
		public static final int find_release		= 1025;
	}
	
	public static final class err {
		public static final String network			= "网络错误";
		public static final String message			= "消息错误";
		public static final String jsonFormat		= "消息格式错误";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// intent & action settings
	
	public static final class intent {
		public static final class action {
			public static final String EDITTEXT		= "com.app.demos.EDITTEXT";
			public static final String EDITBLOG		= "com.app.demos.EDITBLOG";
		}
	}
	
	public static final class action {
		public static final class edittext {
			public static final int CONFIG			= 2001;
			public static final int COMMENT			= 2002;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// additional settings
	
	public static final class web {
		public static final String a				= "http://218.28.167.99:8080/EssFunction.ashx";
		public static final String b				= "http://218.28.167.99:8080/Ws_EssFunction.ashx";
		public static final String c				= "http://218.28.167.99:8080/PushMsgFunction.ashx";
		public static final String d				= "http://218.28.167.99:8080/IWs_EssFunction.ashx";
		public static final String base				= "http://10.0.2.2:8002";
		public static final String index			= base + "/index.php";
		public static final String gomap			= base + "/gomap.php";
	}
}