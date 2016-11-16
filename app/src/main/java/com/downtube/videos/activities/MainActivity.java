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

import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.downtube.videos.adapters.CustomPagerAdapter;
import com.downtube.videos.fragments.FragmentDownloaded;
import com.downtube.videos.vine.ProcessingListener;
import com.downtube.videos.vine.ProcessingThread;
import com.downtube.videos.vine.TweetBuffer;
import com.downtube.videos.vine.TwitterStreamBuilderUtil;
import com.downtube.videos.vine.VineUtil;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.downtube.videos.fragments.FragmentDownloaded.SHOW_FOLDERS_GRID;
import static com.downtube.videos.fragments.FragmentDownloaded.SHOW_FOLDER_CONTENT;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ASK_EXTERNAL_PERMISSION = "keyExPermission";
    public final static int CODE_STORAGE_PERMISSION = 100;
    public static final int CODE_REQUEST_DELETE = 200;
    private static final String STARTAPP_ID = "208888194";
    private static final String FACEBOOK_PLACEMENT_EXIT = "1671180859802010_1671181033135326";


    public static long downId;
    public static String pathId;
    public ViewPager viewPager;
    private boolean fbBackPressed;

    private static final int NUM_VINES_TO_DOWNLOAD = -1;
    private static final int MIN_BUFFER_SIZE = 10;

    private static TwitterStream twitter;
    private static int numVinesScraped = 0;
    private static final String SAVE_DIRECTORY = "vines/";

    private static TweetBuffer buffer1, buffer2;
    private static int currentBufferId;
    private static HashSet<String> duplicateUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, STARTAPP_ID, true);
        StartAppAd.showSplash(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        Connects to the Streaming API
        TwitterStream twitter = TwitterStreamBuilderUtil.getStream();

        // sets keyword to track
        FilterQuery fq = new FilterQuery();
        String keyword[] = { "http" };
        fq.track(keyword);

        // instantiates buffers
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), SAVE_DIRECTORY);
        buffer1 = new TweetBuffer(1, file);
        buffer2 = new TweetBuffer(2, file);
        currentBufferId = 1;

        duplicateUrls = new HashSet<>();

        // instantiates listener to fill and process the buffers
        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {

                if ((currentBufferId == 1) && !buffer1.isProcessing()) {
                    buffer1.addStatus(status);
                    return;
                }

                if ((currentBufferId == 2) && !buffer2.isProcessing()) {
                    buffer2.addStatus(status);
                    return;
                }
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                processCurrentBuffer(MainActivity.this); // see Step 4
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice deletionNotice) {
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                System.out.println(arg0.getMessage());
            }
        };

//        // starts scraping Vine videos
//        twitter.addListener(listener);
//        twitter.filter(fq);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // sets keyword to track
                    FilterQuery fq = new FilterQuery();
                    String keyword[] = {"nba"};
                    fq.track(keyword);

                    TwitterFactory tf = new TwitterFactory(TwitterStreamBuilderUtil.getBuilder().build());
                    Twitter twitter = tf.getInstance();
                    Query query = new Query("dunk vine");
                    QueryResult result;
                    result = twitter.search(query);
                    List<Status> tweets = result.getTweets();
                    String url, downloadUrl;
                    String html = null;

                    for (Status status : tweets) {
                        Log.d("TAG", "@" + status.getUser().getScreenName() + " - " + status.getText());
                        // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)
                        url = VineUtil.findVineUrl(status);
                        if (url == null) {
                            continue;
                        }
                        // gets HTML from Vine URL
                        try {
                            html = VineUtil.sendGet(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (html == null) {
                            continue;
                        }

                        // parses out the download URL
                        downloadUrl = VineUtil.parseDownloadUrl(html);
                        if (downloadUrl == null) {
                            continue;
                        }

                        // downloads the .mp4 video
//            if (VineUtil.downloadVine(saveDirectory, status.getId(),
//                    downloadUrl)) {
                        MainActivity.pathId = status.getId()  + ".mp4";
                        Log.d("TAG", "downloading: " + status.getText());
                        MainActivity.downId = Utils.downloadFile(MainActivity.this, downloadUrl, status.getId()  + ".mp4");
                    }

                } catch (TwitterException te) {
                    te.printStackTrace();
                    System.out.println("Failed to search tweets: " + te.getMessage());
                }
            }
        }).start();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        } else {
            Log.d("TAG", "startApp shown BACK");
            StartAppAd.onBackPressed(this);
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

    private static void processCurrentBuffer(Context context) {

        // Does nothing if the current buffer is already processing or
        // if it contains fewer elements than the minimum buffer size.
        if ((currentBufferId == 1 && (buffer1.isProcessing() || buffer1
                .getTweets().size() < MIN_BUFFER_SIZE))
                || (currentBufferId == 2 && (buffer2.isProcessing() || buffer2
                .getTweets().size() < MIN_BUFFER_SIZE))) {
            return;
        }

        // Otherwise, begin processing the current buffer.
        try {
            switch (currentBufferId) {
                case 1:
                    buffer1.setProcessing(true);
                    currentBufferId = 2;

                    Thread t1 = new Thread(new ProcessingThread(buffer1,
                            duplicateUrls, finishListener, context));
                    t1.start();
                    break;

                case 2:
                    buffer2.setProcessing(true);
                    currentBufferId = 1;

                    Thread t2 = new Thread(new ProcessingThread(buffer2,
                            duplicateUrls, finishListener, context));
                    t2.start();
                    break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static ProcessingListener finishListener = new ProcessingListener() {

        @Override
        public void onProcessFinished(int bufferId, HashSet<String> urls,
                                      int numScraped) {

            synchronized (this) {
                duplicateUrls = urls;
            }

            numVinesScraped += numScraped;
            if (numScraped != 0) {
                System.out.println("Scraped " + numScraped
                        + " vine(s) from buffer" + bufferId
                        + ".  Total vines is now " + numVinesScraped + ".");
            }

            switch (bufferId) {
                case 1:
                    buffer1.clearBuffer();
                    buffer1.setProcessing(false);
                    break;
                case 2:
                    buffer2.clearBuffer();
                    buffer2.setProcessing(false);
                    break;
            }

            // stop scraping when threshold is reached
            if (numVinesScraped >= NUM_VINES_TO_DOWNLOAD
                    && (NUM_VINES_TO_DOWNLOAD != -1)) {
                twitter.cleanUp();
                twitter.shutdown();
            }
        }
    };


}
