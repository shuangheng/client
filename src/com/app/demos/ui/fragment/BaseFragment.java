package com.app.demos.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseTaskPool;
import com.app.demos.base.C;
import com.app.demos.util.BaseDevice;

import java.util.HashMap;

/**
 * Created by tom on 15-10-21.
 */
public class BaseFragment extends Fragment {
    private static final int FRAG_NETWORK_ERROR = 0;
    private static final int FRAG_TASK_COMPLETE = 1;
    protected Activity activity;
    protected BaseTaskPool taskPool;
    private BaseHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        taskPool = new BaseTaskPool(activity);
        handler = new BaseHandler(this);
    }

    public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs, Boolean showProgress) {
        if ( ! BaseDevice.isNetworkConnected(activity)) {
            hideProgressBar();
                toast(getString(R.string.not_network));
            return;
        }
        if (showProgress) {
            showProgressBar();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressBar();
                }
            }, 10000);
        }
        taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask() {
            @Override
            public void onComplete(String httpResult) {
                sendMessage(FRAG_TASK_COMPLETE, this.getId(), httpResult);
            }

            @Override
            public void onError(String error) {
                sendMessage(FRAG_NETWORK_ERROR, this.getId(), null);
            }
        }, 0);
    }

    public void onTaskComplete(int taskId, BaseMessage message) {
        hideProgressBar();
    }

    public void onTaskComplete(int taskId) {
        hideProgressBar();
    }

    public void onNetworkError(int taskId) {
        hideProgressBar();
        toast(C.err.message);
    }

    public void showProgressBar() {

    }

    public void hideProgressBar() {

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

    public void toast (String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        View view = View.inflate(activity, R.layout.toast, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(msg);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);//显示位置
        toast.show();
    }

}
