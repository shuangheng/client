package com.app.demos.ui.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.layout.other.WebViewMy;
import com.app.demos.layout.swipebacklayout.SwipeBackLayout;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;

/**
 * Created by tom on 15-10-2.
 */
public class BaseWebView extends SwipeBackActivity {
    private WebViewMy webView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView mErrorView;
    private String paramUrl;
    private boolean isPageError = false;
    private EditText editText;
    private boolean isFinishScrollLeft;
    private SwipeRefreshLayout swipeRefresh;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        openHardWare();
        setContentView(R.layout.ui_webview);
        initView();
        initSwipeRefresh();

        mSwipeBackLayout = getSwipeBackLayout();
        Bundle localBundle = getIntent().getExtras();
        paramUrl = localBundle.getString("url");
        webView.loadUrl(paramUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
        //swipeRefresh.setRefreshing(true);
    }

    /**
     * 开启硬件加速
     */
    private void openHardWare() {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public static void actionStart(Context context, String url)
    {
        Intent intent = new Intent( context, BaseWebView.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    private void initView() {
        webView = (WebViewMy) findViewById(R.id.ui_web_view);
        progressBar = (ProgressBar) findViewById(R.id.ui_web_view_progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editText = (EditText) LayoutInflater.from(this).inflate(R.layout.toolbar_network_adress, null);
        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                webView.loadUrl("http://" + editText.getText().toString());
                return true;
            }
        });
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toolBarLongClick();
                return false;
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//实现左侧返回按钮

        webView.setWebViewClient(new WebViewClientMy());//在 this WebView中打开所有链接
        seedUp();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClientMy());
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        webView.setOnViewScrollChangedListener(new WebViewMy.OnViewTopListener() {
            @Override
            public void onScroll(int newY, int oldY) {
                swipeRefresh.setEnabled(isViewTop());
                mSwipeBackLayout.setEnableGesture(isViewTop());
            }

            @Override
            public void onViewTop() {

            }

            @Override
            public void onViewBottom() {

            }

            @Override
            public void onViewVicinityBottom() {

            }
        });

    }

    private void initSwipeRefresh() {
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.ui_web_view_swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.ui_green);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void toolBarLongClick() {
        if (editText.isShown()) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
            editText.setVisibility(View.GONE);
        } else {
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT);
            getSupportActionBar().setCustomView(editText, lp);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉标题
            getSupportActionBar().setDisplayShowHomeEnabled(false);//去掉导航
            editText.setVisibility(View.VISIBLE);
        }

    }

    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = (TextView) findViewById(R.id.ui_webview_pageError);
        }
        String html = "<img src='" + R.drawable.page_erro + "'/>";
        Html.ImageGetter imgGetter = new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(html, imgGetter, null);
        mErrorView.setText(charSequence);
        mErrorView.append("\n主人网络不稳定！戳我刷新");
        mErrorView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webView.reload();
                Toast.makeText(BaseWebView.this, paramUrl, Toast.LENGTH_SHORT).show();
                mErrorView.setVisibility(View.GONE);
                isPageError = false;
            }
        });
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

    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //WebView cookies清理：
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

        //清理cache 和历史记录:
        webView.clearCache( true );
        webView.clearHistory();
    }

    /**
     * 1.加快HTML网页装载完成的速度
     * 就是告诉WebView先不要自动加载图片，
     * 等页面finish后再发起图片加载。
     * API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，
     * 会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。
     */
    public void seedUp () {
        if(Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
    }

    /**
     * getScrollY()方法返回的是当前可见区域的顶端距整个页面顶端的距离,也就是当前内容滚动的距离.
     * getHeight()或者getBottom()方法都返回当前WebView 这个容器的高度
     * getContentHeight 返回的是整个html 的高度,但并不等同于当前整个页面的高度,
     * 因为WebView 有缩放功能, 所以当前整个页面的高度实际上应该是原始html 的高度再乘上缩放比例.
     * 因此,更正后的结果,准确的判断方法应该是：
     * @return
     */
    private Boolean isViewbottom() {
        return webView.getContentHeight()*webView.getScale() == (webView.getHeight()+webView.getScrollY());
    }

    private Boolean isViewTop() {
        return 0 == webView.getScrollY();
    }

    private class WebViewClientMy extends WebViewClient {

        @Override
        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //webView.getOriginalUrl();
            //paramUrl = failingUrl;
            //webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);//default pageError show null
            initErrorPage();
            mErrorView.setVisibility(View.VISIBLE);
            isPageError = true;
            //用javascript隐藏系统定义的404页面信息
            String data = "";
            webView.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");//set pageError as data
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            if(!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);//等页面finish后再发起图片加载。
            }
        }

    }

    class WebChromeClientMy extends  WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);

            } else {
                if (View.GONE == progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (!swipeRefresh.isRefreshing()) {
                    //swipeRefresh.setRefreshing(true);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!title.equals("找不到网页")) {
                toolbar.setTitle(title);
                //mErrorView.setVisibility(View.GONE);

            } else {
                toolbar.setTitle(getString(R.string.app_name));
                //mErrorView.setVisibility(View.VISIBLE);
            }
        }

    }


}
