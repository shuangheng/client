package com.app.demos.ui.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.ui.fragment.Fragment2;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.FragmentOne;

public class UiCreateSpeakTest extends FragmentActivity implements View.OnClickListener {

    private ImageView ivPicture;
    private ImageView ivExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_create_speak_test);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.id_content, new Fragment2(),"ONE");
        tx.commit();


        ivExpression = (ImageView) findViewById(R.id.ui_create_speak_iv_expression);
        ivPicture = (ImageView) findViewById(R.id.ui_create_speak_iv_picture);

        ivPicture.setOnClickListener(this);
        ivExpression.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        toast("Onclick");
        switch (v.getId())
        {
            case R.id.ui_create_speak_iv_picture:
                fragment = new Fragment2();
                toast("picture");

                break;
            case R.id.ui_create_speak_iv_expression:

                fragment = new Fragment3();
                toast("expression");
                break;
            default:
                break;
        }
        ft.replace(R.id.id_content, fragment);
        ft.commit();
    }

    public void toast (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
