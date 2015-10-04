package com.app.demos.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;
import com.app.demos.ui.fragment.Fragment2;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.FragmentNull;

/**
 * Created by tom on 15-6-29.
 */
public class UiCreateSpeak extends SwipeBackActivity implements View.OnClickListener {
    private EditText editContent;
    private ImageView ivBg;
    private ImageView ivRelease;
    private ImageView ivPicture;
    private ImageView ivExpression;
    private FrameLayout frameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_speak);

        editContent = (EditText) findViewById(R.id.ui_create_speak_edit_content);
        ivBg = (ImageView) findViewById(R.id.ui_create_speak_bg_image);
        ivExpression = (ImageView) findViewById(R.id.ui_create_speak_iv_expression);
        ivPicture = (ImageView) findViewById(R.id.ui_create_speak_iv_picture);

        ivPicture.setOnClickListener(this);
        ivExpression.setOnClickListener(this);

        editContent.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘

        toast("Onclick");
        switch (v.getId())
        {
            case R.id.ui_create_speak_iv_picture:
                fragment = new Fragment2();
                ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                imm.hideSoftInputFromInputMethod(editContent.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                toast("picture");

                break;
            case R.id.ui_create_speak_iv_expression:
                ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
                fragment = new Fragment3();
                imm.toggleSoftInput(2, InputMethodManager.HIDE_NOT_ALWAYS);
                toast("expression");
                break;
            default:
                fragment = new FragmentNull();
                break;
        }
        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
        ft.commit();
    }

    public void toast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
