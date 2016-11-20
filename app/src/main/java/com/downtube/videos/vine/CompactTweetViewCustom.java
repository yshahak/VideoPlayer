package com.downtube.videos.vine;

import android.content.Context;
import android.util.AttributeSet;

import com.downtube.videos.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

/**
 * Created by B.E.L on 20/11/2016.
 */

public class CompactTweetViewCustom extends CompactTweetView {


    public CompactTweetViewCustom(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public CompactTweetViewCustom(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    public CompactTweetViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompactTweetViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayout() {
        return R.layout.custom_tweet_compact;
    }


}
