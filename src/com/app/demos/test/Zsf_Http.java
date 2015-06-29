package com.app.demos.test;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by tom on 15-6-20.
 */
public class Zsf_Http {

    HttpClient a;
    HttpPost b;
    HttpResponse c;
    String d;
    ArrayList e;

    public final String a(List paramList) throws Exception {
        String str2;
        Iterator localIterator;
        String str1 = "";

        str2 = "http://218.28.167.99:8080/EssFunction.ashx";
        localIterator = paramList.iterator();


        if (!(localIterator.hasNext())) {
            String str3 = str1.replaceFirst("&", "");
            this.e = new ArrayList();
            //  this.e.add(new BasicNameValuePair("str", ac.b(str3)));
            this.b = new HttpPost(str2);
            this.a = new DefaultHttpClient();
        }
        try {


            this.b.setEntity(new UrlEncodedFormEntity(this.e, "Utf-8"));
            this.c = this.a.execute(this.b);
            if (this.c.getStatusLine().getStatusCode() == 200) {
                this.d = EntityUtils.toString(this.c.getEntity());
                return this.d;
            } else {
                return null;
            }
            // str2 = "http://10.207.248.60:8080/EssFunction.ashx";

            //  Object localObject = localIterator.next();
            //   str1 = str1 + "&" + localObject.toString();
        } catch (Exception localException) {

            localException.printStackTrace();
        }

        return null;
    }

    //zsf Login
    public String zsfPost(String empno, String emppwd) throws Exception {
        Log.e("zsf_logon", "zsfPost().start");

        try {

            HttpClient client = new DefaultHttpClient();
            // 如果是Get提交则创建HttpGet对象，否则创建HttpPost对象
            // HttpGet httpGet = new
            // HttpGet("http://192.168.1.100:8080/myweb/hello.jsp?username=abc&pwd=321");
            // post提交的方式
            HttpPost httpPost = new HttpPost(
                    "http://218.28.167.99:8080/EssFunction.ashx");
            // 如果是Post提交可以将参数封装到集合中传递
            List dataList = new ArrayList();
            //dataList.add(new BasicNameValuePair("method", "Sys_Get_DormInfo"));
            dataList.add(new BasicNameValuePair("method", "UserLoginIn"));
            dataList.add(new BasicNameValuePair("empno", empno));
            dataList.add(new BasicNameValuePair("emppwd", emppwd));

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

    //zsf DormInfo
    public String zsfDormInfoPost(String empno) throws Exception {
        Log.e("zsfDormInfoPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();
            // 如果是Get提交则创建HttpGet对象，否则创建HttpPost对象
            // HttpGet httpGet = new
            // HttpGet("http://192.168.1.100:8080/myweb/hello.jsp?username=abc&pwd=321");
            // post提交的方式
            HttpPost httpPost = new HttpPost(
                    "http://218.28.167.99:8080/EssFunction.ashx");
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

    //idsbg  Login
    public String idsbgLogin()
    {
        Log.e("idsbgLogin()", "().start");

        String b = "http://aService.Foxconn.com/APPService/UserLoginService.asmx/UserLogin";
        Object localObject = null;
        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
        ArrayList localArrayList = new ArrayList();
        BasicNameValuePair localBasicNameValuePair1 = new BasicNameValuePair("strEmpNo", "G4050282");
        BasicNameValuePair localBasicNameValuePair2 = new BasicNameValuePair("strPassWord", "134871");
        BasicNameValuePair localBasicNameValuePair3 = new BasicNameValuePair("strSid", "");
        BasicNameValuePair localBasicNameValuePair4 = new BasicNameValuePair("strKey", "");
        //Log.i("empNo", paramString1);
        //Log.i("password", paramString2);
        //Log.i("key", paramString4);
        localArrayList.add(localBasicNameValuePair1);
        localArrayList.add(localBasicNameValuePair2);
        localArrayList.add(localBasicNameValuePair3);
        localArrayList.add(localBasicNameValuePair4);
        HttpParams localHttpParams = localDefaultHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(localHttpParams, 20000);
        HttpConnectionParams.setSoTimeout(localHttpParams, 20000);
        try
        {
            UrlEncodedFormEntity localUrlEncodedFormEntity = new UrlEncodedFormEntity(localArrayList, "UTF-8");
            HttpPost localHttpPost = new HttpPost(b);
            localHttpPost.setEntity(localUrlEncodedFormEntity);
            HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);
            //Log.i("url-->", this.b);
            //Log.i("status", localHttpResponse.getStatusLine().getStatusCode());
            if (localHttpResponse.getStatusLine().getStatusCode() == 200)
            {
                Log.i("info", "OKKKK");
                String str = EntityUtils.toString(localHttpResponse.getEntity());
                Log.e("idsbgLogin", str);
                localObject = str;
                return str;
            }
            return null;
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
            localUnsupportedEncodingException.printStackTrace();
        }
        catch (IllegalStateException localIllegalStateException)
        {
            localIllegalStateException.printStackTrace();
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return null;
    }

    /**
     * test my doTaskAsync() for
     */
    public String myzsfDormInfoPost(String empno) throws Exception {
        Log.e("zsfDormInfoPost()", "().start");

        try {

            HttpClient client = new DefaultHttpClient();
            // 如果是Get提交则创建HttpGet对象，否则创建HttpPost对象
            // HttpGet httpGet = new
            // HttpGet("http://192.168.1.100:8080/myweb/hello.jsp?username=abc&pwd=321");
            // post提交的方式
            HttpPost httpPost = new HttpPost(
                    "http://218.28.167.99:8080/EssFunction.ashx");
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

    public static String decode(byte[] data) throws Exception
    {
        try
        {
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec("ABVD1234".getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            return new String(cipher.doFinal(data));
        } catch (Exception e)
        {
            Log.e("Http_DES", "DES-----DES解密出錯!");

            e.printStackTrace();
            throw new Exception(e);
        }
    }
}

