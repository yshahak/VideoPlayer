package com.downtube.videos.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.downtube.videos.R;
import com.downtube.videos.activities.DownloadDialogActivity;
import com.downtube.videos.activities.MainActivity;

import java.util.List;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;

/**
 * Created by B.E.L on 10/10/2016.
 */

public class FragmentVimeoWebView extends Fragment implements View.OnClickListener {

    private static final String DATA_SAVED = "dataSaved";
    private static boolean active;
    private ImageButton btnDownload;
    private WebView mWebView;
//    public static boolean downloadEnabled;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWebView = (WebView) inflater.inflate(R.layout.fragment_web_view, container, false);
        btnDownload = (ImageButton) getActivity().findViewById(R.id.btn_download_video);
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (savedInstanceState != null && savedInstanceState.getString(DATA_SAVED) != null){
            mWebView.loadUrl(savedInstanceState.getString(DATA_SAVED));
        } else {
            mWebView.loadUrl("https://vimeo.com");
            btnDownload.setEnabled(false);
        }
        mWebView.setWebViewClient(new MyWebClient());
        btnDownload.setOnClickListener(this);
        ((MainActivity)getActivity()).storeWebViewRefernce(mWebView);
        btnDownload.setVisibility(View.VISIBLE);
        return mWebView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.destroy();
        btnDownload.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
        outState.putString(DATA_SAVED, mWebView.getUrl());
    }


    @Override
    public void onClick(View view) {
//        if (downloadEnabled) {
            VimeoVideo video = (VimeoVideo) view.getTag();
            String[] arrayKeys = video.getStreams().keySet().toArray(new String[video.getStreams().keySet().size()]);
            String[] arrayValues = video.getStreams().values().toArray(new String[video.getStreams().keySet().size()]);

            Intent intent = new Intent(getContext(), DownloadDialogActivity.class);
            intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_KEYS, arrayKeys);
            intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_LINKS, arrayValues);

            intent.putExtra(DownloadDialogActivity.EXTRA_VIDEO_TITLE, video.getTitle().replaceAll("[.]", ""));
            getContext().startActivity(intent);
//        } else {
//            Toast.makeText(getContext(), "You need to play or click on video first", Toast.LENGTH_SHORT).show();
//        }
    }

    class MyWebClient extends WebViewClient{

        private String lastUrl;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url != null && Uri.parse(url).getLastPathSegment() != null ) {
                active = !checkString(Uri.parse(url).getLastPathSegment());
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (url.equals("https://player.vimeo.com/log/play") || url.equals("https://player.vimeo.com/log/partial")){
                active = false;
                List<String> list = Uri.parse(lastUrl).getPathSegments();
                if (list.size() > 0) {
                    checkString(list.get(0));
                }
            } else {
                lastUrl = url;
            }
            if (active){
                return;
            }
            List<String> list = Uri.parse(url).getPathSegments();
            int index = list.indexOf("video");
            if (index > 0) {
                checkString(list.get(index - 1));
            }
        }

        private boolean checkString(String id){
            if (id.matches("[0-9]{9}")){
                active = true;
//                downloadEnabled = false;
                btnDownload.setEnabled(false);
                loadVimeoId(id);
                return true;
            }
            return false;
        }

    }

    private void loadVimeoId(String id){
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                Log.d("TAG", "onSuccess: " + video.getTitle());
                btnDownload.setTag(video);
                btnDownload.post(new Runnable() {
                    @Override
                    public void run() {
//                        downloadEnabled = true;
                        btnDownload.setEnabled(true);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                //Error handling here
            }
        });
    }
}
