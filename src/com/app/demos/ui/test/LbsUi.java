package com.app.demos.ui.test;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.util.TimeUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by tom on 15-11-15.
 */
public class LbsUi extends Activity implements View.OnClickListener {
    private TextView textView;
    private Button button;
    private LocationManager locationManager;
    private String locationProvider;

    public static final int SHOW_LOCATION = 0;
    private Button location1;
    private Button location2;

    private double[][] lacations ;
    private Location location;
    private TextView textView1;
    private TextView textView2;
    private Location locationSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_lbs_tv_btn);
        initView();
        //获取显示地理位置信息的TextView
        //postionView = (TextView) findViewById(R.id.positionView);

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case SHOW_LOCATION:
                    String position = (String) msg.obj;
                    textView.setText(position);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示地理位置经度和纬度信息
     * @param location
     */
    private void showLocation(TextView textView, Location location){
        String currentTime = TimeUtil.long2String(System.currentTimeMillis());
        String locationStr = "纬度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude() +"\n"+ currentTime;
        textView.setText(locationStr);
    }

    /**
     * 显示地理位置经度和纬度信息
     * @param location
     */
    private void showLocation2(final Location location){
        /*String locationStr = "维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();
        postionView.setText(locationStr);*/
        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    //组装反向地理编码的接口位置
                    StringBuilder url = new StringBuilder();
                    url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    url.append(location.getLatitude()).append(",");
                    url.append(location.getLongitude());
                    url.append("&sensor=false");
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url.toString());
                    httpGet.addHeader("Accept-Language","zh-CN");
                    HttpResponse response = client.execute(httpGet);
                    if(response.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = response.getEntity();
                        String res = EntityUtils.toString(entity);
                        //解析
                        JSONObject jsonObject = new JSONObject(res);
                        //获取results节点下的位置信息
                        JSONArray resultArray = jsonObject.getJSONArray("results");
                        if(resultArray.length() > 0){
                            JSONObject obj = resultArray.getJSONObject(0);
                            //取出格式化后的位置数据
                            String address = obj.getString("formatted_address");

                            Message msg = new Message();
                            msg.what = SHOW_LOCATION;
                            msg.obj = address;
                            handler.sendMessage(msg);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(textView, location);

            float v1 = location.distanceTo(locationSave);
            button.setText("distance : " + v1);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void initView() {
        textView = (TextView) findViewById(R.id.test_lbs_tv_textView);
        textView1 = (TextView) findViewById(R.id.test_lbs_tv_location1);
        textView2 = (TextView) findViewById(R.id.test_lbs_tv_location2);
        button = (Button) findViewById(R.id.test_lbs_tv_btn_button);
        location1 = (Button) findViewById(R.id.test_lbs_tv_btn_location1);
        location2 = (Button) findViewById(R.id.test_lbs_tv_btn_location2);
        location1.setOnClickListener(this);
        location2.setOnClickListener(this);
        button.setOnClickListener(this);
        location2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                float v1 = location.distanceTo(locationSave);
                button.setText("distance : " + v1);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_lbs_tv_btn_location1:
                lacations[0][0] = location.getLatitude();
                lacations[0][1] = location.getLongitude();
                showLocation(textView1, location);
                break;
            case R.id.test_lbs_tv_btn_location2:
                lacations[1][0] = location.getLatitude();
                lacations[1][1] = location.getLongitude();
                showLocation(textView2, location);
                break;
            case R.id.test_lbs_tv_btn_button:
                //获取地理位置管理器
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //获取所有可用的位置提供器
                List<String> providers = locationManager.getProviders(true);
                if(providers.contains(LocationManager.GPS_PROVIDER)){
                    //如果是GPS
                    locationProvider = LocationManager.GPS_PROVIDER;
                }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                    //如果是Network
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                }else{
                    Toast.makeText(LbsUi.this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                    return ;
                }
                //获取Location
                location = locationManager.getLastKnownLocation(locationProvider);
                locationSave = location;

                if(location!=null){
                    //不为空,显示地理位置经纬度
                    showLocation(textView, location);
                }else{
                    Toast.makeText(LbsUi.this, "location为空", Toast.LENGTH_SHORT).show();
                }
                //监视地理位置变化
                locationManager.requestLocationUpdates(locationProvider, 3000, 0, locationListener);
                break;
            default:
                break;
        }
    }
}
