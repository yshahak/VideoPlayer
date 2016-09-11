package com.ysapps.videoplayer.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ysapps.videoplayer.R;

/**
 * Created by yshahak on 08/09/2016.
 */

public class SplashScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String splash_header =  "<big><font color='##cccccc' >Video downloader</font><font color='red'>Plus++</font></big><br/>"
                +"<small><font color='##cccccc'>Your One Stop Video Downloader</font></small>";
        ((TextView)findViewById(R.id.splash_header)).setText(Html.fromHtml(splash_header));
        findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver viewTreeObserver = findViewById(android.R.id.content).getViewTreeObserver();
                if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    //noinspection deprecation
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                } else {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                }
                new CountDownTimer(2000, 2000){
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }
                }.start();
            }
        });
    }
}
