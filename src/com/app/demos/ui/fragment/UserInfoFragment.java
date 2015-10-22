package com.app.demos.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateAndSetupView(inflater, container, savedInstanceState, R.layout.lay2);
        textView = (TextView) view.findViewById(R.id.lay2_tv);
        button = (Button) view.findViewById(R.id.lay2_btn);
        button.setOnClickListener(this);
        return view;
    }

    private View inflateAndSetupView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState, int layoutResourceId) {
        View layout = inflater.inflate(layoutResourceId, container, false);

        return layout;
    }

    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        textView.setText(message.getResult());
    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("typeId", "0");
        blogParams.put("pageId", "0");
        doTaskAsync(C.task.gg, C.api.gg, blogParams, true);
    }
}
