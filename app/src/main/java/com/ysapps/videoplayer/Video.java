package com.ysapps.videoplayer;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by B.E.L on 04/09/2016.
 */

public class Video {
    private String videoName;
    private int videoId;
    private int videoLength;
    private Uri videoUri;

    public Video(String videoName, int videoId, int videoLength) {
        this.videoName = videoName;
        this.videoId = videoId;
        this.videoLength = videoLength;
        this.videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
        Log.d("TAG", Integer.toString(videoLength));
    }

    public String getVideoName() {
        return videoName;
    }

    public int getVideoId() {
        return videoId;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public Uri getVideoUri() {
        return videoUri;
    }
}
