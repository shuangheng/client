package com.app.demos.base;

public class BaseTask {

	public static final int TASK_COMPLETE = 0;
	public static final int NETWORK_ERROR = 1;
	public static final int SHOW_LOADBAR = 2;
	public static final int HIDE_LOADBAR = 3;
	public static final int SHOW_TOAST = 4;
	public static final int LOAD_IMAGE = 5;
	public static final int TEST_FoxconnEss = 6;

	public static final int IMAGE_LOADING = 7;
	public static final int IMAGE_LOAD_FAIL = 8;
	public static final int IMAGE_LOAD_OK = 9;
	public static final int FRAG_TASK_COMPLETE = 10;
	public static final int FRAG_NETWORK_ERROR = 11;

	private int id = 0;
	private String name = "";
	
	public BaseTask() {}
	
	public int getId () {
		return this.id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void onStart () {
//		Log.w("BaseTask", "onStart");
	}
	
	public void onComplete () {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onComplete (String httpResult) {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onError (String error) {
//		Log.w("BaseTask", "onError");
	}
	
	public void onStop () throws Exception {
//		Log.w("BaseTask", "onStop");
	}
	
}