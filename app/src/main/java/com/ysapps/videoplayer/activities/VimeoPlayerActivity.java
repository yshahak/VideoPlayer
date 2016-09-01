package com.ysapps.videoplayer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ysapps.videoplayer.HTML5WebView;

public class VimeoPlayerActivity extends AppCompatActivity {
    public static final String EXTRA_LINK = "extraLink";
    private WebView webView;
    private HTML5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vimeo_player);
//        webView = (WebView) findViewById(R.id.web_view);
        String html = getIntent().getStringExtra(EXTRA_LINK);
        if(html != null) {
            mWebView = new HTML5WebView(this);
            //Auto playing vimeo videos in Android webview
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.loadData(html, "text/html", "utf-8");
            setContentView(mWebView.getLayout());
        }
    }
}
