package com.ysapps.videoplayer.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.adapters.CustomPagerAdapter;
import com.ysapps.videoplayer.fragments.DownloadedFragment;

import static com.ysapps.videoplayer.fragments.DownloadedFragment.SHOW_FOLDER_CONTENT;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("http://vimeo.com/");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onBackPressed() {
        if (DownloadedFragment.state == SHOW_FOLDER_CONTENT){
            viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
            viewPager.setCurrentItem(1);
        } else {
            super.onBackPressed();
        }
    }
}
