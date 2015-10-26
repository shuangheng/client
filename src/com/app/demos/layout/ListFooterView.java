package com.app.demos.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-10-26.
 */
public class ListFooterView extends LinearLayout {
    private View parentView;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public ListFooterView(Context context) {
        this(context, null);
    }

    public ListFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        parentView = LayoutInflater.from(context).inflate(R.layout.tpl_list_speak_footer, this);
        mProgressBar = (ProgressBar) parentView.findViewById(R.id.list_speak_footer_progressbar);
        mTextView = (TextView) parentView.findViewById(R.id.list_speak_footer_hint_textview);
        setStatus(LoadStatus.LOADING);
    }

    public View getParentView() {
        return parentView;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public TextView getTextView() {
        return mTextView;
    }

    /**
     * set View status
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
                mTextView.setText(getResources().getString(R.string.loading));
                break;
            case END:
                mProgressBar.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(getResources().getString(R.string.load_end));
                break;
            case FAIL:
                mProgressBar.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(getResources().getString(R.string.click_refresh));
                break;
        }
    }



    public static enum LoadStatus {
        GONE(0),
        LOADING(1),
        FAIL(2),
        END(3);

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
                case 2:
                    return FAIL;
                default:
                case 3:
                    return END;

            }
        }


        public int toInt() {
            return asInt;
        }
    }
}
