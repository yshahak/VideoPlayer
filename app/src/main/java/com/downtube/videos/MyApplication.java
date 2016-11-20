package com.downtube.videos;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by B.E.L on 11/09/2016.
 */

public class MyApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Y1ekJsWXUFwP9FhxsAcQAsw65";
    private static final String TWITTER_SECRET = "QK8x0D68PArBk3NaYOBATz5dUnIxaPUwOD9b8j56b60r96HOVk";
    private Twitter twitter;


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        twitter = new Twitter(authConfig);
        Fabric.with(this, new Crashlytics(), twitter);
//        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        final OkHttpClient customClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor).build();
//
//        final TwitterSession activeSession = TwitterCore.getInstance()
//                .getSessionManager().getActiveSession();
//
//        final TwitterApiClient customApiClient;
//        if (activeSession != null) {
//            customApiClient = new TwitterApiClient(activeSession);
//            TwitterCore.getInstance().addApiClient(activeSession, customApiClient);
//        } else {
//            customApiClient = new TwitterApiClient(customClient);
//            TwitterCore.getInstance().addGuestApiClient(customApiClient);
//        }
        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .withContinueSessionMillis(5000L)
                .withCaptureUncaughtExceptions(false)
                .withPulseEnabled(true)
                .build(this, "3NBGWRBH46XN83F2B4GZ");
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/myriadprocond.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }





}


