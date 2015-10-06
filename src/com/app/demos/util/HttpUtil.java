package com.app.demos.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class HttpUtil {
	
	static public int WAP_INT = 1;
	static public int NET_INT = 2;
	static public int WIFI_INT = 3;
	static public int NONET_INT = 4;
	
	static private Uri APN_URI = null;
	
	static public int getNetType (Context ctx) {
		/*/ has network
		ConnectivityManager conn = null;
		try {
			conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (conn == null) {
			return HttpUtil.NONET_INT;
		}
		NetworkInfo info = conn.getActiveNetworkInfo();
		boolean available = info.isAvailable();
		if (!available){
			return HttpUtil.NONET_INT;
		}
		// check use wifi
		String type = info.getTypeName();
		if (type.equals("WIFI")) {
			return HttpUtil.WIFI_INT;
		}

		if(ConnectivityManager.TYPE_WIFI == networkType){
            //当前为wifi网络
        }else if(ConnectivityManager.TYPE_MOBILE == networkType){
            //当前为mobile网络
        }
		// check use wap

///!!!!!!!   下面会报错在android 4.2 以上  java.lang.SecurityException: No permission to write APN settings
//   如果必须要用的APN的信息，则要注意版本兼容性问题，在android4.2以上版本获取apn 的URI发生了变化。

		APN_URI = Uri.parse("content://telephony/carriers/preferapn");
		Cursor uriCursor = ctx.getContentResolver().query(APN_URI, null, null, null, null);
		if (uriCursor != null && uriCursor.moveToFirst()) {
			String proxy = uriCursor.getString(uriCursor.getColumnIndex("proxy"));
			String port = uriCursor.getString(uriCursor.getColumnIndex("port"));
			String apn = uriCursor.getString(uriCursor.getColumnIndex("apn"));
			if (proxy != null && port != null && apn != null && apn.equals("cmwap") && port.equals("80") &&
				(proxy.equals("10.0.0.172") || proxy.equals("010.000.000.172"))) {
				return HttpUtil.WAP_INT;
			}
		}
		return HttpUtil.NET_INT;
		*/
       return  0;
    }

	/**
	 * 判断是否有网络连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断网络类型
	 * @param context
	 * @return 返回值 -1：没有网络  1：WIFI网络 	2：wap网络	3：net网络
	 */
	public static int GetNetype(Context context)
	{
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo==null)
		{
			return netType;
		}
		int nType = networkInfo.getType();
		if(nType==ConnectivityManager.TYPE_MOBILE)
		{
			if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
			{
				netType = 3;
			}
			else
			{
				netType = 2;
			}
		}
		else if(nType==ConnectivityManager.TYPE_WIFI)
		{
			netType = 1;
		}
		return netType;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

}