package com.app.demos.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.app.demos.base.BaseApp;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("rawtypes")
public class AppClient {

	// compress strategy
	final private static int CS_NONE = 0;
	final private static int CS_GZIP = 1;

	// logic variables
	private String apiUrl;
	private HttpParams httpParams;
	private HttpClient httpClient;
	private int timeoutConnection = 10000;
	private int timeoutSocket = 10000;
	private int compress = CS_NONE;

	// charset default utf8
	private String charset = HTTP.UTF_8;

	public AppClient (String url) {
		initClient(url);
	}

	public AppClient (String url, String charset, int compress) {
		initClient(url);
		this.charset = charset; // set charset
		this.compress = compress; // set strategy
	}

	private void initClient (String url) {
		// initialize API URL
		this.apiUrl = C.api.base + url;
		String apiSid = AppUtil.getSessionId();
		if (apiSid != null && apiSid.length() > 0) {
			this.apiUrl += "?sid=" + apiSid;
		}
		// set client timeout
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// init client
		httpClient = new DefaultHttpClient(httpParams);
	}

	public void useWap () {
		HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	public String get () throws Exception {
		try {
			HttpGet httpGet = headerFilter(new HttpGet(this.apiUrl));
			Log.w("AppClient.get.url", this.apiUrl);
			// send get request
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpResult = resultFilter(httpResponse.getEntity());
				Log.w("AppClient.get.result", httpResult);
				return httpResult;
			} else {
				return null;
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception(C.err.network);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String post (HashMap urlParams) throws Exception {
		try {
			HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			// get post parameters
			Iterator it = urlParams.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				postParams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}
			// set data charset
			if (this.charset != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams, this.charset));
				//Log.w("charset", ":");
			} else {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams));
				Log.w("tag", "msg");
			}
			Log.w("AppClient.post.url", this.apiUrl);
			Log.w("AppClient.post.data", postParams.toString());
			// send post request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpResult = resultFilter(httpResponse.getEntity());
				Log.w("AppClient.post.result", httpResult.substring(CS_GZIP));
				return httpResult;
			} else {
				return null;
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception(C.err.network);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private HttpGet headerFilter (HttpGet httpGet) {
		switch (this.compress) {
			case CS_GZIP:
				httpGet.addHeader("Accept-Encoding", "gzip");
				break;
			default :
				break;
		}
		return httpGet;
	}

	private HttpPost headerFilter (HttpPost httpPost) {
		switch (this.compress) {
			case CS_GZIP:
				httpPost.addHeader("Accept-Encoding", "gzip");
				break;
			default :
				break;
		}
		return httpPost;
	}

	private String resultFilter(HttpEntity entity){
		String result = null;
		try {
			switch (this.compress) {
				case CS_GZIP:
					result = AppUtil.gzipToString(entity);
					break;
				default :
					result = EntityUtils.toString(entity);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static final String removeBOM(String data) {
		if (TextUtils.isEmpty(data)) {
			return data;
		}

		if (data.startsWith("\ufeff")) {
			return data.substring(1);
		} else {
			return data;
		}
	}

	public static final String remove(String data) {
		Log.w("remove","yes");
		return data.substring(30);
	}

	/**
	 *  上传文件至Server，uploadUrl：接收文件的处理页面
	 *  @param picPath image path
	 *  @param uploadUrl http://10.0.2.2:8002/save_upload_image.php
	 */
	public static void uploadFile(String picPath, String uploadUrl)
	{
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try
		{
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
			// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			// 允许输入输出流
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			// 使用POST方法
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ picPath.substring(picPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(picPath);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1)
			{
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			dos.close();
			is.close();

			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = httpURLConnection.getResponseCode();
			LogMy.e(BaseApp.getContext(), "response code:" + res);
			if (res == 200) {
				LogMy.e(BaseApp.getContext(), "request success");
				InputStream input = httpURLConnection.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				LogMy.e(BaseApp.getContext(), "result : " + result);
			} else {
				LogMy.e(BaseApp.getContext(), "request error");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
