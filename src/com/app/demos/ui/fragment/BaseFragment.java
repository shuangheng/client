package com.app.demos.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseTaskPool;
import com.app.demos.base.C;
import com.app.demos.util.AppUtil;
import com.app.demos.util.HttpUtil;

import java.util.HashMap;

/**
 * Created by tom on 15-10-21.
 */
public class BaseFragment extends Fragment {
    private static final int FRAG_NETWORK_ERROR = 0;
    private static final int FRAG_TASK_COMPLETE = 1;
    private Activity activity;
    protected BaseTaskPool taskPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        taskPool = new BaseTaskPool(activity);
    }

    public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs, Boolean showProgress) {
        if ( ! HttpUtil.isNetworkConnected(activity)) {
            hideProgressBar();
                toast(getString(R.string.not_network));
            return;
        }
        if (showProgress) {
            showProgressBar();
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

    private void onTaskComplete(int taskId, BaseMessage message) {
        hideProgressBar();
    }

    private void onTaskComplete(int taskId) {
        hideProgressBar();
    }

    private void onNetworkError(int taskId) {
        hideProgressBar();
        toast(C.err.message);
    }

    private void showProgressBar() {

    }

    private void hideProgressBar() {

    }

    public void sendMessage (int what, int taskId, String data) {
        Bundle b = new Bundle();
        b.putInt("task", taskId);
        b.putString("data", data);
        Message m = new Message();
        m.what = what;
        m.setData(b);
        Handler handler = new HandlerMy();
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

    private class HandlerMy extends Handler {

        @Override
        public void handleMessage(Message msg) {
            try {
                int taskId;
                String result;
                switch (msg.what) {
                    case FRAG_TASK_COMPLETE:
                        taskId = msg.getData().getInt("task");
                        result = msg.getData().getString("data");
                        if (result != null) {
                            onTaskComplete(taskId, AppUtil.getMessage(result));
                        } else if (!AppUtil.isEmptyInt(taskId)) {
                            onTaskComplete(taskId);
                        } else {
                            toast(C.err.message);
                        }
                        break;
                    case FRAG_NETWORK_ERROR:
                        taskId = msg.getData().getInt("task");
                        onNetworkError(taskId);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                toast(e.getMessage());
            }
        }
    }



}
