package com.app.demos.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.ButtonFloatSmall;
import com.app.demos.layout.ResizeLayout;
import com.app.demos.layout.ResizeLinearLayout;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.ui.fragment.FragmentOne;
import com.app.demos.ui.fragment.SelectPhotoFragment;
import com.app.demos.ui.fragment.UserInfoFragment;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.FragmentNull;
import com.app.demos.util.AppClient;
import com.app.demos.util.BaseDevice;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by tom on 15-6-29.
 */
public class UiCreateSpeak extends SwipeBackActivity implements View.OnClickListener, TextWatcher {
    private static final int BIGGER = 1;//about InputMode
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private static final int HEIGHT_THREADHOLD = 30;
    private static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    private static final int SELECT_PIC_BY_TACK_PHOTO = 2;
    private ResizeLinearLayout reSizelayout;
    private int default_ResizeLayout_height;
    private HandlerMy handlerMy;

    private EditText editContent;
    private ImageView ivBg;
    private ImageView ivRelease;
    private ButtonFloatSmall btnRelease;
    private ImageView ivPicture;
    private ImageView ivExpression;
    private FrameLayout frameLayout;
    /**
     * bottom bar status
     * <p>
     *     --00  --01  --10
     */
    private int bottomBarStatus = 0;
    //private String actionUrl = "http://10.0.2.2:8002/save_upload_image.php";
    private String actionUrl = C.web.save_upload_image;

    private String picPath = null;
    private boolean mFabIsShown = false;
    private View pupView;
    private PopupWindow popupwindow;
    private Uri photoUri;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_speak);

        handlerMy = new HandlerMy();
        initView();

        ViewHelper.setScaleX(btnRelease, 0);//隐藏按钮
        ViewHelper.setScaleY(btnRelease, 0);
    }

    private void initView() {
        //init toolBar
        TextView release = (TextView) LayoutInflater.from(this).inflate(R.layout.action_bar_menu_text_item, null);
        release.setText(getString(R.string.release));
        getToolBar(R.id.toolbar, getString(R.string.edit_speak), true);
        setCustomViewOnToolBar(release, Gravity.END);

        reSizelayout = (ResizeLinearLayout) findViewById(R.id.ui_create_speak_resizeLayout);
        editContent = (EditText) findViewById(R.id.ui_create_speak_edit_content);
        ivBg = (ImageView) findViewById(R.id.ui_create_speak_bg_image);
        ivExpression = (ImageView) findViewById(R.id.ui_create_speak_iv_expression);
        ivPicture = (ImageView) findViewById(R.id.ui_create_speak_iv_picture);
        btnRelease = (ButtonFloatSmall) findViewById(R.id.buttonFloat_small);

        ivBg.setOnClickListener(this);
        ivPicture.setOnClickListener(this);
        ivExpression.setOnClickListener(this);
        editContent.setOnClickListener(this);
        btnRelease.setOnClickListener(this);
        release.setOnClickListener(this);
        editContent.addTextChangedListener(this);

        reSizelayout.setOnResizeListener(new ResizeLinearLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;//default input hide
                if (h < oldh || h > oldh && h < default_ResizeLayout_height) {
                    if (h < oldh && default_ResizeLayout_height == 0) {
                        default_ResizeLayout_height = oldh;
                    }
                    change = SMALLER;//input is showing
                }

                Message msg = new Message();
                msg.what = MSG_RESIZE;
                msg.arg1 = change;
                handlerMy.sendMessage(msg);
            }
        });
    }

    private void creatSpeak(String customerId, String content, String bgimage) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("user", customerId);
        blogParams.put("title", customerId);
        blogParams.put("content", content);
        blogParams.put("bgimage", bgimage);
        this.doTaskAsync(C.task.ggCreate, C.api.ggCreate, blogParams, true);
    }
    /*
        private void creatSpeak2() {
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("lost_id", "4");
            blogParams.put("lost_item", "@@2");
            blogParams.put("item_summary", "@@131313");
            blogParams.put("where", "ccc");
            blogParams.put("content", "1311313131313123123123123123123122312312312312312312312312231223eerererererer");
            this.doTaskAsync(C.task.find_release, C.api.find_release, blogParams, true);
        }
    */
    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch (taskId) {
            case C.task.ggCreate:
                LogMy.w(UiCreateSpeak.this, message.getMessage());
                if (message.getCode().equals("10000")) {
                    editContent.getText().append("\n"+"ok!!!!!!!!!!");

                    return;
                }
                editContent.getText().append("\n"+"fail-------");
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new FragmentOne();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘

        switch (v.getId())
        {
            case R.id.ui_create_speak_bg_image:
                picPath = null;
                photoUri = null;
                ivBg.setImageBitmap(null);
                break;
            case R.id.action_bar_item_text:
                if (popupwindow == null) {
                    initPupView();
                    editContent.getText().append("\ninitPupView");
                }
                if (popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    editContent.getText().append("\npopupwindow.dismiss()");
                } else {
                    popupwindow.showAsDropDown(v);
                    editContent.getText().append("\npopupwindow.showAsDropDown()");
                }
                break;
            case R.id.buttonFloat_small:
                //creatSpeak2();
                //creatSpeak("g4050282", "111111223223");
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
                        creatSpeak("g4050282", editContent.getText().toString(), picPath.substring(picPath.lastIndexOf("/") + 1));
                    }
                }
                break;
            case R.id.ui_create_speak_iv_picture:
                fragment = new SelectPhotoFragment();
                switch (bottomBarStatus) {
                    case 1:
                        //editContent.getText().append("\nbottomBarStatus = 1");
                        fragment = new FragmentNull();
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        BaseDevice.showSoftInput(this, editContent);
                        break;
                    default:
                        //editContent.getText().append("\nbottomBarStatus = "+String.valueOf(bottomBarStatus));
                        if (imm.isActive()) {
                            BaseDevice.hideSoftInput(this, editContent);
                        }
                        ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
                        ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                        bottomBarStatus = 1;
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        break;
                }
                //initPupView(v);
                // pickPhoto();
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, 1);

                //fragment = new UserInfoFragment();
                //ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                //ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                //imm.hideSoftInputFromInputMethod(editContent.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                //imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0);
                //toast("picture");
                break;
            case R.id.ui_create_speak_iv_expression:
                fragment = new Fragment3();
                switch (bottomBarStatus) {
                    case 10:
                        //editContent.getText().append("\nbottomBarStatus_emoji = 10");
                        fragment = new FragmentNull();
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        BaseDevice.showSoftInput(this, editContent);
                        break;
                    default:
                        //editContent.getText().append("\nbottomBarStatus_emoji_default = "+bottomBarStatus);

                        ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
                        ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                        bottomBarStatus = 10;
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        if (imm.isActive()) {
                            BaseDevice.hideSoftInput(this, editContent);
                        }
                        break;
                }
                //toast("expression");
                break;
            default:
                //fragment = new FragmentNull();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径

             if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
             Bundle bundle = data.getExtras();
             Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
             ivBg.setImageBitmap(bitmap);
             return;
             }*/
            doPhoto(requestCode, data);
            //doPhoto2(requestCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doPhoto2(int requestCode, Intent data) {
        Uri uri = data.getData();
        switch (requestCode) {
            case SELECT_PIC_BY_PICK_PHOTO:

                LogMy.e(this, "uri = " + uri);
                try {
                    String[] pojo = {MediaStore.Images.Media.DATA};

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
                    e.printStackTrace();
                }
                break;
            case SELECT_PIC_BY_TACK_PHOTO:
                Bitmap photo = null;
                if (uri != null) {
                    photo = BitmapFactory.decodeFile(uri.getPath());
                }
                if (photo == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        photo = (Bitmap) bundle.get("data");
                    } else {
                        alert();
                        return;
                    }
                }


                ivBg.setImageBitmap(photo);
                break;
            default:
                break;
        }
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

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(btnRelease).cancel();
            ViewPropertyAnimator.animate(btnRelease).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(btnRelease).cancel();
            ViewPropertyAnimator.animate(btnRelease).scaleX(0).scaleY(0).setDuration(200).start();
            //btnRelease.setVisibility(View.GONE);
            mFabIsShown = false;
        }
    }

    private void initPupView() {
        pupView = LayoutInflater.from(this).inflate(R.layout.pup_list, null);  //获取自定义布局文件pop.xml的视图
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(pupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupwindow.setAnimationStyle(R.style.AnimationFade); // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setBackgroundDrawable(new BitmapDrawable());// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景

        // 使其聚集  下面3条是为了使 点击popuWindow 以外的区域能够关闭它
        popupwindow.setFocusable(false);
        popupwindow.setOutsideTouchable(true);// 设置允许在外点击消失
        popupwindow.update();//刷新状态（必须刷新否则无效）
        pupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        }); // 自定义view添加触摸事件

        String[] strings = {getString(R.string.camera), getString(R.string.get_pic_from_photo), getString(R.string.cancel)};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);
        listView = (ListView) pupView.findViewById(R.id.pup_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        pickPhoto();
                        break;
                    case 2:
                        popupwindow.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 拍照获取图片
     * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
     * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
     * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
     */
    private void takePhoto() {
        String SDState = Environment.getExternalStorageState();//执行拍照前，应该先判断SD卡是否存在
        if(SDState.equals(Environment.MEDIA_MOUNTED))
        {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            //intent = new Intent("android.media.action.IMAGE_CAPTURE");

            ContentValues values = new ContentValues();
            //photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            String imagePath = Environment.getExternalStorageDirectory() + "/Fuyou/temp";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoUri = Uri.fromFile(new File(imagePath, "111111.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        }else{
            Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    private void takePhoto2() {
        String SDState = Environment.getExternalStorageState();//执行拍照前，应该先判断SD卡是否存在
        if(SDState.equals(Environment.MEDIA_MOUNTED))
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        }else{
            Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() >= 1) {
            showFab();
        } else {
            hideFab();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 选择图片后，获取图片的路径
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode,Intent data) {
        switch (requestCode) {
            case SELECT_PIC_BY_TACK_PHOTO:
                if(photoUri != null ) {
                    picPath = photoUri.getPath();
                    editContent.getText().append(picPath+"---tt\n");
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    bitmap = ImageLoader.decodeUri(cr, photoUri);//show thumb image 防止内存泄露
                    ivBg.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
                }
                break;
            case SELECT_PIC_BY_PICK_PHOTO://从相册取图片，有些手机有异常情况，请注意
                if(data == null) {
                    Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                    return;
                }
                photoUri = data.getData();
                if(photoUri == null )
                {
                    Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                    return;
                }
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
                if(cursor != null ) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    editContent.getText().append(picPath+"tt\n");
                    if(Build.VERSION.SDK_INT < 14) {
                        cursor.close();//4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                    }
                }
                LogMy.i(this, "imagePath = " + picPath);
                if(picPath != null ) {
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    //bitmap = BitmapFactory.decodeStream(cr.openInputStream(photoUri));
                    bitmap = ImageLoader.decodeUri(cr, photoUri);//show thumb image 防止内存泄露
                    ivBg.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    public void clickOnCamera(View view) {
        takePhoto();
    }

    public void clickOnAlbum(View view) {
        pickPhoto();
    }

    // inner classes

    private class HandlerMy extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE: {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = new FragmentNull();
                    ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                    ft.commit();
                    //if (msg.arg1 == BIGGER) {//inputMode is hidding
                    ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                    ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
                    bottomBarStatus = 0;
                    //}
                }
                break;
                default:
                    break;
            }
        }
    }

}
