package com.app.demos.util.foxconn_ESS_zsf;

import android.util.Base64;
import android.util.Log;

import com.app.demos.base.C;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by tom on 15-6-21.
 */
public class AppClient {

    /**
     * ok
     * @param empno
     * @return
     * @throws Exception
     */
    public static String myzsfDormInfoPost(String empno) throws Exception {
        Log.e("myzsfDormInfoPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();
            // 如果是Get提交则创建HttpGet对象，否则创建HttpPost对象
            // HttpGet httpGet = new
            // HttpGet("http://192.168.1.100:8080/myweb/hello.jsp?username=abc&pwd=321");
            // post提交的方式
            HttpPost httpPost = new HttpPost(C.web.a);
            // 如果是Post提交可以将参数封装到集合中传递
            List dataList = new ArrayList();
            //dataList.add(new BasicNameValuePair("method", "Sys_Get_DormInfo"));
            dataList.add(new BasicNameValuePair("method", "Sys_Get_DormInfo"));
            dataList.add(new BasicNameValuePair("empno", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(dataList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * get user's empno name mobile
     * @param empno
     * @return
     * @throws Exception
     */
    public static String userInfoPost(String empno) throws Exception {
        Log.e("userInfoPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.b);


            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_UserInfo"));
            localArrayList.add(new BasicNameValuePair("EmpNo", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * fail
     * @param empno
     * @return
     * @throws Exception
     */
    public static String empMobilePost(String empno) throws Exception {
        Log.e("empMobilePost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.b);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "WS_Get_EmpMobile"));
            localArrayList.add(new BasicNameValuePair("EmpNo", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * @param empno
     * @return
     * @throws Exception
     */
    public static String userLogPost(String empno) throws Exception {
        Log.e("userLogPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Get_Sys_UserLog"));
            localArrayList.add(new BasicNameValuePair("EmpNo", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * fail
     * @param empno
     * @return
     * @throws Exception
     */
    public static String getUserCLossLogPost(String empno) throws Exception {
        Log.e("getUserCLossLogPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.b);


            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_UserCLossLog"));
            localArrayList.add(new BasicNameValuePair("EmpNo", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * get OverTime
     * @param empno
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static String dailyIssuePost(String empno, String startDate, String endDate) throws Exception {
        Log.e("dailyIssuePost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.b);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_DailyIssue"));
            localArrayList.add(new BasicNameValuePair("EmpNo", empno));
            //SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            localArrayList.add(new BasicNameValuePair("StartDate", startDate));
            localArrayList.add(new BasicNameValuePair("EndDate", endDate));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * 关系
     * @return
     */
    public static String getRelationshipPost() throws Exception {
        Log.e("getRelationshipPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "WS_Get_Relationship"));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * 奖金
     * @return
     */
    public static String getRMDAwardTypePost() throws Exception {
        Log.e("getRMDAwardTypePost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_RMDAwardType"));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * fail
     * @return
     */
    public static String getPushedBgPost(String empno) throws Exception {
        Log.e("getPushedBgPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "WS_Get_PushedBg"));
            localArrayList.add(new BasicNameValuePair("empno", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * fail
     * @return
     */
    public static String getNoPassReasonPost(String factory) throws Exception {
        Log.e("getNoPassReasonPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.d);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Ws_Get_NoPassReason"));
            localArrayList.add(new BasicNameValuePair("Factory", factory));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * @return
     */
    public static String getUserShortCutNewPost(String empno, String AppVersion) throws Exception {
        Log.e("geUserShortCutNewPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.b);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_UserShortCutNew"));
            localArrayList.add(new BasicNameValuePair("empno", empno));
            localArrayList.add(new BasicNameValuePair("VersionType", "A"));
            localArrayList.add(new BasicNameValuePair("AppVersion", AppVersion));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * @return
     */
    public static String getRecentRecordPost(String empno) throws Exception {
        Log.e("getRecentRecordPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "GetRecentRecord"));
            localArrayList.add(new BasicNameValuePair("empno", empno));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * @return
     */
    public static String getQuestionListPost() throws Exception {
        Log.e("getQuestionListPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_QuestionList"));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * ok
     * @return
     */
    public static String getAutoRespondUrlPost() throws Exception {
        Log.e("getAutoRespondUrlPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(C.web.a);

            List localArrayList = new ArrayList();    // 如果是Post提交可以将参数封装到集合中传递
            localArrayList.add(new BasicNameValuePair("method", "Sys_Get_AutoRespondUrl"));

            httpPost.setEntity(new UrlEncodedFormEntity(localArrayList));// UrlEncodedFormEntity用于将集合转换为Entity对象

            HttpResponse httpResponse = client.execute(httpPost);// 获取相应消息

            HttpEntity entity = httpResponse.getEntity();// 获取消息内容

            String content = EntityUtils.toString(entity); // 把消息对象直接转换为字符串
            Log.e("Http_test", content);
            Log.e("Http_test_DES", aDES(content));
            //showTextView.setText(content);
            return aDES(content);
            //return decode(content.getBytes());

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }
    //DES 解密
    public static String aDES(String paramString)
    {
        Object localObject = null;

        byte[] arrayOfByte1 = Base64.decode(paramString.getBytes(), 1);
        try
        {
            SecureRandom localSecureRandom = new SecureRandom();
            DESKeySpec localDESKeySpec = new DESKeySpec("ABVD1234".getBytes());
            SecretKey localSecretKey = SecretKeyFactory.getInstance("DES").generateSecret(localDESKeySpec);
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(2, localSecretKey, localSecureRandom);
            byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
            localObject = arrayOfByte2;
            String s = new String((byte[]) localObject);
            return s;
        }
        catch (Exception localException)
        {

            Log.e("Http_DES", "DESTool-----DES解密出錯!");
            localException.printStackTrace();

        }
        return null;
    }
}
