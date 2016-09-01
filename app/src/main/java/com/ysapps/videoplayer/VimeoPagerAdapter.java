package com.ysapps.videoplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ysapps.videoplayer.fragments.VimeoFragment;

/**
 * Created by B.E.L on 01/09/2016.
 */

public class VimeoPagerAdapter extends FragmentPagerAdapter {

    public VimeoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return VimeoFragment.newInstance(position);
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
        }
        return null;
    }
}
