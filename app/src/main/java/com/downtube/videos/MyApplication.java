package com.downtube.videos;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import io.fabric.sdk.android.Fabric;

/**
 * Created by B.E.L on 11/09/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .withContinueSessionMillis(5000L)
                .withCaptureUncaughtExceptions(false)
                .withPulseEnabled(true)
                .build(this, "3NBGWRBH46XN83F2B4GZ");
    }


}


