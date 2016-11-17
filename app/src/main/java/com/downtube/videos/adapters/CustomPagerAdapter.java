package com.downtube.videos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.downtube.videos.fragments.FragmentDownloaded;
import com.downtube.videos.fragments.FragmentMenu;
import com.downtube.videos.fragments.VimeoFragment;
import com.downtube.videos.fragments.Vinefragment;

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
                return FragmentMenu.newInstance(position);
            case 1:
                return Vinefragment.newInstance(position);
            case 2:
                return FragmentDownloaded.newInstance(position);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof FragmentDownloaded){ //refresh this fregment
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "MENU";
            case 1:
                return "VIDEO";
            case 2:
                return "DOWNLOADS";
        }
        return null;
    }
}
