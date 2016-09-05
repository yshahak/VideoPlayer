package com.ysapps.videoplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.adapters.CustomPagerAdapter;
import com.ysapps.videoplayer.fragments.DownloadedFragment;

import static com.ysapps.videoplayer.fragments.DownloadedFragment.SHOW_FOLDER_CONTENT;

public class MainActivity extends AppCompatActivity {
    public final static int CODE_STORAGE_PERMISSION = 100;
    private ViewPager viewPager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_STORAGE_PERMISSION && resultCode == RESULT_OK){
            viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
        }
    }
}
