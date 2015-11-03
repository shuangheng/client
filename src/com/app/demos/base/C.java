package com.app.demos.base;

import com.app.demos.R;

public final class C {
	public static final int[] find_imageIds = new int[]{R.drawable.s_15,
			R.drawable.s_19,
			R.drawable.s_2,
			R.drawable.s_8,
			R.drawable.s_24,
			R.drawable.s_28};

	public static final int[] colors = {R.color.color_primary_green_dark,R.color.color_primary_green_dark,
			R.color.color_primary_red,
			R.color.color_primary_red,
			R.color.accent_material_light,
			R.color.accent_material_light,
			R.color.indigo_700,
			R.color.indigo_700,
			R.color.ui_green,
			R.color.ui_green,
			R.color.switch_thumb_normal_material_dark,
			R.color.switch_thumb_normal_material_dark,
			R.color.color_primary_green,
			R.color.color_primary_green,
			R.color.cyan_500,
			R.color.cyan_500
	};

	////////////////////////////////////////////////////////////////////////////////////////////////
	// core settings (important)
	
	public static final class dir {
		public static final String base				= "/sdcard/demos";
		public static final String faces			= base + "/faces";
		public static final String images			= base + "/images";
	}
	
	public static final class api {
		public static final String base				= "http://120.24.97.52:8001";
		//public static final String base				= "http://10.0.2.2:8001";
		public static final String index			= "/index/index";
		public static final String register			= "/index/register";
		public static final String login			= "/index/login";
		public static final String forgotPwd 		= "/index/forgotPwd";
		public static final String sendMail = "/email/sendMail";
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
		public static final String ggCreate				= "/gonggao/ggCreate";
		public static final String ggnew			= "/gonggao/newList";
		public static final String newsList			= "/news/updateNews";
        public static final String find             = "/find/findList";
        public static final String find_release     = "/find/findCreate";
		public static final String favorite_speak	= "/favorite/favoriteList";
		public static final String favorite_speak_count	= "/favorite/favoriteCount";
		public static final String favorite_speak_create	= "/favorite/favoriteCreate";
		public static final String favorite_speak_delete	= "/favorite/favoriteDelete";

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
		public static final int favorite_speak		= 1026;
		public static final int favorite_speak_count		= 1027;
		public static final int favorite_speak_create		= 1028;
		public static final int favorite_speak_delete		= 1029;
		public static final int forgotPwd 			= 1030;
		public static final int sendMail 			= 1031;
		public static final int ggCreate 			= 1032;
	}
	
	public static final class err {
		public static final String network			= "网络不稳定！请稍后重试";
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
		public static final String base				= "http://120.24.97.52:8002";
		//public static final String base				= "http://10.0.2.2:8002";
		public static final String index			= base + "/index.php";
		public static final String agreement		= base + "/agreement/点亮富友软件许可及服务协议.php";
		public static final String gomap			= base + "/gomap.php";
		public static final String bgimage			= base + "/faces/default/l_";
		public static final String thumb_image		= base + "/thumb.php?filename=l_";
		public static final String save_upload_image		= base + "/save_upload_image.php";
	}

	int bgImage_s[] = {R.drawable.s_1,
			R.drawable.s_2,
			R.drawable.s_3,
			R.drawable.s_4,
			R.drawable.s_5,
			R.drawable.s_6,
			R.drawable.s_7,
			R.drawable.s_8,
			R.drawable.s_9,
			R.drawable.s_10,
			R.drawable.s_11,
			R.drawable.s_12,
			R.drawable.s_13,
			R.drawable.s_14,
			R.drawable.s_15,
			R.drawable.s_16,
			R.drawable.s_17,
			R.drawable.s_18,
			R.drawable.s_19,
			R.drawable.s_20,
			R.drawable.s_21,
			R.drawable.s_22,
			R.drawable.s_23,
			R.drawable.s_24,
			R.drawable.s_25,
			R.drawable.s_26,
			R.drawable.s_27,
			R.drawable.s_28,
			R.drawable.s_29,
			R.drawable.s_30};
}