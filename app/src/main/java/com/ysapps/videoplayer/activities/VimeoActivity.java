package com.ysapps.videoplayer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.RecyclerAdapter;

public class VimeoActivity extends AppCompatActivity {
    public static final String STAFF_PICKS_VIDEO_URI = "/channels/927/videos"; // 927 == staffpicks

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("http://vimeo.com/");
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        String accessToken = getString(R.string.access_token);
        VimeoClient.initialize(new Configuration.Builder(accessToken).build());
        VimeoClient.getInstance().fetchNetworkContent(STAFF_PICKS_VIDEO_URI, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {
                // It's good practice to always make sure that the values the API sends us aren't null
                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(VimeoActivity.this));
                    recyclerView.setAdapter(new RecyclerAdapter(VimeoActivity.this, videoList));

                }
            }

            @Override
            public void failure(VimeoError error) {
                String errorMessage = error.getDeveloperMessage();
                Log.d("TAG", errorMessage);
            }
        });
    }
}
