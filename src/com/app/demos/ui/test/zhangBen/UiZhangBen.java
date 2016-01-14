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
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.LogMy;
import com.app.demos.layout.NumInputPan;
import com.app.demos.layout.PickerView;
import com.app.demos.layout.Utils;
import com.app.demos.list.bitmap_load_list.FileCache;
import com.app.demos.model.ZhangBenCategory;
import com.app.demos.model.ZhangBenLocation;
import com.app.demos.model.Zhangben;
import com.app.demos.sqlite.ZhangBenCategorySqlite;
import com.app.demos.sqlite.ZhangBenLocationSqlite;
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

/**
 * Created by tom on 15-12-16.
 */
public class UiZhangBen extends BaseUi implements SelectCategoryHelper.OnFaceOprateListener, View.OnClickListener {
    private static final int MSG_DATA = 100;
    private AppCompatButton export;
    private AppCompatButton importBtn;
    private AppCompatButton history;
    private TextView header;
    private SelectCategoryHelper mFaceHelper;
    private TextView mTvCategry;
    private TextView mTvtime;
    private TextView mTvLocation;
    private TextView mTvRemark;
    private String customerTime;
    private String customerLocation = "无地点";
    private String remarkString;
    private String customerCategory;
    private ArrayList<View> viewCategorySelecteds = new ArrayList<View>();
    //private TextView time;
    //private TextView type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui_zhangben);

        initRecent();
        initView();
    }

    private void initRecent() {
        String recentCategory = sharedPreferences_speak.getString("RecentCategory", null);
        if (recentCategory != null) {
            String[] recentCategorys = recentCategory.split("@");
            //recentCategorys.
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_zhanben_tv_time2:
                showTimePickerDialog2();
                break;
            case R.id.ui_zhanben_tv_location2:
                showLocationPickerDialog();
                break;
            case R.id.ui_zhanben_btn_export:
                exportData();
                Snackbar.make(v, "exprot data ok", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.ui_zhanben_btn_import:
                importData();
                break;
            case R.id.ui_zhanben_btn_history:
                String s = "";
                ArrayList<Zhangben> zList = new ZhangbenSqlite(UiZhangBen.this).getAllZhangben("4");
                for (Zhangben z : zList) {
                    s = s + "\n" + z.getMoney() + "\n" + z.getTwo() + "\n" + z.getTime() + "\n" +
                            z.getLocation() + "\n" + z.getId() + "--------------";
                }
                header.setText(s);
                break;
        }
    }

    private void saveData(View v, String money) {
        if (customerCategory == null) {
            toast(getString(R.string.please_select_category));
            return;
        }
        if (money.trim().length() == 0 || Float.parseFloat(money) == 0 || money.equals("0.")) {
            toast(getString(R.string.please_input_money));
            return;
        }

        Zhangben zhang = new Zhangben();
        zhang.setMoney(money);
        //zhang.setLocation(location.getText().toString());
        //zhang.setOne(type.getText().toString());
        //zhang.setTwo(type.getText().toString());
        //zhang.setThree(type.getText().toString());
        //zhang.setTime(time.getText().toString());
        zhang.setLocation(customerLocation);
        zhang.setTwo(customerCategory);
        zhang.setTime(customerTime);
        if (new ZhangbenSqlite(UiZhangBen.this).updateZhangben(zhang)) {
            Snackbar.make(v, "ok", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //add location used //增加 location 使用次数
            ZhangBenLocation zhangBenLocation = new ZhangBenLocation();
            ZhangBenLocationSqlite zhangBenLocationSqlite = new ZhangBenLocationSqlite(this);
            int used = zhangBenLocationSqlite.getLocationUsed(customerLocation);
            print("location used: "+used + "\nlocation: " + customerLocation);
            zhangBenLocation.setLocation(customerLocation);
            zhangBenLocation.setUsed("" + (used + 1));
            zhangBenLocationSqlite.updateZhangBenLocation(zhangBenLocation);

            //add Category used //增加 Category 使用次数
            ZhangBenCategory zhangBenCategory = new ZhangBenCategory();
            ZhangBenCategorySqlite zhangBenCategorySqlite = new ZhangBenCategorySqlite(this);
            int usedCategory = zhangBenCategorySqlite.getCategoryUsed(customerCategory);
            zhangBenCategory.setResId(zhangBenCategorySqlite.getCategoryResId(customerCategory));
            zhangBenCategory.setCategoryName(customerCategory);
            zhangBenCategory.setUsed((usedCategory + 1));
            zhangBenCategorySqlite.updateZhangBenCategory(zhangBenCategory);

            exportData();
        } else {
            Snackbar.make(v, "fail", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

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
    public void onFaceSelected(CategoryModle modle, View view) {
        SpannableString spannableString = new SpannableString(" " + modle.getCharacter());
        Drawable d = getResources().getDrawable(modle.getId());
        if (d != null) {
            int i = Utils.dpToPx(getResources().getDimension(R.dimen.dp_10), getResources());// "19"为editView 的 TextSize
            d.setBounds(0, 0, i, i);//drawable 大小
        }
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvCategry.setText(spannableString);

        customerCategory = modle.getCharacter();

        //view animation / bg
        switch (viewCategorySelecteds.size()) {
            case 0:
                viewCategorySelecteds.add(view);
                break;
            case 1:
                viewCategorySelecteds.add(view);
                viewCategorySelecteds.get(0).setBackgroundResource(R.drawable.transparent_background);
                view.setBackgroundResource(R.drawable.green_btn_bg);
                break;
            case 2:
                viewCategorySelecteds.set(0, viewCategorySelecteds.get(1));
                viewCategorySelecteds.set(1, view);
                viewCategorySelecteds.get(0).setBackgroundResource(R.drawable.transparent_background);
                view.setBackgroundResource(R.drawable.green_btn_bg);
                break;

        }

    }

    @Override
    public void onCategoryAdd() {
        toast("add category");
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
        timePicker.setCurrentHour(Integer.parseInt(TimeUtil.getHour(customerTime)));
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
        header.append("\n" + customerTime);
    }

    private void showLocationPickerDialog() {
        Dialog dialog = new Dialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.view_location_picker, null);
        PickerView locationPv = (PickerView) view.findViewById(R.id.location_pv);

        ArrayList<String> locations = new ZhangBenLocationSqlite(this).getAllLocation(null);
        locationPv.setData(locations);
        locationPv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                customerLocation = text;
                mTvLocation.setText(customerLocation);
            }
        });
        locationPv.setSelected(customerLocation);

        dialog.setContentView(view);
        dialog.setTitle(getString(R.string.select_location));
        dialog.show();
    }

    private void initView() {
        initToolBar();
        importBtn = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_import);
        export = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_export);
        history = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_history);

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
        //init data
        final String currentTime = TimeUtil.long2String(System.currentTimeMillis());
        customerTime = currentTime;
        mTvtime.setText(TimeUtil.getMMdd(currentTime));

        //init location data
        ZhangBenLocationSqlite zhangBenLocationSqlite = new ZhangBenLocationSqlite(this);
        if (zhangBenLocationSqlite.getAllLocation("1").size() == 0) {
            if (zhangBenLocationSqlite.createData()) {//添加 location data
                customerLocation = zhangBenLocationSqlite.getAllLocation(null).get(0);
                mTvLocation.setText(customerLocation);
                printLoction(zhangBenLocationSqlite);
            }
        } else {
            customerLocation = zhangBenLocationSqlite.getAllLocation(null).get(0);
            mTvLocation.setText(customerLocation);
            printLoction(zhangBenLocationSqlite);
        }

        //init Category data
        ZhangBenCategorySqlite zhangBenCategorySqlite = new ZhangBenCategorySqlite(this);
        if (zhangBenCategorySqlite.getAll(null, null, null).size() == 0) {
            if (zhangBenCategorySqlite.createData()) {
                print("zhangBenCategorySqlite.createData() == ok!!");
            }
        }

        mTvtime.setOnClickListener(this);
        mTvLocation.setOnClickListener(this);
        mTvRemark.setOnClickListener(this);
        export.setOnClickListener(this);
        importBtn.setOnClickListener(this);
        history.setOnClickListener(this);
        final NumInputPan inputPan = (NumInputPan) findViewById(R.id.ui_zhanben_num_input_pan);
        inputPan.setBtnListener(new NumInputPan.OnBtnListener() {
            @Override
            public void onSaveClicked(String result) {
                if (Float.parseFloat(result) == 0){
                    toast(getString(R.string.please_input_money));
                    return;
                }
                saveData(inputPan, result);
            }

            @Override
            public void onNextClicked(String result) {
                if (Float.parseFloat(result) == 0){
                    toast(getString(R.string.please_input_money));
                    return;
                }
                customerTime = TimeUtil.long2String(System.currentTimeMillis());
                saveData(inputPan, result);
                toast("next");
            }
        });
    }

    //test
    private void printLoction(ZhangBenLocationSqlite zhangBenLocationSqlite) {
        String s = "";
        ArrayList<ZhangBenLocation> zList = zhangBenLocationSqlite.getAll(null);
        for (ZhangBenLocation z : zList) {
            s = s + "\n" + z.getLocation() + " --> " + z.getUsed() + "\n--------------";
        }
        print(s);
    }

    public void clickOnEdit(View view) {

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
