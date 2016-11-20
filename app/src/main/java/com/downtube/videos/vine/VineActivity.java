package com.downtube.videos.vine;

import android.app.ListActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.downtube.videos.activities.MainActivity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by B.E.L on 20/11/2016.
 */

public class VineActivity extends ListActivity implements View.OnClickListener{

    private static final String SEARCH_QUERY = "#goat AND filter:vine";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tweets_list);
        final SearchTimeline searchTimeline = new SearchTimeline.Builder().query(SEARCH_QUERY).build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, searchTimeline) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = convertView;
                final Tweet tweet = getItem(position);
                View btnDownload;
                if (rowView == null) {
                    rowView = new CompactTweetViewCustom(context, tweet, R.style.tw__TweetDarkStyle);
                    btnDownload = rowView.findViewById(R.id.btn_download);
                    btnDownload.setOnClickListener(VineActivity.this);
                } else {
                    ((BaseTweetView) rowView).setTweet(tweet);
                    btnDownload = rowView.findViewById(R.id.btn_download);
                }
                btnDownload.setTag(tweet);
                return rowView;
            }
        };
        setListAdapter(adapter);
        getListView().setEmptyView(findViewById(R.id.loading));


    }

    @Override
    public void onClick(View v) {
        Tweet tweet = (Tweet)v.getTag();
        if (tweet != null) {
            String url, downloadUrl;
            String html = null;
            // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)
            if (tweet.entities.urls.size() == 0){
                return;
            }
            url = tweet.entities.urls.get(0).expandedUrl;

            if (url == null) {
                return;
            }
            new SearchVineTask().execute(url);
            Toast.makeText(this, tweet.toString(), Toast.LENGTH_LONG).show();
        }
    }

    class SearchVineTask extends AsyncTask<String, Void, List<VineVideo>> {

        @Override
        protected List<VineVideo> doInBackground(String... params) {

        String downloadUrl;
        String html = null;
        HashMap<String, VineVideo> urls = new HashMap<>();
            // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)

            // gets HTML from Vine URL
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
            Log.d("TAG", "downloading: " + MainActivity.pathId );
            MainActivity.downId = Utils.downloadFile(getBaseContext(), downloadUrl, MainActivity.pathId);
            if (urls.get(id) == null){
                Log.d("TAG", " url: " + params[0] );
                Log.d("TAG", "id: " +id + " . url: " + downloadUrl );
                Log.d("TAG", "thumb: " + VineUtil.parseThumbUrl(html));
                VineVideo vineVideo = new VineVideo();
                vineVideo.setId(id);
                vineVideo.setVinePageUrl(params[0]);
                vineVideo.setVineThumbUrl(VineUtil.parseThumbUrl(html));
                vineVideo.setVineStreamUrl(downloadUrl);
                urls.put(id, vineVideo);
            }
            return new ArrayList<>(urls.values());

        }

        @Override
        protected void onPostExecute(List<VineVideo> vineVideos) {

        }
    }

}
