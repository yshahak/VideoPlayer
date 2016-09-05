package com.ysapps.videoplayer.activities;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.adapters.CustomPagerAdapter;
import com.ysapps.videoplayer.fragments.DownloadedFragment;

import java.io.File;

import static com.ysapps.videoplayer.fragments.DownloadedFragment.SHOW_FOLDER_CONTENT;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ASK_EXTERNAL_PERMISSION = "keyExPermission";
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static long downId;
    public static String pathId;
    public ViewPager viewPager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        Log.d("path external", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        String url = "https://www.youtube.com/watch?v=_sLRnZmpvrc";
//        File file = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sLRnZmpvrc" + ".mp4");
//        VGet v = null;
//        try {
//            v = new VGet(new URL(url), file);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        assert v != null;
//        v.download();
    }

    @Override
    public void onBackPressed() {
        if (DownloadedFragment.state == SHOW_FOLDER_CONTENT){
            viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CODE_STORAGE_PERMISSION){
//            viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_STORAGE_PERMISSION){
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(KEY_ASK_EXTERNAL_PERMISSION, false).apply();
            viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (downloadId == downId){
                    File file = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pathId);
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{
                                    file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
                                }

                            });
                }
            }
        }
    };

}
