package com.downtube.videos.vine;

/**
 * Created by B.E.L on 16/11/2016.
 */

import java.util.HashSet;

// interface
public interface ProcessingListener {
    public void onProcessFinished(int bufferId, HashSet<String> urls, int numScraped);
}