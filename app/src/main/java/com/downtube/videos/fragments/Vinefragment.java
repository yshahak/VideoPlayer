package com.downtube.videos.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.downtube.videos.DividerItemDecoration;
import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.downtube.videos.adapters.RecyclerAdapterVine;
import com.downtube.videos.vine.TwitterStreamBuilderUtil;
import com.downtube.videos.vine.VineUtil;
import com.downtube.videos.vine.VineVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Created by B.E.L on 17/11/2016.
 */

public class Vinefragment extends Fragment implements View.OnClickListener {

    private static final String DATA_SAVED = "dataSaved";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View noInternetContainer;

    public static Fragment newInstance(int position) {
        return new Vinefragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vimeo, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        progressBar = (ProgressBar)view.findViewById(R.id.progress_indicator);
        SearchView mSearchView = (SearchView) view.findViewById(R.id.searchview);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchVineTask().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }

        });
        noInternetContainer = view.findViewById(R.id.no_internet_container);
        noInternetContainer.setOnClickListener(this);
        if (savedInstanceState == null) {
            setUiState();
        }
        return view;
    }

    private void setUiState() {
        boolean connected = Utils.isNetworkAvailable(getContext());
        noInternetContainer.setVisibility(connected ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(connected ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(connected ? View.VISIBLE : View.GONE);
        if (connected) {
            new SearchVineTask().execute("goat");
        }
    }


    private void setRecyclerViewState(List<VineVideo> list){
        progressBar.setVisibility(View.GONE);
        RecyclerAdapterVine adapterVine = (RecyclerAdapterVine) recyclerView.getAdapter();
        if (adapterVine != null) {
            adapterVine.setVideoList(list);
        } else {
            recyclerView.setAdapter(new RecyclerAdapterVine(getActivity(), list));
        }
    }

    @Override
    public void onClick(View view) {
        setUiState();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_SAVED, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    class SearchVineTask extends AsyncTask<String, Void, List<VineVideo>>{

        @Override
        protected List<VineVideo> doInBackground(String... params) {
            try {
                TwitterFactory tf = new TwitterFactory(TwitterStreamBuilderUtil.getBuilder().build());
                Twitter twitter = tf.getInstance();
                Query query = new Query(params[0] + " filter:vine");

                QueryResult result = twitter.search(query);
                List<twitter4j.Status> tweets = result.getTweets();
                String url, downloadUrl;
                String html = null;
                HashMap<String, VineVideo> urls = new HashMap<>();
                for (twitter4j.Status status : tweets) {
                    // gets the Vine URL (eg: https://vine.co/v/OW0ei1Uauxv)
                    url = VineUtil.findVineUrl(status);

                    if (url == null) {
                        continue;
                    }
                    // gets HTML from Vine URL
                    try {
                        html = VineUtil.sendGet(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (html == null) {
                        continue;
                    }
                    // parses out the download URL
                    downloadUrl = VineUtil.parseDownloadUrl(html);
                    if (downloadUrl == null) {
                        continue;
                    }
                    String id = Uri.parse(url).getLastPathSegment();

                    if (urls.get(id) == null){
                        Log.d("TAG", " url: " + url );
                        Log.d("TAG", "id: " +id + " . url: " + downloadUrl );
                        Log.d("TAG", "thumb: " + VineUtil.parseThumbUrl(html));
                        VineVideo vineVideo = new VineVideo();
                        vineVideo.setId(id);
                        vineVideo.setVinePageUrl(url);
                        vineVideo.setVineThumbUrl(VineUtil.parseThumbUrl(html));
                        vineVideo.setVineStreamUrl(downloadUrl);
                        urls.put(id, vineVideo);
                    }
                    return new ArrayList<>(urls.values());
                }

            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<VineVideo> vineVideos) {
            setRecyclerViewState(vineVideos);
        }
    }

}
