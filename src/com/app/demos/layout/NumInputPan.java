package com.app.demos.layout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 16-1-13.
 */
public class NumInputPan extends LinearLayout {
    private AppCompatButton[] btnNum = new AppCompatButton[11];
    private EditText editText;
    private boolean firstFlag = true;
    private OnBtnListener mOnBtnListener;

    public NumInputPan(Context context) {
        super(context);
    }

    public NumInputPan(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.customer_view_num_input_pan, this);
        initView();
        initListener();
    }

    public NumInputPan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        // 获取数字
        btnNum[0] = (AppCompatButton) findViewById(R.id.num0);
        btnNum[1] = (AppCompatButton) findViewById(R.id.num1);
        btnNum[2] = (AppCompatButton) findViewById(R.id.num2);
        btnNum[3] = (AppCompatButton) findViewById(R.id.num3);
        btnNum[4] = (AppCompatButton) findViewById(R.id.num4);
        btnNum[5] = (AppCompatButton) findViewById(R.id.num5);
        btnNum[6] = (AppCompatButton) findViewById(R.id.num6);
        btnNum[7] = (AppCompatButton) findViewById(R.id.num7);
        btnNum[8] = (AppCompatButton) findViewById(R.id.num8);
        btnNum[9] = (AppCompatButton) findViewById(R.id.num9);
        btnNum[10] = (AppCompatButton) findViewById(R.id.point);
        // 初始化显示结果区域
        editText = (EditText) findViewById(R.id.result);
        editText.setInputType(InputType.TYPE_NULL);//hide system input
        editText.setText("0");

        GridLayout gridLayout = (GridLayout) findViewById(R.id.view_num_input_pan_gridlayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            setMargin(gridLayout.getChildAt(i));
        }

    }

    private void initListener() {
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 实例化监听器对象
        NumberAction na = new NumberAction();
        for (AppCompatButton bc : btnNum) {
            bc.setOnClickListener(na);
            bc.setTextColor(getResources().getColor(R.color.white));
        }

        // next按钮的动作
        AppCompatButton btnNext = (AppCompatButton) findViewById(R.id.next);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                firstFlag = true;
                mOnBtnListener.onNextClicked(editText.getText().toString());
                editText.setText("0");
            }
        });
        // save 按钮的动作
        AppCompatButton btnSave = (AppCompatButton) findViewById(R.id.save);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //editText.setText("0");
                mOnBtnListener.onSaveClicked(editText.getText().toString());

            }
        });
        // delete 按钮的动作
        AppCompatButton btnDelete = (AppCompatButton) findViewById(R.id.delete);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                if (!s.equals("0") && s.length() > 0) {
                    if (s.length() == 1) {
                        editText.setText("0");
                        return;
                    }
                    int selection = editText.getSelectionStart();
                    editText.getText().delete(selection - 1, selection);
                }
            }
        });
    }

    public void setMargin(View view) {
        int h = 5;
        view.setPadding(h, h, h, h);
    }

    // 数字按钮监听器
private class NumberAction implements OnClickListener {
    @Override
    public void onClick(View view) {
        AppCompatButton btn = (AppCompatButton) view;
        String input = btn.getText().toString();
            String editTextStr = editText.getText().toString();
        if (editTextStr.equals("0")) {
            if (input.equals("0")) {
                error(editText);
                return;
            }
            if (!input.equals(".") && Integer.parseInt(input) >0) {
                editText.setText("");
            }
        }
            // 判断显示区域的值里面是否已经有".",如果有,输入的又是".",就什么都不做
            if (editTextStr.contains(".")) {
                if (input.equals(".")) {
                    error(editText);
                    return;
                }
                if (editTextStr.substring(editTextStr.indexOf(".")).length() == 3) {//去除“.”前的text  "."后两位数
                    error(editText);
                    return;
                }
            }
            // 判断显示区域的值里面只有"-",输入的又是".",就什么都不做
            if (editTextStr.equals("-") && input.equals(".")) {
                error(editText);
                return;
            }

        editText.append(input);// 设置显示区域的值
    }
}

    private void error(TextView view) {
        ColorStateList color = view.getTextColors();
        int colorError = getResources().getColor(android.R.color.holo_red_light);
        view.setTextColor(colorError);
        view.setTextColor(color);
    }

    public void setBtnListener(OnBtnListener btnListener) {
        this.mOnBtnListener = btnListener;
    }

    public interface OnBtnListener {

        void onSaveClicked(String result);

        void onNextClicked(String result);
    }

    /**
     * // 数字按钮监听器
     private class NumberAction implements OnClickListener {
    @Override
    public void onClick(View view) {
    AppCompatButton btn = (AppCompatButton) view;
    String input = btn.getText().toString();
    if (firstFlag) { // 首次输入
    // 一上就".",就什么也不做
    if (input.equals(".")) {
    editText.setText("0");
    }
    // 如果是"0.0"的话,就清空
    if (editText.getText().toString().equals("0")) {
    editText.setText("");
    }
    firstFlag = false;// 改变是否首次输入的标记值
    } else {
    String editTextStr = editText.getText().toString();
    // 判断显示区域的值里面是否已经有".",如果有,输入的又是".",就什么都不做
    if (editTextStr.contains(".")) {
    if (input.equals(".")) {
    error(editText);
    return;
    }
    if (editTextStr.substring(editTextStr.indexOf(".")).length() == 3) {//去除“.”前的text
    error(editText);
    return;
    }
    }
    // 判断显示区域的值里面只有"-",输入的又是".",就什么都不做
    if (editTextStr.equals("-") && input.equals(".")) {
    error(editText);
    return;
    }
    // 判断显示区域的值如果是"0",输入的不是".",就什么也不做
    if (editTextStr.equals("0")) {
    if (input.equals("0")) {
    error(editText);
    return;
    } else {
    editText.setText("");
    }

    error(editText);
    return;
    }
    if (editTextStr.equals("0.0") && input.equals("0")) {
    error(editText);
    return;
    }
    }
    editText.setText(editText.getText().toString() + input);// 设置显示区域的值
    }
    }
     */

}
