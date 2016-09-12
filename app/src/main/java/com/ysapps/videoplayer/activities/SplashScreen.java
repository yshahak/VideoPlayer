package com.ysapps.videoplayer.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ysapps.videoplayer.R;

import static com.ysapps.videoplayer.activities.MainActivity.CODE_STORAGE_PERMISSION;

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
                new CountDownTimer(1000, 1000){
                    @Override
                    public void onTick(long l) {
                    }
                    @Override
                    public void onFinish() {
                        boolean permission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (permission) {
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        } else {
                            new AlertDialog.Builder(SplashScreen.this)
                                    .setTitle(getString(R.string.permission_title))
                                    .setMessage(getString(R.string.permission_message))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
                                        }
                                    })
                                    .show();
                        }
                    }
                }.start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }
}
