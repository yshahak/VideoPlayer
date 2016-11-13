package com.downtube.videos.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import com.downtube.videos.R;

import java.io.File;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web_view);
        WebView mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://vuclip.com/w?cid=939349379&l=21339396&z=31406&frm=index.html");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri result = intent.getData();
        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(intent.getData().getPath())).toString());
//        MainActivity.downId = Utils.downloadFile(getApplicationContext(), result.toString(), URLUtil.guessFileName(intent.getData().toString(), null, null));


    }
}
