package com.downtube.videos.vine;

/**
 * Created by B.E.L on 16/11/2016.
 */

public class ProcessingThread {//implements Runnable {

//    private HashSet<Status> tweets;
//    private int bufferId;
//    private HashSet<String> urls;
//    private int numVinesScraped = 0;
//    private File saveDirectory;
//    private ProcessingListener listener;
//    private Context context;
//
//    // constructor
//    public ProcessingThread(TweetBuffer buffer, HashSet<String> urls,
//                            ProcessingListener listener, Context context) {
//
//        this.tweets = new HashSet<>(buffer.getTweets());
//        this.bufferId = buffer.getId();
//        this.saveDirectory = buffer.getSaveDirectory();
//        this.urls = urls;
//        this.listener = listener;
//        this.context = context;
//    }
//
//    @Override
//    public void run() {
//
//        String url, downloadUrl;
//        String html = null;
//
//        for (Status status : tweets) {
//
//            // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)
//            url = VineUtil.findVineUrl(status);
//            if (url == null) {
//                continue;
//            }
//
//            // checks if Vine URL is duplicate
//            if (urls.contains(url)) {
//                continue;
//            } else {
//                synchronized (this) {
//                    urls.add(url);
//                }
//            }
//
//            // gets HTML from Vine URL
//            try {
//                html = VineUtil.sendGet(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (html == null) {
//                continue;
//            }
//
//            // parses out the download URL
//            downloadUrl = VineUtil.parseDownloadUrl(html);
//            if (downloadUrl == null) {
//                continue;
//            }
//
//            // downloads the .mp4 video
////            if (VineUtil.downloadVine(saveDirectory, status.getId(),
////                    downloadUrl)) {
//            MainActivity.pathId = status.getId()  + ".mp4";
//            Log.d("TAG", "downloading: " + status.getText());
//            MainActivity.downId = Utils.downloadFile(context, downloadUrl, status.getId()  + ".mp4");
//                synchronized (this) {
//                    numVinesScraped++;
////                }
//            }
//        }
//
//        // invokes finished listener
//        listener.onProcessFinished(bufferId, urls, numVinesScraped);
//    }
//
//    // getters
//    public int getNumVinesScraped() {
//        return numVinesScraped;
//    }
//
//    public HashSet<String> getUrls() {
//        return urls;
//    }
}