package com.app.demos.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.app.demos.R;
import com.app.demos.base.BaseUi;

/**
 * Created by Administrator on 15-2-15.
 */
public class BaseDevice {
	static public int WAP_INT = 2;

	public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    public static int getScreenWidth0() {
        DisplayMetrics dm = new DisplayMetrics();
        new BaseUi().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        return screenW;
    }

	/**
	 * get view height width
	 * @param view
	 */
	public static int getViewHeight(final View view) {
		final int[] i = new int[1];
		ViewTreeObserver vto2 = view.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				i[0] = view.getHeight();
						//view.getWidth();
			}
		});
		return i[0];
	}


    public static void showInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // 显示软键盘 //
         imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        // 切换软键盘的显示与隐藏
        //imm.toggleSoftInputFromWindow(view.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
        //或者
        //                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的

    }

	/**
	 * not use fail !!!!
	 * @param context
	 * @param view
	 */
	public static void hideInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);//强制隐藏键盘
	}

	/**
	 *
	 * @param context
	 * @param view
	 */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//强制隐藏键盘
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
		imm.showSoftInput(view, 0);//显示键盘
        //(如果输入法在窗口上已经显示，则隐藏，反之则显示)
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        //（view为接受软键盘输入的视图，SHOW_FORCED表示强制显示）
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);


        //            4、获取输入法打开的状态
        //    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
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
	public static int getNetype(Context context)
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

	public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

	public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }
}
