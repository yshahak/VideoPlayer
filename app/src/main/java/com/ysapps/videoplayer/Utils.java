package com.ysapps.videoplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class Utils {

    public static void getRootFolders(Context context) {


        Log.d("TAG", "getRootFolders is called");


        String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME};


        /*
        * uri :  The URI, using the content:// scheme
        * projection :  a list of which columns to return, Passing null will return all columns, which is inefficient
        * selection:  Filter declaring which rows to return, formateed as an SQL WHERE clause
        * selectionArgs:  You may include ?s in selection, which will be replaced by values from selectionArgs, in the order that they appear in the selection,
        * sortOrder: How to order the rows, formatted as an SQL ORDER BY clause
        * */


        Cursor vidCsr = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                null, null, MediaStore.Video.VideoColumns.DATE_ADDED);


        getMediaIdnName(context, vidCsr);


    }

    private static void getMediaIdnName(Context context, Cursor videoCsr ) {


        // getting Bucket ID and Display Name Column number for both Images and Videos

        int vidBucketIdCol = videoCsr.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID);
        int vidBucketNameCol = videoCsr.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);


        // Image and Video count as returned by Cursor
        SparseArray<String> nameMap = new SparseArray<>();
        SparseArray<Integer> counterMap = new SparseArray<>();
        if (videoCsr.moveToFirst()) {
            do {
                int vidBucketId = videoCsr.getInt(vidBucketIdCol);
                String vidBucketName = videoCsr.getString(vidBucketNameCol);
                nameMap.put(vidBucketId, vidBucketName);
                Integer count = counterMap.get(vidBucketId);
                count = (count == null) ? 1 : ++count;
                counterMap.put(vidBucketId, count);
                Log.d("TAG", "id: " + vidBucketId + ", bucketName: " + vidBucketName);
            } while (videoCsr.moveToNext());
        }
        videoCsr.close();
        getMediaInFolder(context, nameMap, counterMap);
    }

    private static void getMediaInFolder(Context context, SparseArray<String> map, SparseArray<Integer> counterMap) {
        final String[] VIDEO_PROJECTION = { MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        final String vselection = MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME + "=?";

        for (int i = 0 ; i < map.size() ; i++){
            Integer bucketId = map.keyAt(i);
            final String[] selectionArgs = { map.get(bucketId) };

            Cursor videocursor = context.getContentResolver().query(
                      MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    , VIDEO_PROJECTION
                    , vselection
                    , selectionArgs
                    , orderBy);

            if (videocursor != null && videocursor.moveToFirst()){
                int video_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
                int vdataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);
                int vdisplay_name_index = videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                int vduration_index = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION);
                do {
                    int thumbId = videocursor.getInt(video_column_index);
                    Log.d("TAG", "tumbId: " + thumbId);
                    String mediaData = videocursor.getString(vdataColumnIndex);
                    Log.d("TAG", "mediaData:" +mediaData);
                    String displayName = videocursor.getString(vdisplay_name_index);
                    Log.d("TAG","displayName: " +displayName);
                    String size = videocursor.getString(vduration_index);
                    Log.d("TAG", "size" + size);

                } while (videocursor.moveToNext());
                videocursor.close();
            }

            if (videocursor != null)
                videocursor.close();
        }








    }


}
