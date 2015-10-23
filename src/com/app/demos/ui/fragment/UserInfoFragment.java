package com.app.demos.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.C;

import java.util.HashMap;

/**
 * Created by tom on 15-3-27.
 */

public class UserInfoFragment extends BaseFragment implements View.OnClickListener {
    private TextView textView;
    private Button button;
    private ProgressBar mProgressBar;
    private View mProgressBarContinar;
    private ImageView mReload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay2, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_load_progress);
        mProgressBarContinar = view.findViewById(R.id.fragment_load_progress_continar);
        mReload = (ImageView) view.findViewById(R.id.fragment_load_reload_image);
        textView = (TextView) view.findViewById(R.id.lay2_tv);
        button = (Button) view.findViewById(R.id.lay2_btn);
        button.setOnClickListener(this);
        mReload.setOnClickListener(this);
        return view;
    }

    public void loadData() {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("typeId", "0");
        blogParams.put("pageId", "0");
        doTaskAsync(C.task.gg, C.api.gg, blogParams, true);
    }

    @Override
    public void showProgressBar() {
        mProgressBarContinar.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBarContinar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mReload.setVisibility(View.GONE);
    }

    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        textView.setText(message.getResult());
    }

    @Override
    public void onNetworkError(int taskId) {
        super.onNetworkError(taskId);
        mReload.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mProgressBarContinar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        loadData();
    }
}
