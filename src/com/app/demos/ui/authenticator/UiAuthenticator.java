package com.app.demos.ui.authenticator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.base.baseActionbar.BaseUiActionBar;
import com.app.demos.dialog.ProgressingDialog;
import com.app.demos.layout.materialEditText.MaterialEditText;
import com.app.demos.layout.materialEditText.MaterialEditTextComplete;
import com.app.demos.ui.UiActionBar;
import com.app.demos.ui.webview.BaseWebView;
import com.app.demos.util.foxconn_ESS_zsf.AppClient;

import java.util.HashMap;

/**
 * Created by tom on 15-9-23.
 */
public class UiAuthenticator extends BaseUi {
    private TextView protocol_link;
    private TextView forgotPwd;
    private LinearLayout linearLayout;
    private MaterialEditText editText_empno;
    private MaterialEditText editText_emppwd;
    private MaterialEditTextComplete editText_email;
    private Button btn_auth;
    private TextView textView3;
    private String TAG = UiAuthenticator.class.getName();
    private ProgressingDialog presDialog;
    private View pupView;
    private PopupWindow popupwindow;
    private ArrayAdapter<String> adapter;
    private String[] emailSuffix;
    private ListView listView;
    private int startType;
    private String empno;
    private String emppwd;
    private String email;
    private SharedPreferences sharedPreferences_speak;
    private String authType;
    private Toolbar mToolbar;
    private TextView textView;
    private MenuItem meunItem;

    /**
     * start UiAuthenticator
     * @param context
     * @param paramInt (1 --login, 2 --register, 3 --forgot password)
     */
    public static void actionStart(Context context, int paramInt)
    {
        Intent localIntent = new Intent( context, UiAuthenticator.class);
        localIntent.putExtra("authenticatorType", paramInt);
        context.startActivity(localIntent);
    }

    private void emailAutoCompleteView() {
        pupView = getLayoutInflater().inflate(R.layout.pup_list, null);  //获取自定义布局文件pop.xml的视图
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(pupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailSuffix);
        listView = (ListView) pupView.findViewById(R.id.pup_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText_email.setText(emailSuffix[position]);
            }
        });
    }

    private void initView()
    {
        //this.linearLayout = ((LinearLayout)findViewById(2131492967));
        editText_empno = ((MaterialEditText)findViewById(R.id.edit_empno));
        editText_emppwd = ((MaterialEditText)findViewById(R.id.edit_emppwd));
        editText_email = (MaterialEditTextComplete) findViewById(R.id.edit_email);
        protocol_link = (TextView) findViewById(R.id.protocol_link);
        btn_auth = ((Button)findViewById(R.id.auth_login_button));
        forgotPwd = (TextView) findViewById(R.id.forgot_password);
        //Bundle bundle = new Bundle();
        //bundle.putString("text", "请稍等...");
        presDialog = new ProgressingDialog(this, null);
        presDialog.setCanceledOnTouchOutside(false);
        //this.textView3 = ((TextView)findViewById(21314929qi73));
        //this.tv = ((TextView)findViewById(2131492974));
        //this.textView = ((TextView)findViewById(2131492975));
        sharedPreferences_speak = getSharedPreferences("fragment_speak", Context.MODE_PRIVATE);
    }

    @Override
    public void onTaskComplete(String s) {
        super.onTaskComplete(s);
        authType = s.split(",")[0];
        toast(s);
        int code = Integer.parseInt(authType);
        if (code != 3) {
            doRegister(empno, emppwd, email);
        } else {
            presDialog.dismiss();
            toast(this.getResources().getString(R.string.empno_not_saved));
        }
        /*
        switch(code) {
            case 0:
                toast(this.getResources().getString(R.string.password_wrong));
                break;
            case 1:
                if (startType == 1) {
                    finish();
                }
                //forward(UiActionBar.class);
                break;
            case 3:
                toast(this.getResources().getString(R.string.empno_not_saved));
                break;
            case 4:
                toast(this.getResources().getString(R.string.empno_activation));
                break;

        }*/
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        presDialog.dismiss();
    }

    @Override
    protected void showProgressBar() {
        super.showProgressBar();
        presDialog.show();

    }

    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        //presDialog.dismiss();
        switch (taskId) {
            case C.task.login:
                String code = message.getCode();
                if (code.equals("10000")){
                    doLoginOk();
                } else {
                    toast(getResources().getString(R.string.msg_loginfail));
                }
                break;
            case C.task.register:
                String code1 = message.getCode();
                if (code1.equals("10000")){
                    doRegisterOk();
                } else if (code1.equals("14007")) {
                    toast(getResources().getString(R.string.msg_register_fail_empno_exist));
                } else {
                    toast(getResources().getString(R.string.msg_register_fail));
                }
                break;
            case C.task.forgotPwd:
                String code2 = message.getCode();
                if (code2.equals("10000")){
                    doSendMail();
                    toast(getString(R.string.sendEmail_ok_password));
                    finish();
                } else toast(getResources().getString(R.string.empno_and_email_not_match));
                break;
        }
    }

    private void doRegisterOk() {
        SharedPreferences.Editor editor = sharedPreferences_speak.edit();//获取编辑器
        editor.putBoolean("isRegistered", true);
        editor.putBoolean("isLogined", true);
        editor.putString("empno", empno);
        //editor.putString("empwd", emppwd);
        editor.putString("email", email);
        editor.commit();//提交修改
        UiActionBar.actionStart(this);
        finish();
    }

    private void doLoginOk() {
        SharedPreferences.Editor editor = sharedPreferences_speak.edit();//获取编辑器
        editor.putBoolean("isLogined", true);
        editor.putString("empno", empno);
        //editor.putString("empwd", emppwd);
        editor.commit();//提交修改
        UiActionBar.actionStart(this);
        finish();
    }

    public void clickOnButton(View paramView) {
        empno = this.editText_empno.getText().toString();
        emppwd = this.editText_emppwd.getText().toString().trim();
        email = editText_email.getText().toString();
        if (empno.length() != 0) {
            switch (startType) {
                case 1:
                    doLogin(empno, emppwd);
                    break;
                case 2:
                    doAuth(empno, emppwd);
                    break;
                case 3:
                    doGetNewPwd(empno, email);
            }

        } else if (empno.length() == 0) {
            toast(getResources().getString(R.string.empno_not_null));
        }
    }

    private void doGetNewPwd(String empno,String email) {
        if (email.length() > 7) {
            //presDialog.show();
            HashMap<String, String> urlparams = new HashMap<String, String>();
            urlparams.put("name", empno);
            urlparams.put("email", email);
            doTaskAsync(C.task.forgotPwd, C.api.forgotPwd, urlparams);
        } else {
            toast(getResources().getString(R.string.email_wrong));
        }
    }

    private void doSendMail() {
        HashMap<String, String> urlparams = new HashMap<String, String>();
        urlparams.put("title", empno);
        urlparams.put("content", empno);
        urlparams.put("toemail", email);
        doTaskAsync(C.task.sendMail, C.api.sendMail, urlparams);
    }

    private void doLogin(String empno, String emppwd) {
        if (emppwd.length() >3) {
            //presDialog.show();
            HashMap<String, String> urlParams = new HashMap<String, String>();
            urlParams.put("name", empno);
            urlParams.put("pass", emppwd);
            this.doTaskAsync(C.task.login, C.api.login, urlParams);
        } else {
            toast(getString(R.string.emppwd_wrong));
        }
    }

    private void doRegister(String empno, String emppwd, String email) {
        if (email.length() > 7 && emppwd.length() >3) {
            HashMap<String, String> urlparams = new HashMap<String, String>();
            urlparams.put("name", empno);
            urlparams.put("pass", emppwd);
            urlparams.put("email", email);
            urlparams.put("authType", authType);
            doTaskAsync(C.task.register, C.api.register, urlparams);
        } else if (emppwd.length() < 4) {
            toast(getResources().getString(R.string.emppwd_wrong));
        } else if (email.length() < 8) {
            toast(getResources().getString(R.string.email_wrong));
        }
    }

    /**
     * 通过 FoxconnEss server 验证 empno
     * @param empno
     * @param emppwd
     */
    private void doAuth(final String empno, final String emppwd) {
        if (emppwd.length() > 3 && email.length() > 8) {
            presDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run");
                    try {
                        String s = AppClient.UserLoginIn(empno, emppwd);
                        sendMessage(BaseTask.TEST_FoxconnEss, s);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(BaseTask.NETWORK_ERROR);
                    }
                }
            }).start();
        } else  if (emppwd.length() < 4){
            toast(getString(R.string.emppwd_wrong));
        } else if (email.length() < 8) {
            toast(getString(R.string.email_wrong));
        }
    }

    public void clickOnForgotPassword(View paramView)
    {
        if (startType != 3) {
            UiAuthenticator.actionStart(this, 3);
        } else {
            finish();
        }
    }

    public void clickOnProtocol(View paramView)
    {
        BaseWebView.actionStart(this, "http://www.baidu.com");
        overridePendingTransition(R.anim.in_from_right, android.R.anim.fade_out);
    }

    public void clickOnRegion(View paramView)
    {
        //RegionSelectionActivity.a(this);
    }

    @Override
    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);

    }

    @Override
    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_authenticator_9_29);
        initView();
        initToolBar();

        Bundle localBundle = getIntent().getExtras();

        startType = localBundle.getInt("authenticatorType");
        switch (startType) {
            case 1:
                //setLoginView();
                setRegisterView();
                break;
            case 2:
                setRegisterView();
                break;
            case 3:
                setForgotPasswordView();
                break;
        }

    }

    private void setRegisterView() {
        editText_email.setVisibility(View.VISIBLE);
        btn_auth.setText(getString(R.string.register_submit));

        SpannableString ss = new SpannableString("点击“注册”按钮即表示您已同意使用协议");
        ss.setSpan(new UnderlineSpan(), 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.protocol_link)), 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //String color = "#ff12b7b3";
        //String s = "<font color=\"" + color + "\">使用协议</font>";
        protocol_link.setText(ss);
        //protocol_link.setText(Html.fromHtml("点击“注册”按钮即表示您已同意" + s));
        protocol_link.setVisibility(View.VISIBLE);
        forgotPwd.setVisibility(View.GONE);
    }

    private void setLoginView() {
        editText_empno.setText(sharedPreferences_speak.getString("empno", null));
        editText_email.setVisibility(View.GONE);
        btn_auth.setText(getResources().getString(R.string.login));
        protocol_link.setVisibility(View.GONE);
        forgotPwd.setText(getString(R.string.forgot_password));
        forgotPwd.setVisibility(View.VISIBLE);
    }


    private void setForgotPasswordView() {
        editText_empno.setText(sharedPreferences_speak.getString("empno", null));
        editText_emppwd.setVisibility(View.GONE);
        editText_email.setVisibility(View.VISIBLE);
        btn_auth.setText(getResources().getString(R.string.get_password));
        protocol_link.setVisibility(View.GONE);
        forgotPwd.setText(getString(R.string.login));
    }

    @Override
    public void onNetworkError(int taskId) {
        super.onNetworkError(taskId);
        //presDialog.dismiss();
    }

    @Override
    public void onStop(){
        super.onStop();
        if (popupwindow != null && popupwindow.isShowing()) {
            popupwindow.dismiss();
            popupwindow = null;
        }
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));// 标题的文字需在setSupportActionBar之前，不然会无效
        //mToolbar.setLogo(R.drawable.icon);
        setSupportActionBar(mToolbar);
            /* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
// 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//实现左侧返回按钮

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


}