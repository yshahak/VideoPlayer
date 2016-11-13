package com.downtube.videos.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.downtube.videos.R;
import com.downtube.videos.adapters.CustomPagerAdapter;
import com.downtube.videos.fragments.FragmentDownloaded;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.lang.ref.WeakReference;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.downtube.videos.fragments.FragmentDownloaded.SHOW_FOLDERS_GRID;
import static com.downtube.videos.fragments.FragmentDownloaded.SHOW_FOLDER_CONTENT;
//import static com.downtube.videos.fragments.FragmentVimeoWebView.downloadEnabled;

public class MainActivity extends AppCompatActivity implements  ViewPager.OnPageChangeListener { //InterstitialAdListener,

    public static final String KEY_ASK_EXTERNAL_PERMISSION = "keyExPermission";
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static final int CODE_REQUEST_DELETE = 200;
    private static final String STARTAPP_ID = "208888194";
    private static final String FACEBOOK_PLACEMENT_EXIT = "1671180859802010_1671181033135326";


    public static long downId;
    public static String pathId;
    public ViewPager viewPager;
    private boolean fbBackPressed;
//    private InterstitialAd exitPressedInterstital;

    private ImageButton btnDownload;
    private WeakReference<WebView> webViewWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, STARTAPP_ID, true);
//        exitPressedInterstital = new InterstitialAd(MainActivity.this, FACEBOOK_PLACEMENT_EXIT);
//        exitPressedInterstital.setAdListener(MainActivity.this);
//        StartAppAd.showSplash(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);
        btnDownload = (ImageButton) toolbar.findViewById(R.id.btn_download_video);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

//        AdSettings.addTestDevice("8278fc45c192e519256cfe244e4e1f65");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!exitPressedInterstital.isAdLoaded()) {
//            exitPressedInterstital.loadAd();
//        }
        boolean permission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (permission) {
            if (viewPager.getAdapter() == null) {
                viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
                viewPager.setCurrentItem(1);
            }
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getString(R.string.permission_title))
                    .setMessage(getString(R.string.permission_message))
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
                            viewPager.setCurrentItem(1);
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        if (FragmentDownloaded.state == SHOW_FOLDER_CONTENT) {
            FragmentDownloaded.state = SHOW_FOLDERS_GRID;
            viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
        } else if (viewPager.getCurrentItem() == 1 && webViewWeakReference != null && webViewWeakReference.get() != null && webViewWeakReference.get().canGoBack()){
            webViewWeakReference.get().goBack();
            btnDownload.setEnabled(false);
        }
        else {
//            if (exitPressedInterstital.isAdLoaded()){
//                Log.d("TAG", "facebook shown BACK");
//                exitPressedInterstital.show();
//                fbBackPressed = true;
//            } else {
                Log.d("TAG", "startApp shown BACK");
                StartAppAd.onBackPressed(this);
                super.onBackPressed();
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (exitPressedInterstital != null) {
//            exitPressedInterstital.destroy();
//        }

        unregisterReceiver(receiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_DELETE) {
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
        if (requestCode == CODE_STORAGE_PERMISSION) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(KEY_ASK_EXTERNAL_PERMISSION, false).apply();
            if (viewPager.getAdapter() != null) {
                viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
            } else {
                viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
                viewPager.setCurrentItem(1);
            }
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (downloadId == downId) {
                    Log.d("TAG", "reciever got the doownload complete");
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pathId);
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{
                                    file.getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    if (uri != null) {
                                        Log.d("TAG", "onScanCompleted: " + uri.toString());
                                    }
                                    if (viewPager != null) {
                                        viewPager.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (viewPager.getAdapter() != null) {
                                                    viewPager.getAdapter().notifyDataSetChanged(); //it will refresh second fragment
                                                } else {
                                                    viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
                                                    viewPager.setCurrentItem(1);
                                                }
                                            }
                                        });
                                    }

                                }

                            });
                }
            }
        }
    };


//
//    @Override
//    public void onInterstitialDisplayed(Ad ad) {
//
//    }
//
//    @Override
//    public void onInterstitialDismissed(Ad ad) {
//        if (fbBackPressed){
//            Log.d("TAG", "facebook onInterstitialDismissed");
//            fbBackPressed = false;
//            if (!isFinishing()){
//                finish();
//            }
//        }
//    }
//
//    @Override
//    public void onError(Ad ad, AdError adError) {
//        Log.d("TAG", "facebook onError");
//    }
//
//    @Override
//    public void onAdLoaded(Ad ad) {
//
//    }
//
//    @Override
//    public void onAdClicked(Ad ad) {
//        if (fbBackPressed){
//            fbBackPressed = false;
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btnDownload.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void storeWebViewRefernce(WebView fragmentVimeoWebView) {
        webViewWeakReference = new WeakReference<>(fragmentVimeoWebView);
    }
}