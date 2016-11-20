package com.downtube.videos.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.downtube.videos.activities.MainActivity;
import com.downtube.videos.vine.CompactTweetViewCustom;
import com.downtube.videos.vine.VineUtil;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.io.IOException;

/**
 * Created by B.E.L on 17/11/2016.
 */

public class Vinefragment extends ListFragment implements View.OnClickListener {

    private String SEARCH_QUERY_PREFIX = "";
    private View loading;

    public static Fragment newInstance(int position) {
        return new Vinefragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runQuery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_tweets_list, container, false);
        loading = view.findViewById(R.id.loading);
        ((ListView)view.findViewById(android.R.id.list)).setEmptyView(loading);
        SearchView searchView = (SearchView)view.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SEARCH_QUERY_PREFIX = query;
                runQuery();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    private void runQuery(){
        String SEARCH_QUERY_ENDFIX = " AND filter:vine";
        final SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY_PREFIX +
                SEARCH_QUERY_ENDFIX).build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(getContext(), searchTimeline) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = convertView;
                final Tweet tweet = getItem(position);
                View btnDownload;
                if (rowView == null) {
                    rowView = new CompactTweetViewCustom(context, tweet, R.style.tw__TweetLightStyle);
                    btnDownload = rowView.findViewById(R.id.btn_download);
                    btnDownload.setOnClickListener(Vinefragment.this);
                } else {
                    ((BaseTweetView) rowView).setTweet(tweet);
                    btnDownload = rowView.findViewById(R.id.btn_download);
                }
                btnDownload.setTag(tweet);
                return rowView;
            }
        };
        setListAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        Tweet tweet = (Tweet)v.getTag();
        if (tweet != null) {
            String url;
            // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)
            if (tweet.entities.urls.size() == 0){
                return;
            }
            url = tweet.entities.urls.get(0).expandedUrl;

            if (url == null) {
                return;
            }
            new SearchVineTask().execute(url);
        }
    }

    class SearchVineTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            String downloadUrl;
            String html = null;

            try {
                html = VineUtil.sendGet(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (html == null) {
                return null;
            }
            // parses out the download URL
            downloadUrl = VineUtil.parseDownloadUrl(html);
            if (downloadUrl == null) {
                return null;
            }

            String id = Uri.parse(params[0]).getLastPathSegment();
            MainActivity.pathId = id  + ".mp4";
//            Log.d("TAG", "downloading: " + MainActivity.pathId );
            MainActivity.downId = Utils.downloadFile(getContext(), downloadUrl, MainActivity.pathId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loading.setVisibility(View.GONE);
        }
    }
}
