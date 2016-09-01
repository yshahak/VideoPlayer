package com.ysapps.videoplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.RecyclerAdapter;

import static com.ysapps.videoplayer.activities.VimeoActivity.STAFF_PICKS_VIDEO_URI;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class VimeoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static VimeoFragment newInstance(int page){
        return new VimeoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vimeo, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        String accessToken = getString(R.string.access_token);
        VimeoClient.initialize(new Configuration.Builder(accessToken).build());
        VimeoClient.getInstance().fetchNetworkContent(STAFF_PICKS_VIDEO_URI, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {
                // It's good practice to always make sure that the values the API sends us aren't null
                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new RecyclerAdapter(getActivity(), videoList));

                }
            }

            @Override
            public void failure(VimeoError error) {
                String errorMessage = error.getDeveloperMessage();
                Log.d("TAG", errorMessage);
            }
        });
        return view;
    }
}


