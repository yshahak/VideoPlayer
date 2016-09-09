package com.ysapps.videoplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.VideoList;
import com.vimeo.networking.model.error.VimeoError;
import com.ysapps.videoplayer.AndroidGsonDeserializer;
import com.ysapps.videoplayer.CustomSearchView;
import com.ysapps.videoplayer.R;
import com.ysapps.videoplayer.Utils;
import com.ysapps.videoplayer.adapters.RecyclerAdapterVimeo;

import java.lang.ref.WeakReference;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class VimeoFragment extends Fragment implements View.OnClickListener {

    public static final String STAFF_PICKS_VIDEO_URI = "/channels/927/videos"; // 927 == staffpicks
    public static final String SEARCH_VIDEO_URI = "videos?query=";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private retrofit2.Call<java.lang.Object> currentCall;

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private CustomSearchView mSearchView;
    private ProgressBar progressBar;
    private View noInternetContainer;

    public static VimeoFragment newInstance(int page){
        return new VimeoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vimeo, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar)view.findViewById(R.id.progress_indicator);
        mSearchView = (CustomSearchView) view.findViewById(R.id.searchview);
        mSearchView.setWeakReference(new WeakReference<>(this));
        noInternetContainer = view.findViewById(R.id.no_internet_container);
        noInternetContainer.setOnClickListener(this);
        setUi();
        return view;
    }

    private void setUi() {
        boolean connected = Utils.isNetworkAvailable(getContext());
        noInternetContainer.setVisibility(connected ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(connected ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(connected ? View.VISIBLE : View.GONE);
        if (connected) {
            authenticateWithClientCredentials();
        }
    }

    private void authenticateWithClientCredentials() {
//        String accessToken = getString(R.string.access_token);
//        VimeoClient.initialize(new Configuration.Builder(accessToken).build());
        try {
            VimeoClient.getInstance();
            setVimeoList(STAFF_PICKS_VIDEO_URI);
        } catch (AssertionError e) {
            Log.d("TAG", "vimeoClient is null");
            Configuration.Builder configBuilder =
                    new Configuration.Builder(
                            "a9f0a34c28db401317819037c9c8ae3601b011f4",
                            "C+bMDFPFNzsNTYnt9lkwUu1dQKxnLGg/imGiSjlrEbcj4mwZ0ZoDWz7HAw6bA3UlAm14/ud7YemwGD2mz66IqJJOrfUbtFGIvgpHWVsVDlop8yCE8yML2yPXI1b6RUKd",
                            "public private",
                            null,
                            new AndroidGsonDeserializer()
                    );
            VimeoClient.initialize(configBuilder.build());
            VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
                @Override
                public void success() {
//                    String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                    setVimeoList(STAFF_PICKS_VIDEO_URI);

                }

                @Override
                public void failure(VimeoError error) {
                    String errorMessage = error.getDeveloperMessage();
                    Log.d("TAG", errorMessage);
                }
            });
        }

    }

    public void setVimeoList(String query){
        progressBar.setVisibility(View.VISIBLE);
        RecyclerAdapterVimeo adapterVimeo = (RecyclerAdapterVimeo) recyclerView.getAdapter();
        if (adapterVimeo != null) {
            adapterVimeo.setVideoList(null);
        } else {
            recyclerView.setAdapter(new RecyclerAdapterVimeo(getActivity(), null));
        }
        if (currentCall != null) {
            currentCall.cancel();
        }
        currentCall = VimeoClient.getInstance().fetchNetworkContent(query, new ModelCallback<VideoList>(VideoList.class) {
            @Override
            public void success(VideoList videoList) {
                currentCall = null;
                if (videoList != null && videoList.data != null && !videoList.data.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    RecyclerAdapterVimeo adapterVimeo = (RecyclerAdapterVimeo) recyclerView.getAdapter();
                    if (adapterVimeo != null) {
                        adapterVimeo.setVideoList(videoList);
                    } else {
                        recyclerView.setAdapter(new RecyclerAdapterVimeo(getActivity(), videoList));
                    }

                }
            }

            @Override
            public void failure(VimeoError error) {
                currentCall = null;
                String errorMessage = error.getDeveloperMessage();
                Log.d("TAG", "failure:" + errorMessage);
            }
        });
    }

    @Override
    public void onClick(View view) {
        setUi();
    }
}


