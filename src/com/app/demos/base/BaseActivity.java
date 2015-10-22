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
        LogMy.d(this,"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogMy.d(this, "onStart");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        LogMy.d(this, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogMy.d(this,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogMy.d(this, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogMy.d(this, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogMy.d(this, "onDestroy");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogMy.d(this, "onActivityResult");
    }
}