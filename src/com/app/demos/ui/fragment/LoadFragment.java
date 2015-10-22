package com.app.demos.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.demos.R;

/**
 * Created by tom on 15-10-21.
 */
public class LoadFragment extends Fragment {
    private ProgressBar mProgressBar;
    private ImageView mReload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_load_progress);
        mReload = (ImageView) view.findViewById(R.id.fragment_load_reload_image);
        return view;
    }

    public void setStatus(Boolean isLoading) {
        if (isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
            mReload.setVisibility(View.GONE);
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        mReload.setVisibility(View.VISIBLE);
    }

    public ImageView getmReload() {
        return mReload;
    }

}
