package com.app.demos.ui;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseAuth;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseService;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.model.Customer;
import com.app.demos.service.NoticeService;

public class UiMain extends BaseUi {
	private TextView tv;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check if login
		/*if (BaseAuth.isLogin()) {
			Log.e("isLogin","ok");
			this.forward(UiIndex.class);			
		}
		*/
		// set view after check login
		setContentView(R.layout.ui_main);		
		// remember password
		tv = (TextView) this.findViewById(R.id.tpl_list_find_tvContent);
		
		//自动登录
		settings = getSharedPreferences("login", MODE_PRIVATE);	
		if (settings.getBoolean("remember", false)) {
			doTaskLogin1();		
		}else{
			this.forward(UiIndex.class);
		}
	}
	
	private void doTaskLogin1() {
		app.setLong(System.currentTimeMillis());
		Log.e("login1","ok");
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("name", settings.getString("username", ""));
			urlParams.put("pass", settings.getString("password", ""));
			Log.e(settings.getString("username", ""),settings.getString("password", ""));
			try {
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
			case C.task.login:
				Log.e("onTask","");
				longinOrregister(message, R.string.msg_login_success, R.string.msg_loginfail);
				break;			
		}
	}
	
	private void longinOrregister(BaseMessage message,int ii, int i){
		Customer customer = null;
		// login logic
		try {
			customer = (Customer) message.getResult("Customer");
			// login success
			if (customer.getName() != null) {
				BaseAuth.setCustomer(customer);
				BaseAuth.setLogin(true);
				toast(this.getString(ii));
			// login fail
			} else {
				BaseAuth.setCustomer(customer); // set sid
				BaseAuth.setLogin(false);
				toast(this.getString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast(e.getMessage());
		}
		// login complete
		long startTime = app.getLong();
		long loginTime = System.currentTimeMillis() - startTime;
		Log.w("LoginTime", Long.toString(loginTime));
		// turn to index
		if (BaseAuth.isLogin()) {
			// start service
			BaseService.start(this, NoticeService.class);
			// turn to index
			forward(UiIndex.class);
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
		this.forward(UiIndex.class);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// other methods
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}



