package com.ysapps.videoplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ysapps.videoplayer.fragments.DownloadedFragment;
import com.ysapps.videoplayer.fragments.VimeoFragment;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return VimeoFragment.newInstance(position);
            case 1:
                return DownloadedFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Vimeo";
            case 1:
                return "Downloads";
        }
        return null;
    }
}
