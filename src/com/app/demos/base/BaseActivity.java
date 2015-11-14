package com.app.demos.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by tom on 15-10-14.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMy.e(this,"onCreate");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        LogMy.e(this, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogMy.e(this, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogMy.e(this,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogMy.e(this, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogMy.e(this, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogMy.e(this, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogMy.e(this, "onActivityResult");
    }
}
