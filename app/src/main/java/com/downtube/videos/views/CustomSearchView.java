package com.downtube.videos.views;

import android.app.Service;
import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.downtube.videos.fragments.VimeoFragment;

import java.lang.ref.WeakReference;

import static com.downtube.videos.fragments.VimeoFragment.SEARCH_VIDEO_URI;
import static com.downtube.videos.fragments.VimeoFragment.STAFF_PICKS_VIDEO_URI;

/**
 * Created by B.E.L on 05/09/2016.
 */

public class CustomSearchView extends SearchView {
    WeakReference<VimeoFragment> weakReference;

    public SearchView.SearchAutoComplete mSearchAutoComplete;

    public CustomSearchView(Context context) {
        super(context);
        initialize();
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void initialize() {
        mSearchAutoComplete = (SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
        this.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG", newText);
                openSearchPage(newText);
                return false;
            }

        });

    }


    @Override
    public void setSuggestionsAdapter(CursorAdapter adapter) {
        // don't let anyone touch this
    }


    private void openSearchPage(String query) {
        if (this.weakReference != null) {
            VimeoFragment vimeoFragment = this.weakReference.get();
            if (vimeoFragment != null) {
                vimeoFragment.setVimeoList( (query.length() > 0) ? SEARCH_VIDEO_URI + query : STAFF_PICKS_VIDEO_URI);
            }
        }
    }

    public void setWeakReference(WeakReference<VimeoFragment> weakReference) {
        this.weakReference = weakReference;
    }
}