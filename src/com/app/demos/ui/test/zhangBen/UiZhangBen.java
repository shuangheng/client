package com.app.demos.ui.test.zhangBen;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.LogMy;
import com.app.demos.layout.PickerView;
import com.app.demos.layout.Utils;
import com.app.demos.list.bitmap_load_list.FileCache;
import com.app.demos.model.Zhangben;
import com.app.demos.sqlite.ZhangbenSqlite;
import com.app.demos.util.AppUtil;
import com.app.demos.util.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tom on 15-12-16.
 */
public class UiZhangBen extends BaseUi implements SelectCategoryHelper.OnFaceOprateListener, View.OnClickListener {
    private static final int MSG_DATA = 100;
    private EditText location;
    private EditText money;
    private EditText time;
    private EditText type;
    private AppCompatButton export;
    private AppCompatButton importBtn;
    private AppCompatButton save;
    private AppCompatButton next;
    private TextView header;
    private SelectCategoryHelper mFaceHelper;
    private TextView mTvCategry;
    private TextView mTvtime;
    private TextView mTvLocation;
    private TextView mTvRemark;
    private String customerTime;
    //private TextView time;
    //private TextView type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui_zhangben);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_zhanben_edit_time:
                showTimePickerDialog2();
            case R.id.ui_zhanben_tv_time2:
                showTimePickerDialog2();
                break;
            case R.id.ui_zhanben_btn_save:
                saveData(v);
            case R.id.ui_zhanben_btn_export:
                exportData();
                Snackbar.make(v, "exprot data ok", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ui_zhanben_btn_import:
                importData();
                break;
            case R.id.ui_zhanben_btn_next:
                String s = "";
                ArrayList<Zhangben> zList = new ZhangbenSqlite(UiZhangBen.this).getAllZhangben("4");
                for (Zhangben z : zList) {
                    s = s + "\n" + z.getMoney() + "\n" + z.getThree() + "\n" + z.getTime() + "\n" +
                            z.getLocation() + "\n" + z.getId() + "--------------";
                }
                header.setText(s);
                break;
        }
    }

    private void saveData(View v) {
        Zhangben zhang = new Zhangben();
        zhang.setMoney(money.getText().toString());
        zhang.setLocation(location.getText().toString());
        zhang.setOne(type.getText().toString());
        zhang.setTwo(type.getText().toString());
        zhang.setThree(type.getText().toString());
        //zhang.setTime(time.getText().toString());
        zhang.setTime(customerTime);
        if (new ZhangbenSqlite(UiZhangBen.this).updateZhangben(zhang)) {
            Snackbar.make(v, "ok", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            exportData();
        } else {
            Snackbar.make(v, "fail", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        money.setText("");
        type.setText("");
        location.setText("");
        time.setText(TimeUtil.long2String(System.currentTimeMillis()));
    }

    private void exportData() {
        String s = "";
        String douDian = ",";
        ArrayList<Zhangben> zhangbenArrayList = new ZhangbenSqlite(UiZhangBen.this).getAllZhangben(null);
        int size = zhangbenArrayList.size();
        for (Zhangben z : zhangbenArrayList) {
            if (z == zhangbenArrayList.get(size-1)) {
                douDian = "";
            }
            s += "{\"" + Zhangben.COL_TIME + "\":\"" + z.getTime() + "\",\"" +
                    Zhangben.COL_ONE + "\":\"" + z.getOne() + "\",\"" +
                    Zhangben.COL_TWO + "\":\"" + z.getTwo() + "\",\"" +
                    Zhangben.COL_THREE + "\":\"" + z.getThree() + "\",\"" +
                    Zhangben.COL_LOCATION + "\":\"" + z.getLocation() + "\",\"" +
                    Zhangben.COL_MONEY + "\":\"" + z.getMoney() + "\"}" + douDian;
        }
        String data = "{\"code\":10000,\"message\":\"get data ok\"," +
                "\"result\":{\"Zhangben\""+":" + "[" + s +
                "]}}";

        LogMy.e(UiZhangBen.this, data);

        FileCache fileCache = new FileCache(UiZhangBen.this, "sql");
        File saveFilePath = fileCache.getFile("zhanBen.sql");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFilePath);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importData() {
        Handler mHandler = new IndexHandler();
        FileCache fileCache = new FileCache(UiZhangBen.this, "sql");
        File saveFilePath = fileCache.getFile("zhanBen.sql");
        try {
            FileInputStream is = new FileInputStream(saveFilePath);
            String encoding = "UTF-8";
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                toast(s);
                Bundle bundle = new Bundle();
                bundle.putString("data", s);
                Message msg = new Message();
                msg.what = MSG_DATA;
                msg.setData(bundle);
                mHandler.sendMessage(msg);


            }
            is.close();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolBar() {
        Toolbar toolbar = getToolBar(R.id.toolbar, getString(R.string.ji_zhang_ben), true);
    }

    @Override
    public void onFaceSelected(CategoryModle modle) {
        SpannableString spannableString = new SpannableString(" " + modle.getCharacter());
        Drawable d = getResources().getDrawable(modle.getId());
        if (d != null) {
            int i = Utils.dpToPx(getResources().getDimension(R.dimen.dp_10), getResources());// "19"为editView 的 TextSize
            d.setBounds(0, 0, i, i);//drawable 大小
        }
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvCategry.setText(spannableString);
    }

    @Override
    public void onFaceDeleted() {

    }

    private void showTimePickerDialog2() {
        Dialog dialog = new Dialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.view_time_picker_2, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        UiUtil.resizePicker(datePicker);
        UiUtil.resizePicker(timePicker);

        //datePicker.setMinDate(System.currentTimeMillis() - 1000);
        int year = Integer.parseInt(TimeUtil.getYear(customerTime));
        int monthOfYear = Integer.parseInt(TimeUtil.getMonth(customerTime))-1;
        int dayOfMonth = Integer.parseInt(TimeUtil.getDay(customerTime));
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                //得到时间
                String time = getTwoNum(timePicker.getCurrentHour()) + ":" + getTwoNum(timePicker.getCurrentMinute()) + ":00";

                customerTime = year + "-" + getTwoNum(monthOfYear + 1) + "-" + getTwoNum(dayOfMonth) + " " + time;

                mTvtime.setText(TimeUtil.getMMdd(customerTime));
                print(customerTime);
            }

        });

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String date = datePicker.getYear() + "-" + getTwoNum(datePicker.getMonth() + 1) + "-"
                        + getTwoNum(datePicker.getDayOfMonth());
                customerTime = date + " " +
                        getTwoNum(timePicker.getCurrentHour()) + ":" + getTwoNum(timePicker.getCurrentMinute()) + ":00";
                mTvtime.setText(TimeUtil.getMMdd(customerTime));
                print(customerTime);
            }

        });

        dialog.setTitle(getString(R.string.select_time));
        dialog.setContentView(view);
        dialog.show();
    }

    private String getTwoNum(int numbar) {
        String twoNum = "";
        if (numbar < 10) {
            twoNum = "0" + numbar;
            return  twoNum;
        }
        return numbar + "";
    }

    private void print(String customerTime) {
        header.setText(customerTime);
    }

    private void showTimePickerDialog() {
        Dialog dialog = new Dialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.view_time_picker, null);
        PickerView monthPv = (PickerView) view.findViewById(R.id.month_pv);
        final PickerView dayPv = (PickerView) view.findViewById(R.id.day_pv);
        PickerView hourPv = (PickerView) view.findViewById(R.id.hour_pv);
        PickerView minutePv = (PickerView) view.findViewById(R.id.minute_pv);

        ArrayList<String> months = new ArrayList<String>();
        final ArrayList<String> days = new ArrayList<String>();
        ArrayList<String> hours = new ArrayList<String>();
        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 1; i < 13; i++)
        {
            months.add(i < 10 ? "0" + i : "" + i);
        }
        for (int i = 1; i < getDays(TimeUtil.getMonth(customerTime)) + 1; i++)
        {
            days.add(i < 10 ? "0" + i : "" + i);
        }
        for (int i = 0; i < 24; i++)
        {
            hours.add(i < 10 ? "0" + i : "" + i);
        }
        for (int i = 0; i < 60; i++)
        {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }
        monthPv.setData(months);
        //monthPv.setSelected(TimeUtil.getMonth(customerTime));
        dayPv.setData(days);
        //monthPv.setSelected(TimeUtil.getDay(customerTime));
        hourPv.setData(hours);
        hourPv.setMaxTextSize(Utils.dpToPx(getResources().getDimension(R.dimen.dp_5), getResources()));
        //monthPv.setSelected(TimeUtil.getHour(customerTime));
        minutePv.setData(minutes);
        minutePv.setColorText(getResources().getColor(R.color.green));
       // monthPv.setSelected(TimeUtil.getMinute(customerTime));

        monthPv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = customerTime.substring(0, 5);//月份前的
                String ss = customerTime.substring(7, 19);//月份后的
                customerTime = s + text + ss;
                String currentSelected = dayPv.getCurrentSelected();
                for (int i = 1; i < getDays(text)+1; i++) {
                    days.clear();
                    days.add(i < 10 ? "0" + i : "" + i);
                }
                dayPv.setData(days);
                if (days.size() < Integer.parseInt(currentSelected)) {
                    dayPv.setSelected(days.size() - 1);
                } else { dayPv.setSelected(currentSelected); }
                mTvtime.setText(TimeUtil.getMMdd(customerTime));
            }
        });
        dayPv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = customerTime.substring(0, 8);//月份前的
                String ss = customerTime.substring(10, 19);//月份后的
                customerTime = s + text + ss;
                mTvtime.setText(TimeUtil.getMMdd(customerTime));
            }
        });
        hourPv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = customerTime.substring(0, 11);//月份前的
                String ss = customerTime.substring(13, 19);//月份后的
                customerTime = s + text + ss;
                mTvtime.setText(TimeUtil.getMMdd(customerTime));
            }
        });
        minutePv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String s = customerTime.substring(0, 14);//月份前的
                String ss = customerTime.substring(16, 19);//月份后的
                customerTime = s + text + ss;
                mTvtime.setText(TimeUtil.getMMdd(customerTime));
            }
        });
        dialog.setContentView(view);
        dialog.setTitle(getString(R.string.select_time));
        dialog.show();
    }

    public int getDays(String month) {
        int days = 0;
        switch (Integer.parseInt(month)) {
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                days = 31;
                break;
            case 2:
                //int year = Calendar.getInstance().get(Calendar.YEAR);
                int year = Integer.parseInt(TimeUtil.getYear(customerTime));
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    days = 29;
                } else { days = 28; }
                break;
            default:
                days = 30;
                break;
        }
        return days;
    }

    private void initView() {
        initToolBar();
        location = (EditText) findViewById(R.id.ui_zhanben_edit_loaction);
        money = (EditText) findViewById(R.id.ui_zhanben_edit_money);
        time = (EditText) findViewById(R.id.ui_zhanben_edit_time);
        type = (EditText) findViewById(R.id.ui_zhanben_edit_type);
        importBtn = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_import);
        export = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_export);
        save = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_save);
        next = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_next);

        header = (TextView) findViewById(R.id.ui_zhanben_header_tv);
        mTvCategry = (TextView) findViewById(R.id.ui_zhanben_tv_category);
        mTvtime = (TextView) findViewById(R.id.ui_zhanben_tv_time2);
        mTvLocation = (TextView) findViewById(R.id.ui_zhanben_tv_location2);
        mTvRemark = (TextView) findViewById(R.id.ui_zhanben_tv_remark);
        View view = findViewById(R.id.emoji_layout);
        if (null == mFaceHelper) {
            mFaceHelper = new SelectCategoryHelper(this, view);
            mFaceHelper.setFaceOpreateListener(this);
        }

        String currentTime = TimeUtil.long2String(System.currentTimeMillis());
        customerTime = currentTime;
        time.setText(currentTime);
        mTvtime.setText(TimeUtil.getMMdd(currentTime));

        mTvtime.setOnClickListener(this);
        export.setOnClickListener(this);
        importBtn.setOnClickListener(this);
        save.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private class IndexHandler extends Handler {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {

                    case MSG_DATA:
                        String s = msg.getData().getString("data");
                        header.setText("ssssss" + s);
                        BaseMessage baseMessage = AppUtil.getMessage(s);
                        LogMy.e(UiZhangBen.this, baseMessage.toString());
                        ArrayList<Zhangben> list = (ArrayList<Zhangben>) baseMessage.getResultList("Zhangben");
                        LogMy.e(UiZhangBen.this, list.toString());
                        ZhangbenSqlite zhangbenSqlite = new ZhangbenSqlite(UiZhangBen.this);
                        for (Zhangben z : list) {
                        zhangbenSqlite.updateZhangben(z);
                        }
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                //.toast(e.getMessage());
            }
        }
    }

}
