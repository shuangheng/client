package com.app.demos.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.dialog.ProgressingDialog;
import com.app.demos.layout.ButtonFloatSmall;
import com.app.demos.layout.ResizeLinearLayout;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.ui.authenticator.UiAuthenticator;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.FragmentNull;
import com.app.demos.ui.fragment.FragmentOne;
import com.app.demos.ui.fragment.SelectPhotoFragment;
import com.app.demos.ui.fragment.emoji.EmojiFragment;
import com.app.demos.ui.fragment.emoji.EmojiParser;
import com.app.demos.ui.fragment.emoji.ParseEmojiMsgUtil;
import com.app.demos.ui.uploadFile.SelectPicActivity;
import com.app.demos.ui.uploadFile.UploadUtill;
import com.app.demos.util.AppClient;
import com.app.demos.util.BaseDevice;
import com.app.demos.util.BitmapUtil;
import com.app.demos.util.TimeUtil;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 15-11-12.
 */
public class UiCreateSpeak_v1 extends BaseUi implements View.OnClickListener, UploadUtill.OnUploadProcessListener, TextWatcher {

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2; //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;
    /***
     * 这里的这个URL是我服务器的PHP环境URL
     */
    private static String requestURL = C.web.save_upload_image;
    private Button selectButton,uploadButton;
    private String picPath = null;

    private static final int BIGGER = 1;//about InputMode
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private static final int HEIGHT_THREADHOLD = 30;
    private static final int SELECT_PIC_BY_PICK_PHOTO = 1;
    private static final int SELECT_PIC_BY_TACK_PHOTO = 2;
    private int default_ResizeLayout_height;

    private EditText editContent;
    private ImageView ivBg;
    private ImageView ivRelease;
    private ImageView ivPicture;
    private ImageView ivExpression;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    /**
     * bottom bar status
     * <p>
     *     --00  --01  --10
     */
    private int bottomBarStatus = 0;

    private boolean mFabIsShown = true;
    private View pupView;
    private PopupWindow popupwindow;
    private Uri photoUri;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ProgressingDialog presDialog;
    private ImageView release;
    private String empno;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_speak);
        if (!sharedPreferences_speak.getBoolean("isLogined", false)) {
            UiAuthenticator.actionStart(this, 1);
            return;
        }
        initView();

        hideFab();
        empno = sharedPreferences_speak.getString("empno", null);
        LogMy.e(this, "oncreat");
    }

    @Override
    public void onPause() {
        reSetBottomBar();
        super.onPause();
    }

    public void reSetBottomBar() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new FragmentNull();
        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
        ft.commit();
        ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_photo);
        ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
        bottomBarStatus = 0;
    }

    private void initView() {
        //init toolBar
        release = (ImageView) LayoutInflater.from(this).inflate(R.layout.action_bar_menu_image_item, null);
        release.setImageResource(R.drawable.ic_send_light);
        getToolBar(R.id.toolbar, getString(R.string.edit_speak), true);
        setCustomViewOnToolBar(release, Gravity.END);

        linearLayout = (LinearLayout) findViewById(R.id.ui_create_speak_Layout);
        editContent = (EditText) findViewById(R.id.ui_create_speak_edit_content);
        ivBg = (ImageView) findViewById(R.id.ui_create_speak_bg_image);
        ivExpression = (ImageView) findViewById(R.id.ui_create_speak_iv_expression);
        ivPicture = (ImageView) findViewById(R.id.ui_create_speak_iv_picture);

        presDialog = new ProgressingDialog(this, null);
        presDialog.setCanceledOnTouchOutside(false);

        ivBg.setOnClickListener(this);
        ivPicture.setOnClickListener(this);
        ivExpression.setOnClickListener(this);
        editContent.setOnClickListener(this);
        release.setOnClickListener(this);
        editContent.setOnClickListener(this);
        editContent.addTextChangedListener(this);
    }

    /**
     *
     * @param customerId 工号
     * @param content 内容
     * @param bgimage image file name
     */
    private void creatSpeak(String customerId, String content, String bgimage) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("user", customerId);
        blogParams.put("content", content);
        if (bgimage != null) {
            blogParams.put("bgimage", bgimage);
        }
        this.doTaskAsync(C.task.ggCreate, C.api.ggCreate, blogParams, true);
    }

    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch (taskId) {
            case C.task.ggCreate:
                LogMy.w(this, message.getMessage());
                if (message.getCode().equals("10000")) {
                    editContent.getText().append("\n"+"ok-------");
                    //finish();
                    return;
                }
                editContent.getText().append("\n"+"fail-------");
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new SelectPhotoFragment();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘

        switch (v.getId()) {
            case R.id.ui_create_speak_bg_image:
                picPath = null;
                photoUri = null;
                ivBg.setImageBitmap(null);
                break;
            case R.id.ui_create_speak_edit_content:
                fragment = new FragmentNull();
                ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                ft.commit();
                ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
                bottomBarStatus = 0;
                break;
            case R.id.action_bar_item_image:
                presDialog.show();
                //String content = EmojiFragment.getStringToServer(UiCreateSpeak_v1.this, editContent);
                String msgStr = ParseEmojiMsgUtil.convertToMsg(editContent.getText(), this);// 这里不要直接用mEditMessageEt.getText().toString();
                String content = EmojiParser.getInstance(this).parseEmoji(msgStr);
                LogMy.e(this, content);
                editContent.getText().append(content);
                if (picPath == null) {
                    creatSpeak(empno, content, null);
                } else {
                    final File file = new File(picPath);

                    if (file != null) {
                        compressFile(file);//压缩image
                        handler.sendEmptyMessage(TO_UPLOAD_FILE);//上传图片
                    }
                }
                break;
            case R.id.ui_create_speak_iv_picture:
                switch (bottomBarStatus) {
                    case 1:
                        fragment = new FragmentNull();
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        BaseDevice.showSoftInput(this, editContent);
                        ivPicture.setImageResource(R.drawable.ic_publish_operation_bar_photo);
                        bottomBarStatus = 0;
                        break;
                    default:
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
                break;
            case R.id.ui_create_speak_iv_expression:
                switch (bottomBarStatus) {
                    case 10:
                        fragment = new FragmentNull();
                        ft.replace(R.id.ui_create_speak_fragment_layout, fragment);
                        ft.commit();
                        BaseDevice.showSoftInput(this, editContent);
                        ivExpression.setImageResource(R.drawable.ic_publish_operation_bar_emoticon);
                        bottomBarStatus = 0;
                        break;
                    default:
                        fragment = EmojiFragment.newInstance("", editContent);
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
                break;
            default:
                break;
        }

    }

    private void compressFile(File file) {
        if (file.length()/1024 > 300) {
            String imagePath = C.dir.base2 + "/temp";
            File file2 = new File(imagePath);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File f = new File(imagePath,
                    empno + "_"+ TimeUtil.long2String2(System.currentTimeMillis()) + ".jpg");
            Bitmap bitmap = BitmapUtil.getCompressImage(picPath, 720f, 1280f);//压缩大小
            InputStream is = BitmapUtil.compressImage2(bitmap, 300);//压缩质量
            try {
                OutputStream os = new FileOutputStream(f);
                ImageLoader_my.CopyStream(is, os);//save image
                os.flush();
                os.close();
                picPath = f.getPath();
                editContent.getText().append("\n"+picPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(release).cancel();
            ViewPropertyAnimator.animate(release).scaleX(1).scaleY(1).setDuration(200).start();
            release.setEnabled(true);
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(release).cancel();
            ViewPropertyAnimator.animate(release).scaleX(0).scaleY(0).setDuration(200).start();
            release.setEnabled(false);
            mFabIsShown = false;
        }
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
            String imagePath = C.dir.base2 + "/temp";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            photoUri = Uri.fromFile(new File(imagePath,
                    empno + "_"+ TimeUtil.long2String2(System.currentTimeMillis()) + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
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
        if (s.toString().trim().length() != 0) {
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

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        //progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    private void toUploadFile()
    {

        String fileKey = "uploadedfile";
        UploadUtill uploadUtil = UploadUtill.getInstance();;
        uploadUtil.setOnUploadProcessListener(this); //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "11111");
        uploadUtil.uploadFile(picPath, fileKey, requestURL, params);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;

                case UPLOAD_INIT_PROCESS:
                   // progressBar.setMax(msg.arg1);
                    break;
                case UPLOAD_IN_PROCESS:
                    //progressBar.setProgress(msg.arg1);
                    //uploadImageResult.setText("setProgress  :  "+msg.arg1);
                    break;
                case UPLOAD_FILE_DONE:
                    String imageFileName = picPath.substring(picPath.lastIndexOf("/") + 1);
                    creatSpeak(sharedPreferences_speak.getString("empno", null), editContent.getText().toString(), imageFileName);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg );
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg );
    }

    @Override
    protected void hideProgressBar() {
        super.hideProgressBar();
        presDialog.dismiss();
    }
}
