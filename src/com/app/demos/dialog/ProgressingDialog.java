package com.app.demos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-9-25.
 */
public class ProgressingDialog {
    private Dialog mDialog;
    private TextView mTextMessage;

    public ProgressingDialog(Context context, Bundle params) {
        mDialog = new Dialog(context, R.style.com_app_weibo_theme_dialog);
        mDialog.setContentView(R.layout.progressing_dialog);
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        window.setAttributes(wl);
//		window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mTextMessage = (TextView) mDialog.findViewById(R.id.progressing_dialog_text);
        mTextMessage.setTextColor(context.getResources().getColor(R.color.gray));
        if (params != null) {
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText(params.getString("text"));
        }
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setCanceledOnTouchOutside(Boolean flag) {
        mDialog.setCanceledOnTouchOutside(flag);
    }

    public void setCancelable(boolean flag) {
        mDialog.setCancelable(flag);
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        mDialog.setCanceledOnTouchOutside(flag);
    }

    public void setMessage(String msg) {
        mTextMessage.setText(msg);
    }

}
