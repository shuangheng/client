package com.app.demos.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.layout.materialEditText.MaterialEditText;
import com.app.demos.test.Zsf_Http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tom on 15-6-6.
 */
public class UiCreateFind extends BaseUi{
    private static final String TAg = "UiFindCreate";
    private LinearLayout spinnerConvert;
    private SpinnerLinearLayout spinner;
    private Button button;
    private Context context;
    private ArrayList<SpinnerLinearLayout> spinners;
    private MaterialEditText edit_where;
    private EditText content;
    private Button button2;
    private TextView textView;
    private int spinner_i;
    private int[] imageIds;
    private String[] names;
    private String[] hints;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_create_find);

        context = this;
        edit_where = (MaterialEditText) findViewById(R.id.ui_findcreate_where_etext);
        content = (EditText) findViewById(R.id.ui_findcreate_content_etext);
        //spinners = new List<SpinnerLinearLayout>;
        spinners = new ArrayList<SpinnerLinearLayout>() ;
        progressDialog = new ProgressDialog(this);
        spinner_i = 0;

        names = new String[]{"钱包", "厂牌", "银行卡", "身份证", "手机", "其他"};
        hints = new String[]{"XX色、XX现金", "工号", "卡号后四位", "证号后四位", "品牌型号", "物品特征"};

        addSpinner();
        initButton();
        initTitlebar();

        testEdit();

    }



    private void testEdit(){
        button2 = (Button) findViewById(R.id.ui_findcreate_button2);
        textView = (TextView) findViewById(R.id.sfsfsfsfd);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //release();
                //textView.setText(content.getText().toString());

                final String[] strings = content.getText().toString().split("&");
                //final String strings = content.getText().toString();
                //textView.setText(strings);

                ExecutorService service = Executors.newFixedThreadPool(1);
               // for (int i = 0; i < 10; i++) {
                    //System.out.println("创建线程" + i);
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAg, "HTTP_test");

                            //System.out.println("启动线程");
                            try {
                                new Zsf_Http().zsfPost(strings[0], strings[1]);
                                //new Zsf_Http().zsfDormInfoPost(strings);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    // 在未来某个时间执行给定的命令
                    //service.execute(run);
                    service.submit(run);
               // }
                // 关闭启动线程
                service.shutdown();
                // 等待子线程结束，再继续执行下面的代码
                try {
                    service.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("all thread complete");
                //Log.e(TAg, content.getText().toString());
                Toast.makeText(context, content.getText().toString(), Toast.LENGTH_SHORT).show();

                //content.requestFocus();
                //InputMethodManager imm = (InputMethodManager) content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘
                //imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);


            }
        });
    }

    private void initTitlebar() {
        ImageView ivBack = (ImageView) findViewById(R.id.ui_createFind_iv_back);
        TextView tvRelease = (TextView) findViewById(R.id.ui_createFind_tv_release);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFinish();
            }
        });

        tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                release();
            }
        });
    }


    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        hideProgress();
        switch (taskId) {
            case C.task.find_release:
                String code = message.getCode();
                if (Integer.parseInt(code) == 10000) {
                    Toast.makeText(context, "发布 成功！", Toast.LENGTH_SHORT).show();
                    returnData();
                } else {
                    Toast.makeText(context, "发布 失败！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        this.showDialog();
    }

    /**
     * 发布
     */
    private void release() {

        openPogressDialog();//进度条

        String lost_item = "";
        String item_summary = "";
        String where = edit_where.getText().toString();
        String content_text = content.getText().toString();
        String summary;
        SpinnerLinearLayout spinLayout = new SpinnerLinearLayout(context);
        Boolean isEmpty = false;
        //获取系统输入法
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘


        for (SpinnerLinearLayout s : spinners) {
            int select = s.mSpinner.getSelectedItemPosition();
            summary = s.getText();
            if (android.os.Build.VERSION.SDK_INT >= 9) {
                if (summary.isEmpty()) {//检查EditText是否为空
                    isEmpty = true;
                    spinLayout = s;
                    break;
                }
            } else {
                if (summary.toCharArray().length == 0) {
                    isEmpty = true;
                    spinLayout = s;
                    break;
                }
            }
            lost_item += ("@@" + select);
            item_summary += ("@@" + summary);
        }

        if (where.toCharArray().length == 0) {
            hideProgress();
            //showInput(spinLayout.mEText);
            edit_where.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            toast("请输入丢失地点！");
            //imm.showSoftInputFromInputMethod(spinLayout.mEText.getWindowToken(), 0);//显示键盘
        } else if (isEmpty) {
            hideProgress();
            //showInput(edit_where);
            spinLayout.mEText.requestFocus();
               // imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            imm.showSoftInput(spinLayout.mEText, 0);//显示键盘
            toast("请输入物品特征！");
            //imm.showSoftInputFromInputMethod(edit_where.getWindowToken(), 0);//显示键盘
        } else if (content.getText().toString().toCharArray().length < 30) {
            hideProgress();
            //showInput(content);
            content.requestFocus();
               // imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            toast("请输入详细内容！");
            imm.showSoftInputFromInputMethod(content.getWindowToken(), 0);//显示键盘
        } else {
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("lost_id", "4");
            blogParams.put("lost_item", lost_item);
            blogParams.put("item_summary", item_summary);
            blogParams.put("where", where);
            blogParams.put("content", content_text);
            this.doTaskAsync(C.task.find_release, C.api.find_release, blogParams);
        }
    }

    /**
     * show / hide  输入法
     */
    public void showInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//获取系统键盘
        editText.requestFocus();
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        }
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示  发布 进度条
     */
    public void openPogressDialog() {
        progressDialog.setMessage("发布中...");
        progressDialog.show();
    }

    /**
     * 隐藏 发布 进度条
     */
    private void hideProgress() {
        progressDialog.dismiss();
    }



    /**
     * 按 返回建 时 显示的Dialog
     */
    private void showDialog() {
        new AlertDialog.Builder(this)
                //.setTitle("确定离开？")
                .setMessage("现在离开?\n\n寻物启事还没有发布！")
                .setPositiveButton("离开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doFinish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    /**
     * init add Button
     */
    private void initButton() {
        button = (Button) findViewById(R.id.ui_findcreate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpinner();
            }
        });
    }

    /**
     * add Spinner
     */
    private void addSpinner() {
        if (spinner_i < 5) {//最多 5 个
            if (null == spinnerConvert) {
                spinnerConvert = (LinearLayout) findViewById(R.id.ui_findcreate_spinners);
            }
            final SpinnerLinearLayout spin = new SpinnerLinearLayout(this);
            spin.setTitle("失物 " + (spinner_i +1) + " :");
            spin.mSpinner.setSelection(spinner_i);//默认第几项
            spin.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spin.setHint(hints[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //删除按钮的 显示/隐藏
            if (spinner_i > 0) {
                spin.mButton.setVisibility(View.VISIBLE);
                spinners.get(spinner_i - 1).mButton.setVisibility(View.GONE);
                spin.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinnerConvert.removeView(spinners.get(spinner_i-1));
                        spinners.remove(spinner_i-1);
                        if (spinner_i > 2) {
                            spinners.get(spinner_i -2).mButton.setVisibility(View.VISIBLE);
                        }
                        spinner_i--;
                    }
                });
            }

            spinners.add(spinner_i, spin);
            spinnerConvert.addView(spin);
            //Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
            spinner_i++;
        } else {
            Toast.makeText(this, "窗口达到上限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回数据给上一个Activity
     */
    private void returnData() {
        Intent intent = new Intent();
        intent.putExtra("Find_index", 2);
        setResult(RESULT_OK, intent);
        doFinish();
    }

    /**
     * create Spinner
     */
    class SpinnerLinearLayout extends LinearLayout {
        private Spinner mSpinner;
        private EditText mEText;
        private TextView mText;
        private Button mButton;
        private String str;

        public SpinnerLinearLayout(Context context) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.ui_create_find_spinner, this);

            mText = (TextView) findViewById(R.id.ui_find_create_TEXT);
            mSpinner = (Spinner) findViewById(R.id.ui_find_create_spinner_spinner);
            mEText = (EditText) findViewById(R.id.ui_find_create_spinner_et);
            mButton = (Button) findViewById(R.id.ui_find_create_spinner_btn);

            MyAdapter adapter = new MyAdapter(context, C.find_imageIds, names);
            mSpinner.setAdapter(adapter);

        }

        /**
         * set this title
         */
        private void setTitle(String title) {
            mText.setText(title);
        }

        /**
         * set button VISIB
         */
        private void setButton() {

        }


        /**
         * get EditText's text
         */
        private String getText() {
            Log.e(TAg, "EditText : "+ mEText.getText());
            return mEText.getText().toString();
        }

        /**
         * set EditText's hint
         */
        private void setHint(String hint) {
            mEText.setHint(hint);
        }
    }

    /**
     * 自定义 Spinner 适配器类
     */
    private class MyAdapter extends BaseAdapter {
        private String[] mList;
        private int[] imagrIds;
        private Context mContext;

        public MyAdapter(Context pContext, int[] imageIds, String[] names) {
            this.mContext = pContext;
            this.imagrIds = imageIds;
            this.mList = names;
        }

        @Override
        public int getCount() {
            return mList.length;
        }

        @Override
        public Object getItem(int position) {
            return mList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        /**
         * spinner 中显示的 view
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(R.layout.spinner_item_custom, null);
            if(convertView!=null) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.spinner_item_custom_image);
                imageView.setImageResource(imagrIds[position]);
            }
            return convertView;
        }

        /**
         * 弹出窗口的样式
         */
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(R.layout.spinner_simple_dropdown_item, null);
            if(convertView!=null) {
                CheckedTextView _TextView2 = (CheckedTextView) convertView.findViewById(R.id.spinner_simple_dropdown_item_checked_tv);
                _TextView2.setText(mList[position]);
            }

            /*/convertView=_LayoutInflater.inflate(R.layout.spinner_dropdown_item_custom, null);//自定义view
            if(convertView!=null) {
                TextView _TextView2 = (TextView) convertView.findViewById(R.id.spinner_dropdown_item_custom_text);
                _TextView2.setText(mList[position]);
            }
            */
            return convertView;
        }
    }

    private class Lost {
        private int imageId;
        private String name;
        private String hint;

        public Lost () {

        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }
    }
}
