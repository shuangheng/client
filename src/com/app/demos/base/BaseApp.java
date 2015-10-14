package com.app.demos.base;

import android.app.Application;
import android.content.Context;

public class BaseApp extends Application {

	private static Context context;
	private String s;
	private long l;
	private int i;

	@Override
	public void onCreate() {
		context = getApplicationContext();
	}

	public  static Context getContext() {
		return context;
	}
	public int getInt () {
		return i;
	}
	
	public void setInt (int i) {
		this.i = i;
	}
	
	public long getLong () {
		return l;
	}
	
	public void setLong (long l) {
		this.l = l;
	}
	
	public String getString () {
		return s;
	}
	
	public void setString (String s) {
		this.s = s;
	}
}