package com.app.demos.base;

import com.app.demos.ui.fragment.BaseFragment;
import com.app.demos.util.AppUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class BaseHandler extends Handler {
	
	protected BaseUi ui;
	protected BaseFragment fragment;

	public BaseHandler (BaseUi ui) {
		this.ui = ui;
	}
	
	public BaseHandler (Looper looper) {
		super(looper);
	}

	public BaseHandler(BaseFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
			switch (msg.what) {
				case BaseTask.TASK_COMPLETE:
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					if (result != null) {
						ui.onTaskComplete(taskId, AppUtil.getMessage(result));
					} else if (!AppUtil.isEmptyInt(taskId)) {
						ui.onTaskComplete(taskId);
					} else {
						ui.toast(C.err.message);
					}
					break;
				case BaseTask.TEST_FoxconnEss:
					result = msg.getData().getString("data");
					if (result != null) {
						ui.onTaskComplete(result);
					} else {
						ui.toast(C.err.message);
					}
					break;
				case BaseTask.NETWORK_ERROR:
					taskId = msg.getData().getInt("task");
					ui.onNetworkError(taskId);
					break;
				case BaseTask.SHOW_LOADBAR:
					ui.showLoadBar();
					break;
				case BaseTask.HIDE_LOADBAR:
					ui.hideLoadBar();
					break;
				case BaseTask.SHOW_TOAST:
					ui.hideLoadBar();
					result = msg.getData().getString("data");
					ui.toast(result);
					break;
				case BaseTask.FRAG_TASK_COMPLETE:
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					if (result != null) {
						fragment.onTaskComplete(taskId, AppUtil.getMessage(result));
					} else if (!AppUtil.isEmptyInt(taskId)) {
						fragment.onTaskComplete(taskId);
					} else {
						fragment.toast(C.err.message);
					}
					break;
				case BaseTask.FRAG_NETWORK_ERROR:
					taskId = msg.getData().getInt("task");
					fragment.onNetworkError(taskId);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ui.toast(e.getMessage());
		}
	}
	
}