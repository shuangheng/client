package com.app.demos.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-10-23.
 */
public class ListLoadFragment extends Fragment {
    private View mFragmentView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.tpl_list_speak_footer, container, false);
        mProgressBar = (ProgressBar) mFragmentView.findViewById(R.id.list_speak_footer_progressbar);
        mTextView = (TextView) mFragmentView.findViewById(R.id.list_speak_footer_hint_textview);
        return mFragmentView;
    }

    private View getFragmentView() {
        return mFragmentView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    /**
     *
     * @param loadStatus
     */
    public void setStatus(LoadStatus loadStatus) {
        switch (loadStatus) {
            case GONE:
                mProgressBar.setVisibility(View.GONE);
                mTextView.setVisibility(View.GONE);
                break;
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(getString(R.string.loading));
                break;
            case FAIL:
                mProgressBar.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(getString(R.string.click_refresh));
                break;
        }
    }



    public static enum LoadStatus {
         GONE(0),
        LOADING(1),
        FAIL(2);

        private int asInt;

        LoadStatus(int i) {
            this.asInt = i;
        }

        static LoadStatus fromInt(int i) {
            switch (i) {
                case 0:
                    return GONE;
                case 1:
                    return LOADING;
                default:
                case 2:
                    return FAIL;
            }
        }

        public int toInt() {
            return asInt;
        }
    }

}