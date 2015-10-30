package com.app.demos.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;
import com.app.demos.ui.fragment.FragmentOne;
import com.app.demos.ui.fragment.UserInfoFragment;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.FragmentNull;
import com.app.demos.util.AppClient;

import java.io.File;
import java.util.HashMap;

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
    //private String actionUrl = "http://10.0.2.2:8002/save_upload_image.php";
    private String actionUrl = C.web.base +"/faces/default/";

    private String picPath = null;

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

    private void creatSpeak(String customerId, String content) {
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("user", customerId);
            blogParams.put("content", content);
            this.doTaskAsync(C.task.ggCreate, C.api.ggCreate, blogParams, true);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘

        switch (v.getId())
        {
            case R.id.ui_create_speak_iv_picture:
                fragment = new FragmentOne();
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

                //fragment = new UserInfoFragment();
                //ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                //ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                //imm.hideSoftInputFromInputMethod(editContent.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                //imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0);
                toast("picture");
                break;
            case R.id.ui_create_speak_iv_expression:
                fragment = new Fragment3();
                if (picPath == null) {

                    Toast.makeText(UiCreateSpeak.this, "请选择图片！", Toast.LENGTH_SHORT).show();
                } else {
                    final File file = new File(picPath);

                    if (file != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                AppClient.uploadFile(picPath, actionUrl);
                            }
                        }).start();
                        Toast.makeText(this, "doing uplod_image", Toast.LENGTH_LONG).show();
                        creatSpeak("g4050282", editContent.getText().toString());
                    }
                }

                ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);

                editContent.requestFocus();
                //imm.toggleSoftInput(2, InputMethodManager.HIDE_NOT_ALWAYS);
                //ImmUtil.showSoftInput();
                toast("expression");
                break;
            default:
                fragment = new FragmentNull();
                break;
        }
        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            LogMy.e(this, "uri = " + uri);
            try {
                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    /***
                     * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr
                                .openInputStream(uri));
                        ivBg.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }

}
