package com.ysapps.videoplayer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Folder> getRootFolders(Context context) {


        Log.d("TAG", "getRootFolders is called");


        String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME};

        /*
        * uri :  The URI, using the content:// scheme
        * projection :  a list of which columns to return, Passing null will return all columns, which is inefficient
        * selection:  Filter declaring which rows to return, formateed as an SQL WHERE clause
        * selectionArgs:  You may include ?s in selection, which will be replaced by values from selectionArgs, in the order that they appear in the selection,
        * sortOrder: How to order the rows, formatted as an SQL ORDER BY clause
        * */


        ArrayList<Folder> folders = new ArrayList<>();
        Cursor vidCsr = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                null, null, null);//MediaStore.Video.VideoColumns.DATE_ADDED
        getMediaIdnName(folders, vidCsr);
        Log.d("TAG", "video folders loading finished");
        return folders;

    }

    private static ArrayList<Folder> getMediaIdnName(ArrayList<Folder> folders, Cursor videoCsr) {
        // getting Bucket ID and Display Name Column number for both Images and Videos

        int colId = 0;
        int colData = 1;
        int colDisplayName = 2;
        int colDuration = 3;
        int colBucketId = 4;
        int colBucketDisplayName = 5;

        // Image and Video count as returned by Cursor
        SparseArray<Folder> folderMap = new SparseArray<>();
        if (videoCsr.moveToFirst()) {
            do {
                Video video = new Video(
                        videoCsr.getString(colDisplayName),
                        videoCsr.getInt(colId),
                        videoCsr.getInt(colDuration),
                        videoCsr.getString(colData));
                int vidBucketId = videoCsr.getInt(colBucketId);
                String vidBucketName = videoCsr.getString(colBucketDisplayName);
                Folder folder = folderMap.get(vidBucketId);
                if (folder == null) {
                    folder = new Folder(vidBucketName, vidBucketId);
                    folderMap.put(vidBucketId, folder);
                }
                folder.getVideos().add(video);

            } while (videoCsr.moveToNext());
        }
        videoCsr.close();
        for (int i = 0; i < folderMap.size(); i++) {
            folders.add(folderMap.valueAt(i));
        }
//        getMediaInFolder(context, folderMap, counterMap);
        return folders;
    }

    private static void getMediaInFolder(Context context, SparseArray<String> map, SparseArray<Integer> counterMap) {
        final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        final String vselection = MediaStore.Video.VideoColumns.BUCKET_ID + "=?";

        for (int i = 0; i < map.size(); i++) {
            Integer bucketId = map.keyAt(i);
            final String[] selectionArgs = {bucketId.toString()};

            Cursor videocursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , VIDEO_PROJECTION
                    , vselection
                    , selectionArgs
                    , orderBy);

            if (videocursor != null && videocursor.moveToFirst()) {
                int video_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
                int vdataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);
                int vdisplay_name_index = videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int vduration_index = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                do {
                    int thumbId = videocursor.getInt(video_column_index);
                    Log.d("TAG", "tumbId: " + thumbId);
                    String mediaData = videocursor.getString(vdataColumnIndex);
                    Log.d("TAG", "mediaData:" + mediaData);
                    String displayName = videocursor.getString(vdisplay_name_index);
                    Log.d("TAG", "displayName: " + displayName);
                    String size = videocursor.getString(vduration_index);
                    Log.d("TAG", "size" + size);

                } while (videocursor.moveToNext());
                videocursor.close();
            }

            if (videocursor != null)
                videocursor.close();
        }

    }

    public static long downloadFile(Context context, String url, String fileName) {
        Log.d("TAG", "downloading: " + fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading from server");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.allowScanningByMediaScanner();
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    public static boolean deleteFile(String path) {
        File videofiles = new File(path);
        return videofiles.delete();
    }


}
