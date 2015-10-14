package com.app.demos.base;

import java.util.ArrayList;
import java.util.HashMap;
import com.app.demos.R;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppUtil;
import com.app.demos.util.BaseDevice;
import com.app.demos.util.HttpUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class BaseUi extends BaseActivity {

	protected BaseApp app;
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;

    public static int DEVICE_WIDTH;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//隐藏标题栏
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);

		// debug memory
		debugMemory("onCreate");
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
		// init application
		this.app = (BaseApp) this.getApplicationContext();
        this.DEVICE_WIDTH = BaseDevice.getScreenWidth(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// debug memory
		debugMemory("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// debug memory
		debugMemory("onPause");
	}

	@Override
	public void onStart() {
		super.onStart();
		// debug memory
		debugMemory("onStart");
	}

	@Override
	public void onRestart() {
		super.onRestart();
		// debug memory
		debugMemory("onRestart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// debug memory
		debugMemory("onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// debug memory
		debugMemory("on销毁");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			default:
				break;
		}
		return true;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// util method

	public void toast (String msg) {
		Toast toast = new Toast(this);
		toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);//显示位置
		toast.show();
	}

	/**
	 * don't finish this Activity
	 * @param classObj
	 */
	public void overlay (Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        startActivity(intent);
	}

	/**
	 * don't finish this Activity
	 * @param classObj
	 */
	public void overlay (Class<?> classObj, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
	}

	/**
	 * finish this Activity
	 * @param classObj
	 */
	public void forward (Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * finish this Activity
	 * @param classObj
	 */
	public void forward (Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
	}

	public Context getContext () {
		return this;
	}

	public BaseHandler getHandler () {
		return this.handler;
	}

	public void setHandler (BaseHandler handler) {
		this.handler = handler;
	}

	public LayoutInflater getLayout () {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getLayout (int layoutId) {
		return getLayout().inflate(layoutId, null);
	}

	public View getLayout (int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}

	public BaseTaskPool getTaskPool () {
		return this.taskPool;
	}

	public void showLoadBar () {
		this.findViewById(R.id.main_load_bar).setVisibility(View.VISIBLE);
		this.findViewById(R.id.main_load_bar).bringToFront();
		showLoadBar = true;
	}

	public void hideLoadBar () {
		if (showLoadBar) {
			this.findViewById(R.id.main_load_bar).setVisibility(View.GONE);
			showLoadBar = false;
		}
	}

	public void openDialog(Bundle params) {
		new BaseDialog(this, params).show();
	}

	public void loadImage (final String url) {
		taskPool.addTask(0, new BaseTask(){
			@Override
			public void onComplete(){
				AppCache.getCachedImage(getContext(), url);
				sendMessage(BaseTask.LOAD_IMAGE);
			}
		}, 0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// logic method

	public void doFinish () {
		this.finish();
	}

	public void doLogout () {
		BaseAuth.setLogin(false);
	}

	public void doEditText () {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITTEXT);
		this.startActivity(intent);
	}

	public void doEditText (Bundle data) {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITTEXT);
		intent.putExtras(data);
		this.startActivity(intent);
	}

	public void doEditBlog () {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITBLOG);
		this.startActivity(intent);
	}

	public void doEditBlog (Bundle data) {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITBLOG);
		intent.putExtras(data);
		this.startActivity(intent);
	}

	public void sendMessage (int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	public void sendMessage (int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}

	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}

	public void doTaskAsync (int taskId, int delayTime) {
		taskPool.addTask(taskId, new BaseTask(){
			@Override
			public void onComplete () {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, delayTime);
	}

	public void doTaskAsync (int taskId, BaseTask baseTask, int delayTime) {
		taskPool.addTask(taskId, baseTask, delayTime);
	}

	public void doTaskAsync (int taskId, String taskUrl, Boolean showProgress) {
		if ( ! HttpUtil.isNetworkConnected(this)) {
			toast(getString(R.string.not_network));
			return;//stop moth
		}
			if (showProgress) {
				showProgressBar();
			}
			taskPool.addTask(taskId, taskUrl, new BaseTask() {
				@Override
				public void onComplete(String httpResult) {
					sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
				}

				@Override
				public void onError(String error) {
					sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
				}
			}, 0);

	}

	public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs, Boolean showProgress) {
		if ( ! HttpUtil.isNetworkConnected(this)) {
			toast(getString(R.string.not_network));
			return;
		}
			if (showProgress) {
				showProgressBar();
			}
			taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask() {
				@Override
				public void onComplete(String httpResult) {
					sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
				}

				@Override
				public void onError(String error) {
					sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
				}
			}, 0);
	}

	public void onTaskComplete (int taskId, BaseMessage message) {
		hideProgressBar();
	}

	//public ArrayList<? extends BaseModel> onTaskComplete1 (int taskId, BaseMessage message) {
		//return null;
	//}


	public void onTaskComplete (int taskId) {
		hideProgressBar();
	}

	public void onNetworkError (int taskId) {
		hideProgressBar();
		toast(C.err.network);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// debug method

	public void debugMemory (String tag) {
		if (this.showDebugMsg) {
			Log.w(this.getClass().getSimpleName(), tag+":"+AppUtil.getUsedMemory());
		}
	}

	public void onTaskComplete(String result) {
	}

	protected void hideProgressBar() {}

	protected void showProgressBar() {}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// common classes

	public class BitmapViewBinder implements ViewBinder {
		//
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}
}