package com.ysapps.videoplayer.activities;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.adapters.CustomPagerAdapter;
import com.ysapps.videoplayer.fragments.FragmentDownloaded;

import java.io.File;

import static com.ysapps.videoplayer.fragments.FragmentDownloaded.SHOW_FOLDERS_GRID;
import static com.ysapps.videoplayer.fragments.FragmentDownloaded.SHOW_FOLDER_CONTENT;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ASK_EXTERNAL_PERMISSION = "keyExPermission";
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static final int CODE_REQUEST_DELETE = 200;

    public static long downId;
    public static String pathId;
    public ViewPager viewPager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/myriadprocond.otf");
        TextView myTextView = (TextView)findViewById(R.id.tool_bar_title);
        myTextView.setTypeface(myTypeface);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Log.d("path external", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onBackPressed() {
        if (FragmentDownloaded.state == SHOW_FOLDER_CONTENT){
            FragmentDownloaded.state = SHOW_FOLDERS_GRID;
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
        if (requestCode == CODE_REQUEST_DELETE  ){
            if (resultCode == RESULT_OK) {
                viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
            } else {
                FragmentDownloaded.removedVideo = null;
            }
        }
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
                    Log.d("TAG", "reciever got the doownload complete");
                    File file = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pathId);
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{
                                    file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.d("TAG", "onScanCompleted: " + uri.toString());
                                    viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
                                }

                            });
                }
            }
        }
    };

}
