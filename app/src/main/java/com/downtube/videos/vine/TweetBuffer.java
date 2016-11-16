package com.downtube.videos.vine;

/**
 * Created by B.E.L on 16/11/2016.
 */

import java.io.File;
import java.util.HashSet;

import twitter4j.Status;

public class TweetBuffer {

    private int id;
    private File saveDirectory;

    private HashSet<Status> tweets;
    private boolean isProcessing;

    // constructor
    public TweetBuffer(int id, File saveDirectory) {
        this.id = id;
        this.saveDirectory = saveDirectory;

        tweets = new HashSet<>();
        isProcessing = false;
    }

    // public methods
    public void addStatus(Status status) {
        tweets.add(status);
    }

    public void clearBuffer() {
        tweets.clear();
    }

    // getters
    public int getId() {
        return id;
    }

    public File getSaveDirectory() {
        return saveDirectory;
    }

    public HashSet<Status> getTweets() {
        return tweets;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    // setters
    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }
}