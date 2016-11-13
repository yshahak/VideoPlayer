package com.downtube.videos.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.downtube.videos.R;

/**
 * Created by B.E.L on 10/10/2016.
 */

public class FragmentWebView extends Fragment{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        WebView mWebView = (WebView) inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://vuclip.com");
        return mWebView;
    }


}
